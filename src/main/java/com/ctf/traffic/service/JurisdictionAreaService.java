package com.ctf.traffic.service;

import java.util.*;

import org.springframework.data.domain.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.po.response.*;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
public interface JurisdictionAreaService{
    JurisdictionAreaResponse getJsonTree();

    Page<JurisdictionArea> findByName(String name, int page, int size);

    JurisdictionArea findById(long id);

    JurisdictionArea delete(long id);

    JurisdictionArea saveOrUpdate(JurisdictionArea area);

    Page<JurisdictionArea> findByCenterId(long centerId, int page, int size);

    List<JurisdictionArea> findByCenterId(long centerId);

    Page<JurisdictionArea> findExcludeByCenterId(String name, int page, int size);

    Set<JurisdictionArea> newInstances(long[] ids);


    List<JurisdictionArea> findByParent(Long pid);
}
