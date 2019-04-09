package com.ctf.traffic.controller.thirdparty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctf.traffic.auth.annotation.RequiredAuthentication;
import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.Accident.ProcessMode;
import com.ctf.traffic.po.Accident.ProcessStatus;
import com.ctf.traffic.po.AccidentMedia;
import com.ctf.traffic.po.AccidentMedia.BrokenParty;
import com.ctf.traffic.po.AccidentParty;
import com.ctf.traffic.po.AccidentParty.AccidentIndex;
import com.ctf.traffic.po.AccidentParty.Responsibility;
import com.ctf.traffic.po.AccidentReason;
import com.ctf.traffic.po.Clause;
import com.ctf.traffic.po.CommonResponse;
import com.ctf.traffic.po.DriverLicence;
import com.ctf.traffic.po.DrivingLicence;
import com.ctf.traffic.po.IllegalBehavior;
import com.ctf.traffic.po.InsuranceCar;
import com.ctf.traffic.po.response.AccidentBaseResponse;
import com.ctf.traffic.po.response.AgreementOwnResponse;
import com.ctf.traffic.po.response.AgreementOwnResponse.AgreementOwnParty;
import com.ctf.traffic.po.response.JurisdictionAreaResponse;
import com.ctf.traffic.po.response.PoliceResponse.PoliceAccidentPartResponse;
import com.ctf.traffic.po.response.PoliceResponse.PoliceDriverResponse;
import com.ctf.traffic.po.response.PoliceResponse.PoliceDrivingResponse;
import com.ctf.traffic.remote.RemoteInvoke;
import com.ctf.traffic.repository.AccidentReasonRepository;
import com.ctf.traffic.service.AccidentMediaService;
import com.ctf.traffic.service.AccidentPartyService;
import com.ctf.traffic.service.AccidentReasonCategoryService;
import com.ctf.traffic.service.AccidentReasonService;
import com.ctf.traffic.service.AccidentService;
import com.ctf.traffic.service.ClauseService;
import com.ctf.traffic.service.DriverLicenceService;
import com.ctf.traffic.service.DrivingLicenceService;
import com.ctf.traffic.service.IllegalBehaviorService;
import com.ctf.traffic.service.InsuranceCarService;
import com.ctf.traffic.service.JurisdictionAreaService;
import com.ctf.traffic.service.RoadCodeService;
import com.ctf.traffic.service.RoadSectionCodeService;
import com.ctf.traffic.service.RoadXzqhService;
import com.ctf.traffic.service.SubstationService;
import com.ctf.traffic.service.SysPersonService;
import com.ctf.traffic.service.WeatherService;
import com.ctf.traffic.util.ContextThreadLocal;
import com.ctf.traffic.util.ImageUtil;
import com.ctf.traffic.util.PathUtil.SavingFolder;
import com.ctf.traffic.util.QrcodeUtil;
import com.ctf.traffic.util.SerialNumberUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 7/1/2018
 * @see
 */
@RestController
@RequestMapping("/thirdparty/accident")
@Api(description = "交管项目交警端接口")
@Slf4j
public class TrafficPoliceController {
    @Resource
    private AccidentService accidentService;
    @Resource
    private SysPersonService personService;
    @Resource
    private AccidentMediaService accidentMediaService;
    @Resource
    private AccidentReasonService accidentReasonService;
    @Resource
    private AccidentReasonCategoryService categoryService;
    @Resource
    private ClauseService clauseService;
    @Resource
    private WeatherService weatherService;
    @Resource
    private InsuranceCarService insuranceCarService;
    @Resource
    private IllegalBehaviorService behaviorService;
    @Resource
    private AccidentPartyService accidentPartyService;
    @Resource
    private DrivingLicenceService drivingLicenceService;
    @Resource
    private DriverLicenceService driverLicenceService;
    @Resource
    private JurisdictionAreaService jurisdictionAreaService;
    @Resource
    private SubstationService substationService;
    @Resource
    private RoadXzqhService roadXzqhService;
    @Resource
    private RoadCodeService roadCodeService;
    @Resource
    private RoadSectionCodeService roadSectionCodeService;
    @Resource
    private AccidentReasonRepository reasonRepository;
    @Resource
    private AccidentReasonCategoryService accidentReasonCategoryService;

