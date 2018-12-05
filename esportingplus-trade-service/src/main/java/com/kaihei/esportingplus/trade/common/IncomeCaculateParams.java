package com.kaihei.esportingplus.trade.common;

import com.kaihei.esportingplus.trade.api.params.OrderTeamPVPMember;
import com.kaihei.esportingplus.trade.api.params.OrderTeamRPGMember;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class IncomeCaculateParams implements Serializable {

    private static final long serialVersionUID = -3006740518165118407L;

    //已支付的订单列表
    private List<Order> payedOrders;
    //PVP付费车队的全部队员列表
    private List<OrderTeamPVPMember> pvpTeamMembers;
    //RPG车队的全部队员列表
    private List<OrderTeamRPGMember> rpgTeamMembers;
    //PVP付费车队的当前用户
    private OrderTeamPVPMember pvpMember;
    //RPG付费车队的当前用户
    private OrderTeamRPGMember rpgMember;
    //部分退款的金额，不纳入结算
    private int refundFee;
    //上分净胜局或陪玩已打局
    private int battleSum;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
