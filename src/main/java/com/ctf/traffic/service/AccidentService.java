package com.ctf.traffic.service;

import com.alibaba.fastjson.*;
import com.ctf.traffic.po.*;
import com.ctf.traffic.po.response.*;
import com.ctf.traffic.remote.response.*;
import java.util.*;
import org.springframework.data.domain.*;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
public interface AccidentService{
    Accident saveOrUpdate(Accident accident);

    /**
     * 获取当日事故报表
     * @param sDate"2018-01-01"
     * @param eDate"2018-01-02"
     * @return
     */
    CommonResponse getDatastatistics(String sDate,String eDate);

    /**
     * 通过身份证号获取姓名.
     * @param idNumber 
     * @return
     */
    QueryDriverResponse getNameByIdNumber(String idNumber);

    /**
     * 通过驾驶证号获取车主姓名.
     * @param carType
     * @param carNumber
     * @see com.ctf.traffic.po.DrivingLicence.CarType
     * @return
     */
    QueryVehicleResponse getNameByCarTypeAndNumber(String carType, String carNumber);

    Accident findById(long id);

    /**
     * 获取排队号.
     * @param accidentId
     * @return
     */
    int assignSeqNumber(long accidentId);

    /**
     * 定时重置排队号.
     */
    void resetSeqNumber();

    /**
     * 生成事故编号.
     * @return
     */
    String generateSerialNumber(long accidentId);

    List<Accident> findAllPending();

    InsuranceInfoResponse getInsuranceInfo(String carType,String carNumber);


    List<Accident> findByProcessStatus(Integer processStatus);

    Accident saveReasonAndResponsibility(Accident accident, long reasonId, JSONArray responsibilityArr);

    List<AccidentPendingResponse> listPendingAccident();

    Page<AccidentPendingResponse> listMyPendedAccident(Long id, String date, int page, int size) throws Exception;

    List<Accident> findByNumber(String number, int page, int size);

    Accident findByEncId(long id);

    /**
     * 交警端保存认定结果.
     * @param id
     * @param data
     * @return
     */
    CommonResponse savePoliceResult(long id, String data);

    /**
     * 从六合一接口查询事故信息.
     * @param idNumber
     * @return
     */
    QueryDutySimpleResponse listAccidentFromSixToOne(String idNumber);

    /**
     * 保存在线认定的事故信息到六合一接口.
     * @param accidentId
     * @return
     */
    SavingDutySimpleResponse saveAccidentToSixToOne(long accidentId);

    /**
     * 查询九合一接口事故写入状态.
     * @param id
     * @return
     */
    AccidentWriteInfoResponse queryAccidentWriteInfo(String id);

    CommonResponse saveAccidentPartSignature(long id, String signaturePic);

    /**
     * 获取下一组事故带调节的当事人.
     * @param conductCenterId
     * @return
     */
    List<AccidentParty> getNextProcessAccidentParty(Long conductCenterId);

    /**
     * 清除不完整的事故信息.
     */
    int deleteRedundantAccident();

    void delete(Accident accident);

    void initSeqNumber();

    /**
     * 管理端获取事故详情
     * @param sDate
     * @param eDate
     * @param carNumber
     * @param partyIdNumber
     * @param conductCenterId
     * @return
     */
    CommonResponse getAccidentDetails(String sDate,String eDate,String carNumber,String partyIdNumber,Long conductCenterId,Long accidentSyspersonId,Integer processMode,Integer processStatus);

    /**
     * 管理端通过事故id获取对应事故详情
     * @param id
     * @return
     */
    CommonResponse getAccidentDetailsById(Long id);
    

    /**
     * 根据时间获取所有事故发生地点.
     * @return
     */
    List<Object> findAccidentPoint(Date startDate, Date endDate);

    /**
     * 根据事故id查询车辆列表.
     * @return
     */
    List<DrivingLicence> findDrivingLicenceByAccidentId(Long accidentId);

    /**
     * 根据车牌号及时间查询事故列表.
     * @return
     */
    List<Accident> findAccidentByDrivingLicence(String carmark,Date startDate);


    /**
     * 保险公司根据车牌号、车辆类型获取近一个月内的事故信息
     */
    CommonResponse insuranceGetAccidentDetailsInOneMonth(String carMark,String carType,String code);


}
