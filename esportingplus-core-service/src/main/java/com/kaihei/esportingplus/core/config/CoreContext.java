package com.kaihei.esportingplus.core.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author liuyang
 * @Description coreContext
 * @Date 2018/11/2 11:19
 **/
@Component
public class CoreContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
         context = applicationContext;
    }

    public static ApplicationContext context(){
        return context;
    }
}
