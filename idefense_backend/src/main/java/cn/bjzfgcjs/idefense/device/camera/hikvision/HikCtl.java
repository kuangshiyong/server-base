package cn.bjzfgcjs.idefense.device.camera.hikvision;

import cn.bjzfgcjs.idefense.common.utils.GsonTool;
import cn.bjzfgcjs.idefense.common.utils.MiscUtil;
import cn.bjzfgcjs.idefense.common.utils.Now;
import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.DeviceStorage;
import cn.bjzfgcjs.idefense.device.DevManager;
import cn.bjzfgcjs.idefense.device.PtzApi;
import cn.bjzfgcjs.idefense.device.camera.CameraAPI;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.bjzfgcjs.idefense.device.camera.hikvision.HCNetSDK.NET_DVR_SET_SHOWSTRING_V30;

@Service
public class HikCtl implements CameraAPI, PtzApi, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(HikCtl.class);

    public final static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

    private static final String RtspTemplate =
            "rtsp://${username}:${passwd}@${ip}:554/${codec}/${channel}/${subtype}/av_stream";

    // deviceId => Handler (操作必须的考虑的参数，缓存之)
    private static final ConcurrentHashMap<String, HikHandler> hikCache = new ConcurrentHashMap<>();

    @Resource
    private DeviceStorage deviceStorage;

    @Resource
    private DevManager devManager;


    public static class HikHandler {  // 控制海康的资源

        NativeLong userId;    // 用户句柄，登录后获得

        NativeLong channel;

        NativeLong lAlarmHandle;   //报警布防句柄

        NativeLong lListenHandle;  //报警监听句柄

        Boolean hasPTZ = false;

        Integer status = -1;  // 设备状态：0-正常，1-CPU>85%，2-硬件错误，-1-连接错误

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

        public Boolean getHasPTZ() {
            return hasPTZ;
        }

        public void setHasPTZ(Boolean hasPTZ) {
            this.hasPTZ = hasPTZ;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    /*************************** 操作接口实现 ************************/

    private HikHandler getHikHandler(DeviceInfo obj) throws Exception {
        try {
            HikHandler handler = hikCache.get(obj.getID());
            if (handler == null) {
                handler = login(obj);
            }
            return handler;

        } catch (Exception e){
            logger.error("login camera device：{} failed, {}", GsonTool.toJson(obj), e.getStackTrace());
            throw new Exception("take over Hikvision device failed: " + obj.getID());
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
     * @param obj
     * @return  "rtsp://${username}:${passwd}@${ip}:554/${codec}/${channel}/${subtype}/av_stream";
     */
    @Override
    public String getRtspUrl(DeviceInfo obj) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("username", obj.getUserName());
        paramsMap.put("passwd", obj.getPassWord());
        paramsMap.put("ip", obj.getIPAddress());
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

        HCNetSDK.NET_DVR_JPEGPARA jpegPara = new HCNetSDK.NET_DVR_JPEGPARA();
        jpegPara.wPicQuality = 0;
        jpegPara.wPicSize = 1;
        // 截图文件命名规则 - filename: {yyyyMMdd}_{device id}_{uuid}.jpeg
        String filename = ("d:/").
                concat(Now.getSdf4().format(new Date(Now.getMillis()))).concat("_").
                concat(obj.getID()).concat("_").
                concat(MiscUtil.getUUID()).concat(".jpeg");
        if(!hCNetSDK.NET_DVR_CaptureJPEGPicture(handler.getUserId(), handler.getChannel(), jpegPara,filename)) {
            int errorno = hCNetSDK.NET_DVR_GetLastError();
            logger.error("snapshot failed: {}, errno ", filename, errorno);
            return null;
        }
        return filename;
    }

    public void setOSD(DeviceInfo obj) throws Exception {

    }

    public AppCode changeResolution(DeviceInfo obj) throws Exception {
        HikHandler handler = getHikHandler(obj);
        // 读取当前支持的分辨率
        HCNetSDK.NET_DVR_COMPRESSIONCFG_V30 compressioncfg = new HCNetSDK.NET_DVR_COMPRESSIONCFG_V30();
        compressioncfg.write();
        hCNetSDK.NET_DVR_GetDVRConfig(handler.getUserId(), HCNetSDK.NET_DVR_GET_COMPRESSCFG_V30,
                handler.getChannel(), compressioncfg.getPointer(), compressioncfg.size(),  new IntByReference(0));
        compressioncfg.read();

        logger.info("摄像机当前码率支持的分辨率有：网传-{}, 事件-{} ", compressioncfg.struNetPara.byResolution,
                compressioncfg.struEventRecordPara.byResolution);

        // 使用FFPMEG录像

        return AppCode.OK;
    }

    @Override
    public void startRecord(DeviceInfo obj) throws Exception {
        HikHandler handler = getHikHandler(obj);

        String filename = ("d:/").
                concat(Now.getSdf4().format(new Date(Now.getMillis()))).concat("_").
                concat(obj.getID()).concat("_").
                concat(MiscUtil.getUUID()).concat(".jpeg");

    }

    // 布防，撤控

    /********************************   PTZ 操作 开始  ***********************************/

    /** 摄像机支持PTZ且该机位配置有PTZ，才可以控制
     * @return
     */
    @Override
    public boolean hasPTZ(DeviceInfo obj) throws Exception {
        HikHandler handler = getHikHandler(obj);
        return handler != null && handler.getHasPTZ();
    }

    /** 云台控制 Pan Tilt Zoom
     * @param obj
     * @param command
     * @param speed
     * @param start
     * @return
     */
    public Boolean ptzCtl(DeviceInfo obj, int command, int speed, int start) throws Exception {
        HikHandler handler = getHikHandler(obj);

        logger.info("执行PTZ控制");
        ptzConfigLimit(obj);
        return hCNetSDK.NET_DVR_PTZControlWithSpeed_Other(handler.getUserId(),
                handler.getChannel(), command, start, speed);
    }

    /** 内容：预置点的操作, Set Clear Goto
     * @param obj
     * @param cmd
     * @param index
     */
    public void ptzPreset(DeviceInfo obj, int cmd, int index) throws Exception {
        HikHandler handler = getHikHandler(obj);
        if (handler == null ) return;

        hCNetSDK.NET_DVR_PTZPreset_Other(handler.getUserId(), handler.getChannel(),
                cmd, index);
    }
//
//    /** 工作内容： 巡航参数，OSD，守望参数，定时任务，预置点
//     * @param obj
//     * @param cmd
//     * @param name
//     */
//    public void ptzConfig(DeviceInfo obj, int cmd, String name) {
//        HikHandler handler = getHikHandler(obj);
//        if (handler == null ) return;
//
//        hCNetSDK.NET_DVR_SetDVRConfig(handler.getUserId(), "设置预置点名称",
//                handler.getChannel(), );
//    }

    public void ptzConfigLimit(DeviceInfo obj) throws Exception{
        HikHandler handler = getHikHandler(obj);
        if (handler == null ) return;


        HCNetSDK.NET_DVR_PICCFG_V30 cfg = new HCNetSDK.NET_DVR_PICCFG_V30();
        cfg.write();

        hCNetSDK.NET_DVR_GetDVRConfig(handler.getUserId(), HCNetSDK.NET_DVR_GET_PICCFG_V30,
                handler.getChannel(), cfg.getPointer(), cfg.size(), new IntByReference(0));
        cfg.read();
        logger.info("修改前：{}",new String(cfg.sChanName));
        cfg.dwShowChanName = 1;
        cfg.dwShowOsd = 1;
        cfg.sChanName = "测试".getBytes();
        hCNetSDK.NET_DVR_SetDVRConfig(handler.getUserId(), HCNetSDK.NET_DVR_SET_PICCFG_V30, handler.getChannel(),
                cfg.getPointer(), cfg.size());
        hCNetSDK.NET_DVR_GetDVRConfig(handler.getUserId(), HCNetSDK.NET_DVR_GET_PICCFG_V30,
                new NativeLong(2), cfg.getPointer(), cfg.size(), new IntByReference(0));
        logger.info("修改后通道名：{}", new String(cfg.sChanName));



//        HCNetSDK.NET_DVR_PTZ_LIMITCOND ptzLimitReq = new HCNetSDK.NET_DVR_PTZ_LIMITCOND();
//        ptzLimitReq.dwChan = 2; // handler.getChannel().intValue();
//        ptzLimitReq.dwSize = ptzLimitReq.size();
//        HCNetSDK.NET_DVR_PTZ_LIMITCFG ptzLimitRes = new HCNetSDK.NET_DVR_PTZ_LIMITCFG();
//        ptzLimitReq.write();
//        ptzLimitRes.write();
//
//        IntByReference pInt = new IntByReference(0);
//        Pointer lpStatusList = pInt.getPointer();
//        boolean res = hCNetSDK.NET_DVR_GetDeviceConfig(handler.getUserId(), HCNetSDK.NET_DVR_GET_LIMITCFG,
//                1, ptzLimitReq.getPointer(), ptzLimitReq.size(), lpStatusList,
//                ptzLimitRes.getPointer(), ptzLimitRes.size());
//
//        if (res) {
//            ptzLimitRes.read();
//            logger.info("读取到PTZ限制参数：{}", ptzLimitRes);
//        } else {
//            logger.info("获取限制参数失败：{}", hCNetSDK.NET_DVR_GetLastError());
//        }

//        HCNetSDK.NET_DVR_PTZSCOPE scope = new HCNetSDK.NET_DVR_PTZSCOPE();
//        scope.write();
//
//        hCNetSDK.NET_DVR_GetDVRConfig(handler.getUserId(), HCNetSDK. NET_DVR_GET_PTZSCOPE,
//                handler.getChannel(), scope.getPointer(), scope.size(), new IntByReference(0));
//        scope.read();
//        logger.info("PTZ 范围：{}", scope);

    }

/*

NET_DVR_GET_CRUISE
  NET_DVR_GET_PTZOSDCFG
  NET_DVR_GET_BASICPARAMCFG
  NET_DVR_GET_POWEROFFMEMCFG
  NET_DVR_GET_PTZ_PARKACTION_CFG
NET_DVR_GET_SCH_TASK
NET_DVR_GET_PRESET_NAME


 */
    @Override
    public int ptzManage() {
        return 0;
    }

    /** 初始化串口参数按照中安要求设定。
     * @param obj
     * @throws Exception
     */
    private void initPtz(DeviceInfo obj) throws Exception {
        HikHandler handler = getHikHandler(obj);
        handler.setHasPTZ(true);

        HCNetSDK.NET_DVR_DECODERCFG_V30 ptzDecoder = new HCNetSDK.NET_DVR_DECODERCFG_V30();
        ptzDecoder.write();
        // Peco D 协议配置
        ptzDecoder.dwBaudRate = 9;
        ptzDecoder.byParity = 0;
        ptzDecoder.byDataBit = 3;
        ptzDecoder.byStopBit = 0;
        ptzDecoder.byFlowcontrol = 0;
        ptzDecoder.wDecoderType = 7;

        hCNetSDK.NET_DVR_SetDVRConfig(handler.getUserId(), HCNetSDK.NET_DVR_GET_DECODERCFG_V30,
                handler.getChannel(), ptzDecoder.getPointer(), ptzDecoder.size());
    }

    /********************************   PTZ 操作 结束  ***********************************/

    /**  登录获取海康的操作资源，缓存于hikCache
     */
    private HikHandler login(DeviceInfo obj) throws Exception {

        /*****   用户句柄 *****/
        HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfoV30 = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        NativeLong lUserID = hCNetSDK.NET_DVR_Login_V30(obj.getIPAddress(),
                obj.getIPPort(), obj.getUserName(), obj.getPassWord(), deviceInfoV30);
        if(lUserID.longValue() < 0){
            throw new Exception("login: " + obj.getID() + " error, check your username and password, "
                    + hCNetSDK.NET_DVR_GetLastError());
        }
        // 查下设备的序列号
        logger.info("device:{}, serialNo:{}", obj.getID(), deviceInfoV30.sSerialNumber);

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
                throw new Exception("fail to get the channel of " + obj.getID());
            }
        } else { // 设备支持IP通道
            logger.info("device:{} support ip channel", obj.getID());
        }
        NativeLong lChannel = new NativeLong(chan);

        HikHandler hikHandler = new HikHandler();
        hikHandler.setUserId(lUserID);
        hikHandler.setStatus(0);
        hikHandler.setChannel(lChannel);
        hikCache.put(obj.getID(), hikHandler);
        // 获取设备预览句柄
//        NativeLong lRealHandle = play(chan);
        return hikHandler;
    }

    /** 初始化 摄像机及其控制PTZ的参数
     * @param type
     */
    private void initResouce(Byte type) { // CCD or IR
        try {
            List<DeviceInfo> deviceInfoList = deviceStorage.getDeviceListByType(type);
            if (deviceInfoList == null) return;

            for (DeviceInfo obj : deviceInfoList) {
                login(obj);
                if (deviceStorage.hasPtz(obj)) {
                    logger.info("devive:{} has ptz", obj.getID());
                    initPtz(obj);
                }
            }
        } catch (Exception e) {
            logger.error("loading cameras failed: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void releaseResource() {
        for (String deviceId : hikCache.keySet()) {
            HikHandler hikHandler = hikCache.get(deviceId);
            hCNetSDK.NET_DVR_Logout(hikHandler.getUserId());
        }
    }

    @Override
    public void destroy() throws Exception {
        releaseResource();
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
            initResouce(DeviceInfo.Type.CCD);
            initResouce(DeviceInfo.Type.IR);
        }
    }
}