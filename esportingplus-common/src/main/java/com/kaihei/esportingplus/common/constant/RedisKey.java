package com.kaihei.esportingplus.common.constant;

/**
 * redis key 常量
 *
 * @author LiuQing.Qin
 */
public final class RedisKey {

    public static final String FIND_SUCCESS = "OK";
    public static final String SET_IF_NOT_EXIST = "NX";
    public static final String SET_WITH_EXPIRE_TIME = "PX";
    /**
     * key 分隔符
     */
    public static final String KEY_SEPARATOR = ":";

    /**
     * 缓存在 redis 中的车队房间号列表集合
     */
    public static final String TEAM_PREFIX = "team:%s";
    /**
     * 缓存在 redis 中的车队房间号列表集合
     */
    public static final String TEAM_ROOM_NUM_SET = "team:roomNum";
    /**
     * 缓存在 redis 中的车队实际位置数
     */
    public static final String TEAM_ACTUALLY_POSITION_PREFIX = "team:actuallyPosition:%s";
    /**
     * 缓存在车队 redis 中的游戏列表
     */
    public static final String TEAM_GAME_LIST_KEY = "team:game:list";
    /**
     * 缓存在车队 redis 中的游戏副本列表
     */
    public static final String TEAM_GAME_RAID_LIST_KEY = "team:game:%s:raid:list";
    /**
     * 缓存在车队redis中的游戏小区code对应大区和跨区关系列表
     **/
    public static final String TEAM_SMALL_ZONE_REF_ACROSS_ZONE_KEY = "team:game:%d:small:%d:big:across:zone";

    public static final String USER_GAME_ROLE_CERTS_KEY = "user:%s:game:%d";
    /**
     * 用户游戏分片 key 前缀(校验是否已经在车队中用)
     */
    public static final String TEAM_USER_GAME_PREFIX = "team:user:game";
    /**
     * 车队老板定时任务分片 key
     */
    public static final String TEAM_BOSS_JOB_PREFIX = "team:boss:job";
    /**
     * 车队游戏结果 key, 默认过期一天
     */
    public static final String TEAM_GAME_RESULT_PREFIX = "team:gameResult:%s";
    /**
     * 缓存在 redis 中的车队本地事务幂等标识 key
     */
    public static final String TEAM_LOCAL_TRANSACTION_IDEMPOTENCE = "team:%s:%s";
    /**
     * 车队发起的退款 MQ标识
     */
    public static final String TEAM_REFUND_MQ = "team:%s:%s";

    /**
     * 缓存在用户 redis 中的游戏副本列表
     */
    public static final String USER_GAME_RAID_LIST_KEY = "user:game:%d:raid:list";

    /**
     * 缓存在用户 redis 中的 游戏小区code对应大区和跨区
     **/
    public static final String USER_SMALL_ZONE_REF_ACROSS_ZONE_KEY = "user:game:%d:small:%d:big:across:zone";

    /**
     * 缓存在用户 redis 中的 游戏认证副本对应的其所属副本key
     **/
    public static final String USER_CERT_RAID_REF_TYPE_RAID_KEY = "user:game:%d:cert_raid:%d:raid";

