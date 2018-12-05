package com.kaihei.esportingplus.gamingteam.api.params;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
public class WxTeamOrderPushData implements Serializable {

    private static final long serialVersionUID = 8149202729883354539L;
    @NotBlank(message = "用户不能为空")
    private String uid;
    @NotNull(message = "订单金额不能为空,单位：分")
    private Integer orderMoney;
    @NotBlank(message = "订单序列号不能为空")
    private String orderSequence;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(Integer orderMoney) {
        this.orderMoney = orderMoney;
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
