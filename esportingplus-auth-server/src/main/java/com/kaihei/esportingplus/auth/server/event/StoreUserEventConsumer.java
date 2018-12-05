package com.kaihei.esportingplus.auth.server.event;

import com.alibaba.fastjson.TypeReference;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.auth.server.config.CustomRedisTokenStore;
import com.kaihei.esportingplus.auth.server.remote.rest.ExternalRestClient;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Component;

@Component
public class StoreUserEventConsumer extends EventConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ExternalRestClient externalRestClient;

    @Autowired
    private CustomRedisTokenStore customRedisTokenStore;

    @Value("${auth.refresh_token_expire}")
    private int tokenExpire;

    @Subscribe
    @AllowConcurrentEvents //开启线程安全
    public void store(StoreUserEvent event) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("开始异步缓存用户信息");
        }
        RedisConnection conn = customRedisTokenStore.getConnection();
        String uid = event.getUid();

        ResponsePacket userPaket = externalRestClient.getUserDetailByUid(uid);
        UserSessionContext userDetail = FastJsonUtils.fromMap(userPaket.getData(),new TypeReference<UserSessionContext>(){});
        if(userDetail != null){
            userDetail.setPythonToken(event.getPyToken());

            byte[] serializeUser = customRedisTokenStore.getSerializationStrategy().serialize(userDetail);
            byte[] userKey = customRedisTokenStore.getKeyStrategy().serialize("user:" + event.getToken());

            try {
                conn.openPipeline();
                conn.set(userKey, serializeUser);
                conn.expire(userKey, tokenExpire);
                conn.closePipeline();
            } finally {
                conn.close();
            }
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("异步缓存用户信息完毕");
            }
        }else{
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("用户信息 反序列化后为空。");
            }
        }

    }

}
