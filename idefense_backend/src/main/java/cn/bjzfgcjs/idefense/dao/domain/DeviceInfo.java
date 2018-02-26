package cn.bjzfgcjs.idefense.dao.domain;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "zf_device")
public class DeviceInfo implements Serializable {
    @Id
    private String ID;

    private Integer Position = 0;

    private String IPAddress = "";

    private Integer IPPort = 0;

    private String UserName = "";

    private String Password = "";

    private Byte Type;

    private String Mode = "";

    private String Firmware = "";

    private String Manufacturer = "";

    private Long Date = 0L;

//    TODO: 待协商
//    private String serialno;
//
//    private Boolean ptz;
//
//    private Integer channel;

    private Long ctime = 0L;

    private Long utime = 0L;

}

