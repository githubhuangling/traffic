package com.ctf.traffic.controller.thirdparty;

import com.alibaba.fastjson.*;
import com.ctf.traffic.auth.annotation.*;
import com.ctf.traffic.po.*;
import com.ctf.traffic.remote.*;
import com.ctf.traffic.service.*;
import io.swagger.annotations.*;
import javax.annotation.*;
import lombok.extern.slf4j.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;

/**
 * @author ramer
 * @Date 7/1/2018
 * @see
 */
@RestController
@RequestMapping("/thirdparty/insurance")
@Api(description = "保单信息接口")
@Slf4j
public class InsuranceController{
    @Resource
    private AccidentService accidentService;

    @PostMapping("/accidentInfo")
    @RequiredAuthentication
    @ApiOperation("保险公司根据车牌号、车辆类型查询最近一个月的事故信息")
    public CommonResponse saveAgreementPic(@RequestParam(value = "data", required = true) String data) {
        log.warn(" TrafficPoliceController.saveAgreementPic : [{}]", RemoteInvoke.base64Decode(data));
        JSONObject dataObj = JSON.parseObject(RemoteInvoke.base64Decode(data));
        String carMark=dataObj.getString("carMark");
        String carType=dataObj.getString("carType");
        String code=dataObj.getString("code");
        if(StringUtils.isEmpty(carMark)||StringUtils.isEmpty(carType)||StringUtils.isEmpty(code)){
            return new CommonResponse(false,"缺少参数");
        }
        return   accidentService.insuranceGetAccidentDetailsInOneMonth(carMark, carType, code);
    }
}
