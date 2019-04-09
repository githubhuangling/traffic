package com.ctf.traffic.po;

import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 道路路口路段代码.
 * @author ramer
 * @Date 7/14/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class RoadSectionCode extends EntityTime{
    /** 道路代码 */
    private String dldm;
    /** 行政区划 */
    private String xzqh;
    /** 路口路段代码 */
    private String lddm;
    /** 路口路段名称 */
    private String ldmc;
}
