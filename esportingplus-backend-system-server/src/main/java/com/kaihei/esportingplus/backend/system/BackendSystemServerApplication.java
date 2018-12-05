package com.kaihei.esportingplus.backend.system;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.maihaoche.starter.mq.annotation.EnableMQConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

/**
 * 核心服务程序入口
 *
 * @author xiekeqing
 */
@RestController
@SpringBootApplication
@EnableDiscoveryClient
@EnableMQConfiguration
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@MapperScan(basePackages = {"com.kaihei.esportingplus.backend.system.data.repository"})
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
public class BackendSystemServerApplication {

    public static void main(String[] args) {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
        SpringApplication.run(BackendSystemServerApplication.class, args);
    }
}
