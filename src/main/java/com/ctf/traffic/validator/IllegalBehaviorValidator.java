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
@Component@Slf4j
public class IllegalBehaviorValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return IllegalBehavior.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        IllegalBehavior illegalBehavior = (IllegalBehavior) o;
        log.debug(" IllegalBehaviorValidator.validate : [{}]", illegalBehavior);
        if (illegalBehavior == null) {
            errors.rejectValue("object", "object.null", "违法行为不能为空");
        }
        if (StringUtils.isEmpty(illegalBehavior.getBehavior())) {
            errors.rejectValue("behavior", "field.behavior.length", "违法行为长度不能为空");
        }
    }
}
