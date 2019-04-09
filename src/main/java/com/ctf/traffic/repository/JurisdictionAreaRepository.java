package com.ctf.traffic.repository;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Repository
public interface JurisdictionAreaRepository
        extends JpaRepository<JurisdictionArea, Long>, JpaSpecificationExecutor<JurisdictionArea>{
    List<JurisdictionArea> findByParentIdIsNull();

    List<JurisdictionArea> findByParentId(long id);

    List<JurisdictionArea> findBySubstation(Substation substation);

    Page<JurisdictionArea> findBySubstationAndState(Substation substation, int state, Pageable pageable);

    @Query(value = "select * from jurisdiction_area ja where ja.coordination_center_id is null and ja.state="
            + Constant.STATE_ON, nativeQuery = true)
    Page<JurisdictionArea> findExcludeByCenterId(Pageable pageable);

    @Query(value = "select * from jurisdiction_area ja where ja.coordination_center_id is null and name like %:name% and ja.state="
            + Constant.STATE_ON, nativeQuery = true)
    Page<JurisdictionArea> findExcludeByCenterId(@Param("name") String name, Pageable pageable);
}
