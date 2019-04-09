package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.*;
import javax.transaction.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.*;
import com.ctf.traffic.po.*;
import com.ctf.traffic.remote.*;
import com.ctf.traffic.remote.response.*;
import com.ctf.traffic.repository.*;
import com.ctf.traffic.service.*;
import com.ctf.traffic.util.*;

import lombok.extern.slf4j.*;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Service
@Slf4j
public class InsuranceCompanyServiceImpl implements InsuranceCompanyService{
    @Value("${com.ctf.traffic.insurance.url}")
    private String INSURANCE_URL;
    @Value("${com.ctf.traffic.insurance.code}")
    private String INSURANCE_CODE;
    @Value("${com.ctf.traffic.insurance.secret}")
    private String INSURANCE_SECRET;
    @Resource
    private InsuranceCompanyRepository repository;

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<InsuranceCompany> findInsurance() {
        String url = INSURANCE_URL.concat("thirdparty/policy/listInsuranceCompany");
        Map<String, String> paras = new HashMap<>();
        paras.put("code", INSURANCE_CODE);
        final String data = RemoteInvoke.base64Encode(JSON.toJSONString(new JSONObject()));
        paras.put("data", data);
        paras.put("signed", RemoteInvoke.md5Encode(INSURANCE_SECRET.concat(data)));
        return (List<InsuranceCompany>) HttpUtils.request(url, paras, RequestMethod.POST, ThirdPartyResponse.class)
                .getData();
    }

    @Override
    public List<InsuranceCompany> findAllAvailable() {
        return repository
                .findAll((root, query, builder) -> builder.and(builder.equal(root.get(InsuranceCompany.PROP_STATE), Constant.STATE_ON)),Sort.by(Direction.DESC,InsuranceCompany.PROP_SORT));
    }

    @Override
    public Page<InsuranceCompany> findByName(String name, int page, int size) {
        return repository.findAll(
                (root, query, builder) -> name != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.like(root.get("name"), "%" + name + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Override
    public InsuranceCompany findById(long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public InsuranceCompany delete(long id) {
        InsuranceCompany insuranceCompany = repository.getOne(id);
        insuranceCompany.setState(Constant.STATE_OFF);
        return saveOrUpdate(insuranceCompany);
    }

    @Transactional
    @Override
    public InsuranceCompany saveOrUpdate(InsuranceCompany insuranceCompany) {
        return repository.saveAndFlush(insuranceCompany);
    }
}
