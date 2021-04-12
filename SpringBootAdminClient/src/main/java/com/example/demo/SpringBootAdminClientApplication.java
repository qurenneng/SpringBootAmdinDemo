package com.example.demo;

import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import de.codecentric.boot.admin.server.web.client.InstanceExchangeFilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@SpringBootApplication
public class SpringBootAdminClientApplication {

    private static final Logger log = LoggerFactory.getLogger(SpringBootAdminClientApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminClientApplication.class, args);
    }




}
