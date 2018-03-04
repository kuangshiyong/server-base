package cn.bjzfgcjs.idefense.dao.domain;

import cn.bjzfgcjs.idefense.common.annotation.JsonSkipTag;
import com.sun.javafx.beans.IDProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

// 安防策略总表
@Table(name = "zf_tactis")
public class Tactics implements Serializable {
    @Id
    @Column(name = "ID")
    private Long ID;

    @Column(name = "TacticsName")
    private String TacticsName;

    private String Publisher;

    @Column(name = "PublishType")
    private Byte PublishType;

    @Column(name = "UseTimes")
    private Integer UseTimes;

    private Integer Scenario;

    private String Actions;

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

    public String getTacticsName() {
        return TacticsName;
    }

    public void setTacticsName(String tacticsName) {
        TacticsName = tacticsName;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public Byte getPublishType() {
        return PublishType;
    }

    public void setPublishType(Byte publishType) {
        PublishType = publishType;
    }

    public Integer getUseTimes() {
        return UseTimes;
    }

    public void setUseTimes(Integer useTimes) {
        UseTimes = useTimes;
    }

    public Integer getScenario() {
        return Scenario;
    }

    public void setScenario(Integer scenario) {
        Scenario = scenario;
    }

    public String getActions() {
        return Actions;
    }

    public void setActions(String actions) {
        Actions = actions;
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
