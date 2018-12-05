package com.kaihei.esportingplus.payment.api.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @Author: zhouyu
 * @Date: 2018/11/12 12:10
 * @Description:支付回调消息传输层对象
 */
@Data
public class TenpayNotifyOrderDto implements Serializable {

    private String orderType;

    private String outTradeNo;

    private String transactionId;

    private String state;

    private Date paiedTime;
}
