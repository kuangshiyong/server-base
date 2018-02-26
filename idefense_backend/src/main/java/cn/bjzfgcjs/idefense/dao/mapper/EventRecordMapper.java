package cn.bjzfgcjs.idefense.dao.mapper;

import cn.bjzfgcjs.idefense.dao.domain.EventRecord;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

//智防监控事件日志表
public interface EventRecordMapper extends Mapper<EventRecord>, MySqlMapper<EventRecordMapper> {
}
