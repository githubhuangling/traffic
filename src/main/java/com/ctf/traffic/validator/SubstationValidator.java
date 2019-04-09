package com.ctf.traffic.validator;

import org.springframework.stereotype.*;
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
public class SubstationValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return Substation.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Substation substation = (Substation) o;
        log.debug(" SubstationValidator.validate : [{}]", substation);
        if (substation == null) {
            errors.rejectValue("object", "object.null", "协调中心不能为空");
        }
    }
}
