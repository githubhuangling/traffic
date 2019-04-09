package com.ctf.traffic.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctf.traffic.po.Constant;
import com.ctf.traffic.po.IllegalBehavior;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
@Repository
public interface IllegalBehaviorRepository
        extends JpaRepository<IllegalBehavior, Long>, JpaSpecificationExecutor<IllegalBehavior>{
    @Query(value = "select * from illegal_behavior ib where ib.id in(select arib.illegal_behavior_id from accident_reason_illegal_behaviors arib where arib.accident_reason_id= :reasonId) and state="
            + Constant.STATE_ON, nativeQuery = true)
    Page<IllegalBehavior> findByAccidentReasonId(@Param("reasonId") long id, Pageable pageable);

    @Query(value = "select * from illegal_behavior ib where ib.id not in(select arib.illegal_behavior_id from accident_reason_illegal_behaviors arib where arib.accident_reason_id= :reasonId) and state="
            + Constant.STATE_ON, nativeQuery = true)
    //    Page<IllegalBehavior> findExcludeByAccidentReasonId(Pageable pageable);
    Page<IllegalBehavior> findExcludeByAccidentReasonId(@Param("reasonId") long id, Pageable pageable);

    @Query(value = "select * from illegal_behavior ib where ib.id not in(select arib.illegal_behavior_id from accident_reason_illegal_behaviors arib where arib.accident_reason_id= :reasonId) and ib.behavior like %:behavior% and state="
            + Constant.STATE_ON, nativeQuery = true)
    Page<IllegalBehavior> findExcludeByAccidentReasonId(@Param("reasonId") long id, @Param("behavior") String behavior,
            Pageable pageable);

    @Query(value = "select * from illegal_behavior ib where ib.id in(select arib.illegal_behavior_id from accident_reason_illegal_behaviors arib where arib.accident_reason_id= :reasonId) and state="
            + Constant.STATE_ON, nativeQuery = true)
    List<IllegalBehavior> findByAccidentReasonId(@Param("reasonId") long id);
}
