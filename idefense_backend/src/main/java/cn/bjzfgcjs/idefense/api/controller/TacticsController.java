package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.api.bean.DeviceReq;
import cn.bjzfgcjs.idefense.common.utils.MiscUtil;
import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.core.web.WebResponse;
import cn.bjzfgcjs.idefense.dao.DeviceStorage;
import cn.bjzfgcjs.idefense.dao.TacticsStorage;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.domain.Tactics;
import cn.bjzfgcjs.idefense.device.DevManager;
import cn.bjzfgcjs.idefense.service.TacticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/idefense")
public class TacticsController {
    private static final Logger logger = LoggerFactory.getLogger(TacticsController.class);

    @Resource
    private TacticService tacticService;


    @PostMapping(value = "/tactics/run", produces = "application/json; charset=UTF-8")
    public Object addPosition(@RequestParam Integer position, @RequestParam Long tacticId) throws Exception {
         AppCode ret = tacticService.runTactic(position, tacticId);

        return WebResponse.write("", ret);
    }
}
