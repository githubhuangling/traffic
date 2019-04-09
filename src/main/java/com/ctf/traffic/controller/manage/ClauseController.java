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
 */
@RestController
@RequestMapping("/s/clause")
@Api(description = "管理端法律条款接口")
public class ClauseController{
    @Resource
    private ClauseService service;
    @Resource
    private ClauseValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping
    @ApiOperation("检索法律条款")
    public CommonResponse search(@RequestParam(value = "clause", required = false) String clause,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findByClause(clause, page, size));
    }

    @PostMapping
    @ApiParam("添加法律条款")
    public CommonResponse add(@Validated Clause clause, BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        service.saveOrUpdate(clause);
        boolean result = clause.getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新法律条款")
    public CommonResponse update(@PathVariable("id") long id, @Validated Clause clause, BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        clause.setId(id);
        service.saveOrUpdate(clause);
        boolean result = clause.getId() > 0;
        return new CommonResponse(result, result ? "更新成功" : "更新失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除法律条款")
    public CommonResponse delete(@PathVariable("id") long id) {
        Clause delete = service.delete(id);
        boolean result = delete.getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

    @GetMapping("/byAccidentReason/{reasonId}")
    @ApiOperation("根据事故原因获取法律条款")
    public CommonResponse pageByAccidentReason(@PathVariable("reasonId") long reasonId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findByAccidentReasonId(reasonId, page, size));
    }

    @GetMapping("/excludeByAccidentReason/{reasonId}")
    @ApiOperation("根据事故原因获取未包含的法律条款")
    public CommonResponse pageExcludeByAccidentReason(@PathVariable("reasonId") long reasonId,
            @RequestParam(value = "clause", required = false) String clause,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findExcludeByAccidentReasonId(reasonId, clause, page, size));
    }

}
