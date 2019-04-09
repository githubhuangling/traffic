package com.ctf.traffic.config;

import com.fasterxml.jackson.databind.*;
import java.util.concurrent.*;
import okhttp3.*;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.*;
import org.springframework.web.filter.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger2.annotations.*;

/**
 * @author ramer
 * @Date 6/25/2018
 * @see
 */
@Configuration
@EnableSwagger2
public class CommonBean{
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.MILLISECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).connectionPool(new ConnectionPool()).build();
    }

    @Bean
    public MappingJackson2HttpMessageConverter messageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        return converter;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.ctf.traffic.controller")).paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("交管项目接口文档").description("api 根目录：http://192.168.0.98:8080/")
                .contact(new Contact("Tang Xiaofeng", "", "")).version("1.0").build();
    }

    @Bean
    public HttpPutFormContentFilter httpPutFormContentFilter() {
        return new HttpPutFormContentFilter();
    }
}
