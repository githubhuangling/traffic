package com.ctf.traffic.service;

import com.ctf.traffic.po.*;
import java.util.*;
import org.springframework.data.domain.*;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
public interface InsuranceCompanyService {
    List<InsuranceCompany> findInsurance();

    /**
     * 获取所有可用的保险公司
     * @return
     */
    List<InsuranceCompany> findAllAvailable();

    Page<InsuranceCompany> findByName(String name, int page, int size);

    InsuranceCompany findById(long id);

    InsuranceCompany delete(long id);

    InsuranceCompany saveOrUpdate(InsuranceCompany insuranceCompany);
}
