package com.ctf.traffic.po.response;

import java.util.*;

import com.ctf.traffic.po.*;

import lombok.*;

/**
 * @author ramer
 * @Date 7/2/2018
 * @see
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccidentPartyResponse{
    private Long id;
    private String name;
    private String accidentIndex;
    /** 保险公司 */
    private String insuranceCompany;
    /** 交强险凭证号 */
    private String insuranceCompulsory;
    /** 保险期间 */
    private Date insuranceDate;
    private DrivingLicence drivingLicence;
    private DriverLicence driverLicence;
}
