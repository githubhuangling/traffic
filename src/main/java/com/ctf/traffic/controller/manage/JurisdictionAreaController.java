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
@RequestMapping("/s/jurisdictionArea")
@Api(description = "管理端管辖区域接口")
public class JurisdictionAreaController{
    @Resource
    private JurisdictionAreaService service;
    @Resource
    private JurisdictionAreaValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping
    @ApiOperation("检索管辖区域")
    public CommonResponse search(@RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findByName(name, page, size));
    }

    @PostMapping
    @ApiOperation("添加管辖区域")
    public CommonResponse add(@Validated JurisdictionArea jurisdictionArea, @RequestParam("centerId") long centerId,
            BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        jurisdictionArea.setSubstation(new Substation(centerId));
        service.saveOrUpdate(jurisdictionArea);
        boolean result = jurisdictionArea.getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新管辖区域")
    public CommonResponse update(@PathVariable("id") long id, @Validated JurisdictionArea jurisdictionArea,
            BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        jurisdictionArea.setId(id);
        service.saveOrUpdate(jurisdictionArea);
        boolean result = jurisdictionArea.getId() > 0;
        return new CommonResponse(result, result ? "更新成功" : "更新失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除管辖区域")
    public CommonResponse delete(@PathVariable("id") long id) {
        JurisdictionArea delete = service.delete(id);
        boolean result = delete.getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

    @GetMapping("/bySubstation/{substationId}")
    @ApiOperation("根据快处中心获取管辖区域")
    public CommonResponse pageBySubstation(@PathVariable("substationId") long substationId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findByCenterId(substationId, page, size));
    }

    @GetMapping("/excludeBySubstation/{substationId}")
    @ApiOperation("根据快处中心获取未包含管辖区域")
    public CommonResponse pageExcludeBySubstation(@PathVariable("substationId") long substationId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findExcludeByCenterId(name, page, size));
    }

}
