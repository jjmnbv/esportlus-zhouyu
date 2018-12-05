package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.payment.api.feign.CloudAccountServiceClient;
import com.kaihei.esportingplus.payment.api.params.CloudAccountOrderParams;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountDealerInfoVo;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountRespVo;
import com.kaihei.esportingplus.payment.service.CloudAccountService;
import com.kaihei.esportingplus.payment.service.PayService;
import com.kaihei.esportingplus.payment.util.IpUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(tags = {"云账户提现API"})
@RequestMapping("/cloud")
public class CloudAccountController implements CloudAccountServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(CloudAccountController.class);

    @Autowired
    private PayService cloudAccountServiceImpl;

    @Autowired
    private CloudAccountService cloudAccountService;

    /**
     * 创建云提现订单
     *
     * @param orderParams
     * @param appId
     * @param tag
     * @param httpServletRequest
     * @return
     */
    @Override
    public ResponsePacket<CloudAccountRespVo> create(@RequestBody CloudAccountOrderParams orderParams,
                                                     @PathVariable String appId, @PathVariable String tag,
                                                     HttpServletRequest httpServletRequest) {
        logger.debug("YunAccountController >> create >> orderParams = {}, appId={}, tag={} ", orderParams.toString(), appId, tag);
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, orderParams);
        String ip = IpUtils.getIp(httpServletRequest);
        CloudAccountRespVo respVo = cloudAccountServiceImpl.createWithdrawOrder(orderParams, appId, tag, null, ip);
        logger.debug("YunAccountController >> create >> 出参 >> " + respVo.toString());
        return ResponsePacket.onSuccess(respVo);
    }

    /**
     * 查询商户余额
     *
     * @param appId
     * @param tag
     * @return
     */
    @Override
    public ResponsePacket<List<CloudAccountDealerInfoVo>> queryAccount(@PathVariable String appId, @PathVariable String tag) {
        return ResponsePacket.onSuccess(cloudAccountService.queryAccount(appId, tag));
    }

    /**
     * 查询订单状态
     *
     * @param appId
     * @param tag
     * @param outTradeNo
     * @return
     */
    @Override
    public ResponsePacket queryOrder(@PathVariable String appId, @PathVariable String tag,
                                     @RequestParam(value = "out_trade_no", required = true) String outTradeNo) {
        return ResponsePacket.onSuccess(cloudAccountService.searchWithdrawOrderInfo(outTradeNo, appId, tag, null));
    }
}
