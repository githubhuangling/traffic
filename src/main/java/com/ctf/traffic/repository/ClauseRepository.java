package com.ctf.traffic.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctf.traffic.po.Clause;
import com.ctf.traffic.po.Constant;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
@Repository
public interface ClauseRepository extends JpaRepository<Clause, Long>, JpaSpecificationExecutor<Clause>{
    @Query(value = "select * from clause c where c.id in(select arc.clause_id from accident_reason_clauses arc where arc.accident_reason_id= :reasonId) and state="
            + Constant.STATE_ON, nativeQuery = true)
    Page<Clause> findByAccidentReasonId(@Param("reasonId") long reasonId, Pageable pageable);

    @Query(value = "select * from clause c where c.id not in(select arc.clause_id from accident_reason_clauses arc where arc.accident_reason_id= :reasonId) and clause like %:clause% and  state="
            + Constant.STATE_ON, nativeQuery = true)
    Page<Clause> findExcludeByAccidentReasonId(@Param("reasonId") long reasonId, @Param("clause") String clause,
            Pageable pageable);

    @Query(value = "select * from clause c where c.id not in(select arc.clause_id from accident_reason_clauses arc where arc.accident_reason_id= :reasonId) and  state="
            + Constant.STATE_ON, nativeQuery = true)
    //    Page<Clause> findExcludeByAccidentReasonId(Pageable pageable);
    Page<Clause> findExcludeByAccidentReasonId(@Param("reasonId") long reasonId, Pageable pageable);

    @Query(value = "select * from clause c where c.id in(select arc.clause_id from accident_reason_clauses arc where arc.accident_reason_id= :reasonId) and state="
            + Constant.STATE_ON, nativeQuery = true)
    List<Clause> findByAccidentReasonId(@Param("reasonId") long reasonId);
}
