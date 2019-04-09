package com.ctf.traffic.po;

import com.ctf.traffic.po.AccidentParty.*;
import com.ctf.traffic.po.sys.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import lombok.*;

/**
 * 事故.
 *
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(indexes= {
        @Index(columnList="sys_person_id"),
        @Index(columnList="conduct_center_id"),
        @Index(columnList="createTime")})
public class Accident extends EntityTime{
    /**
     * 临时编号.
     */
    private String serialNumber;

    /**
     * 六合一事故编号.
     */
    private String sgbh;
    /**
     * 事故地点
     */
    private String location;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 关联事故地点
     */
    @ManyToOne
    private JurisdictionArea jurisdictionArea;
    /**
     * 事故发生时间
     */
    private Date occurredTime;
    /**
     * 天气
     */
    private String weather;
    /**
     * 事故原因
     */
    @ManyToOne
    private AccidentReason accidentReason;
    /**
     * 处理方式
     */
    @Column(columnDefinition = "int default -1")
    private Integer processMode = -1;
    /**
     * 处理状态
     */
    @Column(columnDefinition = "int default -1")
    private Integer processStatus = -1;
    /**
     * 排队序号
     */
    private Integer seqNumber;
    /**
     * 交警签名图片
     */
    private String signaturePic;
    /**
     * 协议书图片地址
     */
    private String agreementPicUrl;
    /**
     * 事实及责任
     */
    @Lob
    @Column(columnDefinition="TEXT")
    private String inFactResponsibility;
    /**
     * 调解结果
     */
    @Lob
    @Column(columnDefinition="TEXT")
    private String coordinationResult;
    /**
     * 数据来源
     */
    @Column(columnDefinition = "int default 0")
    private Integer dataSources = 0;
    /**
     * 行政区划
     */
    private String xzqh;
    /**
     * 道路代码
     */
    private String dldm;
    /**
     * 路口路段代码
     */
    private String lddm;
    /**
     * 公里数
     */
    private String gls;
    /**
     * 米数
     */
    private String ms;
    /**
     * 在线认定事故的警官
     */
    @ManyToOne
    private SysPerson sysPerson;
    @ManyToOne
    private Substation substation;
    @ManyToOne
    private ConductCenter conductCenter;

    public Accident(long id) {
        setId(id);
    }

    /**
     * <pre>
     * 数据来源.
     *   0: 默认快处中心
     *   1: 12123
     *   2: 其他
     * </pre>
     */
    public enum DataSources {
        DEFAULT("快处中心"), ONE_TWO_ONE("12123"), OTHER("其他");
        private static Map<Integer, String> indexes;
        private String desc;

        DataSources(String desc) {
            this.desc = desc;
        }

        public static String getDesc(int index) {
            return Optional.ofNullable(indexes).map(descs -> descs.get(index)).orElseGet(() -> {
                indexes = new HashMap<>();
                for (DataSources dataSource : DataSources.class.getEnumConstants()) {
                    indexes.put(dataSource.ordinal(), dataSource.desc);
                }
                return indexes.get(index);
            });
        }
    }

    /**
     * <pre>
     * 待办事故调解状态.
     *   0: 待处理
     *   1: 正在处理
     *   2: 处理完毕
     *   3:当事人不在现场
     * </pre>
     */
    public enum ProcessStatus {
        TO_DEAL("待处理"), DEALING("正在处理"), DEALED("处理完毕"), DEAL_CANCEL("当事人不在现场");
        private static Map<String, Integer> descs;
        private static Map<Integer, String> indexes;
        private String desc;

        ProcessStatus(String desc) {
            this.desc = desc;
        }

        public static Integer getIndex(String name) {
            return Optional.ofNullable(descs).map(descs -> descs.get(name)).orElseGet(() -> {
                descs = new HashMap<>();
                for (Responsibility responsibility : Responsibility.class.getEnumConstants()) {
                    descs.put(responsibility.name(), responsibility.ordinal());
                }
                return descs.get(name);
            });
        }

        public static String getDesc(int index) {
            return Optional.ofNullable(indexes).map(descs -> descs.get(index)).orElseGet(() -> {
                indexes = new HashMap<>();
                for (ProcessStatus processStatus : ProcessStatus.class.getEnumConstants()) {
                    indexes.put(processStatus.ordinal(), processStatus.desc);
                }
                return indexes.get(index);
            });
        }
    }

    /**
     * <pre>
     * 事故处理模式.
     *   0: 自行协商
     *   1: 在线认定
     *   2: 线下处理
     * </pre>
     */
    public enum ProcessMode {
        INDEPENDENT("自行协商"), COORDINATE("在线认定"), OFFLINE("线下处理");
        private static Map<String, Integer> descs;
        private static Map<Integer, String> indexes;
        private String desc;

        ProcessMode(String desc) {
            this.desc = desc;
        }

        public static String getDesc(int index) {
            return Optional.ofNullable(indexes).map(descs -> descs.get(index)).orElseGet(() -> {
                indexes = new HashMap<>();
                for (ProcessMode processMode : ProcessMode.class.getEnumConstants()) {
                    indexes.put(processMode.ordinal(), processMode.desc);
                }
                return indexes.get(index);
            });
        }
    }


    /**
     * 多驾驶证姓名
     * @return
     */
    @Lob
    @Column(columnDefinition="TEXT")
    private String driverNames;
    /**
     * 多驾照号码
     * @return
     */
    @Lob
    @Column(columnDefinition="TEXT")
    private String driverNumbers;
    /**
     * 多车主姓名
     * @return
     */
    @Lob
    @Column(columnDefinition="TEXT")
    private String carOwnerNames;
    /**
     * 多车牌号
     * @return
     */
    @Lob
    @Column(columnDefinition="TEXT")
    private String carMarks;


}