    public static final String USER_CREATE_ROLE_THRESHOLD_KEY = "u:%s:g:c:r:threshold";
    /**
     * 缓存在用户 redis 中的 小区名称key
     */
    public static final String USER_SMALL_ZONE_INFO_KEY = "rpg:game:%d:small:%d:info";
    /**
     * 车队队员信息 key 前缀
     */
    public static final String TEAM_MEMBER_PREFIX = "team:member:%s";
    /**
     * 游戏小区code对应大区和跨区
     **/
    public static final String SMALL_ZONE_REF_ACROSS_ZONE_KEY = "rpg:game:%d:small:%d:big:across:zone";
    /**
     * 游戏跨区code对应大区和小区
     **/
    public static final String ACROSS_ZONE_REF_BIG_SMALL_ZONE_KEY = "rpg:game:%d:across:%d:big:small:zone";
    /**
     * 游戏跨区code对应小区
     **/
    public static final String ACROSS_ZONE_REF_SMALL_ZONE_KEY = "rpg:game:%d:across:%d:small:zone";
    /**
     * 游戏大小区关联key
     **/
    public static final String BIG_ZONE_AND_SMALL_ZONE_KEY = "rpg:game:%d:big:small:zone";
    /**
     * 游戏职业列表key
     **/
    public static final String GAME_CAREER_LIST_KEY = "rpg:game:%d:awakening:career";
    /**
     * 游戏列表key
     **/
    public static final String GAME_LIST_KEY = "rpg:game:list";
    /**
     * 游戏副本key
     **/
    public static final String GAME_RAID_KEY = "rpg:game:%d:raid";
    /**
     * 游戏认证副本key
     **/
    public static final String GAME_CERT_RAID_KEY = "rpg:game:%d:cert_raid";
    /**
     * 游戏认证副本对应的其所属副本key
     **/
    public static final String CERT_RAID_REF_TYPE_RAID_KEY = "rpg:game:%d:cert_raid:%d:raid";
    public static final String GAME_ACROSS_ZONE_KEY = "rpg:game:%d:across:zone";
    /**
     * 跨区key
     */
    public static final String ACROSS_ZONE_INFO_KEY = "rpg:game:%d:across:%d:info";
    /**
     * 大区key
     */
    public static final String BIG_ZONE_INFO_KEY = "rpg:game:%d:big:%d:info";
    /**
     * 小区key
     */
    public static final String SMALL_ZONE_INFO_KEY = "rpg:game:%d:small:%d:info";

    /**
     * 小程序需要认证的接口路径
     */
    public static final String NEED_AUTH_URLS = "esports:auth:urls";
    /**
     * 客户端信息
     */
    public static final String CLIENT_DETAIL = "esports:auth:client_detail";

    /**
     * 限流计数器
     */
    public static final String RATELIMITE = "esports:ratelimite:";

    /**
     * 限流黑名单IP
     */
    public static final String RATELIMITE_BLACKLIST = RATELIMITE + "blacklist";

    /**
     * uid与access token的关联
     */
    public static final String UID_ACCESS_TOKEN = "user:";
    public static final String TOKEN_REFRESH_EXPIRE = "token:expire:refresh";

    public static final String ACCESS = "access:";
    public static final String AUTH_TO_ACCESS = "auth_to_access:";
    public static final String AUTH = "auth:";
    public static final String REFRESH_AUTH = "refresh_auth:";
    /**
     * access token 和 refresh token 的关联
     */
    public static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    public static final String REFRESH = "refresh:";
    public static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    public static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    public static final String UNAME_TO_ACCESS = "uname_to_access:";

    public static final String BAOJI_LEVEL_RATE_KEY = "config:baoji:level:rate";

    public static final String NOTIFY_GAMETEAM_RETRY = "retry:order:notify2game:";
    public static final String PAY4NOTIFY_RETRY = "retry:order:pay4notify:";
    public static final String REFUND4NOTIFY_RETRY = "retry:order:refund4notify:";
    public static final String GET_UID_RETRY = "retry:auth:uid:";
    public static final String GET_UERINFO_RETRY = "retry:auth:userInfo:";

    /**
     * 退款记录 幂等
     */
    public static final String REFUND_HISTORY = "history:order:refund:";
    /**
     * 车队服务发起退款记录 幂等
     */
    public static final String REFUND_HISTORY_FROM_TEAM = "history:order:refund_from_team:";

    /**
     * PVP免费车队结束消息 幂等
     */
    public static final String UPDATE_ORDER_HISTORY_FROM_TEAM_END = "history:order:update_from_team_end:";

    /**
     * 好友完成免费车队订单消息 幂等
     */
    public static final String FRIEND_FINISH_GAMETEAM_ORDER = "history:order:friend_finish_gameteam_order:";

    /**
     * 更新订单记录 幂等
     */
    public static final String UPDATE_ORDER_HISTORY = "history:order:update:";

    /**
     * 更新订单记录完成标识
     */
    public static final String UPDATED_ORDER = "order:updated:";

    /**
     * 分布式锁 key
     */
    public static final String REDIS_DISTLOCK_PREFIX = "distlock:%s";
    /**
     * 创建暴鸡订单放重复幂等可以
     **/
    public static final String CREATE_BAOJI_ORDER_LOCK_KEY = "order:baoji:create:game:%d:teamSequence:%s";

    /***
     * 创建暴鸡币支付订单加锁
     * eg:create:gcoin:payment:orderType:outTradeNo
     */
    public static final String CREATE_GCOIN_PAYMENT_LOCK_KEY = "create:gcoin:payment:%s:%s";

