package com.ctf.traffic.po;

import javax.persistence.*;
import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 行驶证信息.
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class DrivingLicence extends EntityTime{
    /** 事件信息 */
    @OneToOne
    private AccidentParty accidentParty;
    /** 是否在六合一系统验证通过(输入或查询) */
    @Column(columnDefinition="bit default 1")
    private Boolean notcustom = true;
    /** 行驶证图片 */
    private String picUrl;
    /** 车辆号码 */
    private String number;
    /** 车辆类型 */
    private String type;
    /** 所有人 */
    private String name;
    /** 地址 */
    private String address;
    /** 联系电话 */
    private String phone;
    /** 使用性质 */
    private String functional;
    /** 品牌型号 */
    private String brand;
    /** 车辆识别代号 */
    private String identifyCode;
    /** 发动机型号 */
    private String transmitterNumber;
    /** 注册日期 */
    private String registerDate;
    /** 发证日期 */
    private String issueDate;
    /** 档案编号 */
    private String fileNumber;
    /** 检验有效期 */
    private String inspectExpired;

    public DrivingLicence(long id) {
        setId(id);
    }

    /**
     * <pre>
     * 小车类型.
     *   0: 小车
     *   1: 卡车
     * </pre>
     */
    public enum CarType {
        SMALL("小车"), TRUCK("卡车");
        private String desc;

        CarType(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return this.desc;
        }
    }

}
