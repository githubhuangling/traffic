package com.ctf.traffic.service.impl;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import javax.annotation.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import com.ctf.traffic.service.*;

import lombok.extern.slf4j.*;

/** 
* AccidentPartyServiceImpl Tester. 
* 
* @author RAMER
* @since 06/21/2018
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AccidentPartyServiceImplTest{
    @Resource
    private AccidentPartyService service;

    @Test
    public void testSaveOrUpdate() {
        //        AccidentParty accidentParty = new AccidentParty();
        //        accidentParty.setIdNumber("52012119820912154X");
        //        accidentParty.setName("李四");
        //        accidentParty.setPhone("13800138001");
        //        service.saveOrUpdate(accidentParty);
        //        log.info(" AccidentPartyServiceImplTest.testSaveOrUpdate : [{}]", accidentParty);
        //        assertThat(accidentParty.getId(), greaterThan(0l));
    }

    @Test
    public void testFindByAccidentId() {
        assertThat(service.findByAccidentId(1l).size(), greaterThan(0));
    }

}
