package com.ctf.traffic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
/***ha
 * 交管系统.
 */
@SpringBootApplication
@EnableScheduling
//public class AppController extends SpringBootServletInitializer {
public class AppController{
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(AppController.class);
//    }
    public static void main(String[] args) {
        SpringApplication.run(AppController.class, args);
    }
}