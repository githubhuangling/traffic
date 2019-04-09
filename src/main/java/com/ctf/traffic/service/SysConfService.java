package com.ctf.traffic.service;

import org.springframework.data.domain.*;

import com.ctf.traffic.po.sys.*;

/**
 * @author ramer
 * @Date 6/19/2018
 * @see
 */
public interface SysConfService{
    Page<SysConf> pageByCode(String code, int page, int size);

    SysConf findByCode(String code);

    SysConf update(String code, String value);
}
