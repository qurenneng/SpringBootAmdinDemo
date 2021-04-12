package com.example.demo;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import de.codecentric.boot.admin.server.web.client.InstanceExchangeFilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * SpringBootAdminService 服务端:
 */

@EnableAdminServer
@SpringBootApplication
public class SpringBootAdminServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(SpringBootAdminServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminServiceApplication.class, args);
    }

    /**
     * 自定义HTTP标头
     * @return
     */
    @Bean
    public HttpHeadersProvider customHttpHeadersProvider() {
        return (instance) -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("X-CUSTOM", "My Custom Value");
            return httpHeaders;
        };
    }


    /**
     * 拦截请求和响应日志打印
     * @return
     */
    @Bean
    public InstanceExchangeFilterFunction auditLog() {
        return (instance, request, next) -> next.exchange(request).doOnSubscribe((s) -> {
            if (HttpMethod.DELETE.equals(request.method()) || HttpMethod.POST.equals(request.method())) {
                log.info("{} for {} on {}", request.method(), instance.getId(), request.url());
            }
        });
    }

}
