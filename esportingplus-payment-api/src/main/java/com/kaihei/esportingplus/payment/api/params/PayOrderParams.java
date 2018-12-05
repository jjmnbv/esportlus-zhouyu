package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * 支付参数设置，这里主要是封装无状态的参数
 */
@Data
@Validated
@ApiModel(value = "创建第三方支付订单参数", description = "创建第三方支付订单参数")
public class PayOrderParams {

    /***
     * 用户ID
     */
    @NotBlank(message = "用户ID userId 不能为空")
    @ApiModelProperty(value = "用户ID userId", required = true, example = "12323123213")
    private String userId;

    /***
     * 业务订单号
     */
    @NotBlank(message = "业务订单号 outTradeNo 不能为空")
    @ApiModelProperty(value = "业务订单号 outTradeNo ", required = true, example = "12323123")
    private String outTradeNo;

    /***
     * 支付金额（单位：分）
     */
    @NotNull(message = "支付金额 totalAmount 不能为空")
    @ApiModelProperty(value = "支付金额", required = true, example = "100")
    private Integer totalAmount;

    /***
     * 业务订单类型
     */
    @NotBlank(message = "业务订单类型 orderType 不能为空")
    @ApiModelProperty(value = "业务订单类型 orderType")
    private String orderType;

    /***
     * 支付结果回调URL
     */
    @NotBlank(message = "支付回调地址 notifyUrl 不能为空")
    @ApiModelProperty(value = "支付回调地址 notifyUrl", required = true, example = "http://www.baidu.com")
    private String notifyUrl;

    /***
     * 货币类型
     */
    @NotBlank(message = "支付货币类型 currencyType 不能为空")
    @ApiModelProperty(value = "支付货币类型 currencyType", required = true, example = "CN")
    private String currencyType;

    /***
     * 主题
     */
    @NotBlank(message = "主题 subject 不能为空")
    @ApiModelProperty(value = "主题 subject", required = true, example = "支付宝支付")
    private String subject;

    /***
     * 内容
     */
    @ApiModelProperty(value = "内容 body", required = false, example = "支付宝支付-3元")
    private String body;

    /***
     * 描述
     */
    @ApiModelProperty(value = "描述 description", required = false, example = "消息描述信息")
    private String description;

    /***
     * IP地址
     */
    @ApiModelProperty(value = "IP地址 ip", required = false, example = "127.0.0.1")
    private String ip;

    /***
     * 区域
     */
    @ApiModelProperty(value = "区域 area", required = false, example = "beijing")
    private String area;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
