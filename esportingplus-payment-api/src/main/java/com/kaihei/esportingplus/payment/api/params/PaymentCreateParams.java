package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建充值订单参数
 *
 * @author: xusisi
 **/
@Validated
@ApiModel(value = "创建充值订单参数", description = "创建充值订单参数")
public class PaymentCreateParams implements Serializable {

    private static final long serialVersionUID = -2361791993245422197L;

    /***
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true, example = "223867704556126208")
    private String userId;

    /***
     * 充值金额（单位：分）
     */
    @NotBlank(message = "充值金额不能为空")
    @ApiModelProperty(value = "充值金额", required = true, example = "110")
    private String amount;

    /***
     * 暴鸡币数量
     */
    @NotBlank(message = "暴鸡币数量不能为空")
    @ApiModelProperty(value = "暴鸡币数量", required = true, example = "1.00")
    private String gcoinAmount;

    /***
     * 操作系统
     * PC表示PC端、ANDROID表示android、IOS表示ios
     */
    @NotBlank(message = "操作系统不能为空")
    @ApiModelProperty(value = "操作系统", required = true, example = "ANDROID")
    private String sourceId;

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

    public String getGcoinAmount() {
        return gcoinAmount;
    }

    public void setGcoinAmount(String gcoinAmount) {
        this.gcoinAmount = gcoinAmount;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
