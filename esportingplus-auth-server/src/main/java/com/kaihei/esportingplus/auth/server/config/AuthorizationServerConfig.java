package com.kaihei.esportingplus.auth.server.config;

import com.kaihei.esportingplus.auth.server.domain.service.ApplyClientDetailService;
import com.kaihei.esportingplus.auth.server.domain.service.UserDetailsServiceImpl;
import com.kaihei.esportingplus.auth.server.remote.rest.ExternalRestClient;
import com.kaihei.esportingplus.common.config.AuthorityropertiesConfig;
import com.kaihei.esportingplus.common.constant.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 *@Description: //提供/oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/7/31 22:03
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebResponseExceptionTranslator customWebResponseExceptionTranslator;

    @Autowired
    private AuthorityropertiesConfig authorityropertiesConfig;

    @Autowired
    public CustomRedisTokenStore redisTokenStore;

//    @Bean
//    public ClientDetailsService applyClientDetailService(){
//       return new ApplyClientDetailService();
//    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                //若无，refresh_token会有UserDetailsService is required错误
                .userDetailsService(userDetailsService)
                .tokenStore(redisTokenStore)
                .tokenServices(tokenServices())
                .exceptionTranslator(customWebResponseExceptionTranslator);
    }

    @Primary
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices
                .setAccessTokenValiditySeconds(authorityropertiesConfig.getAccess_token_expire());
        defaultTokenServices
                .setRefreshTokenValiditySeconds(authorityropertiesConfig.getRefresh_token_expire());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setTokenStore(redisTokenStore);
        return defaultTokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
        security.tokenKeyAccess("permitAll()");
        //url:/oauth/check_token allow check token
        security.checkTokenAccess("permitAll()");
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.withClientDetails(applyClientDetailService());
        clients.inMemory()
                .withClient(authorityropertiesConfig.getClient_id())
                .secret(authorityropertiesConfig.getClient_secret())
                .authorizedGrantTypes(authorityropertiesConfig.getGrant_types().toArray(new String[]{}))
                .scopes(authorityropertiesConfig.getScope())
//                 true 直接跳转到客户端页面，false 跳转到用户确认授权页面
                .autoApprove(authorityropertiesConfig.isAuto_approve());
    }
}