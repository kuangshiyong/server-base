package cn.bjzfgcjs.idefense.dao.domain;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

// 安防系统信息
@Table(name = "zf_sysinfo")
public class SysInfo implements Serializable {
    @Id
    private String ID;

    private Integer Deploy;

    private Long Scenario;

    private String ProjectName;

    private String Customername;

    private String Desc;

    private String Location;

    private String GPS;

    private String SonudDir;

    private String LogDir;

    private String VideoDir;

    private Integer Videotime;

    private String Snapshotdir;

    private String Snapshorttype;

    private Short Activity;

    private Long ctime;

    private Long utime;

}
