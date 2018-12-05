package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * 创建暴鸡币支付入参
 *
 * @author: xusisi
 **/
@Validated
@ApiModel(value = "创建暴鸡币支付订单参数", description = "创建暴鸡币支付订单参数")
public class GCoinPaymentCreateParams {

    /***
     * 用户ID
     */
    @NotBlank(message = "用户ID userId不能为空")
    @ApiModelProperty(value = "用户ID userId", required = true, example = "20180820163345228956821526941696")
    private String userId;

    /***
     * 支付暴鸡币数量（单位：分）
     */
    @NotBlank(message = "支付暴鸡币数量amount不能为空")
    @ApiModelProperty(value = "支付暴鸡币数量amount", required = true, example = "100")
    private String amount;

    /***
     *业务订单号
     */
    @NotBlank(message = "业务订单号outTradeNo不能为空")
    @ApiModelProperty(value = "业务订单号outTradeNo", required = true, example = "20180820163345228956821526941696")
    private String outTradeNo;

    /***
     * 业务订单类型
     */
    @NotBlank(message = "业务订单类型orderType不能为空")
    @ApiModelProperty(value = "业务订单类型orderType", required = true, example = "001")
    private String orderType;

    /**
     * 附加数据（json格式）
     */
    private String attach;
    /***
     * 数据来源
     * （ANDROID表示android、IOS表示ios、H5表示H5、MP表示小程序）
     */
    @NotBlank(message = "数据来源sourceId不能为空")
    @ApiModelProperty(value = "数据来源sourceId", required = true, example = "IOS")
    private String sourceId;

    /***
     * 主题
     */
    @ApiModelProperty(value = "主题", required = true, example = "主题")
    private String subject;
    /***
     * 内容
     */
    private String body;

    /***
     *  描述
     */
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

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
