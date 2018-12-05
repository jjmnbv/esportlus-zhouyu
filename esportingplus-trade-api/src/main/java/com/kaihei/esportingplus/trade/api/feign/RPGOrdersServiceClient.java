package com.kaihei.esportingplus.trade.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.trade.api.params.CheckTeamMemberPayedParams;
import com.kaihei.esportingplus.trade.api.params.OrderParams;
import com.kaihei.esportingplus.trade.api.params.OrderQueryParams;
import com.kaihei.esportingplus.trade.api.params.RPGInComeParams;
import com.kaihei.esportingplus.trade.api.params.StudioOrderQueryParams;
import com.kaihei.esportingplus.trade.api.params.StudioUserComplitedBossOrderStatisticGetParams;
import com.kaihei.esportingplus.trade.api.params.StudioUserOrderStatisticsQueryParams;
import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusRPGParams;
import com.kaihei.esportingplus.trade.api.vo.BaojiOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.BossOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.CreatedBossOrderVO;
import com.kaihei.esportingplus.trade.api.vo.OrderItemTeamVo;
import com.kaihei.esportingplus.trade.api.vo.OrderVO;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.StudioOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.StudioUserOrderStatisticVO;
import com.kaihei.esportingplus.trade.api.vo.TeamOrderVo;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 基于feign实现远程车队服务接口调用<br/>
 * 1. esportingplus-trade-service为服务名 <br/>
 * 2. fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-trade-service", path = "/orders", fallbackFactory = RPGOrdersClientFallbackFactory.class)
public interface RPGOrdersServiceClient {

    /**
     *@Description: 根据sequeue查询订单
     *@param: [sequeue] 分布式订单id
     *@return: com.kaihei.esportingplus.common.ResponsePacket<com.kaihei.esportingplus.trade.api.vo.OrderVO>
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 15:29
    */
    @GetMapping("/order/{sequeue}")
    ResponsePacket<OrderVO> getBySequenceId(@PathVariable("sequeue") String sequeue);

    /**
     *@Description: 根据sequeue和状态查询订单
     *@param: [sequeue, status]
     *@return: com.kaihei.esportingplus.common.ResponsePacket<com.kaihei.esportingplus.trade.api.vo.OrderVO>
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/22 14:23
    */
    @PostMapping("/order/{teamSequeue}")
    ResponsePacket<List<OrderVO>> getByTeamSequenceIdAndUids(@PathVariable("teamSequeue")String teamSequeue,@RequestBody  List<String> uids);

    /**
     *@Description: 更新车队队员，订单状态
     *@param: [updateOrderStatusParams]
     *@return: com.kaihei.esportingplus.common.ResponsePacket<com.kaihei.esportingplus.trade.api.vo.OrderVO>
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 15:29
    */
    @PostMapping("/order/updateOrderStatus")
    ResponsePacket<Void> updateOrderStatus(@RequestBody UpdateOrderStatusRPGParams updateOrderStatusRPGParams);

    /**
     *@Description: 校验队员(老板)是否已支付
     *@param: [updateOrderStatusParams]
     *@return: com.kaihei.esportingplus.common.ResponsePacket<java.lang.Void>
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 20:15
    */
    @PostMapping("/order/checkTeamMemberPayed")
    ResponsePacket<Void> checkTeamMemberPayed(@RequestBody CheckTeamMemberPayedParams checkTeamMemberPayedParams);

    /**
     *@Description: 校验队员(老板)是否已支付
     *@param: [order_sequence]
     *@return: com.kaihei.esportingplus.common.ResponsePacket<java.lang.Void>
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/8 20:15
     */
    @GetMapping("/order/checkTeamMemberPayed/{orderSequence}")
    ResponsePacket<Void> checkTeamMemberPayed(@PathVariable("orderSequence") String orderSequence);

