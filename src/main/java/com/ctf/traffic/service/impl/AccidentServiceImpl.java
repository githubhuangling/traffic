package com.ctf.traffic.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.*;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.Accident.ProcessMode;
import com.ctf.traffic.po.Accident.ProcessStatus;
import com.ctf.traffic.po.AccidentMedia;
import com.ctf.traffic.po.AccidentParty;
import com.ctf.traffic.po.AccidentParty.AccidentIndex;
import com.ctf.traffic.po.AccidentParty.Responsibility;
import com.ctf.traffic.po.AccidentReason;
import com.ctf.traffic.po.CommonResponse;
import com.ctf.traffic.po.ConductCenter;
import com.ctf.traffic.po.Constant;
import com.ctf.traffic.po.DriverLicence;
import com.ctf.traffic.po.DrivingLicence;
import com.ctf.traffic.po.InsuranceCar;
import com.ctf.traffic.po.Substation;
import com.ctf.traffic.po.response.AccidentDetailsResponse;
import com.ctf.traffic.po.response.AccidentMediaResponse;
import com.ctf.traffic.po.response.AccidentPartyDetailsResponse;
import com.ctf.traffic.po.response.AccidentPendingResponse;
import com.ctf.traffic.po.response.AccidentSimpleDetailsResponse;
import com.ctf.traffic.po.response.CountGroupCounductCenterResponse;
import com.ctf.traffic.po.sys.SysConf;
import com.ctf.traffic.remote.RemoteInvoke;
import com.ctf.traffic.remote.request.SavingDutySimpleRequest;
import com.ctf.traffic.remote.request.SavingDutySimpleRequest.DutySimple;
import com.ctf.traffic.remote.request.SavingDutySimpleRequest.DutysimplehuMan;
import com.ctf.traffic.remote.response.AccidentWriteInfoResponse;
import com.ctf.traffic.remote.response.InsuranceInfoResponse;
import com.ctf.traffic.remote.response.QueryDriverResponse;
import com.ctf.traffic.remote.response.QueryDutySimpleResponse;
import com.ctf.traffic.remote.response.QueryVehicleResponse;
import com.ctf.traffic.remote.response.SavingDutySimpleResponse;
import com.ctf.traffic.repository.AccidentReasonRepository;
import com.ctf.traffic.repository.AccidentRepository;
import com.ctf.traffic.service.AccidentMediaService;
import com.ctf.traffic.service.AccidentPartyService;
import com.ctf.traffic.service.AccidentReasonCategoryService;
import com.ctf.traffic.service.AccidentReasonService;
import com.ctf.traffic.service.AccidentService;
import com.ctf.traffic.service.ClauseService;
import com.ctf.traffic.service.ConductCenterService;
import com.ctf.traffic.service.DriverLicenceService;
import com.ctf.traffic.service.DrivingLicenceService;
import com.ctf.traffic.service.IllegalBehaviorService;
import com.ctf.traffic.service.InsuranceCarService;
import com.ctf.traffic.service.SubstationService;
import com.ctf.traffic.service.SysConfService;
import com.ctf.traffic.util.ContextThreadLocal;
import com.ctf.traffic.util.HttpUtils;
import com.ctf.traffic.util.SpatialRelationUtil;
import com.ctf.traffic.util.SpatialRelationUtil.Point;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Service
@Slf4j
public class AccidentServiceImpl implements AccidentService {
    @Resource
    private AccidentRepository repository;
    @Value("${com.ctf.traffic.six-to-one-url}")
    private String SIX_TO_ONE_URL;
    @Value("${com.ctf.traffic.insurance.url}")
    private String INSURANCE_URL;
    @Value("${com.ctf.traffic.insurance.code}")
    private String INSURANCE_CODE;
    @Value("${com.ctf.traffic.insurance.secret}")
    private String INSURANCE_SECRET;
    @Resource
    private AccidentReasonRepository reasonRepository;
    @Resource
    private AccidentPartyService accidentPartyService;
    @Resource
    private SubstationService substationService;
    @Resource
    private SysConfService sysConfService;
    @Resource
    private DrivingLicenceService drivingLicenceService;
    @Resource
    private DriverLicenceService driverLicenceService;
    @Resource
    private ClauseService clauseService;
    @Resource
    private IllegalBehaviorService illegalBehaviorService;
    @Resource
    private AccidentMediaService accidentMediaService;
    @Resource
    private AccidentReasonCategoryService accidentReasonCategoryService;
    @Resource
    private AccidentService accidentService;
    @Resource
    private InsuranceCarService insuranceCarService;
    @Resource
    private ConductCenterService conductCenterService;
    @Resource
    private AccidentRepository accidentRepository;
    @Resource
    private EntityManager em;
    @Resource
    private AccidentReasonService accidentReasonService;
    /**
     * 协调中心下一个排队号
     */
    private Map<Long, Integer> seqNumberMap = Collections.synchronizedMap(new HashMap<>());
    private static final Long SEQ_KEY = 111111111111111l;

    @Transactional
    @Override
    public Accident saveOrUpdate(Accident accident) {
        return repository.saveAndFlush(accident);
    }

