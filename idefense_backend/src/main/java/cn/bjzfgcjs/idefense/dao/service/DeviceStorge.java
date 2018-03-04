package cn.bjzfgcjs.idefense.dao.service;

import cn.bjzfgcjs.idefense.dao.domain.DeviceInfo;
import cn.bjzfgcjs.idefense.dao.domain.Position;
import cn.bjzfgcjs.idefense.dao.mapper.DeviceInfoMapper;
import cn.bjzfgcjs.idefense.dao.mapper.PositionMapper;
import javafx.geometry.Pos;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class DeviceStorge {

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private PositionMapper positionMapper;


    /////////////////////  设备详情  ////////////////////////

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

    ///////////////////// 机位详情 ///////////////////////////

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


}
