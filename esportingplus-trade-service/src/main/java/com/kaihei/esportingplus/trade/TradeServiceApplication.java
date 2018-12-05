package com.kaihei.esportingplus.trade;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.maihaoche.starter.mq.annotation.EnableMQConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 订单服务
 * @author liangyi
 */
@EnableSwagger2
@RestController
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
@MapperScan(basePackages = {"com.kaihei.esportingplus.trade.data.repository"})
@EnableMQConfiguration
public class TradeServiceApplication {

    public static void main(String[] args) {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
        SpringApplication.run(TradeServiceApplication.class, args);
    }
}
