package com.kaihei.esportingplus.payment.api.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 流水保存接口参数
 *
 * @author: xiaolijun
 **/
//@ApiModel(value = "流水保存接口参数", description = "流水保存接口参数")
@Validated
public class PaymentBillSaveParams implements Serializable {

    private static final long serialVersionUID = 4190206911597748867L;

    /**
     * 支付渠道
     * <p>
     * C001 暴鸡币钱包支付
     * C002 QQ支付
     * C003 微信支付
     * C004 支付宝支付
     * C005 苹果支付
     * C006 平台系统支付
     */
    @Column(length = 8)
    private String channel;

    /**
     * 订单维度
     * <p>
     * D001 普通订单
     * D002 定向订单
     * D003 车队订单
     * D004 自定义技能订单
     * D005 悬赏订单
     * D006 系统订单
     */
    @Column(length = 8)
    private String orderDimension;

    /**
     * 订单类型
     * <p>
     * 001 退款订单
     * 002 提现订单
     * 003 暴击值兑换订单
     * 004 暴鸡币打赏订单
     * 005 结算订单
     */
    @Column(nullable = false, length = 8)
    private String orderType;

    /**
     * 交易类型
     * <p>
     * T001 收入
     * T002 支出
     * T003 扣款
     * T004 退款
     */
    @Column(nullable = false, length = 8)
    private String tradeType;

    /**
     * 业务类型
     * <p>
     * B0001 王者荣耀
     * B0002 英雄联盟
     * B0003 绝地求生
     * B0004 刺激战场
     * B0005 QQ飞车
     * B0006 全军出击
     * B0007 荒野行动
     * B0008 决战平安京
     * B0009 堡垒之夜
     * B0010 投诉补偿
     * B0011 订单补偿
     * B0012 活动补偿
     */
    @Column(length = 16)
    private String businessType;

    /**
     * 流水金额
     */
    @NotNull(message = "金额不能为空")
    @ApiModelProperty(value = "金额", required = true, position = 6, example = "100.00")
    @JsonProperty("amount")
    private BigDecimal amount;

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    @ApiModelProperty(value = "订单号", required = true, position = 7, example = "1234567")
    @JsonProperty("orderId")
    private String orderId;

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true, position = 8, example = "223867704556126208")
    @JsonProperty("userId")
    private String userId;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", required = false, position = 9, example = "描述")
    private String body;

    /**
     * 操作终端
     * H5表示H5端
     * ANDROID表示android
     * IOS表示ios
     * MP表示小程序
     */
    @NotBlank(message = "操作终端不能为空")
    @ApiModelProperty(value = "操作终端", required = true, position = 10, example = "IOS")
    @JsonProperty("sourceId")
    private String sourceId;

    /**
     * 主题
     */
    @NotBlank(message = "主题不能为空")
    @ApiModelProperty(value = "主题", required = true, position = 11, example = "主题")
    @JsonProperty("subject")
    private String subject;

    /**
     * 收支余额类型
     * 001暴鸡币
     * 002暴击值
     */
    @NotBlank(message = "收支余额类型不能为空")
    @ApiModelProperty(value = "收支余额类型", required = true, position = 12, example = "001")
    @JsonProperty("balanceType")
    private String balanceType;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrderDimension() {
        return orderDimension;
    }

    public void setOrderDimension(String orderDimension) {
        this.orderDimension = orderDimension;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
