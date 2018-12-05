/**
 * @Title: RedisUtils.java
 * @Package com.kaihei.interact.infrastructure.util
 * @Description: redis工具类
 * @author xiekeqing
 * @date 2017年12月18日 上午11:33:38
 * @version V1.0
 */
package com.kaihei.esportingplus.common.tools;


import com.kaihei.esportingplus.common.constant.RedisKey;

/**
 * 生成HASH分片key
 *
 */
public class RedisUtils {

    /** HASH分片大小, 必须为2的幂次方 */
    private static int HASH_SEGMENT_SIZE = 2 << 12;


    /**
     * 生成HASH分片key
     *
     * @param segmentSeed 分片计算种子
     * @param keyPrefixs key的前缀字符串序列，按先手顺序组装
     * @return 分片缓存key
     */
    public static String buildHashSegmentKey(String segmentSeed, String... keyPrefixs) {
        StringBuilder key = new StringBuilder();
        for (String keyPrefix : keyPrefixs) {
            key.append(keyPrefix);
            key.append(RedisKey.KEY_SEPARATOR);
        }

        key.append(Integer.toString(hash(segmentSeed) & (HASH_SEGMENT_SIZE - 1)));
        return key.toString();
    }

    private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
            String s = buildHashSegmentKey(Integer.toString(i), "USER:GAMETEAM", "1000");
            System.out.println(s);
        }
    }
}
