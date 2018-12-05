package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;
import com.kaihei.esportingplus.payment.api.params.OrderQueryParam;
import com.kaihei.esportingplus.payment.api.params.QueryDiscountPriceParams;
import com.kaihei.esportingplus.payment.api.params.RatioPrams;
import com.kaihei.esportingplus.payment.api.params.WorkRoomQueryParams;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 结算收益feign客户端
 *
 * @author zhouyu
 */
@FeignClient(name = "esportingplus-payment-service", path = "/order",fallbackFactory = OrderIncomeClientFallbackFactory.class)
public interface OrderIncomeServiceClient {

    @PostMapping("/income_calc")
    ResponsePacket insertIncomes(@RequestBody List<OrderIncomeParams> orderIncomeParams);

    @PostMapping("/income_calct")
    ResponsePacket insertIncomesJa(@RequestBody List<OrderIncomeParams> orderIncomeParams);

    @PostMapping("/order_ques")
    ResponsePacket getIncomeByOrderId(@RequestBody List<OrderQueryParam> queryParamList);

    @PostMapping("/income_room")
    ResponsePacket getIncomeOfWorkRoom(List<WorkRoomQueryParams> workRoomQueryParams);
    /**
     * 查询系统设置的订单结算抽成比例值及状态（原提供给Python方使用）
     */
    @GetMapping("/ratio")
    ResponsePacket getIncomeRatio();
    /**
     * 直接查询系统设置的订单结算抽成比例值
     *
     * @return Data:Float
     */
    @GetMapping("/calc_income_ratio")
    ResponsePacket getCalcIncomeRatio();
    /**
     * 更新订单结算抽成比例值及状态
     *
     * @param ratioPrams->state状态值为: ENABLE/DISABLE
     */
    @PutMapping("/ratio")
    ResponsePacket updateIncomeRatio(RatioPrams ratioPrams);
    /**
     * 直接查询订单结算抽成后的预计结果值
     */
    @GetMapping("/calc_income")
    ResponsePacket getCalcAfterValue(@RequestBody List<QueryDiscountPriceParams> params);
}
