package com.kaihei.esportingplus.marketing;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.maihaoche.starter.mq.annotation.EnableMQConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 营销活动服务程序入口
 *
 * @author haycco
 */
@RestController
@SpringBootApplication
@EnableDiscoveryClient
@EnableMQConfiguration
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@MapperScan(basePackages = {"com.kaihei.esportingplus.marketing.data.repository"})
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
public class MarketingServiceApplication {

    public static void main(String[] args) {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
        SpringApplication.run(MarketingServiceApplication.class, args);
    }

}
