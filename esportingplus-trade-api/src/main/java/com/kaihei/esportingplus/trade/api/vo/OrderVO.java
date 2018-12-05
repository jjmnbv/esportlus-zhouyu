/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import java.util.Date;

public class OrderVO implements Serializable {

    private static final long serialVersionUID = -386196200064341249L;
    /**
     * 订单ID
     */
    private Long id;

    /**
     * 第三方订单号
     */
    private String outerTradeNo;

    /**
     * 订单用户UID
     */
    private String uid;

    /**
     * 优惠券ID，用户使用的优惠券ID，与coupons表里的ID关联
     */
    private Long couponId;

    /**
     * 订单序列号
     */
    private String sequeue;

    /**
     * 订单业务类型，0: 车队订单, 1: 滴滴订单, 2: 悬赏（约玩）订单, 3:自定义技能订单（陪玩）, 4: 专属订单
     */
    private Byte businessType;

    /**
     * 订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    private Integer status;

    /**
     * 用户预付金额，用户完成支付时产生的金额，单位为分
     */
    private Integer prepaidAmount;

    /**
     * 用户实付金额，用户实际支付的金额（剔除退款和优惠的金额），单位为分
     */
    private Integer actualPaidAmount;

    /**
     * 预退款金额
     */
    private Integer preRefundAmount;

    /**
     * 实际退款金额
     */
    private Integer actualRefundAmount;

    /**
     * 优惠金额，用户支付后使用优惠券或奖励金产生的折扣金额，单位为分，若无，记为0
     */
    private Integer discountAmount;

    /**
     * 订单接收时间，订单被响应时间，滴滴订单中为暴鸡接单时间，车队订单中为老板支付时间，悬赏（约玩）订单为第一个人报名时间，自定义技能订单（陪玩）为用户支付时间，专属订单为服务方接单时间
     */
    private Date responseTime;

    /**
     * 支付类型, 0: 微信 1: 支付宝 2:QQ钱包 3: 我的钱包
     */
    private Byte paymentType;

    /**
     * (老板)支付成功时间
     */
    private Date paymentTime;

    /**
     * 订单结束时间
     */
    private Date closeTime;

    /**
     * 订单取消时间
     */
    private Date cancelTime;

    /**
     * 订单创建时间
     */
    private Date gmtCreate;

    /**
     * 订单修改时间
     */
    private Date gmtModified;

    private OrderItemTeamVo orderItemTeam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOuterTradeNo() {
        return outerTradeNo;
    }

    public void setOuterTradeNo(String outerTradeNo) {
        this.outerTradeNo = outerTradeNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getSequeue() {
        return sequeue;
    }

    public void setSequeue(String sequeue) {
        this.sequeue = sequeue;
    }

    public Byte getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Byte businessType) {
        this.businessType = businessType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPrepaidAmount() {
        return prepaidAmount;
    }

    public void setPrepaidAmount(Integer prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    public Integer getActualPaidAmount() {
        return actualPaidAmount;
    }

    public void setActualPaidAmount(Integer actualPaidAmount) {
        this.actualPaidAmount = actualPaidAmount;
    }

    public Integer getPreRefundAmount() {
        return preRefundAmount;
    }

    public void setPreRefundAmount(Integer preRefundAmount) {
        this.preRefundAmount = preRefundAmount;
    }

    public Integer getActualRefundAmount() {
        return actualRefundAmount;
    }

    public void setActualRefundAmount(Integer actualRefundAmount) {
        this.actualRefundAmount = actualRefundAmount;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public Byte getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Byte paymentType) {
        this.paymentType = paymentType;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public OrderItemTeamVo getOrderItemTeam() {
        return orderItemTeam;
    }

    public void setOrderItemTeam(OrderItemTeamVo orderItemTeam) {
        this.orderItemTeam = orderItemTeam;
    }
}
