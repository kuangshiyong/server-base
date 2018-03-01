#ifndef __NP_H_
#define __NP_H_

#define USER_LEN			16
#define PASSWORD_LEN		16
#define DEVICE_NAME_LEN		16
#define VERSION_LEN			32
#define URL_LEN				32
#define DEVICE_TYPENAME_LEN 32

//	**********	�����Ƿ���ֵ�궨��
#define		NP_SUCCESS				1		//	���������ɹ�����ȷ����
#define		NP_NET_INTERFACE_ERROR	-1		//	�����������
#define		NP_DATA_ERROR			-2		//	���ز����������ݣ��������ɹ�
#define		NP_DEVICE_NOT_EXIST		-3		//	�����ݷ��أ��޷���ϵ�豸
#define		NP_WP_ERROR				-4		//	����������д����
#define		NP_PARAM_ERROR			-5		//	��ڲ�������
#define		NP_PASSWORD_ERROR		-6		//	�������
#define		NP_MEMORY_ERROR			-7		//	�ڴ治�����
//	**********	�����Ƕ���ͨ�����ض�Ӧ����
#define  FC_NONE			= 0;
#define	 FC_RTC_CTS		= 1;

//	**********	�����Ƕ���ͨ��Э���Ӧ����
#define	 	UDP				 0;
#define	 	TCP				 1;
#define	 	REAL_COM		 2;
#define	 	MODBUS_TCP		 3;

//	**********	�����Ƕ��幤��ģʽ��Ӧ����
#define	 	SERVER_MODE		 0;		//	������ģʽ
#define	 	AUTO_MODE		 1;		//	�Զ�ģʽ
#define	 	CLIENT_MODE		 2;		//	�ͻ���ģʽ
#define	 	MULTI_MODE		 3;		//	������ģʽ

//	**********	�����Ƕ��崮��У�鷽ʽ��Ӧ����
#define	 	PR_NONE			 0;		//	��У��
#define	 	PR_ODD			 1;		//	��У��
#define	 	PR_EVEN			 2;		//	żУ��
#define	 	PR_MARK			 3;		//	���У��
#define	 	PR_SPACE		 4;		//	�ո�У��

//	**********	�����Ƕ��崮�ڲ����ʶ�Ӧ����
#define	 	BAUD_1200BPS	 12;		//1200bps
#define	 	BAUD_2400BPS	 24;		//2400bps
#define	 	BAUD_4800BPS	 48;		//4800bps
#define	 	BAUD_9600BPS	 96;		//9600bps
#define	 	BAUD_19200BPS	 192;		//19200bps
#define	 	BAUD_38400BPS	 384;		//38400bps
#define	 	BAUD_57600BPS	 576;		//57600bps
#define	 	BAUD_115200BPS	 1152;		//115200bps


struct _Param
{
	BYTE	DeviceType;				//	�豸���
	char	Unuse0[3];				//	δ����
	char	User[USER_LEN];			//	�û���
	char	Password[PASSWORD_LEN];	//	����
	BYTE	ComName;				//	��Ҫ���ʵĴ��ںš�
	char	Unuse1[3];				//	δ����
	char	Unuse2[24];				//	δ����
};



#pragma pack(1)

struct _DeviceInfo
{
	BYTE	DeviceType;							//	�豸���
	BYTE    Unuse0[3];							//  δ����
	char	Version[VERSION_LEN];				//	�̼��汾
	char	DeviceName[DEVICE_NAME_LEN];		//	�豸����
	char	Mac[6];								//	�豸MAC��ַ
	WORD	LocalPort;							//	�豸�����˿�
	WORD	LocalIP[2];							//	�豸��̬IP��ַ
	WORD	PeerIP[2];							//	������IP
	WORD	NetMask[2];							//  ��������
	WORD	GatewayIP[2];						//	���ص�ַ
	WORD	Unuse1[2];							//	δ����
	WORD	Unuse2[2];							//	δ����
	char	Unuse3[28];							//	δ����
	char	DeviceTypeName[DEVICE_TYPENAME_LEN];//	�豸�����ַ�����
	WORD	DeviceID;							//	�豸��ID�ţ��豸��ID�ţ�������NL9080��
	char    UartNumber;							//	�豸���еĴ�������
	char	Unuse4;								//	δ����
	char	MultiGroup[4];						//  �鲥��ַ
	char	MultiGroup2[4];
	WORD	Interface[2];						//	�յ�����֡������ӿڣ�v2.1.0.3���ϰ汾��Ч
	char	Unuse5[200];						//	δ����
};

