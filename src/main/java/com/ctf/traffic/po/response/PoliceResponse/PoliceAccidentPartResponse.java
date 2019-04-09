package com.ctf.traffic.po.response.PoliceResponse;

import lombok.*;

/**
 * @author jiangmin
 * @Date 2018/8/29
 * @see
 */
@Data
public class PoliceAccidentPartResponse {
    private Long id;
    private String name;
    private String accidentIndex;
    private  String illegalBehavior;
    private String clause;
    private PoliceDriverResponse driver;
    private PoliceDrivingResponse driving;

}
