package cn.bjzfgcjs.idefense.dao.domain;

import cn.bjzfgcjs.idefense.common.annotation.JsonSkipTag;

import javax.persistence.*;
import java.io.Serializable;

// 安防系统信息
@Table(name = "zf_sysinfo")
public class SysInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;

    @Column(name = "SessionID")
    private String SessionID;

    private Short Operation;

    private Integer Deploy;

    private Long Scenario;

    @Column(name = "ProjectName")
    private String ProjectName;

    private String Customername;

    private String Desc;

    private String Location;

    @Column(name = "GPS")
    private String GPS;

    @Column(name = "SonudDir")
    private String SoundDir;

    @Column(name = "LogDir")
    private String LogDir;

    @Column(name = "VideoDir")
    private String VideoDir;

    private Integer Videotime;

    private String Snapshotdir;

    private String Snapshottype;

    private Short Activity;

    @Column(name = "FrontSys")
    private String FrontSys;

    @Column(name = "BackendSys")
    private String BackendSys;

    @JsonSkipTag
    private Long ctime;

    @JsonSkipTag
    private Long utime;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getSoundDir() {
        return SoundDir;
    }

    public void setSoundDir(String soundDir) {
        SoundDir = soundDir;
    }

    public Short getOperation() {
        return Operation;
    }

    public void setOperation(Short operation) {
        Operation = operation;
    }

    public Integer getDeploy() {
        return Deploy;
    }

    public void setDeploy(Integer deploy) {
        Deploy = deploy;
    }

    public Long getScenario() {
        return Scenario;
    }

    public void setScenario(Long scenario) {
        Scenario = scenario;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getCustomername() {
        return Customername;
    }

    public void setCustomername(String customername) {
        Customername = customername;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getGPS() {
        return GPS;
    }

    public void setGPS(String GPS) {
        this.GPS = GPS;
    }

    public String getLogDir() {
        return LogDir;
    }

    public void setLogDir(String logDir) {
        LogDir = logDir;
    }

    public String getVideoDir() {
        return VideoDir;
    }

    public void setVideoDir(String videoDir) {
        VideoDir = videoDir;
    }

    public Integer getVideotime() {
        return Videotime;
    }

    public void setVideotime(Integer videotime) {
        Videotime = videotime;
    }

    public String getSnapshotdir() {
        return Snapshotdir;
    }

    public void setSnapshotdir(String snapshotdir) {
        Snapshotdir = snapshotdir;
    }

    public String getSnapshottype() {
        return Snapshottype;
    }

    public void setSnapshottype(String snapshottype) {
        Snapshottype = snapshottype;
    }

    public Short getActivity() {
        return Activity;
    }

    public void setActivity(Short activity) {
        Activity = activity;
    }

    public String getFrontSys() {
        return FrontSys;
    }

    public void setFrontSys(String frontSys) {
        FrontSys = frontSys;
    }

    public String getBackendSys() {
        return BackendSys;
    }

    public void setBackendSys(String backendSys) {
        BackendSys = backendSys;
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
}
