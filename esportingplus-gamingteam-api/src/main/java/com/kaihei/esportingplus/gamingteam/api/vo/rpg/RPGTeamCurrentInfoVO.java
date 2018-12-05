package com.kaihei.esportingplus.gamingteam.api.vo.rpg;

import com.kaihei.esportingplus.trade.api.vo.PVPTeamMembersIncome;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队当前的详细信息(包含队员列表)
 * @author liangyi
 */
public class RPGTeamCurrentInfoVO implements Serializable{

    private static final long serialVersionUID = 2221042927694784654L;

    /** 车队状态，0：准备中 1：已发车(进行中) 2：已解散 3：已结束 */
    private Integer teamStatus;

    /** 车队当前的实际位置数 */
    private Integer actuallyPositionCount;

    /** 车队当前禁用的位置数 */
    private Integer disablePositionCount;

    /** 车队当前的队员数量 */
    private Integer memberSize;

    /** 当前队员的 uid */
    private String uid;

    /**
     * 当前车队队员的金额
     * 如果是暴鸡则为收益金额
     * 如果是老板则为支付金额
     */
    private Integer price;

    /** 订单列表详情 */
    private List<PVPTeamMembersIncome> teamMemberProfitList;

    /**
     * 车队队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     */
    private Integer userIdentity;

    /** 老板支付倒计时, 暴鸡为 0 */
    private Integer payCountdown;

    /** 当前车队的成员信息列表 */
    List<RPGRedisTeamMemberBaseVO> teamMemberList;

    public RPGTeamCurrentInfoVO() {
    }

    public Integer getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Integer teamStatus) {
        this.teamStatus = teamStatus;
    }

    public Integer getActuallyPositionCount() {
        return actuallyPositionCount;
    }

    public void setActuallyPositionCount(Integer actuallyPositionCount) {
        this.actuallyPositionCount = actuallyPositionCount;
    }

    public Integer getDisablePositionCount() {
        return disablePositionCount;
    }

    public void setDisablePositionCount(Integer disablePositionCount) {
        this.disablePositionCount = disablePositionCount;
    }

    public Integer getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(Integer memberSize) {
        this.memberSize = memberSize;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<PVPTeamMembersIncome> getTeamMemberProfitList() {
        return teamMemberProfitList;
    }

    public void setTeamMemberProfitList(
            List<PVPTeamMembersIncome> teamMemberProfitList) {
        this.teamMemberProfitList = teamMemberProfitList;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Integer getPayCountdown() {
        return payCountdown;
    }

    public void setPayCountdown(Integer payCountdown) {
        this.payCountdown = payCountdown;
    }

    public List<RPGRedisTeamMemberBaseVO> getTeamMemberList() {
        return teamMemberList;
    }

    public void setTeamMemberList(
            List<RPGRedisTeamMemberBaseVO> teamMemberList) {
        this.teamMemberList = teamMemberList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
