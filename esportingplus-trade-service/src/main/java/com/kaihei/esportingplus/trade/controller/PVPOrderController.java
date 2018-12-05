package com.kaihei.esportingplus.trade.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.trade.api.feign.PVPOrdersServiceClient;
import com.kaihei.esportingplus.trade.api.params.PVPInComeParams;
import com.kaihei.esportingplus.trade.domain.service.PVPOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单
 *
 * @author liangyi
 */
@RestController
@RequestMapping("/orders/pvp")
@Api(tags = {"PVP订单服务接口"})
public class PVPOrderController implements PVPOrdersServiceClient {

    @Autowired
    private PVPOrderService pvpOrderService;

    @ApiOperation(value = "校验队员(老板)是否已支付")
    @Override
    public ResponsePacket checkTeamMemberPayed(@PathVariable("orderSequence") String orderSequence) {
        BizExceptionEnum checkResult = pvpOrderService
                .checkTeamMemberPayed(orderSequence);

        if(checkResult != BizExceptionEnum.SUCCESS){
            return ResponsePacket.onError(checkResult.getErrCode(), String.format(checkResult.getErrMsg(),orderSequence));
        }

        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "PVP预计收益")
    @Override
    public ResponsePacket preIncome(@RequestBody PVPInComeParams pvpInComeParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,pvpInComeParams);
        return ResponsePacket.onSuccess(pvpOrderService.preIncome(pvpInComeParams));
    }
}
