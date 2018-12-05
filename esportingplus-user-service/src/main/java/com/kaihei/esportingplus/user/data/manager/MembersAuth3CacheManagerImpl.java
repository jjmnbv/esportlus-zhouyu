package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.user.bulider.HashRedisKeyBuilder;
import com.kaihei.esportingplus.user.bulider.RedisKeySegmentType;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.data.pyrepository.MembersAuth3Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @Title: MembersAuth3CacheManagerImpl
 * @Description: 第三方绑定缓存管理实现类
 * @date 2018/9/1120:01
 */
@Component
public class MembersAuth3CacheManagerImpl implements  MembersAuth3CacheManager{

    private static final Logger logger = LoggerFactory
            .getLogger(MembersAuth3CacheManagerImpl.class);

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private MembersAuth3Repository membersAuth3Repository;

    @Override
    public Integer getBindUserIdOpenid(String openid) {
        String key = HashRedisKeyBuilder
                .bulid(RedisKeySegmentType.HASH, openid, UserRedisKey.USER_BIND_MAPPING_USERID);
        return cacheManager.hget(key,openid,Integer.class);
    }

    @Override
    public void setBindUserIdOpenid(String openid,Integer userId) {
        String key = HashRedisKeyBuilder
                .bulid(RedisKeySegmentType.HASH, openid, UserRedisKey.USER_BIND_MAPPING_USERID);
        cacheManager.hset(key,openid,userId);
    }
}