    @GetMapping("/{accidentId}")
    @RequiredAuthentication
    @ApiOperation("交警端根据事故获取事故信息")
    public CommonResponse getBaseInfo(@PathVariable("accidentId") Integer accidentId,
                                      @RequestParam(value = "data", required = false) String data,
                                      @RequestParam(value = "code", required = false) String code,
                                      @RequestParam(value = "signed", required = false) String signed) {

        Accident accident = accidentService.findById(accidentId);
        if (accident == null) {
            return new CommonResponse(false, "事故不存在");
        }
        /**事故基本信息*/
        AccidentBaseResponse accidentBaseResponse = new AccidentBaseResponse();
        accidentBaseResponse.setId(accident.getId());
        accidentBaseResponse.setSerialNumber(accident.getSerialNumber());
        accidentBaseResponse.setOccurredTime(accident.getOccurredTime());
        accidentBaseResponse.setLocation(accident.getLocation());
        accidentBaseResponse.setWeather(accident.getWeather());
        if (accident.getSysPerson() != null) {/**重新认定时会有的字段*/
            accidentBaseResponse.setPoliceName(accident.getSysPerson().getName());
            accidentBaseResponse.setPoliceSignaturePic(accident.getSysPerson().getSignaturePic());
            accidentBaseResponse.setPoliceSubstation(accident.getSysPerson().getSubstation().getName());
            accidentBaseResponse.setConductCenter(accident.getConductCenter().getName());
            accidentBaseResponse.setAccindetReasonCategory(accident.getAccidentReason()==null?"":accident.getAccidentReason().getAccidentReasonCategory().getName());
        }
        /**事故照片*/
        accidentBaseResponse.setAccidentMedias(AccidentMedia.toJson(accidentMediaService.findByAccident(accidentId)));
        /**事故当事人*/
        List<PoliceAccidentPartResponse> partResponses = new ArrayList<>();
        accidentPartyService.findByAccidentId(accident.getId()).forEach(p -> {
            PoliceAccidentPartResponse r = new PoliceAccidentPartResponse();
            r.setId(p.getId());
            r.setName(p.getName());
            r.setAccidentIndex(AccidentIndex.getDesc(p.getAccidentIndex()));
            r.setIllegalBehavior(p.getIllegalBehavior());
            r.setClause(p.getClause());
            /**驾驶证*/
            DriverLicence driverLicence = driverLicenceService.findByAccidentPartyId(p.getId());
            PoliceDriverResponse driverResponse = new PoliceDriverResponse();
            driverResponse.setFindbySixToOne(driverLicence.getNotcustom());
            driverResponse.setIdNumber(driverLicence.getNumber());
            driverResponse.setName(driverLicence.getName());
            driverResponse.setPhone(driverLicence.getPhone());
            driverResponse.setPicUrl(driverLicence.getPicUrl());
            r.setDriver(driverResponse);
            /**行驶证*/
            DrivingLicence drivingLicence = drivingLicenceService.findByAccidentPartyId(p.getId());
            PoliceDrivingResponse drivingResponse = new PoliceDrivingResponse();
            drivingResponse.setFindbySixToOne(drivingLicence.getNotcustom());
            drivingResponse.setPicUrl(drivingLicence.getPicUrl());
            drivingResponse.setCarNumber(drivingLicence.getNumber());
            drivingResponse.setType(drivingLicence.getType());
            drivingResponse.setCarOwner(drivingLicence.getName());
            InsuranceCar insuranceCar = insuranceCarService.findByDrivingLicence(drivingLicence.getId());
            drivingResponse.setInsuranceCompany(insuranceCar.getInsuranceCompany());
            drivingResponse.setInsuranceCompulsory(insuranceCar.getInsuranceCompulsory());
            drivingResponse.setInsuranceDate(new SimpleDateFormat("yyyy-MM-dd").format(insuranceCar.getInsuranceDate()));
            r.setDriving(drivingResponse);

            partResponses.add(r);
        });
        accidentBaseResponse.setAccidentParties(partResponses);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("accident",accidentBaseResponse);
        log.warn(" TrafficPoliceController.getBaseInfo : 获取事故信息返回[{}]", jsonObject.toString());

        return new CommonResponse(true,jsonObject);

/*     修改前
        CommonResponse result;
        Accident accident = accidentService.findById(accidentId);
        //传来的policeId后台没有使用，而是后台直接通过事故获取的
        if (accident != null) {
            AccidentBaseResponse response = new AccidentBaseResponse();
            result = Optional.ofNullable(accident.getSysPerson()).map(person -> {
                response.setPoliceName(person.getName());
                final Substation substation = person.getSubstation();
                if (substation != null) {
                    response.setPoliceSubstation(substation.getName());
                }
                response.setPoliceSignaturePic(person.getSignaturePic());
                return new CommonResponse(true, "调用成功");
            }).orElse(new CommonResponse(false, "事故未分配交警"));
            if (!result.isResult())
                return result;
            response.setId(accidentId);
            response.setSerialNumber(accident.getSerialNumber());
            response.setOccurredTime(accident.getOccurredTime());
            response.setLocation(accident.getLocation());
            response.setWeather(accident.getWeather());
            response.setWeathers(weatherService.findAllAvailable());
            //response.setAccidentReason(accident.getAccidentReason().getAccidentReasonCategory().getName());
            response.setAccidentMedias(AccidentMedia.toJson(accidentMediaService.findByAccident(accidentId)));
            List<AccidentPartyResponse> accidentPartyResponses = new ArrayList<>();
            accidentPartyService.findByAccidentId(accidentId).forEach(accidentParty -> {
                DrivingLicence drivingLicence = drivingLicenceService.findByAccidentPartyId(accidentParty.getId());
                DriverLicence driverLicence = driverLicenceService.findByAccidentPartyId(accidentParty.getId());
                InsuranceCar insuranceCar = insuranceCarService.findByDrivingLicence(drivingLicence.getId());
                accidentPartyResponses.add(new AccidentPartyResponse(accidentParty.getId(), accidentParty.getName(),
                        AccidentIndex.getDesc(accidentParty.getAccidentIndex()), insuranceCar.getInsuranceCompany(),
                        insuranceCar.getInsuranceCompulsory(), insuranceCar.getInsuranceDate(), drivingLicence,
                        driverLicence));
            });
            response.setAccidentParties(accidentPartyResponses);
            log.debug(" ThirdAccidentController.getBaseInfo : [{}]", response);
            return new CommonResponse(true, response);
        }
        return new CommonResponse(false, "事故信息不存在");*/
    }

