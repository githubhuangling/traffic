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

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Service
public class AccidentReasonCategoryImpl implements AccidentReasonCategoryService{
    @Resource
    private AccidentReasonCategoryRepository repository;

    @Override
    public Page<AccidentReasonCategory> findByName(String name, int page, int size) {
        return repository.findAll(
                (root, query, builder) -> name != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.like(root.get("name"), "%" + name + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Override
    public List<AccidentReasonCategory> findAll() {
        return repository
                .findAll((root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON)));
    }

    @Override
    public AccidentReasonCategory findById(long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public AccidentReasonCategory delete(long id) {
        AccidentReasonCategory category = repository.getOne(id);
        category.setState(Constant.STATE_OFF);
        return saveOrUpdate(category);
    }

    @Transactional
    @Override
    public AccidentReasonCategory saveOrUpdate(AccidentReasonCategory category) {
        return repository.saveAndFlush(category);
    }
}
