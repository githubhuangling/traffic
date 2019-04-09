package com.ctf.traffic.po.sys;

import javax.persistence.*;
import javax.persistence.Entity;

import lombok.*;

/**
 * 系统数据字典分类（此表是系统定义好的，没有增删改操作）
 * */
@Entity
@Table(name = SysDictionaryCate.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictionaryCate extends EntityTime{
    private static final long serialVersionUID = -5785795748177436460L;
    public static final String TABLE_NAME = "sys_dictionary_cate";
    public static final String PROP_CODE = "code";
    /* 分类编号 */
    private String code;
    /* 分类名 */
    private String name;
    /* 说明 */
    private String remark;
}