struct _Port
{
	WORD  LocalPort;				//	���ض˿�
	WORD  PeerPort;					//	Զ�˶˿�
    WORD  PeerIp[2];				//	Զ��ip��ַ
    BYTE  Protocol;					//	����Э������
	BYTE  WorkMode;				//	����ģʽ
	BYTE  Unuse0;					//  δ����
	BYTE  KeepLive;				//  ����ʱ��
    BYTE  Unuse1[32];				//	δ����
	BYTE  Unuse2[4];				//	δ����
	WORD  Baud;						//	���ڲ�����
	BYTE  Unuse3[2];				//	δ����
    BYTE  Databit;					//	��������λ����
    BYTE  Parity;					//	����У�鷽ʽ
    BYTE  Stopbit;					//	����ֹͣλ����
    BYTE  Flow_ctrl;				//	�������ط�ʽ
    WORD  FrameSize;				//	����������ݰ�����
	BYTE  ByteInterval;				//	��������ַ����ʱ��
	BYTE  Unuse4[9];				//	δ����
};

struct _Device
{
	BYTE Header;								//	֡ͷ��
	BYTE DeviceType;							//	�豸����
	BYTE Mac[6];								//	�豸mac��ַ
	BYTE UartNumber;							//	�豸�ܵĴ�������
	BYTE Unuse0[3];								//	δ����
	BYTE DeviceTypeName[DEVICE_TYPENAME_LEN];	//	�豸�����ַ���
	BYTE Version[VERSION_LEN];					//	�豸�̼��汾
	WORD  Unuse1;								//	δ����
	BYTE  Unuse2[2];							//	δ����
	char  UserName[USER_LEN];					//	�û���
	char  Password[PASSWORD_LEN];				//	����
	char  DeviceName[DEVICE_NAME_LEN];			//	�豸��
	BYTE  MultiGroup[4];						//	���
	WORD  LocalIP[2];							//	�豸IP��ַ
    WORD  NetMask[2];							//	��������
    WORD  GatewayIP[2];							//	���ص�ַ
	char  DHCP;									//	0����ֹDHCP,1:����DHCP
	char  Unuse3;								//	δ����
    char  TmpGroup;								//	δ����,��ʱ����ʱ����λ�ã���Ӧʹ�����ֵ��
    char  TalkDialInCount;						//	�Խ��Զ���ͨ�����������������ʱ��������	
    
    WORD  ServerIP[2];							//	��������IP��ַ����Ƶ�豸�ᶨʱ�������������Ϣ��
    WORD ServerPort;							//	�����������˿ڣ���ServerIP���ʹ�á�

	char ComVolume;								//	�������õ�������Сƫ������
	char TalkVolume;							//	�Խ��Ǳ�����������С��
	
