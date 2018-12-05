package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.feign.GCoinRechargeServiceClient;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinRechargeUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargePreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;
import com.kaihei.esportingplus.payment.service.GCoinRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
public class GCoinRechargeController implements GCoinRechargeServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(GCoinRechargeController.class);

    @Autowired
    private GCoinRechargeService gcoinRechargeService;

    /***
     * 1、Python发出请求，根据参数预生成一个订单信息，返回Python订单ID信息；
     * 2、Python带着订单ID与充值结果请求，JAVA更新订单状态等信息数据，将操作结果返回Python进行验证。
     */
    @Override
    @ApiOperation(value = "创建充值订单，返回订单ID")
    public ResponsePacket<GCoinRechargePreVo> createOrderInfo(@RequestBody GCoinRechargeCreateParams rechargeCreateParams) {

        GCoinRechargePreVo vo = null;
        try {
            logger.debug("createOrderInfo >> 入参 >> rechargeCreateParams : {}", rechargeCreateParams);
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, rechargeCreateParams);
            vo = gcoinRechargeService.preCreatePayment(rechargeCreateParams);
            logger.debug("createOrderInfo >> 出参 >> vo : {} ", vo);
        } catch (BusinessException e) {
            logger.error("createOrderInfo >> Exception :  " + e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(vo);
    }

    @Override
    @ApiOperation(value = "更新充值订单")
    public ResponsePacket<GCoinRechargeVo> updateOrderInfo(@PathVariable String orderId, @RequestBody GCoinRechargeUpdateParams rechargeUpdateParams) {
        rechargeUpdateParams.setOrderId(orderId);
        GCoinRechargeVo vo = null;
        try {
            logger.debug("updateOrderInfo >> 入参 >> rechargeUpdateParams : {} ", rechargeUpdateParams);

            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, rechargeUpdateParams);
            vo = gcoinRechargeService.updateOrderInfo(orderId, rechargeUpdateParams);

            logger.debug("updateOrderInfo >> 出参 >> success ");

        } catch (BusinessException e) {
            logger.error("updateOrderInfo >> Exception : {} ", e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(vo);
    }

    @Override
    @ApiOperation(value = "获取充值订单详情")
    public ResponsePacket<GCoinRechargeVo> getRechargeDetail(@PathVariable String orderId, @RequestParam String userId) {
        logger.debug("getOrderDetail >> 入参 >> orderId " + orderId + ", userId " + userId);

        GCoinRechargeVo vo = gcoinRechargeService.getRechargeVo(orderId, userId);

        logger.debug("getOrderDetail >> 出参 >> gcoinRecharge : {} ", vo);

        if (vo == null) {
            logger.error("getOrderDetail >> Exception : {} " + BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg());
            return ResponsePacket.onError(BizExceptionEnum.ORDER_NOT_EXIST);
        }
        return ResponsePacket.onSuccess(vo);
    }
}

