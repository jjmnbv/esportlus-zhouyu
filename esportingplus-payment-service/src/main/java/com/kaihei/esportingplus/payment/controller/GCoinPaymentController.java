package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.feign.GCoinPaymentServiceClient;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentCreateParams;
import com.kaihei.esportingplus.payment.api.params.GCoinPaymentUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentPreVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinPaymentVo;
import com.kaihei.esportingplus.payment.service.GCoinPaymentService;
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
 * 暴鸡币支付
 *
 * @author xusisi
 * @create 2018-09-22 21:07
 **/
@RestController
@Api(tags = {"暴鸡币支付相关API"})
@RequestMapping("/gcoin")
public class GCoinPaymentController implements GCoinPaymentServiceClient {

    private static Logger logger = LoggerFactory.getLogger(GCoinPaymentController.class);

    @Autowired
    private GCoinPaymentService gcoinPaymentService;

    /***
     * 生成暴鸡币支付订单信息
     */
    @Override
    @ApiOperation(value = "创建支付订单")
    public ResponsePacket<GCoinPaymentPreVo> createPrePaymentInfo(@RequestBody GCoinPaymentCreateParams gcoinPaymentCreateParams) {
        GCoinPaymentPreVo gcoinPaymentPreVo = null;
        try {
            logger.info("createPrePaymentInfo >> start >> gcoinPaymentCreateParams : {} ", gcoinPaymentCreateParams);
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, gcoinPaymentCreateParams);
            gcoinPaymentPreVo = gcoinPaymentService.preCreatePayment(gcoinPaymentCreateParams);
            logger.info("createPrePaymentInfo >> end >> response : {} ", gcoinPaymentPreVo);

        } catch (BusinessException e) {
            logger.error("createPrePaymentInfo >> exception >> {} ", e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(gcoinPaymentPreVo);
    }

    /***
     * 生成暴鸡币支付订单信息
     */
    @Override
    @ApiOperation(value = "更新支付订单")
    public ResponsePacket<GCoinPaymentVo> updatePaymentInfo(@PathVariable String orderId, @RequestBody GCoinPaymentUpdateParams gcoinPaymentUpdateParams) {
        GCoinPaymentVo vo = null;
        gcoinPaymentUpdateParams.setOrderId(orderId);
        try {
            logger.info("updatePaymentInfo >> start >> gcoinPaymentUpdateParams : {} ", gcoinPaymentUpdateParams);
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, gcoinPaymentUpdateParams);
            vo = gcoinPaymentService.updateOrderInfo(gcoinPaymentUpdateParams);
            logger.info("updatePaymentInfo >> end >> GCoinPaymentVo : {} ", vo);

        } catch (BusinessException e) {
            logger.error("createPrePaymentInfo >> exception >> {} ", e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(vo);
    }

    /***
     * 更新暴鸡币支付订单信息
     */
    @Override
    @ApiOperation(value = "获取充值订单详情")
    public ResponsePacket<GCoinPaymentVo> getPaymentInfo(@RequestParam(required = false) String orderType, @RequestParam(required = false) String orderId, @RequestParam(required = false) String outTradeNo) {

        GCoinPaymentVo vo = null;
        try {
            logger.info("getPaymentInfo >> start >> orderType : {} ,orderId : {} ,outTradeNo : {} ", orderType, orderId, outTradeNo);
            vo = gcoinPaymentService.getVo(orderId, orderType, outTradeNo);
            logger.info("getPaymentInfo >> end >> response : {} ");
        } catch (BusinessException e) {
            logger.error("createPrePaymentInfo >> exception >> {} ", e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        return ResponsePacket.onSuccess(vo);
    }
}
