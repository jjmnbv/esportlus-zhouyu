package com.kaihei.esportingplus.marketing.domian.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "market_user_task_award_order")
public class MarketUserTaskAwardOrder {
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
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 奖励数量
     */
    @Column(name = "award_num")
    private Integer awardNum;

    /**
     * 奖励类型：1-免单，2-暴击币奖励
     */
    @Column(name = "award_type")
    private Integer awardType;

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

    public MarketUserTaskAwardOrder(Long id, Integer userId, String orderNo, Integer awardNum, Integer awardType, Date updateTime, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.orderNo = orderNo;
        this.awardNum = awardNum;
        this.awardType = awardType;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public MarketUserTaskAwardOrder() {
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
     * 获取订单号
     *
     * @return order_no - 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单号
     *
     * @param orderNo 订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * 获取奖励数量
     *
     * @return award_num - 奖励数量
     */
    public Integer getAwardNum() {
        return awardNum;
    }

    /**
     * 设置奖励数量
     *
     * @param awardNum 奖励数量
     */
    public void setAwardNum(Integer awardNum) {
        this.awardNum = awardNum;
    }

    /**
     * 获取奖励类型：1-免单，2-暴击币奖励
     *
     * @return award_type - 奖励类型：1-免单，2-暴击币奖励
     */
    public Integer getAwardType() {
        return awardType;
    }

    /**
     * 设置奖励类型：1-免单，2-暴击币奖励
     *
     * @param awardType 奖励类型：1-免单，2-暴击币奖励
     */
    public void setAwardType(Integer awardType) {
        this.awardType = awardType;
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