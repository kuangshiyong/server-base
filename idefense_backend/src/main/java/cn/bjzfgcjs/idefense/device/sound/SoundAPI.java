package cn.bjzfgcjs.idefense.device.sound;

import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;

public interface SoundAPI {

    public static final int InfiniteLoop = -1;

    public void playLoop(DeviceInfo deviceInfo, String audioFile, Integer volume, int loop, int interval);

    public int stop(DeviceInfo deviceInfo);

    public int setVolume(DeviceInfo deviceInfo, byte volume);

}
