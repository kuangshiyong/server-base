package cn.bjzfgcjs.idefense.device.sound.sv2101;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.util.Arrays;
import java.util.List;

import static com.sun.jna.platform.win32.WinUser.WM_USER;

// 播放接口
public interface LCAudioThrDll extends StdCallLibrary {

    public static final String JNA_LIBRARY_NAME = "LCAudioThrDll";

    LCAudioThrDll INSTANCE = (LCAudioThrDll) Native.loadLibrary(JNA_LIBRARY_NAME, LCAudioThrDll.class);

    /*定义网络通信方式*/
    public final static int cUnicast = 0;	    //单播
    public final static int cMulticast	= 1;	//组播
    public final static int cBroadcast	= 2;	//广播
    public final static int cMulticast2	= 3;	//二类组播

    public final static short SCR_TYPE_FILE = 0;			//数据来源是文件
    public final static short SCR_TYPE_AUDIOCARD = 1;	    //数据来源是声卡
    public final static short SCR_TYPE_BUFFER = 2;		//数据来源是内存缓存
    public final static short SCR_TYPE_STREAM = 3;		//数据来源是数据流//V2.0.3.0

    /*定义函数返回值*/
    public final static int R_OK = 0;		        //成功
    public final static int ERR_PARAM = -1;		    //参数错误
    public final static int ERR_OPT   = -2;		    //函数执行错误
    public final static int ERR_SOCKET = -3;		//socket操作失败
    public final static int ERR_CODEC = -4;	        //初始化编解码器失败。
    public final static int ERR_MEM_FAULT = -6;		//读取文件失败
    public final static int ERR_FILEFORMAT_FAULT = -7;
    public final static int ERR_BAD_BITRATE = -8;
    public final static int ERR_DECODE_DISABLE = -9; //应用程序禁用解码功能，而播放此文件需要解码

    /* Windows 消息定义 */
//exception
    //public final static int WM_MSG_EXCEPTION	= (WM_USER+100)
    public final static int WM_MSG_COMPLETED  = (WM_USER+101);
    public final static int WM_MSG_PAUSE	  = (WM_USER+102);
    public final static int WM_MSG_CONTINUE	  = (WM_USER+103);
    public final static int WM_MSG_AUDIOPOWER = (WM_USER+104);   // sample message
    public final static int WM_MSG_SOUNDCARD  =	(WM_USER+105);

    //_PlayParam.CtrlByte控制字定义
    // 重采样选项，在录播模式时，因计算机采样与播放设备回放频率的差异，可能导致时延
    // 选择重采样选项，动态库自动处理这个问题,但带来轻微的失真。
    public final static int OPT_RESAMPLE = 0X0001;
    public final static int OPT_STREAM   = 0X0002;

    //禁用解码器，因为解码器不是线程安全的，故有些多线程应用需要禁用，以保证稳定。
    public final static int OPT_DECODE_DISABLE = 0X0004;

    // 音频文件支持的格式
    public final static int AUDIO__NOT_EXIST = -1;
    public final static int AUDIO_TYPE_ERR  = 0;
    public final static int AUDIO_TYPE_MP3  = 1;
    public final static int AUDIO_TYPE_WAV  = 2;
    public final static int AUDIO_TYPE_WMA  = 3;
    public final static int AUDIO_TYPE_MP2  = 4;


    public class _FileInfo extends Structure {

        public int Duration;   //播放时长

        public int SampleRate; //采样率

        public int BitRate;    //比特率

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

        public int Priority;   //优先级

        public int MultiGroup; //多播组号

        public int CastMode;   //传输模式，单播，多播，广播

        public NativeLong IP;  // 如果是广播和多播，此参数是源网卡的IP，如果此地址为0，则由系统决定使用哪个网卡，如果是单播，这是目标设备的ip地址。

        public int Volume;   //播放音量取值0～100

        public int Tone;    //音调

        public int Treble;  //高音频率

        public int Bass;    //低音频率

        public int Treble_En;   //高音增益

        public int Bass_En;		//低音增益

        public short SourceType;	//输入源，0为文件，1为声卡

        public short OptionByte;  //选项字，默认为0; bit0=1

        public int DeviceID;		//音频输入ID号 1～N

        // 混音器使用默认首选设备，不再设置。

