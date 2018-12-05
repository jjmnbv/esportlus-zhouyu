package com.kaihei.esportingplus.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 暴鸡币余额表
 *
 * @author haycco
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name="uk_user_id", columnNames = "userId") },
        indexes = {
        @Index(name = "idx_user_id", columnList = "userId")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class GCoinBalance extends AbstractEntity {

    private static final long serialVersionUID = -4914840009169895307L;

    /***
     * 用户ID
     */
    @Column(nullable = false, length = 64, columnDefinition = "varchar(128) COMMENT '用户ID'")
    private String userId;

    /***
     * 暴鸡币总余额
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '暴鸡币总余额'")
    private BigDecimal gcoinBalance;

    /***
     * 余额账户状态：
     * AVAILABLE 表示可用
     * FROZEN 表示冻结
     * UNAVAILABLE 表示不可用
     */
    @Column(length = 32, columnDefinition = "varchar(32) COMMENT '余额账户状态：AVAILABLE表示可用、FROZEN表示冻结、UNAVAILABLE表示不可用'")
    private String state;

    /***
     * 可使用的暴鸡币数量
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '可使用的暴鸡币数量'")
    private BigDecimal usableAmount;

    /***
     * 冻结的暴鸡币数量
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '冻结的暴鸡币数量'")
    private BigDecimal frozenAmount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getGcoinBalance() {
        return gcoinBalance;
    }

    public void setGcoinBalance(BigDecimal gcoinBalance) {
        this.gcoinBalance = gcoinBalance;
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
