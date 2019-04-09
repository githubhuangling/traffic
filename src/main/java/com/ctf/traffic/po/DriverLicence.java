package com.ctf.traffic.po;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;

import com.ctf.traffic.po.Constant.*;
import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 驾驶证信息.
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class DriverLicence extends EntityTime{
    /** 当事人*/
    @OneToOne
    private AccidentParty accidentParty;
    /** 是否在六合一系统验证通过(输入或查询) */
    @Column(columnDefinition="bit default 1")
    private Boolean notcustom = true;
    /** 驾驶证图片 */
    private String picUrl;
    /** 证件号码 */
    private String number;
    /** 姓名 */
    private String name;
    /** 性别 */
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    /** 国籍 */
    private String country;
    /** 地址 */
    private String address;
    /** 联系电话 */
    private String phone;
    /*** 初次领证日期 */
    private Date firstGet;
    /** 有效期限从 */
    private Date validDateFrom;
    /** 有效期限至 */
    private Date validDateTo;
    /** 档案编号 */
    private String fileNumber;
    /** 出生日期 */
    private Date birthDate;

}
