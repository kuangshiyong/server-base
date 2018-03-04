package cn.bjzfgcjs.idefense.dao.domain;

import cn.bjzfgcjs.idefense.common.annotation.JsonSkipTag;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

//声学文件信息表
@Table(name = "zf_acoustic_file")
public class AcousticFile implements Serializable {
    @Id
    @Column(name = "ID")
    private String ID;

    private String Certification;

    @Column(name = "UsingType")
    private Byte UsingType;

    @Column(name = "UsingTimes")
    private Integer UsingTimes;

    @Column(name = "FileName")
    private String FileName;

    private String Description;

    private Byte Sex;

    @Column(name = "FileLanguage")
    private String FileLanguage;

    private int Duration;

    @JsonSkipTag
    private Long ctime;

    @JsonSkipTag
    private Long utime;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCertification() {
        return Certification;
    }

    public void setCertification(String certification) {
        Certification = certification;
    }

    public Byte getUsingType() {
        return UsingType;
    }

    public void setUsingType(Byte usingType) {
        UsingType = usingType;
    }

    public Integer getUsingTimes() {
        return UsingTimes;
    }

    public void setUsingTimes(Integer usingTimes) {
        UsingTimes = usingTimes;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Byte getSex() {
        return Sex;
    }

    public void setSex(Byte sex) {
        Sex = sex;
    }

    public String getFileLanguage() {
        return FileLanguage;
    }

    public void setFileLanguage(String fileLanguage) {
        FileLanguage = fileLanguage;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }
}
