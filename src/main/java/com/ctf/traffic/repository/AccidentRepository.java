package com.ctf.traffic.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.Constant;
import com.ctf.traffic.po.CountGroupCounductCenterReceive;
import com.ctf.traffic.po.CountGroupReceive;
import com.ctf.traffic.po.DrivingLicence;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Repository
public interface AccidentRepository extends JpaRepository<Accident, Long>{
    /*----------------------------------------------------------------------------报表数据统计 START--------------------------------------------------------------------------------*/

    /**
     * 获取当日所有中心的处理事故总数
     * @param sDate
     * @param eDate
     * @return
     */
    @Query(value = "select count(*)  from accident where process_mode <>-1 and  process_status >0 and state <>-1 and create_time between :sDate AND  :eDate",nativeQuery = true)
    Integer countTodayAccident(@Param("sDate") String sDate,@Param("eDate")String eDate);

    /**
     * 获取当日所有中心的处理事故--在线处理
     * @param sDate
     * @param eDate
     * @return
     */
    @Query(value = "SELECT COUNT(*)   FROM `accident` WHERE `process_mode` <>-1 AND  `process_status` >0 AND `sys_person_id` IS NOT NULL AND `state` <>-1 AND `create_time` BETWEEN  :sDate AND  :eDate",nativeQuery = true)
    Integer countTodayAccidentByOnline(@Param("sDate") String sDate,@Param("eDate")String eDate);

    /**
     * 获取当日所有中心的处理事故-----线下处理
     * @param sDate
     * @param eDate
     * @return
     */
    @Query(value = "SELECT COUNT(*)     FROM `accident`WHERE `process_mode` = 2 AND  `process_status` =2 AND `sys_person_id` IS NOT NULL AND `state` <>-1 AND `create_time` BETWEEN  :sDate AND  :eDate",nativeQuery = true)
    Integer countTodayAccidentByOutline(@Param("sDate") String sDate,@Param("eDate")String eDate);

    /**
     *获取当日所有中心的处理事故-----线上处理事故分类
     * @param sDate
     * @param eDate
     * @return
     */
    @Query(value = "SELECT COUNT(`process_status`) as countnum,`process_status` as processStatus   FROM accident  " +
            "WHERE `process_mode` <>-1 AND  `process_status` >0 AND `sys_person_id` IS NOT NULL  " +
            "AND `state` <>-1 AND `create_time` BETWEEN  :sDate AND  :eDate " +
            "GROUP BY `process_status`",nativeQuery = true)
    Collection<CountGroupReceive> countTodayAccidentByGroup(@Param("sDate") String sDate,@Param("eDate")String eDate);

    /**
     * 各中心当日处理事故总数
     * @param sDate
     * @param eDate
     * @return
     *///
    @Query(value = "SELECT c.id, count(a.id) as num FROM conduct_center c LEFT OUTER JOIN accident a ON c.id = a.conduct_center_id AND a.process_mode <>- 1 AND a.process_status > 0 AND a.state <>- 1 AND a.create_time BETWEEN :sDate AND :eDate GROUP BY c.id ORDER BY count(a.id) DESC",nativeQuery = true)
    Collection<CountGroupCounductCenterReceive> countTodayAllAccidentGroupByConductCenter(@Param("sDate") String sDate,@Param("eDate")String eDate);

    @Query(value = "SELECT c.id,count(a.id) as num FROM conduct_center c LEFT OUTER JOIN accident a  ON c.id = a.conduct_center_id  AND a.process_mode <>- 1 AND a.process_status > 0 AND a.sys_person_id IS NOT NULL AND a.state <>- 1 AND a.create_time  BETWEEN :sDate AND :eDate GROUP BY c.id",nativeQuery = true)
    Collection<CountGroupCounductCenterReceive> countTodayOnlineAccidentGroupByConductCenter(@Param("sDate") String sDate,@Param("eDate")String eDate);



/*----------------------------------------------------------------------------报表数据统计 END--------------------------------------------------------------------------------------------*/

