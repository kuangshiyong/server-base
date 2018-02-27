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
#define cMulticast2	3	//�����鲥


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
#define ERR_FILE_FAULT -5	//��ȡ�ļ�ʧ��
#define ERR_MEM_FAULT -6		//��ȡ�ļ�ʧ��
#define ERR_FILEFORMAT_FAULT -7 //
#define ERR_BAD_BITRATE -8
#define ERR_DECODE_DISABLE -9	//Ӧ�ó�����ý��빦�ܣ������Ŵ��ļ���Ҫ����

/*Windows ��Ϣ����*/
//#define WM_MSG_EXCEPTION	(WM_USER+100)
#define WM_MSG_COMPLETED    (WM_USER+101)
#define WM_MSG_PAUSE	    (WM_USER+102)
#define WM_MSG_CONTINUE	    (WM_USER+103)
#define WM_MSG_AUDIOPOWER   (WM_USER+104)
#define WM_MSG_SOUNDCARD	(WM_USER+105)

//_PlayParam.CtrlByte�����ֶ���
//�ز���ѡ���¼��ģʽʱ,�����������벥���豸�ط�Ƶ�ʵĲ���,���ܵ���ʱ��,
//ѡ���ز���ѡ��,��̬���Զ������������,��������΢��ʧ�档
#define OPT_RESAMPLE 0X0001	
#define OPT_STREAM   0X0002
//���ý���������Ϊ�����������̰߳�ȫ�ģ�����Щ���߳�Ӧ����Ҫ���ã��Ա�֤�ȶ���
#define OPT_DECODE_DISABLE   0X0004

/*�ļ���Ϣ*/
struct _FileInfo
{
	int Duration;			//����ʱ��
	int SampleRate;			//������
	int BitRate;			//������
};

/*�̲߳��Žṹ*/
#pragma pack(1)
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
	unsigned short   SourceType;	//����Դ��0Ϊ�ļ���1Ϊ����
	unsigned short   OptionByte;	//ѡ���֣�Ĭ��Ϊ0;bit0=1 ��ֹ�ز�����bit1=1������������bit2=1�����ý��빦�ܣ������ŷ���Ҫ�����Ƶ�ļ���
	int DeviceID;					//��Ƶ����ID�� 1��N
	//unsigned char MuxName[64];		//��������ͨ������
	int MaxBitrate;					//�������ı�������ϣ����Դ�ļ����ڴ˱����ʣ�������ѹ�����˱����ʡ�
	unsigned int Options[15];		//ѡ��
	int nChannels;					//������ͨ�� 1��2 CodecType
	int nSamplesPerSec;				//����Ƶ�� 8K��11.025K,22.05K,44.1K
	int AudioBufferLength;		//Audio���ݵĳ���
	unsigned char* AudioBuf;		//Audio���ݵ�ָ��
	unsigned int PrivateData[128];	//˽����Ϣ��lc_init��ʼ�����û������޸���������ݡ�
};
#pragma

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
int __stdcall lc_wait_time(struct _PlayParam* pParam,int tv);//tv��msΪ��λ
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

//������192kbpsһ��mp3
int __stdcall lc_inputdata_mp3(struct _PlayParam* pParam, char* buf, int datalen);

int __stdcall lc_get_datasize(struct _PlayParam* pParam);	//��ѯ��ģʽ�£���ǰ�Ĳ��Ż������������
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

