package com.kaihei.esportingplus.common.constant;

/**
 * rocketmq的一些常量
 *
 * @author liangyi
 */
public class RocketMQConstant {

    /**
     * topic
     */
    public static final String TOPIC_RPG = "esportingplus_prg";

    public static final String TOPIC_PVP = "esportingplus_pvp";

    public static final String TOPIC_PVP_FREE = "esportingplus_pvp_free";

    public static final String TOPIC_SHARE_INVIT = "esportingplus_share_invit";

    /**
     * 分享邀请注册 tag
     */
    public static final String INVIT_REGIST_TAG = "invit_regist_tag";

    /**
     * 创建暴鸡订单(立即开车) tag
     */
    public static final String CREATE_BAOJI_ORDER_TAG = "create_baoji_order";

    /**
     * 修改订单状态(退出、踢出车队、解散、结束车队) tag
     */
    public static final String UPDATE_ORDER_STATUS_TAG = "update_order_status";

    /**
     * 车队的分布式事务 id
     */
    public static final String GAMINGTEAM_TRANSACTION_ID = "gamingteam_%s_%s";

    /**
     * 退款分布式事务 id
     */
    public static final String REFUND_TRANSACTION_ID = "refund_%s_%s";

    /**
     * MQ TAG：开车后离开车队
     */
    public static final String QUIT_TEAM_AFTER_RUNNING = "quit_team_after_running";

    /**
     * 退款 tags
     */
    public static final String REFUND_ORDER_TAGS = "refund_order";

    /**
     * 车队发起退款 tags
     */
    public static final String REFUND_ORDER_TAGS_FROM_TEAM = "refund_order_from_team";

    /**
     * 创建暴鸡订单生产组
     */
    public static final String CREATE_BAOJI_ORDER_PRODUCER_GROUP = "create_baoji_order_producer_group";

    /**
     * 创建暴鸡订单消费组
     */
    public static final String CREATE_BAOJI_ORDER_CONSUMER_GROUP = "create_baoji_order_consumer_group";
    public static final String PVP_FREE_ORDER_CONSUMER_GROUP = "pvp_free_order_consumer_group";
    /**
     * 修改订单状态生产组
     */
    public static final String UPDATE_ORDER_STATUS_PRODUCER_GROUP = "update_order_status_producer_group";

    /**
     * 修改PVP订单状态消费组
     */
    public static final String UPDATE_ORDER_STATUS_CONSUMER_PVP_GROUP = "update_order_status_consumer_pvp_group";
    /**
     * 车队结束修改PVP_FREE订单状态消费组
     */
    public static final String UPDATE_ORDER_CONSUMER_PVP_FREE_END_TEAM_GROUP = "update_order_consumer_pvp_free_end_team_group";
    /**
     * 已开车主动退出修改PVP_FREE订单状态消费组
     */
    public static final String UPDATE_ORDER_CONSUMER_PVP_FREE_LEAVE_TEAM_GROUP = "UPDATE_ORDER_CONSUMER_PVP_FREE_LEAVE_TEAM_GROUP";
    /**
     * 好友完成免费车队消费组
     */
    public static final String FRIEND_FINISH_ORDER_CONSUMER_PVP_FREE_GROUP = "friend_finish_order_consumer_pvp_free_group";
    /**
     * 好友完成免费车队消费组
     */
    public static final String INVIT_SHARE_CONSUMER_GROUP = "invit_share_consumer_group";
    /**
     * 修改RPG订单状态消费组
     */
    public static final String UPDATE_ORDER_STATUS_CONSUMER_RPG_GROUP = "update_order_status_consumer_rpg_group";
    /**
     * RPG退款生产组
     */
    public static final String REFUND_ORDER_PRODUCER_RPG_GROUP = "refund_order_producer_rpg_group";
    /**
     * PVP退款生产组
     */
    public static final String REFUND_ORDER_PRODUCER_PVP_GROUP = "refund_order_producer_pvp_group";
    /**
     * RPG退款消费组
     */
    public static final String REFUND_ORDER_CONSUMER_RPG_GROUP = "refund_order_consumer_rpg_group";
    /**
     * PVP退款消费组
     */
    public static final String REFUND_ORDER_CONSUMER_PVP_GROUP = "refund_order_consumer_pvp_group";

