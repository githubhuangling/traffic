package com.ctf.traffic.service;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/29/2018
 * @see
 */
public interface ThirdCertificateService{
    ThirdPartyCertificate findByCode(String code);

    ThirdPartyLog saveLog(ThirdPartyLog log);
}
