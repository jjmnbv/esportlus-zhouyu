package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 创建提现订单参数
 *
 * @author: xusisi
 * @update: 2018-09-23 10:33 update by chenzhenjun
 **/
@Validated
@ApiModel(value = "创建提现订单参数", description = "创建提现订单参数")
public class WithdrawCreateParams implements Serializable {

    private static final long serialVersionUID = 5616215733757375375L;
    /***
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true, example = "223867704556126208")
    private String userId;

    /***
     * 提现暴击值（单位：分）
     */
    @NotNull(message = "提现暴击值不能为空")
    @ApiModelProperty(value = "提现暴击值", required = true, example = "110")
    private Integer amount;

    /***
     * 提现渠道
     * C003-微信 C004-支付宝 C007-工作室）
     */
    @NotBlank(message = "提现渠道不能为空")
    @ApiModelProperty(value = "提现渠道", required = true, example = "C003")
    private String channel;

    /***
     * 通知标志
     */
    @NotBlank(message = "通知标志不能为空")
    @ApiModelProperty(value = "通知标志", required = true, example = "1")
    private String notifyFlag;

    /***
     * 第三方订单号
     */
    @NotBlank(message = "第三方订单号不能为空")
    @ApiModelProperty(value = "第三方订单号", required = true, example = "12122222222")
    private String outTradeNo;

    /***
     * 货币类型
     */
    @NotBlank(message = "货币类型不能为空")
    @ApiModelProperty(value = "货币类型", required = true, example = "starlight")
    private String moneyType;

    /***
     * 主题
     */
    @ApiModelProperty(value = "主题", required = false, example = "主题")
    private String subject;

    /***
     * 内容
     */
    @ApiModelProperty(value = "内容", required = false, example = "内容")
    private String body;

    /***
     * 描述
     */
    @ApiModelProperty(value = "描述", required = false, example = "描述")
    private String description;

    /***
     * 数据来源
     * （ANDROID表示android、IOS表示ios、H5表示H5、MP表示小程序）
     */
    @NotBlank(message = "数据来源sourceId不能为空")
    @ApiModelProperty(value = "数据来源sourceId", required = true, example = "IOS")
    private String sourceId;

    /***
     * 回调Url
     */
    @NotBlank(message = "回调Url不能为空")
    @ApiModelProperty(value = "python回调Url", required = true, example = "api/v1/transfer")
    private String notifyUrl;

    /**
     * 客户端IP
     */
    private String clientIp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNotifyFlag() {
        return notifyFlag;
    }

    public void setNotifyFlag(String notifyFlag) {
        this.notifyFlag = notifyFlag;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
