package cn.bjzfgcjs.idefense.device.sound.sv2101;

import cn.bjzfgcjs.idefense.common.utils.MiscUtil;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import com.sun.jna.Pointer;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;


public class LCHandler {

    @Resource
    private LCPlayback lcPlayback;


    private AtomicInteger playLock;

    private LCAudioThrDll._PlayParam.ByReference playParam;

    public LCHandler() {
        playLock = new AtomicInteger(0);
        playParam = new LCAudioThrDll._PlayParam.ByReference();
        playParam.Priority = 10;
        playParam.Treble = 100;
        playParam.Treble_En = 100;
        playParam.Bass = 100;
        playParam.Bass_En = 100;
        playParam.OptionByte = 0;
//        playParam.MuxName[0] = 0;
        playParam.MaxBitrate = 1;
        playParam.Options[0] = 0;
    }

    public AtomicInteger getPlayLock() {
        return playLock;
    }

    public void setPlayLock(AtomicInteger playLock) {
        this.playLock = playLock;
    }

    public LCAudioThrDll._PlayParam.ByReference getPlayParam() {
        return playParam;
    }

    public void setPlayParam(LCAudioThrDll._PlayParam.ByReference playParam) {
        this.playParam = playParam;
    }

    public boolean check(){
        return (playLock.get() == 0 && playParam != null);
    }

    public int get() {
        return playLock.getAndIncrement();
    }

    public int free() {
        return playLock.getAndDecrement();
    }

    public boolean isSpare() {
        return playLock.get() == 0;
    }

}
