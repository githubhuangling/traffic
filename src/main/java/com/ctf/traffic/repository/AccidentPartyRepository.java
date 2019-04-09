package com.ctf.traffic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.AccidentParty;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Repository
public interface AccidentPartyRepository extends JpaRepository<AccidentParty, Long>{
    int deleteByAccident(Accident accident);

    List<AccidentParty> findByAccident(Accident accident);

}
