package cn.bjzfgcjs.idefense.dao.domain;

import cn.bjzfgcjs.idefense.common.annotation.JsonSkipTag;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "zf_device")
public class DeviceInfo implements Serializable {
    @Id
    @Column(name = "ID")
    private String ID;

    private Integer Position;

    @Column(name = "IPAddress")
    private String IPAddress;

    @Column(name = "IPPort")
    private Integer IPPort;

    @Column(name = "UserName")
    private String UserName;

    @Column(name = "Password")
    private String PassWord;

    //设备类型; 0-未定义，1-CCD, 2-IR, 3-云台, 4-声学，5-雷达***另行规定***
    private Byte Type;

    private String Model;

    private String Firmware;

    private String Manufacturer;

    private Long Date;

    private String Serial = "";

    @JsonSkipTag
    private Long ctime;

    @JsonSkipTag
    private Long utime;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Integer getPosition() {
        return Position;
    }

    public void setPosition(Integer position) {
        Position = position;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public Integer getIPPort() {
        return IPPort;
    }

    public void setIPPort(Integer IPPort) {
        this.IPPort = IPPort;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public Byte getType() {
        return Type;
    }

    public void setType(Byte type) {
        Type = type;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getFirmware() {
        return Firmware;
    }

    public void setFirmware(String firmware) {
        Firmware = firmware;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public Long getDate() {
        return Date;
    }

    public void setDate(Long date) {
        Date = date;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    public enum Type{
        NULL,      // 未知
        CCD,       // CCD
        IR,        // 热成像
        Terrence,  // 云台
        Acoustic,  // 声学
        Radar;     // 雷达
    }
}

