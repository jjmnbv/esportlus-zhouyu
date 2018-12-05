package com.kaihei.esportingplus.trade.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.trade.api.feign.PVPFreeOrdersServiceClient;
import com.kaihei.esportingplus.trade.api.params.ChickenPointIncomeParams;
import com.kaihei.esportingplus.trade.api.params.PVPFreeOrdersForBackGroundParams;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBossOrderForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderIncomeForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderListVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreePreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeTeamOrderVo;
import com.kaihei.esportingplus.trade.domain.service.PVPFreeInComeService;
import com.kaihei.esportingplus.trade.domain.service.PVPFreeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/orders/pvp/free")
@Api(tags = {"免费车队订单服务接口"})
public class PVPFreeOrderController implements PVPFreeOrdersServiceClient {

    @Autowired
    private PVPFreeInComeService freeInComeService;
    @Autowired
    private PVPFreeOrderService pvpFreeOrderService;

    @ApiOperation("获取计算暴鸡鸡分收益")
    @PostMapping("chickenPointIncome")
    @Override
    public ResponsePacket<PVPFreePreIncomeVo> getChickenPointIncome(
            @RequestBody ChickenPointIncomeParams incomeParams) {
        return ResponsePacket
                .onSuccess(freeInComeService.getChickenPointIncome(incomeParams, null));
    }

    @ApiOperation("通过订单号获取订单详情")
    @Override
    public ResponsePacket<PVPFreeTeamOrderVo> getOrderDetailsBySequence(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence) {
        return ResponsePacket.onSuccess(pvpFreeOrderService.getOrderDetailsBySequence(
                UserSessionContext.getUser().getUid(), sequence));
    }

    @ApiOperation("通过车队序列号获取订单详情")
    @Override
    public ResponsePacket<PVPFreeTeamOrderVo> getOrderDetailsByTeamSequence(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("teamSequence") String teamSequence) {
        return ResponsePacket.onSuccess(pvpFreeOrderService.getOrderDetailsByUidAndTeamSequence(
                UserSessionContext.getUser().getUid(), teamSequence));
    }

    /**
     * 查询老板订单列表
     */
    @ApiOperation("查询老板订单列表")
    @Override
    public ResponsePacket<PagingResponse<PVPFreeOrderListVO>> selectUserBossOrdersByPage(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "第几页", required = true) @RequestParam(value = "offset", required = true) Integer offset,
            @ApiParam(value = "每页多少记录", required = true) @RequestParam(value = "limit", required = true) Integer limit) {
        return ResponsePacket
                .onSuccess(pvpFreeOrderService.selectUserBossOrdersByPage(offset, limit));
    }

    /**
     * 查询老板订单列表
     */
    @ApiOperation("查询暴鸡或暴娘订单列表")
    @Override
    @GetMapping("/baoji/list")
    public ResponsePacket<PagingResponse<PVPFreeOrderListVO>> selectUserBaojiOrdersByPage(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "第几页", required = true) @RequestParam(value = "offset", required = true) Integer offset,
            @ApiParam(value = "每页多少记录", required = true) @RequestParam(value = "limit", required = true) Integer limit) {
        return ResponsePacket
                .onSuccess(pvpFreeOrderService.selectUserBaojiOrdersByPage(offset, limit));
    }

    /**
     * 后台系统查询暴鸡订单
     */
    @ApiOperation("后台系统查询暴鸡订单")
    @Override
    public ResponsePacket<PagingResponse<PVPFreeBossOrderForBackGroundVO>> selectPvpFreeBaojiOrderForBackGroundVO(
            @RequestBody
                    PVPFreeOrdersForBackGroundParams params) {
        return ResponsePacket
                .onSuccess(pvpFreeOrderService.selectPvpFreeBaojiOrderForBackGroundVO(params));
    }

    /**
     * 后台系统查询老板订单
     */
    @ApiOperation("后台系统查询老板订单")
    @Override
    public ResponsePacket<PagingResponse<PVPFreeBossOrderForBackGroundVO>> selectPvpFreeBossOrderForBackGroundVO(
            @RequestBody PVPFreeOrdersForBackGroundParams params) {
        return ResponsePacket
                .onSuccess(pvpFreeOrderService.selectPvpFreeBossOrderForBackGroundVO(params));
    }

    /**
     * 后管系统该暴鸡订单给从老板处获得的收益明细
     */
    @ApiOperation("后管系统该暴鸡订单给从老板处获得的收益明细")
    @Override
    public ResponsePacket<List<PVPFreeOrderIncomeForBackGroundVO>> selectPVPFreeBaojiFromBossIncome(
            @RequestParam(value = "sequence", required = true) String sequence) {
        return ResponsePacket
                .onSuccess(pvpFreeOrderService.selectPVPFreeBaojiFromBossIncome(sequence));
    }

    /**
     * 后管系统该老板订单给与暴鸡收益明细
     */
    @ApiOperation("后管系统该老板订单给与暴鸡收益明细")
    @Override
    public ResponsePacket<List<PVPFreeOrderIncomeForBackGroundVO>> selectPVPFreeBossGiveBaojiIncome(
            @RequestParam(value = "sequence", required = true) String sequence) {
        return ResponsePacket
                .onSuccess(pvpFreeOrderService.selectPVPFreeBossGiveBaojiIncome(sequence));
    }
}
