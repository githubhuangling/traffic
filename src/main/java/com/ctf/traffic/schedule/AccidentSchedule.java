package com.ctf.traffic.schedule;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ctf.traffic.service.AccidentService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 7/7/2018
 * @see
 */
@Component
@Slf4j
public class AccidentSchedule{
    @Resource
    private AccidentService accidentService;

    /**
     * 每天22:30:00重置排队号.
     */
    @Scheduled(cron = "00 30 22 * * ?")
    public void resetSeqNumber() {
        log.info(" AccidentSchedule.resetSeqNumber");
        accidentService.resetSeqNumber();
    }

    /**
     * 每天04:30:00再次重置排队号(清除测试排队号).
     */
    @Scheduled(cron = "00 30 04 * * ?")
    public void resetSeqNumber1() {
        log.info(" AccidentSchedule.resetSeqNumber");
        accidentService.resetSeqNumber();
    }

    /**
     * 每天02:00 删除不完整的事故信息.
     */
    @Scheduled(cron = "00 00 02 * * ?")
    public void deleteRedundantAccident() {
        log.info(" AccidentSchedule.deleteRedundantAccident : [{}]", accidentService.deleteRedundantAccident());
    }

}
