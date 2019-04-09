package com.ctf.traffic.po;

import javax.persistence.*;
import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * @author ramer
 * @Date 6/29/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class ThirdPartyCertificate extends EntityTime{
    @Column(nullable = false, length = 50)
    private String code;
    @Column(nullable = false)
    private String secret;

}
