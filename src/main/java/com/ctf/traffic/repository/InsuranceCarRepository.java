package com.ctf.traffic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ctf.traffic.po.InsuranceCar;

/**
 * @author ramer
 * @Date 7/10/2018
 * @see
 */
@Repository
public interface InsuranceCarRepository extends JpaRepository<InsuranceCar, Long>{
    List<InsuranceCar> findByDrivingLicenceId(long id);

    int deleteByDrivingLicenceAccidentPartyAccidentId(long accidentId);
}
