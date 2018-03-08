package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.api.bean.Audio;
import cn.bjzfgcjs.idefense.common.utils.GsonTool;
import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.core.web.HWebLogBean;
import cn.bjzfgcjs.idefense.core.web.WebResponse;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.mapper.DeviceInfoMapper;
import cn.bjzfgcjs.idefense.dao.DeviceStorage;
import cn.bjzfgcjs.idefense.device.sound.sv2101.LCPlayback;
import cn.bjzfgcjs.idefense.service.PublishMsg;
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
    private PublishMsg publishMsg;

    @Resource
    private LCPlayback lcPlayback;

    @Resource
    private DeviceStorage deviceStorage;


    @GetMapping(value = "/test/ttm", produces = "application/json; charset=UTF-8")
    public Object runRedisPublish(@RequestParam String topic, @RequestParam String content) throws Exception {
        try {
            publishMsg.radarMessage().publish(content);
        } catch (Exception e) {}
        return WebResponse.write("", AppCode.OK);
    }

    @PostMapping(value = "/test/audio/play", produces = "application/json; charset=UTF-8")
    public Object testAudio(@RequestBody Audio audio)throws Exception {
        DeviceInfo deviceInfo = deviceStorage.getDeviceByPosType(
                audio.getPosition(),
                DeviceInfo.Type.Acoustic);
        HWebLogBean.addProp("device", GsonTool.toJson(deviceInfo));

        int ret = lcPlayback.checkStatus(deviceInfo);
        if (ret != AppCode.DEV_OK.getCode())
            return WebResponse.write("device unreachable", AppCode.lookup(ret));

        if (audio.getPlay()) {
            lcPlayback.playLoop(deviceInfo, audio.getFile(), audio.getVolume(),
                    audio.getLoop(), 100);
        } else {
            lcPlayback.stop(deviceInfo);
        }

        return WebResponse.write("", AppCode.lookup(ret));
    }
}
