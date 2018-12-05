package com.kaihei.esportingplus.payment.api.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @description:流水查询接口参数
 * @author: xiaolijun
 **/
@ApiModel(value = "流水查询接口参数", description = "流水查询接口参数")
@Validated
public class BillQueryParams extends PageParams {

    private static final long serialVersionUID = -2272972000791242809L;

    /**
     * 支付渠道
     *
     * C001 暴鸡币钱包支付 C002 QQ支付 C003 微信支付 C004 支付宝支付 C005 苹果支付 C006 平台系统支付
     */
    @NotBlank(message = "支付渠道不能为空")
    @ApiModelProperty(value = "支付渠道", example = "001")
    @JsonProperty("channel")
    private String channel;

    /**
     * 订单维度
     *
     * D001 普通订单 D002 定向订单 D003 车队订单 D004 自定义技能订单 D005 悬赏订单 D006 系统订单
     */
    @NotBlank(message = "订单维度不能为空")
    @ApiModelProperty(value = "订单维度", required = true, example = "001")
    @JsonProperty("orderDimensionality")
    private String orderDimensionality;

    /**
     * 订单类型
     *
     * 001 退款订单 002 提现订单 003 暴击值兑换订单 004 暴鸡币打赏订单 005 结算订单
     */
    @NotBlank(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型", required = true, example = "001")
    @JsonProperty("orderType")
    private String orderType;

    /**
     * 钱包类型 001 暴鸡币 002 暴击值
     */
    @NotBlank(message = "钱包类型不能为空")
    @ApiModelProperty(value = "钱包类型", required = true, example = "001")
    @JsonProperty("moneyType")
    private String moneyType;

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true, position = 5, example = "223867704556126208")
    @JsonProperty("userId")
    private String userId;

    /**
     * T001 收入 T002 支出 T003 扣款 T004 退款
     */
    @NotBlank(message = "交易类型不能为空")
    @ApiModelProperty(value = "交易类型不能为空", required = true, position = 6, example = "T001")
    @JsonProperty("tradeType")
    private String tradeType;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrderDimensionality() {
        return orderDimensionality;
    }

    public void setOrderDimensionality(String orderDimensionality) {
        this.orderDimensionality = orderDimensionality;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
