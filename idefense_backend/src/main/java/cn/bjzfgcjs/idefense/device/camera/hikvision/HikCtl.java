package cn.bjzfgcjs.idefense.device.camera.hikvision;

import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.service.DeviceStorge;
import cn.bjzfgcjs.idefense.service.PubMessage;
import cn.bjzfgcjs.idefense.device.PtzApi;
import cn.bjzfgcjs.idefense.device.camera.CameraAPI;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class HikCtl implements CameraAPI, PtzApi {

    private static final Logger logger = LoggerFactory.getLogger(HikCtl.class);

    private static HCNetSDK hCNetSDK = HikInit.hCNetSDK;


    // 编码格式：h264、MPEG-4、mpeg4
    private static final String videoCodec = "h264";
    // 通道号，起始为1
    private static final String channel = "ch1";
    // 码流类型，主码流为main,子码流为sub
    private static final String subtype = "main";
    private static final String rtspTemplate =
            "rtsp://${username}:${passwd}@${ip}:554/${codec}/${channel}/${subtype}/av_stream";

    @Resource
    private HikInit hikInit;

    @Resource
    private DeviceStorge deviceStorge;

    @Resource
    private PubMessage pubMessage;


    @Override
    public String getRtspUrl(DeviceInfo deviceInfo) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("username", deviceInfo.getUserName());
        paramsMap.put("passwd", deviceInfo.getPassWord());
        paramsMap.put("ip", deviceInfo.getIPAddress());
        paramsMap.put("codec", videoCodec);
        paramsMap.put("channel", channel);
        paramsMap.put("subtype", subtype);
        StrSubstitutor sub = new StrSubstitutor(paramsMap);
        return sub.replace(rtspTemplate);
    }

    // http://<username>:<password>@<address>:<httpport>/Streaming/Channels/1/picture
    @Override
    public String getSnapshot(DeviceInfo deviceInfo) throws Exception {
        hikInit.login(deviceInfo.getID(), deviceInfo.getIPAddress(), deviceInfo.getIPPort(),
                deviceInfo.getUserName(), deviceInfo.getPassWord());
        HikHandler handler = getHandler(deviceInfo);

        HCNetSDK.NET_DVR_JPEGPARA jpegPara = new HCNetSDK.NET_DVR_JPEGPARA();
        jpegPara.wPicQuality = 0;
        jpegPara.wPicSize = 1;
        // TODO: 读取存储路径，
        // 截图文件命名规则：filename: 日期_UUID.jpeg
        String filename = UUID.randomUUID().toString() + ".jpeg";
        hCNetSDK.NET_DVR_CaptureJPEGPicture(handler.getUserId(), handler.getChannel(), jpegPara,filename);
        return filename;
    }

    @Override
    public void startRecord(DeviceInfo deviceInfo) {

    }

    // set OSD

    // 布防，撤控


    /** 摄像机支持PTZ且该机位配置有PTZ，才可以控制
     * @return
     */
    @Override
    public boolean hasPTZ(DeviceInfo deviceInfo) {
        Position position = deviceStorge.getPosByPostionCode(deviceInfo.getPosition());
        return (position.getHasPTZ() > 0);
    }

    // rtsp://<username>:<password>@<address>:<port>/Streaming/Channels/<id>/
    // id: channel + 01（主码流））
    private HikHandler getHandler(DeviceInfo deviceInfo) {
        try {
            hikInit.login(deviceInfo.getID(), deviceInfo.getIPAddress(), deviceInfo.getIPPort(),
                    deviceInfo.getUserName(), deviceInfo.getPassWord());
            return hikInit.getHikHandler(deviceInfo.getID());
        } catch (Exception e) {
            logger.error("ptz control failed");
            return null;
        }
    }

//    @Override
    public Boolean ptzCtl(DeviceInfo deviceInfo, int command, int speed, int start) {
        HikHandler handler = getHandler(deviceInfo);
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
}