        public int MaxBitrate;  //允许最大的比特率组合，如果源文件高于此比特率，将被重压缩至此比特率。

        public int[] Options = new int[15]; //选项

        public int nChannels;   //采样的通道 1～2 CodecType

        public int nSamplesPerSec; //采样频率 8K，11.025K,22.05K,44.1K

        public int AudioBufferLength; //Audio数据的长度

        public Pointer AudioBuf;      //Audio数据的指针

        public int[] PrivateData = new int[128]; //私有信息，lc_init初始化后，用户不能修改里面的内容。

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("hWnd", "Priority", "MultiGroup", "CastMode", "IP", "Volume", "Tone", "Treble",
                    "Bass", "Treble_En", "Bass_En", "SourceType", "OptionByte", "DeviceID", "MaxBitrate",
                    "Options", "nChannels", "nSamplesPerSec", "AudioBufferLength", "AudioBuf", "PrivateData");
        }

        public static class ByReference extends _PlayParam implements Structure.ByReference {
        };

        public static class ByValue extends _PlayParam implements Structure.ByValue {
        };
    }

    public class _WaveInInfo extends Structure {

        public int Index;                   // 输入通道的序号

        public byte[] name = new byte[32];  //  名字

        public NativeLong Formats;          //  支持的格式

        public int Channels;                //  通道数；2为立体声输入。

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

    public _PlayParam.ByReference lc_play_getmem();

    public int lc_play_freemem(_PlayParam.ByReference pParam);

    public int lc_init(String pFileName, _PlayParam.ByReference pParam);

    public int lc_play(_PlayParam.ByReference pParam);

    public int lc_stop(_PlayParam.ByReference pParam);

    public int lc_pause(_PlayParam.ByReference pParam);

    public int lc_continue(_PlayParam.ByReference pParam);

    public int lc_seek(_PlayParam.ByReference pParam, int time);

    public int lc_wait(_PlayParam.ByReference pParam);

    public int lc_wait_time(_PlayParam.ByReference pParam,int tv);  //tv以ms为单位

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

    //必须是192kbps一下mp3
    public int lc_inputdata_mp3(_PlayParam.ByReference pParam, Pointer buf, int datalen);

    //查询流模式下，当前的播放缓存的数据量。
    public int lc_get_datasize(_PlayParam.ByReference pParam);

    public int lc_addip(_PlayParam.ByReference pParam, DWORD IP);

    public int lc_delip(_PlayParam.ByReference pParam, DWORD IP);

    public int lc_rec_delip(_PlayParam.ByReference pParam, DWORD IP);

    public int lc_set_stream(_PlayParam.ByReference pParam, int enable);

}


// 配置接口
interface NaSetup extends StdCallLibrary {
    public static final String JNA_LIBRARY_NAME = "NaSetup";
    public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(NaSetup.JNA_LIBRARY_NAME);
    public static final NaSetup INSTANCE = (NaSetup)Native.loadLibrary(NaSetup.JNA_LIBRARY_NAME, NaSetup.class);

    public static final int USER_LEN	=		16;
    public static final int PASSWORD_LEN =		16;
    public static final int DEVICE_NAME_LEN	 =	16;
    public static final int VERSION_LEN	 =		32;
    public static final int URL_LEN		=		32;
    public static final int DEVICE_TYPENAME_LEN = 32;

    //	**********	以下是返回值宏定义
    public static final int NP_SUCCESS = 1;		//	函数操作成功，正确返回
    public static final int NP_NET_INTERFACE_ERROR = -1;		//	网络操作错误
    public static final int NP_DATA_ERROR     =		 -2;		//	返回不期望的数据，操作不成功
    public static final int NP_DEVICE_NOT_EXIST	=	 -3;		//	无数据返回，无法联系设备
    public static final int NP_WP_ERROR		  =		 -4;		//	参数保护，写错误
    public static final int NP_PARAM_ERROR	  =		 -5;		//	入口参数错误
    public static final int NP_PASSWORD_ERROR =		 -6;		//	密码错误
    public static final int NP_MEMORY_ERROR	  =	     -7;		//	内存不足错误


    //	**********	以下是定义通信流控对应参数
    public static final int  FC_NONE	= 0;
    public static final int  FC_RTC_CTS	= 1;

    //	**********	以下是定义通信协议对应参数
    public static final int UDP			= 0;
    public static final int TCP			= 1;
    public static final int REAL_COM	= 2;
    public static final int MODBUS_TCP	= 3;

