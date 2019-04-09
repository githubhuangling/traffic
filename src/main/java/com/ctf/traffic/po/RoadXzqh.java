package com.ctf.traffic.po;

import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 行政区划.
 * @author ramer
 * @Date 7/15/2018
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class RoadXzqh extends EntityTime{
    private String xzqh;
    private String name;
}
