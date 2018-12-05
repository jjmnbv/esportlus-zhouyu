package com.kaihei.esportingplus.gamingteam.api.params;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@Validated
public class WxTeamStartParams implements Serializable{

    private static final long serialVersionUID = -2844537828315830027L;
    @NotBlank(message = "游戏名称不能为空")
    private String gameName;
    @NotBlank(message = "副本名称不能为空")
    private String raidName;
    @NotBlank(message = "跨区名称不能为空")
    private String zoneAcrossName;
    @NotBlank(message = "车队序列号不能为空")
    private String teamSequence;
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

    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName;
    }

    public List<WxTeamOrderPushData> getOrders() {
        return orders;
    }

    public void setOrders(
            List<WxTeamOrderPushData> orders) {
        this.orders = orders;
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
