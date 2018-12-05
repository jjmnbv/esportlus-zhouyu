package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.api.enums.MoneyTypeEnum;
import com.kaihei.esportingplus.payment.api.feign.WalletServiceClient;
import com.kaihei.esportingplus.payment.api.params.BillQueryParams;
import com.kaihei.esportingplus.payment.api.params.ConsumeGCoinParams;
import com.kaihei.esportingplus.payment.api.vo.WalletBillsVO;
import com.kaihei.esportingplus.payment.api.vo.WalletsVO;
import com.kaihei.esportingplus.payment.service.BillFlowService;
import com.kaihei.esportingplus.payment.service.GCoinRewardService;
import com.kaihei.esportingplus.payment.service.WalletService;
import com.kaihei.esportingplus.payment.util.SimpleDateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 暴鸡币钱包账户相关
 *
 * @author: xiaolijun
 */
@RestController
@Api(tags = {"暴鸡币钱包账户相关API"})
@RequestMapping("/wallets")
public class WalletController implements WalletServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private GCoinRewardService gCoinService;
    @Autowired
    private BillFlowService billFlowService;
    @Autowired
    private WalletService walletService;

    @ApiOperation(value = "用户消费暴鸡币进行打赏接口")
    @Override
    public ResponsePacket consumeGCoin(@PathVariable("user_id") String userId,
            @PathVariable("order_id") String orderId) {
        logger.debug("打印消费暴鸡币兑换暴击值服务接口-入参信息：" + orderId);
        gCoinService.consumeGCoin(orderId);
        logger.debug("打印消费暴鸡币兑换暴击值服务接口-出参信息：void");
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "创建打赏预支付服务接口")
    @Override
    public ResponsePacket<Map<String, String>> createRewardOrder(
            @PathVariable("user_id") String userId,
            @RequestBody ConsumeGCoinParams consumeGCoinParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, consumeGCoinParams);
        logger.debug("打印暴鸡币订单创建服务接口-入参信息：" + consumeGCoinParams.toString());
        Map<String, String> respMap = new HashMap<String, String>();
        String order_id = gCoinService.gCoinCreateOrder(consumeGCoinParams);
        respMap.put("order_id", order_id);
        logger.debug("打印暴鸡币订单创建服务接口-出参信息：" + order_id);
        if (StringUtils.isEmpty(order_id)) {
            return ResponsePacket.onError(BizExceptionEnum.USER_GCOIN_NOT_ENOUGH);
        } else {
            return ResponsePacket.onSuccess(respMap);
        }
    }

    @ApiOperation(value = "获取用户钱包信息服务接口")
    @Override
    public ResponsePacket<WalletsVO> getBalance(@PathVariable("user_id") String userId) {
        logger.debug("打印获取用户钱包信息服务接口-入参信息：userId={}", userId);
        WalletsVO respJsonObject = walletService.getBalance(userId);
        logger.debug("打印获取用户钱包信息服务接口-出参信息：" + respJsonObject);
        return ResponsePacket.onSuccess(respJsonObject);
    }

    @ApiOperation(value = "获取用户钱包信息服务接口")
    @Override
    public ResponsePacket<WalletsVO> getBalanceByApp(@RequestParam(value = "user_id", required = true) String userId) {
        logger.debug("打印获取用户钱包信息服务接口-入参信息：userId={}", userId);
        WalletsVO respJsonObject = walletService.getBalance(userId);
        logger.debug("打印获取用户钱包信息服务接口-出参信息：" + respJsonObject);
        return ResponsePacket.onSuccess(respJsonObject);
    }

    @ApiOperation(value = "获取用户钱包流水信息接口")
    @Override
    public ResponsePacket<List<WalletBillsVO>> getBills(@PathVariable("user_id") String userId,
            @RequestParam(value = "moneyType") String moneyType,
            @RequestParam(value = "page") String page,
            @RequestParam(value = "size") String size,
            @RequestParam(value = "paymentChannel", required = false) String paymentChannel,
            @RequestParam(value = "orderDimension", required = false) String orderDimension,
            @RequestParam(value = "orderType", required = false) String orderType) {

        BillQueryParams billQueryParams = new BillQueryParams();
        billQueryParams.setMoneyType(moneyType);
        billQueryParams.setOrderDimensionality(orderDimension);
        billQueryParams.setOrderType(orderType);
        billQueryParams.setPage(page);
        billQueryParams.setChannel(paymentChannel);
        billQueryParams.setSize(size);
        billQueryParams.setUserId(userId);

        logger.debug("获取用户钱包流水信息接口-入参信息：" + billQueryParams.toString());
        List<WalletBillsVO> data = null;
        if (MoneyTypeEnum.GCOIN_MONEY.getCode().equals(billQueryParams.getMoneyType())) {
            //暴鸡币
            data = billFlowService.getGCoinBill(billQueryParams);
        } else if (MoneyTypeEnum.STARLIGHT_MONEY.getCode().equals(billQueryParams.getMoneyType())) {
            //暴击值
            data = billFlowService.getStarlightBill(billQueryParams);
        } else {
            logger.debug(BizExceptionEnum.USER_WALLETS_TYPE_EMPTY.getErrMsg());
            throw new BusinessException(BizExceptionEnum.USER_WALLETS_TYPE_EMPTY);
        }
        logger.debug("获取用户钱包流水信息接口-出参信息：" + data);
        return ResponsePacket.onSuccess(data);
    }

    @ApiOperation(value = "获取用户累计暴击值")
    @Override
    public ResponsePacket<Map<String, Integer>> getStarlight(@PathVariable("user_id") String userId, @RequestParam("tradeType") String tradeType) {

        logger.debug("获取用户累计暴击值-入参信息：userId={} tradeType={}", userId, tradeType);

        Integer allAmount = billFlowService.getAllStarlight(userId, tradeType);
        Integer monthAmount = billFlowService
                .getAllStarlight(userId, tradeType, SimpleDateUtils.getBeginDayOfMonth(), SimpleDateUtils.getEndDayOfMonth());
        Integer dayAmount = billFlowService.getAllStarlight(userId, tradeType, SimpleDateUtils.getDayBegin(), SimpleDateUtils.getDayEnd());

        Map<String, Integer> data = new HashMap<>(3);
        data.put("allAmount", allAmount);
        data.put("monthAmount", monthAmount);
        data.put("dayAmount", dayAmount);
        logger.debug("获取用户累计暴击值-出参信息：" + data);
        return ResponsePacket.onSuccess(data);
    }



}