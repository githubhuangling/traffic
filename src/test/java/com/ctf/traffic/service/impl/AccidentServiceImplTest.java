package com.ctf.traffic.service.impl;

import javax.annotation.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import com.ctf.traffic.service.*;

import lombok.extern.slf4j.*;

/** 
* AccidentServiceImpl Tester. 
* 
* @author RAMER
* @since 06/21/2018
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AccidentServiceImplTest{
    @Resource
    private AccidentService service;

    @Test
    public void testGetNameByIdNumber() {
        String idNumber = "51162219940925435X";
        service.getNameByIdNumber(idNumber);
    }

}
