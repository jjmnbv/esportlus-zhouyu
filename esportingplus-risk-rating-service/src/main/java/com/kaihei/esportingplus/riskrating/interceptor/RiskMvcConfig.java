//package com.kaihei.esportingplus.riskrating.interceptor;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * 添加拦截器
// * @author chenzhenjun
// */
//@Configuration
//public class RiskMvcConfig extends WebMvcConfigurerAdapter {
//
//    private static String[] AUTH_URL = {"/im_machine/login_register","/im_machine/register_check","/freeteam/reward","/freeteam/chance"};
//
//    @Bean
//    public RiskSwitchInterceptor riskSwitchInterceptor() {
//        return new RiskSwitchInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(riskSwitchInterceptor()).addPathPatterns(AUTH_URL);
//        super.addInterceptors(registry);
//    }
//}
