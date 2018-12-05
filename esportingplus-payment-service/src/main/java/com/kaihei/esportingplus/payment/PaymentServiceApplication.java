package com.kaihei.esportingplus.payment;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.maihaoche.starter.mq.annotation.EnableMQConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 支付服务程序入口
 *
 * @author haycco
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableMQConfiguration
@EnableTransactionManagement
@EnableAsync
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
public class PaymentServiceApplication {


    public static void main(String[] args) {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
