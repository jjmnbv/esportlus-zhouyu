package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:订单状态
 * @date: 2018/10/29 15:18
 */
public enum GameOrderStatusType {
    STATUS_PRE_ORDER(1,"待接单(未付款)"),
    STATUS_PAYED(2,""),
    STATUS_MATCH(3,""),
    STATUS_START_GAME(4,""),
    STATUS_PLAYER_CANCEL(5,""),
    STATUS_BAOJI_PRE_CANCEL_AND_REFUND(6,""),
    STATUS_BAOJI_CANCEL_AND_REFUND(7,""),
    STATUS_PLAYER_CANCEL_AND_REFUND(8,""),
    STATUS_BAOJI_FINISH(9,""),
    STATUS_PLAYER_FINISH(10,""),
    STATUS_TIME_OUT(11,""),
    STATUS_PLAYER_APPLY_REFUND(12,""),
    STATUS_ADMIN_REFUNDED(13,""),
    STATUS_ADMIN_REFUSE_REFUND(14,""),
    STATUS_PLAYER_AUTO_FINISH(15,""),
    STATUS_BAOJI_ACCEPT_REFUND(16,""),
    STATUS_PLAYER_READY(17,""),
    STATUS_WAITTING(18,"等待暴鸡接单"),
    STATUS_PREPARING(19,"待玩家准备"),
    STATUS_AWAIT_START(20,"等待暴鸡开始"),
    STATUS_STARTING(21,"游戏进行中"),
    STATUS_PLAYRE_CANCEL_BEFORE(22,"比赛前取消"),
    STATUS_AWAIT_RESULT(23,"等待暴鸡填写结果"),
    STATUS_AWAIT_PAY(24,"待支付/待收款"),
    STATUS_PLAYER_CANCEL_AFTER(25,"比赛后取消"),
    STATUS_PAYED_AWAIT_RESULT(26,"游戏后取消-玩家支付等待暴鸡提交结果"),
    STATUS_RESULT_AWAIT_PAY(27,"游戏后取消-暴鸡提交结果等待玩家支付"),
    STATUS_WAIT_APPLY_INFO(28,"待提交申诉信息(已支付)"),
    STATUS_APPLY_REFUND(29,"玩家申请免单"),
    STATUS_END(30,"订单结算结束"),
    STATUS_BAOJI_FREE(31,"暴鸡免单"),
    STATUS_VERIFY_REFUND_END(32,"审批结算-退款-结束"),
    STATUS_PLAYER_CANCEL_END(33,"比赛后取消-正常处理-结束"),
    STATUS_PAYED_APPLY_REFUN_WAIT_RESULT(34,"游戏后取消-玩家支付申请免单等待暴鸡提交结果"),
    STATUS_PAYED_NORMAL_WAIT_RESULT(35,"游戏后取消-玩家支付正常处理等待暴鸡提交结果"),
    STATUS_ABNORMAL_PAYED(36,"取消订单-已付款"),
    STATUS_PAYED_APPLY_REFUN_WAIT_RESULT_OUT_END(37,"游戏后取消-暴鸡提交比赛结果超时"),
    STATUS_VERIFY_NORMAL_END(38,"审批结算-正常-结束"),
    STATUS_PLAYER_SETTLE_TOMEOUT_END(39,"玩家提交结算方式超时结算"),
    STATUS_BAOJI_CANCEL_BEFORE_END(40,"比赛前-暴鸡取消订单"),
    STATUS_BAOJI_CANCEL_AFTER_END(41,"比赛后-暴鸡取消订单"),
    STATUS_PAY_TIMEOUT_WAIT_KH_PAY(42,"玩家支付超时-等待平台垫付审批"),
    STATUS_PAY_TIMEOUT_WAIT_KH_PAY_SUCCESS(43,"玩家支付超时-平台已支付"),
    STATUS_PAY_TIMEOUT_WAIT_KH_PAY_FAIL(44,"玩家支付超时-平台拒绝支付"),
    STATUS_PAY_TIMEOUT_PLAYER_PAIED_END(45,"玩家支付超时-玩家支付-结束"),
    STATUS_FIRST_ORDER_CANCEL(46,"刷单风险-取消订单");

    private Integer code;
    private String msg;

    GameOrderStatusType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
