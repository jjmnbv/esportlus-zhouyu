package com.kaihei.esportingplus.trade.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.trade.api.params.ChickenPointIncomeParams;
import com.kaihei.esportingplus.trade.api.params.PVPFreeOrdersForBackGroundParams;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBossOrderForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderIncomeForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderListVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreePreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeTeamOrderVo;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 基于feign实现远程车队服务接口调用<br/> 1. esportingplus-trade-service为服务名 <br/> 2.
 * fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-trade-service", path = "/orders/pvp/free",
        fallbackFactory = PVPFreeOrdersClientFallbackFactory.class)
public interface PVPFreeOrdersServiceClient {

    @ApiOperation("获取计算暴鸡鸡分收益")
    @PostMapping("chickenPointIncome")
    ResponsePacket<PVPFreePreIncomeVo> getChickenPointIncome(
            @RequestBody ChickenPointIncomeParams incomeParams);

    /**
     * 通过车队序列号查询订单详情
     */
    @GetMapping("/detalis/sequence/{sequence}")
    ResponsePacket<PVPFreeTeamOrderVo> getOrderDetailsBySequence(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("sequence") String sequence);

    /**
     * 通过车队序列号查询订单详情
     */
    @GetMapping("/detalis/team_sequence/{teamSequence}")
    ResponsePacket<PVPFreeTeamOrderVo> getOrderDetailsByTeamSequence(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("teamSequence") String teamSequence);

    /**
     * 查询老板订单列表
     */
    @GetMapping("/boss/list")
    ResponsePacket<PagingResponse<PVPFreeOrderListVO>> selectUserBossOrdersByPage(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit);

    /**
     * 查询老板订单列表
     */
    @GetMapping("/baoji/list")
    ResponsePacket<PagingResponse<PVPFreeOrderListVO>> selectUserBaojiOrdersByPage(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit);

    /**
     * 后台系统查询暴鸡订单
     */
    @PostMapping("/baoji/backup/list")
    ResponsePacket<PagingResponse<PVPFreeBossOrderForBackGroundVO>> selectPvpFreeBaojiOrderForBackGroundVO(
            @RequestBody
                    PVPFreeOrdersForBackGroundParams params);

    /**
     * 后台系统查询老板订单
     */
    @PostMapping("/boss/backup/list")
    ResponsePacket<PagingResponse<PVPFreeBossOrderForBackGroundVO>> selectPvpFreeBossOrderForBackGroundVO(
            @RequestBody PVPFreeOrdersForBackGroundParams params);

    /**
     * 后管系统该暴鸡订单给从老板处获得的收益明细
     */
    @GetMapping("/baoji/backup/income")
    ResponsePacket<List<PVPFreeOrderIncomeForBackGroundVO>> selectPVPFreeBaojiFromBossIncome(
            @RequestParam(value = "sequence", required = true) String sequence);

    /**
     * 后管系统该老板订单给与暴鸡收益明细
     */
    @GetMapping("/boss/backup/income")
    ResponsePacket<List<PVPFreeOrderIncomeForBackGroundVO>> selectPVPFreeBossGiveBaojiIncome(
            @RequestParam(value = "sequence", required = true) String sequence);

}
