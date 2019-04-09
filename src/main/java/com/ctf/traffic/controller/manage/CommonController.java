package com.ctf.traffic.controller.manage;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ctf.traffic.po.CommonResponse;
import com.ctf.traffic.service.AccidentService;
import com.ctf.traffic.util.ImageUtil;
import com.ctf.traffic.util.PathUtil.SavingFolder;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author RAMER
 * @Date 30/06/2018
 * @see
 */
@RestController("manageController")
@RequestMapping("/manage")
@Slf4j
public class CommonController{
    @Resource
    private AccidentService accidentService;

    @PostMapping("/upload/uploadFile")
    @ApiOperation("管理端通用文件上传")
    public CommonResponse uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "name", required = false) String name) {
        String path = ImageUtil.save(file, null, SavingFolder.SYSPERSON);
        return new CommonResponse(path != null, path);
    }

    @GetMapping("/listPendingCoordinationAccident")
    @Transactional
    @ApiOperation("管理端获取待调解事故信息")
    public CommonResponse listPendingCoordinationAccident() {
        return new CommonResponse(true, accidentService.listPendingAccident());
    }

    @GetMapping("/listMyPendedAccident")
    @Transactional
    @ApiOperation("管理端获取当前用户已处理的事故信息")
    public CommonResponse listMyPendedAccident(
            @RequestParam(value = "date") String date,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size, HttpServletRequest request) throws Exception {

        return new CommonResponse(true, accidentService.listMyPendedAccident(1L,date,page,size));
    }

    @GetMapping("/getDataStatistics")
    @Transactional
    @ApiOperation("获取当日事故处理报表")
    public CommonResponse getDataStatistics(@RequestParam(value = "sdate",required = true) String sdate,@RequestParam(value = "edate",required = true) String edate) {
        return accidentService.getDatastatistics(sdate,edate);
    }

    @GetMapping("/getAccidentDetails")
    @ApiOperation("管理端获取事故详情列表")
    public CommonResponse getAccidentDetails(@RequestParam(value = "nodata",required = false)boolean nodata,
                                              @RequestParam(value = "sDate",required = false)String sDate,
                                             @RequestParam(value = "eDate",required = false)String eDate,
                                             @RequestParam(value = "carNumber",required = false)String carNumber,
                                             @RequestParam(value = "partyIdNumber",required = false)String partyIdNumber,
                                             @RequestParam(value = "conductCenterId",required = false)Long conductCenterId,
                                             @RequestParam(value = "processMode",required = false)Integer processMode,
                                             @RequestParam(value = "processStatus",required = false)Integer processStatus,
                                             @RequestParam(value = "accidentSyspersonId",required = false)Long accidentSyspersonId){
        log.info(" CommonController.getAccidentDetails : [{}]", Arrays.asList(sDate,eDate,carNumber,partyIdNumber,conductCenterId,processMode,processStatus,accidentSyspersonId));
        return  accidentService.getAccidentDetails(sDate,eDate,carNumber,partyIdNumber,conductCenterId,accidentSyspersonId,processMode,processStatus);
    }

    @GetMapping("/getAccidentDetailsById")
    @ApiOperation("管理端获取事故详情列表")
    public CommonResponse getAccidentDetailsById(@RequestParam(value = "id",required = false)Long id){
            return  accidentService.getAccidentDetailsById(id);
    }


    @PostMapping("/pointMap")
    @Transactional
    @ApiOperation("根据时间显示事故地图")
    public CommonResponse pointMap(@RequestParam(value = "sdate",required = true) String sdate,@RequestParam(value = "edate",required = true) String edate) {
    		SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    		try {
        		List<Object> ps = accidentService.findAccidentPoint(parse.parse(sdate),parse.parse(edate));
                return new CommonResponse(true, ps);
		} catch (Exception e) {
			log.info("===========",e);
	        return new CommonResponse(false, e.getMessage());
		}
    }

}
