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
@Table(name = "payment_yunorder")
public class YunOrder {

    /**
     * CREATE TABLE `payment_yunorder` ( `out_trade_no` varchar(50) NOT NULL, `total_fee` int(10) unsigned NOT NULL, `pay_time` datetime(6) DEFAULT
     * NULL, `create_time` datetime(6) NOT NULL, `update_time` datetime(6) NOT NULL, `raw_data` longtext NOT NULL, `status` smallint(6) NOT NULL,
     * `query_data` longtext NOT NULL, `ref` varchar(50) DEFAULT NULL, `sys_wallet_ref` varchar(50) DEFAULT NULL, `sys_bank_bill` varchar(50) DEFAULT
     * NULL, `broker_wallet_ref` varchar(50) DEFAULT NULL, `broker_bank_bill` varchar(50) DEFAULT NULL, `user_id` int(11) NOT NULL, `idcard_number`
     * varchar(18) NOT NULL, `openid` varchar(64) DEFAULT NULL, `realname` varchar(32) DEFAULT NULL, `channel` varchar(64) NOT NULL, `status_detail`
     * smallint(6) NOT NULL, PRIMARY KEY (`out_trade_no`), KEY `payment_yunorder_user_id_aa60a72d_fk_members_user_id` (`user_id`), CONSTRAINT
     * `payment_yunorder_user_id_aa60a72d_fk_members_user_id` FOREIGN KEY (`user_id`) REFERENCES `members_user` (`id`) ) ENGINE=InnoDB DEFAULT
     * CHARSET=utf8mb4;
     */
    @Id
    private String outTradeNo;
    private Integer totalFee;
    private Date payTime;
    private Date createTime;
    private Date updateTime;
    private String rawData;
    /**
     * -10 	创建
     *
     * 1 	订单提交到支付网关成功，如发果银行退汇该订单状态值会变化，不代表最终状态
     *
     * 2 	订单数据校验不通过
     *
     * 4 	暂停处理，一般是账户余额不足，充值后可以继续打款
     *
     * 9 	支付被退回
     */
    private Integer status;
    private String ref;//云经纪流水
    private Integer userId;
    private String idcardNumber;
    private String openid;
    private String realname;
    /**
     * 微信 支付宝
     */
    private String channel;
    private Integer statusDetail;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIdcardNumber() {
        return idcardNumber;
    }

    public void setIdcardNumber(String idcardNumber) {
        this.idcardNumber = idcardNumber;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(Integer statusDetail) {
        this.statusDetail = statusDetail;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
