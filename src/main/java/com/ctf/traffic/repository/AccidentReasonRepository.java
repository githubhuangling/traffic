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
public interface AccidentReasonRepository
        extends JpaRepository<AccidentReason, Long>, JpaSpecificationExecutor<AccidentReason>{
    List<AccidentReason> findByAccidentReasonCategoryIdAndState(Long id,Integer state);
}
