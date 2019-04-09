package com.ctf.traffic.po;

import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 快处中心.
 * @author ramer
 * @Date 7/13/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ConductCenter extends EntityTime{
    /** 快处中心名称 */
    private  String name;
    /** 硬盘号集合 */
    private String code;
    /** 分隔符 */
    public static String SPLIT_CHARACTER = ",";

    public ConductCenter(Long id) {
        setId(id);
    }

}
