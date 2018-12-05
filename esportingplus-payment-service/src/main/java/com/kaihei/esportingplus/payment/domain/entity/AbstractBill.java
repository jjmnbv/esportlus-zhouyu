package com.kaihei.esportingplus.payment.domain.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

/**
 * 流水记录抽象
 *
 * @author xiaolijun
 */
@MappedSuperclass
public abstract class AbstractBill extends AbstractEntity {

    private static final long serialVersionUID = 8369002908933334164L;

    /***
     * 流水明细标识
     */
    @Column(columnDefinition = "varchar(128) COMMENT '流水明细标识'")
    private String flowNo;

    /**
     * 订单ID{用于关联“订单表”（包括：充值订单表和暴鸡币订单表），一对一关系；}
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '订单ID'")
    private String orderId;

    /***
     * 用户ID（根据订单类型可分为：充值用户、打赏用户）
     */
    @Column(nullable = false, columnDefinition = "varchar(64) COMMENT '用户ID'")
    private String userId;

    /**
     * 主题
     */
    @Column(nullable = false, columnDefinition = "varchar(128) default '' COMMENT '主题'")
    private String subject;

    /***
     * 描述
     */
    @Column(columnDefinition = "varchar(1024) default '' COMMENT '描述'")
    private String body;

    /**
     * 暴鸡币消费额（单位暴鸡币）
     */
    @Column(nullable = false, columnDefinition = "Decimal(19,2) default '0.00' COMMENT '暴鸡币消费额'")
    private BigDecimal amount;

    /***
     * 操作终端
     *
     * PC PC端
     * ANDROID android
     * IOS ios
     * MP 小程序
     * H5 H5页面
     * PA 微信公众号
     * PLATFORM 暴鸡平台系统
     */
    @Column(columnDefinition = "varchar(16) COMMENT '操作系统(H5|IOS|MP|ANDROID|PA|PLATFORM)'")
    private String sourceId;

    /**
     * 支付渠道
     * <p>
     * C001 暴鸡币钱包支付
     * C002 QQ支付
     * C003 微信app支付
     * C004 支付宝支付
     * C005 苹果支付
     * C006 平台系统支付
     * C007 微信小程序支付
     * C008 微信公众号支付
     */
    @Column(columnDefinition = "varchar(8) COMMENT '支付渠道'")
    private String channel;

    /**
     * 订单维度
     * <p>
     *  D001    游戏订单
     *  D002    暴鸡认证订单
     *  D003    充值订单
     *  D004    奖励订单
     *  D005    违规扣款
     *  D006    福利金
     *  D007    抽奖奖金
     *  D008    车队订单
     *  D009    悬赏订单
     *  D010    系统充值订单
     *  D011    提现订单
     *  D012    扣款订单
     *  D013    工作室提现订单
     *  D014    每周好运礼包订单
     *  D015    技能订单
     *  D016    DNF小程序订单
     *  D017    RPG保证金认证订单
     *  D018    暴鸡币充值订单
     *  D019    暴鸡值兑换订单类型
     *  D020    支付系统-暴鸡币充值
     *  D021    支付系统-暴鸡币扣款
     *  D022    支付系统-暴击值扣款
     *  D023    支付系统-打赏订单
     *  D024    支付系统-暴击值提现订单
     *  D025    支付系统-鸡分兑换暴击值
     *  D026    支付系统-暴击值兑换暴鸡币
     */
    @Column(columnDefinition = "varchar(8) COMMENT '订单维度'")
    private String orderDimension;

    /**
     * 订单类型
     * <p>
     * 001 退款订单
     * 002 提现订单
     * 003 暴击值兑换订单
     * 004 暴鸡币打赏订单
     * 005 结算订单
     * 006 充值暴鸡币订单
     * 007 暴鸡币支付订单
     */
    @Column(nullable = false, columnDefinition = "varchar(8) COMMENT '订单类型'")
    private String orderType;

    /**
     * 交易类型
     * <p>
     * T001 收入
     * T002 支出
     * T003 扣款
     * T004 退款
     */
    @Column(nullable = false, columnDefinition = "varchar(8) COMMENT '交易类型'")
    private String tradeType;

    /**
     * 业务类型
     * <p>
     * B0001 王者荣耀
     * B0002 英雄联盟
     * B0003 绝地求生
     * B0004 刺激战场
     * B0005 QQ飞车
     * B0006 全军出击
     * B0007 荒野行动
     * B0008 决战平安京
     * B0009 堡垒之夜
     * B0010 投诉补偿
     * B0011 订单补偿
     * B0012 活动补偿
     */
    @Column(columnDefinition = "varchar(16) COMMENT '业务类型'")
    private String businessType;

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrderDimension() {
        return orderDimension;
    }

    public void setOrderDimension(String orderDimension) {
        this.orderDimension = orderDimension;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
