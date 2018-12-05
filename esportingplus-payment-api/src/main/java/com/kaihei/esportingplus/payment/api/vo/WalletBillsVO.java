package com.kaihei.esportingplus.payment.api.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 用户钱包流水 vo
 * 
 * @author xiaolijun
 */
public class WalletBillsVO implements Serializable {

    private static final long serialVersionUID = 5120533430242155582L;

    /**
     * 流水号
     */
    private String flowNo;
    /**
     * 描述内容
     */
    private String body;
    /***
     * 主题
     */
    private String subject;
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * Python 需要用于做标记的属性
     * true: + （收入）
     * false: - （支出）
     */
    private boolean positive;
    
    /**
     * 订单类型
     *
     * 001 退款订单
     * 002 提现订单
     * 003 暴击值兑换订单
     * 004 暴鸡币打赏订单
     * 005 结算订单
     * 006 暴鸡币订单
     */
    @Column(nullable = false, length = 16)
    private String orderType;
    
    /**
     * 交易类型
     *
     * 001 收入
     * 002 支出
     * 003 扣款
     * 004 退款
     */
    @Column(nullable = false, length = 16)
    private String tradeType;

    public WalletBillsVO() {
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }
}
