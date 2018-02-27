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
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LCPlayback implements SoundAPI, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(LCPlayback.class);

    public final static LCAudioThrDll LC_AUDIO_THR_DLL = LCAudioThrDll.INSTANCE;

    private static final int SENDBUFFER_MAX_LEN = 64 * 1024;

    private static final int DelayMaxCount = 40;

    private static final int GroupNoMin = 1;

    private static final int GroupNoMax = 255;

    public int threadId = 0;

    private LCAudioThrDll._PlayParam.ByReference playParam;

    @Resource
    private PubMessage pubMessage;

    private static AtomicInteger lock = new AtomicInteger(0);


    @Scheduled(initialDelay = 1000, fixedDelay = 10000)
    public void playSound() {
        try {
            String file = null;
            lock.getAndIncrement();

            if (equals(0)) {
                int a = lock.getAndIncrement();
                logger.debug("播放文件：{}", file);
                logger.info("加锁了：{}", lock.get());
                playback("demo", 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isAvailable(String deviceId) {
        return true;
    }

    private void playback(String deviceId, Integer tacticId) {
        playParam.hWnd = new HWND(Pointer.NULL);
        playParam.Priority = 10;
        playParam.Treble = 100;
        playParam.Treble_En = 100;
        playParam.Bass = 100;
        playParam.Bass_En = 100;


//        playParam.MuxName[0] = (byte) '\0';
//        playParam.nChannels = 2;
        // 测试数据
        playParam.Volume = 80; //策略库
        playParam.MultiGroup = 0; // 单播时无意义
        playParam.CastMode = LC_AUDIO_THR_DLL.cUnicast; // 查机位表
        playParam.IP = new NativeLong(IPAddress.parseAddress("192.168.2.25")); // 多播是0， 单播是目的

        playParam.SourceType = 0; //0 文件， 1-声卡
        playParam.DeviceID = 0; // 喊话时用

        String file = "C:/work/test.mp3";
        LCAudioThrDll._FileInfo.ByReference pFileInfo = new LCAudioThrDll._FileInfo.ByReference();
        int resReadFile = LC_AUDIO_THR_DLL.lc_get_fileinfo(file, pFileInfo);

        if (resReadFile == LCAudioThrDll.R_OK &&  pFileInfo.SampleRate > 0) {
            playParam.nSamplesPerSec = pFileInfo.SampleRate / 1000; // FileInfo->SampleRate， 声卡：44100
            int resInit = LC_AUDIO_THR_DLL.lc_init(file, playParam);

            logger.debug("音频初始化结果：{}", resInit);
            if (resInit == LC_AUDIO_THR_DLL.R_OK) {
                logger.debug("保存好数据");
                threadId = LC_AUDIO_THR_DLL.lc_play(playParam);
            }
        } else {
            logger.debug("读音频文件失败");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
       
    }

    @Override
    public void destroy() throws Exception {
        playParam = LC_AUDIO_THR_DLL.lc_play_getmem();
        playParam.AudioBuf = null;
    }


}

