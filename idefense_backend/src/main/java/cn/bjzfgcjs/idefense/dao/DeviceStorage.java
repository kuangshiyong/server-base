package cn.bjzfgcjs.idefense.dao;

import cn.bjzfgcjs.idefense.common.utils.MiscUtil;
import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.domain.SysInfo;
import cn.bjzfgcjs.idefense.dao.mapper.DeviceInfoMapper;
import cn.bjzfgcjs.idefense.dao.mapper.PositionMapper;
import cn.bjzfgcjs.idefense.dao.mapper.SysInfoMapper;
import javafx.geometry.Pos;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DeviceStorage {

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private PositionMapper positionMapper;

    @Resource
    private SysInfoMapper sysInfoMapper;


    /************************  设备详情 (DeviceInfo) ***************************/

    public DeviceInfo getDeviceById(String deviceId) {
        return deviceInfoMapper.selectByPrimaryKey(deviceId);
    }

    public int addDevice(DeviceInfo obj) {
        return deviceInfoMapper.insertSelective(obj);
    }

    public int updateDevice(DeviceInfo obj) {
        return deviceInfoMapper.updateByPrimaryKeySelective(obj);
    }

    public DeviceInfo getDeviceByPosType(Integer position, Byte type) {
        DeviceInfo obj = new DeviceInfo();
        obj.setPosition(position);
        obj.setType(type);
        return deviceInfoMapper.selectOne(obj);
    }

    public List<DeviceInfo> getDeviceListByType(Byte type) {
        DeviceInfo obj = new DeviceInfo();
        obj.setType(type);
        return deviceInfoMapper.select(obj);
    }

    /************************ 机位详情 (Position) ***************************/

    public Position getPosByPostionCode(Integer positionCode) {
        Position position = new Position();
        position.setPositionCode(positionCode);
        return positionMapper.selectOne(position);
    }

    public int addPosition(Position obj) {
        return positionMapper.insertSelective(obj);
    }

    public int updatePosition(Position obj) {
        return positionMapper.updateByPrimaryKeySelective(obj);
    }

    public int delPosition(Integer positionCode) {
        Position position = new Position();
        position.setPositionCode(positionCode);
        return positionMapper.delete(position);
    }

    /************************ 系统信息 (SysInfo) ***************************/

    // 当前系统信息
    public SysInfo getCurSysInfo() {
        Condition condition = new Condition(SysInfo.class);
        condition.orderBy("utime").desc();
        return sysInfoMapper.selectOneByExample(condition);
    }

    // 系统信息更新的历史记录
    public List<SysInfo> getSysInfoLog() {
        return sysInfoMapper.selectAll();
    }

    // 更新系统信息 （先获取最近的记录）
    public int updateSysInfo(SysInfo obj) {
        SysInfo current = getCurSysInfo();
        obj.setID(current.getID());
        return sysInfoMapper.updateByPrimaryKeySelective(obj);
    }

    public String getSysSessionId() {
        SysInfo current = getCurSysInfo();
        String sessionId = null;
        sessionId = (current == null) ? MiscUtil.getID() : current.getSessionID();

        return sessionId;
    }

    // 新增记录
    public int addSysInfo(SysInfo obj) {
        return sysInfoMapper.insertUseGeneratedKeys(obj);
    }
}
