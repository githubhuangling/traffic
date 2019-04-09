package com.ctf.traffic.auth.annotation;

import java.lang.annotation.*;

/**
 * @author ramer
 * @Date 6/25/2018
 * @see
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiredAuthentication {
}
