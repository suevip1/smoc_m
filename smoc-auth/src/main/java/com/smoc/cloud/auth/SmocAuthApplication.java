package com.smoc.cloud.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * OAuth2认证授权服务
 * @ EnableDiscoveryClient 启用服务注册发现
 */
@SpringBootApplication
@EnableDiscoveryClient
class SmocAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmocAuthApplication.class, args);
    }

}
