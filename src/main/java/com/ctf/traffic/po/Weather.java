package com.ctf.traffic.po;

import javax.persistence.*;
import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 天气.
 * @author ramer
 * @Date 6/26/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Weather extends EntityTime{
    /** 天气 */
    @Column(unique = true)
    private String name;
    /** 图标 */
    private String iconUrl;
}
