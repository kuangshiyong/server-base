package cn.bjzfgcjs.idefense.dao.domain;

import javax.persistence.Column;

public class DeviceWarn {

    @Column(name = "ID")
    private String ID;

    @Column(name = "DeviceId")
    private String DeviceId;

    @Column(name = "PositionCode")
    private Integer PositionCode;

    private Byte Type;

    private String IP;

    @Column(name = "CommandId")
    private Integer CommandId; // 报警类型

    @Column(name = "EventId")
    private Integer EventId;   // 事件类型

    private String Desc;       // 事件描述

    private Long Date;         // 时间（ms）
}
