package com.ctf.traffic.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ctf.traffic.po.Constant;
import com.ctf.traffic.po.RoadCode;
import com.ctf.traffic.repository.RoadCodeRepository;
import com.ctf.traffic.service.RoadCodeService;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Service
public class RoadCodeServiceImpl implements RoadCodeService{
    @Resource
    private RoadCodeRepository repository;

    @Override
    public List<RoadCode> findByXzqh(String xzqh) {
        return repository.findAll(
        		(root, query, builder) -> builder.and(builder.equal(root.get("xzqh"), xzqh), builder.equal(root.get("state"), Constant.STATE_ON)),
            Sort.by(Direction.ASC, "id")
        );
    }

    @Override
    public List<RoadCode> findByDldm(String dldm) {
        return repository.findAll(
                (root, query, builder) -> dldm != null
                        ? builder.and(builder.like(root.get("dldm"), "%" + dldm + "%"),
                                builder.and(builder.equal(root.get("state"), Constant.STATE_ON)))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                Sort.by(Direction.ASC, "id"));
    }
}
