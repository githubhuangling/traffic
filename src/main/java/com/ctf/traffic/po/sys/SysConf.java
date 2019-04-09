package com.ctf.traffic.po.sys;

import javax.persistence.*;
import javax.persistence.Entity;

import lombok.*;

/**
 * 系统参数（此表是系统定义好的，没有增删操作）
 * */
@Entity
@Table(name = SysConf.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConf extends EntityTime{
    private static final long serialVersionUID = -4407165921852297529L;
    public static final String TABLE_NAME = "sys_conf";
    public static final String PROP_CODE = "code";
    /** 阅读须知 */
    public static final String INSTRUCTION = "SYS_INSTRUCTION";
    /* 参数编号 */
    @Column(nullable = false)
    private String code;
    /*参数名称*/
    @Column(nullable = false)
    private String name;
    /*参数值*/
    @Column(nullable = false, columnDefinition = "text")
    private String value;
    private String remark;
}
