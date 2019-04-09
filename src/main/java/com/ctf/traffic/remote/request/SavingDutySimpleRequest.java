package com.ctf.traffic.remote.request;

import java.util.*;

import lombok.*;

/**
 * 保存事故信息到六合一接口.
 * @author ramer
 * @Date 7/10/2018
 * @see
 */
@Data
public class SavingDutySimpleRequest{
    private DutySimple dutysimple;

    private List<DutysimplehuMan> dutysimplehuMan;

    @Data
    public static class DutySimple{
        /** 登记编号*/
        private String djbh;
        /** 事故编号*/
        private String sgbh;
        /** 文书编号*/
        private String wsbh;
        /** 经办人*/
        private String jbr;
        /** 事故事实*/
        private String sgss;
        /** 行政区号 */
        private String xzqh;
        /** 事故发生时间 */
        private String sgfssj;
        /** 道路代码 */
        private String lh;
        /** 路名 */
        private String lm;
        /** 公里数 */
        private String gls;
        /** 米数 */
        private String ms;
        /** 事故地点 */
        private String sgdd;
        /** 受伤人数 */
        private String ssrs;
        /** 直接财产损失 */
        private String zjccss;
        /** 事故认定原因 */
        private String sgrdyy = "";
        /** 天气 */
        private String tq;
        /** 道路类型 */
        private String dllx;
        /** 公路行政等级 */
        private String glxzdj;
        /** 现场 */
        private String xc = "";
        /** 事故形态 */
        private String sgxt;
        /** 车辆间事故 */
        private String cljsg;
        /** 单车事故 */
        private String dcsg = "";
        /** 调解人 */
        private String tjr;
        /** 所属中队 */
        private String sszd = "";
        /** 档案编号 */
        private String dah = "";
        /** 管理部门 */
        private String glbm;
        /** 电子坐标 */
        private String dzzb = "";
        /** 责任调解结果 */
        private String zrtjjg;
        /** 办案人1 */
        private String jar1;
        /** 办案人2 */
        private String jar2;
        /** 经办人 */
        private String jar;
        /** 结案方式 */
        private String jafs;
        /** 调解方式 */
        private String tjfs;
    }

    @Data
    public static class DutysimplehuMan{
        /** 人员编号，多人编号不能重复 */
        private String rybh;
        /** 姓名 */
        private String xm;
        /** 性别 */
        private String xb;
        /** 身份证号码，多人身份证号不能重复 */
        private String sfzmhm;
        /** 住址*/
        private String zz = "";
        /** 电话 */
        private String dh = "";
        /** 人员类型 */
        private String rylx = "";
        /** 伤害程度 */
        private String shcd = "";
        /** 违法行为代码1 */
        private String wfxw1 = "";
        /** 违法行为代码2 */
        private String wfxw2 = "";
        /** 违法行为代码3 */
        private String wfxw3 = "";
        /** 交通方式 */
        private String jtfs;
        private String dabh = "";
        /** 驾驶证种类 */
        private String jszzl = "";
        private String zjcx = "";
        /** 初次领证日期，非必须，格式一定*/
        private String cclzrq;
        /** 发证机关*/
        private String fzjg = "";
        /** 事故责任 */
        private String sgzr;
        /** 号牌号码，多人号牌号不能重复 */
        private String hphm;
        /** 号牌种类 */
        private String hpzl;
        private String clfzjg = "";
        private String fdjh = "";
        /** 车辆识别代号*/
        private String clsbdh = "";
        private String jdcxh = "";
        /** 车辆品牌*/
        private String clpp = "";
        /** 车辆型号*/
        private String clxh = "";
        /** 车辆颜色*/
        private String csys = "";
        private String cllx = "";
        private String jdczt = "";
        private String syq = "";
        private String syr = "";
        private String clsyxz = "";
        /** 保险公司 */
        private String bxgs = "";
        /** 保险凭证号 */
        private String bxpzh = "";
    }
}
