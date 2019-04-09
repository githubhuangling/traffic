package com.ctf.traffic.controller.manage;

import com.ctf.traffic.po.*;
import com.ctf.traffic.service.*;
import io.swagger.annotations.*;
import javax.annotation.*;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiangmin
 * @Date 2018/8/7
 * @see
 */
@RestController
@RequestMapping("/s/acccidentProcess")
@Api(description = "管理端事故处理接口")
public class AccidentProcessController {
    @Resource
    private AccidentProcessService accidentProcessService;

    @PutMapping("/start{id}")
    @ApiOperation("开始处理事故")
    public CommonResponse startProcess(@PathVariable("id") long id){

        return accidentProcessService.start(id);
    }

    @PutMapping("/reprocess{id}")
    @ApiOperation("重新处理事故")
    public CommonResponse reprocess(@PathVariable("id") long id){

        return accidentProcessService.reprocess(id);
    }
}
