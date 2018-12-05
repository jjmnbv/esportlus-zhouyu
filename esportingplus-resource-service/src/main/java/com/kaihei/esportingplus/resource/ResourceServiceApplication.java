package com.kaihei.esportingplus.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

@RestController
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@MapperScan(basePackages = {"com.kaihei.esportingplus.resource.data.repository"})
public class ResourceServiceApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ResourceServiceApplication.class, args);
    }
}
