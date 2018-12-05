package com.kaihei.esportingplus.customer.center;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@RestController
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
@MapperScan(basePackages = {"com.kaihei.esportingplus.customer.center.data.repository"})
@EnableSwagger2
public class CustomerCenterServiceApplication {

    public static void main(String[] args) {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
        SpringApplication.run(CustomerCenterServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