    /**
     * 获取所有分局待解决的所有事故.
     *
     * @return
     */

/*    @Query(value = "select *from accident a where a.agreement_pic_url is null and a.seq_number is not null and a.substation_id is not null and a.process_status in(0,1) and update_time > :lastDate  and a.state="
            + Constant.STATE_ON, nativeQuery = true)
    List<Accident> findPending(@Param("lastDate") Date lastDate);*/
    @Query(value = "select *from accident a where  a.seq_number is not null and a.substation_id is not null and a.process_status in(0,1) and update_time > :lastDate  and a.state="
            + Constant.STATE_ON, nativeQuery = true)
    List<Accident> findPending(@Param("lastDate") Date lastDate);

    Page<Accident> findByStateEqualsAndProcessStatusIsInAndSysPersonIdAndUpdateTimeBetween(int state,
            List<Integer> status, long syspersonId, Date startDate, Date endDate, Pageable pageable);

    /**
     * 获取当前交警处理完成事故,以时间过滤.
     *
     * @return
     */
    // TODO-POST: 获取当前交警处理完成事故,以时间过滤.
    @Query(value = "select *from accident a where a.agreement_pic_url is null and a.seq_number is not null and a.substation_id is not null and a.process_status in(0,1)  and a.state="
            + Constant.STATE_ON, nativeQuery = true)
    List<Accident> findProcessedBySysperonIdAndDate(@Param("sysperonId") long sysperonId, @Param("") Date date);

    /**
     * 获取所有可用状态的事故.满足给定处理方式并且小于指定处理状态
     *
     * @param status
     * @param processMode
     * @param state
     * @return
     */
    List<Accident> findByProcessStatusEqualsAndProcessModeEqualsAndStateEqualsOrderByUpdateTimeDesc(Integer status,
            Integer processMode, Integer state);

    Accident findFirstByConductCenterIdAndProcessModeAndProcessStatusAndStateAndCreateTimeBetweenOrderBySeqNumberAsc(Long conductCenterId,int processMode,int processStatus, int state,Date sDate,Date eDate);

    /**
     * 通过身份证查询事故信息.
     *
     * @param idNumber
     * @param pageable
     * @return
     */
    //    @Query(value = "SELECT DISTINCT a from DriverLicence dl join dl.accidentParty ap join ap.accident as a where a.processMode<>-1 and a.processStatus=0 and dl.number= :idNumber order by a.updateTime desc")
    @Query(value = "SELECT DISTINCT a from DriverLicence dl join dl.accidentParty ap join ap.accident as a where a.processMode<>-1 and a.state="
            + Constant.STATE_ON + " and dl.number= :idNumber order by a.updateTime desc")
    List<Accident> findByIdNumber(@Param("idNumber") String idNumber, Pageable pageable);

    //    @Query(value = "select *from accident where (unix_timestamp(create_time)+id)= :idStr",nativeQuery = true)
    @Query(value = "select *from accident where id= :idStr and state=" + Constant.STATE_ON, nativeQuery = true)
    Accident findByEncId(@Param("idStr") long id);

    /**
     * 获取所有不完整的事故信息.
     * @return
     */
    @Query(value = "select new Accident(id) from Accident  where  processStatus<1")
    List<Accident> findRedundantAccidents();
    
    /**
     * 根据时间获取所有事故发生地点.
     * @return
     */
    @Query(value = "select longitude,latitude from Accident  where processMode <>-1 and processStatus<>-1 and occurredTime between :startDate and :endDate")
    List<Object> findAccidentPoint(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    

    /**
     * 根据事故id查询车辆列表.
     * @return
     */
    @Query(value = "select dl from DrivingLicence dl join dl.accidentParty ap join ap.accident a where a.id=:accidentId")
    List<DrivingLicence> findDrivingLicenceByAccidentId(@Param("accidentId") Long accidentId);
    
    /**
     * 根据车牌号及时间查询事故列表.
     * @return
     */
    @Query(value = "select a from DrivingLicence dl join dl.accidentParty ap join ap.accident a where a.occurredTime>:startDate and dl.number=:carmark and a.processMode <> -1 and a.processStatus <> -1")
    List<Accident> findAccidentByDrivingLicence(@Param("carmark") String carmark,@Param("startDate") Date startDate);
}
