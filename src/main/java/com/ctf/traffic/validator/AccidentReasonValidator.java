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
public class AccidentReasonValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return AccidentReason.class.equals(clazz    );
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccidentReason reason = (AccidentReason) o;
        log.debug(" AccidentReasonValidator.validate : [{}]", reason);
        if (reason == null) {
            errors.rejectValue("object", "object.null", "事故原因不能为空");
        }
    }
}
