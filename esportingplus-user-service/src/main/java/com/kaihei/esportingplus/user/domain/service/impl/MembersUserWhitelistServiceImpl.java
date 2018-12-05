package com.kaihei.esportingplus.user.domain.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.data.pyrepository.MembersUserWhiteListRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersUserWhiteList;
import com.kaihei.esportingplus.user.domain.service.MembersUserWhitelistService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiekeqing
 * @Title: MembersUserWhitelistServiceImpl
 * @Description: 用户白名单服务
 * @date 2018/9/2017:47
 */
@Service
public class MembersUserWhitelistServiceImpl implements MembersUserWhitelistService {

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private MembersUserWhiteListRepository membersUserWhiteListRepository;

    @Override
    public boolean exists(long userId) {
        return cacheManager.sIsMember(UserRedisKey.USER_WHITELIST,String.valueOf(userId));
    }

    @Override
    public void initToCache() {
        boolean whitelistExists =  cacheManager.exists(UserRedisKey.USER_WHITELIST);
        if(!whitelistExists){
            List<MembersUserWhiteList> list =  membersUserWhiteListRepository.selectAll();
            final List<String> userIdList = new ArrayList<>();
            list.forEach(membersUserWhiteList ->{
                userIdList.add(String.valueOf(membersUserWhiteList.getUserId()));
            });

            if(userIdList.size()>0){
                cacheManager.sAdd(UserRedisKey.USER_WHITELIST,userIdList.toArray(new String[userIdList.size()]));
            }

        }
    }
}