    /***
     * 创建暴鸡币退款订单加锁
     * eg:create:gcoin:refund:orderType:outRefundNo
     */
    public static final String CREATE_GCOIN_REFUND_LOCK_KEY = "create:gcoin:refund:%s:%s";

    /**
     * 暴鸡币充值订单重复幂等
     **/
    public static final String GCOIN_RECHARGE_LOCK_KEY = "gcoin:recharge:lock:%s";

    /**
     * 暴鸡币支付订单重复幂等
     **/
    public static final String GCOIN_PAYMENT_LOCK_KEY = "gcoin:payment:lock:%s";
    /**
     * 暴鸡币退款订单重复幂等
     **/
    public static final String GCOIN_REFUND_LOCK_KEY = "gcoin:refund:lock:%s";

    /**
     * 暴鸡币充值订单
     **/
    public static final String GCOIN_RECHARGE_KEY = "gcoin:recharge:%s:%s";

    /**
     * 暴鸡币支付订单
     **/
    public static final String GCOIN_PAYMENT_KEY = "gcoin:payment:%s:%s";

    /**
     * 暴鸡币退款订单
     **/
    public static final String GCOIN_REFUND_KEY = "gcoin:refund:%s:%s";

    /**
     * 第三方支付-支付宝-支付订单
     * eg:trade:alipay:pay:orderType:outTradeNo
     **/
    public static final String EXTERNAL_ALIPAY_PAY_KEY = "trade:alipay:pay:%s:%s";

    /**
     * 第三方支付-支付宝-退款订单
     * eg:trade:alipay:refund:outRefundNo
     */
    public static final String EXTERNAL_ALIPAY_REFUND_KEY = "trade:alipay:refund:%s";

    /**
     * 第三方支付-微信/QQ-支付订单
     * eg:trade:tenpay:pay:orderType:outTradeNo
     */
    public static final String EXTERNAL_TENPAY_PAY_KEY = "trade:tenpay:pay:%s:%s";

    /**
     * 第三方支付--微信/QQ-支付消费标记
     * eg:trade:tenpay:consumer:orderType:outTradeNo
     */
    public static final String EXTERNAL_TENPAY_NOTIFY_CONSUMER_KEY = "trade:tenpay:consumer:%s:%s";

    /**
     * 第三方支付-微信/QQ-退款订单
     * eg:trade:tenpay:refund:outRefundNo
     */
    public static final String EXTERNAL_TENPAY_REFUND_KEY = "trade:tenpay:refund:%s";

    /**
     * 第三方支付-微信/QQ-退款回调标记
     * eg:trade:tenpay:refund:notify:%s
     */
    public static final String EXTERNAL_TENPAY_REFUND_NOTIFY_KEY = "trade:tenpay:refund:notify:%s";

    /**
     * 内部支付-暴鸡币支付
     **/
    public static final String EXTERNAL_GCOIN_PAY_KEY = "trade:gcoin:pay:%s";

    /**
     * 内部支付-暴鸡币支付回调
     **/
    public static final String EXTERNAL_GCOIN_NOTIFY_KEY = "trade:gcoin:notify:%s";

    ///**
    // * 第三方支付-微信/QQ-支付回调标记
    // * trade:tenpay:notify:pay:orderId
    // */
    //public static final String EXTERNAL_TENPAY_PAY_NOTIFY_KEY = "trade:mq::lock:tenpay:notify:pay:%s:%s";

    /***
     * 第三方支付-微信/QQ回调消息消费锁
     * trade:mq:lock:tenpay:pay:notify:orderId
     */
    public static final String EXTERNAL_MQ_LOCK_TENPAY_PAY_NOTIFY = "trade:mq:lock:tenpay:pay:notify:%s";

    /***
     * 第三方支付-微信/QQ回调消息消费锁
     * trade:mq:lock:tenpay:refund:notify:orderId
     */
    public static final String EXTERNAL_MQ_LOCK_TENPAY_REFUND_NOTIFY = "trade:mq:lock:tenpay:refund:notify:%s";

    /***
     * 第三方支付-支付宝回调消息消费锁
     */
    public static final String EXTERNAL_MQ_LOCK_ALIPAY_NOTIFY = "trade:mq:lock:alipay:notify:%s";

