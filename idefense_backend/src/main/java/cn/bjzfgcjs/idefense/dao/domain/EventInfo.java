//add class 2018-2-25 wuchao
package cn.bjzfgcjs.idefense.dao.domain;

import cn.bjzfgcjs.idefense.common.annotation.JsonSkipTag;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

//智防监控事件信息表
@Table(name = "zf_event")
public class EventInfo implements Serializable {
    @Id
    @GeneratedValue
    private Integer ID;

    private Integer Type;

    @Column(name = "EventDesc")
    private String EventDesc;

    @Column(name = "TypeDesc")
    private String TypeDesc;

    private Boolean Enable;

    private Boolean Manuable;

    @JsonSkipTag
    private Long ctime;

    @JsonSkipTag
    private Long utime;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public String getEventDesc() {
        return EventDesc;
    }

    public void setEventDesc(String eventDesc) {
        EventDesc = eventDesc;
    }

    public String getTypeDesc() {
        return TypeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        TypeDesc = typeDesc;
    }

    public Boolean getEnable() {
        return Enable;
    }

    public void setEnable(Boolean enable) {
        Enable = enable;
    }

    public Boolean getManuable() {
        return Manuable;
    }

    public void setManuable(Boolean manuable) {
        Manuable = manuable;
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
