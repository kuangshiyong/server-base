package cn.bjzfgcjs.idefense.dao.domain;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

//智防监控事件信息表
@Table(name = "zf_event")
public class EventInfo implements Serializable {
    @Id
    private Integer ID;

    private Integer Type;

    private String EventDesc;

    private String TypeDesc;

    private Boolean Enable;

    private Boolean Manuable;

    private Long ctime;

    private Long utime;


}
