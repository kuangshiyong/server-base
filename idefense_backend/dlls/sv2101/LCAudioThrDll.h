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
#define cMulticast2	3	//二类组播


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
#define ERR_FILE_FAULT -5	//读取文件失败
#define ERR_MEM_FAULT -6		//读取文件失败
#define ERR_FILEFORMAT_FAULT -7 //
#define ERR_BAD_BITRATE -8
#define ERR_DECODE_DISABLE -9	//应用程序禁用解码功能，而播放此文件需要解码

/*Windows 消息定义*/
//#define WM_MSG_EXCEPTION	(WM_USER+100)
#define WM_MSG_COMPLETED    (WM_USER+101)
#define WM_MSG_PAUSE	    (WM_USER+102)
#define WM_MSG_CONTINUE	    (WM_USER+103)
#define WM_MSG_AUDIOPOWER   (WM_USER+104)
#define WM_MSG_SOUNDCARD	(WM_USER+105)

//_PlayParam.CtrlByte控制字定义
//重采样选项，在录播模式时,因计算机采样与播放设备回放频率的差异,可能导致时延,
//选择重采样选项,动态库自动处理这个问题,但带来轻微的失真。
#define OPT_RESAMPLE 0X0001	
#define OPT_STREAM   0X0002
//禁用解码器，因为解码器不是线程安全的，故有些多线程应用需要禁用，以保证稳定。
#define OPT_DECODE_DISABLE   0X0004

/*文件信息*/
struct _FileInfo
{
	int Duration;			//播放时长
	int SampleRate;			//采样率
	int BitRate;			//比特率
};

/*线程播放结构*/
#pragma pack(1)
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
	unsigned short   SourceType;	//输入源，0为文件，1为声卡
	unsigned short   OptionByte;	//选项字，默认为0;bit0=1 禁止重采样，bit1=1，启动监听，bit2=1，禁用解码功能（仅播放符合要求的音频文件）
	int DeviceID;					//音频输入ID号 1～N
	//unsigned char MuxName[64];		//混音器的通道名字
	int MaxBitrate;					//允许最大的比特率组合，如果源文件高于此比特率，将被重压缩至此比特率。
	unsigned int Options[15];		//选项
	int nChannels;					//采样的通道 1～2 CodecType
	int nSamplesPerSec;				//采样频率 8K，11.025K,22.05K,44.1K
	int AudioBufferLength;		//Audio数据的长度
	unsigned char* AudioBuf;		//Audio数据的指针
	unsigned int PrivateData[128];	//私有信息，lc_init初始化后，用户不能修改里面的内容。
};
#pragma

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

void* __stdcall lc_play_getmem(void);
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
int __stdcall lc_wait_time(struct _PlayParam* pParam,int tv);//tv以ms为单位
//extern "C" 
int __stdcall lc_set_volume(struct _PlayParam* pParam, char volume);
//extern "C" 
int __stdcall lc_set_priority(struct _PlayParam* pParam, char priority);
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

//必须是192kbps一下mp3
int __stdcall lc_inputdata_mp3(struct _PlayParam* pParam, char* buf, int datalen);

int __stdcall lc_get_datasize(struct _PlayParam* pParam);	//查询流模式下，当前的播放缓存的数据量。
//extern "C" 
int __stdcall lc_addip(struct _PlayParam* pParam, DWORD IP);
//extern "C" 
int __stdcall lc_rec_addip(struct _PlayParam* pParam, DWORD IP);
//extern "C" 
int __stdcall lc_delip(struct _PlayParam* pParam, DWORD IP);
//extern "C" 
int __stdcall lc_rec_delip(struct _PlayParam* pParam, DWORD IP);

int __stdcall lc_set_stream(struct _PlayParam* pParam, int enable);

#ifdef __cplusplus
}
#endif

#endif

