package com.ctf.traffic.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long>, JpaSpecificationExecutor<Weather>{
}
