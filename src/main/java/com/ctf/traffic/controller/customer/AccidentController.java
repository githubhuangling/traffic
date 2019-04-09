package com.ctf.traffic.controller.customer;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.annotation.*;
import javax.imageio.*;
import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.alibaba.fastjson.*;
import com.ctf.traffic.po.*;
import com.ctf.traffic.po.Accident.*;
import com.ctf.traffic.po.AccidentMedia.*;
import com.ctf.traffic.po.AccidentParty.*;
import com.ctf.traffic.po.response.*;
import com.ctf.traffic.po.response.AgreementOwnResponse.*;
import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.remote.response.*;
import com.ctf.traffic.remote.response.QueryDutySimpleResponse.*;
import com.ctf.traffic.service.*;
import com.ctf.traffic.util.*;
import com.ctf.traffic.util.HttpUtils;
import com.ctf.traffic.util.PathUtil.*;

import io.swagger.annotations.*;
import lombok.extern.slf4j.*;

/**
 * @author ramer
 * @Date 6/26/2018
 */
@RestController
@RequestMapping("/customer")
@Api(description = "事故基本信息接口")
@Slf4j
public class AccidentController {
    @Resource
    /*系统参数，查找和修改接口*/
    private SysConfService sysConfService;
    @Resource
    /*一体机当日事故用户录入的信息形成的报表和PC端获取事故详情信息*/
    private AccidentService accidentService;
    @Resource
    /*PC端天气，显示所有天气接口*/
    private WeatherService weatherService;
    @Resource
    /*PC端事故原因分类接口*/
    private AccidentReasonCategoryService categoryService;
    @Resource
    /*管理端事故原因接口*/
    private AccidentReasonService accidentReasonService;
    @Resource
    /*一体机通过事故信息录入、行驶证获取事故照片*/
    private AccidentMediaService accidentMediaService;
    @Resource
    /*一体机事故当事人自行协商协议书信息*/
    private AccidentPartyService accidentPartyService;
    @Resource
    /*一体机驾驶证信息.*/
    private DriverLicenceService driverLicenceService;
    @Resource
    /*一体机行驶证信息*/
    private DrivingLicenceService drivingLicenceService;
    @Resource
    /*PC端一体机管辖区域、事故发生地点接口*/
    private JurisdictionAreaService jurisdictionAreaService;
    @Resource
    /*一体机车辆保单.*/
    private InsuranceCarService insuranceCarService;
    @Resource
    /*PC端和管理端协调中心接口*/
    private SubstationService substationService;
    @Resource
    /*所有可用的保险公司*/
    private InsuranceCompanyService insuranceCompanyService;
    @Value("${QrcodeUrl}")
    private String qrcodeUrl;

    private final String accidentPageId = "a";

    /**
     * 阅读须知.
     */
    @GetMapping("/readInstruction")
    @ApiOperation("阅读须知")
    public CommonResponse readInstruction() {
        CommonResponse result = new CommonResponse(true, sysConfService.findByCode(SysConf.INSTRUCTION));
        log.info(" AccidentController.readInstruction : result[{}]", result);
        return result;
    }

    /**
     * 创建新的事故信息.
     */
    @PostMapping("/accident")
    @ApiOperation("创建新的事故信息")
    public CommonResponse addAccident(HttpSession session, HttpServletResponse response) {
        Long centerId = (Long) session.getAttribute("conductCenterId");
        if (centerId == null) {
            return new CommonResponse(false, "使用本系统需要权限,请联系管理员添加");
        }
        Accident accident = new Accident();
        accident.setConductCenter(new ConductCenter(centerId));
        accidentService.saveOrUpdate(accident);
        session.setAttribute("accident", accident);
        //设置cookie
        Cookie cookie = new Cookie(accidentPageId, accident.getId().toString());
        cookie.setMaxAge(8 * 60 * 60);// 设置为8hour
        cookie.setPath("/");
        response.addCookie(cookie);

        CommonResponse result = new CommonResponse(accident.getId() > 0, accident.getId() > 0 ? accident.getId() : "失败");
        log.info(" AccidentController.addAccident : accidentId[{}], centerId[{}],result[{}]",accident.getId(),centerId,result );
        return result;
    }

    /**
     * 校验身份证.
     */
    @GetMapping("/validIdNumber")
    @ApiOperation("校验身份证号")
    public CommonResponse validIdNumber(@RequestParam("idNumber") String idNumber) {
        QueryDriverResponse queryDriver = accidentService.getNameByIdNumber(idNumber);
        CommonResponse result = new CommonResponse(queryDriver != null && queryDriver.getStatus().equals("1000"), queryDriver);
        log.info(" AccidentController.validIdNumber : idNumber[{}],result[{}]", idNumber,result);
        return result;
    }

    /**
     * 校验车牌号.
     */
    @GetMapping("/validCarNumber")
    @ApiOperation("校验车牌号")
    public CommonResponse validCarNumber(@RequestParam("carType") String carType,
                                         @RequestParam("carNumber") String carNumber) {
        QueryVehicleResponse queryVehicle = accidentService.getNameByCarTypeAndNumber(carType, carNumber);

        CommonResponse result = new CommonResponse(queryVehicle != null && queryVehicle.getStatus().equals("1000"), queryVehicle);
        log.info(" AccidentController.validCarNumber : carType[{}],carNumber[{}],result[{}]", carType,carNumber,result);
        return result;
    }

    /**
     * 用户端通用文件上传.
     */
    @PostMapping("/upload/uploadFile")
    @ApiOperation("上传文件")
    public CommonResponse uploadFile(@RequestParam("file") MultipartFile file,
                                     @RequestParam(value = "name", required = false) String name) {
        String path = ImageUtil.save(file, name, SavingFolder.ACCIDENT);

        CommonResponse result = new CommonResponse(path != null, path);
        log.info(" AccidentController.uploadFile : fileName[{}],result[{}]", name,result);
        return result;
    }

