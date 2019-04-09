package com.ctf.traffic.po;

import javax.persistence.*;

import com.ctf.traffic.po.sys.*;

import javax.persistence.Entity;
import lombok.*;

/**
 * 法律条款.
 * @author ramer
 * @Date 6/26/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Clause extends EntityTime{
    /** 违法条款 */
    @Column(length = 500)
    private String clause;

}
