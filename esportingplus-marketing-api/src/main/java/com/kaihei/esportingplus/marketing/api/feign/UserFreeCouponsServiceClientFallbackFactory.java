package com.kaihei.esportingplus.marketing.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsDtlVo;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsInfoVo;
import feign.hystrix.FallbackFactory;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/11/19 16:12
 */
@Component
public class UserFreeCouponsServiceClientFallbackFactory implements FallbackFactory<UserFreeCouponsServiceClient> {
    @Override
    public UserFreeCouponsServiceClient create(Throwable throwable) {
        return new UserFreeCouponsServiceClient() {
            @Override
            public ResponsePacket addUserFreeCoupons(String uid, Integer freeCount, Date invalidTime, Integer type, Integer source) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket reduceFreeCoupons(String uid, Integer freeCount) {
                return ResponsePacket.hystrix();
            }

            @Override
            public ResponsePacket returnFreeCoupons(List<Long> couponsIds) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<UserFreeCouponsInfoVo> getUserFreeCouponsInfo() {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<UserFreeCouponsInfoVo> getUserFreeCouponsInfo(String uid) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<UserFreeCouponsDtlVo> getUserFreeCouponsDetail() {
                return ResponsePacket.onHystrix();
            }
        };
    }
}