    /**
     * 车队发起的退款消费组
     */
    public static final String REFUND_ORDER_CONSUMER_GROUP_FROM_TEAM = "refund_order_consumer_group_fromt_team";

    /***
     * 支付模块group
     */

    public static final String PAYMENT_TOPIC = "payment_group";

    /**
     * 支付模块group的分布式事务 id
     */
    public static final String PAYMENT_TRANSACTION_ID = "payment_%s_%s";

    /**
     * 暴鸡币支付 tag
     */
    public static final String GCOIN_PAYMENT_TAG = "gcoin_payment";

    /**
     * 暴鸡币退款 tag
     */
    public static final String GCOIN_REFUND_TAG = "gcoin_refund";

    /**
     * 暴鸡币充值 tag
     */
    public static final String GCOIN_RECHARGE_TAG = "gcoin_recharge";

    /**
     * 暴击值提现 tag
     */
    public static final String STARLIGHT_WITHDRAW_TAG = "starlight_withdraw";

    /**
     * 暴击值兑换 tag
     */
    public static final String STARLIGHT_EXCHANGE_TAG = "starlight_exchange";

    /***
     * 支付宝回调tag
     */
    public static final String EXTERNAL_ALIPAY_NOTIFY_TAG = "external_alipay_notify";

    /***
     * 支付宝退款tag
     */
    public static final String EXTERNAL_ALIPAY_REFUND_TAG = "external_alipay_refund";

    /**
     * 腾讯支付-退款tag
     */
    public static final String EXTERNAL_TENPAY_REFUND_TAG = "external_tenpay_refund";


    /**
     * 腾讯支付-支付回调tag
     */
    public static final String EXTERNAL_TENPAY_PAY_NOTIFY_TAG = "external_tenpay_pay_notify";

    /**
     * 腾讯支付-退款回调tag
     */
    public static final String EXTERNAL_TENPAY_REFUND_NOTIFY_TAG = "external_tenpay_refund_notify";

    /***
     * 内部支付-暴鸡币支付
     */
    public static final String EXTERNAL_GCOIN_PAY_TAG = "external_gcoin_pay";

    /***
     * 内部支付-暴鸡币支付回调
     */
    public static final String EXTERNAL_GCOIN_NOTIFY_TAG = "external_gcoin_notify";

    /***
     * 暴鸡币充值生产组
     */
    public static final String GCOIN_RECHARGE_PRODUCER_GROUP = "gcoin_recharge_producer_group";

    /***
     * 暴鸡币充值消费组
     */
    public static final String GCOIN_RECHARGE_CONSUMER_GROUP = "gcoin_recharge_consumer_group";

    /***
     * 暴鸡币支付生产组
     */
    public static final String GCOIN_PAYMENT_PRODUCER_GROUP = "gcoin_payment_producer_group";

    /**
     * 暴鸡币支付消费组
     */
    public static final String GCOIN_PAYMENT_CONSUMER_GROUP = "gcoin_payment_consumer_group";

    /**
     * 暴鸡币退款生产组
     */
    public static final String GCOIN_REFUND_PRODUCER_GROUP = "gcoin_refund_producer_group";

    /**
     * 暴鸡币退款消费组
     */
    public static final String GCOIN_REFUND_CONSUMER_GROUP = "gcoin_refund_consumer_group";

    /**
     * 暴击值提现生产组
     */
    public static final String STARLIGHT_WITHDRAW_PRODUCER_GROUP = "starlight_withdraw_producer_group";

    /**
     * 暴击值兑换生产组
     */
    public static final String STARLIGHT_EXCHANGE_PRODUCER_GROUP = "starlight_exchange_producer_group";

    /**
     * 暴击值提现消费组
     */
    public static final String STARLIGHT_WITHDRAW_CONSUMER_GROUP = "starlight_withdraw_consumer_group";

    /**
     * 暴击值兑换消费组
     */
    public static final String STARLIGHT_EXCHANGE_CONSUMER_GROUP = "starlight_exchange_consumer_group";



    /**
     * 腾讯支付-退款生产组
     */
    public static final String EXTERNAL_TENPAY_REFUND_PRODUCER_GROUP = "external_tenpay_refund_producer_group";

    /**
     * 腾讯支付-退款消费组
     */
    public static final String EXTERNAL_TENPAY_REFUND_CONSUMER_GROUP = "external_tenpay_refund_consumer_group";

