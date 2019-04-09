package com.ctf.traffic.service;

import java.util.*;

import org.springframework.data.domain.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.po.response.*;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
public interface AccidentReasonService{

    Page<AccidentReason> findByCateIdAndDescription(long cateId, String description, int page, int size);

    List<AccidentReasonResponse> findByCategoryId(long categoryId);

    AccidentReason findById(long id);

    AccidentReason delete(long id);

    AccidentReason saveOrUpdate(AccidentReason reason);

    AccidentReason update(AccidentReason reason, long[] clauseIds, long[] behaviorIds);

}
