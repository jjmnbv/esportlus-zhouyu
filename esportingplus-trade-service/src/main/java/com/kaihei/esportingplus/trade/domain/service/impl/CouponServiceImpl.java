package com.kaihei.esportingplus.trade.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.trade.api.params.ConsumeCouponParams;
import com.kaihei.esportingplus.trade.data.manager.PythonRestClient;
import com.kaihei.esportingplus.trade.data.repository.OrderCouponRepository;
import com.kaihei.esportingplus.trade.domain.entity.OrderCoupon;
import com.kaihei.esportingplus.trade.domain.service.CouponService;
import com.kaihei.esportingplus.trade.domain.service.PVPOrderService;
import com.kaihei.esportingplus.trade.enums.BusinessTypeEnum;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("couponService")
public class CouponServiceImpl implements CouponService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderCouponRepository orderCouponRepository;

    @Autowired
    private PythonRestClient pythonRestClient;

    /**
     * 消费优惠卷
     */
    @Override
    public void consumeCouoonParams(String userId, Long orderId, String sequeue) {
        //通过orderId查找couponId 不存在直接返回
        OrderCoupon orderCoupon = orderCouponRepository.selectByOrderCouponByOrderId(orderId);
        //订单没有使用优惠卷直接返回
        if (Objects.isNull(orderCoupon) || Objects.isNull(orderCoupon.getCouponId())) {
            return;
        }
        //回调消耗掉订单的优惠卷
        ConsumeCouponParams consumeCouponParams = new ConsumeCouponParams();
        consumeCouponParams.setCouponId(orderCoupon.getCouponId());
        consumeCouponParams.setUid(userId);
        consumeCouponParams.setOrderId(sequeue);
        consumeCouponParams.setOrderType(BusinessTypeEnum.TEAM_ORDER.getCode());

        LOGGER.info("消费优惠卷入参：{}", JSON.toJSONString(consumeCouponParams));

        ResponsePacket consumeCouponResponsePacket = pythonRestClient
                .consumeCoupon(consumeCouponParams);
        if (!consumeCouponResponsePacket.responseSuccess()) {
            throw new BusinessException(consumeCouponResponsePacket.getCode(),
                    consumeCouponResponsePacket.getMsg());
        }
        Map consumeCouponResponseData = (HashMap) consumeCouponResponsePacket.getData();
        String consumeResult = (String) consumeCouponResponseData.get("result");
        if ("fail".equalsIgnoreCase(consumeResult)) {
            LOGGER.info("消费卷已消费");
//            throw new BusinessException(BizExceptionEnum.COUPON_USED_FAIL.getErrCode(),
//                    BizExceptionEnum.COUPON_USED_FAIL.getErrMsg());
        }
    }

    @Override
    public int insertOrderCoupon(List<OrderCoupon> orderConponList) {
        return orderCouponRepository.insertOrderCoupon(orderConponList);
    }
}
