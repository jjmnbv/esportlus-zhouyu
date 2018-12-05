package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;

public class TeamMemberOrderVo implements Serializable {

    private static final long serialVersionUID = -6693050793350806165L;
    /**
     * 队员uid
     */
    private String uid;
    /**
     * 车队队员昵称
     */
    private String username;

    /**
     * 车队队员头像
     */
    private String avatar;
    /**
     * 作为队长开团总数
     */
    private Integer teamLeaderCount;
    /**
     * 队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    private Byte userIdentity;
    /**
     *  订单状态 0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    private Byte orderStatus;
    /**
     * 暴鸡等级，只有队长和暴鸡才有，老板没有
     */
    private Integer baojiLevel;
    /**
     * 作为队长或暴鸡的收益金额，单位分
     */
    private Integer incomeAmount;
    /**
     * 性别
     */
    private Integer sex;
    /**
     *用户实付金额，用户实际支付的金额（剔除退款和优惠的金额），单位为分,针对老板
     */
    private Integer actualPaidAmount;
    /**
     *用户下单总数,针对老板
     */
    private Integer orderCount;
    /**
     * 优惠券金额
     */
    private Integer discountAmount;
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getTeamLeaderCount() {
        return teamLeaderCount;
    }

    public void setTeamLeaderCount(Integer teamLeaderCount) {
        this.teamLeaderCount = teamLeaderCount;
    }

    public Byte getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Byte userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public Integer getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Integer incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getActualPaidAmount() {
        return actualPaidAmount;
    }

    public void setActualPaidAmount(Integer actualPaidAmount) {
        this.actualPaidAmount = actualPaidAmount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }
}
