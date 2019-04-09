package com.ctf.traffic.config;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.ctf.traffic.service.AccidentService;

/**
 * 系统初始化配置.
 * @author Administrator
 * @Date 2018/8/15
 * @see
 */
@Configuration
public class InitialConfig implements ApplicationRunner{
    @Resource
    private AccidentService accidentService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        accidentService.initSeqNumber();
    }
}
