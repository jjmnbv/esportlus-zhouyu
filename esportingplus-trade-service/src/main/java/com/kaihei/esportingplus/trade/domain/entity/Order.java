package com.kaihei.esportingplus.trade.domain.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Table(name = "trade_order")
public class Order {
    /**
     * 订单ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 第三方订单号
     */
    @Column(name = "outer_trade_no")
    private String outerTradeNo;

    /**
     * 订单用户UID
     */
    private String uid;

    /**
     * 优惠券ID，用户使用的优惠券ID，与coupons表里的ID关联
     */
    @Column(name = "coupon_id")
    private Long couponId;

    /**
     * 订单序列号
     */
    private String sequeue;

    /**
     * 订单业务类型，0: 车队订单, 1: 滴滴订单, 2: 悬赏（约玩）订单, 3:自定义技能订单（陪玩）, 4: 专属订单
     */
    @Column(name = "business_type")
    private Byte businessType;

    /**
     * 订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    private Byte status;

    /**
     * 用户预付金额，用户完成支付时产生的金额，单位为分
     */
    @Column(name = "prepaid_amount")
    private Integer prepaidAmount;

    /**
     * 用户实付金额，用户实际支付的金额（剔除退款和优惠的金额），单位为分
     */
    @Column(name = "actual_paid_amount")
    private Integer actualPaidAmount;

    /**
     * 预退款金额
     */
    @Column(name = "pre_refund_amount")
    private Integer preRefundAmount;

    /**
     * 实际退款金额
     */
    @Column(name = "actual_refund_amount")
    private Integer actualRefundAmount;

    /**
     * 优惠金额，用户支付后使用优惠券或奖励金产生的折扣金额，单位为分，若无，记为0
     */
    @Column(name = "discount_amount")
    private Integer discountAmount;

    /**
     * 订单接收时间，订单被响应时间，滴滴订单中为暴鸡接单时间，车队订单中为老板支付时间，悬赏（约玩）订单为第一个人报名时间，自定义技能订单（陪玩）为用户支付时间，专属订单为服务方接单时间
     */
    @Column(name = "response_time")
    private Date responseTime;

    /**
     * 支付类型, 0: 微信 1: 支付宝 2:QQ钱包 3: 我的钱包
     */
    @Column(name = "payment_type")
    private Byte paymentType;

    /**
     * (老板)支付成功时间
     */
    @Column(name = "payment_time")
    private Date paymentTime;

    /**
     * 订单结束时间
     */
    @Column(name = "close_time")
    private Date closeTime;

    /**
     * 订单取消时间
     */
    @Column(name = "cancel_time")
    private Date cancelTime;

    /**
     * 订单创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 订单修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    private OrderItemTeamRPG orderItemTeamRPG;
    private OrderItemTeamPVP orderItemTeamPVP;
    private OrderItemTeamPVPFree orderItemTeamPVPFree;
    private List<OrderRefundRecord> orderRefundRecords;

    public Order(Long id, String outerTradeNo, String uid, Long couponId, String sequeue, Byte businessType, Byte status, Integer prepaidAmount, Integer actualPaidAmount, Integer preRefundAmount, Integer actualRefundAmount, Integer discountAmount, Date responseTime, Byte paymentType, Date paymentTime, Date closeTime, Date cancelTime, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.outerTradeNo = outerTradeNo;
        this.uid = uid;
        this.couponId = couponId;
        this.sequeue = sequeue;
        this.businessType = businessType;
        this.status = status;
        this.prepaidAmount = prepaidAmount;
        this.actualPaidAmount = actualPaidAmount;
        this.preRefundAmount = preRefundAmount;
        this.actualRefundAmount = actualRefundAmount;
        this.discountAmount = discountAmount;
        this.responseTime = responseTime;
        this.paymentType = paymentType;
        this.paymentTime = paymentTime;
        this.closeTime = closeTime;
        this.cancelTime = cancelTime;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public Order() {
        super();
    }

    /**
     * 获取订单ID
     *
     * @return id - 订单ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置订单ID
     *
     * @param id 订单ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取第三方订单号
     *
     * @return outer_trade_no - 第三方订单号
     */
    public String getOuterTradeNo() {
        return outerTradeNo;
    }

    /**
     * 设置第三方订单号
     *
     * @param outerTradeNo 第三方订单号
     */
    public void setOuterTradeNo(String outerTradeNo) {
        this.outerTradeNo = outerTradeNo == null ? null : outerTradeNo.trim();
    }

