package com.kaihei.esportingplus.marketing.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsDtlVo;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsInfoVo;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsQueryResultVo;
import java.util.Date;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zl.zhao
 * @description:用户免费券Feign
 * @date: 2018/11/19 16:10
 */
@FeignClient(name = "esportingplus-marketing-service", path = "/free/coupons", fallbackFactory = UserFreeCouponsServiceClientFallbackFactory.class)
public interface UserFreeCouponsServiceClient {

    /**
     * 添加用户免费上车次数
     *
     * @param uid
     * @param freeCount
     * @param invalidTime
     * @param type
     * @param source
     * @return
     */
    @PostMapping("/add")
    ResponsePacket addUserFreeCoupons(@RequestParam(value = "uid", required = true) String uid,
            @RequestParam(value = "freeCount", required = true) Integer freeCount,
            @RequestParam(value = "invalidTime", required = true) Date invalidTime,
            @RequestParam(value = "type", required = true) Integer type,
            @RequestParam(value = "source", required = true) Integer source);


    /**
     * 扣减用户免费上车次数
     *
     * @param uid
     * @param freeCount
     * @return
     */
    @PostMapping("/reduce")
    public ResponsePacket<UserFreeCouponsQueryResultVo> reduceFreeCoupons(
            @RequestParam(value = "uid", required = true) String uid,
            @RequestParam(value = "freeCount", required = true) Integer freeCount);



    /**
     * 返还用户免费上车次数
     *
     * @return
     */
    @PostMapping("/return")
    public ResponsePacket<UserFreeCouponsQueryResultVo> returnFreeCoupons(
            @RequestParam(value = "couponsIds", required = true) List<Long> couponsIds);



    /**
     * 获取用户车队免单信息
     *
     * @return
     */
    @GetMapping("/info")
    ResponsePacket<UserFreeCouponsInfoVo> getUserFreeCouponsInfo();

    @GetMapping("/info/{uid}")
    ResponsePacket<UserFreeCouponsInfoVo> getUserFreeCouponsInfo(@PathVariable("uid") String uid);

    /**
     * 获取用户车队免单信息
     *
     * @return
     */
    @GetMapping("/detail")
    ResponsePacket<UserFreeCouponsDtlVo> getUserFreeCouponsDetail();
}
