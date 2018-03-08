package cn.bjzfgcjs.idefense.device;

import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;

public interface PtzApi {

    public Boolean ptzCtl(DeviceInfo deviceInfo, int command, int speed, int start) throws Exception;

    // 设置PreSet
    public int ptzManage();
}

