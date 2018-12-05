package com.kaihei.esportingplus.trade.handler;

import com.kaihei.esportingplus.trade.api.params.OrderTeamRPGMember;
import com.kaihei.esportingplus.trade.common.ProfitCheckParams;
import com.kaihei.esportingplus.trade.common.UpdateOrderParams;
import java.util.List;

/**
 * 逻辑事件处理类
 *
 * @author Orochi-Yzh
 * @dateTime 2018/2/6 9:19
 * @updatetor
 */
public class ActionHandler {

    /**
     * 逻辑处理声明
     */
    public static DispatHandler<OrderTeamRPGMember,String,Integer> dispatHandler;
    public static UpdateHandler<UpdateOrderParams> updateHandler;

    @FunctionalInterface
    public interface DispatHandler<OrderTeamMembers,String,Integer> {
        /**
         *@Description: 逻辑划分： 根据队员状态调用对应处理逻辑
         *@param: [members 队员, teamSequence 车队Sequenceid, teamStatus 队员状态 gameResult 游戏结果]
         *@return: void
         *@throws:
         *
         *@author  Orochi-Yzh
         *@dateTime  2018/8/8 21:17
        */
        ProfitCheckParams handle(List<OrderTeamMembers> teamMembers,OrderTeamMembers uid,String teamSequence,Integer teamStatus,Integer gameResult);

    }

    @FunctionalInterface
    public interface UpdateHandler<UpdateOrderParams> {
       /**
        *@Description: 更新订单状态
        *@param: [member, teamSequence, OrderStatus, isRefund, profitAmout, handler]
        *@return: void
        *@throws:
        *
        *@author  Orochi-Yzh
        *@dateTime  2018/8/9 18:01
       */
        void update(UpdateOrderParams params,ProfitCheckParams result);
    }

}
