package cn.bjzfgcjs.idefense.dao.mapper;

import cn.bjzfgcjs.idefense.dao.domain.Position;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

//智防机位信息表
public interface PositionMapper extends Mapper<Position>, MySqlMapper<Position> {
}
