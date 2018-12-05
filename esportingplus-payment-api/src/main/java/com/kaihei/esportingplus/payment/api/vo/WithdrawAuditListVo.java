package com.kaihei.esportingplus.payment.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提现审核列表-APP|后台通用Vo类
 *
 * @author chenzhenjun
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WithdrawAuditListVo implements Serializable {

    private static final long serialVersionUID = -5039840164846491335L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 提现总金额(单位:分)
     */
    private Integer totalFee;

    /**
     * 审批状态
     */
    private String verifyState;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提现订单号
     */
    private String orderId;

    /**
     * 提现渠道
     */
    private String channel;

    /**
     * 截停状态
     */
    private String blockState;

    /**
     * 创建日期
     */
    private String createDate;

    /**
     * 更新时间
     */
    private String lastModifiedDate;

    /**
     * 支付完成时间
     */
    private String finishDate;

    /**
     * 提现总金额(单位:元)
     */
    private BigDecimal totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(String verifyState) {
        this.verifyState = verifyState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBlockState() {
        return blockState;
    }

    public void setBlockState(String blockState) {
        this.blockState = blockState;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
