package com.kaihei.esportingplus.trade.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.payment.api.enums.ExternalPayStateEnum;
import com.kaihei.esportingplus.payment.api.enums.ExternalRefundStateEnum;
import com.kaihei.esportingplus.payment.api.vo.ExternalPaymentOrderVo;
import com.kaihei.esportingplus.payment.api.vo.ExternalRefundOrderVo;
import com.kaihei.esportingplus.trade.api.enums.OrderStatusEnum;
import com.kaihei.esportingplus.trade.api.feign.PayClient;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.service.RPGOrderService;
import com.kaihei.esportingplus.trade.event.PayNofityEvent;
import com.kaihei.esportingplus.trade.event.RefundNofityEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单
 *
 * @author liangyi
 */
@RestController
@RequestMapping("/payment")
@Api(tags = {"支付&退款响应回调服务接口"})
public class PayController implements PayClient {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Value("${pay.notifyUrl}")
    private String payNofity;
    @Value("${refund.notifyUrl}")
    private String refundNofity;

    @Autowired
    private RPGOrderService rpgOrderService;

    @ApiOperation(value = "支付回调通知接口")
    @Override
    @PostMapping("/payNofity")
    public ResponsePacket payNofity(@RequestBody ExternalPaymentOrderVo payPacket){
        if(!payPacket.getState().equals(ExternalPayStateEnum.SUCCESS.getCode())){
            LOGGER.error("支付回调状态不是：SUCCESS，忽略处理({})",
                    ExternalPayStateEnum.valueOf(payPacket.getState()).getMsg());
            return ResponsePacket.onSuccess("支付回调状态不是：SUCCESS，忽略处理");
        }
        LOGGER.info("收到支付结果回调通知: {}",payPacket);

        Date responseTime = DateUtil.str2Date(payPacket.getPaiedTime(), DateUtil.SIMPLE_FORMATTER);
        Order order = new Order();
        order.setSequeue(payPacket.getOutTradeNo());
        order.setOuterTradeNo(payPacket.getOrderId());//支付服务的订单号
        order.setActualPaidAmount(payPacket.getTotalFee());//实际支付金额
        order.setGmtModified(new Date());//订单修改时间
        order.setResponseTime(responseTime); //订单响应时间
        order.setPaymentTime(new Date());//支付成功时间

        //异步更新
        EventBus.post(new PayNofityEvent(order));
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "退款回调通知接口")
    @PostMapping("/refundNofity")
    public ResponsePacket refundNofity(@RequestBody ExternalRefundOrderVo refundPacket){
        if(!refundPacket.getState().equals(ExternalRefundStateEnum.SUCCESS.getCode())){
            LOGGER.error("退款回调状态不是：SUCCESS，忽略处理({})",
                    ExternalPayStateEnum.valueOf(refundPacket.getState()).getMsg());
            return ResponsePacket.onSuccess("退款回调状态不是：SUCCESS，忽略处理");
        }
        LOGGER.info("收到退款结果回调通知: {}", refundPacket);

        Order refundOrder = new Order();

        refundOrder.setSequeue(refundPacket.getOutTradeNo());
        refundOrder.setActualRefundAmount(refundPacket.getTotalFee());//实际退款金额
        refundOrder.setGmtModified(new Date());//订单修改时间
        refundOrder.setStatus((byte) OrderStatusEnum.PAY_CANCEL.getCode());//订单状态
        Date responseTime = DateUtil.str2Date(refundPacket.getRefundTime(), DateUtil.SIMPLE_FORMATTER);
        refundOrder.setCloseTime(responseTime);

        //异步更新
        EventBus.post(new RefundNofityEvent(refundOrder));
        return ResponsePacket.onSuccess();
    }

}
