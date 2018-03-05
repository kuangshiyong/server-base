package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.api.bean.DeviceReq;
import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.core.web.WebResponse;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.DeviceStorage;
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

    @Resource
    private HikCtl hikCtl;

    @Resource
    private DeviceStorage deviceStorage;

    // 实时视频
    @PostMapping(value = "/camera/realtime", produces = "application/json; charset=UTF-8")
    public Object getRtspUrl(@RequestBody DeviceReq req) throws Exception {
        DeviceInfo obj = deviceStorage.getDeviceByPosType(req.getPosition(), req.getType());
        if (obj == null) {
            WebResponse.write("", AppCode.OBJECT_NOTEXIST);
        }
        String rstpUrl = hikCtl.getRtspUrl(obj);

        return WebResponse.write(rstpUrl);
    }

    // 事件录像
    @PostMapping(value = "/camera/record", produces = "application/json; charset=UTF-8")
    public Object onRecording(@RequestBody DeviceReq req) throws Exception {
        DeviceInfo obj = deviceStorage.getDeviceByPosType(req.getPosition(), req.getType());
        if (obj == null) {
            WebResponse.write("", AppCode.OBJECT_NOTEXIST);
        }

        // TODO: 执行录像
        return WebResponse.write("");
    }

    // 事件截图
    @PostMapping(value = "/camera/snapshot", produces = "application/json; charset=UTF-8")
    public Object snapshot(@RequestBody DeviceReq req) throws Exception {
        DeviceInfo obj = deviceStorage.getDeviceByPosType(req.getPosition(), req.getType());
        if (obj == null) {
            WebResponse.write("", AppCode.OBJECT_NOTEXIST);
        }

        // TODO: 执行截图
        return WebResponse.write("");
    }

    // 布防警戒
    @PostMapping(value = "/camera/event", produces = "application/json; charset=UTF-8")
    public Object setAlert(@RequestBody DeviceReq req) throws Exception {
        DeviceInfo obj = deviceStorage.getDeviceByPosType(req.getPosition(), req.getType());
        if (obj == null) {
            WebResponse.write("", AppCode.OBJECT_NOTEXIST);
        }

        // TODO: 执行截图
        return WebResponse.write("");
    }

    /*********************************  云台操作控制  *********************************/

    // 设置云台的限制性参数, 不仅保存在config里，并且生效保存到该机位上的摄像机，掉电不失
    // DeviceReq:Type - 云台
    @PostMapping(value = "/ptz/limit", produces = "application/json; charset=UTF-8")
    public Object setPtzLimit(@RequestBody DeviceReq req) throws Exception {
        DeviceInfo obj = deviceStorage.getDeviceByPosType(req.getPosition(), req.getType());
        if (obj == null) {
            WebResponse.write("", AppCode.OBJECT_NOTEXIST);
        }
        // TODO: 设置云台限制参数
        return WebResponse.write("");
    }

    // 云台工作设置：preset等 生效保存到该机位上的摄像机，掉电不失
    @PostMapping(value = "/ptz/set", produces = "application/json; charset=UTF-8")
    public Object setPtzSet(@RequestBody DeviceReq req) throws Exception {
        DeviceInfo obj = deviceStorage.getDeviceByPosType(req.getPosition(), req.getType());
        if (obj == null) {
            WebResponse.write("", AppCode.OBJECT_NOTEXIST);
        }
        // TODO: 设置云台限制参数
        return WebResponse.write("");
    }


    @PostMapping(value = "/ptz/run", produces = "application/json; charset=UTF-8")
    public Object runPtzCmd(@RequestBody PtzCmdBean ptzCmdBean) throws Exception {

        DeviceInfo deviceInfo = deviceStorage.getDeviceById(ptzCmdBean.getId());

        if (hikCtl.hasPTZ(deviceInfo)) {
            PTZCode code = Enum.valueOf(PTZCode.class, ptzCmdBean.getCmd());
            hikCtl.ptzCtl(deviceInfo, code.getKey(), ptzCmdBean.getSpeed(), ptzCmdBean.getStart());
        }
        logger.info("param:{}", ptzCmdBean);
        return WebResponse.write("", AppCode.OK);
    }
}


