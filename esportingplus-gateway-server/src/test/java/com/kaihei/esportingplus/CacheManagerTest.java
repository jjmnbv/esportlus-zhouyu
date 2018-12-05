package com.kaihei.esportingplus;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

/**
 * Unit test for simple App.
 */
public class CacheManagerTest
{
    private CacheManager cacheManager;
    @Before
    public void initCacheManager()
    {
        Properties prop = new Properties();
        prop.setProperty("redis.cluster-model", "single");
        prop.setProperty("redis.nodes", "[\"120.79.211.158:6380\"]");
        prop.setProperty("redis.password", "");
        prop.setProperty("redis.database", "0");
        prop.setProperty("redis.maxTotal", String.valueOf(2000));
        prop.setProperty("redis.minIdle", String.valueOf(1));
        prop.setProperty("redis.maxIdle", String.valueOf(10));
        prop.setProperty("redis.maxWaitMillis", String.valueOf(20000));
        prop.setProperty("redis.evictorShutdownTimeoutMillis", "60000");
        prop.setProperty("redis.testOnBorrow", "true");
        CacheManagerFactory.create().init(prop);
        cacheManager = CacheManagerFactory.create();
    }

    @Test
    public void testLua() throws IOException {
        System.out.println("开始....");
        int ttl = new Random().nextInt(3) + 1;
//        int ttl = 1;
        int qps =  1600;
        int qpsTotal = qps * ttl;
        System.out.println("ttl : " + ttl);
        System.out.println("qpsTotal : " + qpsTotal);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 30000; i++) {
            String limitLua = Files
                    .toString(ResourceUtils.getFile("classpath:rateLimit.lua"),
                            StandardCharsets.UTF_8);
            long eval = cacheManager.eval(limitLua, Arrays.asList("a"),
                    Lists.newArrayList(qpsTotal + "", ttl+""));
            if(eval / ttl >= qps){
                System.out.println("lua自增达到限流qps:eval="+eval +":" + ttl);
            }
            if((System.currentTimeMillis() - start) == 1000){
                System.out.println("qps："+ i);
                System.out.println("lua："+ eval);
            }
            if(eval == 0){
                System.out.println("lua自增达到限流耗时："+ (System.currentTimeMillis() - start));
                break;
            }
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));

    }

    @Test
    public void testZset(){
        cacheManager.zAdd("a",1,"yzh");
        cacheManager.zAdd("a",2,"yzh");
        cacheManager.zRem("a","yzh");
    }

    @Test
    public void testPickMatching(){
        cacheManager.redissonClient().getKeys()
                .getKeysByPattern(RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_QUEUE + "*")
                .forEach(key->{
                    System.out.println(key);
                });

        String key = RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_QUEUE + "1:14";
        System.out.println(key);
        System.out.println(key.split(":",6)[4]);
    }

    @Test
    public void addUser() {
        String pythonToken = "d83b79731f35fcaa73353390d38178aa8c0b3054";
        String al = "1169 01eb8f17 47398943 不敢用刀";
        String[] split = al.split(" ");
        Integer id = Integer.parseInt(split[0]);
        String uid = split[1];
        String chickenId = split[2];
        String username = split[3];
        UserSessionContext userSessionContext = new UserSessionContext();
        userSessionContext.setUsername(username);
        userSessionContext.setUid(uid);
        userSessionContext.setChickenId(chickenId);
        userSessionContext.setId(id);
        userSessionContext.setPythonToken(uid + "." + pythonToken);

        userSessionContext.setAvatar(
                "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTL6dYhxxccWiaq7qbUVTVibNJukLzCkROdazaRl1SeLGtToOlPMyicZvdQZupjVQJHAPWQeCY66JDPkw/132");
        userSessionContext.setSex(1);
        cacheManager.set("user:" + pythonToken, JSON.toJSONString(userSessionContext));
        cacheManager.set("access_to_refresh:" + pythonToken, UUID.randomUUID().toString());
    }
}
