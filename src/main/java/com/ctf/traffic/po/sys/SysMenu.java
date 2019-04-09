package com.ctf.traffic.po.sys;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import com.ctf.traffic.po.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

/**
 * 系统菜单（此表是系统定义好的，没有增删改操作）
 * */
@Entity
@Table(name = SysMenu.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends EntityTime{
    private static final long serialVersionUID = -5967972374593087116L;
    public static final String TABLE_NAME = "sys_menu";
    @ManyToOne
    @JoinColumn(name = "pid")
    @JsonBackReference
    @Where(clause = "state = " + Constant.STATE_ON)
    private SysMenu parent;//上级
    private String name;//菜单名称
    private String url;//路径
    private Boolean leaf;//是否是最终页面
    private Integer sort;//显示顺序
    private String icon;//图标
    private String remark;
}