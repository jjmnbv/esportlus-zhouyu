package com.kaihei.esportingplus.trade.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "trade_order_refund_record")
public class OrderRefundRecord {
    /**
     * 退款记录主键ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 关联的订单号主键ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 关联的订单号分布式ID
     */
    @Column(name = "order_sequence")
    private String orderSequence;

    /**
     * 退款单号-分布式ID
     */
    @Column(name = "refund_sequence")
    private String refundSequence;

    /**
     * 此次退款金额，有可能是部分退款的金额
     */
    @Column(name = "refund_fee")
    private Integer refundFee;

    /**
     * 支付系统所需的appid参数
     */
    private String appid;

    /**
     * 支付系统需要的渠道标签
     */
    @Column(name = "channel_tag")
    private String channelTag;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款记录创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 退款记录修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    public OrderRefundRecord(Long id, Long orderId, String orderSequence, String refundSequence, Integer refundFee, String appid, String channelTag, String reason, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.orderId = orderId;
        this.orderSequence = orderSequence;
        this.refundSequence = refundSequence;
        this.refundFee = refundFee;
        this.appid = appid;
        this.channelTag = channelTag;
        this.reason = reason;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public OrderRefundRecord() {
        super();
    }

    /**
     * 获取退款记录主键ID
     *
     * @return id - 退款记录主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置退款记录主键ID
     *
     * @param id 退款记录主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取关联的订单号主键ID
     *
     * @return order_id - 关联的订单号主键ID
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置关联的订单号主键ID
     *
     * @param orderId 关联的订单号主键ID
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取关联的订单号分布式ID
     *
     * @return order_sequence - 关联的订单号分布式ID
     */
    public String getOrderSequence() {
        return orderSequence;
    }

    /**
     * 设置关联的订单号分布式ID
     *
     * @param orderSequence 关联的订单号分布式ID
     */
    public void setOrderSequence(String orderSequence) {
        this.orderSequence = orderSequence == null ? null : orderSequence.trim();
    }

    /**
     * 获取退款单号-分布式ID
     *
     * @return record_sequence - 退款单号-分布式ID
     */
    public String getRefundSequence() {
        return refundSequence;
    }

    /**
     * 设置退款单号-分布式ID
     *
     * @param refundSequence 退款单号-分布式ID
     */
    public void setRefundSequence(String refundSequence) {
        this.refundSequence = refundSequence == null ? null : refundSequence.trim();
    }

    /**
     * 获取此次退款金额，有可能是部分退款的金额
     *
     * @return refund_fee - 此次退款金额，有可能是部分退款的金额
     */
    public Integer getRefundFee() {
        return refundFee;
    }

    /**
     * 设置此次退款金额，有可能是部分退款的金额
     *
     * @param refundFee 此次退款金额，有可能是部分退款的金额
     */
    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    /**
     * 获取支付系统所需的appid参数
     *
     * @return appid - 支付系统所需的appid参数
     */
    public String getAppid() {
        return appid;
    }

    /**
     * 设置支付系统所需的appid参数
     *
     * @param appid 支付系统所需的appid参数
     */
    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    /**
     * 获取支付系统需要的渠道标签
     *
     * @return channelTag - 支付系统需要的渠道标签
     */
    public String getChannelTag() {
        return channelTag;
    }

    /**
     * 设置支付系统需要的渠道标签
     *
     * @param channelTag 支付系统需要的渠道标签
     */
    public void setChannelTag(String channelTag) {
        this.channelTag = channelTag == null ? null : channelTag.trim();
    }

    /**
     * 获取退款原因
     *
     * @return reason - 退款原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置退款原因
     *
     * @param reason 退款原因
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    /**
     * 获取退款记录创建时间
     *
     * @return gmt_create - 退款记录创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置退款记录创建时间
     *
     * @param gmtCreate 退款记录创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取退款记录修改时间
     *
     * @return gmt_modified - 退款记录修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置退款记录修改时间
     *
     * @param gmtModified 退款记录修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}