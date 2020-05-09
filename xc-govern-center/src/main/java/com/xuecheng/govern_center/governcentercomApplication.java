package com.xuecheng.govern_center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class governcentercomApplication {
    public static void main(String[] args) {
        SpringApplication.run(governcentercomApplication.class, args);
    }
}
