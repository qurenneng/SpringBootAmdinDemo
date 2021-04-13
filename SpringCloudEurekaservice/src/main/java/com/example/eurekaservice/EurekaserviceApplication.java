package com.example.eurekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * EnableEurekaServer 服务端：
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaserviceApplication.class, args);
    }

}