    //	**********	以下是定义工作模式对应参数
    public static final int SERVER_MODE	= 0;		//	服务器模式
    public static final int AUTO_MODE	= 1;		//	自动模式
    public static final int CLIENT_MODE	= 2;		//	客户端模式
    public static final int MULTI_MODE	= 3;		//	多连接模式

    //	**********	以下是定义串口校验方式对应参数
    public static final int PR_NONE		= 0;		//	无校验
    public static final int PR_ODD		= 1;		//	奇校验
    public static final int PR_EVEN		= 2;		//	偶校验
    public static final int PR_MARK		= 3;		//	标记校验
    public static final int PR_SPACE	= 4;		//	空格校验

    //	**********	以下是定义串口波特率对应参数
    public static final int BAUD_1200BPS  = 12;		//1200bps
    public static final int BAUD_2400BPS  = 24;		//2400bps
    public static final int BAUD_4800BPS  = 48;		//4800bps
    public static final int BAUD_9600BPS  = 96;		//9600bps
    public static final int BAUD_19200BPS =	192;	//19200bps
    public static final int BAUD_38400BPS =	384;	//38400bps
    public static final int BAUD_57600BPS =	576;	//57600bps
    public static final int BAUD_115200BPS= 1152;	//115200bps

    public static class _Param extends Structure {
        public byte DeviceType; //	设备类别

        public byte[] Unuse0 = new byte[3]; //	未定义

        public byte[] User = new byte[16];     // 用户名

        public byte[] Password = new byte[16]; // 密码

        public byte ComName;     //	需要访问的串口号。

        public byte[] Unuse1 = new byte[3];  //	未定义

        public byte[] Unuse2 = new byte[24]; //	未定义
        public _Param() {
            super();
        }
        protected List<String> getFieldOrder() {
            return Arrays.asList("DeviceType", "Unuse0", "User", "Password", "ComName", "Unuse1", "Unuse2");
        }

        public _Param(byte DeviceType, byte Unuse0[], byte User[], byte Password[], byte ComName, byte Unuse1[], byte Unuse2[]) {
            super();
            this.DeviceType = DeviceType;
            if ((Unuse0.length != this.Unuse0.length))
                throw new IllegalArgumentException("Wrong array size !");
            this.Unuse0 = Unuse0;
            if ((User.length != this.User.length))
                throw new IllegalArgumentException("Wrong array size !");
            this.User = User;
            if ((Password.length != this.Password.length))
                throw new IllegalArgumentException("Wrong array size !");
            this.Password = Password;
            this.ComName = ComName;
            if ((Unuse1.length != this.Unuse1.length))
                throw new IllegalArgumentException("Wrong array size !");
            this.Unuse1 = Unuse1;
            if ((Unuse2.length != this.Unuse2.length))
                throw new IllegalArgumentException("Wrong array size !");
            this.Unuse2 = Unuse2;
        }
        public static class ByReference extends _Param implements Structure.ByReference {

        };
        public static class ByValue extends _Param implements Structure.ByValue {

        };
    };

    public static class _DeviceInfo extends Structure {

        public BYTE DeviceType;                 //	设备类别

        public BYTE[] Unuse0 = new BYTE[3];     //  未定义

        public byte[] Version = new byte[32];   //	固件版本

        public byte[] DeviceName = new byte[16];//	设备名称

        public byte[] Mac = new byte[6];        //	设备MAC地址

        public WORD LocalPort;                 //	设备监听端口

        public WORD[] LocalIP = new WORD[2];  //	设备动态IP地址

        public WORD[] PeerIP = new WORD[2];   //	服务器IP

        public WORD[] NetMask = new WORD[2];  //  子网掩码

        public WORD[] GatewayIP = new WORD[2];//	网关地址

        public WORD[] Unuse1 = new WORD[2];   //	未定义

        public WORD[] Unuse2 = new WORD[2];   //	未定义

        public byte[] Unuse3 = new byte[28];    //	未定义

        public byte[] DeviceTypeName = new byte[32]; //	设备类型字符串。

        public WORD DeviceID;                  //	设备的ID号，设备的ID号，仅用于NL9080中

        public byte UartNumber;                 //	设备具有的串口数量

        public byte Unuse4;

