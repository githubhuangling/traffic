package com.ctf.traffic.service;

import com.ctf.traffic.po.sys.SysDictionary;
import com.ctf.traffic.po.sys.SysDictionaryCate;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author ramer
 * @Date 6/19/2018
 * @see
 */
public interface SysDictionaryService{
    /**
     * 查询所有系统字典分类.
     * @return
     */
    List<SysDictionaryCate> listCate();

    /**
     * 通过code查询系统字典.
     * @param cateCode
     * @param page
     * @param size
     * @return
     */
    Page<SysDictionary> listDicByCode(String cateCode, int page, int size);

    /**
     * 添加系统字典.
     * @param name
     * @param remark
     * @param cateCode
     * @return
     */
    SysDictionary addSysDictionary(String name, String remark, String cateCode);

    /**
     * 更新系统字典.
     * @param name
     * @param remark
     * @param dicId
     * @return
     */
    SysDictionary updateSysDictionary(String name, String remark, String dicId);

    /**
     * 物理删除系统字典.
     * @param dicId
     */
    void deleteSysDictionary(String dicId);
}
