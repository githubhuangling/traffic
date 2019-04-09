package com.ctf.traffic.po;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;

import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 事故认定书.
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class AccidentResponsibility extends EntityTime{
    /** 事故 */
    @OneToOne
    private Accident accident;
    /** 交警: 系统管理员 */
    private SysPerson police;
    /** 类型 */
    private PaperType paperType;

    /** 事实及责任 */
    @Column(columnDefinition = "text")
    private String factResponsibility;
    /** 调解结果 */
    @Column(columnDefinition = "text")
    private String result;
    /** 处理时间 */
    private Date processTime;

    /**
     * <pre>
     *  纸质协议类型.
     *   0: 全部责任
     *   1: 同等责任
     *   2: 无责任
     * </pre>
     */
    public enum PaperType {
        PROTOCOL("协议书"), CERTIFICATE("认定书");
        private String desc;

        PaperType(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return this.desc;
        }
    }
}
