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

    //modify Password to PassWord 2018-2-25 wuchao
    private String PassWord = "";

    private Byte Type;

    //modify Mode to Model 2018-2-25 wuchao
    private String Model = "";

    private String Firmware = "";

    private String Manufacturer = "";

    private Long Date = 0L;

    //addColumn 2018-2-25 wuchao
    private String Serial = "";




//    TODO: 待协商
//    private String serialno;
//
//    private Boolean ptz;
//
//    private Integer channel;

    private Long ctime = 0L;

    private Long utime = 0L;

}