    @PostMapping("/upload/uploadFileTmp")
    @ApiOperation("上传文件")
    public CommonResponse uploadFileTmp(@RequestParam(value = "file", required = false) MultipartFile file)
            throws Exception {
        log.info(" AccidentController.uploadFileTmp : originalFilename[{}],fileSize[{}]", file.getOriginalFilename(), file.getSize());
        File f = new File("C:/Users/ramer/Desktop/11.png");
        try (InputStream stream = file.getInputStream(); FileOutputStream outputStream = new FileOutputStream(f);) {
            byte[] bys = new byte[2048];
            int len;
            while ((len = stream.read(bys)) != -1) {
                outputStream.write(bys, 0, len);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        //        file.transferTo(f);
        return new CommonResponse(true, "");
    }

    private Accident thisSessionAccident(HttpServletRequest request) {
        Accident accident = null;
        Object accidentobj = request.getSession().getAttribute("accident");
        if (accidentobj != null) {
            accident = (Accident) accidentobj;
        } else {
            Cookie[] cookies = request.getCookies();
            String cookieAccidentId = null;
            if (null != cookies) {
                for (Cookie cookie : cookies) {
                    if (accidentPageId.equals(cookie.getName())) {
                        cookieAccidentId = cookie.getValue();
                    }
                }
            }
            if(null==cookieAccidentId) {
            	   return null;
            }else {
                accident = accidentService.findById(Long.valueOf(cookieAccidentId));
            }
        }
        return accident;
    }

    /**
     * 保存驾驶人和车辆信息.
     *
     * @param driver  驾驶人信息
     * @param driving 车辆信息
     */
    @PostMapping("/driverAndDriving")
    @ApiOperation("添加驾驶人和车辆信息")
    public CommonResponse saveDriverAndDriving(
            @ApiParam(value = "{idNumber:'身份证号',name:'姓名',phone:'18990029012',driverLicencePicUrl:'图片',idCardNotCustom:false}") @RequestParam("driver") String driver,
            @ApiParam(value = "{carType:'小车类型',carNumber:'车牌号',ownName:'车主姓名',drivingLicencePicUrl:'图片',carNotCustom:false}") @RequestParam("driving") String driving,
            HttpServletRequest request) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            CommonResponse result = new CommonResponse(false, "数据已失效，请返回首页重新录入");
            log.info(" AccidentController.saveDriverAndDriving : driver[{}],driving[{}],result[{}]", driver,driving,result);
            return result;
        }
        accidentMediaService.initUploadMediaM(accident.getId(), null);
        log.info(" AccidentController.saveDriverAndDriving : accident [{}]", accident.getId());
        // 当事人信息
        AccidentParty accidentParty = new AccidentParty();
        JSONObject driverObj = JSON.parseObject(driver);
        String idNumber = driverObj.getString("idNumber");
        String name = driverObj.getString("name");
        String phone = driverObj.getString("phone");
        Boolean idCardNotCustom = driverObj.getBoolean("idCardNotCustom");
        Integer accidentIndex = driverObj.getInteger("accidentIndex");
        accidentParty.setPhone(phone);
        accidentParty.setName(name);
        accidentParty.setAccident(accident);
        accidentParty.setAccidentIndex(accidentIndex);
        accidentPartyService.saveOrUpdate(accidentParty);
        // 驾驶人信息
        String driverLicencePicUrl = driverObj.getString("driverLicencePicUrl");
        DriverLicence driverLicence = new DriverLicence();
        driverLicence.setNotcustom(idCardNotCustom);
        driverLicence.setPhone(phone);
        driverLicence.setName(name);
        driverLicence.setNumber(idNumber);
        driverLicence.setPicUrl(driverLicencePicUrl);
        driverLicence.setAccidentParty(accidentParty);
        driverLicenceService.saveOrUpdate(driverLicence);
        // 行驶证信息
        JSONObject drivingObj = JSON.parseObject(driving);
        Boolean carNotCustom = driverObj.getBoolean("carNotCustom");
        String carType = drivingObj.getString("carType");
        String carNumber = drivingObj.getString("carNumber");
        String ownName = drivingObj.getString("ownName");
        String drivingLicencePicUrl = drivingObj.getString("drivingLicencePicUrl");
        String insuranceCompany = drivingObj.getString("insuranceCompany");
        String insuranceCompanyCode = drivingObj.getString("insuranceCompanyCode");
        String insuranceCompulsory = drivingObj.getString("insuranceCompulsory");
        String insuranceDateStr = drivingObj.getString("insuranceDate");
        String caseNumber = drivingObj.getString("caseNumber");
        Date insuranceDate;
        try {
            insuranceDate = new SimpleDateFormat("yyyy/MM/dd").parse(insuranceDateStr);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return new CommonResponse(false, "保险期间,格式错误");
        }
        DrivingLicence drivingLicence = new DrivingLicence();
        drivingLicence.setNotcustom(carNotCustom);
        drivingLicence.setName(ownName);
        drivingLicence.setPicUrl(drivingLicencePicUrl);
        drivingLicence.setNumber(carNumber);
        drivingLicence.setType(carType);
        drivingLicence.setAccidentParty(accidentParty);
        drivingLicenceService.saveOrUpdate(drivingLicence);
        InsuranceCar insuranceCar = new InsuranceCar();
        insuranceCar.setDrivingLicence(drivingLicence);
        insuranceCar.setInsuranceCompany(insuranceCompany);
        insuranceCar.setInsuranceCompanyCode(insuranceCompanyCode);
        insuranceCar.setInsuranceCompulsory(insuranceCompulsory);
        insuranceCar.setCaseNumber(caseNumber);
        insuranceCar.setInsuranceDate(insuranceDate);
        insuranceCarService.saveOrUpdate(insuranceCar);
        boolean result = driverLicence.getId() > 0 && drivingLicence.getId() > 0 && insuranceCar.getId() > 0;
        if(result){
            //log.info(" AccidentController.saveDriverAndDriving : [{}]", "------------------------回写主表开始-----------------");

            Accident ac = accidentService.findById(accident.getId());
            //log.info(" AccidentController.saveDriverAndDriving before getDriverNames: [{}]", ac.getDriverNames());
            //log.info(" AccidentController.saveDriverAndDriving before getDriverNumbers: [{}]", ac.getDriverNumbers());
            //log.info(" AccidentController.saveDriverAndDriving before getCarOwnerNames: [{}]", ac.getCarOwnerNames());
           // log.info(" AccidentController.saveDriverAndDriving before getCarMarks: [{}]", ac.getCarMarks());
            ac.setDriverNames(ac.getDriverNames()==null?driverLicence.getName():ac.getDriverNames()+","+driverLicence.getName());
            //log.info(" AccidentController.saveDriverAndDriving : add [{}]", driverLicence.getName());
            ac.setDriverNumbers(ac.getDriverNumbers()==null?driverLicence.getNumber():ac.getDriverNumbers()+","+driverLicence.getNumber());
            //log.info(" AccidentController.saveDriverAndDriving : add [{}]", driverLicence.getName());
            ac.setCarOwnerNames(ac.getCarOwnerNames()==null?drivingLicence.getName():ac.getCarOwnerNames()+","+drivingLicence.getName());
            //log.info(" AccidentController.saveDriverAndDriving : add [{}]", drivingLicence.getName());
            ac.setCarMarks(ac.getCarMarks()==null?drivingLicence.getNumber():ac.getCarMarks()+","+drivingLicence.getNumber());
            //log.info(" AccidentController.saveDriverAndDriving : add [{}]", drivingLicence.getNumber());


            //log.info(" AccidentController.saveDriverAndDriving : end  [{}]", ac.getDriverNames());
           // log.info(" AccidentController.saveDriverAndDriving : end [{}]", ac.getDriverNumbers());
           // log.info(" AccidentController.saveDriverAndDriving : end [{}]", ac.getCarOwnerNames());
            //log.info(" AccidentController.saveDriverAndDriving : end [{}]", ac.getCarMarks());
            Accident rea = accidentService.saveOrUpdate(ac);

           // log.info(" AccidentController.saveDriverAndDriving : saveReturn  [{}]", rea.getDriverNames());
            //log.info(" AccidentController.saveDriverAndDriving : saveReturn [{}]", rea.getDriverNumbers());
           // log.info(" AccidentController.saveDriverAndDriving : saveReturn [{}]", rea.getCarOwnerNames());
          //  log.info(" AccidentController.saveDriverAndDriving : saveReturn [{}]", rea.getCarMarks());

        }
        log.info(" AccidentController.saveDriverAndDriving : driver[{}],driving[{}],insuranceCar[{}],result[{}]", driverLicence,
                drivingLicence, insuranceCar,result);
        return new CommonResponse(result, result ? "保存成功" : "保存失败");
    }