    /**
     * 获取订单用户UID
     *
     * @return uid - 订单用户UID
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置订单用户UID
     *
     * @param uid 订单用户UID
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取优惠券ID，用户使用的优惠券ID，与coupons表里的ID关联
     *
     * @return coupon_id - 优惠券ID，用户使用的优惠券ID，与coupons表里的ID关联
     */
    public Long getCouponId() {
        return couponId;
    }

    /**
     * 设置优惠券ID，用户使用的优惠券ID，与coupons表里的ID关联
     *
     * @param couponId 优惠券ID，用户使用的优惠券ID，与coupons表里的ID关联
     */
    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    /**
     * 获取订单序列号
     *
     * @return sequeue - 订单序列号
     */
    public String getSequeue() {
        return sequeue;
    }

    /**
     * 设置订单序列号
     *
     * @param sequeue 订单序列号
     */
    public void setSequeue(String sequeue) {
        this.sequeue = sequeue;
    }

    /**
     * 获取订单业务类型，0: 车队订单, 1: 滴滴订单, 2: 悬赏（约玩）订单, 3:自定义技能订单（陪玩）, 4: 专属订单
     *
     * @return business_type - 订单业务类型，0: 车队订单, 1: 滴滴订单, 2: 悬赏（约玩）订单, 3:自定义技能订单（陪玩）, 4: 专属订单
     */
    public Byte getBusinessType() {
        return businessType;
    }

    /**
     * 设置订单业务类型，0: 车队订单, 1: 滴滴订单, 2: 悬赏（约玩）订单, 3:自定义技能订单（陪玩）, 4: 专属订单
     *
     * @param businessType 订单业务类型，0: 车队订单, 1: 滴滴订单, 2: 悬赏（约玩）订单, 3:自定义技能订单（陪玩）, 4: 专属订单
     */
    public void setBusinessType(Byte businessType) {
        this.businessType = businessType;
    }

    /**
     * 获取订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     *
     * @return status - 订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     *
     * @param status 订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取用户预付金额，用户完成支付时产生的金额，单位为分
     *
     * @return prepaid_amount - 用户预付金额，用户完成支付时产生的金额，单位为分
     */
    public Integer getPrepaidAmount() {
        return prepaidAmount;
    }

