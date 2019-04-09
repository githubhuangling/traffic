package com.ctf.traffic.po;

import javax.persistence.*;
import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 违法行为.
 * @author ramer
 * @Date 6/25/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class IllegalBehavior extends EntityTime{
    /** 违法行为 */
    @Column(length = 500)
    private String behavior;

}
