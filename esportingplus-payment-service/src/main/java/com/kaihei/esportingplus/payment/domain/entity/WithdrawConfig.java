package com.kaihei.esportingplus.payment.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 提现配置表
 * @author chenzhenjun
 */
@Entity
public class WithdrawConfig extends AbstractEntity {

    /**
     * 提现开关
     */
    @Column(columnDefinition = "varchar(8) default 'ENABLE' comment '状态: ENABLE-开启、DISABLE-关闭'")
    private String state;

    /**
     * 每日提现次数
     */
    @Column(nullable = false, length = 4, columnDefinition = "int(4) COMMENT '每日提现次数'")
    private Integer withdrawLimit;

    /**
     * 单次提现最小金额(单位:分)
     */
    @Column(nullable = false, length = 10, columnDefinition = "int(10) COMMENT '单次提现最小金额(单位:分)'")
    private Integer withdrawMin;

    /**
     * 单次提现最大额度(单位:分)
     */
    @Column(nullable = false, length = 10, columnDefinition = "int(10) COMMENT '单次提现最大额度(单位:分)'")
    private Integer withdrawMax;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getWithdrawLimit() {
        return withdrawLimit;
    }

    public void setWithdrawLimit(Integer withdrawLimit) {
        this.withdrawLimit = withdrawLimit;
    }

    public Integer getWithdrawMin() {
        return withdrawMin;
    }

    public void setWithdrawMin(Integer withdrawMin) {
        this.withdrawMin = withdrawMin;
    }

    public Integer getWithdrawMax() {
        return withdrawMax;
    }

    public void setWithdrawMax(Integer withdrawMax) {
        this.withdrawMax = withdrawMax;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
