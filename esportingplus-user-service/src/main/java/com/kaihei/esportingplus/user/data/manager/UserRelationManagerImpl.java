package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import org.springframework.stereotype.Component;

/**
 * @author liuyang
 * @Description
 * @Date 2018/12/3 10:35
 **/
@Component
public class UserRelationManagerImpl implements UserRelationManager {

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Override
    public void follow(String uid, String followId, Boolean isFriend) {
        Unread unread = cacheManager.hget(UserRedisKey.RELATION_UNREADNUM, followId, Unread.class);
        if (unread == null) {
            unread = new Unread();
        }

        unread.addFans();
        if (isFriend) {
            unread.addFriend();
        }
        cacheManager.hset(UserRedisKey.RELATION_UNREADNUM, followId, unread.toJson());

        if (isFriend) {
            Unread fread = cacheManager.hget(UserRedisKey.RELATION_UNREADNUM, uid, Unread.class);
            if (fread == null) {
                fread = new Unread();
            }

            fread.addFriend();
            cacheManager.hset(UserRedisKey.RELATION_UNREADNUM, uid, fread.toJson());
        }
    }

    @Override
    public void unFollow(String uid, String followId, Boolean isFriend) {
        Unread unread = cacheManager.hget(UserRedisKey.RELATION_UNREADNUM, followId, Unread.class);
        if (unread != null) {
            unread.subFans();
            if (isFriend) {
                unread.subFriend();
            }
            cacheManager.hset(UserRedisKey.RELATION_UNREADNUM, followId, unread.toJson());
        }


        Unread fread = cacheManager.hget(UserRedisKey.RELATION_UNREADNUM, uid, Unread.class);
        if (fread != null) {
            if (isFriend) {
                fread.subFriend();
            }
            cacheManager.hset(UserRedisKey.RELATION_UNREADNUM, uid, fread.toJson());
        }
    }

    @Override
    public String getUnreadNum(String uid) {
        Unread unread = cacheManager.hget(UserRedisKey.RELATION_UNREADNUM, uid, Unread.class);
        if (unread == null) {
            unread = new Unread();
        }

        return unread.toJson();
    }

    @Override
    public void clearNum(String uid, Integer type) {
        Unread fread = cacheManager.hget(UserRedisKey.RELATION_UNREADNUM, uid, Unread.class);
        if (fread == null) {
            return;
        }

        if (type.intValue() == 1) {
            //清空粉丝
            fread.clearFans();
        } else if (type.intValue() == 2) {
            //清空好友
            fread.clearFriend();
        }

        //判断是否都为0， 都为0去除缓存
        if (fread.isAllNull()){
            cacheManager.hdel(UserRedisKey.RELATION_UNREADNUM, uid);
            return ;
        }

        cacheManager.hset(UserRedisKey.RELATION_UNREADNUM, uid, fread.toJson());
    }

    static class Unread {
        private Integer fans = 0;
        private Integer friend = 0;

        public void addFans() {
            this.fans = this.fans + 1;
        }

        public void subFans() {
            this.fans = Math.max(0, this.fans - 1);
        }

        public void addFriend() {
            this.friend = this.friend + 1;
        }

        public void subFriend() {
            this.friend = Math.max(0, this.friend - 1);
        }

        public void clearFans() {
            this.fans = 0;
        }

        public void clearFriend() {
            this.friend = 0;
        }

        public boolean isAllNull() {
            return this.fans == 0 && this.friend == 0;
        }

        public String toJson() {
            return JacksonUtils.toJson(this);
        }

        public Integer getFans() {
            return fans;
        }

        public void setFans(Integer fans) {
            this.fans = fans;
        }

        public Integer getFriend() {
            return friend;
        }

        public void setFriend(Integer friend) {
            this.friend = friend;
        }
    }
}
