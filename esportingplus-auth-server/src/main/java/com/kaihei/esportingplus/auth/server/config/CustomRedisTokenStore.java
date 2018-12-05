package com.kaihei.esportingplus.auth.server.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.kaihei.esportingplus.auth.server.event.StoreUserEvent;
import com.kaihei.esportingplus.auth.server.remote.rest.ExternalRestClient;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.stereotype.Component;

@Component
public class CustomRedisTokenStore implements TokenStore {

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Value("${auth.refresh_token_expire}")
    private long refreshTokenExpire;

    @Value("${auth.access_token_expire}")
    private long accessTokenExpire;

    @Autowired
    private ExternalRestClient externalRestClient;

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private Jackson2JsonRedisSerializer serializationStrategy = new Jackson2JsonRedisSerializer(Object.class);
    private RedisTokenStoreSerializationStrategy authStrategy = new JdkSerializationStrategy();
    private Jackson2JsonRedisSerializer accessStrategy = new Jackson2JsonRedisSerializer(
            DefaultOAuth2AccessToken.class);
    private FastJsonRedisSerializer refreshStrategy = new FastJsonRedisSerializer(
            DefaultExpiringOAuth2RefreshToken.class);

    private StringRedisSerializer keyStrategy = new StringRedisSerializer();

    public CustomRedisTokenStore(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void setConnectionFactory(
            RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Jackson2JsonRedisSerializer getSerializationStrategy() {
        return serializationStrategy;
    }

    public void setSerializationStrategy(
            Jackson2JsonRedisSerializer serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    public StringRedisSerializer getKeyStrategy() {
        return keyStrategy;
    }

    public void setKeyStrategy(StringRedisSerializer keyStrategy) {
        this.keyStrategy = keyStrategy;
    }

    public RedisConnection getConnection() {
        return this.connectionFactory.getConnection();
    }

    private byte[] serialize(Object object) {
        return this.serializationStrategy.serialize(object);
    }

    private byte[] jdkSerialize(Object object) {
        return this.authStrategy.serialize(object);
    }

    private byte[] serializeKey(String object) {
        return this.serialize(object);
    }

    private OAuth2AccessToken deserializeAccessToken(byte[] bytes) {
        return (OAuth2AccessToken)this.accessStrategy.deserialize(bytes);
    }

    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return (OAuth2Authentication)this.authStrategy.deserialize(bytes,OAuth2Authentication.class);
    }

    private OAuth2RefreshToken deserializeRefreshToken(byte[] bytes) {
        return (OAuth2RefreshToken)this.refreshStrategy.deserialize(bytes);
    }

    private byte[] serialize(String string) {
        return this.keyStrategy.serialize(string);
    }

    private String deserializeKey(byte[] bytes) {
        return this.keyStrategy.deserialize(bytes);
    }

    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String key = this.authenticationKeyGenerator.extractKey(authentication);
        byte[] serializedKey = this.serializeKey("auth_to_access:" + key);
        byte[] bytes = null;
        RedisConnection conn = this.getConnection();
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }

        OAuth2AccessToken accessToken = this.deserializeAccessToken(bytes);
        if (accessToken != null && !key.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken.getValue())))) {
            this.storeAccessToken(accessToken, authentication);
        }

        return accessToken;
    }

    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return this.readAuthentication(token.getValue());
    }

    public OAuth2Authentication readAuthentication(String token) {
        byte[] bytes = null;
        RedisConnection conn = this.getConnection();

        try {
            bytes = conn.get(this.serializeKey("auth:" + token));
        } finally {
            conn.close();
        }

        OAuth2Authentication var4 = this.deserializeAuthentication(bytes);
        return var4;
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return this.readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        RedisConnection conn = this.getConnection();

        OAuth2Authentication var5;
        try {
            byte[] bytes = conn.get(this.serializeKey("refresh_auth:" + token));
            OAuth2Authentication auth = this.deserializeAuthentication(bytes);
            var5 = auth;
        } finally {
            conn.close();
        }

        return var5;
    }

    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {

        String pyToekn = authentication.getName();
        //同步py token校验
        String uid = externalRestClient.getUidByToken(pyToekn);
        UserSessionContext userDetail = new UserSessionContext();
        userDetail.setUid(uid);
        userDetail.setPythonToken(pyToekn);

        byte[] serializeUser = getSerializationStrategy().serialize(userDetail);

        //异步保存用户信息
        String javaToken = token.getValue();
        EventBus.post(new StoreUserEvent(pyToekn, javaToken,uid));

        byte[] userKey = this.serializeKey("user:" + javaToken);
        byte[] serializedAccessToken = this.serialize(token);
        byte[] serializedAuth = this.jdkSerialize(authentication);
        byte[] accessKey = this.serializeKey("access:" + javaToken);
        byte[] authKey = this.serializeKey("auth:" + javaToken);
        byte[] authToAccessKey = this.serializeKey("auth_to_access:" + this.authenticationKeyGenerator.extractKey(authentication));
        byte[] approvalKey = this.serializeKey("uname_to_access:" + getApprovalKey(authentication));
        byte[] clientId = this.serializeKey("client_id_to_access:" + authentication.getOAuth2Request().getClientId());
        RedisConnection conn = this.getConnection();

        try {
            conn.openPipeline();
            conn.set(accessKey, serializedAccessToken);
            conn.set(authKey, serializedAuth);
            conn.set(authToAccessKey, serializedAccessToken);
            conn.set(userKey, serializeUser);
            conn.expire(userKey, refreshTokenExpire);
            if (!authentication.isClientOnly()) {
                conn.rPush(approvalKey, new byte[][]{serializedAccessToken});
            }

            conn.rPush(clientId, new byte[][]{serializedAccessToken});
            conn.expire(accessKey, accessTokenExpire);
            conn.expire(authKey, accessTokenExpire);
            conn.expire(authToAccessKey, accessTokenExpire);
            conn.expire(clientId, accessTokenExpire);
            conn.expire(approvalKey, accessTokenExpire);

            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken != null && refreshToken.getValue() != null) {
                byte[] refresh = this.serialize(token.getRefreshToken().getValue());
                byte[] auth = this.serialize(javaToken);
                byte[] refreshToAccessKey = this.serializeKey("refresh_to_access:" + token.getRefreshToken().getValue());
                conn.set(refreshToAccessKey, auth);
                byte[] accessToRefreshKey = this.serializeKey("access_to_refresh:" + javaToken);
                conn.set(accessToRefreshKey, refresh);
//                if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
//                    ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken)refreshToken;
//                    Date expiration = expiringRefreshToken.getExpiration();
//                    if (expiration != null) {
//                        int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
                        conn.expire(refreshToAccessKey, refreshTokenExpire);
                        conn.expire(accessToRefreshKey, refreshTokenExpire);
//                    }
//                }
            }

            conn.closePipeline();
        } finally {
            conn.close();
        }

    }

    private static String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? "" : authentication.getUserAuthentication().getName();
        return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }

    private static String getApprovalKey(String clientId, String userName) {
        return clientId + (userName == null ? "" : ":" + userName);
    }

    public void removeAccessToken(OAuth2AccessToken accessToken) {
        this.removeAccessToken(accessToken.getValue());
    }

    public OAuth2AccessToken readAccessToken(String tokenValue) {
        byte[] key = this.serializeKey("access:" + tokenValue);
        byte[] bytes = null;
        RedisConnection conn = this.getConnection();

        try {
            bytes = conn.get(key);
        } finally {
            conn.close();
        }

        OAuth2AccessToken var5 = this.deserializeAccessToken(bytes);
        return var5;
    }

    public void removeAccessToken(String tokenValue) {
        byte[] accessKey = this.serializeKey("access:" + tokenValue);
        byte[] userKey = this.serializeKey("user:" + tokenValue);
        byte[] authKey = this.serializeKey("auth:" + tokenValue);
        byte[] accessToRefreshKey = this.serializeKey("access_to_refresh:" + tokenValue);
        RedisConnection conn = this.getConnection();

        try {
            conn.openPipeline();
            conn.get(accessKey);
            conn.get(authKey);
            conn.del(new byte[][]{accessKey});
            conn.del(new byte[][]{userKey});
            conn.del(new byte[][]{accessToRefreshKey});
            conn.del(new byte[][]{authKey});
            List<Object> results = conn.closePipeline();
            byte[] access = (byte[])((byte[])results.get(0));
            byte[] auth = (byte[])((byte[])results.get(1));
            OAuth2Authentication authentication = this.deserializeAuthentication(auth);
            if (authentication != null) {
                String key = this.authenticationKeyGenerator.extractKey(authentication);
                byte[] authToAccessKey = this.serializeKey("auth_to_access:" + key);
                byte[] unameKey = this.serializeKey("uname_to_access:" + getApprovalKey(authentication));
                byte[] clientId = this.serializeKey("client_id_to_access:" + authentication.getOAuth2Request().getClientId());
                conn.openPipeline();
                conn.del(new byte[][]{authToAccessKey});
                conn.lRem(unameKey, 1L, access);
                conn.lRem(clientId, 1L, access);
                conn.del(new byte[][]{this.serialize("access:" + key)});
                conn.closePipeline();
            }
        } finally {
            conn.close();
        }

    }

    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        byte[] refreshKey = this.serializeKey("refresh:" + refreshToken.getValue());
        byte[] refreshAuthKey = this.serializeKey("refresh_auth:" + refreshToken.getValue());
        byte[] serializedRefreshToken = this.serialize((Object)refreshToken);
        RedisConnection conn = this.getConnection();

        try {
            conn.openPipeline();
            conn.set(refreshKey, serializedRefreshToken);
            conn.set(refreshAuthKey, this.jdkSerialize((Object)authentication));
//            if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
//                ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken)refreshToken;
//                Date expiration = expiringRefreshToken.getExpiration();
//                if (expiration != null) {
//                    int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
                    conn.expire(refreshKey, refreshTokenExpire);
                    conn.expire(refreshAuthKey, refreshTokenExpire);
//                }
//            }

            conn.closePipeline();
        } finally {
            conn.close();
        }

    }

    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        byte[] key = this.serializeKey("refresh:" + tokenValue);
        byte[] bytes = null;
        RedisConnection conn = this.getConnection();

        try {
            bytes = conn.get(key);
        } finally {
            conn.close();
        }

        OAuth2RefreshToken var5 = this.deserializeRefreshToken(bytes);
        return var5;
    }

    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        this.removeRefreshToken(refreshToken.getValue());
    }

    public void removeRefreshToken(String tokenValue) {
        byte[] refreshKey = this.serializeKey("refresh:" + tokenValue);
        byte[] refreshAuthKey = this.serializeKey("refresh_auth:" + tokenValue);
        byte[] refresh2AccessKey = this.serializeKey("refresh_to_access:" + tokenValue);
        byte[] access2RefreshKey = this.serializeKey("access_to_refresh:" + tokenValue);
        RedisConnection conn = this.getConnection();

        try {
            conn.openPipeline();
            conn.del(new byte[][]{refreshKey});
            conn.del(new byte[][]{refreshAuthKey});
            conn.del(new byte[][]{refresh2AccessKey});
            conn.del(new byte[][]{access2RefreshKey});
            conn.closePipeline();
        } finally {
            conn.close();
        }

    }

    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        this.removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
        byte[] key = this.serializeKey("refresh_to_access:" + refreshToken);
        List<Object> results = null;
        RedisConnection conn = this.getConnection();

        try {
            conn.openPipeline();
            conn.get(key);
            conn.del(new byte[][]{key});
            results = conn.closePipeline();
        } finally {
            conn.close();
        }

        if (results != null) {
            byte[] bytes = (byte[])((byte[])results.get(0));
            String accessToken = this.deserializeKey(bytes);
            if (accessToken != null) {
                this.removeAccessToken(accessToken);
            }

        }
    }

    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        byte[] approvalKey = this.serializeKey("uname_to_access:" + getApprovalKey(clientId, userName));
        List<byte[]> byteList = null;
        RedisConnection conn = this.getConnection();

        try {
            byteList = conn.lRange(approvalKey, 0L, -1L);
        } finally {
            conn.close();
        }

        if (byteList != null && byteList.size() != 0) {
            List<OAuth2AccessToken> accessTokens = new ArrayList(byteList.size());
            Iterator var7 = byteList.iterator();

            while(var7.hasNext()) {
                byte[] bytes = (byte[])var7.next();
                OAuth2AccessToken accessToken = this.deserializeAccessToken(bytes);
                accessTokens.add(accessToken);
            }

            return Collections.unmodifiableCollection(accessTokens);
        } else {
            return Collections.emptySet();
        }
    }

    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        byte[] key = this.serializeKey("client_id_to_access:" + clientId);
        List<byte[]> byteList = null;
        RedisConnection conn = this.getConnection();

        try {
            byteList = conn.lRange(key, 0L, -1L);
        } finally {
            conn.close();
        }

        if (byteList != null && byteList.size() != 0) {
            List<OAuth2AccessToken> accessTokens = new ArrayList(byteList.size());
            Iterator var6 = byteList.iterator();

            while(var6.hasNext()) {
                byte[] bytes = (byte[])var6.next();
                OAuth2AccessToken accessToken = this.deserializeAccessToken(bytes);
                accessTokens.add(accessToken);
            }

            return Collections.unmodifiableCollection(accessTokens);
        } else {
            return Collections.emptySet();
        }
    }
}
