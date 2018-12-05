package com.kaihei.esportingplus.user.bulider;

/**
 * @author xiekeqing
 * @Title: HashRedisKeyBuilder
 * @Description: hash分片构造器
 * @date 2018/9/1214:15
 */
public class HashRedisKeyBuilder extends RedisKeySegmentBuilder {

    @Override
    public String bulidSegment(String segmentSeed) {
        return Integer.toString(hash(segmentSeed) & (HASH_SEGMENT_SIZE - 1));
    }

}
