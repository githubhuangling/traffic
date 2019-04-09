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
public class AccidentReasonCategoryValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return AccidentReasonCategory.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccidentReasonCategory reasonCategory = (AccidentReasonCategory) o;
        log.debug(" AccidentReasonCategoryValidator.validate : [{}]", reasonCategory);
        if (reasonCategory == null) {
            errors.rejectValue("object", "object.null", "事故原因类别不能为空");
        }
        if (StringUtils.isEmpty(reasonCategory.getName())) {
            errors.rejectValue("name", "filed.name.length", "名称不能为空");
        }
    }
}
