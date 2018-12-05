package com.kaihei.esportingplus.trade.controller;

import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.trade.api.enums.OrderStatusEnum;
import com.kaihei.esportingplus.trade.api.feign.RPGOrdersServiceClient;
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
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.service.RPGInComeService;
import com.kaihei.esportingplus.trade.domain.service.RPGOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单
 *
 * @author liangyi
 */
@RestController
@RequestMapping("/orders/rpg")
@Api(tags = {"RPG订单服务接口"})
public class RPGOrderController implements RPGOrdersServiceClient {

    @Autowired
    private RPGOrderService rpgOrderService;

    @Autowired
    private RPGInComeService rpgInComeService;

    @ApiOperation(value = "创建老板订单，返回订单ID")
    @Override
    public ResponsePacket<CreatedBossOrderVO> createOrder(@RequestHeader(name = "Authorization") String token, @RequestBody OrderQueryParams orderParams) {
        //参数校验应该使用切面 TODO
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, orderParams);
        return ResponsePacket.onSuccess(rpgOrderService.createTeamBossOrder(orderParams));
    }

    @ApiOperation(value = "创建暴鸡订单，返回订单ID")
    @Override
    public ResponsePacket<List<String>> createBaojiOrder(@PathVariable("sequeue") String sequeue) {
        //参数校验应该使用切面 TODO
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequeue);
        return ResponsePacket.onSuccess(rpgOrderService.createBaojiOrder(sequeue));
    }

    @ApiOperation(value = "根据订单序列号获取队长信息")
    @Override
    public ResponsePacket<OrderItemTeamVo> selectOrderItemTeamByOrderSequeue(@PathVariable("sequeue") String sequeue) {
        //参数校验应该使用切面 TODO
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequeue);
        return ResponsePacket.onSuccess(rpgOrderService.selectOrderItemTeamByOrderSequeue(sequeue));
    }

    @ApiOperation(value = "分页查询老板订单列表")
    @Override
    public ResponsePacket<PagingResponse<BossOrderListVo>> getUserBossOrdersByPage(@RequestHeader(name = "Authorization") String token,@RequestBody OrderParams orderParams) {
        //参数校验应该使用切面 TODO
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, orderParams);
        return ResponsePacket.onSuccess(rpgOrderService.selectUserBossOrdersByPage(orderParams));
    }

    @ApiOperation(value = "分页查询暴鸡订单列表")
    @Override
    public ResponsePacket<PagingResponse<BaojiOrderListVo>> getUserBaojiOrdersByPage(@RequestHeader(name = "Authorization") String token,@RequestBody OrderParams orderParams) {
        //参数校验应该使用切面 TODO
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, orderParams);
        return ResponsePacket.onSuccess(rpgOrderService.selectUserBaojiOrdersByPage(orderParams));
    }

    @ApiOperation(value = "分页查询工作室订单列表")
    @Override
    public ResponsePacket<PagingResponse<StudioOrderListVo>> getStudioUserOrdersByPage(@RequestBody StudioOrderQueryParams studioOrderQueryParams) {
        //参数校验应该使用切面 TODO
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, studioOrderQueryParams);
        return ResponsePacket.onSuccess(rpgOrderService.getStudioUserOrdersByPage(studioOrderQueryParams));
    }

    @ApiOperation(value = "根据sequeue查询订单")
    @Override
    public ResponsePacket<OrderVO> getBySequenceId(@PathVariable("sequeue") String sequeue) {

        Order order = rpgOrderService.getBySequenceId(sequeue);
        if(order != null){
            OrderVO orderVO = BeanMapper.map(order, OrderVO.class);
            return ResponsePacket.onSuccess(orderVO);
        }else{
            return ResponsePacket.onSuccess();
        }
    }

    @Override
    public ResponsePacket<List<OrderVO>> getByTeamSequenceIdAndUids(@PathVariable("teamSequeue")String teamSequeue, @RequestBody List<String> uids) {
        List<Order> orders = rpgOrderService.getByTeamSequenceIdAndUids(teamSequeue,uids,true);
        if(CollectionUtils.isNotEmpty(orders)){
            List<OrderVO> orderVOList = orders.stream()
                    .map(order -> BeanMapper.map(order, OrderVO.class))
                    .collect(Collectors.toList());
            return ResponsePacket.onSuccess(orderVOList);
        }else{
            return ResponsePacket.onSuccess();
        }
    }


    @ApiOperation(value = "更新车队队员，订单状态")
    @Override
    public ResponsePacket<Void> updateOrderStatus(@RequestBody UpdateOrderStatusRPGParams updateOrderStatusRPGParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, updateOrderStatusRPGParams);
        rpgOrderService.updateOrderStatus(updateOrderStatusRPGParams);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "校验队员(老板)是否已支付")
    @Override
    public ResponsePacket checkTeamMemberPayed(@RequestBody CheckTeamMemberPayedParams checkTeamMemberPayedParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, checkTeamMemberPayedParams);
        checkTeamMemberPayedParams.setPayStatus(OrderStatusEnum.PAYED.getCode());
        BizExceptionEnum exceptionEnum = rpgOrderService
                .checkTeamMemberPayed(checkTeamMemberPayedParams);

        if(exceptionEnum != BizExceptionEnum.SUCCESS){
           return ResponsePacket.onError(exceptionEnum.getErrCode(),
                    String.format(exceptionEnum.getErrMsg(),checkTeamMemberPayedParams.getUid()));
        }

        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "校验队员(老板)是否已支付")
    @Override
    public ResponsePacket checkTeamMemberPayed(@PathVariable("orderSequence") String orderSequence) {
        BizExceptionEnum checkResult = rpgOrderService
                .checkTeamMemberPayed(orderSequence);

        if(checkResult != BizExceptionEnum.SUCCESS){
            return ResponsePacket.onError(checkResult.getErrCode(), String.format(checkResult.getErrMsg(),orderSequence));
        }

        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "计算暴鸡预计收益")
    @Override
    public ResponsePacket<PVPPreIncomeVo> getPreProfit(@RequestBody RPGInComeParams inComeParams) {
        return ResponsePacket.onSuccess(rpgInComeService.getPreInCome(inComeParams));
    }
    @ApiOperation(value = "根据订单号获取订单详情")
    @Override
    public ResponsePacket<TeamOrderVo> getOrderDetailsBySequence(@RequestHeader("Authorization") String token,@PathVariable("sequence") String sequence) {
        return ResponsePacket.onSuccess(rpgOrderService
                .getOrderDetailsBySequence(UserSessionContext.getUser().getUid(), sequence));
    }
    @ApiOperation(value = "根据车队订单号获取订单详情")
    @Override
    public ResponsePacket<TeamOrderVo> getOrderDetailsByUidAndTeamSequence(@RequestHeader(name = "Authorization")String token,
            @PathVariable("team_sequence")String teamSequence) {
        return ResponsePacket.onSuccess(rpgOrderService
                .getOrderDetailsByUidAndTeamSequence(UserSessionContext.getUser().getUid(),
                        teamSequence));
    }

    @ApiOperation("统计工作室暴鸡订单")
    @Override
    public ResponsePacket<List<StudioUserOrderStatisticVO>> getStudioUserOrderStatistics(
            @RequestBody
                    StudioUserOrderStatisticsQueryParams studioUserOrderStatisticsQueryParams) {
        return ResponsePacket.onSuccess(
                rpgOrderService.getStudioUserOrderStatistics(studioUserOrderStatisticsQueryParams));
    }

    @ApiOperation("统计车队完成的老板订单数")
    @PostMapping("teamComplitedBossOrderStatistics")
    @Override
    public ResponsePacket<Map<String, Long>> getTeamComplitedBossOrderStatistics(
            @RequestParam List<String> teamSequences) {
        return ResponsePacket
                .onSuccess(rpgOrderService.getTeamComplitedBossOrderStatistics(teamSequences));
    }

    @ApiOperation("统计工作室完成的老板订单的数量")
    @PostMapping("studioUserComplitedBossOrderStatistics")
    @Override
    public ResponsePacket<Map<String, Long>> getStudioUserComplitedBossOrderStatistics(
            @RequestBody List<StudioUserComplitedBossOrderStatisticGetParams> studioUserComplitedBossOrderStatisticGetParams) {
        return ResponsePacket
                .onSuccess(rpgOrderService.getStudioUserComplitedBossOrderStatistics(
                        studioUserComplitedBossOrderStatisticGetParams));
    }
}
