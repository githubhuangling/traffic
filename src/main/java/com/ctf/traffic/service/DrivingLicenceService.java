package com.ctf.traffic.service;

import java.util.List;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.DrivingLicence;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
public interface DrivingLicenceService{
    int delete(Accident accident);

    DrivingLicence saveOrUpdate(DrivingLicence drivingLicence);

    DrivingLicence findById(long id);

    DrivingLicence findByAccidentPartyId(long partyId);

    List<DrivingLicence> findByAccidentId(Long accidentId);

}
