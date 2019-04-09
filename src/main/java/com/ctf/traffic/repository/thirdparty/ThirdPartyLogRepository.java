package com.ctf.traffic.repository.thirdparty;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 7/1/2018
 * @see
 */
@Repository
public interface ThirdPartyLogRepository extends JpaRepository<ThirdPartyLog, Long>{
}
