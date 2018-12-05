package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @program: esportingplus
 * @description: 后台扣款入参
 * @author: xusisi
 * @create: 2018-10-11 11:38
 **/
@ApiModel(value = "后台扣款入参", description = "后台扣款入参")
@Validated
public class DeductParams {

    /***
     * 业务订单号
     */
    @NotBlank(message = "业务订单号")
    @ApiModelProperty(value = "业务订单号不能为空", required = true, example = "201808201633452289568")
    private String outTradeNo;

    /****
     * 业务订单类型
     */
    @NotBlank(message = "业务订单类型")
    @ApiModelProperty(value = "业务订单类型", required = true, example = "001")
    private String orderType;

    /***
     * 用户ID
     */
    @NotBlank(message = "用户ID userId不能为空")
    @ApiModelProperty(value = "用户ID userId", required = true, example = "20180820163345228956821526941696")
    private String userId;

    /***
     * 扣款暴鸡币、暴击值数量
     */
    @NotBlank(message = "扣款暴鸡币、暴击值数量不能为空")
    @ApiModelProperty(value = "扣款暴鸡币、暴击值数量", required = true, example = "110")
    private String amount;

    /***
     * 扣款类型
     */
    @NotBlank(message = "扣款类型不能为空")
    @ApiModelProperty(value = "扣款类型", required = true, example = "001")
    private String deductType;

    /***
     * 推送消息
     */
    @NotBlank(message = "推送消息不能为空")
    @ApiModelProperty(value = "推送消息", required = true, example = "推送消息")
    private String message;

    /***
     * 货币类型：001暴鸡币，002暴击值
     */
    @NotBlank(message = "货币类型")
    @ApiModelProperty(value = "货币类型", required = true, example = "001")
    private String currencyType;

    /***
     * 主题
     */
    private String subject;
    /***
     * 内容
     */
    private String body;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeductType() {
        return deductType;
    }

    public void setDeductType(String deductType) {
        this.deductType = deductType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
