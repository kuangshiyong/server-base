package cn.bjzfgcjs.idefense.device.sound;

import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;

public interface SoundAPI {

    public static final int ALWAYS = -1;

    public int playLoop(DeviceInfo deviceInfo, String audioFile, Integer volume, int loop);

    public int stop(DeviceInfo deviceInfo);

    public int setVolume(DeviceInfo deviceInfo, byte volume);

}
