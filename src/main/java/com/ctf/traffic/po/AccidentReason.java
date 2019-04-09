package com.ctf.traffic.po;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;

import org.hibernate.annotations.*;

import com.ctf.traffic.po.response.*;
import com.ctf.traffic.po.sys.*;

import lombok.*;

/**
 * 事故原因.
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AccidentReason extends EntityTime{
    /** 事故原因类别 */
    @ManyToOne(fetch = FetchType.EAGER)
    private AccidentReasonCategory accidentReasonCategory;
    /** 图片位置 */
    private String url;
    /** 分类图片描述 */
    private String description;
    // 提示: 该处的违法行为和条款用于自行协商协议书
    /** 违法行为 */
    private String behavior;
    /** 法律条款 */
    private String clause;
    /** 法律条款 */
    @JoinTable(joinColumns = { @JoinColumn(name = "accident_reason_id") }, inverseJoinColumns = {
            @JoinColumn(name = "clause_id") })
    @OneToMany(fetch = FetchType.EAGER)
    @Where(clause = "state = " + Constant.STATE_ON)
    @OrderBy("update_time")
    private Set<Clause> clauses;
    /** 违法行为 */
    @JoinTable(joinColumns = { @JoinColumn(name = "accident_reason_id") }, inverseJoinColumns = {
            @JoinColumn(name = "illegal_behavior_id") })
    @OneToMany(fetch = FetchType.EAGER)
    @Where(clause = "state = " + Constant.STATE_ON)
    @OrderBy("update_time")
    private Set<IllegalBehavior> illegalBehaviors;

    public AccidentReason(long id) {
        setId(id);
    }


    public static Set<AccidentReasonResponse> toJson(List<AccidentReason> reasons) {
        if (reasons.size() < 1)
            return new HashSet<>();
        Set<AccidentReasonResponse> pics = new HashSet<>();
        reasons.forEach(reason -> pics.add(
                new AccidentReasonResponse(reason.getId(), reason.getAccidentReasonCategory().getName(), null, null)));
        pics.forEach(pic -> reasons.forEach(reason -> {
            if (pic.getName().equals(reason.getAccidentReasonCategory().getName()))
                pic.getPics().add(
                        new AccidentReasonResponse(reason.getId(), null, reason.getUrl(), reason.getDescription()));
        }));
        return pics;
    }
}
