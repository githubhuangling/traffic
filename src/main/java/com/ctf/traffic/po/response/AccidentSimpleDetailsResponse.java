package com.ctf.traffic.po.response;

import lombok.*;

/**
 *事故详情条目展示
 */
@Data
public class AccidentSimpleDetailsResponse {
    private Long accidentId;
    private String serialNumber;
    private String occurredTime;
    private String parties;
    private String carNumbers;
    private String conductCenterName;
    private String processMode;
    private String processStatus;
    private String processSysperson;
    private String createTime;

}
