package com.kaihei.esportingplus.payment.api.enums;

/**
 * 订单维度
 *
 * @author xiaolijun
 **/
public enum OrderDimensionalityEnum {

    /**
     * 游戏订单
     */
    GAME_ORDER("D001", "游戏订单"),
    /**
     * 暴鸡认证订单
     */
    CERTIFIED_ORDER("D002", "暴鸡认证订单"),
    /**
     * 充值订单
     */
    RECHARGE_ORDER("D003", "充值订单"),
    /***
     * 奖励订单
     */
    BONUS_ORDER("D004", "奖励订单"),
    /***
     * 违规扣款
     */
    ILLEGAL_CHARGE("D005", "违规扣款"),
    /***
     * 福利金
     */
    BALANCE_WELFARE_ORDER("D006", "福利金"),
    /***
     * 抽奖奖金
     */
    ACTIVITY_BOUNTY("D007", "抽奖奖金"),
    /**
     * 车队订单
     */
    FLEET("D008", "车队订单"),
    /***
     * 悬赏订单
     */
    OFFER_A_REWARD("D009", "悬赏订单"),
    /***
     * 系统充值订单
     */
    SYS_RECHARGE_ORDER("D010", "系统充值订单"),
    /**
     * 提现订单
     */
    WITHDRAW("D011", "提现订单"),
    /**
     * 扣款订单
     */
    SYS_DEDUCT_ORDER("D012", "扣款订单"),
    /**
     * 工作室提现订单
     */
    STUDIO_TRANSFER_ORDER("D013", "工作室提现订单"),
    /**
     * 每周好运礼包订单
     */
    WEEKLY_LUCKY_ACTIVITY_ORDER("D014", "每周好运礼包订单"),
    /**
     * 技能订单
     */
    SKILL("D015", "技能订单"),
    /**
     * DNF小程序订单
     */
    DNF_MINI_PROGRAM_ORDER("D016", "DNF小程序订单"),
    /**
     * RPG保证金认证订单
     */
    RPG_CERTIFIED_ORDER("D017", "RPG保证金认证订单"),
    /**
     * 暴鸡币充值订单
     */
    GCOIN_RECHARGE_ORDER("D018", "暴鸡币充值订单"),
    /**
     * 暴鸡值兑换订单类型
     */
    STARLIGHT_CONVERT_ORDER("D019", "暴鸡值兑换订单类型"),

    /***
     * 支付系统-暴鸡币充值
     */
    GCOIN_RECHARGE("D020", "暴鸡币充值"),

    /**
     * 支付系统-暴鸡币扣款
     */
    GCOIN_DEDUCT("D021", "暴鸡币扣款"),

    /***
     * 支付系统-暴击值扣款
     */
    STARLIGHT_DEDUCT("D022", "暴击值扣款"),

    /**
     * 支付系统-打赏订单
     */
    REWARD_ORDER("D023", "打赏订单"),

    /***
     * 支付系统-暴击值提现订单
     */
    WITHDRAW_ORDER("D024", "暴击值提现订单"),
    /***
     * 支付系统-积分兑换暴鸡值订单
     */
    SCORE_EXCHANGE_STARLIGHT("D025", "鸡分兑换暴击值"),
    /***
     * 支付系统-暴击值兑换暴鸡币订单
     */
    STARLIGHT_EXCHANGE_GCOIN("D026", "暴击值兑换暴鸡币");

    private String code;
    private String msg;

    OrderDimensionalityEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
