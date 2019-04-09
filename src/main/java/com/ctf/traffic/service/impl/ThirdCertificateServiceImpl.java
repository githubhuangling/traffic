package com.ctf.traffic.service.impl;

import javax.annotation.*;

import org.springframework.stereotype.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.repository.thirdparty.*;
import com.ctf.traffic.service.*;

/**
 * @author ramer
 * @Date 6/29/2018
 * @see
 */
@Service
public class ThirdCertificateServiceImpl implements ThirdCertificateService{
    @Resource
    private ThirdPartyCertificateRepository certificateRepository;
    @Resource
    private ThirdPartyLogRepository logRepository;

    @Override
    public ThirdPartyCertificate findByCode(String code) {
        return code == null ? null : certificateRepository.findByCode(code);
    }

    @Override
    public ThirdPartyLog saveLog(ThirdPartyLog log) {
        return logRepository.saveAndFlush(log);
    }
}
