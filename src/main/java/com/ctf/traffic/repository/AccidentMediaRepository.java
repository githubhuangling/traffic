package com.ctf.traffic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.AccidentMedia;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Repository
public interface AccidentMediaRepository extends JpaRepository<AccidentMedia, Long>{

    void deleteByAccident(Accident accident);

    List<AccidentMedia> findByAccidentAndPart(Accident accident, int part);

    List<AccidentMedia> findByAccident(Accident accident);

    List<AccidentMedia> findByDrivingLicenceId(long id);
}
