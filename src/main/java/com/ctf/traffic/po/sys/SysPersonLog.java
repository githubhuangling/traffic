package com.ctf.traffic.po.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = SysPersonLog.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPersonLog extends EntityTime{
    private static final long serialVersionUID = -1293934962504692030L;
    public static final String TABLE_NAME = "sysperson_log";
    @Column
    private String methodName;
    @Column
    private String className;
    @Column
    private String costTime;

}
