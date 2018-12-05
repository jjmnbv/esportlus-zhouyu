package com.kaihei.esportingplus.trade.common;

import com.kaihei.esportingplus.trade.domain.entity.Order;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@Data
public final class UpdateOrderParams implements Serializable {

    private static final long serialVersionUID = -521085080651162322L;

    private Order order;
    private Integer memberStatus;
    private Boolean neddRefund;
    private String refundSequence;
    private Integer profitAmout;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
