package cn.bjzfgcjs.idefense.dao;

import cn.bjzfgcjs.idefense.dao.domain.EventInfo;
import cn.bjzfgcjs.idefense.dao.domain.EventRecord;
import cn.bjzfgcjs.idefense.dao.domain.Tactics;
import cn.bjzfgcjs.idefense.dao.mapper.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TacticsStorage {
    @Resource
    private TacticsMapper tacticsMapper;

    @Resource
    private AcousticCertMapper acousticCertMapper;

    @Resource
    private AcousticFileMapper acousticFileMapper;

    @Resource
    private EventInfoMapper eventInfoMapper;

    @Resource
    private EventRecordMapper eventRecordMapper;

    /////////////////////  声学文件认证 (AcousticCert)  ////////////////////////


    /////////////////////  声学文件信息 (AcousticFile)  ////////////////////////


    /////////////////////  安防策略 (Tactics)        ////////////////////////

    public Tactics getTacticsById(Long id) {
        return tacticsMapper.selectByPrimaryKey(id);
    }

    public int addTactics(Tactics obj) {
        return tacticsMapper.insertSelective(obj);
    }

    //////////////////////////    智防监控事件日志    //////////////////////////////
    public int addEventRecord(EventRecord obj) {
        return eventRecordMapper.insertUseGeneratedKeys(obj);
    }
}