package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 提现订单修改参数
 *
 * @author: chenzhenjun
 **/
@Validated
@ApiModel(value = "修改提现状态参数", description = "修改提现状态参数")
public class WithdrawUpdateParams implements Serializable {

    private static final long serialVersionUID = -1136438890892507296L;

    /***
     * 用户Id
     */
    @ApiModelProperty(value = "提现第三方流水号", required = true, example = "223867704556126208")
    private String outTradeNo;

    /***
     * 货币类型(目前只有暴击值)
     */
    @NotBlank(message = "提现结果不能为空")
    @ApiModelProperty(value = "提现结果", required = true, example = "success")
    private String remitStatus;

    /***
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = false, example = "7e709bc1")
    private String userId;

    /***
     * 用户ID
     */
    @ApiModelProperty(value = "订单ID", required = false, example = "242726094779977728")
    private String orderId;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getRemitStatus() {
        return remitStatus;
    }

    public void setRemitStatus(String remitStatus) {
        this.remitStatus = remitStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
