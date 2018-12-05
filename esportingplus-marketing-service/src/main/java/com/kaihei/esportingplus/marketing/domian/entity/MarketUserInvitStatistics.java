package com.kaihei.esportingplus.marketing.domian.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "market_user_invit_statistics")
public class MarketUserInvitStatistics {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 用户userId
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 成功邀请数量
     */
    @Column(name = "invited_amount")
    private Long invitedAmount;

    /**
     * 奖励暴击币数量
     */
    @Column(name = "coin_award_amount")
    private Long coinAwardAmount;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    public MarketUserInvitStatistics(Long id, Integer userId, Long invitedAmount, Long coinAwardAmount, Date updateTime, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.invitedAmount = invitedAmount;
        this.coinAwardAmount = coinAwardAmount;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public MarketUserInvitStatistics() {
        super();
    }

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户userId
     *
     * @return user_id - 用户userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户userId
     *
     * @param userId 用户userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取成功邀请数量
     *
     * @return invited_amount - 成功邀请数量
     */
    public Long getInvitedAmount() {
        return invitedAmount;
    }

    /**
     * 设置成功邀请数量
     *
     * @param invitedAmount 成功邀请数量
     */
    public void setInvitedAmount(Long invitedAmount) {
        this.invitedAmount = invitedAmount;
    }

    /**
     * 获取奖励暴击币数量
     *
     * @return coin_award_amount - 奖励暴击币数量
     */
    public Long getCoinAwardAmount() {
        return coinAwardAmount;
    }

    /**
     * 设置奖励暴击币数量
     *
     * @param coinAwardAmount 奖励暴击币数量
     */
    public void setCoinAwardAmount(Long coinAwardAmount) {
        this.coinAwardAmount = coinAwardAmount;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}