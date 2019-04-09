package com.ctf.traffic.service;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.InsuranceCar;

/**
 * @author ramer
 * @Date 7/10/2018
 * @see
 */
public interface InsuranceCarService{

    int delete(Accident accident);

    InsuranceCar findByDrivingLicence(long drivingLicenceId);

    InsuranceCar saveOrUpdate(InsuranceCar car);
}
