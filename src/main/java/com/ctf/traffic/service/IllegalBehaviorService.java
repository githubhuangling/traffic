package com.ctf.traffic.service;

import java.util.*;

import org.springframework.data.domain.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
public interface IllegalBehaviorService{
    Page<IllegalBehavior> findByBehavior(String behavior, int page, int size);

    IllegalBehavior findById(long id);

    IllegalBehavior delete(long id);

    IllegalBehavior saveOrUpdate(IllegalBehavior illegalBehavior);

    Page<IllegalBehavior> findByAccidentReasonId(long id, int page, int size);

    List<IllegalBehavior> findByAccidentReasonId(long id);

    Page<IllegalBehavior> findExcludeByAccidentReasonId(long id, String behavior, int page, int size);

    Set<IllegalBehavior> newInstances(long[] ids);

}
