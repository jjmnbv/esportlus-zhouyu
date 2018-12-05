package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.payment.api.enums.WithdrawStatusType;
import com.kaihei.esportingplus.payment.api.feign.WithdrawServiceClient;
import com.kaihei.esportingplus.payment.api.params.StarlightExchangeParams;
import com.kaihei.esportingplus.payment.api.params.WithdrawAuditParams;
import com.kaihei.esportingplus.payment.api.params.WithdrawCreateParams;
import com.kaihei.esportingplus.payment.api.params.WithdrawUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.PageInfo;
import com.kaihei.esportingplus.payment.api.vo.UserBalanceListVO;
import com.kaihei.esportingplus.payment.api.vo.WithdrawAuditListVo;
import com.kaihei.esportingplus.payment.api.vo.WithdrawConfigVo;
import com.kaihei.esportingplus.payment.domain.entity.WithdrawOrder;
import com.kaihei.esportingplus.payment.service.WithdrawService;
import com.kaihei.esportingplus.payment.util.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 暴击值兑换
 *
 * @author xusisi
 * @update: 2018-09-23 11:13 update by chenzhenjun
 */
@RestController
@Api(tags = {"提现兑换相关API"})
@RequestMapping("/withdraw")
public class WithdrawController implements WithdrawServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawController.class);

    @Autowired
    private WithdrawService withdrawService;

    @Override
    @ApiOperation(value = "创建提现订单，返回订单ID")
    public ResponsePacket<Map<String, String>> createWithdrawOrder(@PathVariable("money_type") String moneyType,
            @RequestBody WithdrawCreateParams withdrawCreateParams) {
        Map<String, String> orderMap = null;
        logger.debug("createWithdrawOrder >> 入参 >> " + withdrawCreateParams.toString());
        try {
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, withdrawCreateParams);
            orderMap = withdrawService.checkAndCreateWithdrawOrder(withdrawCreateParams);
            logger.debug("createWithdrawOrder >> 出参 >> " + orderMap);
        } catch (BusinessException e) {
            logger.error("createWithdrawOrder >> Exception :  " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }
        return ResponsePacket.onSuccess(orderMap);
    }

    @Override
    @ApiOperation(value = "修改提现订单状态")
    public ResponsePacket<Map<String, String>> updateWithdrawStatus(@PathVariable("money_type") String moneyType, @RequestBody WithdrawUpdateParams withdrawUpdateParams) {
        Map<String, String> updateMap = null;
        try {
            logger.debug("updateWithdrawStatus >> 入参 >> " + withdrawUpdateParams.toString());
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, withdrawUpdateParams);
            updateMap = withdrawService.updateWithdrawStatus(withdrawUpdateParams);

            logger.debug("updateWithdrawStatus >> 出参 >> success ");

        } catch (BusinessException e) {
            logger.error("updateWithdrawStatus >> Exception : " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(updateMap);
    }

    @Override
    @ApiOperation(value = "查询工作室暴鸡的暴击值")
    public ResponsePacket getStarLightValues(@PathVariable("money_type") String moneyType, @RequestParam(value = "userIds", required = true) String userIds) {
        List<UserBalanceListVO> voList = null;
        try {
            logger.debug("getStarLightValues >> 入参 >> userIds ={} ", userIds);
            voList = withdrawService.getStarLightValues(userIds);
        } catch (BusinessException e) {
            logger.error("updateWithdrawStatus >> Exception : " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }
        return ResponsePacket.onSuccess(voList);
    }

    @Override
    @ApiOperation(value = "暴击值兑换暴鸡币")
    public ResponsePacket<Map<String, String>> convertStarlightToGCoin(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(value = "amount", required = true) String amount,
            @RequestParam(value = "source_id", required = true) String sourceId,
            @RequestParam(value = "currency_type", required = true) String currencyType) {
        long start = System.currentTimeMillis();
        Map<String, String> exchangeMap = null;
        StarlightExchangeParams starlightExchangeParams = new StarlightExchangeParams();
        starlightExchangeParams.setUserId(String.valueOf(UserSessionContext.getUser().getUid()));
        starlightExchangeParams.setPythonToken(String.valueOf(UserSessionContext.getUser().getPythonToken()));
        starlightExchangeParams.setAmount(Integer.valueOf(amount));
        starlightExchangeParams.setSourceId(sourceId);
        logger.debug("convertStarlightToGCoin >> 入参 >> " + starlightExchangeParams.toString());
        logger.debug("convertStarlightToGCoin >> 通过token获取用户信息 >>" + UserSessionContext.getUser().toString());
        try {
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, starlightExchangeParams);
            exchangeMap = withdrawService.convertStarlightToGCoin(starlightExchangeParams);

            String orderId = exchangeMap.get("order_id");
            String userId = starlightExchangeParams.getUserId();

            ExecutorService executorService = Executors.newCachedThreadPool();
            ExchangeStateAsync task = new ExchangeStateAsync(orderId, userId);
            Future<String> future = executorService.submit(task);
            executorService.shutdown();

//            Thread.sleep(600);
            String state =  future.get();
            logger.debug("ExchangeStateAsync.call() >> 出参 >> " + state);
            exchangeMap.put("state", state);

            logger.debug("convertStarlightToGCoin >> 出参 >> " + exchangeMap);
            logger.info(">> convertStarlightToGCoin cost "+(System.currentTimeMillis()-start)+" ms");
        } catch (BusinessException e) {
            logger.error("convertStarlightToGCoin >> Exception :  " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        } catch(InterruptedException ex) {
            logger.error("convertStarlightToGCoin >> Exception :  " + ex.getMessage());
            return ResponsePacket.onError(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrCode(),
                    BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg());
        } catch (ExecutionException ex) {
            logger.error("convertStarlightToGCoin >> Exception :  " + ex.getMessage());
            return ResponsePacket.onError(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrCode(),
                    BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg());
        }
        return ResponsePacket.onSuccess(exchangeMap);
    }

    @Override
    @ApiOperation(value = "查询提现订单状态")
    public ResponsePacket getWithdrawStatus(@PathVariable("out_trade_no") String outTradeNo,
            @RequestParam(value = "user_id", required = false) String userId,
            @RequestParam(value = "order_id", required = false) String orderId) {
        WithdrawOrder withdrawOrder = null;
        try {
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, outTradeNo);
            logger.debug("getWithdrawStatus >> 入参 >> outTradeNo ={}", outTradeNo);
            withdrawOrder = withdrawService.getWithdrawStateInfoWithAllParams(outTradeNo, orderId, userId);
            if (null == withdrawOrder) {
                return ResponsePacket.onError(BizExceptionEnum.ORDER_NOT_EXIST.getErrCode(),
                        BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg());
            }
        } catch (BusinessException e) {
            logger.error("getWithdrawStatus >> Exception : " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }
        return ResponsePacket.onSuccess(withdrawOrder);
    }

    @Override
    @ApiOperation(value = "查询兑换订单状态")
    public ResponsePacket getExchangeStatus(@PathVariable("order_id") String orderId, @RequestParam(value = "user_id", required = true) String userId) {
        WithdrawOrder withdrawOrder = null;
        try {
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, orderId, userId);
            logger.debug("getExchangeStatus >> 入参 >> orderId ={}, userId={} ", orderId, userId);
            withdrawOrder = withdrawService.getExchangeOrderInfo(orderId, userId);
        } catch (BusinessException e) {
            logger.error("getExchangeStatus >> Exception : " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }
        return ResponsePacket.onSuccess(withdrawOrder);
    }

    @Override
    @ApiOperation(value = "积分兑换暴击值")
    public ResponsePacket convertScoreToStarlight(@RequestParam(value = "user_id", required = true) String userId,
            @RequestParam(value = "amount", required = true) int amount,
            @RequestParam(value = "out_trade_no", required = true) String outTradeNo) {
        try {
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userId, amount);
            withdrawService.convertScoreToStarlight(userId,amount, outTradeNo);

        } catch (BusinessException e) {
            logger.error("convertScoreToStarlight >> Exception : " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess();
    }

    /**
     * 异步获取状态信息
     */
    class ExchangeStateAsync implements Callable<String> {

        private String orderId;
        private String userId;

        public ExchangeStateAsync(String orderId, String userId) {
            this.orderId = orderId;
            this.userId = userId;
        }

        @Override
        public String call() throws Exception {
            logger.debug(">>>>>>>> ExchangeStateAsync >> 入参 >> orderId ={}, userId={} ", orderId, userId);
            while (true) {
                WithdrawOrder order = withdrawService.getExchangeOrderInfo(orderId, userId);
                if (WithdrawStatusType.SUCCESS.getCode().equals(order.getState())) {
                    return WithdrawStatusType.SUCCESS.getCode();
                }
            }
        }

    }

    @Override
    public ResponsePacket<Map<String, String>> createWithdrawAuditOrder(@RequestHeader(name = "Authorization") String token,
                                                   @RequestParam(value = "amount", required = true) Integer amount,
                                                   @RequestParam(value = "app_id", required = true) String appId,
                                                   @RequestParam(value = "transfer_type", required = true) Integer transferType,
                                                    HttpServletRequest httpServletRequest) {
        WithdrawAuditParams auditParams = new WithdrawAuditParams();
        auditParams.setUserId(Integer.valueOf(UserSessionContext.getUser().getId()));
        auditParams.setUid(UserSessionContext.getUser().getUid());
        auditParams.setAmount(amount);
        auditParams.setAppId(appId);
        auditParams.setTransferType(transferType);
        String ip = IpUtils.getIp(httpServletRequest);
        auditParams.setClientIp(ip);

        return ResponsePacket.onSuccess(withdrawService.createWithdrawAuditOrder(auditParams));
    }

    @Override
    public ResponsePacket<PageInfo<WithdrawAuditListVo>> getAuditListByApp(@RequestHeader(name = "Authorization") String token,
                                                                           @RequestParam(value = "page", defaultValue = "1") String page,
                                                                           @RequestParam(value = "size", defaultValue = "20") String size) {
        String uid = UserSessionContext.getUser().getUid();
        return ResponsePacket.onSuccess(withdrawService.getAuditListByApp(uid, page, size));
    }

    @Override
    public ResponsePacket<PageInfo<WithdrawAuditListVo>> getAuditListByBackend(@RequestParam(value = "uid", required = false) String uid,
                                                                               @RequestParam(value = "verify_state", required = false) String verifyState,
                                                                               @RequestParam(value = "order_id", required = false) String orderId,
                                                                               @RequestParam(value = "block_state", required = false) String blockState,
                                                                               @RequestParam(value = "page", defaultValue = "1") String page,
                                                                               @RequestParam(value = "size", defaultValue = "20") String size) {
        WithdrawAuditListVo queryVo = new WithdrawAuditListVo();
        queryVo.setUid(uid);
        queryVo.setVerifyState(verifyState);
        queryVo.setOrderId(orderId);
        queryVo.setBlockState(blockState);

        return ResponsePacket.onSuccess(withdrawService.getAuditListByBackend(queryVo, page, size));
    }

    @Override
    public ResponsePacket updateAuditState(@RequestBody WithdrawAuditListVo queryVo) {
        withdrawService.updateAuditState(queryVo);
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket updateBlockState(@RequestBody WithdrawAuditListVo queryVo) {
        withdrawService.updateBlockState(queryVo);
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket<WithdrawConfigVo> getWithdrawConfigVo() {
        return ResponsePacket.onSuccess(withdrawService.getWithdrawConfigVo());
    }

    @Override
    public ResponsePacket updateWithdrawConfig(WithdrawConfigVo configVo) {
        withdrawService.updateWithdrawConfig(configVo);
        return ResponsePacket.onSuccess();
    }
}