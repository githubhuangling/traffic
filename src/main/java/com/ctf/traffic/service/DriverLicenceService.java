package com.ctf.traffic.service;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.DriverLicence;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
public interface DriverLicenceService{

    int delete(Accident accident);

    DriverLicence saveOrUpdate(DriverLicence driverLicence);

    DriverLicence findByAccidentPartyId(long accidentPartyId);

}
