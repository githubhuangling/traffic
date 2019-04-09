package com.ctf.traffic.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

/**
 * 事故拦截器.
 */
@Slf4j
public class CustomerInterceptor extends HandlerInterceptorAdapter{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	return true;
//        final StringBuffer requestUrl = request.getRequestURL();
//        log.info(" CustomerInterceptor.preHandle : interceptor[{}]", requestUrl.toString());
//        if (request.getSession().getAttribute("conductCenterId") != null) {
//            return true;
//        }
//        final String servletPath = request.getServletPath();
//        final String rootPath = requestUrl.substring(0, requestUrl.indexOf(servletPath));
//        response.sendRedirect(rootPath + "/customer/index");
//        return false;
    }
}
