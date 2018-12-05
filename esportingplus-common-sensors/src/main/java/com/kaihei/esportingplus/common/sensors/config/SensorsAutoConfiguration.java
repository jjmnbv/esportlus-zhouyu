package com.kaihei.esportingplus.common.sensors.config;

import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author liuyang
 * @Description  神策
 * @Date 2018/11/13 15:11
 **/
@Configuration
public class SensorsAutoConfiguration {
    private Logger logger = LoggerFactory.getLogger(SensorsAutoConfiguration.class);

    @Value("${sensors.analytics.saLogPath}")
    private String saLogPath;


    @Bean
    public SensorsAnalytics sensorsAnalytics() {
        SensorsAnalytics sensorsAnalytics = null;
        try {
            sensorsAnalytics = new SensorsAnalytics(
                    new SensorsAnalytics.BatchConsumer(saLogPath));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sensorsAnalytics;
    }

}
