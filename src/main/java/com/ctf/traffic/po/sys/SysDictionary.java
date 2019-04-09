package com.ctf.traffic.po.sys;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import com.ctf.traffic.po.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

/**
 * 系统数据字典
 * */
@Entity
@Table(name = SysDictionary.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SysDictionary extends EntityTime{
    private static final long serialVersionUID = -7897612630067419119L;
    public static final String TABLE_NAME = "sys_dictionary";
    public static final String PROP_CODE = "code";
    @Where(clause = "state = " + Constant.STATE_ON)
    @ManyToOne
    @JoinColumn(name = "cate_id")
    @JsonBackReference
    private SysDictionaryCate cate;//所属分类
    private String name;//字典名称
    private String remark;

    public SysDictionary(Long id) {
        setId(id);
    }
}
