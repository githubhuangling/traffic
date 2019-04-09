package com.ctf.traffic.service;

import com.ctf.traffic.po.*;
import java.util.*;

/**
 * @author ramer
 * @Date 7/14/2018
 * @see
 */
public interface RoadSectionCodeService{
    List<RoadSectionCode> findByDldm(String xzqh, String dldm,String ldmc);
}
