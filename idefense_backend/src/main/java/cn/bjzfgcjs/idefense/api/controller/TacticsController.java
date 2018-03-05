package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.common.utils.MiscUtil;
import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.core.web.WebResponse;
import cn.bjzfgcjs.idefense.dao.DeviceStorage;
import cn.bjzfgcjs.idefense.dao.TacticsStorage;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.domain.Tactics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/idefense")
public class TacticsController {
    private static final Logger logger = LoggerFactory.getLogger(TacticsController.class);

    @Resource
    private DeviceStorage deviceStorage;

    @Resource
    private TacticsStorage tacticsStorage;


    @PostMapping(value = "/tactics/run", produces = "application/json; charset=UTF-8")
    public Object addPosition(@RequestParam Integer position, @RequestParam Long tacticId) throws Exception {
        // 执行策略
        // 有声音，就检查声卡

        return WebResponse.write("");
    }
}
