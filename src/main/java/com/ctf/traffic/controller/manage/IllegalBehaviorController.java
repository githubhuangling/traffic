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
@RequestMapping("/s/illegalBehavior")
@Api(description = "管理端违法行为接口")
public class IllegalBehaviorController{
    @Resource
    private IllegalBehaviorService service;
    @Resource
    private IllegalBehaviorValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping
    @ApiOperation("检索违法行为")
    public CommonResponse search(@RequestParam(value = "behavior", required = false) String behavior,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findByBehavior(behavior, page, size));
    }

    @PostMapping
    @ApiOperation("添加违法行为")
    public CommonResponse add(@Validated IllegalBehavior illegalBehavior, BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        service.saveOrUpdate(illegalBehavior);
        boolean result = illegalBehavior.getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新违法行为")
    public CommonResponse update(@PathVariable("id") long id, @Validated IllegalBehavior illegalBehavior,
            BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        illegalBehavior.setId(id);
        service.saveOrUpdate(illegalBehavior);
        boolean result = illegalBehavior.getId() > 0;
        return new CommonResponse(result, result ? "更新成功" : "更新失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除违法行为")
    public CommonResponse delete(@PathVariable("id") long id) {
        IllegalBehavior delete = service.delete(id);
        boolean result = delete.getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

    @GetMapping("/byAccidentReason/{reasonId}")
    @ApiOperation("根据事故原因获取违法行为")
    public CommonResponse pageByAccidentReason(@PathVariable("reasonId") long reasonId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findByAccidentReasonId(reasonId, page, size));
    }

    @GetMapping("/excludeByAccidentReason/{reasonId}")
    @ApiOperation("根据事故原因获取违法行为")
    public CommonResponse pageExcludeByAccidentReason(@PathVariable("reasonId") long reasonId,
            @RequestParam(value = "behavior", required = false) String behavior,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findExcludeByAccidentReasonId(reasonId, behavior, page, size));
    }
}
