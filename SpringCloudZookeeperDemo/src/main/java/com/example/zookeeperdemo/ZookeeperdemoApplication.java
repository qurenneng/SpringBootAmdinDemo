package com.example.zookeeperdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Zookeeper demo 实例：
 */
@SpringBootApplication
@RestController
public class ZookeeperdemoApplication {

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperdemoApplication.class, args);
    }

}
