package com.ctf.traffic.po;

import javax.persistence.*;
import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 三方系统请求日志.
 * @author ramer
 * @Date 7/1/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class ThirdPartyLog extends EntityTime{
    @ManyToOne
    @JoinColumn(referencedColumnName = "code")
    private ThirdPartyCertificate thirdPartyCertificate;
    /** 请求地址 */
    private String url;
    /** 请求IP */
    private String ip;
    /** 请求结果 */
    @Column(columnDefinition = "text")
    private String result;
}
