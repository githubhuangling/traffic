package com.ctf.traffic.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.AccidentParty;
import com.ctf.traffic.repository.AccidentPartyRepository;
import com.ctf.traffic.service.AccidentPartyService;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Service
public class AccidentPartyServiceImpl implements AccidentPartyService{
    @Resource
    private AccidentPartyRepository repository;

    @Transactional
    @Override
    public int delete(Accident accident) {
        return repository.deleteByAccident(accident);
    }

    @Transactional
    @Override
    public AccidentParty saveOrUpdate(AccidentParty accidentParty) {
        return repository.saveAndFlush(accidentParty);
    }

    @Override
    public AccidentParty findById(long id) {
        return repository.getOne(id);
    }

    @Override
    public List<AccidentParty> findByAccidentId(long id) {
        return repository.findByAccident(new Accident(id));
    }
}
