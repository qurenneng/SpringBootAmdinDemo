package com.example.demo.configuration;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qrn
 * @version 1.0
 * @date 2021/4/8 10:34
 * SpringBootAdminService 服务类:
 */
@SpringBootApplication
@EnableAdminServer
public class AdmindemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdmindemoApplication.class, args);
    }

}
