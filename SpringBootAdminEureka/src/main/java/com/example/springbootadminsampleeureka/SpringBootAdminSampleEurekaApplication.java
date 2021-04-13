package com.example.springbootadminsampleeureka;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * https://spring.io/projects/spring-cloud#overview
 * spring-cloud 与 spring boot 版本兼容:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class SpringBootAdminSampleEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminSampleEurekaApplication.class, args);
    }


    /**
     * 客户端配置
     */
    @Profile("insecure")
    @Configuration(proxyBeanMethods = false)
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {

        private final String adminContextPath;

        public SecurityPermitAllConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        /**
         * 释放断点
         * @param http
         * @throws Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests((authorizeRequests) -> authorizeRequests.anyRequest().permitAll())
                    .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .ignoringRequestMatchers(
                                    new AntPathRequestMatcher(this.adminContextPath + "/instances",
                                            HttpMethod.POST.toString()),
                                    new AntPathRequestMatcher(this.adminContextPath + "/instances/*",
                                            HttpMethod.DELETE.toString()),
                                    new AntPathRequestMatcher(this.adminContextPath + "/actuator/**")));

        }

    }

    /**
     * 服务端登录配置:
     */
    @Profile("secure")
    @Configuration(proxyBeanMethods = false)
    public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

        private final String adminContextPath;

        private final SecurityProperties security;

        public SecuritySecureConfig(AdminServerProperties adminServerProperties,SecurityProperties security) {
            this.adminContextPath = adminServerProperties.getContextPath();
            this.security = security;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setTargetUrlParameter("redirectTo");
            successHandler.setDefaultTargetUrl(this.adminContextPath + "/");

            http.authorizeRequests((authorizeRequests) -> authorizeRequests
                    .antMatchers(this.adminContextPath + "/assets/**").permitAll()
                    .antMatchers(this.adminContextPath + "/login").permitAll().anyRequest().authenticated())
                    .formLogin((formLogin) -> formLogin.loginPage(this.adminContextPath + "/login")
                            .successHandler(successHandler))
                    .logout((logout) -> logout.logoutUrl(this.adminContextPath + "/logout"))
                    .httpBasic(Customizer.withDefaults())
                    .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .ignoringRequestMatchers(
                                    new AntPathRequestMatcher(this.adminContextPath + "/instances",
                                            HttpMethod.POST.toString()),
                                    new AntPathRequestMatcher(this.adminContextPath + "/instances/*",
                                            HttpMethod.DELETE.toString()),
                                    new AntPathRequestMatcher(this.adminContextPath + "/actuator/**")));
        }

        /**
         * 记住我功能配置，当点击记住我时候，必须有以下配置，否则无法登录成功。
         * @param auth
         * @throws Exception
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser(security.getUser().getName())
                    .password("{noop}" + security.getUser().getPassword()).roles("USER");
        }

    }


}
