package cn.bjzfgcjs.idefense.dao.domain;

import com.sun.javafx.beans.IDProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

// 安防策略总表
@Table(name = "zf_tactis")
public class Tactics implements Serializable {
    @Id
    private Long ID;

    private String Name;

    private String Publisher;

    private Byte PublishType;

    private Integer UseTimes;

    private Integer Scenario;

    private String Actions;

    private Long ctime;

    private Long utime;


}
