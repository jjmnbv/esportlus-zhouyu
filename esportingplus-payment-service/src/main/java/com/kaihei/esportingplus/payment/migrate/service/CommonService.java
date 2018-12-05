package com.kaihei.esportingplus.payment.migrate.service;

import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.payment.api.enums.ExternalRefundStateEnum;
import com.kaihei.esportingplus.payment.api.enums.ExternalWithdrawStateEnum;
import com.kaihei.esportingplus.payment.api.enums.TradeType;
import com.kaihei.esportingplus.payment.domain.entity.*;
import com.kaihei.esportingplus.payment.migrate.constant.TypeMapping;
import com.kaihei.esportingplus.payment.migrate.entity.OrderBill;
import com.kaihei.esportingplus.payment.migrate.entity.PayOrder;
import com.kaihei.esportingplus.payment.migrate.entity.RefundOrder;
import com.kaihei.esportingplus.payment.migrate.entity.YunOrder;
import com.kaihei.esportingplus.payment.util.SimpleDateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.support.Assert;

import java.util.Collection;
import java.util.List;

import static com.kaihei.esportingplus.payment.migrate.constant.Constant.*;

/**
 * @author: tangtao
 **/
@ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
@Service
public class CommonService {

    private static PayChannel PAY_CHANNEL_QQ = new PayChannel(2L, "C002");
    private static PayChannel PAY_CHANNEL_WECHAT = new PayChannel(3L, "C003");
    private static PayChannel PAY_CHANNEL_ALI = new PayChannel(4L, "C004");
    private static PayChannel PAY_CHANNEL_YUN = new PayChannel(7L, "C007");


    @Autowired
    private SnowFlake snowFlake;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public ExternalPaymentOrder createPaymentOder(PayOrder payOrder, String orderType, Integer payType) {

        Assert.notNull(payOrder.getTotalFee(), "交易金额不能为空");
        Assert.notNull(payOrder.getOutTradeNo(), "业务单号不能为空");
        Assert.notNull(orderType, "业务单类型不能为空");
        Assert.notNull(payOrder.getUserId(), "用户ID不能为空");
        Assert.notNull(payType, "支付方式不能为空");

        /**
         * CREATE TABLE `external_payment_order` (
         *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
         *   `create_date` datetime DEFAULT NULL COMMENT '创建时间',
         *   `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
         *   `attach` varchar(512) DEFAULT '' COMMENT '附加数据',
         *   `body` varchar(1024) DEFAULT '' COMMENT '内容',
         *   `channel_id` bigint(20) NOT NULL COMMENT '支付渠道ID',
         *   `channel_name` varchar(16) NOT NULL COMMENT '支付渠道名称',
         *   `order_id` varchar(128) NOT NULL COMMENT '订单号',
         *   `order_type` varchar(16) NOT NULL COMMENT '用户ID',
         *   `out_trade_no` varchar(64) NOT NULL COMMENT '业务订单号',
         *   `paied_time` datetime DEFAULT NULL COMMENT '支付完成时间,没有支付完成是1970-0-0 0:0:0',
         *   `pre_pay_id` varchar(32) DEFAULT NULL COMMENT '预支付订单号（微信使用的）',
         *   `source_app_id` varchar(16) NOT NULL COMMENT ' 来源应用ID',
         *   `state` varchar(32) NOT NULL COMMENT '交易状态',
         *   `subject` varchar(128) DEFAULT '' COMMENT '主题',
         *   `total_fee` int(10) NOT NULL DEFAULT '0' COMMENT '支付金额（单位：分）',
         *   `transaction_id` varchar(128) DEFAULT '' COMMENT '第三方返回的订单号',
         *   `user_id` varchar(32) NOT NULL COMMENT '用户ID',
         *   `currency_type` varchar(16) DEFAULT NULL COMMENT '货币类型',
         *   `notify_url` varchar(256) NOT NULL DEFAULT '' COMMENT '支付通知回调URL',
         *   PRIMARY KEY (`id`),
         *   UNIQUE KEY `order_id` (`order_id`),
         *   UNIQUE KEY `uk_order_type_and_out_trade_no` (`order_type`,`out_trade_no`),
         *   KEY `idx_out_trade_no_and_order_type_external_payment_order` (`out_trade_no`,`order_type`),
         *   KEY `idx_user_id_and_order_id_external_payment_order` (`user_id`,`order_id`)
         * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
         */

        ExternalPaymentOrder externalPaymentOrder = new ExternalPaymentOrder();
        externalPaymentOrder.setUserId(payOrder.getUserId().toString());

        externalPaymentOrder.setOutTradeNo(payOrder.getOutTradeNo());

        externalPaymentOrder.setTotalFee(payOrder.getTotalFee());
        externalPaymentOrder.setPrePayId(payOrder.getPrepayId());
        externalPaymentOrder.setTransactionId(payOrder.getTransactionId());

        externalPaymentOrder.setPaiedTime(payOrder.getPayTime());
        externalPaymentOrder.setCreateDate(payOrder.getCreateTime());
        externalPaymentOrder.setLastModifiedDate(payOrder.getUpdateTime());

        externalPaymentOrder.setOrderType(orderType);

        externalPaymentOrder.setState(TypeMapping.getStatus(payOrder.getStatus()));

        externalPaymentOrder.setOrderId(String.valueOf(snowFlake.nextId()));
        externalPaymentOrder.setSourceAppId(TypeMapping.getSourceAppId(payOrder.getPackageName()));

        PayChannel payChannel = getPayChannel(payType);
        externalPaymentOrder.setChannelId(payChannel.getId());
        externalPaymentOrder.setChannelName(payChannel.getTag());

        externalPaymentOrder.setNotifyUrl("");

        return externalPaymentOrder;

    }

