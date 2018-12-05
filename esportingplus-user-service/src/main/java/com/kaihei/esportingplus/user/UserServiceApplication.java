package com.kaihei.esportingplus.user;


import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.maihaoche.starter.mq.annotation.EnableMQConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableDiscoveryClient
@EnableMQConfiguration
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
//@MapperScan(basePackages = {"com.kaihei.esportingplus.user.data.repository"})
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
public class UserServiceApplication {
    
    public static void main(String[] args) {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
