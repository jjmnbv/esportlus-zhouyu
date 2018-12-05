package com.kaihei.esportingplus.core;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 核心服务程序入口
 *
 * @author xiekeqing
 */
@RestController
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@MapperScan(basePackages = {"com.kaihei.esportingplus.core.data.repository"})
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
public class CoreServiceApplication {

    public static void main(String[] args) {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
        SpringApplication.run(CoreServiceApplication.class, args);
    }
}
