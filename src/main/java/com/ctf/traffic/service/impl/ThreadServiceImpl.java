package com.ctf.traffic.service.impl;

import java.util.concurrent.*;

import org.springframework.stereotype.Service;

import com.ctf.traffic.service.ThreadService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 7/31/2018
 * @see
 */
@Slf4j
@Service
public class ThreadServiceImpl implements ThreadService{
    private ExecutorService executorService;

    @Override
    public void newThread(Runnable runnable) {
        if (executorService == null || executorService.isShutdown()) {
            executorService = Executors.newFixedThreadPool(3);
        }
        final Future<?> future = executorService.submit(runnable);
        try {
            log.info(" ThreadServiceImpl.newThread : [{}]", future.get());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
