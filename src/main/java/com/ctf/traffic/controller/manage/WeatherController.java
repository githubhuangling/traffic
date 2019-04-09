package com.ctf.traffic.controller.manage;

import com.ctf.traffic.po.CommonResponse;
import com.ctf.traffic.po.Weather;
import com.ctf.traffic.service.WeatherService;
import com.ctf.traffic.validator.WeatherValidator;
import io.swagger.annotations.*;
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
@RequestMapping("/s/weather")
@Api(description = "管理端天气接口")
public class WeatherController{
    @Resource
    private WeatherService weatherService;
    @Resource
    private WeatherValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping
    @ApiOperation("检索天气")
    public CommonResponse search(@RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return new CommonResponse(true, weatherService.findByName(name, page, size));
    }

    @PostMapping
    @ApiOperation("添加天气")
    public CommonResponse add(@ApiParam(value = "{name:'不能为空,长度不能大于10',iconUrl:'不能为空'}") @Validated Weather weather,
            BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        weatherService.saveOrUpdate(weather);
        boolean result = weather.getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新天气")
    public CommonResponse update(@PathVariable("id") long id,
            @ApiParam(value = "{name:'不能为空,长度不能大于10',iconUrl:'不能为空'}") @Validated Weather weather,
            BindingResult errMsg) {
        if (errMsg.hasErrors()) {
            StringBuilder sb = new StringBuilder("提交信息有误:\n");
            errMsg.getAllErrors().stream().iterator()
                    .forEachRemaining(error -> sb.append(error.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, sb.toString());
        }
        weather.setId(id);
        weatherService.saveOrUpdate(weather);
        boolean result = weather.getId() > 0;
        return new CommonResponse(result, result ? "更新成功" : "更新失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除天气")
    public CommonResponse delete(@PathVariable("id") long id) {
        Weather delete = weatherService.delete(id);
        boolean result = delete.getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

}