        public byte[] MultiGroup = new byte[4];  // 组播地址

        public byte[] MultiGroup2 = new byte[4];

        public WORD[] Interface = new WORD[2]; //	收到数据帧的网络接口，v2.1.0.3以上版本有效

        public byte[] Unuse5 = new byte[200];    //	未定义

        public _DeviceInfo() {
            super();
        }
        protected List<String> getFieldOrder() {
            return Arrays.asList("DeviceType", "Unuse0", "Version", "DeviceName", "Mac", "LocalPort", "LocalIP", "PeerIP", "NetMask", "GatewayIP", "Unuse1", "Unuse2", "Unuse3", "DeviceTypeName", "DeviceID", "UartNumber", "Unuse4", "MultiGroup", "MultiGroup2", "Interface", "Unuse5");
        }
        public static class ByReference extends _DeviceInfo implements Structure.ByReference {

        };
        public static class ByValue extends _DeviceInfo implements Structure.ByValue {

        };
    };

    public static class _Port extends Structure {

        public WORD LocalPort;  //	本地端口

        public WORD PeerPort;   //	远端端口

        public WORD[] PeerIp = new WORD[2];  //	远端ip地址

        public byte Protocol;    //	网络协议类型

        public byte WorkMode;    //	工作模式

        public byte Unuse0;      //  未定义

        public byte KeepLive;    //  保活时间

        public byte[] Unuse1 = new byte[32];  // 未定义

        public byte[] Unuse2 = new byte[4];   // 未定义

        public WORD Baud;       //	串口波特率

        public BYTE[] Unuse3 = new BYTE[2];   // 未定义

        public BYTE Databit;     //	串口数据位长度

        public BYTE Parity;      //	串口数据位长度

        public BYTE Stopbit;     //	串口数据位长度

        public BYTE Flow_ctrl;   //	串口数据位长度

        public WORD FrameSize;  //	串口最大数据包长度

        public BYTE ByteInterval;//	串口最大字符间隔时间

        public BYTE[] Unuse4 = new BYTE[9];   // 未定义

        public _Port() {
            super();
        }
        protected List<String> getFieldOrder() {
            return Arrays.asList("LocalPort", "PeerPort", "PeerIp", "Protocol", "WorkMode", "Unuse0",
                    "KeepLive", "Unuse1", "Unuse2", "Baud", "Unuse3", "Databit", "Parity", "Stopbit",
                    "Flow_ctrl", "FrameSize", "ByteInterval", "Unuse4");
        }
        public static class ByReference extends _Port implements Structure.ByReference {

        };
        public static class ByValue extends _Port implements Structure.ByValue {

        };
    };

    public static class _Device extends Structure {

        public Byte Header;                //	帧头部

        public BYTE DeviceType;            //	设备类型

        public BYTE[] Mac = new BYTE[6];   //	设备类型

        public BYTE UartNumber;            //	设备总的串口数量

        public BYTE[] Unuse0 = new BYTE[3];

        public BYTE[] DeviceTypeName = new BYTE[32]; //	设备类型字符串

        public BYTE[] Version = new BYTE[32];        //	设备类型字符串

        public WORD Unuse1;

        public BYTE[] Unuse2 = new BYTE[2];

        public byte[] UserName = new byte[16];    //	用户名

        public byte[] Password = new byte[16];    //	密码

        public byte[] DeviceName = new byte[16];  //	设备名

        public BYTE[] MultiGroup = new BYTE[4];   //	组号

        public WORD[] LocalIP = new WORD[2];    //	设备IP地址

        public WORD[] NetMask = new WORD[2];    //	子网掩码

        public WORD[] GatewayIP = new WORD[2];  //	网关地址

        public byte DHCP;                         //	0：禁止DHCP,1:启用DHCP

        public byte Unuse3;

        public byte TmpGroup;                     //	未定义,临时组临时保存位置，不应使用这个值。

        public byte TalkDialInCount;              //	对讲自动接通呼叫振铃次数，等于时间秒数。

        public WORD[] ServerIP = new WORD[2];   //	服务器的IP地址，音频设备会定时向服务器报告消息。

        public WORD ServerPort;                  //	服务器监听端口，与ServerIP配合使用。

        public byte ComVolume;         //	串口设置的音量大小偏移量，

        public byte TalkVolume;        //	对讲是本机的音量大小。

