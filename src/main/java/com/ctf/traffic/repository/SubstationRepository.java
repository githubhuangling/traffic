package com.ctf.traffic.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Repository
public interface SubstationRepository extends JpaRepository<Substation, Long>, JpaSpecificationExecutor<Substation>{
    List<Substation> findByState(Integer state);
}
