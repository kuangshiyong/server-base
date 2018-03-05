package cn.bjzfgcjs.idefense.dao.domain;

import cn.bjzfgcjs.idefense.common.annotation.JsonSkipTag;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

//智防机位信息表
@Table(name = "zf_position")
public class Position {
    @Id
    @Column(name = "PositionCode")
    private Integer PositionCode;

    @Id
    @Column(name = "IdefenseCode")
    private String IdefenseCode;

    private String Description;

    private String Coordinate;

    @Column(name = "GroupNo")
    private Integer GroupNo;

    @Column(name = "hasPTZ")
    private Integer hasPTZ;

    private String Model;

    private String Config;

    private Integer Status;

    @Column(name = "StatusDesc")
    private String StatusDesc;

    @JsonSkipTag
    private Long ctime;

    @JsonSkipTag
    private Long utime;

    public Integer getPositionCode() {
        return PositionCode;
    }

    public void setPositionCode(Integer positionCode) {
        PositionCode = positionCode;
    }

    public String getIdefenseCode() {
        return IdefenseCode;
    }

    public void setIdefenseCode(String idefenseCode) {
        IdefenseCode = idefenseCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCoordinate() {
        return Coordinate;
    }

    public void setCoordinate(String coordinate) {
        Coordinate = coordinate;
    }

    public Integer getGroupNo() {
        return GroupNo;
    }

    public void setGroupNo(Integer groupNo) {
        GroupNo = groupNo;
    }

    public Integer getHasPTZ() {
        return hasPTZ;
    }

    public void setHasPTZ(Integer hasPTZ) {
        this.hasPTZ = hasPTZ;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getConfig() {
        return Config;
    }

    public void setConfig(String config) {
        Config = config;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
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