        public byte DefaultSample;     //	录播时，默认的采样频率0 = 8K；1=16K；2 = 24K；3 = 32K

        public byte DefaultAudioInput; //	对讲和录播时，默认的音频输入端口 0=mic，1=linein

        public byte ButtonMode;        //	按键模式，0=触发模式，1=保持模式

        public byte RecordVolume;      //	录播时，向远端发送数据的音量，默认值。

        public short PADelayTime;      //	功放延时关闭参数，单位秒

        public byte MicGain;           //	默认mic的增益大小0～100

        public byte Unuse5;

        public WORD[] AssistantIp = new WORD[2];    //	对讲呼叫转移设备ip地址，不用呼叫转移应填0.0.0.0

        // CustomServiceIp[0]是第一客服IP
        // CustomServiceIp[1]，CustomServiceIp[2]未用
        // CustomServiceIp[4]是时间服务器IP地址
        public WORD[] CustomServiceIp = new WORD[((4) * (2))];

        public _Port[] Port = new _Port[7];

        public byte UseAssistant;   //	是否允许呼叫转移

        public byte Unuse4;

        public byte TalkMode;        //	对讲模式，0=全双工模式，1=半双工模式

        public byte AECEnable;       //	是否允许AEC，0=运行，1=禁止

        public byte DefaultPlayMode; //	默认播放模式，0=播放模式，1=空闲模式

        public byte[] unuse5 = new byte[2];

        public byte[] MultiGroup2 = new byte[4]; //	第二组多播组，与MultiGroup组成8个组播组；

        public byte[] Unuse = new byte[72];

        public _Device() {
            super();
        }
        protected List<String> getFieldOrder() {
            return Arrays.asList("Header", "DeviceType", "Mac", "UartNumber", "Unuse0", "DeviceTypeName", "Version", "Unuse1", "Unuse2", "UserName", "Password", "DeviceName", "MultiGroup", "LocalIP", "NetMask", "GatewayIP", "DHCP", "Unuse3", "TmpGroup", "TalkDialInCount", "ServerIP", "ServerPort", "ComVolume", "TalkVolume", "DefaultSample", "DefaultAudioInput", "ButtonMode", "RecordVolume", "PADelayTime", "MicGain", "Unuse5", "AssistantIp", "CustomServiceIp", "Port", "UseAssistant", "Unuse4", "TalkMode", "CodecType", "AECEnable", "DefaultPlayMode", "unuse5", "MultiGroup2", "Unuse");
        }
        public static class ByReference extends _Device implements Structure.ByReference {

        }
        public static class ByValue extends _Device implements Structure.ByValue {

        }
    }

    /***********************************************************************************
     函数名称：	np_search_all
     函数功能：	在本地网内搜索NP系列设备
     输入参数：	struct _DeviceInfo * devs	系统参数结构数组指针，用户在调用这个函数前应为这个结构分配空间。
     分配空间不小于Number * sizeof(struct _DeviceInfo);
     搜索到的设备参数写入这个结构中。
     int * number		此指针传入系统开辟的缓存大小，（也就是可以容纳多少个设备的信息），返回搜索到的转换器个数。
     返回值  ：	NP_SUCCESS：		表示操作成功
     其它值：			表示操作出错，返回值请参考函数返回值宏定义。
     其他说明：	搜索局域网内的所有NP设备。
     ***********************************************************************************/
    int np_search_all(_DeviceInfo.ByReference dev, IntByReference number);

    /***********************************************************************************
     函数名称：	np_search_one
     函数功能：	搜索指定IP的NP设备，成功则将该设备的系统参数填入dev结构中
     输入参数：	char * ip		目标转换器的IP地址。ip为以'\0'结尾的字符串。
     struct _DeviceInfo * devs	系统参数结构指针，用户在调用这个函数前应为这个结构分配空间。
     搜索到的设备参数写入这个结构中。
     返回值  ：	NP_SUCCESS：		表示操作成功
     其它值：			表示操作出错，返回值请参考函数返回值宏定义。
     其他说明：	使用IP地址进行设备搜索时，应该注意，目标转换器的IP地址不属本网段而其物理连接实际在本网段的，使用该命令将搜索不到。
     ***********************************************************************************/
    int np_search_one(String ip, _DeviceInfo.ByReference dev);

