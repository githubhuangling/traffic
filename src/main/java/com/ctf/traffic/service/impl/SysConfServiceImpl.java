package com.ctf.traffic.service.impl;

import javax.annotation.*;
import javax.transaction.*;

import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.repository.sys.*;
import com.ctf.traffic.service.*;

/**
 * @author ramer
 * @Date 6/19/2018
 * @see
 */
@Service
public class SysConfServiceImpl implements SysConfService{
    @Resource
    private SysConfRepository sysConfRepository;

    @Override
    public Page<SysConf> pageByCode(String code, int page, int size) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher(SysConf.PROP_CODE,
                ExampleMatcher.GenericPropertyMatchers.startsWith());
        SysConf sysConf = new SysConf();
        sysConf.setCode(code);
        Example<SysConf> ex = Example.of(sysConf, matcher);
        return sysConfRepository.findAll(ex, PageRequest.of(page - 1, size > 1 ? size : 10));
    }

    @Override
    public SysConf findByCode(String code) {
        Assert.notNull(code, "code can not be null");
        return sysConfRepository.findByCode(code);
    }

    @Transactional
    @Override
    public SysConf update(String code, String value) {
        SysConf sc = new SysConf();
        sc.setCode(code);
        Example<SysConf> ex = Example.of(sc);
        SysConf o = sysConfRepository.findOne(ex).orElse(null);;
        o.setValue(value);
        return sysConfRepository.saveOrUpdate(o);
    }
}
