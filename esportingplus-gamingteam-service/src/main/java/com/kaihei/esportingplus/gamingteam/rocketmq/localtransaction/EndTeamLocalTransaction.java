package com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction;

import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcEndTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinOrderCancelEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinOrderEndEvent;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 结束车队(解散车队、正常结束车队)时的本地事务
 * 包括: 车队数据(车队队员数据)入库、异步推送融云和微信通知
 * @author liangyi
 */
public class EndTeamLocalTransaction implements Serializable {

    private static final long serialVersionUID = -4349009728507161849L;

    /**
     * 车队 id
     */
    private Long teamId;

    /**
     * 车队序列号
     */
    private String teamSequence;

    /**
     * 车队状态
     */
    private Integer teamStatus;

    /**
     * 车队比赛结果
     */
    private Integer gameResult;

    /**
     * 待入库的车队队员信息
     */
    List<RPGRedisTeamMemberVO> persistenceMemberList;

    /**
     * 融云
     */
    RcEndTeamEvent rcEndTeamEvent;

    /**
     * 微信订单取消
     */
    WeXinOrderCancelEvent weXinOrderCancelEvent;

    /**
     * 微信订单结束
     */
    WeXinOrderEndEvent weXinOrderEndEvent;

    public EndTeamLocalTransaction() {
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

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

    public Integer getGameResult() {
        return gameResult;
    }

    public void setGameResult(Integer gameResult) {
        this.gameResult = gameResult;
    }

    public List<RPGRedisTeamMemberVO> getPersistenceMemberList() {
        return persistenceMemberList;
    }

    public void setPersistenceMemberList(
            List<RPGRedisTeamMemberVO> persistenceMemberList) {
        this.persistenceMemberList = persistenceMemberList;
    }

    public RcEndTeamEvent getRcEndTeamEvent() {
        return rcEndTeamEvent;
    }

    public void setRcEndTeamEvent(
            RcEndTeamEvent rcEndTeamEvent) {
        this.rcEndTeamEvent = rcEndTeamEvent;
    }

    public WeXinOrderCancelEvent getWeXinOrderCancelEvent() {
        return weXinOrderCancelEvent;
    }

    public void setWeXinOrderCancelEvent(
            WeXinOrderCancelEvent weXinOrderCancelEvent) {
        this.weXinOrderCancelEvent = weXinOrderCancelEvent;
    }

    public WeXinOrderEndEvent getWeXinOrderEndEvent() {
        return weXinOrderEndEvent;
    }

    public void setWeXinOrderEndEvent(
            WeXinOrderEndEvent weXinOrderEndEvent) {
        this.weXinOrderEndEvent = weXinOrderEndEvent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
