package com.ctf.traffic.po.response.PoliceResponse;

import lombok.*;

/**
 * @author jiangmin
 * @Date 2018/8/29
 * @see
 */
@Data
public class PoliceDriverResponse {
    private Boolean findbySixToOne;
    private String name;
    private String idNumber;
    private String phone;
    private String picUrl;
}
