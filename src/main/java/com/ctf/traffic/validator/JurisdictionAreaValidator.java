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
public class JurisdictionAreaValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return JurisdictionArea.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        JurisdictionArea area = (JurisdictionArea) o;
        log.debug(" JurisdictionAreaValidator.validate : [{}]", area);
        if (StringUtils.isEmpty(area.getName())) {
            errors.rejectValue("name", "field.name.length", "名称不能为空");
        }
        if (area.getLevel() < 1) {
            errors.rejectValue("level", "field.level.length", "level应该大于0");
        }
    }
}