    /**
     * 腾讯支付-支付回调生产组
     */
    public static final String EXTERNAL_TENPAY_PAY_NOTIFY_PRODUCER_GROUP = "external_tenpay_pay_notify_producer_group";

    /**
     * 腾讯支付-支付回调消费组
     */
    public static final String EXTERNAL_TENPAY_PAY_NOTIFY_CONSUMER_GROUP = "external_tenpay_pay_notify_consumer_group";

    /**
     * 腾讯支付-退款回调生产组
     */
    public static final String EXTERNAL_TENPAY_REFUND_NOTIFY_PRODUCER_GROUP = "external_tenpay_refund_notify_producer_group";

    /**
     * 腾讯支付-退款回调消费组
     */
    public static final String EXTERNAL_TENPAY_REFUND_NOTIFY_CONSUMER_GROUP = "external_tenpay_refund_notify_consumer_group";


    /**
     * 云账户提现-下单tag
     */
    public static final String EXTERNEL_CLOUD_CREATEORDER_TAG = "cloud_create_order";
    /**
     * 云账户提现-支付回调tag
     */
    public static final String EXTERNEL_CLOUD_NOTIFYORDER_TAG = "cloud_notify_order";
    /**
     * 云账户提现-下单生产组
     */
    public static final String EXTERNAL_CLOUD_CREATE_PRODUCER_GROUP = "external_cloud_create_producer_group";
    /**
     * 云账户提现-下单消费组
     */
    public static final String EXTERNAL_CLOUD_CREATE_CONSUMER_GROUP = "external_cloud_create_consumer_group";
    /**
     * 云账户提现-回调生产组
     */
    public static final String EXTERNAL_CLOUD_NOTIFY_PRODUCER_GROUP = "external_cloud_notify_producer_group";
    /**
     * 云账户提现-回调消费组
     */
    public static final String EXTERNAL_CLOUD_NOTIFY_CONSUMER_GROUP = "external_cloud_notify_consumer_group";
    /**
     * 支付宝支付-回调生产组
     */
    public static final String EXTERNAL_ALIPAY_NOTIFY_PRODUCER_GROUP = "external_alipay_notify_producer_group";

    /**
     * 支付宝支付-回调消费组
     */
    public static final String EXTERNAL_ALIPAY_NOTIFY_CONSUMER_GROUP = "external_alipay_notify_consumer_group";

    /**
     * 支付宝支付-用户发起退款生产组
     */
    public static final String EXTERNAL_ALIPAY_REFUND_PRODUCER_GROUP = "external_alipay_refund_producer_group";

    /**
     * 支付宝支付-用户发起退款消费组
     */
    public static final String EXTERNAL_ALIPAY_REFUND_CONSUMER_GROUP = "external_alipay_refund_consumer_group";

    /**
     * 支付宝支付-关闭订单回调生产组
     */
    public static final String EXTERNAL_ALIPAY_CLOSE_PRODUCER_GROUP = "external_alipay_close_producer_group";

    /**
     * 支付宝支付-关闭订单回调消费组
     */
    public static final String EXTERNAL_ALIPAY_CLOSE_CONSUMER_GROUP = "external_alipay_close_consumer_group";

    /**
     * 支付宝支付-撤销订单回调生产组
     */
    public static final String EXTERNAL_ALIPAY_CANCEL_PRODUCER_GROUP = "external_alipay_cancel_producer_group";

    /**
     * 支付宝支付-撤销订单回调消费组
     */
    public static final String EXTERNAL_ALIPAY_CANCEL_CONSUMER_GROUP = "external_alipay_cancel_consumer_group";

    /**
     * 内部支付-暴鸡币支付调用生产组
     */
    public static final String EXTERNAL_GCOIN_PAY_PRODUCER_GROUP = "external_gcoin_pay_producer_group";

    /**
     * 内部支付-暴鸡币支付调用消费组
     */
    public static final String EXTERNAL_GCOIN_PAY_CONSUMER_GROUP = "external_gcoin_pay_consumer_group";

    /**
     * 内部支付-暴鸡币支付回调生产组
     */
    public static final String EXTERNAL_GCOIN_NOTIFY_PRODUCER_GROUP = "external_gcoin_notify_producer_group";

    /**
     * 内部支付-暴鸡币支付回调消费组
     */
    public static final String EXTERNAL_GCOIN_NOTIFY_CONSUMER_GROUP = "external_gcoin_notify_consumer_group";

}
