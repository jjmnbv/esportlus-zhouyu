package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.feign.BackStageServiceClient;
import com.kaihei.esportingplus.payment.api.params.DeductParams;
import com.kaihei.esportingplus.payment.api.params.ExchangeQueryParams;
import com.kaihei.esportingplus.payment.api.params.GCoinBackStageRechargeParam;
import com.kaihei.esportingplus.payment.api.vo.DeductOrderVo;
import com.kaihei.esportingplus.payment.api.vo.FrontGCcoinRechargeVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;
import com.kaihei.esportingplus.payment.service.BackStagePaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: esportingplus
 * @description: 后台支付相关API
 * @author: xusisi
 * @create: 2018-10-09 17:11
 **/
@RestController
@Api(tags = {"后台支付相关API"})
@RequestMapping("/back_stage")
public class BackStagePaymentController implements BackStageServiceClient {

    private static Logger logger = LoggerFactory.getLogger(BackStagePaymentController.class);

    @Autowired
    private BackStagePaymentService backStagePaymentService;

    @Override
    @ApiOperation(value = "创建暴鸡币充值,返回充值详情结果")
    public ResponsePacket<GCoinRechargeVo> createGcoinRecharge(@RequestBody GCoinBackStageRechargeParam gCoinBackStageRechargeParam) throws Exception {
        GCoinRechargeVo vo = null;

        try {
            logger.debug("createGcoinRecharge >> gCoinBackStageRechargeParam : {}", gCoinBackStageRechargeParam);

            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, gCoinBackStageRechargeParam);

            vo = backStagePaymentService.createGcoinRecharge(gCoinBackStageRechargeParam);

            logger.debug("createGcoinRecharge >> vo : {} ", vo);

        } catch (BusinessException e) {
            logger.error("createGcoinRecharge >> exception : {} ", e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(vo);
    }

    @Override
    @ApiOperation(value = "创建后台扣款,返回扣款详情结果")
    public ResponsePacket<DeductOrderVo> createDeductOrder(@RequestBody DeductParams deductParams) throws Exception {
        DeductOrderVo vo = null;
        try {
            logger.debug("createDeductOrder >> deductParams : {}", deductParams);

            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, deductParams);

            vo = backStagePaymentService.createDeductOrder(deductParams);

            logger.debug("createDeductOrder >> vo : {} ", vo);

        } catch (BusinessException e) {
            logger.error("createDeductOrder >> exception : {} ", e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(vo);

    }


    @Override
    @ApiOperation(value = "查看用户前台充值记录")
    public ResponsePacket<FrontGCcoinRechargeVo> getGCoinRechargeList(@RequestParam(value = "userId", required = false) String userId,
                                                                      @RequestParam(value = "channel", required = false) String channel,
                                                                      @RequestParam(value = "sourceId", required = false) String sourceId,
                                                                      @RequestParam(value = "beginDate", required = false) String beginDate,
                                                                      @RequestParam(value = "endDate", required = false) String endDate,
                                                                      @RequestParam(value = "page", defaultValue = "1") String page,
                                                                      @RequestParam(value = "size", defaultValue = "20") String size) throws Exception {

        FrontGCcoinRechargeVo vo = null;
        try {
            logger.debug("getGCoinRechargeList >> params >> userId :{}, channel:{}, sourceId:{}, beginDate:{}, endDate:{}, page:{}, size:{}", userId, channel, sourceId, beginDate, endDate, page, size);

            vo = backStagePaymentService.getGCoinRechargeList(userId, channel, sourceId, beginDate, endDate, page, size);
            logger.debug("vo : {} ", vo);

        } catch (BusinessException e) {
            logger.error("createDeductOrder >> exception : {} ", e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(vo);

    }

    @Override
    @ApiOperation(value = "查看暴击值兑换记录")
    public ResponsePacket<Map<String, Object>> getStarlightExchangeList(@RequestParam(value = "uid", required = false) String uid,
                                                                        @RequestParam(value = "begin_date", required = false) String beginDate,
                                                                        @RequestParam(value = "end_date", required = false) String endDate,
                                                                        @RequestParam(value = "page", defaultValue = "1") String page,
                                                                        @RequestParam(value = "size", defaultValue = "20") String size) throws Exception {
        logger.debug("getGCoinRechargeList >> params >> uid :{}, beginDate:{}, endDate:{}, page:{}, size:{}", uid, beginDate, endDate, page, size);
        ExchangeQueryParams queryParams = new ExchangeQueryParams();
        queryParams.setUid(uid);
        queryParams.setBeginDate(beginDate);
        queryParams.setEndDate(endDate);
        queryParams.setPage(page);
        queryParams.setSize(size);
        Map<String, Object> restultMap = new HashMap<>();
        try {
            logger.debug("getStarlightExchangeList >> 入参 >> queryParams ={}  ", queryParams);
            restultMap = backStagePaymentService.getStarlightExchangeList(queryParams);
        } catch (BusinessException e) {
            logger.error("getStarlightExchangeList >> Exception : " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }
        return ResponsePacket.onSuccess(restultMap);
    }
}
