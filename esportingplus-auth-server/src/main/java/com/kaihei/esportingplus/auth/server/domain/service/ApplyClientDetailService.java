package com.kaihei.esportingplus.auth.server.domain.service;

import static com.kaihei.esportingplus.common.enums.BizExceptionEnum.INVALID_CLIENT;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.kaihei.esportingplus.auth.server.domain.entity.ClientDetails;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

//@Service
public class ApplyClientDetailService implements ClientDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @PostConstruct
    public void init(){
        redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer(ClientDetails.class));
    }

    private InheritableThreadLocal<ClientDetails> detailsThreadLocal = new InheritableThreadLocal<>();

    @Override
    public org.springframework.security.oauth2.provider.ClientDetails loadClientByClientId(String applyName)
            throws ClientRegistrationException {
        ClientDetails clientDetails = detailsThreadLocal.get();
        if(clientDetails == null){
            clientDetails = getClientDetail(applyName);
            detailsThreadLocal.set(clientDetails);
        }

        List<String> grantTypes = Arrays.asList(clientDetails.getAuthorizedGrantTypes().split(","));
        List<String> scopes = Arrays.asList(clientDetails.getScope().split(","));


        //目前只需要这些参数
        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId(clientDetails.getClientId());
        baseClientDetails.setClientSecret(clientDetails.getClientSecret());
        baseClientDetails.setScope(scopes);
        baseClientDetails.setAuthorizedGrantTypes(grantTypes);
        baseClientDetails.setAutoApproveScopes(Arrays.asList(clientDetails.getAutoapprove()));

//        BaseClientDetails baseClientDetails = new BaseClientDetails();
//        baseClientDetails.setClientId("ede2b0a310157e24e174ff60c1aee6a0");
//        baseClientDetails.setClientSecret("ede2b0a310157e24e174ff60c1aee6a0");
//        baseClientDetails.setScope(Arrays.asList("server"));
//        baseClientDetails.setAuthorizedGrantTypes(Arrays.asList("refresh_token","client_credentials","password","authorization_code"));
//        baseClientDetails.setAutoApproveScopes(Arrays.asList("true"));

        return baseClientDetails;
    }

    private ClientDetails getClientDetail(String clientId){
        ClientDetails clientDetails = (ClientDetails) redisTemplate.opsForHash()
                .get(RedisKey.CLIENT_DETAIL, clientId);
        if (clientDetails == null) {
            LOGGER.error("缓存中不存在此客户端信息：{}",clientId);
            throw new BusinessException(INVALID_CLIENT);
        }
        return clientDetails;
    }

}