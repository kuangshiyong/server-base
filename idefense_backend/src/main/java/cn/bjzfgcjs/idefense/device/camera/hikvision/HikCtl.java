package cn.bjzfgcjs.idefense.device.camera.hikvision;

import cn.bjzfgcjs.idefense.core.DataCache;
import cn.bjzfgcjs.idefense.service.PubMessage;
import cn.bjzfgcjs.idefense.device.PtzApi;
import cn.bjzfgcjs.idefense.device.bean.HikHandlerBean;
import cn.bjzfgcjs.idefense.device.camera.CameraAPI;
import com.sun.jna.NativeLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class HikCtl implements CameraAPI, PtzApi {

        private static final Logger logger = LoggerFactory.getLogger(HikCtl.class);

        private static HCNetSDK hCNetSDK = Login.hCNetSDK;

        private String deviceId;

        private NativeLong userID;

        private HikHandlerBean hikHandlerBean;

//        @Resource
        private Login login;

//        private Device cameraInfo;
//        @Resource
//        private DeviceMapper deviceMapper;
//
//        @Resource
//        private PositionMapper positionMapper;

        @Resource
        private PubMessage pubMessage;

        public HikCtl(String deviceId) {
            this.deviceId = deviceId;
//
//        cameraInfo = deviceMapper.selectByPrimaryKey(deviceId);
//        if (cameraInfo != null)
//            logger.debug("camera info: {}", GsonTool.toJson(cameraInfo));
//            cameraInfo = new Device();
//            cameraInfo.setId("192.168.1.64");
//            cameraInfo.setPosition(1);
//            cameraInfo.setPort(554);
//            cameraInfo.setUsername("admin");
//            cameraInfo.setPassword("hzzf888888");
//            cameraInfo.setChannel(1);
//            cameraInfo.setPtz(true);
        }

        public boolean isAvailable(String deviceId) {
            try {
                if (hikHandlerBean == null) {
                    hikHandlerBean = DataCache.getHikHandler(deviceId);
                    logger.debug("还没登录摄像头");
                }
                if (hikHandlerBean == null) {
                    login.doLogin(deviceId,"192.168.1.64", (short)8000, "admin", "hzzf888888");
                    return true;
                }
            } catch (Exception e) {
                logger.error("camera unavailable: {}", e);
            } finally {
                return false;
            }
        }

        /** 摄像机支持PTZ且该机位配置有PTZ，才可以控制
         * @return
         */
        @Override
        public boolean hasPTZ(String deviceId) {
//            HikHandlerBean hikHandlerBean = DataCache.getHikHandler(deviceId);
            return true;
//            if (cameraInfo != null && cameraInfo.getPtz()) {
//                Integer positionNo = cameraInfo.getPosition();
//                return true;
////            Position position = positionMapper.selectByPrimaryKey(positionNo);
////            if (position != null && position.getPtz()) {
////                return true;
////            }
//            }
//            return false;
        }

        // rtsp://<username>:<password>@<address>:<port>/Streaming/Channels/<id>/
        // id: channel + 01（主码流））
        @Override
        public String getRtspURl(String deviceId) {
            return "rtsp://admin:hzzf888888@192.168.1.64:554/h264/ch1/main/av_stream";
//            if (cameraInfo != null) {
//                StringBuffer buffer = new StringBuffer();
//                buffer.append("rtsp://");
//                buffer.append(cameraInfo.getUsername()).append(":").append(cameraInfo.getPassword());
//                buffer.append("@");
//                buffer.append(cameraInfo.getIp()).append(":").append(cameraInfo.getPort());
//                buffer.append("/Streaming/Channels/").append(cameraInfo.getChannel()).append("01");
//                buffer.append("/?transportmode=unicast");
//                return buffer.toString();
//            }
//
//            return null;
        }

        // http://<username>:<password>@<address>:<httpport>/Streaming/Channels/1/picture
        @Override
        public String getSnapshot(String deviceId) {
            return null;
        }

        @Override
        public void startRecord(String deviceId) {

        }

        @Override
        public Boolean ptzCtl(String deviceId, int command, int speed, int start) {
            HikHandlerBean hikHandlerBean = DataCache.getHikHandler(deviceId);
            return  hCNetSDK.NET_DVR_PTZControlWithSpeed_Other(hikHandlerBean.getUserId(),
                    hikHandlerBean.getChannel(), command, start, speed);
        }

        @Override
        public int ptzManage() {
            return 0;
        }
}

