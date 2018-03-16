package cn.bjzfgcjs.idefense.service;

import cn.bjzfgcjs.idefense.common.utils.GsonTool;
import cn.bjzfgcjs.idefense.core.AppCode;
import cn.bjzfgcjs.idefense.dao.DeviceStorage;
import cn.bjzfgcjs.idefense.dao.TacticsStorage;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Tactics;
import cn.bjzfgcjs.idefense.device.DevManager;
import cn.bjzfgcjs.idefense.device.sound.sv2101.LCPlayback;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TacticService {
    @Resource
    private TacticsStorage tacticsStorage;

    @Resource
    private LCPlayback lcPlayback;

    @Resource
    private DevManager devManager;

    @Resource
    private DeviceStorage deviceStorage;

    @Resource
    private TaskExecutor taskExecutor;


    public AppCode runTactic(Integer position, Long tacticId) {
        Tactics tactics = tacticsStorage.getTacticsById(tacticId);
        String content = tactics.getActions();
        final Tactics.Action actions = GsonTool.fromJson(content, Tactics.Action.class);
        if (actions != null && actions.getSound() != null) {

            // 声学策略需要声学设备
            final DeviceInfo obj = deviceStorage.getDeviceByPosType(position, DeviceInfo.Type.Acoustic);
            if (!devManager.canUse(obj))
                return AppCode.DEV_BUSY;
            devManager.acqurire(obj);

            taskExecutor.execute(()-> {
                List<Tactics.Sound> soundList = Tactics.getAcousticList(actions.getSound());
                if (soundList != null && soundList.size() > 0) {
                    for (Tactics.Sound sound : soundList) {
                        String filename = "D:/work/" + sound.getSoundID() + ".mp3";
                        lcPlayback.playLoop(obj, filename, sound.getVolume(),
                                sound.getTimes(), sound.getInterval());
                    }
                }
            });
        }
        return AppCode.OK;
    }

}