    @Override
    public CommonResponse getDatastatistics(String sDate, String eDate) {
        //当日受理事故总数
        Integer all = accidentRepository.countTodayAccident(sDate, eDate);
        if (all == null) {
            all = 0;
        }

        //当日线上处理事故
        Integer online = accidentRepository.countTodayAccidentByOnline(sDate, eDate);
        if (online == null) {
            online = 0;
        }
        //当时线下处理事故
        Integer outline = accidentRepository.countTodayAccidentByOutline(sDate, eDate);
        if (outline == null) {
            outline = 0;
        }

        //线上事故处理分类
        Map<Integer, Integer> map = new HashMap<>();
        accidentRepository.countTodayAccidentByGroup(sDate, eDate).forEach(a -> {
            map.put(a.getProcessStatus() == null ? 0 : a.getProcessStatus(),
                    a.getCountnum() == null ? 0 : a.getCountnum());
        });
        //各中心当日处理事故总数
        List<CountGroupCounductCenterResponse> allAccidentCountGroupByCounductCenterId = new ArrayList<>();
        accidentRepository.countTodayAllAccidentGroupByConductCenter(sDate, eDate).forEach(a -> {
            allAccidentCountGroupByCounductCenterId.add(new CountGroupCounductCenterResponse(conductCenterService.findById(a.getId()).getName(), a.getNum() == null ? 0 : a.getNum()));
        });


        //各中心当日处理在线事故
        List<CountGroupCounductCenterResponse> onlineNoOrder = new ArrayList<>();
        accidentRepository.countTodayOnlineAccidentGroupByConductCenter(sDate, eDate).forEach(a -> {
            onlineNoOrder.add(new CountGroupCounductCenterResponse(conductCenterService.findById(a.getId()).getName(), a.getNum() == null ? 0 : a.getNum()));
        });

        List<CountGroupCounductCenterResponse> onlineAccidentCountGroupByCounductCenterId = new ArrayList<>();
        allAccidentCountGroupByCounductCenterId.forEach(a -> {
            onlineNoOrder.forEach(b -> {
                if (a.getConductCenter().equals(b.getConductCenter())) {
                    onlineAccidentCountGroupByCounductCenterId.add(b);
                }
            });
        });


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("allnum", all);//所有
        jsonObject.put("zxxs", all - online);
        jsonObject.put("zxxcl", outline);
        if (map.size() == 0) {
            jsonObject.put("zzcl", 0);
            jsonObject.put("clwc", 0);
            jsonObject.put("bzxc", 0);
            jsonObject.put("wcnum", 0);
        } else {
            jsonObject.put("zzcl", map.get(1) == null ? 0 : map.get(1));
            jsonObject.put("clwc", map.get(2) == null ? 0 : map.get(2));
            jsonObject.put("bzxc", map.get(3) == null ? 0 : map.get(3));
            jsonObject.put("wcnum", (map.get(2) == null ? 0 : map.get(2)) - outline);
        }
        jsonObject.put("allGroupByCenter", allAccidentCountGroupByCounductCenterId);
        jsonObject.put("onlineGroupByCenter", onlineAccidentCountGroupByCounductCenterId);
        return new CommonResponse(true, jsonObject);
    }

    @Override
    public QueryDriverResponse getNameByIdNumber(String idNumber) {
        QueryDriverResponse response = HttpUtils.request(SIX_TO_ONE_URL.concat("query/queryDriver"), new HashMap() {
            {
                put("sfzmhm", idNumber);
            }
        }, RequestMethod.POST, QueryDriverResponse.class);
        log.info(" AccidentServiceImpl.getNameByIdNumber : 六合一根据身份证号查询驾驶证信息[{}]", response);
        return idNumber == null ? null : response;
    }

    @Override
    public QueryVehicleResponse getNameByCarTypeAndNumber(String carType, String carNumber) {
        QueryVehicleResponse response = HttpUtils.request(SIX_TO_ONE_URL.concat("query/queryVehicle"), new HashMap() {
            {
                put("hphm", carNumber);
                put("hpzl", carType);
            }
        }, RequestMethod.POST, QueryVehicleResponse.class);
        log.info(" AccidentServiceImpl.getNameByCarTypeAndNumber : [{}]", response);
        return carNumber == null ? null : response;
    }

    @Override
    public Accident findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public synchronized int assignSeqNumber(long accidentId) {
        Accident accident = findById(accidentId);
        if (accident == null){ return -1;}

        Long substationId = SpatialRelationUtil.getSubstation(substationService.findAll(),
                new Point(StringUtils.isEmpty(accident.getLongitude())?"104.07":accident.getLongitude(), StringUtils.isEmpty(accident.getLatitude())?"30.67":accident.getLatitude()));
        if (substationId == null)
            substationId = 1l;
        int seqNumber = Optional.ofNullable(seqNumberMap.get(SEQ_KEY))
                .map(number -> seqNumberMap.put(SEQ_KEY, number + 1)).orElseGet(() -> {
                    seqNumberMap.put(SEQ_KEY, 2);
                    return 1;
                });
        accident.setProcessStatus(ProcessStatus.TO_DEAL.ordinal());
        accident.setSeqNumber(seqNumber);
        accident.setProcessMode(ProcessMode.COORDINATE.ordinal());
        accident.setSubstation(new Substation(substationId));
        saveOrUpdate(accident);
        sysConfService.update(Constant.ACCIDENT_SEQ_NUMBER_CODE, String.valueOf(seqNumberMap.get(SEQ_KEY)));
        return seqNumber;
    }

    @Override
    public void resetSeqNumber() {
        seqNumberMap.clear();
        sysConfService.update(Constant.ACCIDENT_SEQ_NUMBER_CODE, String.valueOf(1));
    }

    @Override
    public void initSeqNumber() {
        SysConf seqNumber = sysConfService.findByCode(Constant.ACCIDENT_SEQ_NUMBER_CODE);
        log.info(" AccidentServiceImpl.initSeqNumber : [{}]", seqNumber);
        if (seqNumber != null) {
            seqNumberMap.put(SEQ_KEY, Integer.valueOf(seqNumber.getValue()));
        }
    }