    @GetMapping("/listAccidentReason")
    @RequiredAuthentication
    @ApiOperation("交警端获取所有可选事故原因")
    public CommonResponse getAccidentReasonList(@RequestParam(value = "data", required = false) String data,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "signed", required = false) String signed) {
        log.error(" TrafficPoliceController.getAccidentReasonList : [{交警端获取所有可选事故原因}]", data);
        log.error(" TrafficPoliceController.getAccidentReasonList : [{交警端获取所有可选事故原因}]",  accidentReasonService.findByCategoryId(0));
        return new CommonResponse(true, accidentReasonService.findByCategoryId(0));
    }

    @GetMapping("/listClauseAndIllegalBehavior/{categoryId}")
    @RequiredAuthentication
    @ApiOperation("交警端根据事故原因类型获取所有对应法律条款和违法行为")
    public CommonResponse getClauseAndIllegalBehaviorList(@PathVariable("categoryId") long categoryId,
                                                          @RequestParam(value = "data", required = false) String data,
                                                          @RequestParam(value = "code", required = false) String code,
                                                          @RequestParam(value = "signed", required = false) String signed) {
        log.warn(" TrafficPoliceController.getClauseAndIllegalBehaviorList : [{}]", categoryId);
        log.warn(" TrafficPoliceController.getClauseAndIllegalBehaviorList : [{}]", data);

        JSONObject jsonObject = new JSONObject();
        List<AccidentReason> reasons = reasonRepository.findByAccidentReasonCategoryIdAndState(categoryId, 1);
        List<Clause> clauses = new ArrayList<>();
        List<IllegalBehavior> illegalBehaviors = new ArrayList<>();
        List<AccidentReason> reasonss = new ArrayList<>();
        reasons.forEach(reason -> {
            if (reason.getState() == AccidentReason.STATE_VALID) {
                reasonss.add(reason);
                clauses.addAll(clauseService.findByAccidentReasonId(reason.getId()));
                illegalBehaviors.addAll(behaviorService.findByAccidentReasonId(reason.getId()));
            }
        });
        jsonObject.put("reasons", reasonss);//reason中维护了行为代码与法律条款的关系
        jsonObject.put("clauses", clauses);
        jsonObject.put("illegalBehaviors", illegalBehaviors);

        log.warn(" TrafficPoliceController.getClauseAndIllegalBehaviorList : [{}]", new CommonResponse(true, jsonObject));
        return new CommonResponse(true, jsonObject);
    }

    @GetMapping("/listJurisdictionArea")
    @RequiredAuthentication
    @ApiOperation("事故地点联动")
    public CommonResponse listJurisdictionArea(@RequestParam(value = "data", required = false) String data,
                                               @RequestParam(value = "code", required = false) String code,
                                               @RequestParam(value = "signed", required = false) String signed) {
        JSONObject dataObj = JSON.parseObject(RemoteInvoke.base64Decode(ContextThreadLocal.getData()));
        log.warn(" TrafficPoliceController.listJurisdictionArea : [{}]", dataObj);

        Long pid = dataObj.getLong("pid");
        List<JurisdictionAreaResponse> responses = new ArrayList<>();
        jurisdictionAreaService.findByParent(pid).forEach(area -> responses
                .add(new JurisdictionAreaResponse(area, area.getParent() != null ? area.getParent().getId() : -1)));

        log.warn(" TrafficPoliceController.listJurisdictionArea : [{}]", new CommonResponse(true, responses));
        return new CommonResponse(true, responses);
    }

    @PostMapping("/offlineDeal")
    @RequiredAuthentication
    @ApiOperation("线下处理事故，并完成事故")
    public CommonResponse offlineDealAndFinish(@RequestParam(value = "data", required = false) String data,
                                               @RequestParam(value = "code", required = false) String code,
                                               @RequestParam(value = "signed", required = false) String signed) {
        JSONObject dataObj = JSON.parseObject(RemoteInvoke.base64Decode(ContextThreadLocal.getData()));
        log.warn(" TrafficPoliceController.offlineDealAndFinish : [{}]", dataObj);

        Integer accidentId = dataObj.getInteger("accidentId");
        if (accidentId == null) {
            return new CommonResponse(false, "'accidentId' 未传递");
        }
        Accident acc = accidentService.findById(accidentId);
        if (acc == null) {
            return new CommonResponse(false, "事故信息不存在");
        }
        acc.setProcessMode(ProcessMode.OFFLINE.ordinal());
        acc.setProcessStatus(ProcessStatus.DEALED.ordinal());
        boolean result = accidentService.saveOrUpdate(acc).getId() > 0;

        log.warn(" TrafficPoliceController.offlineDealAndFinish : [{}]", new CommonResponse(result, result ? "转为线下处理成功" : "转为线下处理失败"));
        return new CommonResponse(result, result ? "转为线下处理成功" : "转为线下处理失败");
    }

    @PostMapping("/accidentPartyAbsence")
    @RequiredAuthentication
    @ApiOperation("当事人不在场")
    public CommonResponse absense(@RequestParam(value = "data", required = false) String data,
                                  @RequestParam(value = "code", required = false) String code,
                                  @RequestParam(value = "signed", required = false) String signed) {
        JSONObject dataObj = JSON.parseObject(RemoteInvoke.base64Decode(ContextThreadLocal.getData()));
        log.warn(" TrafficPoliceController.absense : [{}]", dataObj);

        Integer accidentId = dataObj.getInteger("accidentId");
        if (accidentId == null) {
            return new CommonResponse(false, "'accidentId' 未传递");
        }
        Accident acc = accidentService.findById(accidentId);
        if (acc == null) {
            return new CommonResponse(false, "事故信息不存在");
        }
        acc.setProcessStatus(ProcessStatus.DEAL_CANCEL.ordinal());
        boolean result = accidentService.saveOrUpdate(acc).getId() > 0;

        log.warn(" TrafficPoliceController.absense : [{}]", new CommonResponse(result, result ? "已处理,当事人不在场" : "当事人不在场,处理失败"));
        return new CommonResponse(result, result ? "已处理,当事人不在场" : "当事人不在场,处理失败");
    }

    @GetMapping("/getNextProcessAccidentParty")
    @RequiredAuthentication
    @ApiOperation("交警端获取下一组待调解事故当事人信息")
    public CommonResponse getNextProcessAccidentParty(@RequestParam(value = "data", required = false) String data,
                                                      @RequestParam(value = "code", required = false) String code,
                                                      @RequestParam(value = "signed", required = false) String signed) {
        JSONObject dataObj = JSON.parseObject(RemoteInvoke.base64Decode(ContextThreadLocal.getData()));

        log.warn(" TrafficPoliceController.getNextProcessAccidentParty : [{}]", dataObj);
        Long conductCenterId = dataObj.getLong("conductCenterId");
        if (conductCenterId == null) {
            return new CommonResponse(false, "'conductCenterId' 未传递");
        }
        List<AccidentParty> accidentParties = accidentService.getNextProcessAccidentParty(conductCenterId);
        JSONObject jo = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        accidentParties.forEach(accidentParty -> {
            JSONObject jsonObject = new JSONObject();
            DrivingLicence drivingLicence = drivingLicenceService.findByAccidentPartyId(accidentParty.getId());
            jsonObject.put("name", accidentParty.getName());
            jo.put("number", accidentParty.getAccident().getSeqNumber());
            jsonObject.put("seqNumber", accidentParty.getAccident().getSeqNumber());
            if (drivingLicence != null)
                jsonObject.put("carNumber", drivingLicence.getNumber());
            jsonArray.add(jsonObject);
        });
        jo.put("parties", jsonArray);

        log.warn(" TrafficPoliceController.getNextProcessAccidentParty : [{}]", new CommonResponse(true, jo));
        return new CommonResponse(true, jo);
    }

    @GetMapping("/getAllWeather")
    @RequiredAuthentication
    @ApiOperation("交警端获取所有的天气信息")
    public CommonResponse getAllWeather() {
        return new CommonResponse(true, weatherService.findAllAvailable());
    }

    @PostMapping("/{accidentId}")
    @RequiredAuthentication
    @ApiOperation("交警端保存认定结果")
    public CommonResponse saveResult(@PathVariable("accidentId") long id,
                                     @RequestParam(value = "data", required = false) String data,
                                     @RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "signed", required = false) String signed) {

        CommonResponse response = accidentService.savePoliceResult(id, RemoteInvoke.base64Decode(ContextThreadLocal.getData()));
        return response;
    }

    @PostMapping("/saveAgreementPic/{accidentId}")
    @RequiredAuthentication
    @ApiOperation("交警端保存认定书图片")
    public CommonResponse saveAgreementPic(@PathVariable("accidentId") long id,
                                           @RequestParam(value = "data", required = false) String data,
                                           @RequestParam(value = "code", required = false) String code,
                                           @RequestParam(value = "signed", required = false) String signed) {

        Accident accident = accidentService.findById(id);
        JSONObject dataObj = JSON.parseObject(RemoteInvoke.base64Decode(ContextThreadLocal.getData()));
        String agreementPicUrl = dataObj.getString("agreementPicUrl");
        accident.setAgreementPicUrl(agreementPicUrl);
        boolean result = accidentService.saveOrUpdate(accident).getId() > 0;
        CommonResponse response = new CommonResponse(result, result ? "保存成功" : "保存失败");
        log.warn(" TrafficPoliceController.saveAgreementPic : [{}]", response);
        return response;
    }

    @GetMapping("/cognizance/{id}")
    @ApiOperation("认定书信息")
    public CommonResponse cognizance(@PathVariable("id") long id,
                                     @RequestParam(value = "data", required = false) String data,
                                     @RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "signed", required = false) String signed) {
        log.warn(" TrafficPoliceController.cognizance : [{id}]", id);
        log.warn(" TrafficPoliceController.cognizance : [{}]", data);
        Accident accident;
        if (data == null || (accident = accidentService.findById(id)) == null) {
            return new CommonResponse(false, "参数错误");
        }
        AgreementOwnResponse response = new AgreementOwnResponse();
        response.setTime(accident.getOccurredTime());
        response.setLocation(accident.getLocation());
        response.setWeather(accident.getWeather());
        response.setAccidentReason(accident.getAccidentReason().getAccidentReasonCategory().getName());
        response.setCoordinationResult(accident.getCoordinationResult());
        response.setInFactResponsibility(accident.getInFactResponsibility());
        response.setPolice(accident.getSysPerson().getName());
        List<AgreementOwnParty> parties = new ArrayList<>();
        accidentPartyService.findByAccidentId(id).forEach(accidentParty -> {
            AgreementOwnParty agreementParty = new AgreementOwnParty();
            DrivingLicence drivingLicence = drivingLicenceService.findByAccidentPartyId(accidentParty.getId());
            agreementParty.setClause(accidentParty.getClause());
            agreementParty.setIllegalBehavior(accidentParty.getIllegalBehavior());
            agreementParty.setDrivingNumber(drivingLicence.getNumber());
            agreementParty
                    .setDriverNumber(driverLicenceService.findByAccidentPartyId(accidentParty.getId()).getNumber());
            agreementParty.setName(accidentParty.getName());
            agreementParty.setPhone(accidentParty.getPhone());
            agreementParty.setAccidentIndex(AccidentIndex.getDesc(accidentParty.getAccidentIndex()));
            agreementParty.setResponsibility(Responsibility.getDesc(accidentParty.getResponsibility()));
            agreementParty.setCarType(drivingLicence.getType());
            InsuranceCar insuranceCar = insuranceCarService.findByDrivingLicence(drivingLicence.getId());
            agreementParty.setInsuranceCompany(insuranceCar.getInsuranceCompany());
            agreementParty.setInsuranceDate(insuranceCar.getInsuranceDate());
            agreementParty.setInsuranceCompulsory(insuranceCar.getInsuranceCompulsory());
            agreementParty.setCaseNumber(insuranceCar.getCaseNumber());
            agreementParty.setIllegalBehavior(accidentParty.getIllegalBehavior());
            agreementParty.setClause(accidentParty.getClause());
            Set<String> brokenParts = new HashSet<>();
            accidentMediaService.findByDrivingLicence(drivingLicence.getId())
                    .forEach(accidentMedia -> brokenParts.add(BrokenParty.getDesc(accidentMedia.getPart())));
            agreementParty.setBrokenParts(brokenParts.toString());
            agreementParty.setAccidentReason(accident.getAccidentReason().getAccidentReasonCategory().getName());
            parties.add(agreementParty);
        });
        response.setAccidentParties(parties);
        JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(response));
        jsonObject.put("reasonCategories", categoryService.findAll());

        CommonResponse response1 = new CommonResponse(response.getAccidentParties().size() > 1, jsonObject);
        log.warn(" TrafficPoliceController.cognizance : [{}]", response1);
        return response1;
    }

    @PostMapping("/uploadFile")
    @RequiredAuthentication
    @ApiOperation("交警端文件上传")
    public CommonResponse uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                     @RequestParam(value = "data", required = false) String data,
                                     @RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "signed", required = false) String signed) {
        CommonResponse response = new CommonResponse(true, ImageUtil.save(multipartFile, null, SavingFolder.ACCIDENT));
        log.warn(" TrafficPoliceController.uploadFile : [{}]", response);
        return response;
    }

    @PostMapping("/saveAgreementPicUrl/{accidentId}")
    @ApiOperation("保存认定书地址")
    public CommonResponse saveAgreementPicUrl(@PathVariable("accidentId") long accidentId,
                                              @RequestParam(value = "data", required = false) String data,
                                              @RequestParam(value = "code", required = false) String code,
                                              @RequestParam(value = "signed", required = false) String signed) {
        JSONObject dataObj = JSON.parseObject(RemoteInvoke.base64Decode(ContextThreadLocal.getData()));
        log.warn(" TrafficPoliceController.saveAgreementPicUrl : [{}]", accidentId);
        log.warn(" TrafficPoliceController.saveAgreementPicUrl : [{}]", dataObj);

        final Accident accident = accidentService.findById(accidentId);
        String agreementPicUrl = dataObj.getString("agreementPicUrl");
        accident.setAgreementPicUrl(agreementPicUrl);
        boolean result = accidentService.saveOrUpdate(accident).getId() > 0;

        CommonResponse response = new CommonResponse(result, result ? "保存成功" : "保存失败");
        log.warn(" TrafficPoliceController.saveAgreementPicUrl : [{}]", response);
        return response;
    }

    @PostMapping("/sixToOne/{accidentId}")
    @ApiOperation("保存事故信息到六合一接口")
    public CommonResponse saveAccidentToSixToOne(@PathVariable("accidentId") long accidentId) {
        return new CommonResponse(true, accidentService.saveAccidentToSixToOne(accidentId));
    }

    @PostMapping("/sixToOne/queryAccidentWriteInfo")
    @ApiOperation("通过六合一接口的Id从六合一接口获取写入事故状态")
    public CommonResponse queryAccidentWriteInfo(@RequestParam("accidentWriteId") String accidentWriteId) {
        return new CommonResponse(true, accidentService.queryAccidentWriteInfo(accidentWriteId));
    }

    @GetMapping("/sixToOne/list")
    @ApiOperation("从六合一接口通过身份证查询事故信息")
    public CommonResponse listAccidentFromSixToOne(@RequestParam("idNumber") String idNumber) {
        return new CommonResponse(true, accidentService.listAccidentFromSixToOne(idNumber));
    }

    @GetMapping("/cognizanceQrcode/{accidentId}")
    @ApiOperation("认定书二维码")
    public void phoneQrcode(@PathVariable("accidentId") long accidentId, HttpServletResponse response)
            throws Exception {
        log.warn(" TrafficPoliceController.phoneQrcode : [{}]", accidentId);

        Accident accident = accidentService.findById(accidentId);
        response.setHeader("Pragma", "Pragma");
        response.setContentType("image/jpeg");
        ImageIO.write(QrcodeUtil.QRCodeCreate(SerialNumberUtil.parseToBase64(accident.getId()+accident.getSerialNumber()),200, 200), "png", response.getOutputStream());
    }

    @GetMapping("/listXzqh")
    @RequiredAuthentication
    @ApiOperation("获取所有行政区划")
    public CommonResponse listXzqh(@RequestParam(value = "data", required = false) String data,
                                   @RequestParam(value = "code", required = false) String code,
                                   @RequestParam(value = "signed", required = false) String signed) {
    	
        CommonResponse response = new CommonResponse(true, roadXzqhService.findAllAvailable());
        log.warn(" TrafficPoliceController.listXzqh : [{}]", response);
        return response;
    }

    @GetMapping("/listDldmByXzqh")
    @RequiredAuthentication
    @ApiOperation("通过行政区划获取道路代码")
    public CommonResponse listDldmByXzqh(@RequestParam(value = "data", required = false) String data,
                                         @RequestParam(value = "code", required = false) String code,
                                         @RequestParam(value = "signed", required = false) String signed) {

        JSONObject dataObj = JSON.parseObject(RemoteInvoke.base64Decode(ContextThreadLocal.getData()));

        log.warn(" TrafficPoliceController.listDldmByXzqh : [{}]", dataObj);
        String xzqh = dataObj.getString("xzqh");


        CommonResponse response = new CommonResponse(true, roadCodeService.findByXzqh(xzqh));
        log.warn(" TrafficPoliceController.listDldmByXzqh : [{}]", response);
        return response;
    }

    @GetMapping("/listLddmByDldm")
    @RequiredAuthentication
    @ApiOperation("通过道路代码获取路口路段代码")
    public CommonResponse listLddmByDldm(@RequestParam(value = "data", required = false) String data,
                                         @RequestParam(value = "code", required = false) String code,
                                         @RequestParam(value = "signed", required = false) String signed) {
        JSONObject dataObj = JSON.parseObject(RemoteInvoke.base64Decode(ContextThreadLocal.getData()));

        log.warn(" TrafficPoliceController.listLddmByDldm : [{}]", dataObj);
        String xzqh = dataObj.getString("xzqh");
        String dldm = dataObj.getString("dldm");
        String ldmc = dataObj.getString("ldmc");
        CommonResponse response = new CommonResponse(true, roadSectionCodeService.findByDldm(xzqh, dldm, ldmc));
        log.warn(" TrafficPoliceController.listLddmByDldm : [{}]", response);
        return response;
    }

}
