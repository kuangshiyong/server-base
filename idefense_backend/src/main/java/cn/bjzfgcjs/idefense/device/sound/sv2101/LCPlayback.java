package cn.bjzfgcjs.idefense.device.sound.sv2101;

import cn.bjzfgcjs.idefense.common.utils.IPAddress;
import cn.bjzfgcjs.idefense.device.sound.SoundAPI;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class LCPlayback implements SoundAPI {
    private static final Logger logger = LoggerFactory.getLogger(LCPlayback.class);

    public final static LCAudioDll lcAudioDll = LCAudioDll.INSTANCE;

    private static final int SENDBUFFER_MAX_LEN = 64 * 1024;

    private static final int DelayMaxCount = 40;

    private static final int GroupNoMin = 1;

    private static final int GroupNoMax = 255;

    private LCAudioDll._PlayParam.ByReference playParam;

    @Override
    public boolean isAvailable(String deviceId) {
        return true;
    }

//    @Override
    public int playback(String deviceId, Integer tacticId) {
        playParam = new LCAudioDll._PlayParam.ByReference();

        final HWND hwnd = new HWND(Native.getComponentPointer(new JWindow()));

        playParam.hWnd = new HWND(Pointer.NULL);
        playParam.Priority = 10;
        playParam.Treble = 100;
        playParam.Treble_En = 100;
        playParam.Bass = 100;
        playParam.Bass_En = 100;
        char mixName = '\0';
        playParam.MuxName[0] = (byte) '\0';
        playParam.nChannels = 2;

        // 测试数据
        playParam.Volume = 80; //策略库
        playParam.MultiGroup = 0; // 单播时无意义
        playParam.CastMode = lcAudioDll.cUnicast; // 查机位表
        playParam.IP = new NativeLong(IPAddress.parseAddress("192.168.2.25")); // 多播是0， 单播是目的

        playParam.SourceType = 0; //0 文件， 1-声卡
        playParam.DeviceID = 0; // 喊话时用

        String file = "D:/work/demo.mp3";
        LCAudioDll._FileInfo.ByReference pFileInfo = new LCAudioDll._FileInfo.ByReference();
        int resReadFile = lcAudioDll.lc_get_fileinfo(file, pFileInfo);

        if (resReadFile == LCAudioDll.R_OK &&  pFileInfo.SampleRate > 0) {
            playParam.nSamplesPerSec = pFileInfo.SampleRate / 1000; // FileInfo->SampleRate， 声卡：44100
            int resInit = lcAudioDll.lc_init(file, playParam);
            logger.debug("音频初始化结果：{}", resInit);
            if (resInit == lcAudioDll.R_OK) {
                return lcAudioDll.lc_play(playParam);
            }
        } else {
            logger.debug("读音频文件失败");
        }

        return -1;
    }

//    public static int audio_file_init(String file) {
//        LCAudioDll._FileInfo.ByReference pFileInfo = new LCAudioDll._FileInfo.ByReference();
//        int resReadFile = lcAudioDll.lc_get_fileinfo(file, pFileInfo);
//        if ( resReadFile == LCAudioDll.R_OK) {
////            pFileInfo.SampleRate
//        } else {
//            logger.info("文件读取失败");
//
//        }
//        return resReadFile;
//    }
}

