package cn.bjzfgcjs.idefense.device.camera.hikvision;

import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import com.sun.jna.NativeLong;

public class HikHandler {

    NativeLong userId;    // 用户句柄，登录后获得

    NativeLong channel;

    NativeLong lAlarmHandle;   //报警布防句柄

    NativeLong lListenHandle;  //报警监听句柄

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
}
