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
@Table(name = "payment_transfer")
public class TransferOrder {

    /**
     * CREATE TABLE `payment_transfer` ( `id` int(11) NOT NULL AUTO_INCREMENT, `fee` int(10) unsigned NOT NULL, `transfer_type` smallint(5) unsigned
     * NOT NULL, `verify_status` smallint(5) unsigned NOT NULL, `remark` varchar(64) NOT NULL, `update_time` datetime(6) NOT NULL, `create_time`
     * datetime(6) NOT NULL, `user_id` int(11) NOT NULL, `do_transfer_ret` longtext NOT NULL, `do_transfer_status` varchar(32) NOT NULL,
     * `out_trade_no` varchar(32) NOT NULL, `query_ret` longtext NOT NULL, `query_status` varchar(32) NOT NULL, `package_name` varchar(20) NOT NULL,
     * `payment_time` datetime(6) DEFAULT NULL, `block_status` smallint(5) unsigned NOT NULL, PRIMARY KEY (`id`), KEY
     * `payment_transfer_user_id_2a384916_fk_members_user_id` (`user_id`), CONSTRAINT `payment_transfer_user_id_2a384916_fk_members_user_id` FOREIGN
     * KEY (`user_id`) REFERENCES `members_user` (`id`) ) ENGINE=InnoDB AUTO_INCREMENT=292878 DEFAULT CHARSET=utf8mb4;
     */

    @Id
    private Integer id;
    private Integer fee;
    private Integer transferType;
    private Integer verifyStatus;

    private Date updateTime;
    private Date createTime;
    private Date paymentTime;

    private Integer userId;
    private String outTradeNo;
    private String packageName;

    private Integer blockStatus;

    private String doTransferRet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(Integer blockStatus) {
        this.blockStatus = blockStatus;
    }

    public String getDoTransferRet() {
        return doTransferRet;
    }

    public void setDoTransferRet(String doTransferRet) {
        this.doTransferRet = doTransferRet;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
