package com.kaihei.esportingplus.payment.domain.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 结算收益订单表
 *
 * @author zhouyu
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_income_ordernum", columnNames = "incomeOrdernum")},
        indexes = {
                @Index(name = "idx_income_ordernum", columnList = "incomeOrdernum"),
                @Index(name = "idx_user_id", columnList = "userId"),
        })
public class OrderIncome extends AbstractEntity {

    @Column(columnDefinition = "varchar(128) COMMENT '订单id'")
    private String incomeOrdernum;

    @Column(columnDefinition = "int COMMENT '订单类型(1:游戏订单类型,2:暴鸡认证订单类型,3:充值订单类型,\n"
            + "4:奖励订单类型,5:违规扣款类型,6:福利金类型,\n"
            + "7:抽奖奖金,8:车队订单,9:悬赏订单,\n"
            + "10:系统充值订单类型,11:提现订单类型,12:系统扣款订单类型,\n"
            + "13:工作室提现订单类型,14:每周好运礼包订单类型,\n"
            + "16:技能订单类型,18:RPG保证金认证订单,17:DNF小程序订单,\n"
            + "19:暴鸡币充值订单类型,20:暴鸡值兑换订单类型)'")
    private int orderType;

    @Column(columnDefinition = "varchar(128) COMMENT '用户id'", nullable = false)
    private String userId;

    @Column(columnDefinition = "int COMMENT '总金额（单位分）'", nullable = false)
    private int totalAmount;

    @Column(columnDefinition = "Decimal(4,2) default '00.00' COMMENT '抽成比例 暴鸡：平台'", nullable = false)
    private BigDecimal ratio;

    @Column(columnDefinition = "int COMMENT '平台抽成'", nullable = false)
    private int platformIncome;

    @Column(columnDefinition = "int COMMENT '暴鸡、暴娘抽成金额（单位分）'", nullable = false)
    private int baojiIncome;

    @Column(columnDefinition = "varchar(128) COMMENT '附加信息'", nullable = false)
    private String attach;

    @Column(columnDefinition = "varchar(128) COMMENT '流水号'", nullable = false)
    private String flowNo;

    /**
     * 账户余额(元）
     */
    @Transient
    private String balance;

    public String getIncomeOrdernum() {
        return incomeOrdernum;
    }

    public void setIncomeOrdernum(String incomeOrdernum) {
        this.incomeOrdernum = incomeOrdernum;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public int getPlatformIncome() {
        return platformIncome;
    }

    public void setPlatformIncome(int platformIncome) {
        this.platformIncome = platformIncome;
    }

    public int getBaojiIncome() {
        return baojiIncome;
    }

    public void setBaojiIncome(int baojiIncome) {
        this.baojiIncome = baojiIncome;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
