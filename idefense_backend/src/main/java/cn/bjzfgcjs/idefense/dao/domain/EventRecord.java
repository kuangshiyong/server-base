package cn.bjzfgcjs.idefense.dao.domain;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

//智防监控事件日志表
@Table(name = "zf_eventrecord")
public class EventRecord implements Serializable {
    @Id
    private Long ID;

    private Short HappenType;

    private Integer PositionCode;

    private Integer EventID;

    private Integer EventRange;

    private String GPS;

    private Long TacticsID;

    private String VideoLogFile;

    private String SoundLogFile;

    private String CountriesandRegions;

    private Long ctime;
}
