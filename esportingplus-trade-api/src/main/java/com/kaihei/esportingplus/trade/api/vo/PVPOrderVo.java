package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PVPOrderVo implements Serializable{

    private static final long serialVersionUID = 6946972909538144935L;
    /**
     * 订单状态，0：准备中(暴鸡队员）, 1：待付款（老板上车后的状态），2：已付款（老板付款后的状态），3：已取消，4：已完成
     */
    private String statusZh;
    /**
     * 暴鸡级别
     */
    private Byte baojiLevel;

    /**
     * 订单序列号
     */
    private String sequeue;

    /**
     * 订单名称 免费车队采用采用 游戏类型模板名称|大区|结算数量|局或者小时  组装
     */
    private String orderInfoZh;

    /**
     * 玩法类型名称
     */
    private String playModeName;
    /**
     * 支付或者收益数量
     */
    private String amount;


}
