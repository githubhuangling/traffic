package com.ctf.traffic.po.response;

import lombok.*;

/**
 * @author jiangmin
 * @Date 2018/8/28
 * @see
 */
@Data
@NoArgsConstructor
public class AccidentPartyDetailsResponse {

    /****************  当事人 *******************/
    /**
     * 当事人姓名
     */
    private String name;
    /**
     * 当事人索引
     */
    private String accidentIndex;
    /**
     * 对应事故Id
     */
    private Long accident;
    /**
     * 报案号
     */
    private String reportNumber;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 事故责任
     */
    private String responsibility;
    /**
     * 违法行为
     */
    private String illegalBehavior;
    /**
     * 法律条款
     */
    private String clause;
    /**
     * 签名图片位置
     */
    private String signaturePic;


    /******************   驾驶证   *******************/

    /**
     * 驾驶证是否在六合一验证通过
     */
    private Boolean driverNotcustom;
    /**
     * 驾驶证图片
     */
    private String driverPicUrl;
    /**
     * 驾驶证号
     */
    private String driverNumber;
    /**
     * 驾驶证姓名
     */
    private String driverName;
    /**
     * 驾驶证性别
     */
    //private String driverGender;
    /**
     * 驾驶证国籍
     */
    private String driverCountry;
    /**
     *驾驶证家庭地址
     */
    private String driverAddress;
    /**
     * 驾驶证联系电话
     */
    private String driverPhone;
    /**
     * 驾驶证初次领证日期
     */
    private String driverFirstGet;
    /**
     * 驾驶证有效期从
     */
    private String driverValiDateFrom;
    /**
     * 驾驶证有效期至
     */
    private String driverValiDateTo;
    /**
     * 驾驶证档案编号
     */
    private String driverFileNumber;
    /**
     * 驾驶证驾驶员出生日期
     */
    private String driverBirthDate;
/************   车辆行驶证信息  ***************/
    /**
     *行驶证是否在六合一验证通过
     */
    private Boolean drivingNotcustom;
    /** 行驶证图片 */
    private String drivingPicUrl;
    /** 车辆号码 */
    private String drivingNumber;
    /** 车辆类型 */
    private String drivingType;
    /** 所有人 */
    private String drivingName;
    /** 地址 */
    private String drivingAddress;
    /** 联系电话 */
    private String drivingPhone;
    /** 使用性质 */
    private String drivingFunctional;
    /** 品牌型号 */
    private String drivingBrand;
    /** 车辆识别代号 */
    private String drivingIdentifyCode;
    /** 发动机型号 */
    private String drivingTransmitterNumber;
    /** 注册日期 */
    private String drivingRegisterDate;
    /** 发证日期 */
    private String drivingIssueDate;
    /** 档案编号 */
    private String drivingFileNumber;
    /** 检验有效期 */
    private String drivingInspectExpired;


    /******************  车辆保单 *****************/
    /** 保险公司*/
    private String insuranceCompany;
    /** 保险公司code */
    private String insuranceCompanyCode;
    /** 保险凭证号 */
    private String insuranceCompulsory;
    /** 报案号 */
    private String caseNumber;
    /** 保险期限期间 */
    private String insuranceDate;
}
