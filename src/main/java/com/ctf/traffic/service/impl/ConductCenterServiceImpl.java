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
 * @Date 7/13/2018
 * @see
 */
@Service
public class ConductCenterServiceImpl implements ConductCenterService {
    @Resource
    private ConductCenterRepository repository;

    @Override
    public Page<ConductCenter> findByName(String name, int page, int size) {
        return repository.findAll(
                (root, query, builder) -> name != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                        builder.like(root.get("name"), "%" + name + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Transactional
    @Override
    public ConductCenter delete(long id) {
        ConductCenter conductCenter = findById(id);
        if (conductCenter != null) {
            conductCenter.setState(Constant.STATE_OFF);
            return saveOrUpdate(conductCenter);
        }
        return null;
    }

    @Override
    public List<ConductCenter> findAll() {
        return repository.findByState(Constant.STATE_ON);
    }

    @Override
    public ConductCenter findById(long id) {
        return repository.findById(id).get();
    }

    @Transactional
    @Override
    public ConductCenter saveOrUpdate(ConductCenter conductCenter) {
        if (conductCenter == null) {
            return null;
        }
        return repository.saveAndFlush(conductCenter);
    }


    @Override
    public ConductCenter findByCode(String code) {
        return repository.findByCodeLike("%".concat(ConductCenter.SPLIT_CHARACTER).concat(code)
                .concat(ConductCenter.SPLIT_CHARACTER).concat("%"));
    }

}
