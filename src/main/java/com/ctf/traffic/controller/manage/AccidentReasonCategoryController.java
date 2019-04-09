package com.ctf.traffic.controller.manage;

import com.ctf.traffic.po.AccidentReasonCategory;
import com.ctf.traffic.po.CommonResponse;
import com.ctf.traffic.service.AccidentReasonCategoryService;
import com.ctf.traffic.validator.AccidentReasonCategoryValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ramer
 * @Date 6/28/2018
 */
@RestController
@RequestMapping("/s/reasonCategory")
@Api(description = "管理端事故原因分类接口")
public class AccidentReasonCategoryController{
    @Resource
    private AccidentReasonCategoryService service;
    @Resource
    private AccidentReasonCategoryValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping
    @ApiOperation("检索事故原因分类")
    public CommonResponse search(@RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findByName(name, page, size));
    }

    @PostMapping
    @ApiOperation("添加事故原因分类")
    public CommonResponse add(@Validated AccidentReasonCategory reasonCategory, BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        service.saveOrUpdate(reasonCategory);
        boolean result = reasonCategory.getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新事故原因分类")
    public CommonResponse update(@PathVariable("id") long id, @Validated AccidentReasonCategory reasonCategory,
            BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        reasonCategory.setId(id);
        service.saveOrUpdate(reasonCategory);
        boolean result = reasonCategory.getId() > 0;
        return new CommonResponse(result, result ? "更新成功" : "更新失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除事故原因分类")
    public CommonResponse delete(@PathVariable("id") long id) {
        AccidentReasonCategory delete = service.delete(id);
        boolean result = delete.getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

}
