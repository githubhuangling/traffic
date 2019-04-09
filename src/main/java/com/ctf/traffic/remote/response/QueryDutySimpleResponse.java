package com.ctf.traffic.remote.response;

import java.util.*;

import lombok.*;

/**
 * 查询六合一事故信息接口返回信息.
 * @author ramer
 * @Date 7/10/2018
 * @see
 */
@Data
public class QueryDutySimpleResponse{
    private Integer total;
    private String statusText;
    private String status;
    private List<QueryDutySimpleData> data;

    @Data
    public static class QueryDutySimpleData{
        /** 登记编号 */
        private String djbh;
        /** 事故编号 */
        private String sgbh;
        /** 行政区划  */
        private String xzqh;
        /** 事故发生时间 */
        private String sgfssj;
        /** 事故地点 */
        private String sgdd;
        /** 天气 */
        private String tq;
        /** 办案单位 */
        private String badw;
        /** 文书编号 */
        private String wsbh;
        /** 事故事实 */
        private String sgss;
        /** 责任调解结果 */
        private String zrtjjg;
        /** 结案人1 */
        private String jar1;
        /** 数据来源 */
        private int sjly;
        private List<DutysimplehuManList> dutysimplehuManList;
    }

    @Data
    public static class DutysimplehuManList{
        /** 保险公司 */
        private String bxgs;
        /** 电话 */
        private String dh;
        /** 号牌号码 */
        private String hphm;
        /** 号牌种类 */
        private String hpzl;
        /** 交通方式 */
        private String jtfs;
        /** 身份证明号码 */
        private String sfzmhm;
        /** 姓名 */
        private String xm;
        /** 保险 */
        private String bx;
        /** 保险凭证号 */
        private String bxpzh;
    }

    /**
     * <pre>
     * 事故处理模式.
     *   0: 自行协商
     *   1: 在线认定
     *   2: 线下处理
     * </pre>
     */
    public enum DataSource {
        INTEGRATED("综合平台"), ONETOONE("12123"), TRAFFIC("快处中心");
        private static Map<Integer, String> indexes;
        private String desc;

        DataSource(String desc) {
            this.desc = desc;
        }

        public static String getDesc(int index) {
            return Optional.ofNullable(indexes).map(descs -> descs.get(index)).orElseGet(() -> {
                indexes = new HashMap<>();
                for (DataSource dataSource : DataSource.class.getEnumConstants()) {
                    indexes.put(dataSource.ordinal(), dataSource.desc);
                }
                return indexes.get(index);
            });
        }
    }

}
