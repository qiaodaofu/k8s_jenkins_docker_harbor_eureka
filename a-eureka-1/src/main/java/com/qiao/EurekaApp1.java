package com.qiao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApp1 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApp1.class,args);
    }
}