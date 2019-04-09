package com.ctf.traffic.po.response;

import java.util.*;

import lombok.*;

/**
 * 自行协商协议书响应.
 * @author ramer
 * @Date 7/4/2018
 * @see
 */
@Data
public class AgreementOwnResponse{
    private Long id;
    /** 时间 */
    private Date time;
    /** 地点 */
    private String location;
    /** 天气 */
    private String weather;
    private String serialNumber;
    /** 事实及责任 */
    private String inFactResponsibility;
    /** 调解结果 */
    private String coordinationResult;
    private List<AgreementOwnParty> accidentParties;
    private String accidentReason;
    private String police;

    @Data
    /** 当事人 */
    public static class AgreementOwnParty{
        private Long id;
        /** 姓名 */
        private String name;
        private String signaturePic;
        private String accidentIndex;
        /** 驾驶证号 */
        private String driverNumber;
        /** 联系方式 */
        private String phone;
        /** 号牌号码 */
        private String drivingNumber;
        /** 车辆类型 */
        private String carType;
        /** 保险公司 */
        private String insuranceCompany;
        /** 交强险凭证号 */
        private String insuranceCompulsory;
        /** 报案号 */
        private String caseNumber;
        /** 保险期间 */
        private Date insuranceDate;
        /** 责任 */
        private String responsibility;
        /** 法律条款 */
        private String clause;
        /** 违法行为 */
        private String illegalBehavior;
        /** 车损部位 */
        private String brokenParts;
        /** 事故情形 */
        private String accidentReason;
    }
}
