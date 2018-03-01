package cn.bjzfgcjs.idefense.dao.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

// 声学文件认证表
@Table(name = "zf_acoustic_certificaion")
public class AcousticCert implements Serializable {
    @Id
    private String AcousticFileID;

    @Id
    private String PublisherCode;

    private String PublisherName;

    private String Certification;

    private String CertificaterCode;

    private String CertificaterName;

    private String CertificationContent;

    private Long StratTime;

    private Long PassTime;

    private Long ctime;



}
