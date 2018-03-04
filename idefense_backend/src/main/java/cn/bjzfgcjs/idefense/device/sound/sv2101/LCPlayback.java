package cn.bjzfgcjs.idefense.device.sound.sv2101;

import cn.bjzfgcjs.idefense.common.utils.IPAddress;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.service.DeviceStorge;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.ptr.IntByReference;
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
public class LCPlayback implements InitializingBean, DisposableBean {

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
    private PlaybackMsgWnd playbackMsgWnd;

    // 由设备查找对应的handler
    private static final ConcurrentHashMap<String, LCHandler> lckCache = new ConcurrentHashMap<>();

    // 用于检查音频卡状态
    private NaSetup._DeviceInfo.ByReference dev;

    private static AtomicInteger micLock = new AtomicInteger(0);

    @Resource
    private TaskExecutor taskExecutor;


    /**
     * @param deviceInfo
     * @param audioFile
     * @param volume
     * @param loop
     * @throws Exception
     */
    public void playLoop(DeviceInfo deviceInfo, String audioFile, Integer volume, final int loop) {
        LCHandler lcHandler = getLcHandler(deviceInfo);
        if (!lcHandler.isSpare()) {
            logger.info("audio card no spare time: " + deviceInfo.getID());
            return;
        }

        logger.info("播放的文件：{}", audioFile);

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int count = StringUtils.isBlank(audioFile) ? 1 : loop;
                    while (count-- > 0) {
                        anounce(deviceInfo, audioFile, volume);
                        if (LC_AUDIO_THR_DLL.lc_wait(lcHandler.getPlayParam()) == LCAudioThrDll.R_OK) {
                            lcHandler.free();
                            continue;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lcHandler.getPlayLock().set(0);
                }
            }
        });
    }

    /** 检查状态不对，就得上报情况。
     * @param deviceInfo
     * @return
     */
    public boolean isAvailable(DeviceInfo deviceInfo) {
        int ret = NA_SETUP.np_search_one(deviceInfo.getIPAddress(), dev);
        if (ret == 1) {
            logger.info("Device {}, mac {}", deviceInfo.getIPAddress(), dev);
        } else {
            logger.info("search audio card: sv2101 failed, code {}", ret);
        }

        return (ret == 1);
    }

    public void anounce(DeviceInfo deviceInfo, String audioFile, Integer volume) throws Exception {

        LCHandler lcHandler = getLcHandler(deviceInfo);
        if (lcHandler.getPlayParam() == null) {
        }
        logger.info("锁呢：{}", lcHandler.getPlayLock());
        if (!lcHandler.check()) return;

        lcHandler.get();

        LCAudioThrDll._PlayParam.ByReference playParam = lcHandler.getPlayParam();
//
//        LCAudioThrDll._PlayParam.ByReference playParam = new LCAudioThrDll._PlayParam.ByReference();
//        playParam.Priority = 10;
//        playParam.Treble = 100;
//        playParam.Treble_En = 100;
//        playParam.Bass = 100;
//        playParam.Bass_En = 100;
//        playParam.OptionByte = 0;
////        playParam.MuxName[0] = 0;
        playParam.MaxBitrate = 0;
        playParam.Options[0] = 0;

//        initParamGroup(deviceInfo, playParam);
        initParamSource(audioFile, playParam);

        playParam.MultiGroup = 0;
        playParam.CastMode = LC_AUDIO_THR_DLL.cUnicast;
        playParam.IP = new NativeLong(IPAddress.parseAddress(deviceInfo.getIPAddress()));

//        playParam.SourceType = LC_AUDIO_THR_DLL.SCR_TYPE_AUDIOCARD;
//        playParam.DeviceID   = 0;
//        playParam.nChannels  = 2;
//        playParam.nSamplesPerSec = 44100;
//        playParam.hWnd = new HWND(Pointer.NULL);
        playParam.Volume = volume;

        LCAudioThrDll._WaveInInfo.ByReference info = new LCAudioThrDll._WaveInInfo.ByReference();
        IntByReference i = new IntByReference();
        LC_AUDIO_THR_DLL.lc_rec_devinfo(info, i);
        logger.info("信息是: {}, n:{}", info,  i);


        logger.info("参数有错: {}", playParam);
//        audioFile = "d:/play.mp3";
//        audioFile="";
        int res = LC_AUDIO_THR_DLL.lc_init(audioFile, playParam);
        if (res == LC_AUDIO_THR_DLL.R_OK) {

            needMic(audioFile);
            if(LC_AUDIO_THR_DLL.lc_play(playParam) == 0){
                releaseMic();
                lcHandler.free();
                throw new Exception("init playback thread failed");

            } else {
                logger.info("开始播音了吗？");
                LC_AUDIO_THR_DLL.lc_getlasterror(playParam);
            }

        } else {
            logger.info("初始化失败了, {}", res);
        }
        lcHandler.getPlayLock().set(0);
    }

    public void stop(DeviceInfo deviceInfo) throws Exception {
        LCHandler lcHandler = getLcHandler(deviceInfo);
        LC_AUDIO_THR_DLL.lc_stop(lcHandler.getPlayParam());
            releaseMic();
            lcHandler.getPlayLock().set(0);

            logger.info("lock: {}, ", lcHandler.getPlayLock().get());
    }

    public void setVolume(DeviceInfo deviceInfo, byte volume) {
        LCHandler lcHandler = getLcHandler(deviceInfo);
        LC_AUDIO_THR_DLL.lc_set_volume(lcHandler.getPlayParam(), volume);
    }

    private void needMic(String audioFile) throws Exception {
        if (!StringUtils.isBlank(audioFile)) return;

        if (micLock.get() > 0)
            throw new Exception("microhphone in use");
        micLock.getAndIncrement();
    }

    private void releaseMic() {
        micLock.getAndDecrement();
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

    private void initParamSource(String file, LCAudioThrDll._PlayParam.ByReference playParam) throws Exception {
        if (StringUtils.isBlank(file)) {   // Mic
            logger.info("启用麦克风");
            playParam.SourceType = LC_AUDIO_THR_DLL.SCR_TYPE_AUDIOCARD;
            playParam.DeviceID   = 0;
            playParam.nChannels  = 2;
            playParam.nSamplesPerSec = 44100;

        } else {       // File
            playParam.SourceType = LC_AUDIO_THR_DLL.SCR_TYPE_FILE;
            LCAudioThrDll._FileInfo.ByReference pFileInfo = new LCAudioThrDll._FileInfo.ByReference();
            if (LC_AUDIO_THR_DLL.lc_get_fileinfo(file, pFileInfo) == LCAudioThrDll.R_OK) {
                playParam.nSamplesPerSec = pFileInfo.SampleRate / 1000;
            } else {
                throw new Exception("playback failed to load audio file: " + file);
            }
        }
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


}

