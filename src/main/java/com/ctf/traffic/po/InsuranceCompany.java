package com.ctf.traffic.po;

import javax.persistence.*;
import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 保险公司
 * @Date 10/7/2018
 * @see
 */
@Entity
@Data
@Table
@EqualsAndHashCode(callSuper = true)
public class InsuranceCompany extends EntityTime{
	
    public static final String PROP_SORT = "sort";
	
    /** 保险公司名称 */
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String code;
    private String sort;
    /** 备注 */
    private String remark;
}
