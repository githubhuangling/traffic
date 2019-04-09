package com.ctf.traffic.po;

import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 道路代码.
 * @author ramer
 * @Date 7/14/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class RoadCode extends EntityTime{
    /** 道路代码 */
    private String dldm;
    /** 行政区划 */
    private String xzqh;
    /** 道路名称 */
    private String dlmc;
    /** 道路类型 */
    private String dllx;
    /** 公路行政等级 */
    private String glxzdj;
    /** 起始里程 */
    private String qs;
    /** 结束里程 */
    private String js;
    /** 辖区里程 */
    private String xqlc;
}
