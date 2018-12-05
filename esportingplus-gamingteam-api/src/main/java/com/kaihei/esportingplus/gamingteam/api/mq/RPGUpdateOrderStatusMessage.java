package com.kaihei.esportingplus.gamingteam.api.mq;

import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusRPGParams;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 修改订单状态消息--退出车队、踢出车队、解散车队、结束车队
 * @author liangyi
 */
public class RPGUpdateOrderStatusMessage implements Serializable {

    private static final long serialVersionUID = 3279239755705782189L;

    /**
     * 修改订单状态参数
     */
    private UpdateOrderStatusRPGParams updateOrderStatusRPGParams;

    public RPGUpdateOrderStatusMessage() {
    }

    public UpdateOrderStatusRPGParams getUpdateOrderStatusRPGParams() {
        return updateOrderStatusRPGParams;
    }

    public void setUpdateOrderStatusRPGParams(
            UpdateOrderStatusRPGParams updateOrderStatusRPGParams) {
        this.updateOrderStatusRPGParams = updateOrderStatusRPGParams;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
