package com.ctf.traffic.po;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 车辆保单.
 * @author RAMER
 * @Date 20/06/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class InsuranceCar extends EntityTime{
    /** 行驶证 */
    @OneToOne
    private DrivingLicence drivingLicence;
    /** 保险公司*/
    private String insuranceCompany;
    /** 保险公司code */
    private String insuranceCompanyCode;
    /** 保险凭证号 */
    private String insuranceCompulsory;
    /** 报案号 */
    private String caseNumber;
    /** 保险期限期间 */
    private Date insuranceDate;
}
