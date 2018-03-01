package cn.bjzfgcjs.idefense.dao.domain;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

// 安防系统信息
@Table(name = "zf_sysinfo")
public class SysInfo implements Serializable {
    @Id
    private String ID;

    //add colum, 2018-2-25 wuchao
    private Short Operation;

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

    //modify Snapshorttype to Snapshottype 2018-2-25 wuchao
    private String Snapshottype;

    private Short Activity;

    //add 2 column 2018-2-25 wuchao
    private String FrontSys;

    private String BackendSys;

    private Long ctime;

    private Long utime;

}
