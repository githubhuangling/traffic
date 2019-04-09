package com.ctf.traffic.repository.thirdparty;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/29/2018
 * @see
 */
@Repository
public interface ThirdPartyCertificateRepository extends JpaRepository<ThirdPartyCertificate, Long>{
    ThirdPartyCertificate findByCode(String code);
}
