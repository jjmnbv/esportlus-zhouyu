package com.kaihei.esportingplus.payment.api.enums;

/**
 * 云账户返回状态Status——订单State映射枚举类
 * @author chenzhenjun
 */
public enum CloudStatusStateMappingEnum {

    /**
     * 被标记为删除的订单
     */
    DELETE(-1, "DELETE"),
    /**
     * 处理中
     */
    PROCESSING(0, "PROCESSING"),
    /**
     * 打款成功
     */
    SUCCESS(1, "SUCCESS"),
    /**
     * 打款失败：主要指订单数据校验不通过
     */
    FAILED(2, "FAILED"),
    /**
     * 暂停处理，一般是账户余额不足，充值后可以继续打款
     */
    SUSPEND(4, "PROCESSING"),
    /**
     * 调用支付网关超时等状态异常情况导致，处于等待交易查证的中间状态
     */
    WAITING(5, "PROCESSING"),
    /**
     * 订单税务筹划完毕，等待执行打款的状态
     */
    WAIT_EXECUTE(8, "PROCESSING"),
    /**
     * 打款失败（已退款，退汇或者冲正)
     */
    TIMEOUT(9, "FAILED"),
    /**
     * 订单被取消，无需支付
     */
    CANCEL(15, "CANCEL");

    private final int value;
    private final String name;

    CloudStatusStateMappingEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * find enum by channel tag.
     */
    public static CloudStatusStateMappingEnum lookup(int value) {
        for (CloudStatusStateMappingEnum channel : values()) {
            if (channel.value == value) {
                return channel;
            }
        }
        throw new IllegalArgumentException("No matching channel constant for [" + value + "]");
    }


}
