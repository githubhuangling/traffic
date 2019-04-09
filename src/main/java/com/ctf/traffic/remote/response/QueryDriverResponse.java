package com.ctf.traffic.remote.response;

import lombok.*;

/**
 * 六合一接口驾驶证查询返回.
 * @author ramer
 * @Date 7/4/2018
 * @see
 */
@Data
public final class QueryDriverResponse{
    private Integer total;
    private String status;
    private String statusText;
    private QueryDriverData data;

    @Data
    public static class QueryDriverData{
        private String zt;
        private String xm;
        private String yxqz;
        private String sfzmhm;
    }
}
