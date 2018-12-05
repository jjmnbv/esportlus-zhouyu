package com.kaihei.esportingplus.gamingteam.api.params;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
public class WxTeamOrderCancelParams {
    @NotBlank(message = "游戏名称不能为空")
    private String gameName;
    @NotBlank(message = "副本名称不能为空")
    private String raidName;
    /**
     * 订单取消原因类型 1:队长解散车队
     */
    @NotNull(message = "原因类型不能为空")
    private Integer reasonType;
    @NotEmpty(message = "订单信息不能为空")
    private List<WxTeamOrderPushData> orders;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }


    public Integer getReasonType() {
        return reasonType;
    }

    public void setReasonType(Integer reasonType) {
        this.reasonType = reasonType;
    }

    public List<WxTeamOrderPushData> getOrders() {
        return orders;
    }

    public void setOrders(
            List<WxTeamOrderPushData> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
