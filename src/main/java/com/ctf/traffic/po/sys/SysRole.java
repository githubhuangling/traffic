package com.ctf.traffic.po.sys;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import com.ctf.traffic.po.*;

import lombok.*;

/**
 * 系统操作员角色
 * */
@Entity
@Table(name = SysRole.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRole extends EntityTime{
    private static final long serialVersionUID = -4727445367422150381L;
    public static final String TABLE_NAME = "sys_role";
    public static final String PROP_NAME = "name";
    private String name;//角色名称
    private String remark;//说明
    @Where(clause = "state = " + Constant.STATE_ON)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_menu", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
            @JoinColumn(name = "menu_id") })
    @OrderBy(value = "id")
    private List<SysMenu> menus;
}