package com.kaihei.esportingplus.user.bulider;

/**
 * @author xiekeqing
 * @Title: IntPrefixRedisKeyBuilder
 * @Description: 整数前缀构造器
 * @date 2018/9/1214:11
 */
public class IntPrefixRedisKeyBuilder extends RedisKeySegmentBuilder {

    @Override
    public String bulidSegment(String segmentSeed) {
        return segmentSeed.length()<=3?"0":segmentSeed.substring(0,segmentSeed.length()-3);
    }
}