    public ExternalTradeBill createExternalTradeBill(OrderBill orderBill, String transactionId, String externalOrderId, Integer payType,
            TradeType tradeType) {

        Assert.notNull(orderBill.getTotalFee(), "交易金额不能为空");
        Assert.notNull(orderBill.getOrderId(), "业务单号不能为空");
        Assert.notNull(orderBill.getOrderType(), "业务单类型不能为空");
        Assert.notNull(transactionId, "第三方支付流水号不能为空");
        Assert.notNull(tradeType, "交易类型不能为空");

        ExternalTradeBill externalTradeBill = new ExternalTradeBill();
        long id = snowFlake.nextId();
        externalTradeBill.setId(id);

        externalTradeBill.setOrderType(orderBill.getOrderType());
        externalTradeBill.setOutTradeNo(orderBill.getOrderId());
        externalTradeBill.setFlowNo(String.valueOf(id));
        externalTradeBill.setTotalFee(orderBill.getTotalFee());

        externalTradeBill.setOrderId(externalOrderId);
        externalTradeBill.setTransactionId(transactionId);
        externalTradeBill.setTradeType(tradeType.getCode());

        externalTradeBill.setLastModifiedDate(orderBill.getReceiptTime());
        externalTradeBill.setCreateDate(orderBill.getCreateTime());

        PayChannel payChannel = getPayChannel(payType);
        externalTradeBill.setChannel(payChannel.getTag());

        return externalTradeBill;
    }

