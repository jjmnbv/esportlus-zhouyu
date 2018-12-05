package com.kaihei.esportingplus.user.domain.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.bulider.RedisKeySegmentBuilder;
import com.kaihei.esportingplus.user.bulider.RedisKeySegmentType;
import com.kaihei.esportingplus.user.config.UserProperties;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.constant.UserRedisKey;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.service.AuthTokenService;
import com.kaihei.esportingplus.user.domain.service.MembersUserService;
import java.util.UUID;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Pipeline;

/**
 * 访问token管理服务
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/29 16:12
 */
@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    private final static String PYTHON_TOKEN = "%s.%s";
    private final static String PYTHON_TOKEN_SEPARATOR = ".";
    private final static String PYTHON_TOKEN_REGEX = "\\.";

    @Resource(name = "pythonCacheManager")
    private CacheManager pythonCacheManager;

    @Value("${auth.refresh_token_expire}")
    private int refreshTokenExpire;

    @Value("${auth.access_token_expire}")
    private int accessTokenExpire;

    private static final CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private UserProperties userProperties;

    @Autowired
    private MembersUserService membersUserService;

    @Override
    public String getAccessToken(String uid, String version) {
        String tokenType = getTokenType(version);

        //生成并返回访问token
        String token = storeAccessToken(uid, tokenType);

        //生成刷新token
        storeRefreshToken(token);

        //刷新用户映射token，需要单点登陆
        refreshUidMapingToken(uid, token);

        return String.format(PYTHON_TOKEN, uid, token);
    }

    @Override
    public String refreshToken(String pythonToken, String version) {
        Pair<String, String> tokeninfoPair = parsePythonToken(pythonToken);
        String tokenType = getTokenType(version);

        String accessToRefreshKey = String
                .format(UserRedisKey.ACCESS_TO_REFRESH_KEY, tokeninfoPair.getRight());
        boolean refreshTokenExists = cacheManager.exists(accessToRefreshKey);
        if (refreshTokenExists) {
            //生成并返回访问token
            String token = storeAccessToken(tokeninfoPair.getLeft(), tokenType);
            //修改原有刷新token名称
            String oldkey = String
                    .format(UserRedisKey.ACCESS_TO_REFRESH_KEY, tokeninfoPair.getRight());
            String newkey = String.format(UserRedisKey.ACCESS_TO_REFRESH_KEY, token);
            cacheManager.rename(oldkey, newkey);

            //刷新用户映射token，需要单点登陆
            refreshUidMapingToken(tokeninfoPair.getLeft(), token);

            return String.format(PYTHON_TOKEN, tokeninfoPair.getLeft(), token);
        }

        return null;
    }

    @Override
    public Boolean removeAccessToken(String pythonToken, String version) {
        Pair<String, String> tokeninfoPair = parsePythonToken(pythonToken);
        if (tokeninfoPair != null) {
            String userKey = String.format(UserRedisKey.USER_LOGIN_KEY, tokeninfoPair.getRight());
            String accessKey = String
                    .format(UserRedisKey.ACCESS_TOKEN_KEY, tokeninfoPair.getRight());
            String accessToRefreshKey = String
                    .format(UserRedisKey.ACCESS_TOKEN_KEY, tokeninfoPair.getRight());
            Pipeline pipeline = cacheManager.pipelined();
            pipeline.del(userKey);
            pipeline.del(accessKey);
            pipeline.del(accessToRefreshKey);
            pipeline.sync();

            //删除python token
            String pythonTokenKey = String
                    .format(UserRedisKey.PYTHON_LOGIN_TOKEN_KEY, getTokenType(version),
                            tokeninfoPair.getLeft());
            pythonCacheManager.del(pythonTokenKey);

            return true;
        }

        return false;
    }

    private Pair<String, String> parsePythonToken(String pythonToken) {
        if (StringUtils.isNotBlank(pythonToken) || pythonToken.contains(PYTHON_TOKEN_SEPARATOR)) {
            String[] tokenInfos = pythonToken.split(PYTHON_TOKEN_REGEX);
            return MutablePair.of(tokenInfos[0], tokenInfos[1]);
        }
        return null;
    }

    private String getTokenType(String version) {
        if (StringUtils.isNotBlank(version)) {
            String channel = version.split("_")[0];
            if ("i".equals(channel) || "a".equals(channel)) {
                return MembersAuthConstants.APP_TOKEN;
            } else if ("mp".equals(channel)) {
                return MembersAuthConstants.MP_TOKEN;
            } else if ("wpa".equals(channel)) {
                return MembersAuthConstants.WPA_TOKEN;
            }
        }
        return MembersAuthConstants.UNKOWN_TOKEN;
    }

    private String storeAccessToken(String uid, String tokenType) {

        //access token
        String token = randomToken();

        //返回终端的python token
        String returnPythonToken = String.format(PYTHON_TOKEN, uid, token);

        UserSessionContext userDetial = getUserSessionContext(uid, returnPythonToken);

        //设置访问用户信息和访问token到redis
        String userKey = String.format(UserRedisKey.USER_LOGIN_KEY, token);
        String accessKey = String.format(UserRedisKey.ACCESS_TOKEN_KEY, token);
        int expire = getAccessTokenExpire(tokenType);
        Pipeline pipeline = cacheManager.pipelined();
        pipeline.set(accessKey, token);
        pipeline.set(userKey, JsonsUtils.toJson(userDetial));
        pipeline.expire(accessKey, expire);
        pipeline.expire(userKey, expire);
        pipeline.sync();

        storePythonAccessToken(uid, tokenType, token, expire);

        return token;
    }

    private void storeRefreshToken(String token) {
        String accessToRefreshKey = String.format(UserRedisKey.ACCESS_TO_REFRESH_KEY, token);
        Pipeline pipeline = cacheManager.pipelined();
        pipeline.set(accessToRefreshKey, token);
        pipeline.expire(accessToRefreshKey, refreshTokenExpire);
        pipeline.sync();
    }

    private UserSessionContext getUserSessionContext(String uid, String pyToken) {
        MembersUser memberUser = membersUserService.getMembersUserByUid(uid);
        UserSessionContext userDetail = new UserSessionContext();
        userDetail.setUid(uid);
        userDetail.setId(memberUser.getId());
        userDetail.setUsername(memberUser.getUsername());
        userDetail.setAvatar(memberUser.getThumbnail());
        userDetail.setSex(memberUser.getSex());
        userDetail.setChickenId(memberUser.getChickenId());
        userDetail.setPythonToken(pyToken);
        return userDetail;
    }

    private void storePythonAccessToken(String uid, String tokenType, String token, int expire) {
        String key = String.format(UserRedisKey.PYTHON_LOGIN_TOKEN_KEY, tokenType, uid);

        Pipeline pipeline = pythonCacheManager.pipelined();
        pipeline.set(key, token);
        pipeline.expire(key, expire);
        pipeline.sync();
    }

    private int getAccessTokenExpire(String tokenType) {
        int expire = accessTokenExpire;
        if (MembersAuthConstants.MP_TOKEN.equals(tokenType)) {
            expire = userProperties.getUserMpTokenExpire();
        } else if (MembersAuthConstants.APP_TOKEN.equals(tokenType)) {
            expire = userProperties.getUserAppTokenExpire();
        } else if (MembersAuthConstants.PC_TOKEN.equals(tokenType)) {
            expire = userProperties.getUserPcTokenExpire();
        }
        return expire;
    }

    private String randomToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void refreshUidMapingToken(String uid, String newToken) {
        String key = RedisKeySegmentBuilder.bulid(RedisKeySegmentType.HASH, uid,
                UserRedisKey.USER_UID_MAPPING_TOKEN);
        String oldToken = cacheManager.hget(key, uid, String.class);
        //清除原有token
        if (StringUtils.isNotBlank(oldToken)) {
            Pipeline pipeline = cacheManager.pipelined();
            String userKey = String.format(UserRedisKey.USER_LOGIN_KEY, oldToken);
            String accessKey = String.format(UserRedisKey.ACCESS_TOKEN_KEY, oldToken);
            String accessToRefreshKey = String.format(UserRedisKey.ACCESS_TO_REFRESH_KEY, oldToken);
            pipeline.del(userKey);
            pipeline.del(accessKey);
            pipeline.del(accessToRefreshKey);
            pipeline.sync();
        }

        //设置新关联token
        cacheManager.hset(key, uid, newToken);
    }


}
