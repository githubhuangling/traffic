package com.ctf.traffic.controller;

import com.ctf.traffic.util.*;
import java.util.*;
import javax.servlet.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

/**
 * @author jiangmin
 * @Date 2019/4/15
 * @see
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    @RequestMapping(method = RequestMethod.GET)
    public void signature(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("weixin idnex..............");
        //1 明文接入认证方式 直接回显token 没有认证的公众号不能明文认证
        //response.getWriter().println("weixin");
        // 2加密认证
        //1)获取四个参数
        // signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String signature = request.getParameter("signature");
        // timestamp 时间戳
        String timestamp = request.getParameter("timestamp");
        // nonce 随机数
        String nonce = request.getParameter("nonce");
        // echostr 随机字符串
        String echostr = request.getParameter("echostr");
        System.out.println("signature:"+signature);
        System.out.println("timestamp:"+timestamp);
        System.out.println("nonce:"+nonce);
        System.out.println("echostr:"+echostr);
        //2)使用四个参数，按以下步骤做接入认证
        // 1）将token、timestamp、nonce三个参数进行字典序排序(数组排序)
        String[] params = {"weixin",timestamp,nonce};
        Arrays.sort(params);
        // 2）将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder sb = new StringBuilder();
        for (String str : params) {
            sb.append(str);
        }
        String content = sb.toString();
        String securityContent = SecurityUtil.sha1(content);
        // 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (securityContent.equals(signature)) {
            //认证成功-之间响应echostr
            response.getWriter().print(echostr);
        }
        else{
            //认证失败
            throw new RuntimeException("接入认证失败！");
        }
    }
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("index........................");
        response.setContentType("text/html; charset=UTF-8");

        return null;
    }
}

