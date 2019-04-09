package com.ctf.traffic.service;

import com.ctf.traffic.po.*;

/**
 * @author jiangmin
 * @Date 8/7/2018
 * @see
 */
public interface AccidentProcessService {
    CommonResponse start(Long id);
    CommonResponse reprocess(Long id);
}
