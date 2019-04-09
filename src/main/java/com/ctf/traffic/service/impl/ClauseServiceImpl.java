package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ctf.traffic.po.Clause;
import com.ctf.traffic.po.Constant;
import com.ctf.traffic.repository.ClauseRepository;
import com.ctf.traffic.service.ClauseService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Service
@Slf4j
public class ClauseServiceImpl implements ClauseService{
    @Resource
    private ClauseRepository repository;

    @Override
    public Page<Clause> findByClause(String clause, int page, int size) {
        return repository.findAll(
                (root, query, builder) -> clause != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.like(root.get("clause"), "%" + clause + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Override
    public Clause findById(long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public Clause delete(long id) {
        Clause clause = repository.getOne(id);
        clause.setState(Constant.STATE_OFF);
        return saveOrUpdate(clause);
    }

    @Transactional
    @Override
    public Clause saveOrUpdate(Clause clause) {
        return repository.saveAndFlush(clause);
    }

    @Override
    public Page<Clause> findByAccidentReasonId(long id, int page, int size) {
        return repository.findByAccidentReasonId(id, PageRequest.of(page - 1, size));
    }

    @Override
    public List<Clause> findByAccidentReasonId(long id) {
        return repository.findByAccidentReasonId(id);
    }

    @Override
    public Page<Clause> findExcludeByAccidentReasonId(long id, String clause, int page, int size) {
        return clause == null ? repository.findExcludeByAccidentReasonId(id, PageRequest.of(page - 1, size))
                : repository.findExcludeByAccidentReasonId(id, clause, PageRequest.of(page - 1, size));
    }

    @Override
    public Set<Clause> newInstances(long[] ids) {
        Set<Clause> clauses = new HashSet<>();
        for (int i = 0; i < ids.length; i++)
            clauses.add(findById(ids[i]));
        return clauses;
    }

}
