package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ctf.traffic.po.Constant;
import com.ctf.traffic.po.IllegalBehavior;
import com.ctf.traffic.repository.IllegalBehaviorRepository;
import com.ctf.traffic.service.IllegalBehaviorService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
@Service
@Slf4j
public class IllegalBehaviorServiceImpl implements IllegalBehaviorService{
    @Resource
    private IllegalBehaviorRepository repository;

    @Override
    public Page<IllegalBehavior> findByBehavior(String behavior, int page, int size) {
        return repository.findAll(
                (root, query, builder) -> behavior != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.like(root.get("behavior"), "%" + behavior + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Override
    public IllegalBehavior findById(long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public IllegalBehavior delete(long id) {
        IllegalBehavior illegalBehavior = repository.getOne(id);
        illegalBehavior.setState(Constant.STATE_OFF);
        return saveOrUpdate(illegalBehavior);
    }

    @Transactional
    @Override
    public IllegalBehavior saveOrUpdate(IllegalBehavior illegalBehavior) {
        return repository.saveAndFlush(illegalBehavior);
    }

    @Override
    public Page<IllegalBehavior> findByAccidentReasonId(long id, int page, int size) {
        return repository.findByAccidentReasonId(id, PageRequest.of(page - 1, size));
    }

    @Override
    public List<IllegalBehavior> findByAccidentReasonId(long id) {
        return repository.findByAccidentReasonId(id);
    }

    @Override
    public Page<IllegalBehavior> findExcludeByAccidentReasonId(long id, String behavior, int page, int size) {
        return behavior == null ? repository.findExcludeByAccidentReasonId(id, PageRequest.of(page - 1, size))
                : repository.findExcludeByAccidentReasonId(id, behavior, PageRequest.of(page - 1, size));
    }

    @Override
    public Set<IllegalBehavior> newInstances(long[] ids) {
        Set<IllegalBehavior> behaviors = new HashSet<>();
        for (int i = 0; i < ids.length; i++)
            behaviors.add(findById(ids[i]));
        return behaviors;
    }
}
