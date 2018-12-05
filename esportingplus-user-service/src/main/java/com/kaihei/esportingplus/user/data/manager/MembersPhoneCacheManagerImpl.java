package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.user.bulider.RedisKeySegmentBuilder;
import com.kaihei.esportingplus.user.bulider.RedisKeySegmentType;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiekeqing
 * @Title: membersPhoneCacheManagerImpl
 * @Description: TODO
 * @date 2018/9/2115:01
 */
@Component
public class MembersPhoneCacheManagerImpl implements MembersPhoneCacheManager {

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private MembersUserRepository membersUserRepository;

    @Override
    public Integer getUserIdByPhone(String phone) {
        String key = RedisKeySegmentBuilder
                .bulid(RedisKeySegmentType.HASH, phone, UserRedisKey.USER_BIND_MAPPING_USERID);
        Integer userId = cacheManager.hget(key, phone, Integer.class);
        if (userId == null || userId.intValue() <= 0) {
            MembersUser membersUser = membersUserRepository.selectByPhone(phone);
            if (membersUser != null) {
                userId = membersUser.getId();
                cacheManager.hset(key, phone, userId);
            }
        }
        return userId;
    }

    @Override
    public void updatePhoneBindUserId(String oldPhone, String newPhone, Integer userId) {
        String key = RedisKeySegmentBuilder
                .bulid(RedisKeySegmentType.HASH, oldPhone, UserRedisKey.USER_BIND_MAPPING_USERID);
        cacheManager.hdel(key,oldPhone);
        key = RedisKeySegmentBuilder
                .bulid(RedisKeySegmentType.HASH, newPhone, UserRedisKey.USER_BIND_MAPPING_USERID);
        cacheManager.hset(key, newPhone, userId);
    }
}
