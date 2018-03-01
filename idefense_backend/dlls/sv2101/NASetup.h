#ifndef __NP_H_
#define __NP_H_

#define USER_LEN			16
#define PASSWORD_LEN		16
#define DEVICE_NAME_LEN		16
#define VERSION_LEN			32
#define URL_LEN				32
#define DEVICE_TYPENAME_LEN 32

//	**********	以下是返回值宏定义
#define		NP_SUCCESS				1		//	函数操作成功，正确返回
#define		NP_NET_INTERFACE_ERROR	-1		//	网络操所错误
#define		NP_DATA_ERROR			-2		//	返回不期望的数据，操作不成功
#define		NP_DEVICE_NOT_EXIST		-3		//	无数据返回，无法联系设备
#define		NP_WP_ERROR				-4		//	参数保护，写错误
#define		NP_PARAM_ERROR			-5		//	入口参数错误
#define		NP_PASSWORD_ERROR		-6		//	密码错误
#define		NP_MEMORY_ERROR			-7		//	内存不足错误
//	**********	以下是定义通信流控对应参数
#define  FC_NONE			= 0;
#define	 FC_RTC_CTS		= 1;

//	**********	以下是定义通信协议对应参数
#define	 	UDP				 0;
#define	 	TCP				 1;
#define	 	REAL_COM		 2;
#define	 	MODBUS_TCP		 3;

//	**********	以下是定义工作模式对应参数
#define	 	SERVER_MODE		 0;		//	服务器模式
#define	 	AUTO_MODE		 1;		//	自动模式
#define	 	CLIENT_MODE		 2;		//	客户端模式
#define	 	MULTI_MODE		 3;		//	多连接模式

//	**********	以下是定义串口校验方式对应参数
#define	 	PR_NONE			 0;		//	无校验
#define	 	PR_ODD			 1;		//	奇校验
#define	 	PR_EVEN			 2;		//	偶校验
#define	 	PR_MARK			 3;		//	标记校验
#define	 	PR_SPACE		 4;		//	空格校验

//	**********	以下是定义串口波特率对应参数
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
	BYTE	DeviceType;				//	设备类别
	char	Unuse0[3];				//	未定义
	char	User[USER_LEN];			//	用户名
	char	Password[PASSWORD_LEN];	//	密码
	BYTE	ComName;				//	需要访问的串口号。
	char	Unuse1[3];				//	未定义
	char	Unuse2[24];				//	未定义
};



#pragma pack(1)

struct _DeviceInfo
{
	BYTE	DeviceType;							//	设备类别
	BYTE    Unuse0[3];							//  未定义
	char	Version[VERSION_LEN];				//	固件版本
	char	DeviceName[DEVICE_NAME_LEN];		//	设备名称
	char	Mac[6];								//	设备MAC地址
	WORD	LocalPort;							//	设备监听端口
	WORD	LocalIP[2];							//	设备动态IP地址
	WORD	PeerIP[2];							//	服务器IP
	WORD	NetMask[2];							//  子网掩码
	WORD	GatewayIP[2];						//	网关地址
	WORD	Unuse1[2];							//	未定义
	WORD	Unuse2[2];							//	未定义
	char	Unuse3[28];							//	未定义
	char	DeviceTypeName[DEVICE_TYPENAME_LEN];//	设备类型字符串。
	WORD	DeviceID;							//	设备的ID号，设备的ID号，仅用于NL9080中
	char    UartNumber;							//	设备具有的串口数量
	char	Unuse4;								//	未定义
	char	MultiGroup[4];						//  组播地址
	char	MultiGroup2[4];
	WORD	Interface[2];						//	收到数据帧的网络接口，v2.1.0.3以上版本有效
	char	Unuse5[200];						//	未定义
};

struct _Port
{
	WORD  LocalPort;				//	本地端口
	WORD  PeerPort;					//	远端端口
    WORD  PeerIp[2];				//	远端ip地址
    BYTE  Protocol;					//	网络协议类型
	BYTE  WorkMode;				//	工作模式
	BYTE  Unuse0;					//  未定义
	BYTE  KeepLive;				//  保活时间
    BYTE  Unuse1[32];				//	未定义
	BYTE  Unuse2[4];				//	未定义
	WORD  Baud;						//	串口波特率
	BYTE  Unuse3[2];				//	未定义
    BYTE  Databit;					//	串口数据位长度
    BYTE  Parity;					//	串口校验方式
    BYTE  Stopbit;					//	串口停止位长度
    BYTE  Flow_ctrl;				//	串口流控方式
    WORD  FrameSize;				//	串口最大数据包长度
	BYTE  ByteInterval;				//	串口最大字符间隔时间
	BYTE  Unuse4[9];				//	未定义
};

