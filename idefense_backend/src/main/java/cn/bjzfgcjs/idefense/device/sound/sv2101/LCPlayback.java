package cn.bjzfgcjs.idefense.device.sound.sv2101;

import cn.bjzfgcjs.idefense.common.utils.IPAddress;
import cn.bjzfgcjs.idefense.device.sound.SoundAPI;
import cn.bjzfgcjs.idefense.service.PubMessage;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LCPlayback implements SoundAPI, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(LCPlayback.class);

    public final static LCAudioThrDll LC_AUDIO_THR_DLL = LCAudioThrDll.INSTANCE;

    private static LCAudioThrDll._PlayParam.ByReference playParam;

    private static final int SENDBUFFER_MAX_LEN = 64 * 1024;

    private static final int DelayMaxCount = 40;

    private static final int GroupNoMin = 1;

    private static final int GroupNoMax = 255;

    public int threadId = 0;

    @Resource
    private PubMessage pubMessage;

    public static AtomicInteger lock = new AtomicInteger(0);


    @Override
    public boolean isAvailable(String deviceId) {
        return true;
    }

    public void playback(String deviceId, Integer tacticId) {
        if (lock.getAndIncrement() == 0){
            playback("demo", 3);
        } else {
            return;
        }

        if (playParam == null) {
            logger.debug("还没分配内存");
            return;
        }

		// Generic Initialization
        playParam.Priority = 10;
        playParam.Treble = 100;
        playParam.Treble_En = 100;
        playParam.Bass = 100;
        playParam.Bass_En = 100;
        playParam.MaxBitrate = 0;

		// Specific Initialization
        playParam.hWnd = new HWND();
//        playParam.nChannels = 2;
        // 测试数据
        playParam.Volume = 100; //策略库
//        playParam.MultiGroup = 0; // 单播时无意义
        playParam.CastMode = LC_AUDIO_THR_DLL.cUnicast; // 查机位表
        playParam.IP = new NativeLong(IPAddress.parseAddress("192.168.2.25")); // 多播是0， 单播是目的
        playParam.SourceType = 0; //0 文件， 1-声卡
//        playParam.DeviceID = 0; // 喊话时用

        String file = "D:/work/demo.mp3";
        LCAudioThrDll._FileInfo.ByReference pFileInfo = new LCAudioThrDll._FileInfo.ByReference();
        int resReadFile = LC_AUDIO_THR_DLL.lc_get_fileinfo(file, pFileInfo);

        if (resReadFile == LCAudioThrDll.R_OK &&  pFileInfo.SampleRate > 0) {
            playParam.nSamplesPerSec = pFileInfo.SampleRate / 1000; // FileInfo->SampleRate， 声卡：44100
            if (LC_AUDIO_THR_DLL.lc_init(file, playParam) == LC_AUDIO_THR_DLL.R_OK) {
                threadId = LC_AUDIO_THR_DLL.lc_play(playParam);
                int res = LC_AUDIO_THR_DLL.lc_getlasterror(playParam);
                logger.debug("播放线程{}开始，错误码：{}, {}", threadId, res, playParam);
            }
        } else {
            logger.debug("读音频文件失败");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (playParam == null) {
            playParam = new LCAudioThrDll._PlayParam.ByReference();
            playParam.AudioBuf = null;
        }
    }

    @Override
    public void destroy() throws Exception {
    }
}

