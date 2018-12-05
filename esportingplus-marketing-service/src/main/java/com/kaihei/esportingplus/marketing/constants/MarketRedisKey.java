package com.kaihei.esportingplus.marketing.constants;

/**
 * @author xiekeqing
 * @Title: UserRedisKey
 * @Description: 用户服务redis key 常量
 * @date 2018/9/1120:11
 */
public class MarketRedisKey {

    /**
     * 应用命名
     */
    public static final String KEY_APP = "market";

    /**
     * key 分隔符
     */
    public static final String KEY_SEPARATOR = ":";

    /**
     * 用户初始化邀请记录redis锁key
     */
    public static final String STATISTICS_INIT_REDIS_LOCK =
            KEY_APP + KEY_SEPARATOR + "statistics_init_lock_userid" + KEY_SEPARATOR + "%s";

}
