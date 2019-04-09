package com.ctf.traffic.po.sys;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.springframework.format.annotation.*;

import com.ctf.traffic.po.*;

import lombok.*;

/**
 * 系统操作员: 协调警官.
 * */
@Entity
@Table(name = SysPerson.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPerson extends EntityTime{
    private static final long serialVersionUID = 5097587183771973161L;
    public static final String TABLE_NAME = "sys_person";
    public static final String PROP_EMPNO = "emp_no";
    public static final String PROP_PWD = "password";
    /** 可登录 */
    public static final int ACTIVE_TRUE = 1;
    /** 不可登录 */
    public static final int ACTIVE_FALSE = 0;
    /** 员工工号，用于登录,唯一约束 */
    @Column(nullable = false, unique = true)
    private String empNo;
    /** 登录密码 */
    @Column(nullable = false)
    private String password;
    /** 操作员名称 */
    @Column(nullable = false)
    private String name;
    /** 性别 */
    @Column(nullable = false)
    private Integer gender;
    /** 电话号码 */
    @Column(nullable = false, length = 11)
    private String phone;
    /** 头像 */
    private String imageUrl;
    /** 交警签名图片 */
    private String signaturePic;
    /** 是否可登录 */
    @Column(nullable = false, columnDefinition = "tinyint default 1")
    private Integer active;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validDate;
    /** 所属协调中心 */
    @ManyToOne(fetch = FetchType.EAGER)
    @Where(clause = "state = " + Constant.STATE_ON)
    private Substation substation;
    /** 系统角色 */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_person_role", joinColumns = { @JoinColumn(name = "person_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    @Where(clause = "state = " + Constant.STATE_ON)
    private List<SysRole> roles;


}