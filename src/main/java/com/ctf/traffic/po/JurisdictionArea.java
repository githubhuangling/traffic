package com.ctf.traffic.po;

import javax.persistence.*;
import javax.persistence.Entity;

import org.hibernate.annotations.*;

import com.ctf.traffic.po.sys.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

/**
 * 管辖区域.
 * @author ramer
 * @Date 6/22/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(exclude = { "substation", "parent" })
public class JurisdictionArea extends EntityTime{
    /** 名称 */
    private String name;
    /** 区域级别: 1. 市 2. 区 3. 街道 */
    private int level;
    /** 行政区号 */
    private String szqh;
    /** 道路代码 */
    private String lh;
    /** 路名 */
    private String lm;
    /** 公里数 */
    private String gls;
    /** 米数 */
    private String ms;
    /** 道路类型 */
    private String dllx;
    /** 上级 */
    @ManyToOne
    @Where(clause = "state = " + Constant.STATE_ON)
    private JurisdictionArea parent;
    /** 协调中心*/
    @ManyToOne
    @Where(clause = "state = " + Constant.STATE_ON)
    @JsonIgnore
    private Substation substation;

    public JurisdictionArea(long id) {
        setId(id);
    }

}
