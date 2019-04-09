package com.ctf.traffic.po;

import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 事故原因类别.
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AccidentReasonCategory extends EntityTime{
    /** 分类称 */
    private String name;

    public AccidentReasonCategory(long id) {
        setId(id);
    }
}
