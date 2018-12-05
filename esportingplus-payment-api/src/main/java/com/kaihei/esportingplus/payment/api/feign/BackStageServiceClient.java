package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.DeductParams;
import com.kaihei.esportingplus.payment.api.params.GCoinBackStageRechargeParam;
import com.kaihei.esportingplus.payment.api.vo.DeductOrderVo;
import com.kaihei.esportingplus.payment.api.vo.FrontGCcoinRechargeVo;
import com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * 基于feign实现远程客服服务接口调用<br/> 1. esportingplus-customer-center-service为服务名 <br/> 2.
 * fallbackFactory指定断路器实现类<br/>
 */
@FeignClient(name = "esportingplus-payment-service", path = "/back_stage", fallbackFactory = BackStageClientFallbackFactory.class)
public interface BackStageServiceClient {

    /**
     * @Description: 暴鸡币后台充值
     * @Param: [gCoinBackStageRechargeParam]
     * @return: com.kaihei.esportingplus.common.ResponsePacket<com.kaihei.esportingplus.payment.api.vo.GCoinRechargeVo>
     * @Author: xusisi
     * @Date: 2018/10/9
     */
    @RequestMapping(value = "/gcoin_recharge", method = RequestMethod.POST)
    public ResponsePacket<GCoinRechargeVo> createGcoinRecharge(@RequestBody GCoinBackStageRechargeParam gCoinBackStageRechargeParam) throws Exception;


    /**
     * @Description: 创建暴鸡币、暴击值扣款
     * @Param: [deductParams]
     * @return: com.kaihei.esportingplus.common.ResponsePacket<com.kaihei.esportingplus.payment.api.vo.DeductOrderVo>
     * @Author: xusisi
     * @Date: 2018/10/11
     */
    @RequestMapping(value = "/deduct", method = RequestMethod.POST)
    public ResponsePacket<DeductOrderVo> createDeductOrder(@RequestBody DeductParams deductParams) throws Exception;

    /**
     * @Description: 查看用户前台充值记录
     * @Param: [userId, channel, source, beginDate, endDate, page, size]
     * @return: com.kaihei.esportingplus.common.ResponsePacket<com.kaihei.esportingplus.payment.api.vo.FrontGCcoinRechargeVo>
     * @Author: xusisi
     * @Date: 2018/10/18
     */
    @RequestMapping(value = "/gcoin_recharge", method = RequestMethod.GET)
    public ResponsePacket<FrontGCcoinRechargeVo> getGCoinRechargeList(@RequestParam(value = "userId", required = false) String userId,
                                                                      @RequestParam(value = "channel", required = false) String channel,
                                                                      @RequestParam(value = "sourceId", required = false) String sourceId,
                                                                      @RequestParam(value = "beginDate", required = false) String beginDate,
                                                                      @RequestParam(value = "endDate", required = false) String endDate,
                                                                      @RequestParam(value = "page", defaultValue = "1") String page,
                                                                      @RequestParam(value = "size", defaultValue = "20") String size) throws Exception;


    /**
     * 后台查看暴击值兑换记录
     * @param uid
     * @param beginDate
     * @param endDate
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exchange/records", method = RequestMethod.GET)
    public ResponsePacket<Map<String, Object>> getStarlightExchangeList(@RequestParam(value = "uid", required = false) String uid,
                                                                      @RequestParam(value = "begin_date", required = false) String beginDate,
                                                                      @RequestParam(value = "end_date", required = false) String endDate,
                                                                      @RequestParam(value = "page", defaultValue = "1") String page,
                                                                      @RequestParam(value = "size", defaultValue = "20") String size) throws Exception;



}