    /**
     *@Description: 计算暴鸡收益
     *@param: [profitQueryParams]
     *@return: com.kaihei.esportingplus.common.ResponsePacket<java.lang.Integer>
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/8/10 14:57
    */
    @PostMapping("/order/profit")
    ResponsePacket<PVPPreIncomeVo> getPreProfit(@RequestBody RPGInComeParams inComeParams);

    /**
     * 创建老板订单
     * @param orderQueryParams
     * @return
     */
    @PostMapping("/order/createOrder")
    ResponsePacket<CreatedBossOrderVO> createOrder(@RequestHeader(name = "Authorization") String token, @RequestBody OrderQueryParams orderQueryParams);

    /**
     * 创建暴鸡订单
     * @Param sequeue
     */
    @GetMapping("/order/createBaojiOrder/{sequeue}")
    ResponsePacket<List<String>> createBaojiOrder(@PathVariable("sequeue") String sequeue);

    /**
     * 根据订单序列号获取队长信息
     * @param sequeue
     * @return
     */
    @GetMapping("/order/selectOrderItemTeamByOrderSequeue/{sequeue}")
    ResponsePacket<OrderItemTeamVo> selectOrderItemTeamByOrderSequeue(@PathVariable("sequeue") String sequeue);

    /**
     * 分页查询老板订单列表
     * @param orderParams
     * @return PagingResponse<BossOrderListVo>
     */
    @PostMapping("/order/getUserBossOrdersByPage")
    ResponsePacket<PagingResponse<BossOrderListVo>> getUserBossOrdersByPage(@RequestHeader(name = "Authorization") String token,@RequestBody OrderParams orderParams);

    /**
     * 分页查询暴鸡订单列表
     * @param orderParams
     * @return PagingResponse<BaojiOrderListVo>
     */
    @PostMapping("/order/getUserBaojiOrdersByPage")
    ResponsePacket<PagingResponse<BaojiOrderListVo>> getUserBaojiOrdersByPage(@RequestHeader(name = "Authorization") String token,@RequestBody OrderParams orderParams);

    /**
     * 分页查询工作室订单列表
     * @param studioOrderQueryParams
     * @return PagingResponse<StudioOrderListVo>
     */
    @PostMapping("/order/getStudioUserOrdersByPage")
    ResponsePacket<PagingResponse<StudioOrderListVo>> getStudioUserOrdersByPage(@RequestBody StudioOrderQueryParams studioOrderQueryParams);

    /**
     * 通过车队序列号获取订单详情
     */
    @GetMapping("/order/details/team_sequence/{team_sequence}")
    ResponsePacket<TeamOrderVo> getOrderDetailsByUidAndTeamSequence(@RequestHeader(name = "Authorization")String token,@PathVariable("team_sequence") String teamSequence);

    /**
     * 通过订单序列号获取订单详情
     */
    @GetMapping("/order/details/order_sequence/{sequence}")
    ResponsePacket<TeamOrderVo> getOrderDetailsBySequence(@RequestHeader(name = "Authorization")String token,@PathVariable("sequence") String sequence);

    /**
     * 统计工作室暴鸡订单
     */
    @PostMapping("/order/getStudioUserOrderStatistics")
    ResponsePacket<List<StudioUserOrderStatisticVO>> getStudioUserOrderStatistics(@RequestBody
            StudioUserOrderStatisticsQueryParams studioUserOrderStatisticsQueryParams);

    @ApiOperation("统计车队完成的老板订单数")
    @PostMapping("getTeamComplitedBossOrderStatistics")
    ResponsePacket<Map<String, Long>> getTeamComplitedBossOrderStatistics(
            @RequestParam("teamSequences") List<String> teamSequences);

    @ApiOperation("统计工作室完成的老板订单的数量")
    @PostMapping("studioUserComplitedBossOrderStatistics")
    ResponsePacket<Map<String, Long>> getStudioUserComplitedBossOrderStatistics(
            @RequestBody List<StudioUserComplitedBossOrderStatisticGetParams> studioUserComplitedBossOrderStatisticGetParams);
}