struct _Device
{
	BYTE Header;								//	帧头部
	BYTE DeviceType;							//	设备类型
	BYTE Mac[6];								//	设备mac地址
	BYTE UartNumber;							//	设备总的串口数量
	BYTE Unuse0[3];								//	未定义
	BYTE DeviceTypeName[DEVICE_TYPENAME_LEN];	//	设备类型字符串
	BYTE Version[VERSION_LEN];					//	设备固件版本
	WORD  Unuse1;								//	未定义
	BYTE  Unuse2[2];							//	未定义
	char  UserName[USER_LEN];					//	用户名
	char  Password[PASSWORD_LEN];				//	密码
	char  DeviceName[DEVICE_NAME_LEN];			//	设备名
	BYTE  MultiGroup[4];						//	组号
	WORD  LocalIP[2];							//	设备IP地址
    WORD  NetMask[2];							//	子网掩码
    WORD  GatewayIP[2];							//	网关地址
	char  DHCP;									//	0：禁止DHCP,1:启用DHCP
	char  Unuse3;								//	未定义
    char  TmpGroup;								//	未定义,临时组临时保存位置，不应使用这个值。
    char  TalkDialInCount;						//	对讲自动接通呼叫振铃次数，等于时间秒数。	
    
    WORD  ServerIP[2];							//	服务器的IP地址，音频设备会定时向服务器报告消息。
    WORD ServerPort;							//	服务器监听端口，与ServerIP配合使用。

	char ComVolume;								//	串口设置的音量大小偏移量，
	char TalkVolume;							//	对讲是本机的音量大小。
	
	char DefaultSample;							//	录播时，默认的采样频率0 = 8K；1=16K；2 = 24K；3 = 32K
	char DefaultAudioInput;						//	对讲和录播时，默认的音频输入端口 0=mic，1=linein
	char ButtonMode;							//	按键模式，0=触发模式，1=保持模式
	char RecordVolume;							//	录播时，向远端发送数据的音量，默认值。
	WORD PADelayTime;							//	功放延时关闭参数，单位秒
	char MicGain;								//	默认mic的增益大小0～100
	char Unuse5;
	WORD AssistantIp[2];						//	对讲呼叫转移设备ip地址，不用呼叫转移应填0.0.0.0
	WORD CustomServiceIp[4][2];					//	CustomServiceIp[0]是第一客服IP
												//	CustomServiceIp[1]，CustomServiceIp[2]未用
												//	CustomServiceIp[4]是时间服务器IP地址
	struct _Port Port[7];
	char UseAssistant;							//	是否允许呼叫转移
	char Unuse4;
	char TalkMode;								//	对讲模式，0=全双工模式，1=半双工模式
	char CodecType;								//	对讲数据编码格式，0=pcm，1=adpcm
	char AECEnable;								//	是否允许AEC，0=运行，1=禁止
	char DefaultPlayMode;						//	默认播放模式，0=播放模式，1=空闲模式
	char unuse5[2];
	char MultiGroup2[4];						//	第二组多播组，与MultiGroup组成8个组播组；
	char Unuse[72];	
};

#pragma pack()
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
extern "C" int __stdcall np_search_all(struct _DeviceInfo* devs, int* number);


/***********************************************************************************
函数名称：	np_search_one
函数功能：	搜索指定IP的NP设备，成功则将该设备得系统参数填入dev结构中
输入参数：	char * ip		目标转换器得IP地址。ip为以'\0'结尾的字符串。
			struct _DeviceInfo * devs	系统参数结构指针，用户在调用这个函数前应为这个结构分配空间。
								搜索到的设备参数写入这个结构中。
返回值  ：	NP_SUCCESS：		表示操作成功
			其它值：			表示操作出错，返回值请参考函数返回值宏定义。
其他说明：	使用IP地址进行设备搜索时，应该注意，目标转换器的IP地址不属本网段而其物理连接实际在本网段的，使用该命令将搜索不到。
***********************************************************************************/
extern "C" int __stdcall np_search_one(char* ip, struct _DeviceInfo* dev);






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
extern "C" int __stdcall np_login(char* ip,char* mac,struct _Param* Param);





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
extern "C" int __stdcall np_logout(char* ip,char* mac,struct _Param* Param);




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
extern "C" int __stdcall np_read_setting(char* ip,char* mac, void* dev,struct _Param* Param);

//下面的读参数函数适用设备固件版本>v2.5 设备
extern "C" int __stdcall np_read_setting2(char* ip,char* mac, void* dev,struct _Param* Param);

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
//extern "C" int __stdcall np_write_setting(char* ip,char* mac, struct _Device* dev,struct _Param* Param);
extern "C" int __stdcall np_write_setting(char* ip,char* mac, void* dev,int DataLength,struct _Param* Param);
//下面的写参数函数适用设备固件版本>v2.5 设备
extern "C" int __stdcall np_write_setting2(char* ip,char* mac, void* dev,int DataLength,struct _Param* Param);


#endif
