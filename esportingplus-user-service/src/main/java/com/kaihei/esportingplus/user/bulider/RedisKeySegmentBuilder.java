package com.kaihei.esportingplus.user.bulider;

import com.kaihei.esportingplus.common.constant.RedisKey;

/**
 * @author xiekeqing
 * @Title: RedisKeySegmentBuilder
 * @Description: redis分片key构造器
 * @date 2018/9/1212:17
 */
public abstract class RedisKeySegmentBuilder {

    /**
     * HASH分片大小,为尽量保证每个HASH的数量小于1000，以1000w用户计算设置HASH总分片数量为16383
     */
    protected static int HASH_SEGMENT_SIZE = 2 << 13;

    public static String bulid(RedisKeySegmentType segmentType, String segmentSeed,
            String... keyPrefixs) {
        StringBuilder key = new StringBuilder();
        for (String keyPrefix : keyPrefixs) {
            key.append(keyPrefix);
            key.append(RedisKey.KEY_SEPARATOR);
        }
        key.append(segmentType.getBuilder().bulidSegment(segmentSeed));
        return key.toString();
    }

    public int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public abstract String bulidSegment(String segmentSeed);

}
