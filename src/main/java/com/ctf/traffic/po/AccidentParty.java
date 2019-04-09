package com.ctf.traffic.po;

import com.ctf.traffic.po.sys.*;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.*;
import lombok.*;

/***
 * 当事人.
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AccidentParty extends EntityTime{
    /** 事故方 */
    private String name;
    /** 当时人索引 0,1 */
    private Integer accidentIndex;
    /** 事故信息 */
    @ManyToOne
    private Accident accident;
    /** 保险公司报案号 */
    private String insuranceReportNumber;
    /** 联系电话 */
    private String phone;
    /** 事故责任 */
    private Integer responsibility;
    /** 违法行为 */
    private String illegalBehavior;
    /** 法律条款 */
    private String clause;
    /** 签名图片位置 */
    private String signaturePic;
    /**碰撞形态（=事故原因类型，在线认定时，各当事人碰撞形态不同，所有各自保存）*/
    @ManyToOne
    private AccidentReasonCategory accidentReasonCategory;

    public AccidentParty(long id) {
        setId(id);
    }

    /**
     * <pre>
     * 事故责任分类.
     *   0: 全部责任
     *   1: 同等责任
     *   2: 无责任
     * </pre>
     */
    public enum Responsibility {
        ALL("全部责任"), EQUAL("同等责任"), NONE("无责任");
        private static Map<Integer, String> descs;
        private String desc;

        Responsibility(String desc) {
            this.desc = desc;
        }

        public static String getDesc(Integer index) {
            return Optional.ofNullable(descs).map(descs -> descs.get(index)).orElseGet(() -> {
                descs = new HashMap<>();
                for (Responsibility responsibility : Responsibility.class.getEnumConstants()) {
                    descs.put(responsibility.ordinal(), responsibility.desc);
                }
                return descs.get(index);
            });
        }

    }

    /**
     * <pre>
     * 当事人索引: 甲乙丙丁.
     * </pre>
     */
    public enum AccidentIndex {
        NONE("无"), FIRST("甲"), SECOND("乙"), THIRD("丙"), FORTH("丁"), FIFTH("戊"), SIXTH("己"), SEVENTH("庚"), EIGHTH(
                "辛"), NINTH("壬"), TENTH("癸");
        private static Map<Integer, String> descs;
        private String desc;

        AccidentIndex(String desc) {
            this.desc = desc;
        }

        public static String getDesc(Integer index) {
            return Optional.ofNullable(descs).map(descs -> descs.get(index)).orElseGet(() -> {
                descs = new HashMap<>();
                for (AccidentIndex accidentIndex : AccidentIndex.class.getEnumConstants()) {
                    descs.put(accidentIndex.ordinal(), accidentIndex.desc);
                }
                return descs.get(index);
            });
        }

    }


}
