package cn.bjzfgcjs.idefense.device.camera.hikvision;

import cn.bjzfgcjs.idefense.core.DataCache;
import cn.bjzfgcjs.idefense.service.PubMessage;
import cn.bjzfgcjs.idefense.device.bean.HikHandlerBean;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

//@Service
public class Login {

    public final static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

    @Resource
    private PubMessage pubMessage;

    /**
     * 设备参数信息
     */
    private HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;

    /**
     * 设备IP参数
     */
    private HCNetSDK.NET_DVR_IPPARACFG m_strIpparaCfg;

    /**
     * 用户参数
     */
    private HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo;

    /**
     * 用户句柄
     */
    private NativeLong lUserID;

    public boolean doLogin(String deviceId, String ip, Short port, String username, String password) throws Exception {
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true){
            // hCNetSDK初始化失败
            throw new Exception("init HCNetSDK error");
        }

        m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        // 获取用户句柄
        lUserID = hCNetSDK.NET_DVR_Login_V30(ip, port, username, password, m_strDeviceInfo);

        long userID = lUserID.longValue();
        if(userID == -1){
            // 登录失败
            throw new Exception("login error, check your username and password");
        }

        // 获取通道号
        int chan = getChannel();
        if(chan == -1) {
            // 没有获取到通道号
            throw new Exception("didn't get the channel");
        }
        NativeLong lChannel = new NativeLong(chan);

        HikHandlerBean hikHandlerBean = new HikHandlerBean();
        hikHandlerBean.setUserId(lUserID);
        hikHandlerBean.setChannel(lChannel);

//        // 获取设备预览句柄
//        NativeLong lRealHandle = play(chan);
//        // 获取设备通道句柄
//        NativeLong lChannel = new NativeLong(chan);
//        // 保存到缓存中, 下次不必再进行登录操作
//        TempData.getTempData().setNativeLong(ip, lUserID, lRealHandle, lChannel);

        DataCache.setHikHandler(deviceId, hikHandlerBean);

//        pubMessage.hikHandls().put(deviceId, hikHandlerBean);
        return true;
    }

    private int getChannel() {
        // 获取IP接入配置参数
        IntByReference ibrBytesReturned = new IntByReference(0);
        boolean bRet = false;

        m_strIpparaCfg = new HCNetSDK.NET_DVR_IPPARACFG();
        m_strIpparaCfg.write();
        Pointer lpIpParaConfig = m_strIpparaCfg.getPointer();
        bRet = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_IPPARACFG, new NativeLong(0), lpIpParaConfig, m_strIpparaCfg.size(), ibrBytesReturned);
        m_strIpparaCfg.read();

        // 设备是否支持IP通道, true为不支持
        if(!bRet){
            for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++) {
                System.out.println("通道号: " + iChannum + m_strDeviceInfo.byStartChan);
            }
            if(m_strDeviceInfo.byChanNum > 0){
                return Integer.valueOf(0 + m_strDeviceInfo.byStartChan);
            }
        }

        return -1;
    }

}
