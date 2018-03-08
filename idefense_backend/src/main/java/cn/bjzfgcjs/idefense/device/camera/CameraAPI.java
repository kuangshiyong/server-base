package cn.bjzfgcjs.idefense.device.camera;

import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;

public interface CameraAPI {

    /**
     * @return 实时主码流的URL
     */
    public String getRtspUrl(DeviceInfo deviceInfo);

    /**
     * @return 视频截图文件名
     */
    public String getSnapshot(DeviceInfo deviceInfo) throws Exception;

    /**
     * @return 录入的录像文件名
     */
    // 录像时长由系统配置确定
    public void startRecord(DeviceInfo deviceInfo) throws Exception;

    // 要控制云台？
    public boolean hasPTZ(DeviceInfo deviceInfo) throws Exception;

}