    /**
     * 云账户提现专用
     */
    public ExternalWithdrawOrder createExternalWithdrawOrder(YunOrder yunOrder, String packageName) {

        Assert.notNull(yunOrder.getTotalFee(), "交易金额不能为空");
        Assert.notNull(yunOrder.getOutTradeNo(), "业务单号不能为空");
        Assert.notNull(yunOrder.getIdcardNumber(), "身份证号");
        Assert.notNull(yunOrder.getOpenid(), "用户提现银行卡号、支付宝账号、微信openid");
        Assert.notNull(yunOrder.getChannel(), "支付渠道不能为空");

        /**
         *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
         *   `create_date` datetime DEFAULT NULL COMMENT '创建时间',
         *   `last_modified_date` datetime DEFAULT NULL COMMENT '最近更新时间',
         *   `broker_bank_bill` varchar(64) DEFAULT NULL COMMENT '经纪公司打款交易流水号',
         *   `broker_wallet_ref` varchar(64) DEFAULT NULL COMMENT '经纪公司打款商户订单号',
         *   `card_no` varchar(32) NOT NULL COMMENT '用户提现银行卡号、支付宝账号、微信openid',
         *   `channel` varchar(16) NOT NULL COMMENT '支付渠道',
         *   `idcard_number` varchar(18) NOT NULL COMMENT '身份证号',
         *   `message` varchar(128) DEFAULT NULL COMMENT '状态码详细说明',
         *   `order_id` varchar(50) NOT NULL COMMENT '订单号',
         *   `out_trade_no` varchar(50) NOT NULL COMMENT '业务订单号',
         *   `paied_time` datetime DEFAULT NULL COMMENT '付款时间',
         *   `real_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
         *   `ref_no` varchar(64) DEFAULT NULL COMMENT '云经纪外部关联流水号',
         *   `source_app_id` varchar(16) NOT NULL COMMENT '来源应用ID',
         *   `state` varchar(16) NOT NULL COMMENT '状态(PROCESSING-处理中|SUCCESS-已完成|FAILED-失败|CANCEL-已取消)',
         *   `status_code` varchar(16) DEFAULT NULL COMMENT '失败状态码',
         *   `sys_bank_bill` varchar(128) DEFAULT NULL COMMENT '系统打款交易流水号',
         *   `sys_wallet_ref` varchar(64) DEFAULT NULL COMMENT '系统打款商户订单号',
         *   `total_fee` int(10) NOT NULL COMMENT '提现金额(单位:分)',
         *   `user_id` varchar(32) NOT NULL COMMENT '用户外键id',
         */

        ExternalWithdrawOrder externalWithdrawOrder = new ExternalWithdrawOrder();

        externalWithdrawOrder.setOrderId(String.valueOf(snowFlake.nextId()));

        externalWithdrawOrder.setCardNo(yunOrder.getOpenid());
        externalWithdrawOrder.setIdcardNumber(yunOrder.getIdcardNumber());
        externalWithdrawOrder.setOutTradeNo(yunOrder.getOutTradeNo());

        externalWithdrawOrder.setCreateDate(yunOrder.getCreateTime());
        externalWithdrawOrder.setLastModifiedDate(yunOrder.getUpdateTime());
        externalWithdrawOrder.setPaiedTime(yunOrder.getPayTime() == null ? null : yunOrder.getPayTime());

        externalWithdrawOrder.setRealName(yunOrder.getRealname());
        externalWithdrawOrder.setRefNo(yunOrder.getRef());
        externalWithdrawOrder.setSourceAppId(TypeMapping.getSourceAppId(packageName));

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
        switch (yunOrder.getStatus()) {
            case -10:
                //-10 	创建
                externalWithdrawOrder.setState(ExternalWithdrawStateEnum.PROCESSING.getValue());
                break;
            case 1:
                //1 	订单提交到支付网关成功，如发果银行退汇该订单状态值会变化，不代表最终状态
                externalWithdrawOrder.setState(ExternalWithdrawStateEnum.SUCCESS.getValue());
                break;
            case 2:
                //2 	订单数据校验不通过
                externalWithdrawOrder.setState(ExternalWithdrawStateEnum.FAILED.getValue());
                break;
            case 4:
                //4 	暂停处理，一般是账户余额不足，充值后可以继续打款
                externalWithdrawOrder.setState(ExternalWithdrawStateEnum.PROCESSING.getValue());
                break;
            case 9:
                //9 	支付被退回
                externalWithdrawOrder.setState(ExternalWithdrawStateEnum.REFUND.getValue());
                break;
            default:
                throw new IllegalArgumentException("不支持的云账户状态：" + yunOrder.getStatus());
        }

        externalWithdrawOrder.setTotalFee(yunOrder.getTotalFee());
        externalWithdrawOrder.setUserId(yunOrder.getUserId().toString());

        externalWithdrawOrder.setChannel(PAY_CHANNEL_YUN.getTag());

        return externalWithdrawOrder;

    }

