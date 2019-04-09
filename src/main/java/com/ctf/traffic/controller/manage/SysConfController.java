package com.ctf.traffic.controller.manage;

import javax.annotation.*;

import org.springframework.web.bind.annotation.*;

import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.service.*;

import io.swagger.annotations.*;
import lombok.extern.slf4j.*;

@RestController
@RequestMapping("/s/sysconf")
@Slf4j
@Api(description = "管理端系统参数接口")
public class SysConfController{
    @Resource
    private SysConfService sysConfService;

    /**
     * 系统参数.
     * */
    @GetMapping("/person/getSysconf")
    String list(@RequestParam("code") String code, @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        code = null;
        return new RtnJson(true, "查询成功", sysConfService.pageByCode(code, page, size),
                new String[] { "pageable", "sort" }).toString();
    }

    /**
     * 根据code更新
     * */
    @PutMapping("/person/updateSysconf")
    String update(@RequestParam("code") String code, @RequestParam("value") String value) {
        log.info(" SysConfController.update : [{},{}]", code, value);
        Long id = sysConfService.update(code, value).getId();
        return new RtnJson(id > 0, id > 0 ? "更新成功" : "更新失败").toString();
    }

}
