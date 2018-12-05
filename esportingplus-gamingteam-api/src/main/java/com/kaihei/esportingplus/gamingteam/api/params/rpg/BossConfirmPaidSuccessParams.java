package com.kaihei.esportingplus.gamingteam.api.params.rpg;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 老板支付成功后参数
 * @author liangyi
 */
@Validated
public class BossConfirmPaidSuccessParams implements Serializable {

    private static final long serialVersionUID = 7331141668488471015L;

    /** 车队序列号 */
    @NotEmpty(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1, example = "GT231312388327489")
    private String teamSequence;

    /** 订单序列号 */
    @NotEmpty(message = "订单序列号")
    @ApiModelProperty(value = "订单序列号", required = true, position = 2, example = "BJ20181001231312388327489")
    private String orderSequence;

    public BossConfirmPaidSuccessParams() {
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public String getOrderSequence() {
        return orderSequence;
    }

    public void setOrderSequence(String orderSequence) {
        this.orderSequence = orderSequence;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
