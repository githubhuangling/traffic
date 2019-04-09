package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.*;

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
public class RoadXzqhServiceImpl implements RoadXzqhService{
    @Resource
    private RoadXzqhRepository repository;

    @Override
    public List<RoadXzqh> findAllAvailable() {
        return repository.findAll();
    }

}
