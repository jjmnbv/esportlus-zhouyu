package com.kaihei.esportingplus.gamingteam.api.params;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

@ApiModel("订单结束微信小程序通知")
@Validated
public class WxTeamEndParams implements Serializable {

    private static final long serialVersionUID = 3035855024569131819L;
    @NotBlank(message = "游戏名称不能为空")
    private String gameName;
    @NotBlank(message = "副本名称不能为空")
    private String raidName;
    @NotBlank(message = "队长名称不能为空")
    private String leaderName;
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

    public List<WxTeamOrderPushData> getOrders() {
        return orders;
    }

    public void setOrders(List<WxTeamOrderPushData> orders) {
        this.orders = orders;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
