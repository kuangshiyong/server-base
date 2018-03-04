package cn.bjzfgcjs.idefense.dao.service;

import cn.bjzfgcjs.idefense.dao.domain.EventInfo;
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





}
