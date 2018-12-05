package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.enums.DeductRatioEnum;
import com.kaihei.esportingplus.payment.api.enums.DeductRatioStatus;
import com.kaihei.esportingplus.payment.api.feign.OrderIncomeServiceClient;
import com.kaihei.esportingplus.payment.api.params.*;
import com.kaihei.esportingplus.payment.api.vo.InComeBenefitVo;
import com.kaihei.esportingplus.payment.domain.entity.DeductRatioSetting;
import com.kaihei.esportingplus.payment.service.DeductRatioService;
import com.kaihei.esportingplus.payment.service.OrderIncomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算收益分成
 *
 * @author zhouyu, haycco
 */
@RestController
@Api(value = "结算收益分成相关API", tags = "结算收益分成接口")
@RequestMapping("/order")
public class OrderIncomeController implements OrderIncomeServiceClient {

    @Autowired
    OrderIncomeService orderIncomeService;

    @Autowired
    DeductRatioService deductRatioService;

    @ApiOperation(value = "接受python数据，计算分成")
    @Override
    public ResponsePacket insertIncomes(@RequestBody List<OrderIncomeParams> orderIncomeParams) {
        ResponsePacket<List<InComeBenefitVo>> responsePacket = orderIncomeService.insertBatchRateOrderIncome(orderIncomeParams, 0);
        return responsePacket;
    }

    @Override
    public ResponsePacket insertIncomesJa(@RequestBody List<OrderIncomeParams> orderIncomeParams) {
        ResponsePacket<List<InComeBenefitVo>> responsePacket = orderIncomeService.insertBatchRateOrderIncome(orderIncomeParams, 1);
        return responsePacket;
    }

    @ApiOperation(value = "接受python数据，查询用户分成")
    @Override
    public ResponsePacket getIncomeByOrderId(@RequestBody List<OrderQueryParam> queryParamList) {
        ResponsePacket<Map<String, Object>> orderIncomeDetail = orderIncomeService.getOrderIncomDetail(queryParamList);
        return orderIncomeDetail;
    }

    @ApiOperation(value = "接受python数据，查询工作室分成")
    @Override
    public ResponsePacket getIncomeOfWorkRoom(@RequestBody List<WorkRoomQueryParams> workRoomQueryParams) {
        ResponsePacket<Map<String, Object>> workRoomIncome = orderIncomeService.getWorkRoomIncome(workRoomQueryParams);
        return workRoomIncome;
    }

    @ApiOperation(value = "查询订单分成比例分成,没有配置默认为0(兼容Python 2.8.0以下版本)")
    @Override
    public ResponsePacket getIncomeRatio() {
        DeductRatioSetting orderIncomeRatio = deductRatioService.queryCalcOrderIncomeRatio();
//        return ResponsePacket.onSuccess(orderIncomeRatio);
        //FIXME Python v2.8.0↓ Response
        Map<String, Object> result = new HashMap<>(2);
        result.put("ratio", orderIncomeRatio.getRatio());
        // 0：启用 1：禁用
        result.put("status", (DeductRatioStatus.ENABLE == DeductRatioStatus.valueOf(orderIncomeRatio.getState())) ? 0 : 1);
        return ResponsePacket.onSuccess(result);
    }

    @ApiOperation(value = "查询订单分成比例值")
    @Override
    public ResponsePacket getCalcIncomeRatio() {
        DeductRatioSetting orderIncomeRatio = deductRatioService.queryCalcOrderIncomeRatio();
        return ResponsePacket.onSuccess(orderIncomeRatio.getRatio());
    }

    @ApiOperation(value = "后台管理员设置分成比例")
    @Override
    public ResponsePacket updateIncomeRatio(@RequestBody RatioPrams ratioPrams) {
        DeductRatioSetting deductRatioSetting = deductRatioService.updateCalcOrderIncomeRatio(ratioPrams.getRatio(),
                DeductRatioStatus.valueOf(ratioPrams.getState()));
        return ResponsePacket.onSuccess(deductRatioSetting);
    }

    @ApiOperation(value = "尝试计算订单抽成后的结果值")
    @Override
    public ResponsePacket getCalcAfterValue(@RequestBody List<QueryDiscountPriceParams> params) {
        params.forEach(item -> {
            if (item != null && item.getDiscountValue() != null) {
                Integer result = deductRatioService.getCalcAfterDeductRatioValue(item.getSourceValue(), DeductRatioEnum.CALC_ORDER);
                item.setDiscountValue(result);
            }
        });
        return ResponsePacket.onSuccess(params);
    }
}