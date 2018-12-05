package com.kaihei.esportingplus.trade.domain.service;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import com.kaihei.esportingplus.trade.api.params.OrderParams;
import com.kaihei.esportingplus.trade.api.params.OrderQueryParams;
import com.kaihei.esportingplus.trade.api.params.StudioOrderQueryParams;
import com.kaihei.esportingplus.trade.api.params.StudioUserComplitedBossOrderStatisticGetParams;
import com.kaihei.esportingplus.trade.api.params.StudioUserOrderStatisticsQueryParams;
import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusRPGParams;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinPayConfirmPacket;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinRefundPacket;
import com.kaihei.esportingplus.trade.api.vo.BaojiOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.BossOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.CreatedBossOrderVO;
import com.kaihei.esportingplus.trade.api.vo.OrderItemTeamVo;
import com.kaihei.esportingplus.trade.api.vo.StudioOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.StudioUserOrderStatisticVO;
import com.kaihei.esportingplus.trade.api.vo.TeamOrderVo;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;

public interface RPGOrderService extends OrderService {

    /**
     * 创建车队老板订单
     * @param orderParams
     * @return
     */
    public CreatedBossOrderVO createTeamBossOrder(OrderQueryParams orderParams);
    /**
     * 创建暴鸡订单
     * @param sequeue
     */
    List<String> createBaojiOrder(String sequeue);

    void createBaojiOrderAndUpdateBossOrderStatus(RPGTeamStartOrderVO vo);
    /**
     * 分页查询老板订单列表
     * @param orderParams
     * @return PagingResponse<BossOrderListVo>
     */
    PagingResponse<BossOrderListVo> selectUserBossOrdersByPage(OrderParams orderParams);

    /**
     *分页查询暴鸡订单列表
     * @param orderParams
     * @return PagingResponse<BaojiOrderListVo>
     */
    PagingResponse<BaojiOrderListVo> selectUserBaojiOrdersByPage(OrderParams orderParams);

    /**
     * 分页查询工作室订单列表
     * @param studioOrderQueryParams
     * @return PagingResponse<StudioOrderListVo>
     */
    PagingResponse<StudioOrderListVo> getStudioUserOrdersByPage(
            StudioOrderQueryParams studioOrderQueryParams);

    /**
     * 根据订单号获取队长的uid，暴鸡等级，游戏code
     * @param sequeue
     * @return
     */
    OrderItemTeamVo selectOrderItemTeamByOrderSequeue(String sequeue);

    /**
     *@Description: 更新RPG订单状态
     *@param: [updateOrderStatusRPGParams] 参数
     *@return: boolean true:更新状态成功，false:更新状态失败
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 15:45
    */
    void updateOrderStatus(UpdateOrderStatusRPGParams updateOrderStatusRPGParams);

    /**
     * 获取车队订单详情
     * @param uid
     * @param sequence
     */
    TeamOrderVo getOrderDetailsBySequence(String uid, String sequence);

    /**
     * 根据uid和车队序列号获取订单
     * @param uid
     * @param teamSequence
     */
    TeamOrderVo getOrderDetailsByUidAndTeamSequence(String uid, String teamSequence);

    /**
     * 统计工作室暴鸡订单
     */
    List<StudioUserOrderStatisticVO> getStudioUserOrderStatistics(
            StudioUserOrderStatisticsQueryParams studioUserOrderStatisticsQueryParams);

    /**
     *@Description: 退款回调更新订单
     *@param: [WeiXinRefundResult] 微信退款回调参数
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 15:45
     */
    BizExceptionEnum updateOrder(WeiXinRefundPacket weiXinRefundPacket);
    BizExceptionEnum updateOrder(WeiXinPayConfirmPacket weiXinPayConfirmPacket);

    /**
     * 根据车队唯一标识
     *
     * 统计车队完成订单
     */
    Map<String, Long> getTeamComplitedBossOrderStatistics(List<String> teamSequences);

    /**
     * 统计工作室完成的老板订单的数量
     */
    Map<String, Long> getStudioUserComplitedBossOrderStatistics(
            @RequestBody List<StudioUserComplitedBossOrderStatisticGetParams> studioUserComplitedBossOrderStatisticGetParams);
}
