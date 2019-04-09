package com.ctf.traffic.service;

import java.util.*;
import org.springframework.data.domain.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
/*管理端事故原因分类接口*/
public interface AccidentReasonCategoryService{
    Page<AccidentReasonCategory> findByName(String name, int page, int size);

    List<AccidentReasonCategory> findAll();

    AccidentReasonCategory findById(long id);

    AccidentReasonCategory delete(long id);

    AccidentReasonCategory saveOrUpdate(AccidentReasonCategory category);
}
