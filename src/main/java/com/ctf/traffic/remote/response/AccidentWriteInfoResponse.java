package com.ctf.traffic.remote.response;

import lombok.*;

/**
 * 查询六合一接口事故信息写入状态响应.
 * @author ramer
 * @Date 7/10/2018
 * @see
 */
@Data
public class AccidentWriteInfoResponse{
    private String status;
    private String statusText;
    private AccidentWriteInfoData data;

    @Data
    public static class AccidentWriteInfoData{
        /** 写入信息 */
        private String msg;
        /** 登记编号 */
        private String djbh;
        /** 转递状态 */
        private String zdzt;
        /** 写入状态 */
        private String xrzt;
        /** 事故编号  */
        private String sgbh;
    }
}
