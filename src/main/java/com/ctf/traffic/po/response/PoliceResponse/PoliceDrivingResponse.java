package com.ctf.traffic.po.response.PoliceResponse;

import lombok.*;

/**
 * @author jiangmin
 * @Date 2018/8/29
 * @see
 */
@Data
public class PoliceDrivingResponse {
    private Boolean findbySixToOne;
    private String picUrl;
    private String carNumber;
    private String type;
    private String carOwner;
    private String insuranceCompany;
    private String insuranceCompulsory;
    private String insuranceDate;
}
