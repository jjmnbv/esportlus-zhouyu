package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 提现配置Vo类
 *
 * @author chenzhenjun
 */
public class WithdrawConfigVo implements Serializable {

    private static final long serialVersionUID = 8558265903333119287L;
    private Long id;

    /**
     * 提现开关状态
     */
    private String state;

    /**
     * 每日提现次数
     */
    private Integer withdrawLimit;

    /**
     * 单次提现最小金额(单位:分)
     */
    private Integer withdrawMin;

    /**
     * 单次提现最大额度(单位:分)
     */
    private Integer withdrawMax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
