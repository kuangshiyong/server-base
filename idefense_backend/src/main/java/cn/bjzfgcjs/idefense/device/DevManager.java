package cn.bjzfgcjs.idefense.device;

import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DevManager {

    private static final Logger logger = LoggerFactory.getLogger(DevManager.class);

    private static final String DevLock = "devLock";

    @Resource
    private RedissonClient redissonClient;


    public boolean acquire(DeviceInfo deviceInfo, Integer waitTime) {
        RLock lock = redissonClient.getLock(DevLock + deviceInfo.getID());
        return true;
    }
}
