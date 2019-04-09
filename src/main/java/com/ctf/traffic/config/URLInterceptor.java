package com.ctf.traffic.config;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ctf.traffic.po.sys.SysPerson;
import com.ctf.traffic.util.ContextThreadLocal;

import lombok.extern.slf4j.Slf4j;

/**
 * URL拦截器（做权限认证）
 */
@Slf4j
public class URLInterceptor extends HandlerInterceptorAdapter{

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        log.debug(" URLInterceptor.afterCompletion : [{}]", request.getRequestURI());
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response,
            Object handler) {
        log.debug(" URLInterceptor.afterConcurrentHandlingStarted : [{}]", request.getRequestURI());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
        log.debug(" URLInterceptor.postHandle : [{}]", request.getRequestURI());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug(" URLInterceptor.preHandle : [{}]", request.getRequestURI());
        //return super.preHandle(request, response, handler);
        if (request.getServletPath().contains("/person") || request.getServletPath().contains("/s")
                || request.getServletPath().contains("/manage")) {
            if (request.getSession().getAttribute("person") == null) {
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("登录已过期");
                ajaxReDirect(request, response);
                return false;
            } else {
                try {
                    SysPerson person = (SysPerson) request.getSession().getAttribute("person");
                    ContextThreadLocal.setPerson(person);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return false;
                }
                //安全退出登录
                if (request.getServletPath().contains("/logout")) {
                    request.getSession().removeAttribute("person");
                    ContextThreadLocal.clearAll();
                    response.setHeader("Content-type", "text/html;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("已成功注销");
                    ajaxReDirect(request, response);
                    return false;
                }

                //管理员图片请求
                if (request.getServletPath().contains("/image")) {
                    log.debug(" URLInterceptor.preHandle : 管理员图片: [{}]", request.getServletPath());
                    SysPerson person = ContextThreadLocal.getPerson();
                    //根据用户id转发到其对应的图片URL
                    String userImageUrl = "";
                    if (person.getImageUrl() != null) {
                        userImageUrl = request.getRequestURL().toString().replaceAll(request.getRequestURI(), "")
                                + "/personImage/" + person.getImageUrl().replaceAll("\\\\", "/");
                        log.debug(" URLInterceptor.preHandle : 管理员图片: [{}]", userImageUrl);
                    }
                    response.sendRedirect(userImageUrl);
                    return false;
                }
                return true;
            }
            //外部系统请求拦截（包括APP与第三方系统）
        } else if (request.getServletPath().contains("/extsys")) {
            //外部系统鉴权
            if (request.getServletPath().contains("/login")) {
                //如果是登录请求则放行
                return true;
            } else {
                //不是登录请求则鉴权（token信息从头部或者请求参数中获取）
                String token = (null == request.getHeader("token") || StringUtils.isEmpty(request.getHeader("token")))
                        ? (String) request.getAttribute("token")
                        : request.getHeader("token");
                if (null != token && !StringUtils.isEmpty(token)) {
                    Set allTokenSet = (Set) request.getSession().getAttribute("tokenSet");
                    return null != allTokenSet && allTokenSet.contains(token);
                }
                return false;
            }
        } else {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("路径不存在");
            return false;
        }
    }

    //对于请求是ajax请求重定向问题的处理方法
    private void ajaxReDirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageName = "/login.html";
        if (request.getServletPath().contains("person")) {
            pageName = "/login.html";
        }
        //获取当前请求的路径
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        //如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            //告诉ajax我是重定向
            response.setHeader("REDIRECT", "REDIRECT");
            //告诉ajax我重定向的路径
            response.setHeader("CONTENTPATH", basePath + pageName);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.sendRedirect(basePath + pageName);
        }
    }

}
