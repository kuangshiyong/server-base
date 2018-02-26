package cn.bjzfgcjs.idefense.dao.domain;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

//声学文件信息表
@Table(name = "zf_acoustic_file")
public class AcousticFile implements Serializable {
    @Id
    private String ID;

    private String Certification;

    private Byte UsingType;

    private Integer UsingTimes;

    private String FileName;

    private String Description;

    private Byte Sex;

    private String FileLanguage;

    private int Duration;

    private Long ctime;

    private Long utime;

}
