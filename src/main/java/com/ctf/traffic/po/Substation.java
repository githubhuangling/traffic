package com.ctf.traffic.po;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.ctf.traffic.po.sys.EntityTime;

import lombok.*;

/**
 * 交通分局.
 * @author ramer
 * @Date 6/22/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Substation extends EntityTime{
    /** 名称 */
    private String name;
    /** 管辖的封闭区间 */
    @Column(columnDefinition = "text")
    private String roundArea;
    //    /** 管辖区域 */
    //    @OneToMany(mappedBy = "substation")
    //    @Where(clause = "state = " + Constant.STATE_ON)
    //    private Set<JurisdictionArea> jurisdictionAreas;
    /** 多个协调警官 */
    //    @OneToMany(mappedBy = "substation")
    //    @Where(clause = "state = " + Constant.STATE_ON)
    //    @JsonIgnore
    //    private Set<JurisdictionArea> jurisdictionAreas;

    public Substation(long id) {
        setId(id);
    }

}
