package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.*;
import javax.transaction.*;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.*;
import org.springframework.stereotype.*;

import com.alibaba.fastjson.*;
import com.ctf.traffic.po.*;
import com.ctf.traffic.po.response.*;
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
public class JurisdictionAreaServiceImpl implements JurisdictionAreaService{
    @Resource
    private JurisdictionAreaRepository repository;

    @Override
    public JurisdictionAreaResponse getJsonTree() {
        List<JurisdictionArea> jurisdictionAreas = repository.findByParentIdIsNull();
        if (jurisdictionAreas.size() < 1) {
            return null;
        }
        JurisdictionAreaResponse response = new JurisdictionAreaResponse(jurisdictionAreas.get(0), -1);
        retrieveChildArea(response.getId(), response);
        log.info(" JurisdictionAreaServiceImpl.getJsonTree : [{}]", JSON.toJSONString(response));
        return response;
    }

    @Override
    public Page<JurisdictionArea> findByName(String name, int page, int size) {
        return repository.findAll(
                (root, query, builder) -> name != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.like(root.get("name"), "%" + name + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Override
    public JurisdictionArea findById(long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public JurisdictionArea delete(long id) {
        JurisdictionArea area = repository.getOne(id);
        area.setState(Constant.STATE_OFF);
        return saveOrUpdate(area);
    }

    @Transactional
    @Override
    public JurisdictionArea saveOrUpdate(JurisdictionArea area) {
        return repository.saveAndFlush(area);
    }

    @Override
    public Page<JurisdictionArea> findByCenterId(long centerId, int page, int size) {
        return repository.findBySubstationAndState(new Substation(centerId), Constant.STATE_ON,
                PageRequest.of(page - 1, size));
    }

    @Override
    public List<JurisdictionArea> findByCenterId(long centerId) {
        return repository.findBySubstation(new Substation(centerId));
    }

    @Override
    public Page<JurisdictionArea> findExcludeByCenterId(String name, int page, int size) {
        return name == null ? repository.findExcludeByCenterId(PageRequest.of(page - 1, size))
                : repository.findExcludeByCenterId(name, PageRequest.of(page - 1, size));
    }

    @Override
    public Set<JurisdictionArea> newInstances(long[] ids) {
        Set<JurisdictionArea> areas = new HashSet<>();
        for (int i = 0; i < ids.length; i++)
            areas.add(findById(ids[i]));
        return areas;
    }

    @Override
    public List<JurisdictionArea> findByParent(Long pid) {
        return pid == null ? repository.findByParentIdIsNull() : repository.findByParentId(pid);
    }

    private void retrieveChildArea(long parentId, JurisdictionAreaResponse response) {
        List<JurisdictionAreaResponse> childResponses = new ArrayList<>();
        repository.findByParentId(parentId)
                .forEach(area -> childResponses.add(new JurisdictionAreaResponse(area, parentId)));
        response.setChild(childResponses);
        if (childResponses.size() > 0) {
            for (JurisdictionAreaResponse childArea : childResponses) {
                retrieveChildArea(childArea.getId(), childArea);
            }
        }
    }
}
