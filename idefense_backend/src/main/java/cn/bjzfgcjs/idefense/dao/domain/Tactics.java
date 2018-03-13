package cn.bjzfgcjs.idefense.dao.domain;

import cn.bjzfgcjs.idefense.common.annotation.JsonSkipTag;
import cn.bjzfgcjs.idefense.common.utils.GsonTool;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sun.javafx.beans.IDProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// 安防策略总表
@Table(name = "zf_tactics")
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

    public static class Action {
        public String creater;
        public String url;
        public String actiondescribe;
        public JsonElement operate;   //执行规则
        public JsonElement sound;
        public JsonElement light;

        public String getCreater() {
            return creater;
        }

        public void setCreater(String creater) {
            this.creater = creater;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getActiondescribe() {
            return actiondescribe;
        }

        public void setActiondescribe(String actiondescribe) {
            this.actiondescribe = actiondescribe;
        }

        public JsonElement getOperate() {
            return operate;
        }

        public void setOperate(JsonElement operate) {
            this.operate = operate;
        }

        public JsonElement getSound() {
            return sound;
        }

        public void setSound(JsonElement sound) {
            this.sound = sound;
        }

        public JsonElement getLight() {
            return light;
        }

        public void setLight(JsonElement light) {
            this.light = light;
        }
    }

    public static class Sound {
        Integer order;  // 排执行顺序

        String soundID;

        Integer volume;

        Integer times;

        Integer interval;

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getSoundID() {
            return soundID;
        }

        public void setSoundID(String soundID) {
            this.soundID = soundID;
        }

        public Integer getVolume() {
            return volume;
        }

        public void setVolume(Integer volume) {
            this.volume = volume;
        }

        public Integer getTimes() {
            return times;
        }

        public void setTimes(Integer times) {
            this.times = times;
        }

        public Integer getInterval() {
            return interval;
        }

        public void setInterval(Integer interval) {
            this.interval = interval;
        }
    }

    public static List<Sound> getAcousticList(JsonElement sounds) {
        List<Sound> soundList = GsonTool.fromJsonList(sounds, Sound.class);
        if (soundList == null) return null;

        soundList.sort((s1, s2) -> s1.order.compareTo(s2.order));
        return soundList;
    }
}
