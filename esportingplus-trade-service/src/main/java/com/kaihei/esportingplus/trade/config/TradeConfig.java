package com.kaihei.esportingplus.trade.config;

import com.kaihei.esportingplus.common.config.SnowFlakeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TradeConfig {
    @Autowired
    private SnowFlakeConfig snowFlakeConfig;
//    @Bean
//    public SnowFlake snowFlake(){
//        return  new SnowFlake(snowFlakeConfig.getDataCenter(),
//                snowFlakeConfig.getMachine());
//    }
}
