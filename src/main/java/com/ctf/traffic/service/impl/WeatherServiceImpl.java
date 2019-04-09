package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.*;
import javax.transaction.*;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.repository.*;
import com.ctf.traffic.service.*;

import lombok.extern.slf4j.*;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService{
    @Resource
    private WeatherRepository repository;

    @Override
    public List<Weather> findAllAvailable() {
        return repository
                .findAll((root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON)));
    }

    @Override
    public Page<Weather> findByName(String name, int page, int size) {
        return repository.findAll(
                (root, query, builder) -> name != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.like(root.get("name"), "%" + name + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Override
    public Weather findById(long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public Weather delete(long id) {
        Weather weather = repository.getOne(id);
        weather.setState(Constant.STATE_OFF);
        return saveOrUpdate(weather);
    }

    @Transactional
    @Override
    public Weather saveOrUpdate(Weather weather) {
        return repository.saveAndFlush(weather);
    }
}
