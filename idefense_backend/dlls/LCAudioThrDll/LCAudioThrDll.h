#ifndef __LCAUDIOTHRDLL_H_
#define __LCAUDIOTHRDLL_H_


#ifdef __cplusplus
extern "C"
{
#endif

/*定义网络通信方式*/
#define cUnicast	0	//单播
#define cMulticast	1	//组播
#define cBroadcast	2	//广播


#define AUDIO_TYPE_UNKNOWN 0
#define AUDIO_TYPE_MP3  1
#define AUDIO_TYPE_WAV  2
#define AUDIO_TYPE_WMA  3

#define SCR_TYPE_FILE 0			//数据来源是文件
#define SCR_TYPE_AUDIOCARD 1	//数据来源是声卡
#define SCR_TYPE_BUFFER 2		//数据来源是内存缓存
#define SCR_TYPE_STREAM 3		//数据来源是数据流//V2.0.3.0

/*定义函数返回值*/
#define R_OK		0		//成功
#define ERR_PARAM -1		//参数错误
#define ERR_OPT   -2		//函数执行错误
#define ERR_SOCKET -3		//socket操作失败
#define ERR_CODEC -4	//初始化编解码器失败。

/*Windows 消息定义*/
//exception
//#define WM_MSG_EXCEPTION	(WM_USER+100)
#define WM_MSG_COMPLETED    (WM_USER+101)
#define WM_MSG_PAUSE	    (WM_USER+102)
#define WM_MSG_CONTINUE	    (WM_USER+103)
#define WM_MSG_AUDIOPOWER   (WM_USER+104)


//#define WM_MSG_DATA			(WM_USER+9901)

/*文件信息*/
struct _FileInfo
{
	int Duration;			//播放时长
	int SampleRate;			//采样率
	int BitRate;			//比特率
};

/*线程播放结构*/
struct _PlayParam
{
	HWND hWnd;						//主窗口的句柄，如果不为0，则线程有事件会向该窗口发送消息
	int Priority;					//优先级
	int MultiGroup;					//多播组号
	int	CastMode;					//传输模式，单播，多播，广播
	long IP;						//ip，如果是广播和多播，此参数是源网卡的IP，如果此地址为0，则由系统决定使用哪个网卡，如果是单播，这是个目标设备的ip地址。
	int   Volume;					//播放音量取值0～100
	int	  Tone;						//音调
	int   Treble;					//高音频率
	int   Bass;						//低音频率
	int   Treble_En;				//高音增益
	int   Bass_En;					//低音增益
	short   SourceType;				//输入源，0为文件，1为声卡
	short  OptionByte;
	int DeviceID;					//音频输入ID号 1～N
	unsigned char MuxName[64];		//混音器的通道名字
	int nChannels;					//采样的通道 1～2 CodecType
	int nSamplesPerSec;				//采样频率 8K，11.025K,22.05K,44.1K
	int   AudioBufferLength;		//Audio数据的长度
	unsigned char* AudioBuf;		//Audio数据的指针
	unsigned int PrivateData[128];	//私有信息，lc_init初始化后，用户不能修改里面的内容。
};
/*
#pragma pack(1)
struct _MpegPack
{
	char Cmd;
	char SumCmd;
	WORD Length;
	int  Factor;
	char volume;
	char tone;
	char treble;
	char bass;
	char CtrlByte;
	char Unuse;
	signed char treble_en;
	signed char bass_en;
	char Data[NET_PACK_SIZE];
};
#pragma
*/
/*声卡信息结构*/
struct _WaveInInfo
{
	int				Index;		//	输入通道的序号
    char			name[32];	//  名字
    unsigned long	Formats;	//  支持的格式
    int				Channels;	//  通道数 2为立体声输入。
};

struct _MuxInfo
{
	char name[64];	
};

struct _PlayParam*  __stdcall lc_play_getmem(void);
//extern "C" 
int __stdcall lc_play_freemem(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_init(unsigned char* pFileName, struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_play(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_stop(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_pause(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_continue(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_seek(struct _PlayParam* pParam, int time);
//extern "C" 
int __stdcall lc_wait(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_set_volume(struct _PlayParam* pParam, char volume);
//extern "C" 
int __stdcall lc_get_playtime(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_get_playstatus(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_get_duration(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_get_fileinfo(unsigned char* pFileName, struct _FileInfo* pFileInfo);
//extern "C" 
int __stdcall lc_get_version(void);
//extern "C" 
int __stdcall lc_rec_devinfo(struct _WaveInInfo* Info,int* Number);
//extern "C" 
int __stdcall lc_rec_muxinfo(int Index, struct _MuxInfo* Info, int* Number);
//extern "C" 
int __stdcall lc_rec_getmicname(int Index, char* pName, int NameSize);
//extern "C" 
int __stdcall lc_rec_setmicvolume(int Index, int volume);
//extern "C" 
int __stdcall lc_getlasterror(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_record_start(struct _PlayParam* pParam,char* filename);
//extern "C" 
int __stdcall lc_record_stop(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_record_status(struct _PlayParam* pParam);
//extern "C" 
int __stdcall lc_inputdata(struct _PlayParam* pParam, char* buf, int datalen);
//extern "C" 
int __stdcall lc_addip(struct _PlayParam* pParam, DWORD IP);
//extern "C" 
int __stdcall lc_rec_addip(struct _PlayParam* pParam, DWORD IP);
//extern "C" 
int __stdcall lc_delip(struct _PlayParam* pParam, DWORD IP);
//extern "C" 
int __stdcall lc_rec_delip(struct _PlayParam* pParam, DWORD IP);


#ifdef __cplusplus
}
#endif

#endif

