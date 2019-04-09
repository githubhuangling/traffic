package com.ctf.traffic.controller.customer;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ctf.traffic.po.CommonResponse;
import com.ctf.traffic.po.ConductCenter;
import com.ctf.traffic.service.AccidentMediaService;
import com.ctf.traffic.service.ConductCenterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 6/26/2018
 * @see
 */
@Slf4j
@Controller("customerController")
@RequestMapping("/customer")
@Api(description = "事故基本信息接口")
public class CommonController {
    @Resource
    private ConductCenterService conductCenterService;
    @Resource
    private AccidentMediaService accidentMediaService;

    @GetMapping("/interval")
    @ApiOperation("心跳")
    public void interval(HttpSession session) {
        log.debug("=========interval-conductCenterId：{}",session.getAttribute("conductCenterId"));
    }
    
    @GetMapping("/index")
    @ApiOperation("事故入口")
    public String redirectInstructions(HttpSession session) {
        session.removeAttribute("accident");
        session.removeAttribute("conductCenterId");
        return "index";
    }

    @GetMapping("/mInfoUpload")
    public String mobileUpload(@RequestParam("asdf") long accidentId, Map<String, Object> map) {
        final String uuid = UUID.randomUUID().toString();
        accidentMediaService.initUploadMediaM(accidentId, uuid);
        map.put("asdf", accidentId);
        map.put("uuid", uuid);
        return "customer/m_info_upload";
    }

    @GetMapping("/validConductCenter")
    @ApiOperation("校验硬盘号")
    @ResponseBody
    public CommonResponse validConductCenter(@RequestParam("code") String code, HttpSession session) {
        ConductCenter conductCenter = conductCenterService.findByCode(code);
        boolean valid = conductCenter != null;
        if (valid)
            session.setAttribute("conductCenterId", conductCenter.getId());
        return new CommonResponse(valid, valid ? "校验成功" : "使用本系统需要权限,请联系管理员添加");
    }
}
