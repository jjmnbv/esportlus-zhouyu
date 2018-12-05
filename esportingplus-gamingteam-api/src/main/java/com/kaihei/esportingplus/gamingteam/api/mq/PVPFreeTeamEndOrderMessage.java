package com.kaihei.esportingplus.gamingteam.api.mq;

import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberVO;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队结束消息，更新订单服务那边的状态和收益结算
 * @author liangyi
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PVPFreeTeamEndOrderMessage implements Serializable {

    private static final long serialVersionUID = -7728869554228366593L;
    /**
     * 车队序列号
     */
    private String teamSequence;
    //车队状态，0:未开车，1:已开车, 2:已结束
    private Integer teamStatus;
    /**
     * 房间Id
     */
    private Integer roomNum;
    /**
     * 游戏结果枚举Code
     */
    private Integer gameResultCode;

    /**
     * 游戏Id
     */
    private Integer gameId;

    private List<TeamMemberVO> teamMemberVOS;

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public Integer getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Integer teamStatus) {
        this.teamStatus = teamStatus;
    }

    public Integer getGameResultCode() {
        return gameResultCode;
    }

    public void setGameResultCode(Integer gameResultCode) {
        this.gameResultCode = gameResultCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
