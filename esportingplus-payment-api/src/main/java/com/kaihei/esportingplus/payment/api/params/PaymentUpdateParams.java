package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 更新充值订单参数
 *
 * @author: xusisi
 **/
@Validated
@ApiModel(value = "更新充值订单参数", description = "更新充值订单参数")
public class PaymentUpdateParams implements Serializable {

    private static final long serialVersionUID = 3737828163879846396L;

    /***
     * 充值订单ID
     */
    @NotBlank(message = "充值订单Id不能为空")
    @ApiModelProperty(value = "充值订单Id", required = true, example = "12122222222")
    private String orderId;


    /***
     * 支付方式
     * 001表示暴鸡钱包，002表示微信支付，003表示支付宝支付，004表示QQ钱包，005表示apple支付
     */
    @NotBlank(message = "支付方式不能为空")
    @ApiModelProperty(value = "支付方式", required = true, example = "001")
    private String channel;

    /***
     * 用户Id
     */
    @NotBlank(message = "用户Id不能为空")
    @ApiModelProperty(value = "用户ID", required = true, example = "12122222222")
    private String userId;

    /***
     * apple支付收据
     */
    @ApiModelProperty(value = "apple支付收据", required = false, example = "12122222222")
    private String receiptData;

    /***
     * 第三方支付订单编号
     */
    @ApiModelProperty(value = "第三方支付订单编号", required = false, example = "12122222222")
    private String paymentOrderNo;

    /***
     * 第三方支付成功时间
     */
    @ApiModelProperty(value = "第三方支付成功时间", required = false, example = "2018-12-12 11:11:11")
    private String paymentDate;

    /***
     * 实收金额
     */
    @ApiModelProperty(value = "实收金额", required = false, example = "12")
    private int payInAmount;

    /***
     * 主题
     */
    @ApiModelProperty(value = "主题", required = false, example = "请求主题")
    private String subject;

    /***
     * 内容
     */
    @ApiModelProperty(value = "内容", required = false, example = "请求内容")
    private String body;

    /***
     * 描述
     */
    @ApiModelProperty(value = "描述", required = false, example = "请求描述")
    private String description;

    public String getPaymentOrderNo() {
        return paymentOrderNo;
    }

    public void setPaymentOrderNo(String paymentOrderNo) {
        this.paymentOrderNo = paymentOrderNo;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public String getReceiptData() {
        return receiptData;
    }

    public void setReceiptData(String receiptData) {
        this.receiptData = receiptData;
    }

    public int getPayInAmount() {
        return payInAmount;
    }

    public void setPayInAmount(int payInAmount) {
        this.payInAmount = payInAmount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
