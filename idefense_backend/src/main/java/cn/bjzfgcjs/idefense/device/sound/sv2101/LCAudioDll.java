package cn.bjzfgcjs.idefense.device.sound.sv2101;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.util.Arrays;
import java.util.List;

public interface LCAudioDll extends StdCallLibrary {

    LCAudioDll INSTANCE = (LCAudioDll) Native.loadLibrary("LCAudioThrDll",
            LCAudioDll.class);

    /*定义网络通信方式*/
    public final static int cUnicast = 0;	//单播
    public final static int cMulticast	= 1;	//组播
    public final static int cBroadcast	= 2;	//广播


    public final static int AUDIO__NOT_EXIST = -1;
    public final static int AUDIO_TYPE_ERR  = 0;
    public final static int AUDIO_TYPE_MP3  = 1;
    public final static int AUDIO_TYPE_WAV  = 2;
    public final static int AUDIO_TYPE_WMA  = 3;
    public final static int AUDIO_TYPE_MP2  = 4;

    public final static int SCR_TYPE_FILE = 0;			//数据来源是文件
    public final static int SCR_TYPE_AUDIOCARD = 1;	    //数据来源是声卡
    public final static int SCR_TYPE_BUFFER = 2;		//数据来源是内存缓存
    public final static int SCR_TYPE_STREAM = 3;		//数据来源是数据流//V2.0.3.0

    /*定义函数返回值*/
    public final static int R_OK = 0;		//成功
    public final static int ERR_PARAM = -1;		//参数错误
    public final static int ERR_OPT   = -2;		//函数执行错误
    public final static int ERR_SOCKET = -3;		//socket操作失败
    public final static int ERR_CODEC = -4;	//初始化编解码器失败。

    /*Windows 消息定义*/
//exception
    public final static int WM_USER = 0x400;
    //public final static int WM_MSG_EXCEPTION	= (WM_USER+100)
    public final static int WM_MSG_COMPLETED =  (WM_USER+101);
    public final static int WM_MSG_PAUSE	  = (WM_USER+102);
    public final static int WM_MSG_CONTINUE	  = (WM_USER+103);
    public final static int WM_MSG_AUDIOPOWER = (WM_USER+104);


    public class _FileInfo extends Structure {

        public int Duration;  //播放时长

        public int SampleRate; //采样率

        public int BitRate; //比特率

        public _FileInfo() {
            super();
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("Duration", "SampleRate", "BitRate");
        }

        public _FileInfo(int Duration, int SampleRate, int BitRate) {
            super();
            this.Duration = Duration;
            this.SampleRate = SampleRate;
            this.BitRate = BitRate;
        }

        public static class ByReference extends _FileInfo implements Structure.ByReference {
        };

        public static class ByValue extends _FileInfo implements Structure.ByValue {
        };

    }

    public class _PlayParam extends Structure {
        public HWND hWnd; //主窗口的句柄，如果不为0，则线程有事件会向该窗口发送消息

        public int Priority; //优先级

        public int MultiGroup; //多播组号

        public int CastMode; //传输模式，单播，多播，广播

        public NativeLong IP; // 如果是广播和多播，此参数是源网卡的IP，如果此地址为0，则由系统决定使用哪个网卡，如果是单播，这是目标设备的ip地址。

        public int Volume; //播放音量取值0～100

        public int Tone;    //音调

        public int Treble;   //高音频率

        public int Bass;    //低音频率

        public int   Treble_En;		//高音增益

        public int   Bass_En;		//低音增益

        public int   SourceType;	//输入源，0为文件，1为声卡

        public int DeviceID;		//音频输入ID号 1～N

        public byte[] MuxName = new byte[64]; //混音器的通道名字

        public int nChannels; //采样的通道 1～2 CodecType

        public int nSamplesPerSec; //采样频率 8K，11.025K,22.05K,44.1K

        public int AudioBufferLength; //Audio数据的长度

        public Pointer AudioBuf;  //Audio数据的指针