    @GetMapping("/listInsuranceCompany")
    @ApiOperation("获取所有保险公司")
    public CommonResponse listInsuranceCompany() {
        return new CommonResponse(true, insuranceCompanyService.findAllAvailable());
    }

    /**
     * 上传事故照片/视频.
     */
    @PostMapping("/uploadAccidentMedia")
    @ApiOperation("上传事故照片/视频")
    public CommonResponse uploadAccidentMedia(HttpServletRequest request, HttpSession session,
                                              @ApiParam(value = "2018/06/27 11:50:10") @RequestParam("date") String date,
                                              @RequestParam("location") String location, @RequestParam("longitude") String longitude,
                                              @RequestParam("latitude") String latitude, @RequestParam("weather") String weather,
                                              @ApiParam(value = "[{drivingLicenceId:'1',mediaPart:'1',url:''},{drivingLicenceId:'1',mediaPart:'1',url:''}]") @RequestParam(value = "accidentMedias") String accidentMedias) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        accidentMediaService.releaseUploadMediaM(accident.getId());
        Date occurredTime;
        try {
            occurredTime = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return new CommonResponse(false, "wrong format of field 'date'");
        }
        accident = accidentService.findById(accident.getId());
        accident.setOccurredTime(occurredTime);
        accident.setLocation(location);
        accident.setWeather(weather);
        accident.setLongitude(longitude);
        accident.setLatitude(latitude);
        JSONArray jsonArray = JSON.parseArray(accidentMedias);
        log.info(" AccidentController.uploadAccidentMedia : [{}]", jsonArray);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject mediaObj = jsonArray.getJSONObject(i);
            long drivingLicenceId = mediaObj.getLong("drivingLicenceId");
            int mediaPart = mediaObj.getIntValue("mediaPart");
            String url = mediaObj.getString("url");
            log.info(" AccidentController.uploadAccidentMedia : {},{},{}", drivingLicenceId, mediaPart, url);
            accidentMediaService.saveOrUpdate(new AccidentMedia(mediaPart, url, accident.getId(), drivingLicenceId));
        }
        accidentService.saveOrUpdate(accident);
        boolean result = accident.getId() > 0;
        return new CommonResponse(result, result ? "操作成功" : "操作失败");
    }

    /**
     * 重新排号上传事故照片/视频.
     */
    @PostMapping("/reuploadAccidentMedia")
    @ApiOperation("重新上传事故照片/视频")
    public CommonResponse reuploadAccidentMedia(
                                              @ApiParam(value = "[{drivingLicenceId:'1',mediaPart:'1',url:''},{drivingLicenceId:'1',mediaPart:'1',url:''}]") @RequestParam(value = "accidentMedias") String accidentMedias,HttpServletRequest request) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        accidentMediaService.releaseUploadMediaM(accident.getId());

        JSONArray jsonArray = JSON.parseArray(accidentMedias);
        log.info(" AccidentController.uploadAccidentMedia : [{}]", jsonArray);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject mediaObj = jsonArray.getJSONObject(i);
            long drivingLicenceId = mediaObj.getLong("drivingLicenceId");
            int mediaPart = mediaObj.getIntValue("mediaPart");
            String url = mediaObj.getString("url");
            log.info(" AccidentController.uploadAccidentMedia : {},{},{}", drivingLicenceId, mediaPart, url);
            accidentMediaService.saveOrUpdate(new AccidentMedia(mediaPart, url, accident.getId(), drivingLicenceId));
        }
        accidentService.saveOrUpdate(accident);
        boolean result = accident.getId() > 0;
        return new CommonResponse(result, result ? "操作成功" : "操作失败");
    }

    /**
     * 手机上传事故照片/视频.
     */
    @PostMapping("/uploadAccidentMediaM")
    @ApiOperation("手机上传事故照片/视频")
    public CommonResponse uploadAccidentMediaM(@RequestParam(value = "mediaPart") Integer mediaPart,
                                               @RequestParam(value = "fileUrl") String fileUrl, @RequestParam("asdf") Long accidentId) {
        boolean result = accidentMediaService.saveUploadMediaM(accidentId, mediaPart, fileUrl) != null;
        CommonResponse response = new CommonResponse(result, result ? "操作成功" : "操作失败");
        log.info(" AccidentController.uploadAccidentMediaM : accidentId[{}],mediaPart[{}],fileUrl[{}],result[{}]", accidentId,mediaPart,fileUrl,response);
        return response;
    }

    @GetMapping("/canUploadMediaM")
    @ApiOperation("手机端是否可上传图片")
    public CommonResponse canUploadMediaM(@RequestParam("asdf") Long accidentId, @RequestParam("uuid") String uuid) {
        boolean result = accidentMediaService.canUploadMediaM(accidentId, uuid);
        CommonResponse response = new CommonResponse(result, result ? "成功" : "当前网页已失效，请重新扫描二维码上传");
        log.info(" AccidentController.canUploadMediaM : accidentId[{}],uuid[{}],response[{}]", accidentId,uuid,response);
        return response;
    }

    @GetMapping("/uploadMediaQrcode")
    @ApiOperation("获取手机事故照片上传二维码")
    public CommonResponse uploadMediaQrcode(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {

        Accident accident = thisSessionAccident(request);
 /*        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        response.setHeader("Pragma", "Pragma");
        response.setContentType("image/jpeg");
        String rootPath = url.substring(0, url.indexOf(uri));
        log.debug(" AccidentController.uploadMediaQrcode : rootPath[{}]", rootPath);
        ImageIO.write(QrcodeUtil.QRCodeCreate(rootPath + "/customer/mInfoUpload?asdf=" + accident.getId(), 200, 200),
                "png", response.getOutputStream());*/
        String result = HttpUtils.request(qrcodeUrl + accident.getId(),null, RequestMethod.GET, String.class);
        return new CommonResponse(true,result);

    }

    @GetMapping("/phoneQrcode")
    @ApiOperation("获取主页二维码")
    public void phoneQrcode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        response.setHeader("Pragma", "Pragma");
        response.setContentType("image/jpeg");
        String rootPath = url.substring(0, url.indexOf(uri));
        log.info(" AccidentController.uploadMediaQrcode : rootPath[{}]", rootPath);
        ImageIO.write(QrcodeUtil.QRCodeCreate(rootPath + "/customer/m_index.html", 200, 200), "png",
                response.getOutputStream());
    }

    @GetMapping("/getUploadMediaM")
    @ApiOperation("获取手机上传的事故照片/视频")
    public CommonResponse getUploadMediaM(HttpServletRequest request) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        CommonResponse result = new CommonResponse(true, accidentMediaService.getUploadMediaM(accident.getId()));
        log.info(" AccidentController.getUploadMediaM : accidentId[{}],result[{}]", accident.getId(),result);
        return result;
    }

    @GetMapping("/getLocationList")
    @ApiOperation("获取可选地址")
    public CommonResponse getLocationList(@RequestParam(value = "pid", required = false) Long pid) {
        List<JurisdictionAreaResponse> responses = new ArrayList<>();
        jurisdictionAreaService.findByParent(pid).forEach(area -> responses
                .add(new JurisdictionAreaResponse(area, area.getParent() != null ? area.getParent().getId() : -1)));
        CommonResponse result = new CommonResponse(true, responses);
        log.info(" AccidentController.getLocationList : pid[{}],result[{}]", pid,result);
        return result;
    }

    @GetMapping("/listSubstation")
    @ApiOperation("获取所有分局")
    public CommonResponse listSubstation() {
        return new CommonResponse(true, substationService.findAll());
    }

    @GetMapping("/getWeatherList")
    @ApiOperation("获取可选天气")
    public CommonResponse getSelectorWeather() {
        return new CommonResponse(true, weatherService.findAllAvailable());
    }

    @GetMapping("/getDrivingLicenceList")
    @ApiOperation("获取可选行驶证")
    public CommonResponse getDrivingLicenceList(HttpServletRequest request) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        log.debug(" AccidentController.getDrivingLicenceList : [{}]", accident.getId());
        return new CommonResponse(true, drivingLicenceService.findByAccidentId(accident.getId()));
    }

    @GetMapping("/test")
    @ApiOperation("test")
    public CommonResponse test(@RequestParam("accidentId") Long accidentId, HttpServletRequest request) {
        return new CommonResponse(true, "");
    }

    @GetMapping("/reSeq")
    @ApiOperation("重新排号")
    public CommonResponse reSeq(HttpServletRequest request) {
        Accident a = thisSessionAccident(request);
        Accident accident = accidentService.findById(a.getId());
        if(accident==null){new CommonResponse(false,"无事故信息");}
        int i = accidentService.assignSeqNumber(accident.getId());
        return new CommonResponse(true,i);
    }

    @PutMapping("/updateProcessMode")
    @ApiOperation("更新事故处理方式")
    public CommonResponse updateProcessMode(@RequestParam("processMode") int processMode,
    		HttpServletRequest request) {
		Accident accident = thisSessionAccident(request);
		if(accident==null) {
			return new CommonResponse(false, "数据已失效，请返回首页重新录");
		}
		//查看一月内是否有同一起事故做在线认定(根据当事人方车牌号判断)，如果有，则不能提交。--zengdayong 20180907
		//查询本事故的车辆信息
		List<DrivingLicence> dls1 = accidentService.findDrivingLicenceByAccidentId(accident.getId());
		//根据车牌号查询一月内的事故信息
	    	Calendar c = Calendar.getInstance();
	    	c.add(Calendar.MONTH, -1);
	    	List<Accident> as = accidentService.findAccidentByDrivingLicence(dls1.get(0).getNumber(), c.getTime());
		for(Accident a:as) {
			//根据事故id取得车辆信息
			List<DrivingLicence> dls2 = accidentService.findDrivingLicenceByAccidentId(a.getId());
			for(DrivingLicence d:dls2) {
				if(dls1.get(1).getNumber().equals(d.getNumber()) && (a.getProcessMode()==1||a.getProcessMode()==2)) {//如果其它车辆，有一辆车相同，则认定为同一起事故，如果也是在线认定，则不能提交
					return new CommonResponse(false, "已有同一事故记录，请勿重复提交");
				}
			}
		}
		
        accident = accidentService.findById(accident.getId());
        accident.setProcessMode(processMode);
        accident.setSerialNumber(accidentService.generateSerialNumber(accident.getId()));//生成事故临时编号
        accidentService.saveOrUpdate(accident);
        boolean result = accident.getId() > 0;
        CommonResponse response = new CommonResponse(result, result ? "更新成功" : "更新失败");
            log.info(" AccidentController.updateProcessMode : accidentId[{}],processMode[{}],response[{}]", accident.getId(),processMode,response);
        return response;
    }

    @PostMapping("/updateProcessStatus")
    @ApiOperation("更新事故处理状态")
    public CommonResponse updateAccidentProcessStatus(HttpServletRequest request,
                                                      @RequestParam("processStatus") Integer processStatus) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            CommonResponse response = new CommonResponse(false, "数据已失效，请返回首页重新录");
            log.info(" AccidentController.updateAccidentProcessStatus : accidentId[{}],processStatus[{}],result[{}]", accident.getId(),processStatus,response);
            return response;
        }
        accident = accidentService.findById(accident.getId());
        accident.setProcessStatus(processStatus);
        boolean result = accidentService.saveOrUpdate(accident).getId() > 0;
        CommonResponse response = new CommonResponse(result, result ? "更新成功" : "更新失败");
        log.info(" AccidentController.updateAccidentProcessStatus : accidentId[{}],processStatus[{}],response[{}]", accident.getId(),processStatus,response);
        return response;
    }

    @PutMapping("/updateProcessModeAndProcessStatus")
    @ApiOperation("更新事故处理方式和处理状态")
    public CommonResponse updateProcessModeProcessStatus(@RequestParam("processMode") Integer processMode, @RequestParam("processStatus") Integer processStatus,
                                                         HttpServletRequest request) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            CommonResponse response = new CommonResponse(false, "数据已失效，请返回首页重新录");
            return response;
        }
        if (processMode == null || processStatus == null) {
            CommonResponse response = new CommonResponse(false, "参数不正确");
            log.info(" AccidentController.updateProcessModeProcessStatus : accidentId[{}],processMode[{}],processStatus[{}],response[{}]", accident.getId(),processMode,processStatus,response);
            return response;
        }
        accident = accidentService.findById(accident.getId());
        accident.setProcessMode(processMode);
        accident.setSerialNumber(accidentService.generateSerialNumber(accident.getId()));//生成事故临时编号
        accident.setProcessStatus(processStatus);
        boolean result = accidentService.saveOrUpdate(accident).getId() > 0;
        CommonResponse response = new CommonResponse(result, result ? "更新成功" : "更新失败");
        log.info(" AccidentController.updateProcessModeProcessStatus : accidentId[{}],processMode[{}],processStatus[{}],response[{}]", accident.getId(),processMode,processStatus,response);
        return response;
    }

    @GetMapping("/accident")
    @ApiOperation("获取事故基本信息")
    public CommonResponse getAccident(HttpServletRequest request) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        CommonResponse result = new CommonResponse(true, accidentService.findById(accident.getId()));
        log.info(" AccidentController.getAccident : accidentId[{}],result[{}]", accident.getId(),result);
        return result;
    }

    @GetMapping("/accidentReason/list")
    @ApiOperation("获取所有事故原因")
    public CommonResponse getAllAccidentReason(@RequestParam(value = "categoryId", required = false) Long categoryId) {
        CommonResponse result = new CommonResponse(true, accidentReasonService.findByCategoryId(categoryId == null ? 0 : categoryId));
        return result;
    }

    @PostMapping("/accidentReason")
    @Transactional
    @ApiOperation("自行协商保存事故原因和事故责任")
    public CommonResponse saveAccidentReasonAndResponsibility(HttpServletRequest request,
                                                              @RequestParam("accidentReasonId") long accidentReasonId,
                                                              @ApiParam("[{'id':1,responsibility:'可选责任(0,1,2)'}]") @RequestParam("responsibilities") String responsibilities) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        Accident acc = accidentService.findById(accident.getId());
        acc.setProcessMode(ProcessMode.INDEPENDENT.ordinal());
        acc = accidentService.saveReasonAndResponsibility(acc, accidentReasonId, JSON.parseArray(responsibilities));
        boolean result = acc.getId() > 0;
        CommonResponse response = new CommonResponse(result, result ? "保存事故原因成功" : "保存事故原因失败");
        log.info(" AccidentController.saveAccidentReasonAndResponsibility : accidentId[{}],accidentReasonId[{}],responsibilities[{}],response[{}]", accident.getId(),accidentReasonId,responsibilities,response);
        return response;
    }

    @PutMapping("/finishAccident")
    @ApiOperation("更新事故处理状态为完成")
    public CommonResponse finishAccident(HttpServletRequest request) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        Accident acc = accidentService.findById(accident.getId());
        int ordinal = ProcessStatus.DEALED.ordinal();
        log.info(" AccidentController.finishAccident : >>>>>>>>>>>>>>>>>>[{}]", ordinal);
        acc.setProcessStatus(ordinal);
        boolean result = accidentService.saveOrUpdate(acc).getId() > 0;
        CommonResponse response = new CommonResponse(result, result ? "成功" : "失败");
        log.info(" AccidentController.finishAccident : accindetId[{}],response[{}]", accident.getId(),response);
        return response;
    }

    @GetMapping("/accidentParty")
    @ApiOperation("获取所有当事人基本信息")
    public CommonResponse listAccidentPartyByAccident(HttpServletRequest request,
                                                      @RequestParam(value = "accidentId", required = false) Long accidentId) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        List<AccidentPartyResponse> responses = new ArrayList<>();
        if (accidentId == null)
            accidentId = accident.getId();
        accidentPartyService.findByAccidentId(accidentId).forEach(party -> {
            DrivingLicence drivingLicence = drivingLicenceService.findByAccidentPartyId(party.getId());
            InsuranceCar insuranceCar = insuranceCarService.findByDrivingLicence(drivingLicence.getId());
            responses.add(new AccidentPartyResponse(party.getId(), party.getName(),
                    AccidentIndex.getDesc(party.getAccidentIndex()), insuranceCar.getInsuranceCompany(),
                    insuranceCar.getInsuranceCompulsory(), insuranceCar.getInsuranceDate(), drivingLicence, null));
        });
        CommonResponse response = new CommonResponse(true, responses);
        log.info(" AccidentController.listAccidentPartyByAccident : accidentId[{}],response[{}]", accident.getId(),response);
        return response;
    }

    @PostMapping("/accidentPart/{id}")
    @ApiOperation("保存当事人签名图片")
    public CommonResponse saveAccidentPartSignature(@PathVariable("id") long id,
                                                    @RequestParam("signaturePic") String signaturePic) {
        CommonResponse result = accidentService.saveAccidentPartSignature(id, signaturePic);
        log.info(" AccidentController.saveAccidentPartSignature : partyId[{}],signaturePic[{}],result[{}]", id,signaturePic,result);
        return result;
    }

    @GetMapping("/ownAgreement")
    @ApiOperation("自行协商协议书")
    public CommonResponse ownAgreement(HttpServletRequest request) {
        Accident acc = thisSessionAccident(request);
        if (acc == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        AgreementOwnResponse response = new AgreementOwnResponse();
        final Accident accident = accidentService.findById(acc.getId());
        response.setId(accident.getId());
        response.setTime(accident.getOccurredTime());
        response.setLocation(accident.getLocation());
        response.setWeather(accident.getWeather());
        response.setSerialNumber(accident.getSerialNumber());
        response.setId(accident.getId());
        response.setAccidentReason(accident.getAccidentReason().getAccidentReasonCategory().getName());
        List<AgreementOwnParty> parties = new ArrayList<>();
        accidentPartyService.findByAccidentId(accident.getId()).forEach(accidentParty -> {
            AgreementOwnParty party = new AgreementOwnParty();
            party.setId(accidentParty.getId());
            DrivingLicence drivingLicence = drivingLicenceService.findByAccidentPartyId(accidentParty.getId());
            party.setClause(accidentParty.getClause());
            party.setIllegalBehavior(accidentParty.getIllegalBehavior());
            party.setDrivingNumber(drivingLicence.getNumber());
            party.setDriverNumber(driverLicenceService.findByAccidentPartyId(accidentParty.getId()).getNumber());
            party.setName(accidentParty.getName());
            party.setPhone(accidentParty.getPhone());
            party.setSignaturePic(accidentParty.getSignaturePic());
            log.info(" AccidentController.ownAgreement : [{}]", accidentParty.getResponsibility());
            party.setAccidentIndex(AccidentIndex.getDesc(accidentParty.getAccidentIndex()));
            party.setResponsibility(Responsibility.getDesc(accidentParty.getResponsibility()));
            party.setCarType(drivingLicence.getType());
            InsuranceCar insuranceCar = insuranceCarService.findByDrivingLicence(drivingLicence.getId());
            party.setInsuranceCompany(insuranceCar.getInsuranceCompany());
            party.setInsuranceCompulsory(insuranceCar.getInsuranceCompulsory());
            party.setCaseNumber(insuranceCar.getCaseNumber());
            party.setInsuranceDate(insuranceCar.getInsuranceDate());
            Set<String> brokenParts = new HashSet<>();
            accidentMediaService.findByDrivingLicence(drivingLicence.getId())
                    .forEach(accidentMedia -> brokenParts.add(BrokenParty.getDesc(accidentMedia.getPart())));
            party.setBrokenParts(brokenParts.toString());
            log.info(" AccidentController.ownAgreement :[{},{}]", accident.getId(), accident.getAccidentReason());
            party.setAccidentReason(accident.getAccidentReason().getAccidentReasonCategory().getName());
            parties.add(party);
        });
        response.setAccidentParties(parties);
        JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(response));
        jsonObject.put("reasonCategories", categoryService.findAll());
        CommonResponse result = new CommonResponse(response.getAccidentParties().size() > 1, jsonObject);
        log.info(" AccidentController.ownAgreement : [{}]", result);
        return result;
    }

    @GetMapping("/ownAgreement/{accidentId}")
    @ApiOperation("自行协商协议书打印")
    public CommonResponse ownAgreementPrint(@PathVariable("accidentId") long accidentId) {
        return Optional.ofNullable(accidentService.findById(accidentId)).map(acc -> {
            AgreementOwnResponse response = new AgreementOwnResponse();
            response.setId(acc.getId());
            Accident accident = accidentService.findById(acc.getId());
            response.setTime(accident.getOccurredTime());
            response.setLocation(accident.getLocation());
            response.setWeather(accident.getWeather());
            response.setSerialNumber(accident.getSerialNumber());
            response.setId(accident.getId());
            response.setAccidentReason(accident.getAccidentReason().getAccidentReasonCategory().getName());
            List<AgreementOwnParty> parties = new ArrayList<>();
            accidentPartyService.findByAccidentId(acc.getId()).forEach(accidentParty -> {
                AgreementOwnParty party = new AgreementOwnParty();
                party.setId(accidentParty.getId());
                DrivingLicence drivingLicence = drivingLicenceService.findByAccidentPartyId(accidentParty.getId());
                party.setClause(accidentParty.getClause());
                party.setIllegalBehavior(accidentParty.getIllegalBehavior());
                party.setDrivingNumber(drivingLicence.getNumber());
                party.setDriverNumber(driverLicenceService.findByAccidentPartyId(accidentParty.getId()).getNumber());
                party.setName(accidentParty.getName());
                party.setPhone(accidentParty.getPhone());
                party.setAccidentIndex(AccidentIndex.getDesc(accidentParty.getAccidentIndex()));
                party.setResponsibility(Responsibility.getDesc(accidentParty.getResponsibility()));
                party.setCarType(drivingLicence.getType());
                InsuranceCar insuranceCar = insuranceCarService.findByDrivingLicence(drivingLicence.getId());
                party.setInsuranceCompany(insuranceCar.getInsuranceCompany());
                party.setInsuranceCompulsory(insuranceCar.getInsuranceCompulsory());
                party.setSignaturePic(accidentParty.getSignaturePic());
                party.setCaseNumber(insuranceCar.getCaseNumber());
                party.setInsuranceDate(insuranceCar.getInsuranceDate());
                Set<String> brokenParts = new HashSet<>();
                accidentMediaService.findByDrivingLicence(drivingLicence.getId())
                        .forEach(accidentMedia -> brokenParts.add(BrokenParty.getDesc(accidentMedia.getPart())));
                party.setBrokenParts(brokenParts.toString());
                log.info(" AccidentController.ownAgreement :[{},{}]", accident.getId(), accident.getAccidentReason());
                party.setAccidentReason(accident.getAccidentReason().getAccidentReasonCategory().getName());
                parties.add(party);
            });
            response.setAccidentParties(parties);
            JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(response));
            jsonObject.put("reasonCategories", categoryService.findAll());
            return new CommonResponse(response.getAccidentParties().size() > 1, jsonObject);
        }).orElse(new CommonResponse(false, "无事故信息"));
    }

    @GetMapping("/ownAgreementByAccidentId")
    @ApiOperation("根据事故id查自行协商协议书")
    public CommonResponse ownAgreementByAccidentId(
            @RequestParam(value = "accidentid", required = true) Long accidentid) {
        AgreementOwnResponse response = new AgreementOwnResponse();
        Accident accident = accidentService.findById(accidentid);
        response.setTime(accident.getOccurredTime());
        response.setLocation(accident.getLocation());
        response.setWeather(accident.getWeather());
        //QrcodeUtil.QRCodeCreate();
        response.setAccidentReason(accident.getAccidentReason().getAccidentReasonCategory().getName());
        List<AgreementOwnParty> parties = new ArrayList<>();
        accidentPartyService.findByAccidentId(accidentid).forEach(accidentParty -> {
            AgreementOwnParty party = new AgreementOwnParty();
            DrivingLicence drivingLicence = drivingLicenceService.findByAccidentPartyId(accidentParty.getId());
            party.setClause(accidentParty.getClause());
            party.setIllegalBehavior(accidentParty.getIllegalBehavior());
            party.setDrivingNumber(drivingLicence.getNumber());
            party.setDriverNumber(driverLicenceService.findByAccidentPartyId(accidentParty.getId()).getNumber());
            party.setName(accidentParty.getName());
            party.setPhone(accidentParty.getPhone());
            log.info(" AccidentController.ownAgreement : [{}]", accidentParty.getResponsibility());
            party.setResponsibility(Responsibility.getDesc(accidentParty.getResponsibility()));
            party.setCarType(drivingLicence.getType());
            InsuranceCar insuranceCar = insuranceCarService.findByDrivingLicence(drivingLicence.getId());
            party.setInsuranceCompany(insuranceCar.getInsuranceCompany());
            party.setInsuranceCompulsory(insuranceCar.getInsuranceCompulsory());
            party.setInsuranceDate(insuranceCar.getInsuranceDate());
            Set<String> brokenParts = new HashSet<>();
            accidentMediaService.findByDrivingLicence(drivingLicence.getId())
                    .forEach(accidentMedia -> brokenParts.add(BrokenParty.getDesc(accidentMedia.getPart())));
            party.setBrokenParts(brokenParts.toString());
            log.info(" AccidentController.ownAgreement :[{},{}]", accident.getId(), accident.getAccidentReason());
            party.setAccidentReason(accident.getAccidentReason().getAccidentReasonCategory().getName());
            parties.add(party);
        });
        response.setAccidentParties(parties);
        JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(response));
        jsonObject.put("reasonCategories", categoryService.findAll());
        CommonResponse response1 = new CommonResponse(response.getAccidentParties().size() > 1, jsonObject);
        log.info(" AccidentController.ownAgreementByAccidentId : accidentid[{}],response1[{}]", accidentid,response1);
        return response1;
    }

    @GetMapping("/ownAgreementQrcode/{accidentId}")
    @ApiOperation("获取事故认定书二维码")
    public void ownAgreementQrcode(@PathVariable("accidentId") Long accidentId, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        Accident accident = accidentService.findById(accidentId);
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        response.setHeader("Pragma", "Pragma");
        response.setContentType("image/jpeg");
        String rootPath = url.substring(0, url.indexOf(uri));
        log.info(" AccidentController.ownAgreementQrcode : content[{}]", rootPath);
        ImageIO.write(QrcodeUtil.QRCodeCreate(SerialNumberUtil.parseToBase64(accident.getId() + accident.getSerialNumber()), 200, 200), "png", response.getOutputStream());
    }

    @GetMapping("/ownAgreementQrcode")
    @ApiOperation("获取事故认定书二维码")
    public void ownAgreementQrcode(HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        Accident accident = thisSessionAccident(request);
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        response.setHeader("Pragma", "Pragma");
        response.setContentType("image/jpeg");
        String rootPath = url.substring(0, url.indexOf(uri));
        accident = accidentService.findById(accident.getId());
        log.info(" AccidentController.ownAgreementQrcode : content[{},{}]", rootPath, accident.getId());
        ImageIO.write(QrcodeUtil.QRCodeCreate(SerialNumberUtil.parseToBase64(accident.getId() + accident.getSerialNumber()), 200, 200), "png", response.getOutputStream());
    }

    @PostMapping("/processMode")
    @ApiOperation("保存事故处理方式")
    public CommonResponse saveProcessMode(HttpServletRequest request,
                                          @ApiParam("可选值: 0,1,2") @RequestParam("processMode") Integer processMode) {
        Accident sessionAccident = thisSessionAccident(request);
        if (sessionAccident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        return Optional.of(accidentService.findById(sessionAccident.getId())).map(accident -> {
            accident.setProcessMode(processMode);
            accident.setSerialNumber(accidentService.generateSerialNumber(accident.getId()));
            accidentService.saveOrUpdate(accident);
            boolean result = accident.getId() > 0;
            CommonResponse response = new CommonResponse(result, result ? "保存成功" : "保存失败");
            log.info(" AccidentController.saveProcessMode : sessionAccident.getId()[{}],processMode[{}],response[{}]", sessionAccident.getId(),processMode,response);
            return response;
        }).orElse(new CommonResponse(false, "事故信息不存在"));
    }

    @PostMapping("/seqNumber")
    @ApiOperation("获取排队号")
    public CommonResponse getSeqNumber(HttpServletRequest request) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        int seqNumber = accidentService.assignSeqNumber(accident.getId());
        log.info(" AccidentController.getSeqNumber : [{}]", seqNumber);
        boolean result = seqNumber > 0;
        CommonResponse response = new CommonResponse(result, result ? String.format("%03d", seqNumber) : "获取排队号失败");
        log.info(" AccidentController.getSeqNumber : accidentId[{}],response[{}]", accident.getId(),response);
        return response;
    }

    @GetMapping("/getAccidentTotalCount")
    @ApiOperation("获取当前待处理事故总数")
    public CommonResponse getAccidentTotalCount() {
        List<Accident> accidents = accidentService.findByProcessStatus(0);
        log.info(" AccidentController.listPendingCoordinationAccident : [{}]", accidents.size());
        int code = Integer.valueOf(sysConfService.findByCode(Constant.SYS_WAIT_TIME_CODE).getValue());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", accidents.size());
        jsonObject.put("totalTime", code * accidents.size());
        CommonResponse response = new CommonResponse(true, jsonObject);
        log.info(" AccidentController.getAccidentTotalCount : response[{}]", response);
        return response;
    }

    @GetMapping("/listByIdNumber")
    @ApiOperation("通过身份证获取事故信息")
    public CommonResponse listAccidentByIdNumber(@RequestParam("idNumber") String idNumber) {
        List<AccidentPendingResponse> responses = new ArrayList<>();
        List<Accident> accidents = accidentService.findByNumber(idNumber, 1, 4);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (accidents.size() < 1)
            return new CommonResponse(true, responses);
        accidents.forEach(accident -> {
            Integer processMode = accident.getProcessMode();
            if (processMode != null && processMode > -1) {
                AccidentPendingResponse response = new AccidentPendingResponse();
                final Integer processStatus = accident.getProcessStatus();
                /*if (processMode.equals(ProcessMode.COORDINATE.ordinal())
                        && (accident.getUpdateTime().getTime() < calendar.getTimeInMillis()
                                && processStatus == ProcessStatus.TO_DEAL.ordinal()
                                || processStatus == ProcessStatus.DEAL_CANCEL.ordinal())) {*/
                if (processStatus.equals(ProcessStatus.DEAL_CANCEL.ordinal()) || processMode.equals(ProcessMode.OFFLINE.ordinal())) {
                    response.setRefreshSeqNumber(true);
                } else {
                    response.setRefreshSeqNumber(false);
                }
                response.setIdString(String.valueOf(accident.getId()));
                response.setOccurredTime(accident.getOccurredTime());
                response.setLocation(accident.getLocation());
                List<String> parties = new ArrayList<>();
                List<String> drivingNumbers = new ArrayList<>();
                StringBuilder partyBuilder = new StringBuilder();
                StringBuilder drivingNumberBuilder = new StringBuilder();
                accidentPartyService.findByAccidentId(accident.getId()).forEach(party -> {
                    String accidentPartyName = party.getName();
                    String drivingNumber = drivingLicenceService.findByAccidentPartyId(party.getId()).getNumber();
                    if (partyBuilder.length() < 1) {
                        partyBuilder.append(accidentPartyName);
                        drivingNumberBuilder.append(drivingNumber);
                    } else {
                        partyBuilder.append(",").append(accidentPartyName);
                        drivingNumberBuilder.append(",").append(drivingNumber);
                    }
                    parties.add(accidentPartyName);
                    drivingNumbers.add(drivingNumber);
                });
                response.setAccidentPartyStr(partyBuilder.toString());
                response.setDrivingNumberStr(drivingNumberBuilder.toString());
                response.setAccidentParties(parties);
                response.setDrivingNumber(drivingNumbers);
                response.setProcessMode(ProcessMode.getDesc(processMode));
                response.setProcessStatus(ProcessStatus.getDesc(processStatus));
                response.setDataSources(DataSources.getDesc(accident.getDataSources()));
                responses.add(response);
            }
        });
        int size = responses.size();
        if (size >= 4) {
            return new CommonResponse(true, responses);
        } else {
            QueryDutySimpleResponse dutySimpleResponse = accidentService.listAccidentFromSixToOne(idNumber);
            if (null != dutySimpleResponse) {
                List<QueryDutySimpleData> dataList = dutySimpleResponse.getData();
                if (dutySimpleResponse.getStatus().equals("1000") && dutySimpleResponse.getTotal() > 0) {
                    for (int i = 0; size <= 4 && i < 4 - size; i++, size++) {
                        QueryDutySimpleData dutySimpleData = dataList.get(i);
                        AccidentPendingResponse response = new AccidentPendingResponse();
                        response.setIdString("");
                        Date sgfssj = new Date();
                        try {
                            sgfssj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dutySimpleData.getSgfssj());
                        } catch (ParseException e) {
                            log.error(e.getMessage(), e);
                        }
                        response.setOccurredTime(sgfssj);
                        response.setLocation(dutySimpleData.getSgdd());
                        response.setDataSources(DataSource.getDesc(dutySimpleData.getSjly()));
                        List<String> parties = new ArrayList<>();
                        List<String> drivingNumbers = new ArrayList<>();
                        StringBuilder partyBuilder = new StringBuilder();
                        StringBuilder drivingNumberBuilder = new StringBuilder();
                        dutySimpleData.getDutysimplehuManList().forEach(manList -> {
                            String accidentPartyName = manList.getXm();
                            String drivingNumber = manList.getHphm();
                            if (partyBuilder.length() < 1) {
                                partyBuilder.append(accidentPartyName);
                                drivingNumberBuilder.append(drivingNumber);
                            } else {
                                partyBuilder.append(",").append(accidentPartyName);
                                drivingNumberBuilder.append(",").append(drivingNumber);
                            }
                            parties.add(accidentPartyName);
                            drivingNumbers.add(drivingNumber);
                        });
                        response.setAccidentPartyStr(partyBuilder.toString());
                        response.setDrivingNumberStr(drivingNumberBuilder.toString());
                        response.setAccidentParties(parties);
                        response.setDrivingNumber(drivingNumbers);
                        // TODO-POST: 12123事故信息状态,处理方式没有
                        response.setProcessMode("自行协商");
                        response.setProcessStatus(ProcessStatus.getDesc(0));
                        responses.add(response);
                    }
                }
            }

        }
        CommonResponse response = new CommonResponse(true, responses);
        log.info(" AccidentController.listAccidentByIdNumber : idNumber[{}],response[{}]",idNumber,response );
        return response;
    }

    @GetMapping("/validUniqueIdNumber")
    @ApiOperation("校验身份证唯一性")
    public CommonResponse validUniqueIdNumberAndCarNumber(HttpServletRequest request,
                                                          @RequestParam(value = "idNumber", required = false) String idNumber,
                                                          @RequestParam(value = "carNumber", required = false) String carNumber) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        if (StringUtils.isEmpty(idNumber) && StringUtils.isEmpty(carNumber)) {
            return new CommonResponse(false, "待校验身份证或车牌号不能全为空");
        }
        boolean unique = isUnique(accident.getId(), idNumber, carNumber);
        return new CommonResponse(unique, unique ? "证件号不重复" : "证件号重复");
    }

    private boolean isUnique(long accidentId, String idNumber, String carNumber) {
        List<AccidentParty> accidentParties = accidentPartyService.findByAccidentId(accidentId);
        boolean unique = true;
        if (accidentParties.size() > 0) {
            for (int i = 0; i < accidentParties.size(); i++) {
                if (idNumber != null) {
                    if (driverLicenceService.findByAccidentPartyId(accidentParties.get(i).getId()).getNumber()
                            .equals(idNumber)) {
                        unique = false;
                        break;
                    }
                } else {
                    if (drivingLicenceService.findByAccidentPartyId(accidentParties.get(i).getId()).getNumber()
                            .equals(carNumber)) {
                        unique = false;
                        break;
                    }
                }
            }
        }
        return unique;
    }


    @GetMapping("/listPendingAccident")
    @ApiOperation("大屏获取待调解事故信息")
    public CommonResponse listPendingCoordinationAccident() {
        List<AccidentPendingResponse> responses = new ArrayList<>();
        List<Accident> accidents = accidentService.findByProcessStatus(0);
        log.info(" AccidentController.listPendingCoordinationAccident : [{}]", accidents.size());
        accidents.forEach(accident -> {
            AccidentPendingResponse response = new AccidentPendingResponse();
            response.setId(accident.getId());
            response.setSeqNumber(accident.getSeqNumber());
            List<String> parties = new ArrayList<>();
            List<String> drivingNumbers = new ArrayList<>();
            StringBuilder partyBuilder = new StringBuilder();
            StringBuilder drivingNumberBuilder = new StringBuilder();
            accidentPartyService.findByAccidentId(accident.getId()).forEach(party -> {
                String accidentPartyName = party.getName();
                String drivingNumber = drivingLicenceService.findByAccidentPartyId(party.getId()).getNumber();
                if (partyBuilder.length() < 1) {
                    partyBuilder.append(accidentPartyName);
                    drivingNumberBuilder.append(drivingNumber);
                } else {
                    partyBuilder.append(",").append(accidentPartyName);
                    drivingNumberBuilder.append(",").append(drivingNumber);
                }
                parties.add(party.getName());
                drivingNumbers.add(drivingLicenceService.findByAccidentPartyId(party.getId()).getNumber());
            });
            response.setAccidentPartyStr(partyBuilder.toString());
            response.setDrivingNumberStr(drivingNumberBuilder.toString());
            response.setAccidentParties(parties);
            response.setTime(accident.getUpdateTime());
            response.setDrivingNumber(drivingNumbers);
            response.setProcessStatus(ProcessStatus.getDesc(accident.getProcessStatus()));
            response.setLocation(accident.getLocation());
            responses.add(response);
        });
        return new CommonResponse(true, responses);
    }

    @PostMapping("/saveAgreement")
    @ApiOperation("保存协议书")
    public CommonResponse saveAgreement(HttpServletRequest request,
                                        @RequestParam("agreementUrl") String agreementUrl) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        accident = accidentService.findById(accident.getId());
        accident.setAgreementPicUrl(agreementUrl);
        boolean result = accidentService.saveOrUpdate(accident).getId() > 0;
        CommonResponse response = new CommonResponse(result, result ? "保存成功" : "保存失败");
        log.info(" AccidentController.saveAgreement : accidentId[{}],agreementUrl[{}],response[{}]", accident.getId(),agreementUrl,response);
        return response;
    }

    @GetMapping("/previewAgreement")
    @ApiOperation("预览协议书")
    public CommonResponse previewAgreement(HttpServletRequest request) {
        Accident accident = thisSessionAccident(request);
        if (accident == null) {
            return new CommonResponse(false, "数据已失效，请返回首页重新录");
        }
        CommonResponse response = new CommonResponse(true, accident.getAgreementPicUrl());
        log.info(" AccidentController.previewAgreement : accidentId[{}],response[{}]", accident.getId(),response);
        return response;
    }

    @GetMapping("/accident/from/{accidentId}")
    @ApiOperation("排队久了想改成自行协商")
    public CommonResponse sessionFrom(@PathVariable("accidentId") long accidentId, HttpSession session) {
        Accident accident = accidentService.findByEncId(accidentId);
        session.setAttribute("accident", accident);
        CommonResponse response = new CommonResponse(accident != null, accident != null ? "成功" : "无当事人信息");
        log.info(" AccidentController.sessionFrom : accidentId[{}],response[{}]", accidentId,response);
        return response;
    }



    @GetMapping("/accident/setAccindetInSession/{accidentId}")
    @ApiOperation("通过事故Id设置session中的accident")
    public CommonResponse setAccidentInSession(@PathVariable("accidentId") long accidentId,HttpServletRequest request){
       Accident accident=accidentService.findById(accidentId);
       if(accident==null){
           return  new CommonResponse(false,"事故不存在");
       }
        request.getSession().setAttribute("accident",accident);
       return new CommonResponse(true,"设置成功");
    }

    @GetMapping("/accident/getInsuranceInfoByCarMark")
    @ApiOperation("通过车牌号码查询保单信息")
    public CommonResponse getInsuranceInfoByCarMark(@RequestParam(value = "carMark",required = true)String carMark,@RequestParam(value = "carType",required = true) String carType){
        InsuranceInfoResponse info = accidentService.getInsuranceInfo(carType, carMark);
        if(info==null){return new CommonResponse(false,null);}
        return  new CommonResponse(true,info);
    }
}
