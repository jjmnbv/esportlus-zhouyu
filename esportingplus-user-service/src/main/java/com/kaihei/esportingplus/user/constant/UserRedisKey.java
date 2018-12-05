package com.kaihei.esportingplus.user.constant;

/**
 * @author xiekeqing
 * @Title: UserRedisKey
 * @Description: 用户服务redis key 常量
 * @date 2018/9/1120:11
 */
public class UserRedisKey {

    /**
     * 应用命名
     */
    public static final String KEY_APP = "user";

    /**
     * key 分隔符
     */
    public static final String KEY_SEPARATOR = ":";

    /**
     * 用户基础信息
     */
    public static final String USER_BASE_INFO = KEY_APP + KEY_SEPARATOR + "base";

    /**
     * 用户绑定信息
     */
    public static final String USER_BIND_MAPPING_USERID =
            KEY_APP + KEY_SEPARATOR + "bind" + KEY_SEPARATOR + "mapping";

    /**
     * 用户uid映射userId
     */
    public static final String USER_UID_MAPPING_USERID =
            KEY_APP + KEY_SEPARATOR + "uid" + KEY_SEPARATOR + "mapping" + KEY_SEPARATOR + "userid";

    /**
     * 用户uid映射token
     */
    public static final String USER_UID_MAPPING_TOKEN =
            KEY_APP + KEY_SEPARATOR + "uid" + KEY_SEPARATOR + "mapping" + KEY_SEPARATOR + "token";

    /**
     * 用户注册锁key
     */
    public static final String USER_REGIST_LOCK_KEY = "members.register3.union_id.%s";

    /**
     * 用户登录token key
     */
    public static final String PYTHON_LOGIN_TOKEN_KEY = "%s:%s";

    /**
     * 用户登录信息 key
     */
    public static final String USER_LOGIN_KEY = "user:%s";

    /**
     * 用户访问token key
     */
    public static final String ACCESS_TOKEN_KEY = "access:%s";

    /**
     * 刷新token key
     */
    public static final String ACCESS_TO_REFRESH_KEY = "access_to_refresh:%s";

    /**
     * 注册用户新增bank锁
     */
    public static final String USER_CREATE_BANK_KEY =
            KEY_APP + KEY_SEPARATOR + "bank" + KEY_SEPARATOR + "create" + KEY_SEPARATOR + "uid_%s";

    /**
     * 用户鸡牌号
     */
    public static final String USER_CHICKEN_AVAILABLE_KEY =
            KEY_APP + KEY_SEPARATOR + "chicken" + KEY_SEPARATOR + "available";

    /**
     * 用户id自增
     */
    public static final String USER_UID_INCR_KEY =
            KEY_APP + KEY_SEPARATOR + "uid" + KEY_SEPARATOR + "incr";

    /**
     * 用户id自增锁
     */
    public static final String USER_UID_INCR_LOCK_KEY =
            USER_UID_INCR_KEY + KEY_SEPARATOR + "lock";

    /**
     * 用户白名单
     */
    public static final String USER_WHITELIST =
            KEY_APP + KEY_SEPARATOR + "whitelist";

    /**
     * 用户白名单
     */
    public static final String PHONECODE = "phone_code%s";

    /**
     * 用户初始化邀请记录redis锁key
     */
    public static final String STATISTICS_INIT_REDIS_LOCK =
            KEY_APP + KEY_SEPARATOR + "statistics_init_lock_userid" + KEY_SEPARATOR + "%s";

    /**
     * 用户消费暴击币支付订单key
     */
    public static final String COIN_CONSUME_PAY_ORDER_NO =
            KEY_APP + KEY_SEPARATOR + "coin_consume_award_pay_order_no" + KEY_SEPARATOR + "%s";

    /**
     * python微信sessionKey缓存key
     */
    public static final String PYTHON_WX_SESSIONKEY_KEY = "mp_session_key:%s:%s";

    /**
     * python融云rtoken缓存key
     */
    public static final String PYTHON_RTOKEN_KEY = "rcloud:%s";

    /**
     * 用户兑换暴击值字典key(dictionary)
     */
    public static final String EXCHANGE_AUTHORITY_DICTIONARY = "exchange_authority_config";

    /**
     * 用户兑换暴击值字典key(dictionary_category)
     */
    public static final String EXCHANGE_AUTHORITY_DICTIONARY_CATEGORY = "exchange_authority";

    /**
     * 未读关注、粉丝、好友数
     */
    public static final String RELATION_UNREADNUM = KEY_APP + KEY_SEPARATOR + "relation";

    /**
     *
     */
    public static final String USER_CARD_INFO = KEY_APP + KEY_SEPARATOR + "info:%s";

}