    /**
     * 设置用户预付金额，用户完成支付时产生的金额，单位为分
     *
     * @param prepaidAmount 用户预付金额，用户完成支付时产生的金额，单位为分
     */
    public void setPrepaidAmount(Integer prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    /**
     * 获取用户实付金额，用户实际支付的金额（剔除退款和优惠的金额），单位为分
     *
     * @return actual_paid_amount - 用户实付金额，用户实际支付的金额（剔除退款和优惠的金额），单位为分
     */
    public Integer getActualPaidAmount() {
        return actualPaidAmount;
    }

    /**
     * 设置用户实付金额，用户实际支付的金额（剔除退款和优惠的金额），单位为分
     *
     * @param actualPaidAmount 用户实付金额，用户实际支付的金额（剔除退款和优惠的金额），单位为分
     */
    public void setActualPaidAmount(Integer actualPaidAmount) {
        this.actualPaidAmount = actualPaidAmount;
    }

    /**
     * 获取预退款金额
     *
     * @return pre_refund_amount - 预退款金额
     */
    public Integer getPreRefundAmount() {
        return preRefundAmount;
    }

    /**
     * 设置预退款金额
     *
     * @param preRefundAmount 预退款金额
     */
    public void setPreRefundAmount(Integer preRefundAmount) {
        this.preRefundAmount = preRefundAmount;
    }

    /**
     * 获取实际退款金额
     *
     * @return actual_refund_amount - 实际退款金额
     */
    public Integer getActualRefundAmount() {
        return actualRefundAmount;
    }

    /**
     * 设置实际退款金额
     *
     * @param actualRefundAmount 实际退款金额
     */
    public void setActualRefundAmount(Integer actualRefundAmount) {
        this.actualRefundAmount = actualRefundAmount;
    }

    /**
     * 获取优惠金额，用户支付后使用优惠券或奖励金产生的折扣金额，单位为分，若无，记为0
     *
     * @return discount_amount - 优惠金额，用户支付后使用优惠券或奖励金产生的折扣金额，单位为分，若无，记为0
     */
    public Integer getDiscountAmount() {
        return discountAmount;
    }

    /**
     * 设置优惠金额，用户支付后使用优惠券或奖励金产生的折扣金额，单位为分，若无，记为0
     *
     * @param discountAmount 优惠金额，用户支付后使用优惠券或奖励金产生的折扣金额，单位为分，若无，记为0
     */
    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * 获取订单接收时间，订单被响应时间，滴滴订单中为暴鸡接单时间，车队订单中为老板支付时间，悬赏（约玩）订单为第一个人报名时间，自定义技能订单（陪玩）为用户支付时间，专属订单为服务方接单时间
     *
     * @return response_time - 订单接收时间，订单被响应时间，滴滴订单中为暴鸡接单时间，车队订单中为老板支付时间，悬赏（约玩）订单为第一个人报名时间，自定义技能订单（陪玩）为用户支付时间，专属订单为服务方接单时间
     */
    public Date getResponseTime() {
        return responseTime;
    }

    /**
     * 设置订单接收时间，订单被响应时间，滴滴订单中为暴鸡接单时间，车队订单中为老板支付时间，悬赏（约玩）订单为第一个人报名时间，自定义技能订单（陪玩）为用户支付时间，专属订单为服务方接单时间
     *
     * @param responseTime 订单接收时间，订单被响应时间，滴滴订单中为暴鸡接单时间，车队订单中为老板支付时间，悬赏（约玩）订单为第一个人报名时间，自定义技能订单（陪玩）为用户支付时间，专属订单为服务方接单时间
     */
    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * 获取支付类型, 0: 微信 1: 支付宝 2:QQ钱包 3: 我的钱包
     *
     * @return payment_type - 支付类型, 0: 微信 1: 支付宝 2:QQ钱包 3: 我的钱包
     */
    public Byte getPaymentType() {
        return paymentType;
    }

    /**
     * 设置支付类型, 0: 微信 1: 支付宝 2:QQ钱包 3: 我的钱包
     *
     * @param paymentType 支付类型, 0: 微信 1: 支付宝 2:QQ钱包 3: 我的钱包
     */
    public void setPaymentType(Byte paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * 获取(老板)支付成功时间
     *
     * @return payment_time - (老板)支付成功时间
     */
    public Date getPaymentTime() {
        return paymentTime;
    }

    /**
     * 设置(老板)支付成功时间
     *
     * @param paymentTime (老板)支付成功时间
     */
    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    /**
     * 获取订单结束时间
     *
     * @return close_time - 订单结束时间
     */
    public Date getCloseTime() {
        return closeTime;
    }

    /**
     * 设置订单结束时间
     *
     * @param closeTime 订单结束时间
     */
    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * 获取订单取消时间
     *
     * @return cancel_time - 订单取消时间
     */
    public Date getCancelTime() {
        return cancelTime;
    }

    /**
     * 设置订单取消时间
     *
     * @param cancelTime 订单取消时间
     */
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    /**
     * 获取订单创建时间
     *
     * @return gmt_create - 订单创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置订单创建时间
     *
     * @param gmtCreate 订单创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取订单修改时间
     *
     * @return gmt_modified - 订单修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置订单修改时间
     *
     * @param gmtModified 订单修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public OrderItemTeamRPG getOrderItemTeamRPG() {
        return orderItemTeamRPG;
    }

    public void setOrderItemTeamRPG(OrderItemTeamRPG orderItemTeamRPG) {
        this.orderItemTeamRPG = orderItemTeamRPG;
    }

    public OrderItemTeamPVP getOrderItemTeamPVP() {
        return orderItemTeamPVP;
    }

    public void setOrderItemTeamPVP(
            OrderItemTeamPVP orderItemTeamPVP) {
        this.orderItemTeamPVP = orderItemTeamPVP;
    }

    public OrderItemTeamPVPFree getOrderItemTeamPVPFree() {
        return orderItemTeamPVPFree;
    }

    public void setOrderItemTeamPVPFree(
            OrderItemTeamPVPFree orderItemTeamPVPFree) {
        this.orderItemTeamPVPFree = orderItemTeamPVPFree;
    }

    public List<OrderRefundRecord> getOrderRefundRecords() {
        return orderRefundRecords;
    }

    public void setOrderRefundRecords(
            List<OrderRefundRecord> orderRefundRecords) {
        this.orderRefundRecords = orderRefundRecords;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}