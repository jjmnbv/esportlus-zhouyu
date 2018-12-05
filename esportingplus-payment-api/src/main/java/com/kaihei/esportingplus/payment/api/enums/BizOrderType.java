package com.kaihei.esportingplus.payment.api.enums;
/**
 * 订单收益枚举类型
 *
 * @author haycco
 **/
public enum BizOrderType {

    /**
     * 1	游戏订单类型
     * 2	暴鸡认证订单类型
     * 3	充值订单类型
     * 4    奖励订单类型
     * 5	违规扣款类型
     * 6    福利金类型
     * 7    抽奖奖金
     * 8	车队订单
     * 9	悬赏订单
     * 10   SYS_RECHARGE_ORDER
     * 11	提现订单类型
     * 12   扣款订单类型
     * 13   工作室提现订单类型
     * 14   每周好运礼包订单类型
     * 16	技能订单类型
     * 17	DNF小程序订单
     * 18	RPG保证金认证订单
     * 19   暴鸡币充值订单
     * 20   暴鸡值兑换订单类型
     */

    GAME_ORDER(1,"游戏订单"),
    CERTIFIED_ORDER(2,"暴鸡认证订单"),
    RECHARGE_ORDER(3,"充值订单"),
    BONUS_ORDER(4,"奖励订单"),
    ILLEGAL_CHARGE(5,"违规扣款"),
    BALANCE_WELFARE_ORDER(6,"福利金"),
    ACTIVITY_BOUNTY(7,"抽奖奖金"),
    FLEET(8,"车队订单"),
    OFFER_A_REWARD(9,"悬赏订单"),
    SYS_RECHARGE_ORDER(10,"系统充值订单"),
    WITHDRAW(11,"提现订单"),
    SYS_DEDUCT_ORDER(12,"扣款订单"),
    STUDIO_TRANSFER_ORDER(13,"工作室提现订单"),
    WEEKLY_LUCKY_ACTIVITY_ORDER(14,"每周好运礼包订单"),
    SKILL(16,"技能订单"),
    DNF_MINI_PROGRAM_ORDER(17,"DNF小程序订单"),
    RPG_CERTIFIED_ORDER(18,"RPG保证金认证订单"),
    GCOIN_RECHARGE_ORDER(19,"暴鸡币充值订单"),
    STARLIGHT_CONVERT_ORDER(20,"暴鸡值兑换订单类型");

    private int code;

    private String msg;

    BizOrderType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static BizOrderType valueOf(int code){
        BizOrderType result = null;
        for (BizOrderType item : BizOrderType.values()) {
            if (item.getCode() == code) {
                result = item;
                break;
            }
        }
        if(result == null) {
            throw new IllegalArgumentException("No enum constant code 【" + code + "】");
        }
        return result;
    }
}
