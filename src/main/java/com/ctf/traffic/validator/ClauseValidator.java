package com.ctf.traffic.validator;

import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.validation.*;

import com.ctf.traffic.po.*;

import lombok.extern.slf4j.*;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
@Component
@Slf4j
public class ClauseValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return Clause.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Clause clause = (Clause) o;
        log.debug(" ClauseValidator.validate : [{}]", clause);
        if (clause == null) {
            errors.rejectValue("object", "object.null", "法律条款不能为空");
        }
        if (StringUtils.isEmpty(clause.getClause())) {
            errors.rejectValue("name", "filed.name.length", "法律条款不能为空");
        }
    }
}
