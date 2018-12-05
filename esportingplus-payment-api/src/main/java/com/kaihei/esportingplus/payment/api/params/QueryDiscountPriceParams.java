package com.kaihei.esportingplus.payment.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询折扣价入参
 *
 * @author haycco
 */
@ApiModel(value = "查询折扣价入参", description = "查询折扣价入参")
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryDiscountPriceParams implements Serializable {

    private static final long serialVersionUID = -1614714256983389700L;
    /***
     * 业务订单号
     */
    @NotBlank(message = "业务订单号")
    @ApiModelProperty(value = "业务订单号", required = false, example = "201808201633452289568")
    private String outTradeNo;

    /***
     * 条目唯一标识
     */
    @NotBlank(message = "唯一标识")
    @ApiModelProperty(value = "唯一标识", required = false, example = "201808")
    private String id;

    /***
     * 原价（单位：分）
     */
    @NotNull(message = "原价（单位：分）")
    @ApiModelProperty(value = "原价（单位：分）", required = true, example = "2000")
    private Integer sourceValue;

    /***
     * 优惠价（单位：分）
     */
    @NotNull(message = "优惠价（单位：分）")
    @ApiModelProperty(value = "优惠价（单位：分）", required = false, example = "1980")
    private Integer discountValue;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(Integer sourceValue) {
        this.sourceValue = sourceValue;
    }

    public Integer getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Integer discountValue) {
        this.discountValue = discountValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
