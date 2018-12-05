//package com.kaihei.esportingplus.auth.server.init;
//
//
//import com.kaihei.commons.cache.api.spi.common.CacheManager;
//import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
//import com.kaihei.esportingplus.auth.server.domain.entity.ClientDetails;
//import com.kaihei.esportingplus.auth.server.domain.service.IClientDetailsService;
//import com.kaihei.esportingplus.common.constant.RedisKey;
//import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
//import com.kaihei.esportingplus.common.exception.BusinessException;
//import com.kaihei.esportingplus.common.tools.FastJsonUtils;
//import java.util.List;
//import org.apache.commons.collections.CollectionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.oauth2.provider.client.BaseClientDetails;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.Pipeline;
//
//@Component
//public class InitClientDetail {
//
//    private Logger LOGGER = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    @Qualifier("myclientDetailsService")
//    private IClientDetailsService clientDetailsService;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    /**
//     * @Description: 系统启动的时候预热客户端信息到redis
//     * @author Orochi-Yzh
//     * @dateTime 2018/8/4 11:33
//     */
//    public void init() {
//        LOGGER.info("预热客户端信息缓存....");
//        RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
//        try {
//            List<ClientDetails> baseClientDetails = clientDetailsService.listAll();
//            if (CollectionUtils.isNotEmpty(baseClientDetails)) {
//                conn.openPipeline();
//                for (ClientDetails client : baseClientDetails) {
//                    conn.hSet(RedisKey.CLIENT_DETAIL.getBytes(),client.getClientId().getBytes(), FastJsonUtils.toJson(client).getBytes());
//                }
//
//            } else {
//                throw new BusinessException(BizExceptionEnum.INIT_AUTHURL_ERROR);
//            }
//            conn.closePipeline();
//        } catch (Exception e) {
//            throw new BusinessException(BizExceptionEnum.INIT_AUTHURL_ERROR, e);
//        }finally {
//            conn.close();
//        }
//        LOGGER.info("客户端信息预热完毕");
//
//    }
//}
