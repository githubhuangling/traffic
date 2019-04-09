package com.ctf.traffic.remote.response;

import lombok.*;

/**
 * 保存事故信息到六合一接口响应.
 * @author ramer
 * @Date 7/11/2018
 * @see
 */
@Data
public class SavingDutySimpleResponse{
    /** 事故写入id,通过该id查询写入状态 */
    private String id;
    /** 转递状态. 0: 失败 1: 成功*/
    private String zdzt;
}