	char DefaultSample;							//	¼��ʱ��Ĭ�ϵĲ���Ƶ��0 = 8K��1=16K��2 = 24K��3 = 32K
	char DefaultAudioInput;						//	�Խ���¼��ʱ��Ĭ�ϵ���Ƶ����˿� 0=mic��1=linein
	char ButtonMode;							//	����ģʽ��0=����ģʽ��1=����ģʽ
	char RecordVolume;							//	¼��ʱ����Զ�˷������ݵ�������Ĭ��ֵ��
	WORD PADelayTime;							//	������ʱ�رղ�������λ��
	char MicGain;								//	Ĭ��mic�������С0��100
	char Unuse5;
	WORD AssistantIp[2];						//	�Խ�����ת���豸ip��ַ�����ú���ת��Ӧ��0.0.0.0
	WORD CustomServiceIp[4][2];					//	CustomServiceIp[0]�ǵ�һ�ͷ�IP
												//	CustomServiceIp[1]��CustomServiceIp[2]δ��
												//	CustomServiceIp[4]��ʱ�������IP��ַ
	struct _Port Port[7];
	char UseAssistant;							//	�Ƿ��������ת��
	char Unuse4;
	char TalkMode;								//	�Խ�ģʽ��0=ȫ˫��ģʽ��1=��˫��ģʽ
	char CodecType;								//	�Խ����ݱ����ʽ��0=pcm��1=adpcm
	char AECEnable;								//	�Ƿ�����AEC��0=���У�1=��ֹ
	char DefaultPlayMode;						//	Ĭ�ϲ���ģʽ��0=����ģʽ��1=����ģʽ
	char unuse5[2];
	char MultiGroup2[4];						//	�ڶ���ಥ�飬��MultiGroup���8���鲥�飻
	char Unuse[72];	
};

#pragma pack()
/***********************************************************************************
�������ƣ�	np_search_all
�������ܣ�	�ڱ�����������NPϵ���豸
���������	struct _DeviceInfo * devs	ϵͳ�����ṹ����ָ�룬�û��ڵ����������ǰӦΪ����ṹ����ռ䡣
								����ռ䲻С��Number * sizeof(struct _DeviceInfo);
								���������豸����д������ṹ�С�
			int * number		��ָ�봫��ϵͳ���ٵĻ����С����Ҳ���ǿ������ɶ��ٸ��豸����Ϣ����������������ת����������
����ֵ  ��	NP_SUCCESS��		��ʾ�����ɹ�
			����ֵ��			��ʾ������������ֵ��ο���������ֵ�궨�塣
����˵����	�����������ڵ�����NP�豸��
***********************************************************************************/
extern "C" int __stdcall np_search_all(struct _DeviceInfo* devs, int* number);


/***********************************************************************************
�������ƣ�	np_search_one
�������ܣ�	����ָ��IP��NP�豸���ɹ��򽫸��豸��ϵͳ��������dev�ṹ��
���������	char * ip		Ŀ��ת������IP��ַ��ipΪ��'\0'��β���ַ�����
			struct _DeviceInfo * devs	ϵͳ�����ṹָ�룬�û��ڵ����������ǰӦΪ����ṹ����ռ䡣
								���������豸����д������ṹ�С�
����ֵ  ��	NP_SUCCESS��		��ʾ�����ɹ�
			����ֵ��			��ʾ������������ֵ��ο���������ֵ�궨�塣
����˵����	ʹ��IP��ַ�����豸����ʱ��Ӧ��ע�⣬Ŀ��ת������IP��ַ���������ζ�����������ʵ���ڱ����εģ�ʹ�ø��������������
***********************************************************************************/
extern "C" int __stdcall np_search_one(char* ip, struct _DeviceInfo* dev);






/***********************************************************************************
�������ƣ�	np_login
�������ܣ�	��Ŀ���豸�Ͻ��е�¼������ֻ�е�¼�����󣬲���ʹ��np_write_setting������
���������	char* ip			Ŀ��ת������ip��ַ������"192.168.0.2",��'\0'��β���ַ�����
			char *mac			Ŀ��ת������MAC��ַ��6��byte���ȵ�����ָ�롣
			struct _Param* Param���ú���ǰ���û�Ӧ���Param����Ա��
								DeviceType��Ա��ͨ��np_search_all��np_search_one��ȡ����ͬ��
����ֵ  ��	NP_SUCCESS��		��ʾ�����ɹ�
			����ֵ��			��ʾ������������ֵ��ο���������ֵ�궨�塣
����˵����	������¼ָ��ip��ַ���豸�����ip��ַΪ�գ����¼�ڱ����η���ָ��mac��ַ���豸��ip��macӦ������һ����������Ч�ġ�
***********************************************************************************/
extern "C" int __stdcall np_login(char* ip,char* mac,struct _Param* Param);





