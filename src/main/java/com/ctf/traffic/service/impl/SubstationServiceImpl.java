package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.*;
import javax.transaction.*;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.repository.*;
import com.ctf.traffic.service.*;

import lombok.extern.slf4j.*;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
@Service
@Slf4j
public class SubstationServiceImpl implements SubstationService{
    @Resource
    private SubstationRepository repository;
    @Resource
    private JurisdictionAreaService areaService;

    @Override
    public Page<Substation> findByName(String name, int page, int size) {
        return repository.findAll(
                (root, query, builder) -> name != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.like(root.get("name"), "%" + name + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Override
    public List<Substation> findAll() {
        return repository.findByState(Constant.STATE_ON);
    }

    @Override
    public Substation findById(long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public Substation delete(long id) {
        Substation substation = repository.getOne(id);
        substation.setState(Constant.STATE_OFF);
        return saveOrUpdate(substation);
    }

    @Transactional
    @Override
    public Substation saveOrUpdate(Substation substation) {
        return repository.saveAndFlush(substation);
    }

    @Transactional
    @Override
    public Substation update(Substation c, long[] areaIds) {
        Substation substation = findById(c.getId());
        if (!StringUtils.isEmpty(c.getName()))
            substation.setName(c.getName());
        if (areaIds != null) {
            //  清除所有关联
            areaService.findByCenterId(c.getId()).forEach(area -> {
                area.setSubstation(null);
                areaService.saveOrUpdate(area);
                log.info(" SubstationServiceImpl.update : [{}]", area);
            });
            // 添加新关联
            areaService.newInstances(areaIds).forEach(area -> {
                area.setSubstation(new Substation(c.getId()));
                areaService.saveOrUpdate(area);
            });
        }
        return saveOrUpdate(substation);
    }
}
