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
@Table(name = "payment_accountstatement")
public class OrderBill {

    /**
     * CREATE TABLE `payment_accountstatement` ( `id` int(11) NOT NULL AUTO_INCREMENT, `bill_type` smallint(5) unsigned NOT NULL, `total_fee` int(10)
     * unsigned NOT NULL, `order_id` varchar(64) NOT NULL, `order_type` varchar(32) NOT NULL, `create_time` datetime(6) NOT NULL, `user_id` int(11)
     * NOT NULL, `receipt_time` datetime(6) DEFAULT NULL, PRIMARY KEY (`id`), KEY `payment_accountstatement_user_id_ab8191bf_fk_members_user_id`
     * (`user_id`), CONSTRAINT `payment_accountstatement_user_id_ab8191bf_fk_members_user_id` FOREIGN KEY (`user_id`) REFERENCES `members_user` (`id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=9939435 DEFAULT CHARSET=utf8mb4;
     */

    @Id
    private Integer id;
    private Integer billType;
    private Integer totalFee;
    private String orderId;
    private String orderType;
    private Date createTime;
    private Integer userId;
    private Date receiptTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
