package cn.bjzfgcjs.idefense.device;

import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.DeviceStorage;
import cn.bjzfgcjs.idefense.service.PubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DevManager {

    private static final Logger logger = LoggerFactory.getLogger(DevManager.class);

    @Resource
    private PubMessage pubMessage;

    @Resource
    private DeviceStorage deviceStorage;

    // deviceId : lock; TODO：是否考虑超时解除锁定？
    private static final ConcurrentHashMap<String, Boolean> devLock = new ConcurrentHashMap<>();


    public boolean canUse(DeviceInfo deviceInfo) {
        return devLock.getOrDefault(deviceInfo.getID(), true);
    }

    public int acqurire(DeviceInfo deviceInfo) {
        if (canUse(deviceInfo)) {
            devLock.put(deviceInfo.getID(), false);
            return AppCode.OK.getCode();
        } else {
            return AppCode.DEV_BUSY.getCode();
        }
    }

    public void release(DeviceInfo deviceInfo) {
        devLock.put(deviceInfo.getID(), true);
    }

    // TODO: 设备故障报警消息入库以及发布通知
    public void reportDevStatus(DeviceInfo deviceInfo, Integer status) {

        pubMessage.devStatus().publish("");

    }
}
