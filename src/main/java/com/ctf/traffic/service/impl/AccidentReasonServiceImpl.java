package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.*;
import javax.transaction.*;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.po.response.*;
import com.ctf.traffic.repository.*;
import com.ctf.traffic.service.*;

import lombok.extern.slf4j.*;

/**
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Service
@Slf4j
public class AccidentReasonServiceImpl implements AccidentReasonService{
    @Resource
    private AccidentReasonRepository repository;
    @Resource
    private ClauseService clauseService;
    @Resource
    private IllegalBehaviorService illegalBehaviorService;
    @Resource
    private AccidentReasonCategoryService categoryService;

    @Override
    public Page<AccidentReason> findByCateIdAndDescription(long cateId, String description, int page, int size) {
        return repository.findAll(
                (root, query, builder) -> description != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.and(builder.equal(root.get("accidentReasonCategory"), cateId)),
                                builder.like(root.get("description"), "%" + description + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.and(builder.equal(root.get("accidentReasonCategory"), cateId))),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Override
    public List<AccidentReasonResponse> findByCategoryId(long categoryId) {
        List<AccidentReasonCategory> categories = categoryId > 0 ? Arrays.asList(categoryService.findById(categoryId))
                : categoryService.findAll();
        List<AccidentReasonResponse> responses = new ArrayList<>();
        categories.forEach(category -> {
            AccidentReasonResponse response = new AccidentReasonResponse(category.getId(), category.getName(), null,
                    null);
            List<AccidentReasonResponse> pics = new ArrayList<>();
            repository.findByAccidentReasonCategoryIdAndState(category.getId(),1).forEach(reason -> pics
                    .add(new AccidentReasonResponse(reason.getId(), null, reason.getUrl(), reason.getDescription())));
            response.setPics(pics);
            responses.add(response);
        });
        return responses;
    }

    @Override
    public AccidentReason findById(long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public AccidentReason delete(long id) {
        AccidentReason reason = repository.getOne(id);
        reason.setState(Constant.STATE_OFF);
        return saveOrUpdate(reason);
    }

    @Transactional
    @Override
    public AccidentReason saveOrUpdate(AccidentReason reason) {
        return repository.saveAndFlush(reason);
    }

    @Transactional
    @Override
    public AccidentReason update(AccidentReason reason, long[] clauseIds, long[] behaviorIds) {
        AccidentReason accidentReason = findById(reason.getId());
        if (!StringUtils.isEmpty(reason.getBehavior()))
            accidentReason.setBehavior(reason.getBehavior());
        if (!StringUtils.isEmpty(reason.getClause()))
            accidentReason.setClause(reason.getClause());
        if (!StringUtils.isEmpty(reason.getUrl()))
            accidentReason.setUrl(reason.getUrl());
        if (!StringUtils.isEmpty(reason.getDescription()))
            accidentReason.setDescription(reason.getDescription());
        if (clauseIds != null)
            accidentReason.setClauses(clauseService.newInstances(clauseIds));
        if (behaviorIds != null)
            accidentReason.setIllegalBehaviors(illegalBehaviorService.newInstances(behaviorIds));
        return saveOrUpdate(accidentReason);
    }
}