    /***********************************************************************************
     函数名称：	np_login
     函数功能：	在目标设备上进行登录操作，只有登录操作后，才能使用np_write_setting这个命令。
     输入参数：	char* ip			目标转换器的ip地址。形如"192.168.0.2",以'\0'结尾的字符串。
     char *mac			目标转换器的MAC地址。6个byte长度的数组指针。
     struct _Param* Param调用函数前，用户应填充Param各成员。
     DeviceType成员可通过np_search_all或np_search_one获取，下同。
     返回值  ：	NP_SUCCESS：		表示操作成功
     其它值：			表示操作出错，返回值请参考函数返回值宏定义。
     其他说明：	函数登录指定ip地址的设备，如果ip地址为空，则登录在本网段符合指定mac地址的设备。ip或mac应至少有一个参数是有效的。
     ***********************************************************************************/
    int np_login(String ip, String mac, _Param Param);

    /***********************************************************************************
     函数名称：	np_logout
     函数功能：	在目标设备上进行登出操作，使用np_login命令并操作完成后，应进行登出操作。
     输入参数：	char* ip			目标转换器的ip地址。形如"192.168.0.2",以'\0'结尾的字符串。
     char *mac			目标转换器的MAC地址。6个byte长度的数组指针。
     struct _Param* Param调用函数前，用户应填充Param的DeviceType。
     返回值  ：	NP_SUCCESS：		表示操作成功
     其它值：			表示操作出错，返回值请参考函数返回值宏定义。
     其他说明：	函数登出指定ip地址的设备，如果ip地址为空，则登出在本网段符合指定mac地址的设备。ip或mac应至少有一个参数是有效的。
     ***********************************************************************************/
    int np_logout(String ip, String mac, NaSetup._Param Param);

    /***********************************************************************************
     函数名称：	np_read_setting
     函数功能：	读取指定IP或Mac地址的NP设备系统参数
     输入参数：	char* ip			目标转换器的ip地址。形如"192.168.0.2",以'\0'结尾的字符串。
     char *mac			目标转换器的MAC地址。6个byte长度的数组指针。
     void* dev			系统参数缓存指针，用户在调用这个函数前应为这个缓存分配空间，缓存不小于sizeof(struct _DeviceEx);
     读取的设备参数写入到这个缓冲中。
     函数详细说明及调用实例请参考动态库使用说明。

     struct _Param* Param搜索参数选项，调用函数前，用户应填充Param各成员。
     返回值  ：	NP_SUCCESS：		表示操作成功。
     其它值：			表示操作出错，返回值请参考函数返回值宏定义。
     其他说明：	函数读取指定ip地址的设备参数，如果ip地址为空，函数在本网段读取符合指定mac地址设备的参数。ip或mac应至少有一个参数是有效的。
     ***********************************************************************************/
    int np_read_setting(String ip, String mac, Pointer dev, NaSetup._Param Param);

    //下面的读参数函数适用设备固件版本>v2.5 设备
    int np_read_setting2(String ip, String mac, Pointer dev, NaSetup._Param Param);

    /***********************************************************************************
     函数名称：	np_write_setting
     函数功能：	设置指定指定IP或Mac地址的NP设备系统参数
     输入参数：	char* ip			目标转换器的ip地址。以'\0'结尾的字符串。
     char *mac			目标转换器的MAC地址。6个byte长度的数组指针。
     void* dev			系统参数缓存，用户在调用这个函数前应为这个结构分配空间，读取的设备参数填入这个缓存中。
     函数详细说明及调用实例请参考动态库使用说明。
     int datalength		写入数据的长度。
     struct _Param* Param搜索参数选项，调用函数前，用户应填充Param各成员。
     返回值  ：	NP_SUCCESS：		表示操作成功
     其它值：			表示操作出错，返回值请参考函数返回值宏定义。
     其他说明：	函数设置指定ip地址的设备参数，如果ip地址为空，函数在本网段设置符合指定mac地址设备的参数。ip或mac应至少有一个参数是有效的。
     ***********************************************************************************/
    int np_write_setting(String ip, String mac, Pointer dev, int DataLength, NaSetup._Param Param);

    // 下面的写参数函数适用设备固件版本>v2.5 设备
    int np_write_setting2(String ip, String mac, Pointer dev, int DataLength, NaSetup._Param Param);
}
