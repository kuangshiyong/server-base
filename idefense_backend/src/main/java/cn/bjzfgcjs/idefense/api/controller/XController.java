package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.common.utils.GsonTool;
import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.core.web.WebResponse;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.mapper.DeviceInfoMapper;
import cn.bjzfgcjs.idefense.dao.service.DeviceStorge;
import cn.bjzfgcjs.idefense.device.sound.sv2101.LCPlayback;
import cn.bjzfgcjs.idefense.service.PubMessage;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/idefense")
public class XController {

    private static final Logger logger = LoggerFactory.getLogger(XController.class);

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private PubMessage pubMessage;

    @Resource
    private LCPlayback lcPlayback;

    @Resource
    private DeviceStorge deviceStorge;


    @GetMapping(value = "/test/ttm", produces = "application/json; charset=UTF-8")
    public Object runRedisPublish(@RequestParam String topic, @RequestParam String content) throws Exception {
        try {
            DeviceInfo deviceInfo = deviceInfoMapper.selectByPrimaryKey("demo");
            RTopic<String> channel = redissonClient.getTopic(topic);
            channel.publish(content);
            return WebResponse.write(deviceInfo, AppCode.OK);
        } catch (Exception e) {}
        return WebResponse.write("", AppCode.OK);
    }

    @GetMapping(value = "/test/audio", produces = "application/json; charset=UTF-8")
    public Object testAudio(@RequestParam String file, @RequestParam Integer stop)throws Exception {
        DeviceInfo deviceInfo = deviceStorge.getDeviceByPosType(1, Integer.valueOf(4).byteValue());
        logger.info("device: {}", GsonTool.toJson(deviceInfo));

        // 如果是声卡的话，还得加停止指令
        if (lcPlayback.isAvailable(deviceInfo) && stop > 1) {
            logger.info("音频卡可以用的");
            lcPlayback.anounce(deviceInfo, file, 100);
        } else {
            lcPlayback.stop(deviceInfo);
        }
        return WebResponse.write("", AppCode.OK);
    }
}
