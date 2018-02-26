#ifndef __LCAUDIOTHRDLL_H_
#define __LCAUDIOTHRDLL_H_


#ifdef __cplusplus
extern "C"
{
#endif

/*��������ͨ�ŷ�ʽ*/
#define cUnicast	0	//����
#define cMulticast	1	//�鲥
#define cBroadcast	2	//�㲥


#define AUDIO_TYPE_UNKNOWN 0
#define AUDIO_TYPE_MP3  1
#define AUDIO_TYPE_WAV  2
#define AUDIO_TYPE_WMA  3

#define SCR_TYPE_FILE 0			//������Դ���ļ�
#define SCR_TYPE_AUDIOCARD 1	//������Դ������
#define SCR_TYPE_BUFFER 2		//������Դ���ڴ滺��
#define SCR_TYPE_STREAM 3		//������Դ��������//V2.0.3.0

/*���庯������ֵ*/
#define R_OK		0		//�ɹ�
#define ERR_PARAM -1		//��������
#define ERR_OPT   -2		//����ִ�д���
#define ERR_SOCKET -3		//socket����ʧ��
#define ERR_CODEC -4	//��ʼ���������ʧ�ܡ�

/*Windows ��Ϣ����*/
//exception
//#define WM_MSG_EXCEPTION	(WM_USER+100)
#define WM_MSG_COMPLETED    (WM_USER+101)
#define WM_MSG_PAUSE	    (WM_USER+102)
#define WM_MSG_CONTINUE	    (WM_USER+103)
#define WM_MSG_AUDIOPOWER   (WM_USER+104)


//#define WM_MSG_DATA			(WM_USER+9901)

/*�ļ���Ϣ*/
struct _FileInfo
{
	int Duration;			//����ʱ��
	int SampleRate;			//������
	int BitRate;			//������
};

/*�̲߳��Žṹ*/
struct _PlayParam
{
	HWND hWnd;						//�����ڵľ���������Ϊ0�����߳����¼�����ô��ڷ�����Ϣ
	int Priority;					//���ȼ�
	int MultiGroup;					//�ಥ���
	int	CastMode;					//����ģʽ���������ಥ���㲥
	long IP;						//ip������ǹ㲥�Ͷಥ���˲�����Դ������IP������˵�ַΪ0������ϵͳ����ʹ���ĸ�����������ǵ��������Ǹ�Ŀ���豸��ip��ַ��
	int   Volume;					//��������ȡֵ0��100
	int	  Tone;						//����
	int   Treble;					//����Ƶ��
	int   Bass;						//����Ƶ��
	int   Treble_En;				//��������
	int   Bass_En;					//��������
	short   SourceType;				//����Դ��0Ϊ�ļ���1Ϊ����
	short  OptionByte;
	int DeviceID;					//��Ƶ����ID�� 1��N
	unsigned char MuxName[64];		//��������ͨ������
	int nChannels;					//������ͨ�� 1��2 CodecType
	int nSamplesPerSec;				//����Ƶ�� 8K��11.025K,22.05K,44.1K
	int   AudioBufferLength;		//Audio���ݵĳ���
	unsigned char* AudioBuf;		//Audio���ݵ�ָ��
	unsigned int PrivateData[128];	//˽����Ϣ��lc_init��ʼ�����û������޸���������ݡ�
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
/*������Ϣ�ṹ*/
struct _WaveInInfo
{
	int				Index;		//	����ͨ�������
    char			name[32];	//  ����
    unsigned long	Formats;	//  ֧�ֵĸ�ʽ
    int				Channels;	//  ͨ���� 2Ϊ���������롣
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

