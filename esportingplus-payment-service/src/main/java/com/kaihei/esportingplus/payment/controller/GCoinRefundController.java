package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.feign.GCoinRefundServiceClient;
import com.kaihei.esportingplus.payment.api.params.GCoinRefundParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRefundVo;
import com.kaihei.esportingplus.payment.service.GCoinRefundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 暴鸡币充值
 *
 * @author xusisi
 */
@RestController
@Api(tags = {"暴鸡币充值相关API"})
@RequestMapping("/gcoin")
public class GCoinRefundController implements GCoinRefundServiceClient {

    private Logger logger = LoggerFactory.getLogger(GCoinRefundController.class);

    @Autowired
    private GCoinRefundService gcoinRefundService;

    @Override
    @ApiOperation(value = "创建退款订单,返回退款订单ID")
    public ResponsePacket<GCoinRefundPreVo> createOrderInfo(@RequestBody GCoinRefundParams gcoinRefundParams) {
        GCoinRefundPreVo vo = null;
        try {
            logger.debug("createOrderInfo >> 入参 >> gcoinRefundParams : {}", gcoinRefundParams);
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, gcoinRefundParams);
            vo = gcoinRefundService.createRefund(gcoinRefundParams);
            logger.debug("createOrderInfo >> 出参 >> GCoinRefundVo : {} ", vo);
        } catch (BusinessException e) {
            logger.error("createOrderInfo >> Exception :  " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(vo);
    }

    @Override
    @ApiOperation(value = "获取充值订单详情")
    public ResponsePacket<GCoinRefundVo> getRefundInfo(@RequestParam(required = false) String orderId, @RequestParam(required = false) String orderType, @RequestParam(required = false) String outRefundNo) {

        logger.debug("getRefundInfo >> 入参 >> orderId : {} , orderType :{} ,outRefundNo:{}", orderId, orderType, outRefundNo);

        GCoinRefundVo vo = gcoinRefundService.getRefundVo(orderId, orderType, outRefundNo);

        logger.debug("getRefundInfo >> 出参 >> gcoinRecharge : {} ", vo);

        if (vo == null) {
            logger.error("getOrderDetail >> Exception : {} " + BizExceptionEnum.GCOIN_REFUND_SEARCH_ORDER_NOT_FOUND.getErrMsg());
            return ResponsePacket.onError(BizExceptionEnum.GCOIN_REFUND_SEARCH_ORDER_NOT_FOUND);
        }
        return ResponsePacket.onSuccess(vo);
    }
}

