package com.kaihei.esportingplus.gamingteam;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.maihaoche.starter.mq.annotation.EnableMQConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 车队服务
 * @author liangyi
 */
@RestController
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement(order = 1)
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
@MapperScan(basePackages = {"com.kaihei.esportingplus.gamingteam.data.repository"})
@EnableMQConfiguration
public class GamingTeamServiceApplication {

    public static void main(String[] args) {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
        SpringApplication.run(GamingTeamServiceApplication.class, args);
    }
}
