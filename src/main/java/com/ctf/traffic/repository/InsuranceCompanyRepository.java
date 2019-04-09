package com.ctf.traffic.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.*;


@Repository
public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Long>, JpaSpecificationExecutor<InsuranceCompany>{
}
