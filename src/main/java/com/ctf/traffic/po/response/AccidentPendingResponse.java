package com.ctf.traffic.po.response;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 待调解事故/查询事故信息.
 * @author ramer
 * @Date 7/3/2018
 * @see
 */
@Data
public class AccidentPendingResponse{
    private long id;
    private String idString;
    /*传导; 引导; 带领; 控制; */
    private Long conductCenterId;
    /** 发生时间 */
    private Date occurredTime;
    /** 地点 */
    private String location;
    /** 数据来源*/
    private String dataSources;
    /** 处理模式 */
    private String processMode;
    /** 处理状态 */
    private String processStatus;
    /** 序号 */
    private Integer seqNumber;
    /** 申请时间 */
    private Date time;
    /** 当事人 */
    private List<String> accidentParties;
    private String accidentPartyStr;
    /** 事故车辆 */
    private List<String> drivingNumber;
    private String drivingNumberStr;
    /** 快处中心 */
    private String conductCenter;
    /** 处理人Id*/
    private String sysPersonId;
    /** 当前处理人姓名*/
    private String sysPersonName;
    /** 是否可重新排号 */
    private boolean refreshSeqNumber;
}
