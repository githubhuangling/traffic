package com.ctf.traffic.po.response;

import com.ctf.traffic.po.response.PoliceResponse.*;
import java.util.*;
import lombok.*;

/**废弃
 * 交警端事故信息响应.
 * @author ramer
 * @Date 7/1/2018
 * @see
 */
@Data

public class AccidentBaseResponse{
    private long id;
    /** 临时事故编号 */
    private String serialNumber;
    private Date occurredTime;
    private String location;
    private String weather;
    /** 交警名称 */
    private String policeName;
    /** 交警签名图片 */
    private String policeSignaturePic;
    /** 交警所属分局 */
    private String policeSubstation;
    /** 事故所属快处中心*/
    private  String conductCenter;
    /**碰撞形态*/
    private String accindetReasonCategory;
    /**事故现场照片*/
    Set<AccidentMediaResponse> accidentMedias;
    /** 事故当事人*/
    List<PoliceAccidentPartResponse> accidentParties;

}
