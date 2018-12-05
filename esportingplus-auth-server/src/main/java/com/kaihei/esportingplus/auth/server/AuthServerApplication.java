package com.kaihei.esportingplus.auth.server;

import com.kaihei.esportingplus.auth.server.init.InitAuthUrl;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.kaihei.esportingplus.**"})
//@EnableCircuitBreaker
//@RefreshScope
@ComponentScan(basePackages = {"com.kaihei.esportingplus.**"})
@MapperScan(basePackages = {"com.kaihei.esportingplus.auth.server.data.repository"})
public class AuthServerApplication {

    public static void main(String[] args) {
        EventBus.create(ThreadPoolManager.INSTANCE.getTokenBusExecutor());
        ApplicationContext context = SpringApplication.run(AuthServerApplication.class, args);

        //预热接口路径到redis，默认为全部
        InitAuthUrl initAuthUrl = context.getBean(InitAuthUrl.class);
        initAuthUrl.init();
        //预热客户端信息，默认为全部
//        InitClientDetail initClientDetail = context.getBean(InitClientDetail.class);
//        initClientDetail.init();
    }
}
