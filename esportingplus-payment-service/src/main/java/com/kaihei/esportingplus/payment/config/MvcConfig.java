package com.kaihei.esportingplus.payment.config;

import com.kaihei.esportingplus.payment.config.converter.StringToDateConverter;
import com.kaihei.esportingplus.payment.config.converter.StringToDateTimeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: tangtao
 **/
@Configuration
public class MvcConfig {

    @Bean
    public StringToDateConverter stringToDateConverter() {
        return new StringToDateConverter();
    }

    @Bean
    public StringToDateTimeConverter stringToDateTimeConverter() {
        return new StringToDateTimeConverter();
    }
}
