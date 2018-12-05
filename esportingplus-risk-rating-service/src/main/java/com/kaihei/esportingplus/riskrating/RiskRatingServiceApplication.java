package com.kaihei.esportingplus.riskrating;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.riskrating.service.RiskDictService;
import com.maihaoche.starter.mq.annotation.EnableMQConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 风控中心程序入口
 *
 * @author haycco
 */
@RestController
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
@EnableMQConfiguration
public class RiskRatingServiceApplication {
  public static void main(String[] args) {
    EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    ConfigurableApplicationContext context = SpringApplication
            .run(RiskRatingServiceApplication.class, args);
    RiskDictService riskDictService = context.getBean(RiskDictService.class);
    riskDictService.initDataToRedis();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
