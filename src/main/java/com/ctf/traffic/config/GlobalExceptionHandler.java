package com.ctf.traffic.config;

import com.ctf.traffic.po.sys.RtnJson;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler{
    @ExceptionHandler(value=Exception.class)
    public String handlerException(HttpServletRequest request, Exception e) {
        log.error("request url[{}]", request.getRequestURL());
        if (e instanceof HttpMessageNotWritableException) {
            log.warn(" GlobalExceptionHandler.handle : encounter HttpMessageNotWritableException.");
            return new RtnJson(false, "NotWritable").toString();
        }else {
            log.error(e.getMessage(), e);
            return new RtnJson(false, e.getMessage()).toString();
        }
    }

    @ExceptionHandler(value=RuntimeException.class)
    public String handlerRuntimeException(HttpServletRequest request, RuntimeException e) {
        log.error("request url[{}]", request.getRequestURL());
        log.error(e.getMessage(), e);
        return new RtnJson(false, e.getMessage()).toString();
    }

    @ExceptionHandler(value=SizeLimitExceededException.class)
    public String handlerSizeLimitExceededException(HttpServletRequest request, SizeLimitExceededException e) {
        log.error("request url[{}]", request.getRequestURL());
        log.error(e.getMessage(), e);
        return new RtnJson(false, "上传文件不能超过150M").toString();
    }
}