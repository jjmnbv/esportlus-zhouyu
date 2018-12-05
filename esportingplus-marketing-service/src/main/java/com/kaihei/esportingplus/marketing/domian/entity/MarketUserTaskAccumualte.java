package com.kaihei.esportingplus.marketing.domian.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "market_user_task_accumualte")
public class MarketUserTaskAccumualte {

    /**
     * 1.用户注册 2：邀请好友，3：好友消费，4：好友完成车队奖励，5好友邀请好友
     */
    public static final int USER_REGIST = 1, INVIT_FRIEND = 2, COIN_CONSUME = 3, FINISHED_TEAM = 4, FRIEND_INVIT_FRIEND = 5;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 用户userid
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 累计数量：邀请用户数量/完成订单数量/消耗暴击币数量/完成车队次数
     */
    private Integer accumulate;

    /**
     * 奖励时数量快照
     */
    @Column(name = "award_snapshot")
    private Integer awardSnapshot;

    /**
     * 邀请分享类型 1：新版本用户奖励，2：邀请好友奖励，3：好友消费，4：好友完成车队奖励，5：好友成功邀请新人
     */
    private Integer type;

    /**
     * 任务批次日期
     */
    @Column(name = "batch_date")
    private Date batchDate;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    public MarketUserTaskAccumualte(Long id, Integer userId, Integer accumulate, Integer awardSnapshot, Integer type, Date batchDate, Date updateTime, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.accumulate = accumulate;
        this.awardSnapshot = awardSnapshot;
        this.type = type;
        this.batchDate = batchDate;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public MarketUserTaskAccumualte() {
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
     * 获取用户userid
     *
     * @return user_id - 用户userid
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户userid
     *
     * @param userId 用户userid
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取累计数量：邀请用户数量/完成订单数量/消耗暴击币数量/完成车队次数
     *
     * @return accumulate - 累计数量：邀请用户数量/完成订单数量/消耗暴击币数量/完成车队次数
     */
    public Integer getAccumulate() {
        return accumulate;
    }

    /**
     * 设置累计数量：邀请用户数量/完成订单数量/消耗暴击币数量/完成车队次数
     *
     * @param accumulate 累计数量：邀请用户数量/完成订单数量/消耗暴击币数量/完成车队次数
     */
    public void setAccumulate(Integer accumulate) {
        this.accumulate = accumulate;
    }

    /**
     * 获取奖励时数量快照
     *
     * @return award_snapshot - 奖励时数量快照
     */
    public Integer getAwardSnapshot() {
        return awardSnapshot;
    }

    /**
     * 设置奖励时数量快照
     *
     * @param awardSnapshot 奖励时数量快照
     */
    public void setAwardSnapshot(Integer awardSnapshot) {
        this.awardSnapshot = awardSnapshot;
    }

    /**
     * 获取邀请分享类型 1：新版本用户奖励，2：邀请好友奖励，3：好友消费，4：好友完成车队奖励，5：好友成功邀请新人
     *
     * @return type - 邀请分享类型 1：新版本用户奖励，2：邀请好友奖励，3：好友消费，4：好友完成车队奖励，5：好友成功邀请新人
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置邀请分享类型 1：新版本用户奖励，2：邀请好友奖励，3：好友消费，4：好友完成车队奖励，5：好友成功邀请新人
     *
     * @param type 邀请分享类型 1：新版本用户奖励，2：邀请好友奖励，3：好友消费，4：好友完成车队奖励，5：好友成功邀请新人
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取任务批次日期
     *
     * @return batch_date - 任务批次日期
     */
    public Date getBatchDate() {
        return batchDate;
    }

    /**
     * 设置任务批次日期
     *
     * @param batchDate 任务批次日期
     */
    public void setBatchDate(Date batchDate) {
        this.batchDate = batchDate;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
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