package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "members_ordercount")
public class MembersOrderCount {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户uid
     */
    private String uid;

    /**
     * 订单类型 0：所有；1：免费车队；2：付费车队；3：滴滴单
     */
    @Column(name = "order_type")
    private Byte orderType;

    /**
     * 总接单数
     */
    @Column(name = "total_accept_count")
    private Integer totalAcceptCount;

    /**
     * 今日接单数
     */
    @Column(name = "today_accept_count")
    private Byte todayAcceptCount;

    /**
     * 总下单数
     */
    @Column(name = "total_place_count")
    private Integer totalPlaceCount;

    /**
     * 今日下单数
     */
    @Column(name = "today_place_count")
    private Byte todayPlaceCount;

    /**
     * 最后接单时间
     */
    @Column(name = "last_accept_datetime")
    private Date lastAcceptDatetime;

    /**
     * 最后下单时间
     */
    @Column(name = "last_place_datetime")
    private Date lastPlaceDatetime;

    public MembersOrderCount(Integer id, String uid, Byte orderType, Integer totalAcceptCount, Byte todayAcceptCount, Integer totalPlaceCount, Byte todayPlaceCount, Date lastAcceptDatetime, Date lastPlaceDatetime) {
        this.id = id;
        this.uid = uid;
        this.orderType = orderType;
        this.totalAcceptCount = totalAcceptCount;
        this.todayAcceptCount = todayAcceptCount;
        this.totalPlaceCount = totalPlaceCount;
        this.todayPlaceCount = todayPlaceCount;
        this.lastAcceptDatetime = lastAcceptDatetime;
        this.lastPlaceDatetime = lastPlaceDatetime;
    }

    public MembersOrderCount() {
        super();
    }

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户uid
     *
     * @return uid - 用户uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置用户uid
     *
     * @param uid 用户uid
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取订单类型 0：所有；1：免费车队；2：付费车队；3：滴滴单
     *
     * @return order_type - 订单类型 0：所有；1：免费车队；2：付费车队；3：滴滴单
     */
    public Byte getOrderType() {
        return orderType;
    }

    /**
     * 设置订单类型 0：所有；1：免费车队；2：付费车队；3：滴滴单
     *
     * @param orderType 订单类型 0：所有；1：免费车队；2：付费车队；3：滴滴单
     */
    public void setOrderType(Byte orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取总接单数
     *
     * @return total_accept_count - 总接单数
     */
    public Integer getTotalAcceptCount() {
        return totalAcceptCount;
    }

    /**
     * 设置总接单数
     *
     * @param totalAcceptCount 总接单数
     */
    public void setTotalAcceptCount(Integer totalAcceptCount) {
        this.totalAcceptCount = totalAcceptCount;
    }

    /**
     * 获取今日接单数
     *
     * @return today_accept_count - 今日接单数
     */
    public Byte getTodayAcceptCount() {
        return todayAcceptCount;
    }

    /**
     * 设置今日接单数
     *
     * @param todayAcceptCount 今日接单数
     */
    public void setTodayAcceptCount(Byte todayAcceptCount) {
        this.todayAcceptCount = todayAcceptCount;
    }

    /**
     * 获取总下单数
     *
     * @return total_place_count - 总下单数
     */
    public Integer getTotalPlaceCount() {
        return totalPlaceCount;
    }

    /**
     * 设置总下单数
     *
     * @param totalPlaceCount 总下单数
     */
    public void setTotalPlaceCount(Integer totalPlaceCount) {
        this.totalPlaceCount = totalPlaceCount;
    }

    /**
     * 获取今日下单数
     *
     * @return today_place_count - 今日下单数
     */
    public Byte getTodayPlaceCount() {
        return todayPlaceCount;
    }

    /**
     * 设置今日下单数
     *
     * @param todayPlaceCount 今日下单数
     */
    public void setTodayPlaceCount(Byte todayPlaceCount) {
        this.todayPlaceCount = todayPlaceCount;
    }

    /**
     * 获取最后接单时间
     *
     * @return last_accept_datetime - 最后接单时间
     */
    public Date getLastAcceptDatetime() {
        return lastAcceptDatetime;
    }

    /**
     * 设置最后接单时间
     *
     * @param lastAcceptDatetime 最后接单时间
     */
    public void setLastAcceptDatetime(Date lastAcceptDatetime) {
        this.lastAcceptDatetime = lastAcceptDatetime;
    }

    /**
     * 获取最后下单时间
     *
     * @return last_place_datetime - 最后下单时间
     */
    public Date getLastPlaceDatetime() {
        return lastPlaceDatetime;
    }

    /**
     * 设置最后下单时间
     *
     * @param lastPlaceDatetime 最后下单时间
     */
    public void setLastPlaceDatetime(Date lastPlaceDatetime) {
        this.lastPlaceDatetime = lastPlaceDatetime;
    }
}