    @Override
    public List<Accident> findAllPending() {
        try {
            return repository.findPending(
                    new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public synchronized String generateSerialNumber(long accidentId) {
        //        String time = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    public InsuranceInfoResponse getInsuranceInfo(String carType, String carNumber) {
        String url = INSURANCE_URL.concat("thirdparty/policy/queryInsurance");
        log.info(" AccidentServiceImpl.getInsuranceInfo : [{},{},{}]", carType, carNumber);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("carType", carType);
        jsonObject.put("carMark", carNumber);
        String data = RemoteInvoke.base64Encode(jsonObject.toJSONString());
        Map<String, String> paras = new HashMap<>();
        paras.put("code", INSURANCE_CODE);
        paras.put("data", data);
        paras.put("signed", RemoteInvoke.md5Encode(INSURANCE_SECRET.concat(data)));
        JSONObject request = HttpUtils.request(url, paras, RequestMethod.POST, JSONObject.class);
        if(request==null){return null;}
        if ((Boolean) request.get("success")) {
            JSONObject responseData = JSONObject.parseObject(request.get("data").toString());
            InsuranceInfoResponse response = new InsuranceInfoResponse();
            if (responseData != null) {
                response.setCode(responseData.getString("code"));
                response.setName(responseData.getString("name"));
                response.setPolicyNo(responseData.getString("policyNo"));
                response.setEndDate(responseData.getString("endDate"));
                return response;
            }
        }
        return null;
    }


    @Override
    public List<Accident> findByProcessStatus(Integer processStatus) {
        return repository.findByProcessStatusEqualsAndProcessModeEqualsAndStateEqualsOrderByUpdateTimeDesc(
                ProcessStatus.TO_DEAL.ordinal(), ProcessMode.COORDINATE.ordinal(), Constant.STATE_ON);
    }

    @Override
    public Accident saveReasonAndResponsibility(Accident accident, long reasonId, JSONArray responsibilityArr) {
        for (int i = 0; i < responsibilityArr.size(); i++) {
            JSONObject responsibilityObj = responsibilityArr.getJSONObject(i);
            Optional.ofNullable(accidentPartyService.findById(responsibilityObj.getLongValue("id"))).map(
                    accidentParty -> Optional.ofNullable(responsibilityObj.getInteger("responsibility")).map(resp -> {
                        accidentParty.setResponsibility(resp);
                        accidentPartyService.saveOrUpdate(accidentParty);
                        return accidentParty;
                    }));
        }
        accident.setAccidentReason(new AccidentReason(reasonId));
        accident.setSerialNumber(generateSerialNumber(accident.getId()));
        saveOrUpdate(accident);
        return accident;
    }

    @Override
    public List<AccidentPendingResponse> listPendingAccident() {
        List<Accident> accidents = findAllPending();
        if (accidents.size() < 1)
            return new ArrayList<>();
        List<AccidentPendingResponse> responses = new ArrayList<>();
        accidents.forEach(accident -> {
            AccidentPendingResponse response = new AccidentPendingResponse();
            response.setId(accident.getId());
            response.setSeqNumber(accident.getSeqNumber() == null ? 0 : accident.getSeqNumber());
            response.setConductCenterId(accident.getConductCenter().getId());
            response.setSysPersonId(accident.getSysPerson() == null ? "" : accident.getSysPerson().getId().toString());
            response.setProcessStatus(accident.getProcessStatus().toString());
            response.setSysPersonName(accident.getSysPerson() == null ? "" : accident.getSysPerson().getName());
            List<String> parties = new ArrayList<>();
            List<String> drivingNumbers = new ArrayList<>();
            accidentPartyService.findByAccidentId(accident.getId()).forEach(party -> {
                parties.add(party.getName());
                drivingNumbers.add(drivingLicenceService.findByAccidentPartyId(party.getId()).getNumber());
            });
            response.setAccidentParties(parties);
            ConductCenter conductCenter = conductCenterService.findById(accident.getConductCenter().getId());
            response.setConductCenter(conductCenter.getName());
            response.setTime(accident.getUpdateTime());
            response.setDrivingNumber(drivingNumbers);
            responses.add(response);
        });
        return responses;
    }

    @Override
    public Page<AccidentPendingResponse> listMyPendedAccident(Long id, String date, int page, int size)
            throws Exception {
        Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        Sort.Order order = Sort.Order.desc("updateTime");
        Sort sort = Sort.by(order);
        id = ContextThreadLocal.getPerson().getId();
        Page<Accident> time = accidentRepository
                .findByStateEqualsAndProcessStatusIsInAndSysPersonIdAndUpdateTimeBetween(1,
                        Arrays.asList(ProcessStatus.DEALED.ordinal(), ProcessStatus.DEAL_CANCEL.ordinal()), id, parse,
                        Date.from(parse.toInstant().plus(Duration.ofDays(1))), PageRequest.of(page - 1, size, sort));
        List<Accident> accidents = time.getContent();
        List<AccidentPendingResponse> responses = new ArrayList<>();
        accidents.forEach(accident -> {
            AccidentPendingResponse response = new AccidentPendingResponse();
            response.setId(accident.getId());
            response.setProcessMode(accident.getProcessMode().toString());
            response.setSeqNumber(accident.getSeqNumber() == null ? 0 : accident.getSeqNumber());
            response.setConductCenterId(accident.getConductCenter().getId());
            response.setSysPersonId(accident.getSysPerson() == null ? "" : accident.getSysPerson().getId().toString());
            response.setProcessStatus(accident.getProcessStatus().toString());
            List<String> parties = new ArrayList<>();
            List<String> drivingNumbers = new ArrayList<>();
            accidentPartyService.findByAccidentId(accident.getId()).forEach(party -> {
                parties.add(party.getName());
                drivingNumbers.add(drivingLicenceService.findByAccidentPartyId(party.getId()).getNumber());
            });
            response.setAccidentParties(parties);
            ConductCenter conductCenter = conductCenterService.findById(accident.getConductCenter().getId());
            response.setConductCenter(conductCenter.getName());
            response.setTime(accident.getUpdateTime());
            response.setDrivingNumber(drivingNumbers);
            responses.add(response);
        });
        PageImpl<AccidentPendingResponse> page1 = new PageImpl(responses, time.getPageable(), time.getTotalElements());
        return page1;
    }

    @Override
    public List<Accident> findByNumber(String number, int page, int size) {
        return repository.findByIdNumber(number, PageRequest.of(page - 1, size));
    }

    @Override
    public Accident findByEncId(long id) {
        return repository.findByEncId(id);
    }

    @Transactional
    @Override
    public CommonResponse savePoliceResult(long id, String data) {
        Accident accident;
        if (data == null || (accident = findById(id)) == null) {
            return new CommonResponse(false, "参数错误");
        }
        JSONObject dataObj = JSON.parseObject(data);
        log.info(" AccidentServiceImpl.savePoliceResult :保存认定结果： [{}]", dataObj.toString());

        String occurredTimeStr = dataObj.getString("occurredTime");
        String weather = dataObj.getString("weather");

        String ms = dataObj.getString("ms");
        String gls = dataObj.getString("gls");
        String lddm = dataObj.getString("lddm");
        String xzqh = dataObj.getString("xzqh");
        String dldm = dataObj.getString("dldm");
        Integer reasonId = dataObj.getInteger("reasonId");
        Integer processMode = dataObj.getInteger("processMode");
        String agreementPicUrl = dataObj.getString("agreementPicUrl");
        String coordinationResult = dataObj.getString("coordinationResult");
        String inFactResponsibility = dataObj.getString("inFactResponsibility");
        Date occurredTime;
        try {
            occurredTime = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(occurredTimeStr);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return new CommonResponse(false, "事故发生时间,格式错误");
        }
        accident.setOccurredTime(occurredTime);
        //        accident.setJurisdictionArea(new JurisdictionArea(locationId));
        if (lddm == null && gls == null) {
            return new CommonResponse(false, "路段代码为空时,公里数不能为空");
        }
        accident.setXzqh(xzqh);
        accident.setDldm(dldm);
        accident.setGls(gls);
        accident.setLddm(lddm);
        accident.setMs(ms);
        accident.setWeather(weather);
        if (!processMode.equals(ProcessMode.INDEPENDENT.ordinal())) {
            accident.setProcessMode(processMode);
        } else {
            accident.setProcessMode(ProcessMode.COORDINATE.ordinal());
        }
        Optional<AccidentReason> ro = reasonRepository.findById(Long.valueOf(reasonId));
        if (ro.isPresent())
            accident.setAccidentReason(ro.get());
        accident.setAgreementPicUrl(agreementPicUrl);
        accident.setCoordinationResult(coordinationResult);
        accident.setProcessStatus(ProcessStatus.DEALED.ordinal());
        accident.setInFactResponsibility(inFactResponsibility);
        accident.setSignaturePic(accident.getSysPerson().getSignaturePic());

        accidentService.saveOrUpdate(accident);
        //当事人
        JSONArray responsibilities = dataObj.getJSONArray("ext");
        for (int i = 0; i < responsibilities.size(); i++) {
            JSONObject respObj = responsibilities.getJSONObject(i);

            AccidentParty accidentParty = accidentPartyService.findById(respObj.getLongValue("accidentPartyId"));
            if (accidentParty != null) {
                Integer responsibility = respObj.getInteger("responsibility");
                accidentParty.setResponsibility(responsibility);

                if (!(responsibility == 2)) {//若无责任，则没有违法行为与法律条款 2代表无责任
                    Long behaviorId = respObj.getLong("illegalBehaviorId");
                    if (behaviorId != 0) {
                        accidentParty.setIllegalBehavior(illegalBehaviorService.findById(behaviorId).getBehavior());
                    }

                    Long clauseId = respObj.getLong("clauseId");
                    if (clauseId != 0) {
                        accidentParty.setClause(clauseService.findById(clauseId).getClause());
                    }
                }


                String signaturePic = respObj.getString("signaturePic");
                accidentParty.setSignaturePic(signaturePic);

                Long accidentReasonCategoryId = respObj.getLong("reasonCateId");//当事人碰撞形态
                if (accidentReasonCategoryId != 0) {
                    accidentParty.setAccidentReasonCategory(accidentReasonCategoryService.findById(accidentReasonCategoryId));
                }


                accidentPartyService.saveOrUpdate(accidentParty);

            } else {
                return new CommonResponse(false, "当事人不存在");
            }
        }
        final SavingDutySimpleResponse savingDutySimpleResponse = saveAccidentToSixToOne(accident.getId());
        if (savingDutySimpleResponse != null && "1".equals(savingDutySimpleResponse.getZdzt())) {
            String writeId = savingDutySimpleResponse.getId();
            if (writeId != null) {
                return new CommonResponse(true, writeId);
            }
        } else {
            final boolean result = accident.getId() > 0;
            return new CommonResponse(result, result ? "保存成功,保存到六合一失败" : "保存失败");
        }
        return new CommonResponse(false, "参数不正确");
    }

    @Override
    public QueryDutySimpleResponse listAccidentFromSixToOne(String idNumber) {
        QueryDutySimpleResponse response = HttpUtils.request(SIX_TO_ONE_URL.concat("query/queryDutysimple"),
                new HashMap() {
                    {
                        put("sfzmhm", idNumber);
                    }
                }, RequestMethod.POST, QueryDutySimpleResponse.class);
        log.info(" AccidentServiceImpl.listAccidentFromSixToOne : [{}]", response);
        return response;
    }

    @Override
    public SavingDutySimpleResponse saveAccidentToSixToOne(long accidentId) {
        /** 事故*/
        final Accident accident = findById(accidentId);
        SavingDutySimpleRequest dutySimpleRequest = new SavingDutySimpleRequest();
        DutySimple dutySimple = new DutySimple();
        /**  行政区划*/
        dutySimple.setXzqh(accident.getXzqh());
        /**  事故发生时间*/
        //dutySimple.setSgfssj(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(accident.getOccurredTime()));
        dutySimple.setSgfssj(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(accident.getOccurredTime()));
        /** 道路代码 */
        //dutySimple.setLh("95168");
        dutySimple.setLh(accident.getDldm());
        /** 路名 */
        //dutySimple.setLm("简阳市东三路(壮溪-宏缘)");
        dutySimple.setLm(accident.getLocation());
        /**  公里数*/
        dutySimple.setGls(accident.getGls());
        /**  米数*/
        dutySimple.setMs(accident.getMs());
        /**  事故地点*/
        dutySimple.setSgdd(accident.getLocation());
        /**  受伤人数*///  无法确定 统一传0
        dutySimple.setSsrs("0");
        /**  直接财产损失*///  无法确定 统一传0
        dutySimple.setZjccss("0");
        /** 天气 */ //  无法确定 统一传0
        dutySimple.setTq("0");
        /**  道路类型*///  无法确定 统一传0
        dutySimple.setDllx("0");
        /**  公路行政等级*///  无法确定
        dutySimple.setGlxzdj("0");
        /**  事故形态*/ //只能两个字，截取的“让行行为”前两个字
        if (null != accident.getAccidentReason()) {
            dutySimple.setSgxt(accident.getAccidentReason().getBehavior().substring(0, 2));
        } else {
            dutySimple.setSgxt("行为");
        }

        /**  车辆间事故*/
        dutySimple.setCljsg(accidentPartyService.findByAccidentId(accidentId).size() + "");
        /** 调解人 */
        dutySimple.setTjr(accident.getSysPerson().getName());
        /** 管理部门 */
        dutySimple.setGlbm(accident.getSysPerson().getSubstation().getId() + "");
        /** 事故事实 */
        dutySimple.setSgss(accident.getInFactResponsibility());
        /** 责任调解结果 */
        dutySimple.setZrtjjg(accident.getCoordinationResult());
        /** 办案人1 */
        dutySimple.setJar1(accident.getSysPerson().getName());
        /** 结案方式 */ //无法确定 统一传0
        dutySimple.setJafs("0");
        /**  调解方式*/ //无法确定 统一传0
        dutySimple.setTjfs("0");

        List<DutysimplehuMan> dutysimplehuMEN = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);
        /**  登记编号*/ //无法确定
        dutySimple.setDjbh("");
        /**  事故编号*/
        dutySimple.setSgbh("");
        /**  文书编号*/ //无法确定
        dutySimple.setWsbh("");
        /** 事故事实 */ //无法确定
        dutySimple.setSgss("无");
        /** 责任调解结果 *///无法确定
        dutySimple.setZrtjjg("0");
        /** 办案人2 *///无法确定
        dutySimple.setJar2("");
        /** 经办人 *///无法确定
        dutySimple.setJbr("");

        accidentPartyService.findByAccidentId(accidentId).forEach(accidentParty -> {
            DutysimplehuMan dutysimplehuMan = new DutysimplehuMan();
            /**人员编号，多人编号不能重复  */
            dutysimplehuMan.setRybh(String.valueOf(index.getAndIncrement()));
            /** 姓名*/
            dutysimplehuMan.setXm(accidentParty.getName());

            DriverLicence driverLicence = driverLicenceService.findByAccidentPartyId(accidentParty.getId());
            /** 性别 */
            String sex = driverLicence.getNumber().charAt(16) % 2 == 1 ? "女" : "男";
            dutysimplehuMan.setXb(sex);

            /**  身份证号码，多人身份证号不能重复*/
            dutysimplehuMan.setSfzmhm(driverLicence.getNumber());
            /** 电话 */
            dutysimplehuMan.setDh(accidentParty.getPhone());
            /**  违法行为代码1*///无法确定
            dutysimplehuMan.setWfxw1("");
            /**  交通方式*///无法确定
            dutysimplehuMan.setJtfs("0");
            /**  初次领证日期*/ //todo 无法确定 传入的当前时间
            dutysimplehuMan.setCclzrq(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            /**  事故责任*///todo  对方测试数据责任为0和1
            dutysimplehuMan.setSgzr(String.valueOf(accidentParty.getResponsibility()));
            /**  号牌号码，多人号牌号不能重复*/
            DrivingLicence drivingLicence = drivingLicenceService.findByAccidentPartyId(accidentParty.getId());
            dutysimplehuMan.setHphm(drivingLicence.getNumber());
            /**  号牌种类*/ //需确定对应关系
            dutysimplehuMan.setHpzl(drivingLicence.getType());
            /**  车辆品牌*///无法确定
            dutysimplehuMan.setClpp("");
            /**  车辆型号*///无法确定
            dutysimplehuMan.setClxh("");

            dutysimplehuMEN.add(dutysimplehuMan);
        });

        dutySimpleRequest.setDutysimple(dutySimple);
        dutySimpleRequest.setDutysimplehuMan(dutysimplehuMEN);
        String value = JSON.toJSONString(dutySimpleRequest);
        log.info(" AccidentServiceImpl.saveAccidentToSixToOne : [{}]", value);
        return HttpUtils.request(SIX_TO_ONE_URL.concat("insert/insertDutysimple"), new HashMap() {
            {
                put("dutysimple", value);
            }
        }, RequestMethod.POST, SavingDutySimpleResponse.class);
    }

    @Override
    public AccidentWriteInfoResponse queryAccidentWriteInfo(String accidentWriteId) {
        return HttpUtils.request(SIX_TO_ONE_URL.concat("insert/queryInState"), new HashMap() {
            {
                put("id", accidentWriteId);
            }
        }, RequestMethod.POST, AccidentWriteInfoResponse.class);
    }

    @Transactional
    @Override
    public CommonResponse saveAccidentPartSignature(long id, String signaturePic) {
        return Optional.ofNullable(accidentPartyService.findById(id)).map(party -> {
            party.setSignaturePic(signaturePic);
            accidentPartyService.saveOrUpdate(party);
            boolean result = party.getId() > 0;
            return new CommonResponse(result, result ? "保存成功" : "保存失败");
        }).orElse(new CommonResponse(false, "当事人信息不存在"));
    }

    @Override
    public List<AccidentParty> getNextProcessAccidentParty(Long conductCenterId) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();

        Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(new Date());
        calendar1.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date tomorrow = calendar1.getTime();

        final Accident accident = repository.findFirstByConductCenterIdAndProcessModeAndProcessStatusAndStateAndCreateTimeBetweenOrderBySeqNumberAsc(conductCenterId, ProcessMode.COORDINATE.ordinal(), ProcessStatus.TO_DEAL.ordinal(), 1, zero, tomorrow);
        if (accident == null) {
            return new ArrayList<>();
        }

        return accidentPartyService.findByAccidentId(accident.getId());
    }

    @Override
    public int deleteRedundantAccident() {
        AtomicInteger result = new AtomicInteger(0);
        accidentRepository.findRedundantAccidents().forEach(accident -> {
            log.info(" TestController.foo : delete[{}]", accident.getId());
            accidentMediaService.delete(accident);
            final int deleteInsuranceC = insuranceCarService.delete(accident);
            final int deleteDrivingL = drivingLicenceService.delete(accident);
            final int deleteDriverL = driverLicenceService.delete(accident);
            final int deleteAccidentP = accidentPartyService.delete(accident);
            accidentService.delete(accident);
            result.set(deleteInsuranceC + deleteDrivingL + deleteDriverL + deleteAccidentP);
        });
        return result.get();
    }

    @Override
    public void delete(Accident accident) {
        repository.delete(accident);
    }

    @Override
    public CommonResponse getAccidentDetails(String sDate, String eDate, String carNumber, String partyIdNumber, Long conductCenterId, Long accidentSyspersonId, Integer processMode, Integer processStatus) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select distinct a from Accident a where");

/*        stringBuilder.append("select distinct a from Accident a " +
                "join AccidentParty p on a.id=p.accident.id " +
                "join DrivingLicence dg on p.id=dg.accidentParty.id " +
                "join DriverLicence dr on  p.id=dr.accidentParty.id " +
                "and a.processMode<>-1 and a.processStatus<>-1 ");*/

        if (StringUtils.isEmpty(sDate)) {
            sDate = simpleDateFormat.format(new Date(0));
        } else {
            sDate += " 00:00:00";
        }
        if (StringUtils.isEmpty(eDate)) {
            eDate = simpleDateFormat.format(new Date());
        } else {
            eDate += " 23:59:59";
        }
        stringBuilder.append(" a.createTime between '" + sDate + "' and '" + eDate + "' ");
        stringBuilder.append("and a.state=1 and a.processMode!=-1 and a.processStatus!=-1 ");
        if (!StringUtils.isEmpty(processMode)) {
            stringBuilder.append("and a.processMode=" + processMode + " ");
        }
        if (!StringUtils.isEmpty(processStatus)) {
            stringBuilder.append("and a.processStatus=" + processStatus + " ");
        }
        if (!StringUtils.isEmpty(carNumber)) {
            stringBuilder.append("and a.carMarks like'%" + carNumber + "%' ");
        }
        if (!StringUtils.isEmpty(partyIdNumber)) {
            stringBuilder.append("and a.driverNumbers like '%" + partyIdNumber + "%' ");
        }
        if (!StringUtils.isEmpty(conductCenterId)) {
            stringBuilder.append("and a.conductCenter.id=" + conductCenterId + " ");
        }
        if (!StringUtils.isEmpty(accidentSyspersonId)) {
            stringBuilder.append("and a.sysPerson.id=" + accidentSyspersonId + " ");
        }
        stringBuilder.append("order by a.occurredTime desc ");

        Query query = em.createQuery(stringBuilder.toString());
        List<Accident> accidents = query.getResultList();
        List<AccidentSimpleDetailsResponse> accidentSimpleDetailsResponses = new ArrayList<>();
        accidents.forEach(a -> {
            AccidentSimpleDetailsResponse asdr = new AccidentSimpleDetailsResponse();
/*            String accidentPartySql = "select name,id from accident_party where accident_id=" + a.getId();
            Query accidentPartyQuery = em.createNativeQuery(accidentPartySql);
            List<Object[]> accidentParties = accidentPartyQuery.getResultList();
            List<String> parties = new ArrayList<>();
            List<String> carNumbers = new ArrayList<>();
            accidentParties.forEach(accidentParty -> {
                parties.add(accidentParty[0].toString());
                String drivingLicenceSql = "select number from driving_licence where accident_party_id=" + accidentParty[1];
                Query drivingLicenceQuery = em.createNativeQuery(drivingLicenceSql);
                carNumbers.add(drivingLicenceQuery.getSingleResult().toString());
            });*/
            asdr.setAccidentId(a.getId());
            asdr.setCreateTime(a.getCreateTime() == null ? "" : simpleDateFormat.format(a.getCreateTime()));
            asdr.setSerialNumber(a.getSerialNumber());
            asdr.setOccurredTime(a.getOccurredTime() == null ? "" : simpleDateFormat.format(a.getOccurredTime()));
            asdr.setProcessSysperson(a.getSysPerson() == null ? "-" : a.getSysPerson().getName());
            asdr.setConductCenterName(a.getConductCenter().getName());
            asdr.setProcessMode(ProcessMode.getDesc(a.getProcessMode()));
            try{
                asdr.setProcessStatus(ProcessStatus.getDesc(a.getProcessStatus()));
            }catch (Exception e){
                log.error(a.toString());
                log.error(e.getMessage(), e);
            }
            asdr.setParties(a.getDriverNames());
            asdr.setCarNumbers(a.getCarMarks());
            accidentSimpleDetailsResponses.add(asdr);
//            List<String> parties = new ArrayList<>();
//            List<String> carNumbers = new ArrayList<>();
//            accidentPartyService.findByAccidentId(a.getId()).forEach(p -> {
//                parties.add(p.getName());
//                carNumbers.add(drivingLicenceService.findByAccidentPartyId(p.getId()).getNumber());
//            });
        });
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accidents", accidentSimpleDetailsResponses);

        return new CommonResponse(true, jsonObject);

    }

    @Override
    public CommonResponse getAccidentDetailsById(Long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Accident a = accidentService.findById(id);
        AccidentDetailsResponse d = new AccidentDetailsResponse();
        d.setSerialNumber(a.getSerialNumber());
        d.setSgbh(a.getSgbh());
        d.setLocation(a.getLocation());
        d.setLongitude(a.getLongitude());
        d.setLatitude(a.getLatitude());
        d.setOccurredTime(a.getOccurredTime() == null ? "" : sdf.format(a.getOccurredTime()));
        d.setWeather(a.getWeather());
        if (a.getAccidentReason() != null && a.getAccidentReason().getBehavior() != null) {
            d.setAccidentReason(a.getAccidentReason().getBehavior());
        }
        d.setProcessMode(ProcessMode.getDesc(a.getProcessMode()));
        d.setProcessStatus(ProcessStatus.getDesc(a.getProcessStatus()));
        if (a.getSeqNumber() != null) {
            d.setSeqNumber(a.getSeqNumber().toString());
        }
        d.setSignaturePic(a.getSignaturePic());
        d.setAgreementPicUrl(a.getAgreementPicUrl());
        d.setInFactResponsibility(a.getInFactResponsibility());
        d.setCoordinationResult(a.getCoordinationResult());
        d.setXzqh(a.getXzqh());
        d.setDldm(a.getDldm());
        d.setLddm(a.getLddm());
        d.setGls(a.getGls());
        d.setMs(a.getMs());
        d.setSysPerson(a.getSysPerson() == null ? "" : a.getSysPerson().getName());
        d.setConductCenter(a.getConductCenter() == null ? "" : a.getConductCenter().getName());

        List<AccidentPartyDetailsResponse> accidentPartyDetailsResponses = new ArrayList<>();
        List<AccidentParty> parties = accidentPartyService.findByAccidentId(a.getId());
        parties.forEach(p -> {
            /*********  当事人  ******/
            AccidentPartyDetailsResponse r = new AccidentPartyDetailsResponse();
            r.setName(p.getName());
            r.setAccidentIndex(AccidentIndex.getDesc(p.getAccidentIndex()));
            r.setAccident(p.getAccident().getId());
            r.setReportNumber(p.getInsuranceReportNumber() == null ? "" : p.getInsuranceReportNumber());
            r.setPhone(p.getPhone());
            r.setResponsibility(Responsibility.getDesc(p.getResponsibility()));
            r.setIllegalBehavior(p.getIllegalBehavior() == null ? "" : p.getIllegalBehavior());
            r.setClause(p.getClause() == null ? "" : p.getClause());
            r.setSignaturePic(p.getSignaturePic());
            /************  驾驶证**********/
            DriverLicence driver = driverLicenceService.findByAccidentPartyId(p.getId());
            r.setDriverNotcustom(driver.getNotcustom());
            r.setDriverPicUrl(driver.getPicUrl());
            r.setDriverNumber(driver.getNumber());
            r.setDriverName(driver.getName());
            //r.setDriverGender(driver.getGender().toString());
            r.setDriverCountry(driver.getCountry());
            r.setDriverAddress(driver.getAddress());
            r.setDriverPhone(driver.getPhone());
            r.setDriverFirstGet(driver.getFirstGet() == null ? "" : sdf1.format(driver.getFirstGet()));
            r.setDriverValiDateFrom(driver.getValidDateFrom() == null ? "" : sdf1.format(driver.getValidDateFrom()));
            r.setDriverValiDateTo(driver.getValidDateTo() == null ? "" : sdf1.format(driver.getValidDateTo()));
            r.setDriverFileNumber(driver.getFileNumber());
            r.setDriverBirthDate(driver.getBirthDate() == null ? "" : sdf1.format(driver.getBirthDate()));
            /*************行驶证************/
            DrivingLicence driving = drivingLicenceService.findByAccidentPartyId(p.getId());
            r.setDrivingNotcustom(driving.getNotcustom());
            r.setDrivingPicUrl(driving.getPicUrl());
            r.setDrivingNumber(driving.getNumber());
            r.setDrivingType(driving.getType());
            r.setDrivingName(driving.getName());
            r.setDrivingAddress(driving.getAddress());
            r.setDrivingPhone(driving.getPhone());
            r.setDrivingFunctional(driving.getFunctional());
            r.setDrivingBrand(driving.getBrand());
            r.setDrivingIdentifyCode(driving.getIdentifyCode());
            r.setDrivingTransmitterNumber(driving.getTransmitterNumber());
            r.setDrivingRegisterDate(driving.getRegisterDate());
            r.setDrivingIssueDate(driving.getIssueDate());
            r.setDrivingFileNumber(driving.getFileNumber());
            r.setDrivingInspectExpired(driving.getInspectExpired());
            /***************  车辆保单**************/
            InsuranceCar l = insuranceCarService.findByDrivingLicence(driving.getId());
            r.setInsuranceCompany(l.getInsuranceCompany());
            r.setInsuranceCompanyCode(l.getInsuranceCompanyCode());
            r.setInsuranceCompulsory(l.getInsuranceCompulsory());
            r.setCaseNumber(l.getCaseNumber());
            r.setInsuranceDate(l.getInsuranceDate() == null ? "" : sdf1.format(l.getInsuranceDate()));
            /*******************************************/

            accidentPartyDetailsResponses.add(r);
        });
        d.setParties(accidentPartyDetailsResponses);


        Set<AccidentMediaResponse> responses = AccidentMedia.toJson(accidentMediaService.findByAccident(a.getId()));
        d.setAccidentMediaResponses(responses);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accidentDetailsById", d);

        return new CommonResponse(true, jsonObject);
    }

    @Override
    public List<Object> findAccidentPoint(Date startDate, Date endDate) {
        return repository.findAccidentPoint(startDate, endDate);
    }

    @Override
    public List<DrivingLicence> findDrivingLicenceByAccidentId(Long accidentId) {
        return repository.findDrivingLicenceByAccidentId(accidentId);
    }

    @Override
    public List<Accident> findAccidentByDrivingLicence(String carmark, Date startDate) {
        return repository.findAccidentByDrivingLicence(carmark, startDate);
    }

    @Override
    public CommonResponse insuranceGetAccidentDetailsInOneMonth(String carMark, String carType,String code) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,-30);
        Date monthAgo=calendar.getTime();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("select distinct a from Accident a " +
                "join AccidentParty p on a.id=p.accident.id " +
                "join DrivingLicence dg on p.id=dg.accidentParty.id "+
                "where a.occurredTime>'"+sdf1.format(monthAgo)+"' and dg.number='"+carMark+"' and dg.type='"+carType+"' ");
        Query query = em.createQuery(stringBuilder.toString());
        List<Accident> accidents = query.getResultList();
        log.info(" AccidentServiceImpl.insuranceGetAccidentDetailsInOneMonth : [{}]", accidents);

