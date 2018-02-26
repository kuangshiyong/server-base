package cn.bjzfgcjs.idefense.dao.mapper;

import cn.bjzfgcjs.idefense.dao.domain.AcousticCert;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

// 声学文件认证表
public interface AcousticCertMapper extends Mapper<AcousticCert>, MySqlMapper<AcousticCert> {
}
