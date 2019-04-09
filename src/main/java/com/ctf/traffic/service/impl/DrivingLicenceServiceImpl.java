package com.ctf.traffic.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ctf.traffic.po.*;
import com.ctf.traffic.repository.DrivingLicenceRepository;
import com.ctf.traffic.service.AccidentPartyService;
import com.ctf.traffic.service.DrivingLicenceService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Service
@Slf4j
public class DrivingLicenceServiceImpl implements DrivingLicenceService{
    @Resource
    private DrivingLicenceRepository repository;
    @Resource
    private AccidentPartyService accidentPartyService;

    @Transactional
    @Override
    public int delete(Accident accident) {
        return repository.deleteByAccidentPartyAccident(accident);
    }

    @Transactional
    @Override
    public DrivingLicence saveOrUpdate(DrivingLicence drivingLicence) {
        return repository.saveAndFlush(drivingLicence);
    }

    @Override
    public DrivingLicence findById(long id) {
        return repository.getOne(id);
    }

    @Override
    public DrivingLicence findByAccidentPartyId(long partyId) {
        return repository.findByAccidentParty(new AccidentParty(partyId));
    }

    @Override
    public List<DrivingLicence> findByAccidentId(Long accidentId) {
        List<DrivingLicence> drivingLicences = new ArrayList<>();
        List<AccidentParty> accidentParties = accidentPartyService.findByAccidentId(accidentId);
        if (accidentParties.size() < 1)
            return new ArrayList<>();
        accidentParties.forEach(accidentParty -> {
            log.debug(" DrivingLicenceServiceImpl.findByAccidentId : [{}]", accidentParty.getId());
            drivingLicences.add(repository.findByAccidentParty(accidentParty));
        });
        return drivingLicences;
    }
}
