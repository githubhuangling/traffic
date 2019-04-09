package com.ctf.traffic.service;

import java.util.*;

import org.springframework.data.domain.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
public interface ConductCenterService {
    Page<ConductCenter> findByName(String name, int page, int size);

    ConductCenter delete(long id);

    List<ConductCenter> findAll();

    ConductCenter findById(long id);

    ConductCenter saveOrUpdate(ConductCenter conductCenter);


    ConductCenter findByCode(String code);

}
