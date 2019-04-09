package com.ctf.traffic.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ctf.traffic.po.*;
import com.ctf.traffic.repository.DriverLicenceRepository;
import com.ctf.traffic.service.DriverLicenceService;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Service
public class DriverLicenceServiceImpl implements DriverLicenceService{
    @Resource
    private DriverLicenceRepository repository;

    @Transactional
    @Override
    public int delete(Accident accident) {
        return repository.deleteByAccidentPartyAccident(accident);
    }

    @Transactional
    @Override
    public DriverLicence saveOrUpdate(DriverLicence driverLicence) {
        return repository.saveAndFlush(driverLicence);
    }

    @Override
    public DriverLicence findByAccidentPartyId(long accidentPartyId) {
        return repository.findByAccidentParty(new AccidentParty(accidentPartyId));
    }
}
