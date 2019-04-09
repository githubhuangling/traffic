package com.ctf.traffic.controller.manage;

import javax.annotation.*;

import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.service.*;
import com.ctf.traffic.util.*;
import com.ctf.traffic.util.PathUtil.*;
import com.ctf.traffic.validator.*;

import io.swagger.annotations.*;
import lombok.extern.slf4j.*;

/**
 * @author ramer
 * @Date 6/28/2018
 */
@RestController
@RequestMapping("/s/accidentReason")
@Api(description = "管理端事故原因接口")
@Slf4j
public class AccidentReasonController{
    @Resource
    private AccidentReasonService service;
    @Resource
    private ClauseService clauseService;
    @Resource
    private IllegalBehaviorService illegalBehaviorService;
    @Resource
    private AccidentReasonValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping("/getReasonByCategoryId")
    @ApiOperation("根据事故原因分类获取事故原因")
    public CommonResponse getReasonByCategoryId(@RequestParam("categoryId") long categoryId,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, service.findByCateIdAndDescription(categoryId, desc, page, size));
    }

    @PostMapping
    @ApiOperation("添加事故原因")
    public CommonResponse add(@Validated AccidentReason accidentReason, @RequestParam("categoryId") long categoryId,
            BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        accidentReason.setAccidentReasonCategory(new AccidentReasonCategory(categoryId));
        service.saveOrUpdate(accidentReason);
        boolean result = accidentReason.getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新事故原因")
    public CommonResponse update(@PathVariable("id") long id, AccidentReason reason,
            @ApiParam(value = "arr[]") @RequestParam(value = "behaviorIds[]", required = false) long[] behaviorIds,
            @ApiParam(value = "arr[]") @RequestParam(value = "clauseIds[]", required = false) long[] clauseIds) {
        AccidentReason accidentReason = service.update(reason, clauseIds, behaviorIds);
        boolean result = accidentReason.getId() > 0;
        return new CommonResponse(result, result ? "更新成功" : "更新失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除事故原因")
    public CommonResponse delete(@PathVariable("id") long id) {
        AccidentReason delete = service.delete(id);
        boolean result = delete.getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

    @PostMapping("/upload/uploadFile")
    @ApiOperation("管理端通用事故文件上传")
    public CommonResponse uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "name", required = false) String name) {
        String path = ImageUtil.save(file, name, SavingFolder.SYSPERSON);
        return new CommonResponse(path != null, path);
    }
}
