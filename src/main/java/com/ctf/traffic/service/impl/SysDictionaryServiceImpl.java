package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.*;
import javax.transaction.*;

import org.apache.commons.lang.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.repository.sys.*;
import com.ctf.traffic.service.*;

/**
 * @author ramer
 * @Date 6/19/2018
 * @see
 */
@Service
public class SysDictionaryServiceImpl implements SysDictionaryService{

    @Resource
    private SysDictionaryCateRepository dictionaryCateRepository;
    @Resource
    private SysDictionaryRepository dictionaryRepository;

    @Override
    public List<SysDictionaryCate> listCate() {
        SysDictionaryCate sc = new SysDictionaryCate();
        sc.setState(1);
        Example<SysDictionaryCate> example = Example.of(sc);
        return dictionaryCateRepository.findAll(example);
    }

    @Override
    public Page<SysDictionary> listDicByCode(String cateCode, int page, int size) {
        SysDictionaryCate dictionaryCate = new SysDictionaryCate();
        dictionaryCate.setCode(cateCode);
        Example<SysDictionaryCate> cateEx = Example.of(dictionaryCate);
        SysDictionaryCate sdc = dictionaryCateRepository.findOne(cateEx).orElse(null);
        SysDictionary sd = new SysDictionary();
        sd.setCate(sdc);
        sd.setState(1);
        return dictionaryRepository.pages(sd, page, size, true);
    }

    @Transactional
    @Override
    public SysDictionary addSysDictionary(String name, String remark, String cateCode) {
        SysDictionary dic = new SysDictionary();
        SysDictionaryCate sc = new SysDictionaryCate();
        sc.setCode(cateCode);
        Example<SysDictionaryCate> example = Example.of(sc);
        dic.setName(name);
        dic.setRemark(remark);
        dic.setCate(dictionaryCateRepository.findOne(example).orElse(null));
        return dictionaryRepository.saveOrUpdate(dic);
    }

    @Transactional
    @Override
    public SysDictionary updateSysDictionary(String name, String remark, String dicId) {
        SysDictionary dic = new SysDictionary();
        dic.setId(Long.parseLong(dicId));
        Example<SysDictionary> example = Example.of(dic);
        SysDictionary thisDic = dictionaryRepository.findOne(example).orElse(null);
        if (StringUtils.isNotEmpty(name)) {
            thisDic.setName(name);
        }
        if (StringUtils.isNotEmpty(remark)) {
            thisDic.setRemark(remark);
        }
        return dictionaryRepository.saveOrUpdate(thisDic);
    }

    @Transactional
    @Override
    public void deleteSysDictionary(String dicId) {
        dictionaryRepository.deleteById(Long.parseLong(dicId));
    }
}
