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

@Service
public class SysRoleServiceImpl implements SysRoleService{

    @Resource
    private SysMenuRepository menuRepository;

    @Resource
    private SysRoleRepository roleRepository;

    @Transactional
    @Override
    public void add(SysRole o, String menuIds) {
        List<SysMenu> menus = new ArrayList<>();
        if (StringUtils.isNotEmpty(menuIds)) {
            menuIds = null;
            o.setMenus(menus);
        } else {
            String[] menuIdStrs = menuIds.split(",");
            for (String id : menuIdStrs) {
                menus.add(menuRepository.findById(Long.parseLong(id)).orElse(null));
                o.setMenus(menus);
            }
        }
        roleRepository.saveOrUpdate(o);
    }

    @Override
    public Page<SysRole> findAll(int page, int size) {
        return roleRepository.pages(new SysRole(), page, (page == 0 || size == 0) ? 10000 : size, true);
    }

    @Transactional
    @Override
    public SysRole update(SysRole sysRole, String menuIds) {
        Optional<SysRole> opl = roleRepository.findById(sysRole.getId());
        SysRole role = opl.orElse(null);;
        if (StringUtils.isNotEmpty(sysRole.getName())) {
            role.setName(sysRole.getName().trim());
        }
        if (StringUtils.isNotEmpty(sysRole.getRemark())) {
            role.setRemark(sysRole.getRemark().trim());
        }
        List<SysMenu> menus = new ArrayList<>();
        if (StringUtils.isNotEmpty(menuIds)) {
            String[] menuIdStrs = menuIds.split(",");
            for (String id : menuIdStrs) {
                menus.add(menuRepository.findById(Long.parseLong(id)).orElse(null));
                role.setMenus(menus);
            }
        } else {
            role.setMenus(null);
        }
        return roleRepository.saveOrUpdate(role);
    }

    @Override
    public List<Long> getMenuIds(SysRole sysRole) {
        List<Long> menuids = new ArrayList<Long>();
        for (int i = 0; i < sysRole.getMenus().size(); i++) {
            menuids.add(sysRole.getMenus().get(i).getId());
        }
        return menuids;
    }

    @Override
    public SysRole findById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

}