        AtomicBoolean checkCode= new AtomicBoolean(false);
        List<AccidentDetailsResponse> accidentDetailsResponses=new ArrayList<>();
        accidents.forEach(a->{
            /*判断查询公司与车辆保险公司是否一致*/
            List<AccidentParty> parties = accidentPartyService.findByAccidentId(a.getId());
            parties.forEach(p->{
                DrivingLicence driving = drivingLicenceService.findByAccidentPartyId(p.getId());
                InsuranceCar l = insuranceCarService.findByDrivingLicence(driving.getId());
                if(code.equals(l.getInsuranceCompanyCode())){
                    checkCode.set(true);
                }
            });
            AccidentDetailsResponse d = new AccidentDetailsResponse();
            if(checkCode.get()){
                d.setSerialNumber(a.getSerialNumber());
                d.setSgbh(a.getSgbh());
                d.setLocation(a.getLocation());
                d.setLongitude(a.getLongitude());
                d.setLatitude(a.getLatitude());
                d.setOccurredTime(a.getOccurredTime() == null ? "" : sdf.format(a.getOccurredTime()));
                d.setWeather(a.getWeather());
                if (a.getAccidentReason() != null && a.getAccidentReason().getBehavior() != null) {
                    d.setAccidentReason(a.getAccidentReason().getBehavior());
                }
                d.setProcessMode(ProcessMode.getDesc(a.getProcessMode()));
                d.setProcessStatus(ProcessStatus.getDesc(a.getProcessStatus()));
                if (a.getSeqNumber() != null) {
                    d.setSeqNumber(a.getSeqNumber().toString());
                }
                d.setSignaturePic(a.getSignaturePic());
                d.setAgreementPicUrl(a.getAgreementPicUrl());
                d.setInFactResponsibility(a.getInFactResponsibility());
                d.setCoordinationResult(a.getCoordinationResult());
                d.setXzqh(a.getXzqh());
                d.setDldm(a.getDldm());
                d.setLddm(a.getLddm());
                d.setGls(a.getGls());
                d.setMs(a.getMs());
                d.setSysPerson(a.getSysPerson() == null ? "" : a.getSysPerson().getName());
                d.setConductCenter(a.getConductCenter() == null ? "" : a.getConductCenter().getName());

                List<AccidentPartyDetailsResponse> accidentPartyDetailsResponses = new ArrayList<>();
                parties.forEach(p -> {
                    /*********  当事人  ******/
                    AccidentPartyDetailsResponse r = new AccidentPartyDetailsResponse();
                    r.setName(p.getName());
                    r.setAccidentIndex(AccidentIndex.getDesc(p.getAccidentIndex()));
                    r.setAccident(p.getAccident().getId());
                    r.setReportNumber(p.getInsuranceReportNumber() == null ? "" : p.getInsuranceReportNumber());
                    r.setPhone(p.getPhone());
                    r.setResponsibility(Responsibility.getDesc(p.getResponsibility()));
                    r.setIllegalBehavior(p.getIllegalBehavior() == null ? "" : p.getIllegalBehavior());
                    r.setClause(p.getClause() == null ? "" : p.getClause());
                    r.setSignaturePic(p.getSignaturePic());
                    /************  驾驶证**********/
                    DriverLicence driver = driverLicenceService.findByAccidentPartyId(p.getId());
                    r.setDriverNotcustom(driver.getNotcustom());
                    r.setDriverPicUrl(driver.getPicUrl());
                    r.setDriverNumber(driver.getNumber());
                    r.setDriverName(driver.getName());
                    //r.setDriverGender(driver.getGender().toString());
                    r.setDriverCountry(driver.getCountry());
                    r.setDriverAddress(driver.getAddress());
                    r.setDriverPhone(driver.getPhone());
                    r.setDriverFirstGet(driver.getFirstGet() == null ? "" : sdf1.format(driver.getFirstGet()));
                    r.setDriverValiDateFrom(driver.getValidDateFrom() == null ? "" : sdf1.format(driver.getValidDateFrom()));
                    r.setDriverValiDateTo(driver.getValidDateTo() == null ? "" : sdf1.format(driver.getValidDateTo()));
                    r.setDriverFileNumber(driver.getFileNumber());
                    r.setDriverBirthDate(driver.getBirthDate() == null ? "" : sdf1.format(driver.getBirthDate()));
                    /*************行驶证************/
                    DrivingLicence driving = drivingLicenceService.findByAccidentPartyId(p.getId());
                    r.setDrivingNotcustom(driving.getNotcustom());
                    r.setDrivingPicUrl(driving.getPicUrl());
                    r.setDrivingNumber(driving.getNumber());
                    r.setDrivingType(driving.getType());
                    r.setDrivingName(driving.getName());
                    r.setDrivingAddress(driving.getAddress());
                    r.setDrivingPhone(driving.getPhone());
                    r.setDrivingFunctional(driving.getFunctional());
                    r.setDrivingBrand(driving.getBrand());
                    r.setDrivingIdentifyCode(driving.getIdentifyCode());
                    r.setDrivingTransmitterNumber(driving.getTransmitterNumber());
                    r.setDrivingRegisterDate(driving.getRegisterDate());
                    r.setDrivingIssueDate(driving.getIssueDate());
                    r.setDrivingFileNumber(driving.getFileNumber());
                    r.setDrivingInspectExpired(driving.getInspectExpired());
                    /***************  车辆保单**************/
                    InsuranceCar l = insuranceCarService.findByDrivingLicence(driving.getId());
                    r.setInsuranceCompany(l.getInsuranceCompany());
                    r.setInsuranceCompanyCode(l.getInsuranceCompanyCode());
                    r.setInsuranceCompulsory(l.getInsuranceCompulsory());
                    r.setCaseNumber(l.getCaseNumber());
                    r.setInsuranceDate(l.getInsuranceDate() == null ? "" : sdf1.format(l.getInsuranceDate()));
                    /*******************************************/

                    accidentPartyDetailsResponses.add(r);
                });
                d.setParties(accidentPartyDetailsResponses);


                Set<AccidentMediaResponse> responses = AccidentMedia.toJson(accidentMediaService.findByAccident(a.getId()));
                d.setAccidentMediaResponses(responses);
                accidentDetailsResponses.add(d);
            }
        });
        if(!checkCode.get()){
            return new CommonResponse(false,"非本公司保险车辆，无法查询");
        }
        return new CommonResponse(true,accidentDetailsResponses);
    }

}
