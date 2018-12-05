package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @program: esportingplus
 * @description: 暴鸡币后台充值入参
 * @author: xusisi
 * @create: 2018-10-09 16:29
 **/
@Validated
@ApiModel(value = "暴鸡币后台充值入参", description = "暴鸡币后台充值入参")
public class GCoinBackStageRechargeParam {
    /***
     * 业务订单号
     */
    @NotBlank(message = "业务订单号")
    @ApiModelProperty(value = "业务订单号不能为空", example = "1232321323")
    private String outTradeNo;

    /***
     * 业务订单类型
     */
    @NotBlank(message = "业务订单类型不能为空")
    @ApiModelProperty(value = "业务订单类型", example = "001")
    private String orderType;

    /***
     * 用户ID
     */
    @NotBlank(message = "用户ID userId不能为空")
    @ApiModelProperty(value = "用户ID userId", required = true, example = "20180820163345228956821526941696")
    private String userId;

    /***
     * 暴鸡币充值数量
     */
    @NotBlank(message = "暴鸡币充值数量不能为空")
    @ApiModelProperty(value = "暴鸡币充值数量", required = true, example = "123")
    private String gcoinAmount;

    /***
     * 充值类型
     */
    @NotBlank(message = "充值类型不能为空")
    @ApiModelProperty(value = "充值类型", required = true, example = "001")
    private String rechargeType;

    /***
     * 推送消息
     */
    @NotBlank(message = "推送消息不能为空")
    @ApiModelProperty(value = "推送消息", required = true, example = "推送消息")
    private String message;

    /***
     * 备注
     */
    private String remarks;

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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGcoinAmount() {
        return gcoinAmount;
    }

    public void setGcoinAmount(String gcoinAmount) {
        this.gcoinAmount = gcoinAmount;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    @Override
    public String toString() {
        return "GCoinBackStageRechargeParam{" +
                "outTradeNo='" + outTradeNo + '\'' +
                ", orderType='" + orderType + '\'' +
                ", userId='" + userId + '\'' +
                ", gcoinAmount='" + gcoinAmount + '\'' +
                ", rechargeType='" + rechargeType + '\'' +
                ", message='" + message + '\'' +
                ", remarks='" + remarks + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
