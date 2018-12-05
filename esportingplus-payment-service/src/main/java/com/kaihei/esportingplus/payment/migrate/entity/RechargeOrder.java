package com.kaihei.esportingplus.payment.migrate.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author: tangtao
 **/
@Entity
@Table(name = "payment_rechargeorder")
public class RechargeOrder {

    /**
     * CREATE TABLE `payment_rechargeorder` ( `id` int(11) NOT NULL AUTO_INCREMENT, `oid` varchar(32) NOT NULL, `total_fee` int(10) unsigned NOT NULL,
     * `status` smallint(5) unsigned NOT NULL, `status_time` datetime(6) NOT NULL, `create_time` datetime(6) NOT NULL, `user_id` int(11) NOT NULL,
     * `recharge_info` longtext NOT NULL, `recharge_type` smallint(5) unsigned NOT NULL, `pay_type` smallint(5) unsigned NOT NULL, `inside_trade_no`
     * varchar(32) NOT NULL, PRIMARY KEY (`id`), UNIQUE KEY `oid` (`oid`), KEY `payment_rechargeorder_user_id_baff5ff7_fk_members_user_id`
     * (`user_id`), KEY `payment_rechargeorder_9acb4454` (`status`), KEY `payment_rechargeorder_recharge_type_33ac81fb` (`recharge_type`), CONSTRAINT
     * `payment_rechargeorder_user_id_baff5ff7_fk_members_user_id` FOREIGN KEY (`user_id`) REFERENCES `members_user` (`id`) ) ENGINE=InnoDB
     * AUTO_INCREMENT=867670 DEFAULT CHARSET=utf8mb4;
     */

    @Id
    private Integer id;
    private String oid;
    private Integer totalFee;
    private Integer status;
    private Date statusTime;
    private Date createTime;
    private Integer userId;
    private String rechargeInfo;
    private Integer rechargeType;
    private Integer payType;
    private String insideTradeNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Date statusTime) {
        this.statusTime = statusTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRechargeInfo() {
        return rechargeInfo;
    }

    public void setRechargeInfo(String rechargeInfo) {
        this.rechargeInfo = rechargeInfo;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getInsideTradeNo() {
        return insideTradeNo;
    }

    public void setInsideTradeNo(String insideTradeNo) {
        this.insideTradeNo = insideTradeNo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
