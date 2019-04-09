package com.ctf.traffic.service;

import com.ctf.traffic.po.sys.SysRole;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SysRoleService{
    void add(SysRole o, String menuIds) throws Exception;

    Page<SysRole> findAll(int page, int size);

    SysRole update(SysRole sysRole, String menuIds);

    List<Long> getMenuIds(SysRole sysRole);

    SysRole findById(long id);

}
