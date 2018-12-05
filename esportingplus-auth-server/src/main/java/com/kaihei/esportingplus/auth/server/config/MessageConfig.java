package com.kaihei.esportingplus.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 *@Description: oauth 国际化
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/8/28 17:17
*/
@Configuration
public class MessageConfig {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message/messages_zh_CN");
        return messageSource;
    }
}
