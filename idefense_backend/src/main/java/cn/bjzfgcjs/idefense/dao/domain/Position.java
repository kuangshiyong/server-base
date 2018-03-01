package cn.bjzfgcjs.idefense.dao.domain;

import javax.persistence.Id;
import javax.persistence.Table;

//智防机位信息表
@Table(name = "zf_position")
public class Position {
    @Id
    private Integer PositionCode;

    @Id
    private String IdefenseCode;

    private String Description;

    private String Coordinate;

    private Integer Group;

    //add column 2018-2-25 wuchao
    private Integer hasPTZ;

    private String Model;

    private String Config;

    private Integer Status;

    private String StatusDesc;

    private Long ctime;

    private Long utime;
}
