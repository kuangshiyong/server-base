package cn.bjzfgcjs.idefense.dao.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

//智防监控事件日志表
@Table(name = "zf_eventrecord")
public class EventRecord implements Serializable {

    @Id
    @Column(name = "ID")
    private Long ID;

    @Column(name = "HappenType")
    private Short HappenType;

    @Column(name = "PositionCode")
    private Integer PositionCode;

    @Column(name = "EventID")
    private Integer EventID;

    @Column(name = "EventRange")
    private Integer EventRange;

    @Column(name = "GPS")
    private String GPS;

    @Column(name = "TacticsID")
    private Long TacticsID;

    @Column(name = "VideoLogFile")
    private String VideoLogFile;

    @Column(name = "SoundLogFile")
    private String SoundLogFile;

    @Column(name = "CountriesandRegionsCode")
    private String CountriesandRegionsCode;

    private Long ctime;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Short getHappenType() {
        return HappenType;
    }

    public void setHappenType(Short happenType) {
        HappenType = happenType;
    }

    public Integer getPositionCode() {
        return PositionCode;
    }

    public void setPositionCode(Integer positionCode) {
        PositionCode = positionCode;
    }

    public Integer getEventID() {
        return EventID;
    }

    public void setEventID(Integer eventID) {
        EventID = eventID;
    }

    public Integer getEventRange() {
        return EventRange;
    }

    public void setEventRange(Integer eventRange) {
        EventRange = eventRange;
    }

    public String getGPS() {
        return GPS;
    }

    public void setGPS(String GPS) {
        this.GPS = GPS;
    }

    public Long getTacticsID() {
        return TacticsID;
    }

    public void setTacticsID(Long tacticsID) {
        TacticsID = tacticsID;
    }

    public String getVideoLogFile() {
        return VideoLogFile;
    }

    public void setVideoLogFile(String videoLogFile) {
        VideoLogFile = videoLogFile;
    }

    public String getSoundLogFile() {
        return SoundLogFile;
    }

    public void setSoundLogFile(String soundLogFile) {
        SoundLogFile = soundLogFile;
    }

    public String getCountriesandRegionsCode() {
        return CountriesandRegionsCode;
    }

    public void setCountriesandRegionsCode(String countriesandRegionsCode) {
        CountriesandRegionsCode = countriesandRegionsCode;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }
}
