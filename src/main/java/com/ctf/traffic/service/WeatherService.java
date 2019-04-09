package com.ctf.traffic.service;

import java.util.*;

import org.springframework.data.domain.*;

import com.ctf.traffic.po.*;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
public interface WeatherService{
    /**
     * 获取所有可用的天气.
     * @return
     */
    List<Weather> findAllAvailable();

    Page<Weather> findByName(String name, int page, int size);

    Weather findById(long id);

    Weather delete(long id);

    Weather saveOrUpdate(Weather weather);
}
