package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 暴鸡币打赏订单表
 *
 * @author xiaolijun
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_order_id", columnNames = "orderId")},
        indexes = {
                @Index(name = "idx_user_id_and_order_id", columnList = "orderId,userId ")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GCoinRewardOrder extends AbstractEntity {

    private static final long serialVersionUID = -5247875356468854767L;

    /***
     * 充值订单ID
     */
    @Column(length = 128, columnDefinition = "varchar(128) COMMENT '充值订单ID'")
    private String orderId;

    /***
     * 打赏支出用户ID
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '打赏支出用户ID'")
    private String userId;

    /***
     * 被打赏收入用户ID
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) COMMENT '被打赏收入用户ID'")
    private String receivedUserId;

    /***
     * 交易状态：
     * 001（交易创建，待支付）
     * 002（已支付成功）
     * 003（已退款）
     */
    @Column(length = 16, columnDefinition = "varchar(16) COMMENT '交易状态：001（交易创建，待支付）、002（已支付成功）、003（已退款）'")
    private String state;

    /***
     * 订单金额（单位为元，两位小数）
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '订单金额（单位为元，两位小数）'")
    private BigDecimal gcoinAmount;

    /***
     * 兑换暴击值数量
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '兑换暴击值数量'")
    private BigDecimal starlightAmount;

    /***
     * 操作终端
     *
     * PC PC端
     * ANDROID android
     * IOS ios
     * MP 小程序
     * H5 H5页面
     * PA 微信公众号
     * PLATFORM 暴鸡平台系统
     */
    @Column(nullable = false, length = 16, columnDefinition = "varchar(16) COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)'")
    private String sourceId;

    /***
     * 主题
     */
    @Column(columnDefinition = "varchar(256) default '' COMMENT '主题'")
    private String subject;

    /***
     * 内容
     */
    @Column(length = 1024, columnDefinition = "varchar(1024) default '' COMMENT '内容'")
    private String body;

    /***
     * 描述
     */
    @Column(columnDefinition = "longtext COMMENT '描述'")
    private String description;

    /**
     * 账户余额(元）
     */
    @Transient
    private String balance;

    /**
     * 暴击值账户余额(元）
     */
    @Transient
    private String startLightBalance;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceivedUserId() {
        return receivedUserId;
    }

    public void setReceivedUserId(String receivedUserId) {
        this.receivedUserId = receivedUserId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public BigDecimal getStarlightAmount() {
        return starlightAmount;
    }

    public void setStarlightAmount(BigDecimal starlightAmount) {
        this.starlightAmount = starlightAmount;
    }

    public BigDecimal getGcoinAmount() {
        return gcoinAmount;
    }

    public void setGcoinAmount(BigDecimal gcoinAmount) {
        this.gcoinAmount = gcoinAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getStartLightBalance() {
        return startLightBalance;
    }

    public void setStartLightBalance(String startLightBalance) {
        this.startLightBalance = startLightBalance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
