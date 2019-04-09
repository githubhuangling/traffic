package com.ctf.traffic.util;

import com.ctf.traffic.po.*;
import java.util.*;
import javax.annotation.*;
import lombok.extern.slf4j.*;
import org.springframework.jdbc.core.*;
import org.springframework.scheduling.annotation.*;

/**
 * @author jiangmin
 * @Date 2018/10/23
 * @see
 */
@Slf4j
//@Component
public class InsuranceRetrieveFailResend {
    @Resource
    JdbcTemplate jdbcTemplate;

    @Scheduled(initialDelay=10000,fixedRate = 20000)
    public void resend() {



        log.info(" InsuranceRetrieveFailResend.resend : 复写字段 [{}]");
        List<Accident> accidents = jdbcTemplate.query("select * from accident a where a.state=1 and a.process_status!=-1 and a.driver_names is null limit 500", new BeanPropertyRowMapper<>(Accident.class));


        if(accidents.size()==0){
            log.info(" InsuranceRetrieveFailResend.resend : [{复写已完成}]");

            return;
        }
        accidents.forEach(accident->{
            StringBuilder driverNames=new StringBuilder();
            StringBuilder driverNumbers=new StringBuilder();
            StringBuilder carOwnerNames=new StringBuilder();
            StringBuilder carMarks=new StringBuilder();

            List<AccidentParty> accidentParties = jdbcTemplate.query("select * from accident_party a where a.accident_id=" + accident.getId(), new BeanPropertyRowMapper<>(AccidentParty.class));
            accidentParties.forEach(party -> {
                List<DriverLicence> driverList = jdbcTemplate.query("select * from driver_licence a where a.accident_party_id=" + party.getId(), new BeanPropertyRowMapper<>(DriverLicence.class));
                List<DrivingLicence> drivingrList = jdbcTemplate.query("select * from driving_licence a where a.accident_party_id=" + party.getId(), new BeanPropertyRowMapper<>(DrivingLicence.class));
                if(driverList.size()==1&&drivingrList.size()==1){
                    DriverLicence driver=driverList.get(0);
                    DrivingLicence driving=drivingrList.get(0);
                    driverNames.append(","+driver.getName());
                    driverNumbers.append(","+driver.getNumber());
                    carOwnerNames.append(","+driving.getName());
                    carMarks.append(","+driving.getNumber());
                }
            });
            if(driverNames.length()!=0&& driverNumbers.length()!=0 && carOwnerNames.length()!=0 && carMarks.length()!=0){
                jdbcTemplate.update("update accident a set driver_names='"+driverNames.substring(1)+"',driver_numbers='"+driverNumbers.substring(1)+"', car_owner_names='"+carOwnerNames.substring(1)+"', car_marks='"+carMarks.substring(1)+"' where a.id="+accident.getId());
            }else {
                log.error(accident.getId()+"无驾驶证行驶证信息");
            }


        });

        /*accidents.forEach(a->{
            List<AccidentParty> aps = jdbcTemplate.query("select ap.id from accident_party ap where ap.accident_id='" + a.getId() + "'", new BeanPropertyRowMapper<>(AccidentParty.class));
            aps.forEach(ap->{
                List<DriverLicence> dr = jdbcTemplate.query("select * from driver_licence dr where dr.accident_party_id='" + ap.getId() + "'", new BeanPropertyRowMapper<>(DriverLicence.class));
                a.setDriverNames(a.getDriverNames()==null?dr.get(0).getName():a.getDriverNames()+","+dr.get(0).getName());
                a.setDriverNumbers(a.getDriverNumbers()==null?dr.get(0).getNumber():a.getDriverNumbers()+","+dr.get(0).getNumber());
                List<DrivingLicence> dg = jdbcTemplate.query("select * from driving_licence dg where dg.accident_party_id='" + ap.getId() + "'", new BeanPropertyRowMapper<>(DrivingLicence.class));
                a.setCarOwnerNames(a.getCarOwnerNames()==null?dg.get(0).getName():a.getCarOwnerNames()+","+dg.get(0).getName());
                a.setCarMarks(a.getCarMarks()==null?dg.get(0).getNumber():a.getCarMarks()+","+dg.get(0).getNumber());
            });
            accidentService.saveOrUpdate(a);
        });*/
    }

}
