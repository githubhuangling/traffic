package com.ctf.traffic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Repository
public interface DrivingLicenceRepository extends JpaRepository<DrivingLicence, Long>{
    DrivingLicence findByAccidentParty(AccidentParty accidentParty);

    int deleteByAccidentPartyAccident(Accident accident);
}
