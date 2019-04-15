package com.ctf.traffic.controller;


import java.io.*;
import java.util.*;
import java.util.Map.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.catalina.security.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;



@Controller
@RequestMapping("/index")
public class IndexController {
  /**
   * 处理微信认证，get提交
   */
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

  /**
   * 接入认证填写地址的post方法用来接收微信服务器转发微信信息用户的请求
   * 
   * index........................
	<xml>
	<ToUserName><![CDATA[gh_ae9b8f50b1b3]]></ToUserName> 接收者
	<FromUserName><![CDATA[oxLXms6cL0ECuVST7vQiDZdg4RbU]]></FromUserName> 发送者
	<CreateTime>1507706381</CreateTime> 创建时间
	<MsgType><![CDATA[text]]></MsgType> 消息类型
	<Content><![CDATA[呵呵]]></Content>   消息内容
	<MsgId>6475549598927510918</MsgId>  消息编号
	</xml>
   */
  @RequestMapping(method = RequestMethod.POST)
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
	 System.out.println("index........................");
	 response.setContentType("text/html; charset=UTF-8");
	// 1 获取请求消息内容并打印
	 /*
	// 字节输入流
	ServletInputStream inputStream = request.getInputStream();
	// 转换为字符流--一个一个字符读
	InputStreamReader isr = new InputStreamReader(inputStream, "utf-8");
	// 缓冲流--一行一行的读
	BufferedReader br = new BufferedReader(isr);
	String content = null;
	while ((content = br.readLine()) != null) {
		System.out.println(content);

	}*/
	//2 接收请求并且做出响应（发什么就回什么）--需要从原来请求里面获取信息来做响应
	//2.1 对请求进行xml解析
	// 字节输入流
	ServletInputStream inputStream = request.getInputStream();
	// 转换为字符流--一个一个字符读
	InputStreamReader isr = new InputStreamReader(inputStream, "utf-8");
	Map<String, String> result = XmlPullParserUtil.parse(isr);
	print(result);
	//2.2 回复消息-拼接字符串
	//response.getWriter().print(buildXmlMsg(result));
	//2.3 回复消息-使用jsp模块，jsp是一个特殊的Servlet，最终一个jsp页面会被会被翻译成一个Servlet类，这个类里面就把我们jsp
	//中一行一行的文本通过writer进行输出
	ModelAndView mv = new ModelAndView();
	mv.setViewName("weixin");;//视图名称，可以跟进它获取jsp的路径
	mv.addObject("map",result);//设置模型，map表示页面中使用到模型名称，result就是模型对象
	return mv;
	  
  }

  
  //拼接响应的xml字符串
	private String buildXmlMsg(Map<String, String> result) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		//原来的发送者成了响应的接收者，原来接受者成了发送者
		sb.append("<ToUserName>"+result.get("FromUserName")+"</ToUserName>");
		sb.append("<FromUserName>"+result.get("ToUserName")+"</FromUserName>");
		sb.append("<CreateTime>"+new Date().getTime()+"</CreateTime>");
		String msgType = result.get("MsgType");
		if ("image".equals(msgType)) {
			//图片消息
			sb.append("<MsgType><![CDATA[image]]></MsgType>");
			sb.append("<Image>");
			sb.append("<MediaId>"+result.get("MediaId")+"</MediaId>");
			sb.append("</Image>");
			
		}else if ("voice".equals(msgType)) {
			sb.append("<MsgType><![CDATA[voice]]></MsgType>");
			sb.append("<Voice>");
			sb.append("<MediaId>"+result.get("MediaId")+"</MediaId>");
			sb.append("</Voice>");
		}
		else{
			//文本消息
			sb.append("<MsgType><![CDATA[text]]></MsgType>");
			sb.append("<Content>"+result.get("Content")+"</Content>");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	private void print(Map<String, String> result) {
		Set<Entry<String, String>> entrySet = result.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			System.out.println(entry.getKey()+"--->"+entry.getValue());
		}		
	}
}
