package com.ctf.traffic.service;

import java.util.*;

import org.springframework.data.domain.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
public interface SubstationService{
    Page<Substation> findByName(String name, int page, int size);

    List<Substation> findAll();

    Substation findById(long id);

    Substation delete(long id);

    Substation saveOrUpdate(Substation substation);

    Substation update(Substation substation, long[] ids);
}
