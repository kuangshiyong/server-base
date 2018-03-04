package cn.bjzfgcjs.idefense.device.sound.sv2101;

import cn.bjzfgcjs.idefense.common.utils.IPAddress;
import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.service.DeviceStorge;
import cn.bjzfgcjs.idefense.device.DevManager;
import cn.bjzfgcjs.idefense.device.CodeTranslator;
import cn.bjzfgcjs.idefense.device.sound.SoundAPI;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/*
 * 以下假定：
 *
 * 1. 一个策略的执行仅通过一个机位来下达
 *
 */

@Service
public class LCPlayback implements CodeTranslator, SoundAPI, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(LCPlayback.class);

    // SV-2101 SDK
    public final static LCAudioThrDll LC_AUDIO_THR_DLL = LCAudioThrDll.INSTANCE;
    public final static NaSetup NA_SETUP = NaSetup.INSTANCE;

    // 限制参数配置
    private static final int SENDBUFFER_MAX_LEN = 64 * 1024;

    private static final int DelayMaxCount = 40;

    private static final int GroupNoMin = 1;

    private static final int GroupNoMax = 255;

    private static final long WaitFiniTime = 100L; // 单位： ms

    @Resource
    private DeviceStorge deviceStorge;

    @Resource
    private DevManager devManager;

    // 由设备查找对应的handler
    private static final ConcurrentHashMap<String, LCHandler> lckCache = new ConcurrentHashMap<>();

    // 用于检查音频卡状态
    private NaSetup._DeviceInfo.ByReference dev;

    private static AtomicInteger micLock = new AtomicInteger(0);

    @Resource
    private TaskExecutor taskExecutor;

    // 保存所有音频卡的操作资源
    public static class LCHandler {
        private LCAudioThrDll._PlayParam.ByReference playParam;

        public LCHandler() {
            playParam = new LCAudioThrDll._PlayParam.ByReference();
            playParam.hWnd = new WinDef.HWND(Pointer.NULL);
            playParam.Priority = 10;
            playParam.Treble = 100;
            playParam.Treble_En = 100;
            playParam.Bass = 100;
            playParam.Bass_En = 100;
            playParam.OptionByte = 0;
            playParam.MaxBitrate = 0;
            playParam.Options[0] = 0;
        }

        public LCAudioThrDll._PlayParam.ByReference getPlayParam() {
            return playParam;
        }
    }

    /**
     * @param deviceInfo
     * @param audioFile
     * @param volume
     * @param loop
     * @throws Exception
     */
    @Override
    public int playLoop(DeviceInfo deviceInfo, String audioFile, Integer volume, final int loop) {
        if (devManager.canUse(deviceInfo))
            devManager.acqurire(deviceInfo);
        else
            return AppCode.DEV_BUSY.getCode();

        LCHandler lcHandler = lckCache.get(deviceInfo.getID());

        logger.info("播放的文件：{}", audioFile);
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int count = StringUtils.isBlank(audioFile) ? 1 : loop;
                    while (count-- > 0 || loop == SoundAPI.ALWAYS) {
                        anounce(deviceInfo, audioFile, volume);
                        if (LC_AUDIO_THR_DLL.lc_wait(lcHandler.getPlayParam()) == LCAudioThrDll.R_OK) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    devManager.release(deviceInfo);
                }
            }
        });
        return AppCode.OK.getCode();
    }

    /** 检查状态不对，就得上报情况。
     * @param deviceInfo
     * @return
     */
    public int checkStatus(DeviceInfo deviceInfo) {
        int res = NA_SETUP.np_search_one(deviceInfo.getIPAddress(), dev);
        if (res == NaSetup.NP_SUCCESS) {
            return AppCode.DEV_OK.getCode();
        } else {
            // TODO: 加入库，及上报调用。

            return configCode(res);
        }
    }

    @Override
    public int stop(DeviceInfo deviceInfo) {
        LCHandler lcHandler = getLcHandler(deviceInfo);
        devManager.release(deviceInfo);

        return operateCode(LC_AUDIO_THR_DLL.lc_stop(lcHandler.getPlayParam()));
    }

    @Override
    public int setVolume(DeviceInfo deviceInfo, byte volume) {
        LCHandler lcHandler = getLcHandler(deviceInfo);
        return operateCode(LC_AUDIO_THR_DLL.lc_set_volume(lcHandler.getPlayParam(), volume));
    }

    private int anounce(DeviceInfo deviceInfo, String audioFile, Integer volume) throws Exception {
        LCHandler lcHandler = getLcHandler(deviceInfo);
        LCAudioThrDll._PlayParam.ByReference playParam = lcHandler.getPlayParam();
        initParamGroup(deviceInfo, playParam);
        initParamSource(audioFile, playParam);
        playParam.Volume = volume;

        int ret = LC_AUDIO_THR_DLL.lc_init(audioFile, playParam);
        if (ret == LCAudioThrDll.R_OK) {
            if(LC_AUDIO_THR_DLL.lc_play(playParam) == 0){ // 返回线程ID
                //  "init playback thread failed"
                ret = LCAudioThrDll.ERR_OPT;

            } else {
                ret = LC_AUDIO_THR_DLL.lc_getlasterror(playParam);
            }
        }
        return ret;
    }

    private LCHandler getLcHandler(DeviceInfo deviceInfo) {
        return lckCache.get(deviceInfo.getID());
    }

    private void initParamGroup(DeviceInfo deviceInfo, LCAudioThrDll._PlayParam.ByReference playParam) {
        Position position = deviceStorge.getPosByPostionCode(deviceInfo.getPosition());
        if (position == null || position.getGroupNo() == 0) { // 单播
            playParam.MultiGroup = 0;
            playParam.CastMode = LC_AUDIO_THR_DLL.cUnicast;
            playParam.IP = new NativeLong(IPAddress.parseAddress(deviceInfo.getIPAddress()));

        } else {  // 组播
            playParam.MultiGroup = position.getGroupNo();
            playParam.CastMode = LC_AUDIO_THR_DLL.cMulticast;
            playParam.IP = new NativeLong(IPAddress.parseAddress(IPAddress.getLocalIp()));
        }
    }

    private int initParamSource(String file, LCAudioThrDll._PlayParam.ByReference playParam) {
        int respCode = AppCode.OK.getCode();

        if (StringUtils.isBlank(file)) {   // Mic
            playParam.SourceType = LC_AUDIO_THR_DLL.SCR_TYPE_AUDIOCARD;
            playParam.DeviceID   = 0;
            playParam.nChannels  = 2;
            playParam.nSamplesPerSec = 44100;

        } else {       // File
            playParam.SourceType = LC_AUDIO_THR_DLL.SCR_TYPE_FILE;
            LCAudioThrDll._FileInfo.ByReference pFileInfo = new LCAudioThrDll._FileInfo.ByReference();
            int res = LC_AUDIO_THR_DLL.lc_get_fileinfo(file, pFileInfo);

            if ( res == LCAudioThrDll.R_OK) {
                playParam.nSamplesPerSec = pFileInfo.SampleRate / 1000;
            } else {
                // playback failed to load audio file
                respCode = operateCode(res);
            }
        }
        return respCode;
    }

    private void initResource(){
        List<DeviceInfo> deviceInfos = deviceStorge.getDeviceListByType(
                Integer.valueOf(DeviceInfo.Type.Acoustic.ordinal()).byteValue());

        for(DeviceInfo obj : deviceInfos) {
            lckCache.put(obj.getID(), new LCHandler());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initResource();
        if (dev == null) { // 用于检查音频卡状态
            dev = new NaSetup._DeviceInfo.ByReference();
        }
    }

    @Override
    public void destroy() throws Exception {
    }


    @Override
    public int configCode(Integer errno) {
        switch(errno) {
            case NaSetup.NP_SUCCESS:
                return AppCode.OK.getCode();

            case NaSetup.NP_NET_INTERFACE_ERROR:
                return AppCode.AUDIO_NP_EINET.getCode();

            case NaSetup.NP_DATA_ERROR:
                return AppCode.AUDIO_NP_ERESP.getCode();

            case NaSetup.NP_DEVICE_NOT_EXIST:
                return AppCode.AUDIO_NP_NONEXIST.getCode();

            case NaSetup.NP_WP_ERROR:
                return AppCode.AUDIO_NP_EWRITE.getCode();

            case NaSetup.NP_PARAM_ERROR:
                return AppCode.AUDIO_NP_EPARAM.getCode();

            case NaSetup.NP_PASSWORD_ERROR:
                return AppCode.AUDIO_NP_EAUTH.getCode();

            case NaSetup.NP_MEMORY_ERROR:
                return AppCode.AUDIO_NP_EMEM.getCode();

            default:
                return AppCode.UNKNOWN.getCode();
        }
    }

    @Override
    public int operateCode(Integer errno) {
        switch (errno) {
            case LCAudioThrDll.R_OK:
                return AppCode.OK.getCode();

            case LCAudioThrDll.ERR_PARAM:
                return AppCode.AUDIO_OP_EPARAM.getCode();

            case LCAudioThrDll.ERR_OPT:
                return AppCode.AUDIO_OP_ERROR.getCode();

            case LCAudioThrDll.ERR_SOCKET:
                return AppCode.AUDIO_OP_ESOCKET.getCode();

            case LCAudioThrDll.ERR_CODEC:
                return AppCode.AUDIO_OP_ECODEC.getCode();

            case LCAudioThrDll.ERR_MEM_FAULT:
                return AppCode.AUDIO_OP_EFILE.getCode();

            case LCAudioThrDll.ERR_FILEFORMAT_FAULT:
                return AppCode.AUDIO_OP_EFORMAT.getCode();

            case LCAudioThrDll.ERR_BAD_BITRATE:
                return AppCode.AUDIO_OP_BAD_BITRATE.getCode();

            case LCAudioThrDll.ERR_DECODE_DISABLE:
                return AppCode.AUDIO_OP_DECODE_BAN.getCode();

            default:
                return AppCode.UNKNOWN.getCode();
        }
    }
}
