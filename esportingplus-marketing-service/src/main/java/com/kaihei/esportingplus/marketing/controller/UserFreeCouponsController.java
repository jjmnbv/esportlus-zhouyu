package com.kaihei.esportingplus.marketing.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.marketing.api.feign.UserFreeCouponsServiceClient;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsDtlVo;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsInfoVo;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsQueryResultVo;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserFreeCouponsService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/11/19 16:17
 */
@RestController
@RequestMapping("free/coupons")
public class UserFreeCouponsController implements UserFreeCouponsServiceClient {

    @Autowired
    private MarketUserFreeCouponsService marketUserFreeCouponsService;

    /**
     * 添加用户免费上车次数
     *
     * @param uid
     * @param freeCount
     * @param invalidTime
     * @param type 类型 1-免费车队券
     * @param source 来源 1-每日启动赠送 2-邀请任务
     * @return
     */
    @Override
    public ResponsePacket addUserFreeCoupons(@RequestParam(value = "uid", required = true) String uid,
                                             @RequestParam(value = "freeCount", required = true) Integer freeCount,
                                             @RequestParam(value = "invalidTime", required = true) Date invalidTime,
                                             @RequestParam(value = "type", required = true) Integer type,
                                             @RequestParam(value = "source", required = true) Integer source){
        marketUserFreeCouponsService.addUserFreeCoupons(uid, freeCount, invalidTime, type, source);
        return ResponsePacket.onSuccess();
    }

    /**
     * 扣减用户免费上车次数
     *
     * @param uid
     * @param freeCount
     * @return
     */
    @Override
    public ResponsePacket<UserFreeCouponsQueryResultVo> reduceFreeCoupons(@RequestParam(value = "uid", required = true) String uid,
                                                                          @RequestParam(value = "freeCount", required = true) Integer freeCount){
        return ResponsePacket.onSuccess(
                marketUserFreeCouponsService.reduceFreeCoupons(uid, freeCount));
    }

    /**
     * 返还用户免费上车次数
     *
     * @return
     */
    @Override
    public ResponsePacket<UserFreeCouponsQueryResultVo> returnFreeCoupons(@RequestParam(value = "couponsIds", required = true) List<Long> couponsIds) {
        return ResponsePacket.onSuccess(marketUserFreeCouponsService.returnFreeCoupons(couponsIds));
    }

    /**
     * 获取用户车队免单信息
     *
     * @return
     */
    @Override
    public ResponsePacket<UserFreeCouponsInfoVo> getUserFreeCouponsInfo() {
        String uid = UserSessionContext.getUser().getUid();
        return ResponsePacket.onSuccess(marketUserFreeCouponsService.getUserFreeCouponsInfo(uid));
    }

    /**
     * 获取用户车队免单信息
     *
     * @return
     */
    @Override
    public ResponsePacket<UserFreeCouponsInfoVo> getUserFreeCouponsInfo(@PathVariable String uid) {
        return ResponsePacket.onSuccess(marketUserFreeCouponsService.getUserFreeCouponsInfo(uid));
    }

    /**
     * 查询匹配机会信息
     *
     * @return
     */
    @Override
    public ResponsePacket<UserFreeCouponsDtlVo> getUserFreeCouponsDetail() {
        return ResponsePacket.onSuccess(marketUserFreeCouponsService.getUserFreeCouponsDetail());
    }
}
