package cn.bjzfgcjs.idefense.device.camera.hikvision;

import cn.bjzfgcjs.idefense.common.utils.GsonTool;
import cn.bjzfgcjs.idefense.common.utils.MiscUtil;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.service.DeviceStorge;
import cn.bjzfgcjs.idefense.device.DevManager;
import cn.bjzfgcjs.idefense.service.PubMessage;
import cn.bjzfgcjs.idefense.device.PtzApi;
import cn.bjzfgcjs.idefense.device.camera.CameraAPI;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HikCtl implements CameraAPI, PtzApi, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(HikCtl.class);

    public final static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

    private static final String RtspTemplate =
            "rtsp://${username}:${passwd}@${ip}:554/${codec}/${channel}/${subtype}/av_stream";

    // deviceId => Handler (操作必须的考虑的参数，缓存之)
    private static final ConcurrentHashMap<String, HikHandler> hikCache = new ConcurrentHashMap<>();

    @Resource
    private DeviceStorge deviceStorge;

    @Resource
    private DevManager devManager;


    public static class HikHandler {  // 控制海康的资源

        NativeLong userId;    // 用户句柄，登录后获得

        NativeLong channel;

        NativeLong lAlarmHandle;   //报警布防句柄

        NativeLong lListenHandle;  //报警监听句柄

        boolean hasPTZ;

        public NativeLong getUserId() {
            return userId;
        }

        public void setUserId(NativeLong userId) {
            this.userId = userId;
        }

        public NativeLong getChannel() {
            return channel;
        }

        public void setChannel(NativeLong channel) {
            this.channel = channel;
        }

        public NativeLong getlAlarmHandle() {
            return lAlarmHandle;
        }

        public void setlAlarmHandle(NativeLong lAlarmHandle) {
            this.lAlarmHandle = lAlarmHandle;
        }

        public NativeLong getlListenHandle() {
            return lListenHandle;
        }

        public void setlListenHandle(NativeLong lListenHandle) {
            this.lListenHandle = lListenHandle;
        }

        public boolean isHasPTZ() {
            return hasPTZ;
        }

        public void setHasPTZ(boolean hasPTZ) {
            this.hasPTZ = hasPTZ;
        }
    }

    /*************************** 操作接口实现 ************************/

    private HikHandler getHikHandler(DeviceInfo obj) {
        try {
            HikHandler handler = hikCache.get(obj.getID());
            if (handler == null) {
                handler = login(obj.getID(), obj.getIPAddress(), obj.getIPPort(),
                        obj.getUserName(), obj.getPassWord());
            }
            return handler;

        } catch (Exception e){
            logger.error("login camera device：{} failed, {}", GsonTool.toJson(obj), e.getStackTrace());
            return null;
        }
    }

    private boolean isReady(String deviceId) {
        HikHandler hikHandler = hikCache.get(deviceId);
        return (hikHandler != null && hikHandler.getUserId().longValue() > 0);
    }

    /**
     *  参数取值范围：
     *
     * codec： h264, MPEG-4, mpeg4
     * channel: ch{1-32}
     * subtype： main, sub
     *
     * @param deviceInfo
     * @return  "rtsp://${username}:${passwd}@${ip}:554/${codec}/${channel}/${subtype}/av_stream";
     */
    @Override
    public String getRtspUrl(DeviceInfo deviceInfo) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("username", deviceInfo.getUserName());
        paramsMap.put("passwd", deviceInfo.getPassWord());
        paramsMap.put("ip", deviceInfo.getIPAddress());
        paramsMap.put("codec", "h264");
        paramsMap.put("channel", "ch1");
        paramsMap.put("subtype", "main");
        StrSubstitutor sub = new StrSubstitutor(paramsMap);

        return sub.replace(RtspTemplate);
    }

    // http://<username>:<password>@<address>:<httpport>/Streaming/Channels/1/picture
    @Override
    public String getSnapshot(DeviceInfo obj) throws Exception {
        HikHandler handler = getHikHandler(obj);
        if (handler == null) return null;

        HCNetSDK.NET_DVR_JPEGPARA jpegPara = new HCNetSDK.NET_DVR_JPEGPARA();
        jpegPara.wPicQuality = 0;
        jpegPara.wPicSize = 1;
        // TODO: 读取存储路径，
        // 截图文件命名规则：filename: 日期_UUID.jpeg
        String filename = MiscUtil.getUUID() + ".jpeg";
        hCNetSDK.NET_DVR_CaptureJPEGPicture(handler.getUserId(), handler.getChannel(), jpegPara,filename);
        return filename;
    }

    @Override
    public void startRecord(DeviceInfo deviceInfo) {

    }

    // set OSD

    // 布防，撤控

    /********************************   PTZ 操作 开始  ***********************************/

    /** 摄像机支持PTZ且该机位配置有PTZ，才可以控制
     * @return
     */
    @Override
    public boolean hasPTZ(DeviceInfo obj) {
        HikHandler handler = getHikHandler(obj);
        return handler != null && handler.isHasPTZ();
    }

    // rtsp://<username>:<password>@<address>:<port>/Streaming/Channels/<id>/
    // id: channel + 01（主码流））
    public Boolean ptzCtl(DeviceInfo deviceInfo, int command, int speed, int start) {
        HikHandler handler = getHikHandler(deviceInfo);
        if (handler != null) {
            return hCNetSDK.NET_DVR_PTZControlWithSpeed_Other(handler.getUserId(),
                    handler.getChannel(), command, start, speed);
        } else {
            return false;
        }
    }

    @Override
    public int ptzManage() {
        return 0;
    }

    private void initPtz(DeviceInfo obj) {
        HikHandler handler = getHikHandler(obj);

        HCNetSDK.NET_DVR_DECODERCFG_V30 ptzDecoder = new HCNetSDK.NET_DVR_DECODERCFG_V30();
        ptzDecoder.write();
        // 取出来看下格式：
        hCNetSDK.NET_DVR_GetDVRConfig(handler.getUserId(), HCNetSDK.NET_DVR_GET_DECODERCFG_V30,
                handler.getChannel(), ptzDecoder.getPointer(), ptzDecoder.size(), new IntByReference(0));
        ptzDecoder.read();

        // Peco D 协议配置
        ptzDecoder.dwBaudRate = 10;
        ptzDecoder.byParity = 0;
        ptzDecoder.byDataBit = 8;
        ptzDecoder.byStopBit = 1;
        ptzDecoder.byFlowcontrol = 0;
        ptzDecoder.wDecoderType = 7;
//        ptzDecoder.wDecoderAddress = 1;
        logger.info("PTZ参数设置情况：{}", ptzDecoder);

        hCNetSDK.NET_DVR_SetDVRConfig(handler.getUserId(), HCNetSDK.NET_DVR_GET_DECODERCFG_V30,
                handler.getChannel(), ptzDecoder.getPointer(), ptzDecoder.size());

    }


    /**  登录获取海康的操作资源，缓存于hikCache
     * @param deviceId
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    private HikHandler login(String deviceId, String ip, Integer port, String username, String password) throws Exception {
        /*****   用户句柄 *****/
        HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfoV30 = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        NativeLong lUserID = hCNetSDK.NET_DVR_Login_V30(ip, port, username, password, deviceInfoV30);
        if(lUserID.longValue() < 0){
            logger.error("failed to login: {}, errorCode: {}", deviceId, hCNetSDK.NET_DVR_GetLastError());
            throw new Exception("login error, check your username and password");
        }
        // 查下设备的序列号
        logger.info("device:{}, serialNo:{}", deviceId, deviceInfoV30.sSerialNumber);

        /********** 获取 IPC 通道号  *********/
        IntByReference ibrBytesReturned = new IntByReference(0);
        boolean bRet = false;
        int chan = -1;
        HCNetSDK.NET_DVR_IPPARACFG ipParaCfg = new HCNetSDK.NET_DVR_IPPARACFG();
        ipParaCfg.write();
        Pointer lpIpParaConfig = ipParaCfg.getPointer();
        bRet = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_IPPARACFG,
                new NativeLong(0), lpIpParaConfig, ipParaCfg.size(), ibrBytesReturned);
        ipParaCfg.read();
        if(!bRet){ // 设备不支持,则表示没有IP通道
            for (int iChannum = 0; iChannum < deviceInfoV30.byChanNum; iChannum++) {
                logger.info ("通道号: " + iChannum + deviceInfoV30.byStartChan);
            }
            if(deviceInfoV30.byChanNum > 0) { // 设备为IPC
                chan = deviceInfoV30.byStartChan;
            } else {
                logger.error("failed to get channel: {}", deviceId);
                throw new Exception("didn't get the channel for ");
            }
        } else { // 设备支持IP通道
            logger.info("device support ip channel");
        }
        NativeLong lChannel = new NativeLong(chan);

        // 缓存用户句柄及通道
        HikHandler hikHandler = new HikHandler();
        hikHandler.setUserId(lUserID);
        hikHandler.setChannel(lChannel);
        // 获取设备预览句柄
//        NativeLong lRealHandle = play(chan);
        hikCache.put(deviceId, hikHandler);

        return hikHandler;
    }



    @Override
    public void destroy() throws Exception {
        hCNetSDK.NET_DVR_Cleanup();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!hCNetSDK.NET_DVR_Init()){
            // hCNetSDK初始化失败
            throw new Exception("HCNetSDK failed to init: " + hCNetSDK.NET_DVR_GetLastError());
        } else {
            hCNetSDK.NET_DVR_SetConnectTime(2000, 1);
            hCNetSDK.NET_DVR_SetReconnect(10000, true);
            // TODO: 启动即登录相机， 并确认机位PTZ情况，保存到缓存里去
//
//            Position position = deviceStorge.getPosByPostionCode(deviceInfo.getPosition());
//            return (position.getHasPTZ() > 0);
        }
    }

}