/***********************************************************************************
�������ƣ�	np_logout
�������ܣ�	��Ŀ���豸�Ͻ��еǳ�������ʹ��np_login���������ɺ�Ӧ���еǳ�������
���������	char* ip			Ŀ��ת������ip��ַ������"192.168.0.2",��'\0'��β���ַ�����
			char *mac			Ŀ��ת������MAC��ַ��6��byte���ȵ�����ָ�롣
			struct _Param* Param���ú���ǰ���û�Ӧ���Param��DeviceType��
����ֵ  ��	NP_SUCCESS��		��ʾ�����ɹ�
			����ֵ��			��ʾ������������ֵ��ο���������ֵ�궨�塣
����˵����	�����ǳ�ָ��ip��ַ���豸�����ip��ַΪ�գ���ǳ��ڱ����η���ָ��mac��ַ���豸��ip��macӦ������һ����������Ч�ġ�
***********************************************************************************/
extern "C" int __stdcall np_logout(char* ip,char* mac,struct _Param* Param);




/***********************************************************************************
�������ƣ�	np_read_setting
�������ܣ�	��ȡָ��IP��Mac��ַ��NP�豸ϵͳ����
���������	char* ip			Ŀ��ת������ip��ַ������"192.168.0.2",��'\0'��β���ַ�����
			char *mac			Ŀ��ת������MAC��ַ��6��byte���ȵ�����ָ�롣
			void* dev			ϵͳ��������ָ�룬�û��ڵ����������ǰӦΪ����������ռ䣬���治С��sizeof(struct _DeviceEx);
								��ȡ���豸����д�뵽��������С�
								������ϸ˵��������ʵ����ο���̬��ʹ��˵����

			struct _Param* Param��������ѡ����ú���ǰ���û�Ӧ���Param����Ա��
����ֵ  ��	NP_SUCCESS��		��ʾ�����ɹ���
			����ֵ��			��ʾ������������ֵ��ο���������ֵ�궨�塣
����˵����	������ȡָ��ip��ַ���豸���������ip��ַΪ�գ������ڱ����ζ�ȡ����ָ��mac��ַ�豸�Ĳ�����ip��macӦ������һ����������Ч�ġ�
***********************************************************************************/
extern "C" int __stdcall np_read_setting(char* ip,char* mac, void* dev,struct _Param* Param);

//����Ķ��������������豸�̼��汾>v2.5 �豸
extern "C" int __stdcall np_read_setting2(char* ip,char* mac, void* dev,struct _Param* Param);

/***********************************************************************************
�������ƣ�	np_write_setting
�������ܣ�	����ָ��ָ��IP��Mac��ַ��NP�豸ϵͳ����
���������	char* ip			Ŀ��ת������ip��ַ����'\0'��β���ַ�����
			char *mac			Ŀ��ת������MAC��ַ��6��byte���ȵ�����ָ�롣
			void* dev			ϵͳ�������棬�û��ڵ����������ǰӦΪ����ṹ����ռ䣬��ȡ���豸����������������С�
								������ϸ˵��������ʵ����ο���̬��ʹ��˵����
			int datalength		д�����ݵĳ��ȡ�
			struct _Param* Param��������ѡ����ú���ǰ���û�Ӧ���Param����Ա��
����ֵ  ��	NP_SUCCESS��		��ʾ�����ɹ�
			����ֵ��			��ʾ������������ֵ��ο���������ֵ�궨�塣
����˵����	��������ָ��ip��ַ���豸���������ip��ַΪ�գ������ڱ��������÷���ָ��mac��ַ�豸�Ĳ�����ip��macӦ������һ����������Ч�ġ�
***********************************************************************************/
//extern "C" int __stdcall np_write_setting(char* ip,char* mac, struct _Device* dev,struct _Param* Param);
extern "C" int __stdcall np_write_setting(char* ip,char* mac, void* dev,int DataLength,struct _Param* Param);
//�����д�������������豸�̼��汾>v2.5 �豸
extern "C" int __stdcall np_write_setting2(char* ip,char* mac, void* dev,int DataLength,struct _Param* Param);


#endif
