package com.ctf.traffic.remote.response;

import lombok.*;

/**
 * 保单系统返回的保单信息.
 * @author ramer
 * @Date 7/4/2018
 * @see
 */
@Data
public class InsuranceInfoResponse{
    /** 保险公司名称 */
    private String name;
    /** 保险公司代码 */
    private String code;
    /** 有效期限 */
    private String endDate;
    /** 保险凭证号 */
    private String policyNo;

}
