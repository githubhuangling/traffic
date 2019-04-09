package com.ctf.traffic.service.impl;

import javax.annotation.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.service.*;

import lombok.extern.slf4j.*;

/** 
* DriverLicenceServiceImpl Tester. 
* 
* @author RAMER
* @since 06/21/2018
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DriverLicenceServiceImplTest{
    @Resource
    private DriverLicenceService service;

    @Test
    public void testSaveOrUpdate() throws Exception {
        DriverLicence driverLicence = new DriverLicence();
        driverLicence.setAddress("四川成都高新区");
        log.info(" DriverLicenceServiceImplTest.testSaveOrUpdate : [{}]", service.saveOrUpdate(driverLicence));
    }

}
