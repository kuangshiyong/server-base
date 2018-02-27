package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.core.web.WebResponse;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.mapper.DeviceInfoMapper;
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
    public Object testAudio(@RequestParam String file) throws Exception {
        lcPlayback.playback("demo", 3);
        return WebResponse.write("", AppCode.OK);
    }
}
