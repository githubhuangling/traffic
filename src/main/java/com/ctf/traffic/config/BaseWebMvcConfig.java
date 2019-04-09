package com.ctf.traffic.config;

import com.ctf.traffic.*;
import com.ctf.traffic.util.*;
import java.nio.charset.*;
import java.util.*;
import javax.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.*;
import org.springframework.web.servlet.config.annotation.*;

/**
 * MVC配置
 * 配置视图解析器以及静态资源等
 * */
@Configuration
@ComponentScan(basePackageClasses = AppController.class)
public class BaseWebMvcConfig extends WebMvcConfigurationSupport{
    @Resource
    private MappingJackson2HttpMessageConverter jsonConverter;
    /** 会被拦截器排除的规则列表 **/
    private final List<String> patterns = new ArrayList<String>() {
        private static final long serialVersionUID = -6596709397258346216L;
        {
            add("/**/public**");//以public开头命名的方法不用拦截
            add("/**/static**");//以static开头命名的方法不用拦截
            add("/customer/index");//事故初始化页面
            add("/customer/instructions.html");//事故录入须知
            add("/customer/validConductCenter");//校验硬盘号
            add("/customer/phoneQrcode");//首页二维码
            add("/customer/ownAgreement/**");//打印服务协议书
            add("/customer/upload/uploadFile");//打印服务文件上传
            add("/customer/ownAgreementQrcode/**");//打印服务二维码
            add("/test");//校验硬盘号
        }
    };

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        //配置编码，解决中文乱码
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        converters.add(jsonConverter);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/public/", "classpath:/static/",
                "classpath:/resources", "classpath:/META-INF/resources");
        //    配置静态资源
        registry.addResourceHandler("/personImage/**", "/accidentImage/**")
                .addResourceLocations("file:" + PathUtil.getImgBasePath());
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //配置拦截器，拦截以“/s/”开头的路径，进行权限管理
        registry.addInterceptor(new URLInterceptor()).addPathPatterns("/c/**").excludePathPatterns(patterns);
        registry.addInterceptor(new URLInterceptor()).addPathPatterns("/s/**").excludePathPatterns(patterns);
        registry.addInterceptor(new URLInterceptor()).addPathPatterns("/manage/**").excludePathPatterns(patterns);
        registry.addInterceptor(new CustomerInterceptor()).addPathPatterns("/customer/**")
                .excludePathPatterns(patterns);
    }

}
