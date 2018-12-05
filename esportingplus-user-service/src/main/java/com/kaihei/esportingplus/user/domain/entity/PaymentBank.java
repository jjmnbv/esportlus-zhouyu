package com.kaihei.esportingplus.user.domain.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "payment_bank")
public class PaymentBank {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 余额
     */
    private Integer balance;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 用户
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 奖金
     */
    private Integer bonus;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取余额
     *
     * @return balance - 余额
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * 设置余额
     *
     * @param balance 余额
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取用户
     *
     * @return user_id - 用户
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户
     *
     * @param userId 用户
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取奖金
     *
     * @return bonus - 奖金
     */
    public Integer getBonus() {
        return bonus;
    }

    /**
     * 设置奖金
     *
     * @param bonus 奖金
     */
    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }
}