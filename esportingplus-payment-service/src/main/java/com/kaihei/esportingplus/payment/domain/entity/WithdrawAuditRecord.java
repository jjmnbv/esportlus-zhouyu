package com.kaihei.esportingplus.payment.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * 提现审批记录表
 *
 * @author chenzhenjun
 */
@Entity
public class WithdrawAuditRecord extends AbstractEntity {

    //订单号
    @Column(nullable = false, columnDefinition = "varchar(50) COMMENT '订单id'")
    private String orderId;

    //用户id
    @Column(nullable = false, columnDefinition = "varchar(10) COMMENT '用户id'")
    private String uid;

    //提现总金额
    @Column(nullable = false, length = 10, columnDefinition = "int(10) COMMENT '提现总金额(单位:分)'")
    private Integer totalFee;

    //提现渠道
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '提现渠道'")
    private String channel;

    //审批状态
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '审批状态'")
    private String verifyState;

    //截停状态
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) COMMENT '截停状态'")
    private String blockState;

    //备注
    @Column(columnDefinition = "varchar(64) DEFAULT NULL COMMENT '备注'")
    private String remark;

    //来源应用ID
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '来源应用ID'")
    private String sourceAppId;

    // 客户端ip
    @Column(columnDefinition = "varchar(16) DEFAULT NULL COMMENT '客户端ip'")
    private String clientIp;

    //提现完成时间
    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '提现完成时间'")
    private Date finishDate;

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getTotalFee() {
        return this.totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVerifyState() {
        return this.verifyState;
    }

    public void setVerifyState(String verifyState) {
        this.verifyState = verifyState;
    }

    public String getBlockState() {
        return this.blockState;
    }

    public void setBlockState(String blockState) {
        this.blockState = blockState;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSourceAppId() {
        return this.sourceAppId;
    }

    public void setSourceAppId(String sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    public Date getFinishDate() {
        return this.finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
