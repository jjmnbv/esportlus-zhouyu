package com.kaihei.esportingplus.payment.migrate.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author tangtao
 */
@Entity
@Table(name = "payment_refund")
public class RefundOrder {

    /**
     * CREATE TABLE `payment_refund` ( `id` int(11) NOT NULL AUTO_INCREMENT, `fee` int(10) unsigned NOT NULL, `verify_status` smallint(6) NOT NULL,
     * `out_trade_no` varchar(32) NOT NULL, `goods_type` int(11) NOT NULL, `refund_type` int(11) NOT NULL, `remark` varchar(200) NOT NULL,
     * `update_time` datetime(6) NOT NULL, `create_time` datetime(6) NOT NULL, `user_id` int(11) NOT NULL, `out_refund_no` varchar(32) NOT NULL,
     * `do_refund_ret` longtext NOT NULL, `do_refund_status` varchar(32) NOT NULL, `query_ret` longtext NOT NULL, `query_status` varchar(32) NOT NULL,
     * `refund_way` int(11) NOT NULL, `package_name` varchar(20) NOT NULL, `refund_success_time` datetime(6) DEFAULT NULL, PRIMARY KEY (`id`), KEY
     * `payment_refund_user_id_22023e46_fk_members_user_id` (`user_id`), KEY `idx_out_trade_no_refund_way` (`out_trade_no`,`refund_way`), KEY
     * `idx_out_trade_no_verify_status` (`out_trade_no`,`verify_status`), CONSTRAINT `payment_refund_user_id_22023e46_fk_members_user_id` FOREIGN KEY
     * (`user_id`) REFERENCES `members_user` (`id`) ) ENGINE=InnoDB AUTO_INCREMENT=2014190 DEFAULT CHARSET=utf8mb4;
     */

    @Id
    private Integer id;
    private Integer fee;
    private Integer verifyStatus;//1 待审批(未退款),2 审批通过(退款成功),3 审批通过(退款失败),4 按照暴鸡提交结果结算,5 取消申请退款
    private String outTradeNo;
    private Integer goodsType;// orderType
    private Integer refundType;
    private Date updateTime;
    private Date createTime;
    private Integer userId;
    private String outRefundNo;
    /**
     * 1	暴鸡钱包 2	微信 4	支付宝 7	QQ支付
     */
    private Integer refundWay;
    private String packageName;
    private Date refundSuccessTime;

    private String doRefundRet;

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

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getRefundType() {
        return refundType;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public Integer getRefundWay() {
        return refundWay;
    }

    public void setRefundWay(Integer refundWay) {
        this.refundWay = refundWay;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Date getRefundSuccessTime() {
        return refundSuccessTime;
    }

    public void setRefundSuccessTime(Date refundSuccessTime) {
        this.refundSuccessTime = refundSuccessTime;
    }

    public String getDoRefundRet() {
        return doRefundRet;
    }

    public void setDoRefundRet(String doRefundRet) {
        this.doRefundRet = doRefundRet;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
