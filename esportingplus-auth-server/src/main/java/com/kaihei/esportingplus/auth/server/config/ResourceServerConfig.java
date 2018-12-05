package com.kaihei.esportingplus.auth.server.config;

import static com.kaihei.esportingplus.common.constant.SecurityConstants.LOG_OUT;
import static com.kaihei.esportingplus.common.constant.SecurityConstants.OAUTH_AUTHORIZE_URL;
import static com.kaihei.esportingplus.common.constant.SecurityConstants.OAUTH_TOKEN_URL;

import com.kaihei.esportingplus.auth.server.common.AuthExceptionEntryPoint;
import com.kaihei.esportingplus.auth.server.common.CustomAccessDeniedHandler;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//            .requestMatcher(new OAuth2RequestedMatcher())
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response
                        .sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
//            .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler);
    }
}