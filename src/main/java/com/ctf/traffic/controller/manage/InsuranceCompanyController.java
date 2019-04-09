package com.ctf.traffic.controller.manage;

import com.ctf.traffic.po.*;
import com.ctf.traffic.service.*;
import io.swagger.annotations.*;
import javax.annotation.*;
import org.springframework.web.bind.annotation.*;

/**
 * @author ramer
 * @Date 6/28/2018
 */
@RestController
@RequestMapping("/s/insuranceCompany")
@Api(description = "管理保险公司接口")
public class InsuranceCompanyController {
    @Resource
    private InsuranceCompanyService insuranceCompanyService;

    @GetMapping
    @ApiOperation("检索保险公司")
    public CommonResponse search(@RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, insuranceCompanyService.findByName(name, page, size));
    }

    @PostMapping
    @ApiOperation("添加保险公司")
    public CommonResponse add(@ApiParam(value = "{name:'name不能为空',code:'code不能为空'}") InsuranceCompany insuranceCompany) {
        insuranceCompanyService.saveOrUpdate(insuranceCompany);
        boolean result = insuranceCompany.getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新保险公司")
    public CommonResponse update(@PathVariable("id") long id,
            @ApiParam(value = "{name:'不能为空,长度不能大于10'}")  InsuranceCompany insuranceCompany) {
        insuranceCompany.setId(id);
        insuranceCompanyService.saveOrUpdate(insuranceCompany);
        boolean result = insuranceCompany.getId() > 0;
        return new CommonResponse(result, result ? "更新成功" : "更新失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除保险公司")
    public CommonResponse delete(@PathVariable("id") long id) {
        InsuranceCompany delete = insuranceCompanyService.delete(id);
        boolean result = delete.getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

}
