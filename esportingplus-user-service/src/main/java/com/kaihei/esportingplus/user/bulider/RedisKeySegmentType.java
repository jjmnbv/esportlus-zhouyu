package com.kaihei.esportingplus.user.bulider;

/**
 * TODO 功能描述
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/15 16:57
 */
public enum RedisKeySegmentType {

    /**
     * hash值分片
     */
    HASH(new HashRedisKeyBuilder()),
    /**
     * 整数前缀，例如：
     * <p>124-->0:124</p>
     * <p>2124-->2:124</p>
     * <p>23124-->23:124</p>
     */
    INT_PREFIX(new IntPrefixRedisKeyBuilder());

    RedisKeySegmentType(RedisKeySegmentBuilder builder) {
        this.builder = builder;
    }

    private RedisKeySegmentBuilder builder;

    public RedisKeySegmentBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(RedisKeySegmentBuilder builder) {
        this.builder = builder;
    }
}