    /***
     * 第三方支付-支付宝退款消费锁
     */
    public static final String EXTERNAL_MQ_LOCK_ALIPAY_REFUND = "trade:mq:lock:alipay:refund:%s";

    /***
     * 第三方支付-微信、QQ支付 退款消费锁
     */
    public static final String EXTERNAL_MQ_LOCK_TENPAY_REFUND = "trade:mq:lock:tenpay:refund:%s";

    /***
     * 第三方支付配置信息
     * eg:payment:pay_setting:%s
     */
    public static final String PAY_SETTING_KEY_PREFIX = "payment:pay_setting:%s";
    /**
     * 支付服务扣减比率配置前缀
     */
    public static final String PAYMENT_DEDUCT_RATIO_PREFIX = "payment:deduct_ratio:%s";

    /**
     * 设备每小时充值次数
     **/
    public static final String DEVICE_HOUR_RECHARGE_COUNT = "risk:device:%s:hour:%s:recharge:count";
    /**
     * 设备每天充值次数
     **/
    public static final String DEVICE_DAY_RECHARGE_COUNT = "risk:device:%s:day:%s:recharge:count";
    /**
     * 用户每小时充值次数
     **/
    public static final String USER_HOUR_RECHARGE_COUNT = "risk:uid:%s:hour:%s:recharge:count";
    /**
     * 用户每天充值次数
     **/
    public static final String USER_DAY_RECHARGE_COUNT = "risk:uid:%s:day:%s:recharge:count";

    /**
     * 免费车队类型
     **/
    public static final String FREE_TEAM_TYPE_ID = "freeteamtype:id:%s";

    /**
     * 免费车队类型段位
     **/
    public static final String FREE_TEAM_TYPE_DAN = "freeteamtype:id:%s:dan:%s";

    /**
     * pvp 免费车队 老板匹配队列
     */
    public static final String PVP_FREE_TEAM_MATCHING_BOSS_QUEUE = "pvp:freeteam:matching:boss:";

    /**
     * pvp 免费车队 老板点击匹配时的记录
     */
    public static final String PVP_FREE_TEAM_MATCHING_BOSS_HISTORY = "pvp:freeteam:matching:history:boss:";

    /**
     * pvp 免费车队 车队匹配队列
     */
    public static final String PVP_FREE_TEAM_MATCHING_TEAM_QUEUE = "pvp:freeteam:matching:team:";

    /**
     * pvp 免费车队取消匹配队列
     */
    public static final String PVP_FREE_TEAM_MATCHING_CANCEL_QUEUE = PVP_FREE_TEAM_MATCHING_BOSS_QUEUE
            + "cancel";

    /**
     * 暴鸡接单范围
     */
    public static final String BAOJI_DAN_RANGE_GAME = "baojidanrange:game:%s";

    /**
     * 暴鸡接单范围
     */
    public static final String BAOJI_DAN_RANGE_BAOJILEVEL = "baojidanrange:game:%s:levelcode:%s";

    /**
     * 云账户订单-创建订单key
     */
    public static final String EXTERNAL_CLOUD_PAY_KEY = "thirdtrade:cloud:pay:%s";

    //重构常量

    /**
     * 保存数据到Redis的时间
     */
    public static final int SAVE_DATA_TIME = 30 * 60;

    /***
     * 第三方支付订单信息
     * eg:external:payment:order:key:orderType:outTradeNo
     */
    public static final String EXTERNAL_PAYMENT_ORDER_KEY = "external:payment:order:key:%s:%s";

    /***
     * 第三方退款订单信息
     * eg:external:refund:order:key:outRefundNo
     */
    public static final String EXTERNAL_REFUND_ORDER_KEY = "external:refund:order:key:%s";

    /***
     * 第三方退款订单消费锁
     * eg:external:refund:consumer:lock:orderId
     */
    public static final String EXTERNAL_REFUND_CONSUMER_LOCK = "external:refund:consumer:lock:%s";

    /**
     * 提现配置
     */
    public static final String PAYMENT_WITHDRAW_CONFIG = "payment:withdraw_config";


    /**
     * 用户免费车队次数
     */
    public static final String FREE_TEAM_CHANCES = "freeteamchances:uid:%s";

    /**
     * 防止用户重复提交
     */
    public static final String PREVENT_REPEAT_OPERATION = "prevent_repeat_operation:%s:%s";
}
