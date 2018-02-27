package cn.bjzfgcjs.idefense.device.bean;

import cn.bjzfgcjs.idefense.device.sound.sv2101.LCAudioThrDll;

public class SvHandlerBean {

    private LCAudioThrDll._PlayParam.ByReference  playParam;


    public LCAudioThrDll._PlayParam.ByReference getPlayParam() {
        return playParam;
    }

    public void setPlayParam(LCAudioThrDll._PlayParam.ByReference playParam) {
        this.playParam = playParam;
    }
}
