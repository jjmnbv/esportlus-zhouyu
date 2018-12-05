package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kaihei.esportingplus.payment.api.enums.AccountStateType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 暴击值余额表
 *
 * @author xusisi
 * @create 2018-08-17 10:45
 **/
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name="uk_user_id", columnNames = "userId") },
        indexes = {
        @Index(name = "idx_user_id", columnList = "userId")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarlightBalance extends AbstractEntity {

    private static final long serialVersionUID = 6566986961612100711L;

    /***
     * 用户ID
     */
    @Column(nullable = false,length = 64 ,columnDefinition = "varchar(128) COMMENT '用户ID'")
    private String userId;

    /***
     * 暴击值数量
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '暴击值数量'")
    private BigDecimal balance;

    /***
     * 状态码
     * AVAILABLE表示可用
     * FROZEN表示冻结
     * UNAVAILABLE表示不可用
     */
    @Column(nullable = false, length = 16 ,columnDefinition = "varchar(16) COMMENT '状态码:AVAILABLE表示可用、FROZEN表示冻结、UNAVAILABLE表示不可用'")
    private String state;

    /**
     * 可使用的暴击值数量
     */
    @Column(name = "usable_amount", nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '可使用的暴击值数量'")
    private BigDecimal usableAmount;

    /**
     * 冻结的暴击值数量
     */
    @Column(name = "frozen_amount", nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '冻结的暴击值数量' ")
    private BigDecimal frozenAmount;

    public StarlightBalance() {
        this.state = AccountStateType.AVAILABLE.getCode();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getUsableAmount() {
        return usableAmount;
    }

    public void setUsableAmount(BigDecimal usableAmount) {
        this.usableAmount = usableAmount;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
