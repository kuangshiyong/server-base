package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.core.web.WebResponse;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.service.DeviceStorge;
import cn.bjzfgcjs.idefense.device.bean.PtzCmdBean;
import cn.bjzfgcjs.idefense.device.camera.hikvision.HikCtl;
import cn.bjzfgcjs.idefense.device.camera.hikvision.PTZCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/idefense")
public class CameraController {
    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);

//    @Resource
//    private PubMessage pubMessage;
//
//    @Resource
//    private Login login;

    @Resource
    private HikCtl hikCtl;

    @Resource
    private DeviceStorge deviceStorge;

    @GetMapping(value = "/camera/realtime", produces = "application/json; charset=UTF-8")
    public Object getRtspUrl(@RequestParam String position) throws Exception {

//       HikCtl hikCtl = new HikCtl("");
//       return WebResponse.write(hikCtl.getRtspURl());
        return WebResponse.write("rtsp://admin:hzzf888888@192.168.1.64:554/");
    }

    @PostMapping(value = "/ptz/run", produces = "application/json; charset=UTF-8")
    public Object runPTZ(@RequestBody PtzCmdBean ptzCmdBean) throws Exception {

        DeviceInfo deviceInfo = deviceStorge.getDeviceById(ptzCmdBean.getId());

        if (hikCtl.hasPTZ(deviceInfo)) {
            PTZCode code = Enum.valueOf(PTZCode.class, ptzCmdBean.getCmd());
            hikCtl.ptzCtl(deviceInfo, code.getKey(), ptzCmdBean.getSpeed(), ptzCmdBean.getStart());
        }
        logger.info("param:{}", ptzCmdBean);
        return WebResponse.write("", AppCode.OK);
    }
}