        public int[] PrivateData = new int[128]; //私有信息，lc_init初始化后，用户不能修改里面的内容。

        public _PlayParam() {
            super();
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("hWnd", "Priority", "MultiGroup", "CastMode", "IP", "Volume", "Tone", "Treble", "Bass", "Treble_En", "Bass_En", "SourceType", "DeviceID", "MuxName", "nChannels", "nSamplesPerSec", "AudioBufferLength", "AudioBuf", "PrivateData");
        }

        public static class ByReference extends _PlayParam implements Structure.ByReference {
        };

        public static class ByValue extends _PlayParam implements Structure.ByValue {
        };

    }

    public class _WaveInInfo extends Structure {

        public int Index;  // 输入通道的序号

        public byte[] name = new byte[32];  //  名字

        public NativeLong Formats;          //  通道数 2为立体声输入。

        public int Channels;                //  通道数 2为立体声输入。

        public _WaveInInfo() {
            super();
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("Index", "name", "Formats", "Channels");
        }

        public _WaveInInfo(int Index, byte name[], NativeLong Formats, int Channels) {
            super();
            this.Index = Index;
            if ((name.length != this.name.length))
                throw new IllegalArgumentException("Wrong array size !");
            this.name = name;
            this.Formats = Formats;
            this.Channels = Channels;
        }

        public static class ByReference extends _WaveInInfo implements Structure.ByReference {
        };

        public static class ByValue extends _WaveInInfo implements Structure.ByValue {
        };
    }

    public class _MuxInfo extends Structure {
        public byte[] name = new byte[64]; //Mixer Name

        public _MuxInfo() {
            super();
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("name");
        }
        /** @param name C type : char[64] */
        public _MuxInfo(byte name[]) {
            super();
            if ((name.length != this.name.length))
                throw new IllegalArgumentException("Wrong array size !");
            this.name = name;
        }

        public static class ByReference extends _MuxInfo implements Structure.ByReference {
        };

        public static class ByValue extends _MuxInfo implements Structure.ByValue {
        };
    }

    public Pointer lc_play_getmem();

    public int lc_play_freemem(_PlayParam.ByReference pParam);

    public int lc_init(String pFileName, _PlayParam.ByReference pParam);

    public int lc_play(_PlayParam.ByReference pParam);

    public int lc_stop(_PlayParam.ByReference pParam);

    public int lc_pause(_PlayParam.ByReference pParam);

    public int lc_continue(_PlayParam.ByReference pParam);

    public int lc_seek(_PlayParam.ByReference pParam, int time);

    public int lc_wait(_PlayParam.ByReference pParam);

    public int lc_set_volume(_PlayParam.ByReference pParam, byte volume);

    public int lc_get_playtime(_PlayParam.ByReference pParam);

    public int lc_get_playstatus(_PlayParam.ByReference pParam);

    public int lc_get_duration(_PlayParam.ByReference pParam);

    public int lc_get_fileinfo(String pFileName, _FileInfo.ByReference pFileInfo);

    public int lc_get_version();

    public int lc_rec_devinfo(_WaveInInfo.ByReference Info, IntByReference Number);

    public int lc_rec_muxinfo(int Index, _MuxInfo.ByReference Info, IntByReference Number);

    public int lc_rec_getmicname(int Index, String pName, int NameSize);

    public int lc_rec_setmicvolume(int Index, int volume);

    public int lc_getlasterror(_PlayParam.ByReference pParam);

    public int lc_record_start(_PlayParam.ByReference pParam, String filename);

    public int lc_record_stop(_PlayParam.ByReference pParam);

    public int lc_record_status(_PlayParam.ByReference pParam);

    public int lc_inputdata(_PlayParam.ByReference pParam, Pointer buf, int datalen);

    public int lc_addip(_PlayParam.ByReference pParam, DWORD IP);

    public int lc_delip(_PlayParam.ByReference pParam, DWORD IP);

    public int lc_rec_delip(_PlayParam.ByReference pParam, DWORD IP);
}