    public ExternalRefundOrder createExternalRefundOrder(RefundOrder refundOrder) {

        Assert.notNull(refundOrder.getFee(), "交易金额不能为空");
        Assert.notNull(refundOrder.getOutTradeNo(), "业务单号不能为空");
        Assert.notNull(refundOrder.getOutRefundNo(), "退款业务号不能为空");
        ExternalRefundOrder externalRefundOrder = new ExternalRefundOrder();

        externalRefundOrder.setTransactionId("");
        externalRefundOrder.setNotifyUrl("");

        externalRefundOrder.setCreateDate(refundOrder.getCreateTime());
        externalRefundOrder.setLastModifiedDate(refundOrder.getUpdateTime());
        externalRefundOrder.setOutTradeNo(refundOrder.getOutTradeNo());
        externalRefundOrder.setOrderType(refundOrder.getGoodsType().toString());
        externalRefundOrder.setOutRefundNo(refundOrder.getOutRefundNo());

        externalRefundOrder.setRefundTime(refundOrder.getRefundSuccessTime());

        PayChannel payChannel = getPayChannel(refundOrder.getRefundWay());
        externalRefundOrder.setChannelId(payChannel.getId());
        externalRefundOrder.setChannelName(payChannel.getTag());
        externalRefundOrder.setSourceAppId(TypeMapping.getSourceAppId(refundOrder.getPackageName()));

        // TODO 目前只迁移了成功的
        externalRefundOrder.setState(ExternalRefundStateEnum.SUCCESS.getCode());
        externalRefundOrder.setTotalFee(refundOrder.getFee());
        externalRefundOrder.setOrderId(String.valueOf(snowFlake.nextId()));
        externalRefundOrder.setUserId(refundOrder.getUserId().toString());

        return externalRefundOrder;
    }


