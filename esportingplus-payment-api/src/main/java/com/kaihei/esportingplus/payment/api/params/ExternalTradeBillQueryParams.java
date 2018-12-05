package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * @author: tangtao
 **/
@Validated
@ApiModel(value = "支付流水查询参数", description = "支付流水查询参数")
public class ExternalTradeBillQueryParams implements Serializable {

    private static final long serialVersionUID = -2037272939047734071L;

    @ApiModelProperty(value = "流水号", example = "123456")
    private String flowNo;

    @ApiModelProperty(value = "第三方支付流水号", example = "123456")
    private String transactionId;

    @ApiModelProperty(value = "支付渠道", example = "C003")
    private String channel;

    @ApiModelProperty(value = "业务单号", example = "123456")
    private String outTradeNo;

    @ApiModelProperty(value = "业务单类型", example = "001")
    private String orderType;

    @ApiModelProperty(value = "支付订单号", example = "123456")
    private String orderId;

    @ApiModelProperty(value = "交易类型", example = "001")
    private String tradeType;

    @NotEmpty(message = "流水起始日期不能为空")
    @ApiModelProperty(value = "流水起始日期", required = true, example = "2018-10-20")
    private String createDateBegin;

    @NotEmpty(message = "流水结束日期不能为空")
    @ApiModelProperty(value = "流水结束日期", required = true, example = "2018-10-30")
    private String createDateEnd;

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getCreateDateBegin() {
        return createDateBegin;
    }

    public void setCreateDateBegin(String createDateBegin) {
        this.createDateBegin = createDateBegin;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }
}
