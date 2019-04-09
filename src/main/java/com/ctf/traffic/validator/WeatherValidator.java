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
public class WeatherValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return Weather.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Weather weather = (Weather) o;
        log.debug(" WeatherValidator.validate : [{}]", weather);
        if (weather == null) {
            errors.rejectValue("object", "object.null", "天气信息不能为空");
        }
        String name = weather.getName();
        if (StringUtils.isEmpty(name) || name.length() > 10) {
            errors.rejectValue("name", "filed.name.length", "天气名长度应该在1-10之间");
        }
        if (StringUtils.isEmpty(weather.getIconUrl())) {
            errors.rejectValue("iconUrl", "filed.iconUrl.length", "天气图标不能为空");
        }
    }
}
