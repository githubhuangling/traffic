package com.ctf.traffic.controller.manage;

import javax.annotation.*;

import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.service.*;
import com.ctf.traffic.validator.*;

import io.swagger.annotations.*;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
@RestController
@RequestMapping("/s/substation")
@Api(description = "管理端协调中心接口")
public class SubstationController{
    @Resource
    private SubstationService service;
    @Resource
    private SubstationValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping
    @ApiOperation("检索协调中心")
    public CommonResponse search(@RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findByName(name, page, size));
    }

    @PostMapping
    @ApiOperation("添加协调中心")
    public CommonResponse add(@Validated Substation substation, BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        service.saveOrUpdate(substation);
        boolean result = substation.getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新协调中心")
    public CommonResponse update(@PathVariable("id") long id, @Validated Substation substation,
            @RequestParam(value = "areaIds[]", required = false) long[] areaIds, BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        substation = service.update(substation, areaIds);
        boolean result = substation.getId() > 0;
        return new CommonResponse(result, result ? "更新成功" : "更新失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除协调中心")
    public CommonResponse delete(@PathVariable("id") long id) {
        Substation delete = service.delete(id);
        boolean result = delete.getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

}
