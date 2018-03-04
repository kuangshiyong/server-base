package cn.bjzfgcjs.idefense.device.camera.hikvision;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class HikInit implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(HikInit.class);

    public final static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

    private static final ConcurrentHashMap<String, HikHandler> hikCache = new ConcurrentHashMap<>();

    public HikHandler getHikHandler(String deviceId) {
        return hikCache.get(deviceId);
    }

    public boolean isReady(String deviceId) {
        HikHandler hikHandler = hikCache.get(deviceId);
        return  (hikHandler != null && hikHandler.getUserId().longValue() > 0);
    }

    /**  初始化海康设备的操作句柄，见"HikHandler"
     * @param deviceId
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return
     * @throws Exception
     * ？？： 断线重连后，handler是否可用；通道号怎么才是有效的，尤其是同时截图、录像
     */
    public boolean login(String deviceId, String ip, Integer port, String username, String password) throws Exception {
        if (isReady(deviceId)) return true;

        // 用户句柄
        HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfoV30 = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        NativeLong lUserID = hCNetSDK.NET_DVR_Login_V30(ip, port, username, password, deviceInfoV30);
        if(lUserID.longValue() < 0){
            logger.error("failed to login: {}, errorCode: {}", deviceId, hCNetSDK.NET_DVR_GetLastError());
            throw new Exception("login error, check your username and password");
        }
        // 查下设备的序列号
        logger.debug("device:{}, serialNo:{}", deviceId, deviceInfoV30.sSerialNumber);

        // 获取 IPC 通道号
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
            logger.debug("device support ip channel");
        }
        NativeLong lChannel = new NativeLong(chan);

        // 缓存用户句柄及通道
        HikHandler hikHandler = new HikHandler();
        hikHandler.setUserId(lUserID);
        hikHandler.setChannel(lChannel);
//        // 获取设备预览句柄
//        NativeLong lRealHandle = play(chan);
        hikCache.put(deviceId, hikHandler);

        return true;
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
        }
    }
}
