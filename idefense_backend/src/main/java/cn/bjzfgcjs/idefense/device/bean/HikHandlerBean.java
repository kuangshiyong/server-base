package cn.bjzfgcjs.idefense.device.bean;

import com.sun.jna.NativeLong;

public class HikHandlerBean {

    NativeLong userId;

    NativeLong channel;

//    NativeLong lUserID;//用户句柄
//    NativeLong lAlarmHandle;//报警布防句柄
//    NativeLong lListenHandle;//报警监听句柄

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
