package com.kaihei.esportingplus.gamingteam.api.mq;

import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusPVPParams;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 修改订单状态消息
 * 场景: 退出车队、踢出车队、解散车队、结束车队
 * @author liangyi
 */
public class PVPUpdateOrderStatusMessage implements Serializable {

    private static final long serialVersionUID = 3561476320519766556L;
    /**
     * 修改订单状态参数
     */
    private UpdateOrderStatusPVPParams updateOrderStatusPVPParams;

    public PVPUpdateOrderStatusMessage() {
    }

    public UpdateOrderStatusPVPParams getUpdateOrderStatusPVPParams() {
        return updateOrderStatusPVPParams;
    }

    public void setUpdateOrderStatusPVPParams(
            UpdateOrderStatusPVPParams updateOrderStatusPVPParams) {
        this.updateOrderStatusPVPParams = updateOrderStatusPVPParams;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
