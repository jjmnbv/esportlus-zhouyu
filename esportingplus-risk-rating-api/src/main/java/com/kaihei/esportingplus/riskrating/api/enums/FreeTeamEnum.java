package com.kaihei.esportingplus.riskrating.api.enums;

/**
 * 免费车队-风控参数枚举类
 * @author chenzhenjun
 */
public enum FreeTeamEnum {

    SUCCESS("success", "正常"),

    DAY_CHECK("day_limit", "该数美id上车次数超出每日可使用的免费上车机会上限"),

    WEEK_CHECK("week_limit", "该数美id上车次数超出每周可使用的免费上车机会上限"),

    MALICE_DEVICE("malice_device", "恶意设备，免费上车机会需冻结"),

    /**    风控-登陆注册   */
    LOGIN_REJECT("login_reject", "登录操作异常，请稍后再试"),

    LOGIN_OVER_LIMIT("login_over_limit", "抱歉，该设备登录账号数已达到上限，请使用原有登录账号进行登录"),

    REGISTER_REJECT("register_reject", "注册操作异常，请稍后再试"),

    REGISTER_OVER_LIMIT("register_over_limit", "抱歉，该设备注册账号数已达到上限"),

    REGISTER_REPEAT("register_repeat", "重复注册"),

    REGISTER_AWARD_REJECT("register_award_reject", "该设备已有用户注册，后续注册的账号不享受新用户奖励"),

    /**    风控-提现黑名单   **/
    TRANSFER_LIMIT("transfer_limit", "该用户在提现黑名单中，已限制提现");

    /**
     * 风控编码
     */
    private String code;
    /**
     * 风控消息
     */
    private String msg;

    FreeTeamEnum(String code, String msg) {
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
