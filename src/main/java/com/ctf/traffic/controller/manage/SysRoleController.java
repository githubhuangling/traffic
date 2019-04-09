package com.ctf.traffic.controller.manage;

import javax.annotation.*;

import org.springframework.web.bind.annotation.*;

import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.repository.sys.*;
import com.ctf.traffic.service.*;

import io.swagger.annotations.*;
import lombok.extern.slf4j.*;

@RestController
@RequestMapping("/s/sysrole")
@Slf4j
@Api(description = "管理端系统角色接口")
public class SysRoleController{
    @Resource
    private SysRoleService roleService;

    @Resource
    private SysMenuRepository menuRepository;

    @Resource
    private SysRoleRepository roleRepository;

    /**
     * 添加角色类型
     * @param o
     * @param menuIds
     * @return
     */
    @RequestMapping("/person/add")
    String add(SysRole o, @RequestParam("menuIds") String menuIds) {
        try {
            roleService.add(o, menuIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new RtnJson(true, "保存成功").toString();
    }

    /**
     * 修改角色类型
     * @param o
     * @param menuIds
     * @return
     */
    @RequestMapping("/person/update")
    String update(SysRole o, @RequestParam("menuIds") String menuIds) {
        SysRole sysRole = roleService.update(o, menuIds);
        return sysRole.getId() > 0 ? new RtnJson(true, "保存成功").toString() : new RtnJson(false, "保存失败").toString();
    }

    /**
     * 查询所有角色类型
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/person/findAll")
    String findAll(@RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        if (page == null || size == null) {
            return new RtnJson(true, "查询所有角色成功", roleService.findAll(0, 0).getContent()).toString();
        }
        return new RtnJson(true, "查询所有用户成功", roleService.findAll(page, size), new String[] { "pageable", "sort" })
                .toString();
    }

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    @RequestMapping("/person/findById")
    String findById(@RequestParam("id") Long id) {
        SysRole sysRole = roleService.findById(id);
        return new RtnJson(true, "查询成功", sysRole).put("menuids", roleService.getMenuIds(sysRole)).toString();
    }

    /**
     * 根据id删除角色
     * @param toDeleteRoleId
     * @return
     */
    @RequestMapping("/person/deleteById")
    String delete(@RequestParam("toDeleteRoleId") String toDeleteRoleId) {
        Long id = Long.parseLong(toDeleteRoleId);
        roleRepository.deleteById(id);
        return new RtnJson(true, "删除成功").toString();
    }

    /**
     * 查询角色菜单
     * @return
     */
    @RequestMapping("/person/findAllMenu")
    String findAllMenu() {
        return new RtnJson(true, "菜单查询成功", menuRepository.pages(new SysMenu(), null, 10000, null).getContent())
                .toString();
    }

}
