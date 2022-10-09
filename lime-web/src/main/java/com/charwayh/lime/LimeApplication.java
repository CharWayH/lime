package com.charwayh.lime;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author charwayH
 */
@MapperScan("com.charwayh.lime.support.mapper")
@SpringBootApplication
public class LimeApplication {

    public static void main(String[] args) {
        /**
         * 如果你需要启动Apollo动态配置
         * 1、启动apollo
         * 2、将application.properties配置文件的 austin.apollo.enabled 改为true
         * 3、下方的property替换真实的eureka ip和port
         */
        System.setProperty("apollo.config-service", "http://127.0.0.1:8081");
        SpringApplication.run(LimeApplication.class);
    }
}
