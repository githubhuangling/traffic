package com.ctf.traffic.remote.response;

import lombok.*;

/**
 * 六合一接口车辆信息查询返回.
 * @author ramer
 * @Date 7/4/2018
 * @see
 */
@Data
public final class QueryVehicleResponse{
    private Integer total;
    private String status;
    private String statusText;
    private QueryVehicleData data;

    @Data
    public static class QueryVehicleData{
        private String syr;
        private String hpzl;
        private String yxqz;
        private String hphm;
    }
}
