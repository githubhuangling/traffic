package com.ctf.traffic;

import javax.annotation.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import com.ctf.traffic.service.*;

import lombok.extern.slf4j.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class AppTest{
    @Resource
    private AccidentService accidentService;
    @Resource
    private AccidentPartyService accidentPartyService;
    @Resource
    private AccidentReasonCategoryService reasonCategoryService;
    @Resource
    private AccidentReasonService reasonCategoryPicService;

    @Test
    public void testFoo() {
    }
}
