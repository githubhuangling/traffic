package com.ctf.traffic.auth;

import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.ctf.traffic.po.CommonResponse;
import com.ctf.traffic.po.Constant;
import com.ctf.traffic.po.ThirdPartyCertificate;
import com.ctf.traffic.po.ThirdPartyLog;
import com.ctf.traffic.po.Constant.ResultCode;
import com.ctf.traffic.remote.RemoteInvoke;
import com.ctf.traffic.service.ThirdCertificateService;
import com.ctf.traffic.util.ContextThreadLocal;
import com.ctf.traffic.util.IpUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 6/29/2018
 * @see
 */
@Component
@Aspect
@Slf4j
public class AuthenticationAop{
    @Resource
    private ThirdCertificateService service;

    @Around("execution(@com.ctf.traffic.auth.annotation.RequiredAuthentication * *(..))")
    public Object handlerAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String code = request.getParameter("code");
        String signed = request.getParameter("signed");
        String data = request.getParameter("data");
        log.info(" AuthenticationAop.handlerAuthentication : [url={},data={},code={},signed={}]",request.getRequestURL().toString(), data, code, signed);
        Object result = new CommonResponse(false, "缺少参数，[code,signed,data不能为空]");
        if (code != null && data != null && signed != null) {
            ThirdPartyCertificate certificate = service.findByCode(code);
            if (certificate == null) {
                return new CommonResponse(false, ResultCode.E0001);
            }
            if (certificate.getState().equals(Constant.STATE_OFF)) {
                return new CommonResponse(false, ResultCode.E0002);
            }
            if (RemoteInvoke.md5Encode(certificate.getSecret().concat(data)).equalsIgnoreCase(signed)) {
                log.debug(" AuthenticationAop.handlerAuthentication outer request: success0");
                ContextThreadLocal.setData(data.replaceAll("\n", ""));
                result = joinPoint.proceed();
            } else if (RemoteInvoke.md5Encode(certificate.getSecret().concat(URLDecoder.decode(data, "UTF-8"))).equalsIgnoreCase(signed)) {
                log.debug(" AuthenticationAop.handlerAuthentication outer request: success1");
                ContextThreadLocal.setData(URLDecoder.decode(data, "UTF-8").replaceAll("\n", ""));
                result = joinPoint.proceed();
            } else {
                log.info(" AuthenticationAop.handlerAuthentication outer request: authentication fail");
                result = new CommonResponse(false, "认证失败");
            }
            ThirdPartyLog thirdPartyLog = new ThirdPartyLog();
            String realIP = IpUtils.getRealIP(request);
            thirdPartyLog.setIp(realIP);
            thirdPartyLog.setUrl(request.getRequestURL().toString());
            thirdPartyLog.setThirdPartyCertificate(certificate);
            String jsonResult;
            if (result instanceof CommonResponse && ((CommonResponse) result).isResult()) {
                CommonResponse resp = new CommonResponse();
                resp.setResult(((CommonResponse) result).isResult());
                resp.setMsg("调用成功");
                jsonResult = JSON.toJSONString(resp);
            } else {
                jsonResult = JSON.toJSONString(result);
            }
            thirdPartyLog.setResult(jsonResult);
            log.debug(" AuthenticationAop.handlerAuthentication : [{}]", jsonResult);
            service.saveLog(thirdPartyLog);
        }
        return result;
    }

    //    @Around("execution(* com.ctf.traffic.controller.customer.AccidentController.*(..))")
    //    public Object handlerConductCenterAuth(ProceedingJoinPoint joinPoint) throws Throwable {
    //        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
    //                .getRequest();
    //        final HttpSession session = request.getSession();
    //        Long conductCenterId = (Long) session.getAttribute("conductCenterId");
    //        return conductCenterId == null ? new CommonResponse(false, "使用本系统需要权限,请联系管理员添加") : joinPoint.proceed();
    //    }
}
