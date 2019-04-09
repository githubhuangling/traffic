package com.ctf.traffic.service;

import java.util.List;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.AccidentParty;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
public interface AccidentPartyService{
    int delete(Accident accident);

    AccidentParty saveOrUpdate(AccidentParty accidentParty);

    AccidentParty findById(long id);

    List<AccidentParty> findByAccidentId(long id);
}
