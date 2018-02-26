package cn.bjzfgcjs.idefense.device.camera;

public interface CameraAPI {

    // 要控制云台？
    public boolean hasPTZ(String deviceId);

    /**
     * @return 实时主码流的URL
     */
    public String getRtspURl(String deviceId);

    /**
     * @return 视频截图文件名
     */
    public String getSnapshot(String deviceId);

    /**
     * @return 录入的录像文件名
     */
    // 录像时长由系统配置确定
    public void startRecord(String deviceId);
}