package com.ctf.traffic.controller.manage;

import com.alibaba.fastjson.*;
import com.ctf.traffic.po.*;
import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.service.*;
import io.swagger.annotations.*;
import javax.annotation.*;
import lombok.extern.slf4j.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/s/conductCenter")
@Slf4j
@Api(description = "管理端快处中心管理")
public class ConductCenterController {
    @Resource
    private ConductCenterService conductCenterService;

    /**
     * 获取所有快处中心.
     */
    @GetMapping("/getConductCenter")
    String findAll(@RequestParam("page") Integer page,
                   @RequestParam("size") Integer size) {
        return new RtnJson(true, "查询成功", conductCenterService.findByName(null, 1,999),
                new String[]{"pageable", "sort"}).toString();
    }

    @GetMapping("/listDeviceByConductCenterId")
    public CommonResponse listDeviceByConductCenterId(@RequestParam("conductCenterId") long conductCenterId) {
        ConductCenter conductCenter = conductCenterService.findById(conductCenterId);
        log.info(" ConductCenterController.listDeviceByConductCenterId : [{}]", conductCenter.getCode());
        JSONObject jsonObject = new JSONObject();
        if (conductCenter != null
                && !StringUtils.isEmpty(conductCenter.getCode())) {
            String hdSerial = conductCenter.getCode();
            log.info(" ConductCenterController.listDeviceByConductCenterId : hdSerial[{}]", hdSerial);
            String[] hdSerials = hdSerial.split(",", -1);
            log.info(" ConductCenterController.listDeviceByConductCenterId : [{},{}]", hdSerials, hdSerials.length);
            JSONArray jsonArray = new JSONArray();
            for (int i = 1; i < hdSerials.length - 1; i++) {
                JSONObject contentJson = new JSONObject();
                contentJson.put("name", hdSerials[i]);
                jsonArray.add(contentJson);
                log.info(" ConductCenterController.listDeviceByConductCenterId : [{},{}]", hdSerials[i], jsonArray.size());
            }
            jsonObject.put("first", true);
            jsonObject.put("last", true);
            jsonObject.put("number", 0);
            jsonObject.put("size", 9999);
            jsonObject.put("totalElements", jsonArray.size());
            jsonObject.put("numberOfElements", jsonArray.size());
            jsonObject.put("content", jsonArray);
        } else {
            jsonObject.put("first", true);
            jsonObject.put("last", true);
            jsonObject.put("number", 0);
            jsonObject.put("size", 9999);
            jsonObject.put("totalElements", 0);
            jsonObject.put("numberOfElements", 0);
            jsonObject.put("content", new JSONArray());
        }
        return new CommonResponse(true, jsonObject);
    }

    @PostMapping
    public CommonResponse addConductCenter(ConductCenter conductCenter) {
        conductCenter = conductCenterService.saveOrUpdate(conductCenter);
        boolean result = conductCenter != null && conductCenter.getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @DeleteMapping("/{id}")
    public CommonResponse deleteConductCenter(@PathVariable("id") long id) {
        ConductCenter conductCenter = conductCenterService.delete(id);
        boolean result = conductCenter != null && conductCenter.getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

    @PutMapping("/{id}")
    public CommonResponse updateConductCenter(@PathVariable("id") long id, ConductCenter conductCenter) {
        ConductCenter center = conductCenterService.findById(id);
        if (center == null)
            return new CommonResponse(false, "快处中心不存在");
        if (!StringUtils.isEmpty(conductCenter.getName())) {
            center.setName(conductCenter.getName());
        }
        center = conductCenterService.saveOrUpdate(center);
        boolean result = center.getId() > 0;
        return new CommonResponse(result, result ? "更新成功" : "更新失败");
    }

    @PostMapping("/addDevice")
    @ApiOperation("快处中心添加设备")
    public CommonResponse addDevice(@RequestParam("newcode") String newcode, @RequestParam("conductCenterId") long conductCenterId) {
        ConductCenter conductCenter = conductCenterService.findById(conductCenterId);
        if (conductCenter == null ||newcode.contains(",")) {
            return new CommonResponse(false, "添加失败");
        }
        StringBuilder code =null;
        if(conductCenter.getCode()==null){
            code=new StringBuilder();
            code.append(",");
            code.append(newcode+",");
        }else{
            code = new StringBuilder(conductCenter.getCode());
            code.append(newcode+",");
        }
        if (code.length() > 0)
            conductCenter.setCode(code.toString());
        boolean result = conductCenterService.saveOrUpdate(conductCenter).getId() > 0;
        return new CommonResponse(result, result ? "添加成功" : "添加失败");
    }

    @PostMapping("/deleteDevice")
    @ApiOperation("快处中心删除设备")
    public CommonResponse deleteDevice(@RequestParam("deletecode") String deletecode, @RequestParam("conductCenterId") long conductCenterId) {
        ConductCenter conductCenter = conductCenterService.findById(conductCenterId);
        if (conductCenter == null ||deletecode.contains(",")) {
            return new CommonResponse(false, "删除失败");
        }
        String code=conductCenter.getCode();
        String newcode=code.replaceAll(","+deletecode,"");

        if (code.length() > 0)
            if (newcode.length()==1){
                conductCenter.setCode(null);
            }else {
                conductCenter.setCode(newcode);
            }

        boolean result = conductCenterService.saveOrUpdate(conductCenter).getId() > 0;
        return new CommonResponse(result, result ? "删除成功" : "删除失败");
    }

}
