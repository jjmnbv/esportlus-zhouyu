package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @program: esportingplus
 * @description: 账户信息
 * @author: xusisi
 * @create: 2018-09-30 16:11
 **/
public class AccountInfoVo {

    /***
     * 账号类型：暴鸡币账号，暴击值账号
     */
    private String accountType;

    /***
     * 用户ID
     */
    private String userId;

    /***
     * 总金额
     */
    private Integer totalAmount;

    /***
     * 冻结金额
     */
    private Integer frozenAmount;

    /***
     * 可用金额
     */
    private Integer usableAmount;

    /***
     * 账号状态
     */
    private String state;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Integer frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Integer getUsableAmount() {
        return usableAmount;
    }

    public void setUsableAmount(Integer usableAmount) {
        this.usableAmount = usableAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
