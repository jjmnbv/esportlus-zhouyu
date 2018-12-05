package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;

/**
 * @author liangyi
 */
public class PVPTeamMembersIncome implements Serializable{
    private static final long serialVersionUID = 5684644518977935391L;
    /**
     * 队员uid
     */
    private String uid;

    /**
     * 队员名称
     */
    private String userName;

    /**
     * 老板支付单价或暴鸡原始预计收益金额
     */
    private Integer price;
    /**
     * 抽成后的暴鸡收益金额
     */
    private Integer profitAfterRatio;

    /**
     * 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    private Integer userIdentity;

    public PVPTeamMembersIncome() {
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

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getProfitAfterRatio() {
        return profitAfterRatio;
    }

    public void setProfitAfterRatio(Integer profitAfterRatio) {
        this.profitAfterRatio = profitAfterRatio;
    }
}
