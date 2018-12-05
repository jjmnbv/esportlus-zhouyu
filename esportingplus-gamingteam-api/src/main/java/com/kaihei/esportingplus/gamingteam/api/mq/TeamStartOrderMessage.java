package com.kaihei.esportingplus.gamingteam.api.mq;

import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 创建暴鸡订单的消息--立即开车
 * @author liangyi
 */
public class TeamStartOrderMessage implements Serializable {

    private static final long serialVersionUID = -7064221188875267597L;

    /**
     * 暴鸡队员信息
     */
    private RPGTeamStartOrderVO RPGTeamStartOrderVO;

    public TeamStartOrderMessage() {
    }

    public RPGTeamStartOrderVO getRPGTeamStartOrderVO() {
        return RPGTeamStartOrderVO;
    }

    public void setRPGTeamStartOrderVO(
            RPGTeamStartOrderVO RPGTeamStartOrderVO) {
        this.RPGTeamStartOrderVO = RPGTeamStartOrderVO;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
