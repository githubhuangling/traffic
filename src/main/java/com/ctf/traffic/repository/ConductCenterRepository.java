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
public interface ConductCenterRepository
        extends JpaRepository<ConductCenter, Long>, JpaSpecificationExecutor<ConductCenter>{
    List<ConductCenter> findByState(Integer state);

    ConductCenter findByCodeLike(String code);
}
