package com.ctf.traffic.po.response;

import java.util.*;
import lombok.*;

/**
 *事故详情展示
 */
@Data
public class AccidentDetailsResponse {
    /**
     * 临时编号.
     */
    private String serialNumber;

    /**
     * 六合一事故编号.
     */
    private String sgbh;

    /**
     * 事故地点
     */
    private String location;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;

    /**
     * 关联事故地点
     */
//    private String jurisdictionArea;

    /**
     * 事故发生时间
     */
    private String occurredTime;

    /**
     * 天气
     */
    private String weather;

    /**
     * 事故原因
     */
    private String accidentReason;

    /**
     * 处理方式
     */
    private String  processMode;

    /**
     * 处理状态
     */
    private String  processStatus;

    /**
     * 排队序号
     */
    private String  seqNumber;

    /**
     * 交警签名图片
     */
    private String signaturePic;

    /**
     * 协议书/认定书图片地址
     */
    private String agreementPicUrl;

    /**
     * 事实及责任
     */
    private String inFactResponsibility;

    /**
     * 调解结果
     */
    private String coordinationResult;


    /**
     * 行政区划
     */
    private String xzqh;
    /**
     * 道路代码
     */
    private String dldm;

    /**
     * 路口路段代码
     */
    private String lddm;
    /**
     * 公里数
     */
    private String gls;
    /**
     * 米数
     */
    private String ms;
    /**
     * 在线认定事故的警官
     */
    private String sysPerson;

    /**
     * 事故所属快处中心
     */
    private String conductCenter;

    private List<AccidentPartyDetailsResponse> parties;

   private Set<AccidentMediaResponse> accidentMediaResponses;
}
