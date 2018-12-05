package com.kaihei.esportingplus.trade.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.trade.api.enums.OrderStatusEnum;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinPayConfirmPacket;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinPayPacket;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinRefundPacket;
import com.kaihei.esportingplus.trade.api.vo.PrepayVo;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.service.RPGOrderService;
import com.kaihei.esportingplus.trade.event.PythonPayConfirmNofityEvent;
import com.kaihei.esportingplus.trade.event.PythonPayNofityEvent;
import com.kaihei.esportingplus.trade.event.PythonRefundNofityEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/pay")
@Api(tags = {"支付退款响应服务接口"})
public class PythonPayController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Value("${python2java.payNofity}")
    private String payNofity;
    @Value("${python2java.payConfirmNofity}")
    private String payConfirmNofity;
    @Value("${python2java.refundNofity}")
    private String refundNofity;

    @Autowired
    private RPGOrderService rpgOrderService;

    @ApiOperation(value = "支付回调通知接口")
    @PostMapping("/payNofity")
    public ResponsePacket payNofity(@RequestBody WeiXinPayPacket weiXinPayPacket){
        if(weiXinPayPacket.getResult_code().equals(CommonConstants.WEIXIN_PAY_FAIL)){
            LOGGER.error(weiXinPayPacket.getReturn_code());
        }
        LOGGER.info("收到支付结果回调通知: {}",weiXinPayPacket);
        Order order = rpgOrderService
                .getBySequenceId(weiXinPayPacket.getOut_trade_no());
        if (order == null) {
            LOGGER.error(BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg() + ",微信单号={}，orderId={}",
                    weiXinPayPacket.getTransaction_id(),weiXinPayPacket.getOut_trade_no());
            return ResponsePacket.onError(BizExceptionEnum.ORDER_NOT_EXIST);
        }

        //如果订单状态在车队服务确认支付的时候已更新，则直接返回
        if(order.getStatus().equals(OrderStatusEnum.PAYED.getCode())){
            LOGGER.info("订单状态在车队服务确认支付的时候已更新为已支付，则直接返回。");
            return ResponsePacket.onSuccess();
        }

        Date responseTime = DateUtil.str2Date(weiXinPayPacket.getTime_end(), DateUtil.SIMPLE_FORMATTER);
        order.setOuterTradeNo(weiXinPayPacket.getTransaction_id());//第三方订单号
        order.setActualPaidAmount(weiXinPayPacket.getTotal_fee());//实际支付金额
        order.setGmtModified(new Date());//订单修改时间
        order.setResponseTime(responseTime); //订单响应时间
        order.setPaymentTime(new Date());//支付成功时间

        //异步更新
        EventBus.post(new PythonPayNofityEvent(order,weiXinPayPacket.getReturn_code()));
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "确认支付回调通知接口")
    @PostMapping("/payConfirmNofity")
    public ResponsePacket payConfirmNofity(@RequestBody WeiXinPayConfirmPacket weiXinPayConfirmPacket){
        if(weiXinPayConfirmPacket.getReturn_code().equals(CommonConstants.WEIXIN_PAY_FAIL)){
            LOGGER.error(weiXinPayConfirmPacket.getReturn_msg());
        }
        LOGGER.info("收到确认支付回调通知: {}",weiXinPayConfirmPacket);
        //异步更新
        EventBus.post(new PythonPayConfirmNofityEvent(weiXinPayConfirmPacket));
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "退款回调通知接口")
    @PostMapping("/refundNofity")
    public ResponsePacket refundNofity(@RequestBody WeiXinRefundPacket weiXinRefundPacket){
        if(weiXinRefundPacket.getReturn_code().equals(CommonConstants.WEIXIN_PAY_FAIL)){
            LOGGER.error(weiXinRefundPacket.getReturn_msg());
        }
        LOGGER.info("收到退款结果回调通知: {}", weiXinRefundPacket);
        //异步更新
        EventBus.post(new PythonRefundNofityEvent(weiXinRefundPacket));
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "预支付信息")
    @GetMapping("/prepayInfo/{order_id}")
    public ResponsePacket<PrepayVo> prepayInfo(@PathVariable("order_id") String orderId){
        Order order = rpgOrderService.getBySequenceId(orderId);
        if(order == null){
            return ResponsePacket.onError(BizExceptionEnum.ORDER_NOT_EXIST);
        }
        PrepayVo prepayVo = new PrepayVo();
        prepayVo.setFee(order.getPrepaidAmount() - order.getDiscountAmount());
        prepayVo.setOrderId(order.getSequeue());
        prepayVo.setPayNofityUrl(payNofity);
        prepayVo.setPayConfirmNofityUrl(payConfirmNofity);
        prepayVo.setRefundNofityUrl(refundNofity);
        return ResponsePacket.onSuccess(prepayVo);
    }

}
