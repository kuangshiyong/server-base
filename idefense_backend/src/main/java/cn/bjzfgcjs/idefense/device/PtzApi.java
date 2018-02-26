package cn.bjzfgcjs.idefense.device;

public interface PtzApi {

    public Boolean ptzCtl(String deviceId, int command, int speed, int start);

    // 设置PreSet
    public int ptzManage();
}

