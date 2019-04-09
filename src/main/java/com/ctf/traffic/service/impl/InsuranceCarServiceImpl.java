package com.ctf.traffic.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.InsuranceCar;
import com.ctf.traffic.repository.InsuranceCarRepository;
import com.ctf.traffic.service.InsuranceCarService;

/**
 * @author ramer
 * @Date 7/10/2018
 * @see
 */
@Service
public class InsuranceCarServiceImpl implements InsuranceCarService{
    @Resource
    private InsuranceCarRepository repository;

    @Transactional
    @Override
    public int delete(Accident accident) {
        return repository.deleteByDrivingLicenceAccidentPartyAccidentId(accident.getId());
    }

    @Override
    public InsuranceCar findByDrivingLicence(long drivingLicenceId) {
        List<InsuranceCar> insuranceCars = repository.findByDrivingLicenceId(drivingLicenceId);
        return insuranceCars.size() > 0 ? insuranceCars.get(0) : new InsuranceCar();
    }

    @Transactional
    @Override
    public InsuranceCar saveOrUpdate(InsuranceCar car) {
        return repository.saveAndFlush(car);
    }
}
