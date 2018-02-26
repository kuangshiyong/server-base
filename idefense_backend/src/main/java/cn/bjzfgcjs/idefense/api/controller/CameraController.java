package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.core.DataCache;
import cn.bjzfgcjs.idefense.core.web.HWebLogBean;
import cn.bjzfgcjs.idefense.core.web.WebResponse;
import cn.bjzfgcjs.idefense.device.bean.HikHandlerBean;
import cn.bjzfgcjs.idefense.device.bean.PtzCmdBean;
import cn.bjzfgcjs.idefense.device.camera.hikvision.HikCtl;
import cn.bjzfgcjs.idefense.device.camera.hikvision.Login;
import cn.bjzfgcjs.idefense.device.camera.hikvision.PTZCode;
import cn.bjzfgcjs.idefense.service.PubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.EnumMap;

@RestController
@RequestMapping("/idefense")
public class CameraController {
    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);

//    @Resource
//    private PubMessage pubMessage;
//
//    @Resource
//    private Login login;
//
//    @Resource
//    private HikCtl hikCtl;

    @GetMapping(value = "/camera/realtime", produces = "application/json; charset=UTF-8")
    public Object getRtspUrl(@RequestParam String position) throws Exception {

//       HikCtl hikCtl = new HikCtl("");
//       return WebResponse.write(hikCtl.getRtspURl());
        return WebResponse.write("rtsp://admin:hzzf888888@192.168.1.64:554/");
    }

    @PostMapping(value = "/ptz/run", produces = "application/json; charset=UTF-8")
    public Object runPTZ(@RequestBody PtzCmdBean ptzCmdBean) throws Exception {
        HikCtl hikCtl = new HikCtl(ptzCmdBean.getId());
        String deviceId = ptzCmdBean.getId();
        if (hikCtl.isAvailable(deviceId) && hikCtl.hasPTZ(deviceId)) {
            PTZCode code = Enum.valueOf(PTZCode.class, ptzCmdBean.getCmd());
            hikCtl.ptzCtl(deviceId, code.getKey(), ptzCmdBean.getSpeed(), ptzCmdBean.getStart());
        }
        logger.info("param:{}", ptzCmdBean);
        return WebResponse.write("", AppCode.OK);
    }
}


