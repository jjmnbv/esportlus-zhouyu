package com.kaihei.esportingplus.trade.mq.message;

import com.kaihei.esportingplus.trade.domain.entity.Order;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 老板需要退款时发送 mq 消息修改订单状态
 * 本地事务为更新老板订单状态
 * @author liangyi
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefundMessage implements Serializable {

    private static final long serialVersionUID = -3137007081961658358L;
    private Order order;
    private Integer memberStatus;
    private String refundSequence;
    private int refundFee;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
