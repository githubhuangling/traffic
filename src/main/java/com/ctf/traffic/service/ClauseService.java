package com.ctf.traffic.service;

import java.util.*;

import org.springframework.data.domain.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
public interface ClauseService{
    Page<Clause> findByClause(String name, int page, int size);

    Clause findById(long id);

    Clause delete(long id);

    Clause saveOrUpdate(Clause clause);

    Page<Clause> findByAccidentReasonId(long id, int page, int size);

    List<Clause> findByAccidentReasonId(long id);

    Page<Clause> findExcludeByAccidentReasonId(long id, String clause, int page, int size);

    Set<Clause> newInstances(long[] ids);
}
