package com.kaihei.esportingplus.customer.center.constant;

/**
 * customer-center 服务redis常量类
 *
 * @author yangshidong
 * @date 2018/12/4
 */
public class CustomerRedisKey {
    /**
     * 应用名
     * */

    /**
     * 应用命名
     */
    public static final String KEY_APP = "customer";

    /**
     * key 分隔符
     */
    public static final String KEY_SEPARATOR = ":";

    /**
     * 反馈提交记录自增id
     */
    public static final String FEEDBACK_LOG_ID = KEY_APP + KEY_SEPARATOR + "feedback" + KEY_SEPARATOR + "id";

    /**
     * 反馈提交记录每天的自增id
     */
    public static final String FEEDBACK_LOG_ID_DAY = FEEDBACK_LOG_ID + KEY_SEPARATOR + "%s";
}
