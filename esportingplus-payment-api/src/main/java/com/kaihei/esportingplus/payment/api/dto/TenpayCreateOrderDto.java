package com.kaihei.esportingplus.payment.api.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Author: zhouyu
 * @Date: 2018/11/7 11:44
 * @Description: 腾讯下单传输层对象
 */
@Data
public class TenpayCreateOrderDto implements Serializable {

    private String attach;

    private String body;

    private long channelId;

    private String channelName;

    private String orderId;

    private String orderType;

    private String outTradeNo;

    private String prePayId;

    private String subject;

    private Integer totalFee;

    private Date paiedTime;

    private String transactionId;

    /***
     * 交易状态
     * UNPAIED 订单待付款；
     * CLOSING 订单关闭中
     * CLOSED  订单已关闭（系统主动关闭、全额退款）；
     * SUCCESS 订单已付款；
     * CLOSED_NO_REFUND 交易结束，不可退款；
     * CANCELING 订单撤销中
     * CANCEL  订单已撤销（用户主动）。
     */
    private String state;


    private String userId;

    private String sourceAppId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
