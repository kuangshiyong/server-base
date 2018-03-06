package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.common.utils.MiscUtil;
import cn.bjzfgcjs.idefense.common.utils.Now;
import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.core.web.WebResponse;
import cn.bjzfgcjs.idefense.dao.DeviceStorage;
import cn.bjzfgcjs.idefense.dao.TacticsStorage;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.domain.SysInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/idefense")
public class InfoController {
    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Resource
    private DeviceStorage deviceStorge;

    @Resource
    private TacticsStorage tacticsStorage;


    /************************** 系统信息 *************************/

    // 获取当前系统信息
    @GetMapping(value = "/systeminfo", produces = "application/json; charset=UTF-8")
    public Object getSysInfo() throws Exception {
        SysInfo current = deviceStorge.getCurSysInfo();
        if (current != null)
            return WebResponse.write(current);
        else
            return WebResponse.write("no record", AppCode.DB_ERROR);
    }

    @PostMapping(value = "/systeminfo", produces = "application/json; charset=UTF-8")
    public Object addSysInfo(@RequestBody SysInfo obj) throws Exception {
        if (StringUtils.isBlank(obj.getSoundDir()) ||
                StringUtils.isBlank(obj.getSnapshotdir()) ||
                StringUtils.isBlank(obj.getVideoDir()) ||
                StringUtils.isBlank(obj.getLogDir()) ||
                StringUtils.isBlank(obj.getLocation()) ||
                StringUtils.isBlank(obj.getProjectName()) ||
                StringUtils.isBlank(obj.getCustomername()) ||
                obj.getDeploy() == null ||
                obj.getScenario() == null) {
            return WebResponse.write("", AppCode.BAD_REQUEST);
        }

        // 要系统填的参数
        Long now = Now.getMillis();
        obj.setUtime(now);
        obj.setCtime(now);
        obj.setSessionID(deviceStorge.getSysSessionId());

        if (deviceStorge.addSysInfo(obj) > 0) {
            SysInfo newInfo = deviceStorge.getCurSysInfo();
            return WebResponse.write(newInfo);
        } else {
            return WebResponse.write("", AppCode.DB_ERROR);
        }
    }

    @PutMapping(value = "/systeminfo", produces = "application/json; charset=UTF-8")
    public Object updateSysInfo(@RequestBody SysInfo obj) throws Exception {
        if (StringUtils.isBlank(obj.getSessionID())) {
            return WebResponse.write("", AppCode.BAD_REQUEST);
        }

        // 有历史记录 ？
        SysInfo current = deviceStorge.getCurSysInfo();
        if (current == null) {
            return WebResponse.write("", AppCode.DB_ERROR);
        }

        // SessionId 吻合，合法数据 ？
        if (!current.getSessionID().equals(obj.getSessionID())) {
            return WebResponse.write("", AppCode.BAD_REQUEST);
        }

        obj.setUtime(Now.getMillis());
        obj.setSessionID(deviceStorge.getSysSessionId());

        if (deviceStorge.updateSysInfo(obj) > 0) {
            return WebResponse.write("");
        } else {
            return WebResponse.write("", AppCode.DB_ERROR);
        }
    }

    /************************** 机位信息 *************************/

    @GetMapping(value = "/installation", produces = "application/json; charset=UTF-8")
    public Object getPosition(@RequestParam Integer no) throws Exception {
        if (no == null || no < 1) {
            return WebResponse.write("", AppCode.BAD_REQUEST);
        }

        Position position = deviceStorge.getPosByPostionCode(no);
        if (position != null)
            return WebResponse.write(position);
        else
            return WebResponse.write("no record in db", AppCode.DB_ERROR);
    }

    @PostMapping(value = "/installation", produces = "application/json; charset=UTF-8")
    public Object addPosition(@RequestBody Position obj) throws Exception {
        if (obj.getPositionCode() == null || obj.getIdefenseCode() == null ||
                obj.getGroupNo() == null  || obj.getStatus() == null ) {
            return WebResponse.write("", AppCode.BAD_REQUEST);
        }

        Long now = Now.getMillis();
        obj.setCtime(now);
        obj.setUtime(now);
        if (deviceStorge.addPosition(obj) > 0) {
            return WebResponse.write(obj);
        } else {
            return WebResponse.write("", AppCode.DB_ERROR);
        }
    }

    @PutMapping(value = "/installation", produces = "application/json; charset=UTF-8")
    public Object updatePosition(@RequestBody Position obj) throws Exception {
        if (obj.getPositionCode() == null || obj.getIdefenseCode() == null ) {
            return WebResponse.write("", AppCode.BAD_REQUEST);
        }

        obj.setUtime(Now.getMillis());
        if (deviceStorge.updatePosition(obj) > 0) {
            return WebResponse.write(obj);
        } else {
            return WebResponse.write("", AppCode.DB_ERROR);
        }
    }

    @DeleteMapping(value = "/installation", produces = "application/json; charset=UTF-8")
    public Object delPosition(@RequestParam Integer no) throws Exception {

        if (deviceStorge.delPosition(no) > 0) {
            return WebResponse.write("");
        } else {
            return WebResponse.write("", AppCode.DB_ERROR);
        }
    }

    /************************** 设备信息 *************************/

    @GetMapping(value = "/deviceInfo", produces = "application/json; charset=UTF-8")
    public Object getDevice(@RequestParam Integer position,  @RequestParam Byte type) throws Exception {
        if (position == null || position < 1 ||
                type == null || type < 1)
            return WebResponse.write("", AppCode.BAD_REQUEST);

        DeviceInfo deviceInfo = deviceStorge.getDeviceByPosType(position, type);
        if (deviceInfo != null)
            return WebResponse.write(deviceInfo);
        else
            return WebResponse.write("no record in db", AppCode.DB_ERROR);
    }

    @PostMapping(value = "/deviceInfo", produces = "application/json; charset=UTF-8")
    public Object addDevice(@RequestBody DeviceInfo obj) throws Exception {
        if (StringUtils.isBlank(obj.getIPAddress())
                || obj.getType() == null
                || obj.getPosition() == null
                || obj.getPosition() < 1) {
            return WebResponse.write("", AppCode.BAD_REQUEST);
        }

        obj.setID(MiscUtil.getID());
        Long now = Now.getMillis();
        obj.setCtime(now);
        obj.setUtime(now);
        if (deviceStorge.addDevice(obj) > 0) {
            return WebResponse.write(obj);
        } else {
            return WebResponse.write("no record in db", AppCode.DB_ERROR);
        }
    }

    @PutMapping(value = "/deviceInfo", produces = "application/json; charset=UTF-8")
    public Object updateDevice(@RequestBody DeviceInfo obj) throws Exception {
        if (StringUtils.isBlank(obj.getID()) ||
                deviceStorge.getDeviceById(obj.getID()) == null) {
            return WebResponse.write("", AppCode.BAD_REQUEST);
        }

        obj.setUtime(Now.getMillis());
        if (deviceStorge.updateDevice(obj) > 0) {
            return WebResponse.write(obj);
        } else {
            return WebResponse.write("no record in db", AppCode.DB_ERROR);
        }
    }



}
