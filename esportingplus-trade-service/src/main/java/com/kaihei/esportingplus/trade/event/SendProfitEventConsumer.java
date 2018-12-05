package com.kaihei.esportingplus.trade.event;

import com.alibaba.fastjson.TypeReference;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.payment.api.feign.OrderIncomeServiceClient;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;
import com.kaihei.esportingplus.payment.api.vo.InComeBenefitJaVoJa;
import com.kaihei.esportingplus.trade.data.repository.OrderItemTeamRPGRepository;
import com.kaihei.esportingplus.trade.data.repository.OrderRepository;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamRPG;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendProfitEventConsumer extends EventConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderIncomeServiceClient orderIncomeServiceClient;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemTeamRPGRepository orderItemTeamRepository;

    //订单类型:17 DNF单
    @Value("${pay.refund.param.order_type}")
    private int order_type;

    @Value("${pay.convert.baojiValue2RMBRate}")
    private int baojiValue2RMBRate;

    @Value("${pay.profit.retryInterval}")
    private String retryInterval;

    /**
     * 发送暴鸡收益到工作室
     * @param event
     */
    @Subscribe
    @AllowConcurrentEvents //开启线程安全
    public void sendProfit(SendProfitEvent event) {
        OrderIncomeParams orderIncomeParam = event.getOrderIncomeParams();
        orderIncomeParam.setOrderType(order_type);
        orderIncomeParam.setAttach(StringUtils.EMPTY);
        orderIncomeParam.setNotifyUrl(StringUtils.EMPTY);
        List<OrderIncomeParams> orderIncomeParams = new ArrayList<>();
        orderIncomeParams.add(orderIncomeParam);
        //重试
        long incr = 0;
        String[] intervals = retryInterval.split("/");
        do {
            ResponsePacket responsePacket = orderIncomeServiceClient
                    .insertIncomesJa(orderIncomeParams);
            if (responsePacket.getCode() == BizExceptionEnum.HYSTRIX_SERVER.getErrCode()) {
                LOGGER.error(responsePacket.getMsg());
                incr ++;
                try {
                    LOGGER.error("发送暴鸡[{}]收益[{}]到工作室失败，{}s后,重试：{}次",
                            orderIncomeParam.getUserId(),
                            orderIncomeParam.getAmount(),
                            intervals[(int)incr-1],
                            incr);
                    Thread.sleep(Long.valueOf(intervals[(int)incr-1]) * 1000);
                } catch (InterruptedException e) {
                    LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(),e);
                    break;
                }
            }else{
                LOGGER.info("发送暴鸡[{}]收益[{}]到工作室成功,结果:{}，参数：{}",orderIncomeParam.getUserId(),
                        orderIncomeParam.getAmount(),responsePacket,orderIncomeParams);


                if(responsePacket.getCode() != CommonConstants.SUCCESS){
                    LOGGER.error("暴鸡收益结算失败:{}",responsePacket);
                }else {
                    //更新暴鸡收益
                    List<Map> inComesList = (List<Map>)responsePacket.getData();
                    inComesList.forEach(inCome->{
                        InComeBenefitJaVoJa vo = FastJsonUtils.fromMap(inCome, new TypeReference<InComeBenefitJaVoJa>(){});
                        Order order = orderRepository.getBySequenceId(vo.getIncomeOrderId());
                        if(order == null){
                            LOGGER.error(BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg()+
                                    ",订单号：{}",vo.getIncomeOrderId());
                        }else{
                            OrderItemTeamRPG orderItemTeam = order.getOrderItemTeamRPG();
                            orderItemTeam.setGmtModified(new Date());

                            int amount = vo.getBaojiIncome();
                            int price = amount * baojiValue2RMBRate;
                            LOGGER.info("暴鸡最后实际收益:抽成后的暴鸡值={}，RMB={}",amount,price);
                            orderItemTeam.setPrice(price);
                            orderItemTeamRepository.updateByPrimaryKeySelective(orderItemTeam);
                        }

                    });
                }

                break;
            }
        }while (incr < intervals.length);

        if(incr >= intervals.length){
            LOGGER.error("发送暴鸡[{}]收益[{}]到工作室,重试次数已达阀值：{}，不在重试",
                    orderIncomeParam.getUserId(),orderIncomeParam.getAmount(),intervals.length);
        }
    }

}
