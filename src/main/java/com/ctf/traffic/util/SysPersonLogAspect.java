package com.ctf.traffic.util;

import java.lang.reflect.*;

import javax.annotation.*;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.annotation.DeclareAnnotation;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.repository.sys.*;

/**
 * AOP记录
 * @author Administrator
 */
@Component
@Aspect
public class SysPersonLogAspect{
    @Resource
    SysPersonLogRepository sysPersonLogRepository;

    /**
     * 定义拦截规则：拦截com.xjj.web.controller包下面的所有类中，有@RequestMapping注解的方法。
     */
    @Pointcut("execution(* com.ctf.traffic.controller.manage..*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    @DeclareAnnotation("")
    public void controllerMethodPointcut() {
    }

    /**
     * 拦截器具体实现
     * @param pjp
     * @return RtnJson
     * @throws Throwable 
     */
    //@Around("controllerMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* .........)”写进这里
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        String className = signature.getDeclaringTypeName();
        String methodName = method.getName(); //获取被拦截的方法名
        SysPersonLog spl = new SysPersonLog();
        spl.setClassName(className);
        spl.setMethodName(methodName);
        Object result = null;
        long beginTime = System.currentTimeMillis();
        if (result == null) {
            // 一切正常的情况下，继续执行被拦截的方法
            result = pjp.proceed();
        }
        long costMs = System.currentTimeMillis() - beginTime;
        spl.setCostTime(costMs + "(Ms)");
        sysPersonLogRepository.saveOrUpdate(spl);
        return result;
    }

}