    private PayChannel getPayChannel(Integer payType) {
        switch (payType) {
            case PAY_TYPE_WECHAT:
                return PAY_CHANNEL_WECHAT;
            case PAY_TYPE_ALI:
                return PAY_CHANNEL_ALI;
            case PAY_TYPE_QQ:
                return PAY_CHANNEL_QQ;
            case PAY_TYPE_YUN:
                return PAY_CHANNEL_YUN;
            default:
                throw new IllegalArgumentException("不支持的支付类型：payType=" + payType);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSavePaymentOrder(Collection<ExternalPaymentOrder> orders, List<ExternalTradeBill> externalTradeBills) {
        if (CollectionUtils.isNotEmpty(orders)) {
            StringBuffer slq = new StringBuffer(
                    "INSERT INTO external_payment_order(create_date, last_modified_date, channel_id, channel_name, order_id, order_type, out_trade_no, paied_time, pre_pay_id, source_app_id, state, total_fee, transaction_id, user_id, notify_url) VALUES");
            for (ExternalPaymentOrder order : orders) {
                slq.append("('" + SimpleDateUtils.format(order.getCreateDate()) + "',");
                if (order.getLastModifiedDate() == null) {
                    slq.append("NULL,");
                } else {
                    slq.append("'" + SimpleDateUtils.format(order.getLastModifiedDate()) + "',");
                }
                slq.append(order.getChannelId() + ",");
                slq.append("'" + order.getChannelName() + "',");
                slq.append("'" + order.getOrderId() + "',");
                slq.append("'" + order.getOrderType() + "',");
                slq.append("'" + order.getOutTradeNo() + "',");
                if (order.getPaiedTime() == null) {
                    slq.append("NULL,");
                } else {
                    slq.append("'" + SimpleDateUtils.format(order.getPaiedTime()) + "',");
                }
                slq.append("'" + order.getPrePayId() + "',");
                slq.append("'" + order.getSourceAppId() + "',");
                slq.append("'" + order.getState() + "',");
                slq.append(order.getTotalFee() + ",");
                slq.append("'" + order.getTransactionId() + "',");
                slq.append("'" + order.getUserId() + "',");
                slq.append("'" + order.getNotifyUrl() + "'),");

            }
            String sql = slq.substring(0, slq.length() - 1);
            jdbcTemplate.execute(sql);
        }
        batchSaveTradeBill(externalTradeBills);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSaveWithdrawOrder(Collection<ExternalWithdrawOrder> orders, List<ExternalTradeBill> externalTradeBills) {
        if (CollectionUtils.isNotEmpty(orders)) {
            StringBuffer slq = new StringBuffer(
                    "INSERT INTO external_withdraw_order(create_date, last_modified_date, card_no, channel, idcard_number, order_id, out_trade_no, paied_time, real_name, ref_no, source_app_id, state, total_fee, user_id) VALUES");
            for (ExternalWithdrawOrder order : orders) {
                slq.append("('" + SimpleDateUtils.format(order.getCreateDate()) + "',");
                if (order.getLastModifiedDate() == null) {
                    slq.append("NULL,");
                } else {
                    slq.append("'" + SimpleDateUtils.format(order.getLastModifiedDate()) + "',");
                }
                slq.append("'" + order.getCardNo() + "',");
                slq.append("'" + order.getChannel() + "',");
                slq.append("'" + order.getIdcardNumber() + "',");
                slq.append("'" + order.getOrderId() + "',");
                slq.append("'" + order.getOutTradeNo() + "',");
                if (order.getPaiedTime() == null) {
                    slq.append("NULL,");
                } else {
                    slq.append("'" + SimpleDateUtils.format(order.getPaiedTime()) + "',");
                }
                slq.append("'" + order.getRealName() + "',");
                slq.append("'" + order.getRefNo() + "',");
                slq.append("'" + order.getSourceAppId() + "',");
                slq.append("'" + order.getState() + "',");
                slq.append(order.getTotalFee() + ",");
                slq.append("'" + order.getUserId() + "'),");
            }

            String sql = slq.substring(0, slq.length() - 1);
            jdbcTemplate.execute(sql);
        }

        batchSaveTradeBill(externalTradeBills);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSaveRefundOrder(Collection<ExternalRefundOrder> orders, List<ExternalTradeBill> externalTradeBills) {
        if (CollectionUtils.isNotEmpty(orders)) {
            StringBuffer slq = new StringBuffer(
                    "INSERT INTO external_refund_order(create_date, last_modified_date, channel_id, channel_name, order_id, order_type, out_refund_no"
                            + ", out_trade_no, pay_order_id, refund_time, source_app_id, state, total_fee, transaction_id, user_id, notify_url) VALUES");
            for (ExternalRefundOrder order : orders) {
                slq.append("('" + SimpleDateUtils.format(order.getCreateDate()) + "',");
                if (order.getLastModifiedDate() == null) {
                    slq.append("NULL,");
                } else {
                    slq.append("'" + SimpleDateUtils.format(order.getLastModifiedDate()) + "',");
                }
                slq.append(order.getChannelId() + ",");
                slq.append("'" + order.getChannelName() + "',");
                slq.append("'" + order.getOrderId() + "',");
                slq.append("'" + order.getOrderType() + "',");
                slq.append("'" + order.getOutRefundNo() + "',");
                slq.append("'" + order.getOutTradeNo() + "',");
                slq.append("'" + order.getPayOrderId() + "',");
                if (order.getRefundTime() == null) {
                    slq.append("NULL,");
                } else {
                    slq.append("'" + SimpleDateUtils.format(order.getRefundTime()) + "',");
                }
                slq.append("'" + order.getSourceAppId() + "',");
                slq.append("'" + order.getState() + "',");
                slq.append(order.getTotalFee() + ",");
                slq.append("'" + order.getTransactionId() + "',");
                slq.append("'" + order.getUserId() + "',");
                slq.append("'" + order.getNotifyUrl() + "'),");
            }
            String sql = slq.substring(0, slq.length() - 1);
            jdbcTemplate.execute(sql);
        }
        batchSaveTradeBill(externalTradeBills);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSaveTradeBill(List<ExternalTradeBill> externalTradeBills) {
        if (CollectionUtils.isNotEmpty(externalTradeBills)) {
            StringBuffer slq = new StringBuffer(
                    "INSERT INTO external_trade_bill(id, create_date, last_modified_date, channel, flow_no, order_id, order_type, out_trade_no, total_fee, trade_type, transaction_id) VALUES");
            for (ExternalTradeBill tradeBill : externalTradeBills) {
                slq.append("(" + tradeBill.getId() + ",");
                slq.append("'" + SimpleDateUtils.format(tradeBill.getCreateDate()) + "',");
                if (tradeBill.getLastModifiedDate() == null) {
                    slq.append("NULL,");
                } else {
                    slq.append("'" + SimpleDateUtils.format(tradeBill.getLastModifiedDate()) + "',");
                }
                slq.append("'" + tradeBill.getChannel() + "',");
                slq.append("'" + tradeBill.getFlowNo() + "',");
                slq.append("'" + tradeBill.getOrderId() + "',");
                slq.append("'" + tradeBill.getOrderType() + "',");
                slq.append("'" + tradeBill.getOutTradeNo() + "',");
                slq.append(tradeBill.getTotalFee() + ",");
                slq.append("'" + tradeBill.getTradeType() + "',");
                slq.append("'" + tradeBill.getTransactionId() + "'),");
            }
            String sql = slq.substring(0, slq.length() - 1);
            jdbcTemplate.execute(sql);
        }
    }
}
