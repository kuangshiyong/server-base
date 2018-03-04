package cn.bjzfgcjs.idefense.dao.domain;

import cn.bjzfgcjs.idefense.common.annotation.JsonSkipTag;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

// 声学文件认证表
@Table(name = "zf_acoustic_certificaion")
public class AcousticCert implements Serializable {
    @Id
    @Column(name = "AcousticFileID")
    private String AcousticFileID;

    @Id
    @Column(name = "PublisherCode")
    private String PublisherCode;

    @Column(name = "PublisherName")
    private String PublisherName;

    private String Certification;

    @Column(name = "CertificaterCode")
    private String CertificaterCode;

    @Column(name = "CertificaterName")
    private String CertificaterName;

    @Column(name = "CertificationContent")
    private String CertificationContent;

    @Column(name = "StratTime")
    private Long StratTime;

    @Column(name = "PassTime")
    private Long PassTime;

    @JsonSkipTag
    private Long ctime;

    public String getAcousticFileID() {
        return AcousticFileID;
    }

    public void setAcousticFileID(String acousticFileID) {
        AcousticFileID = acousticFileID;
    }

    public String getPublisherCode() {
        return PublisherCode;
    }

    public void setPublisherCode(String publisherCode) {
        PublisherCode = publisherCode;
    }

    public String getPublisherName() {
        return PublisherName;
    }

    public void setPublisherName(String publisherName) {
        PublisherName = publisherName;
    }

    public String getCertification() {
        return Certification;
    }

    public void setCertification(String certification) {
        Certification = certification;
    }

    public String getCertificaterCode() {
        return CertificaterCode;
    }

    public void setCertificaterCode(String certificaterCode) {
        CertificaterCode = certificaterCode;
    }

    public String getCertificaterName() {
        return CertificaterName;
    }

    public void setCertificaterName(String certificaterName) {
        CertificaterName = certificaterName;
    }

    public String getCertificationContent() {
        return CertificationContent;
    }

    public void setCertificationContent(String certificationContent) {
        CertificationContent = certificationContent;
    }

    public Long getStratTime() {
        return StratTime;
    }

    public void setStratTime(Long stratTime) {
        StratTime = stratTime;
    }

    public Long getPassTime() {
        return PassTime;
    }

    public void setPassTime(Long passTime) {
        PassTime = passTime;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }
}
