package com.ctf.traffic.service;

import java.util.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
public interface RoadCodeService{
    List<RoadCode> findByXzqh(String xzqh);

    List<RoadCode> findByDldm(String dldm);
}
