package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;
import com.kaihei.esportingplus.payment.api.params.OrderQueryParam;
import com.kaihei.esportingplus.payment.api.params.QueryDiscountPriceParams;
import com.kaihei.esportingplus.payment.api.params.RatioPrams;
import com.kaihei.esportingplus.payment.api.params.WorkRoomQueryParams;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderIncomeClientFallbackFactory implements FallbackFactory<OrderIncomeServiceClient> {

    @Override
    public OrderIncomeServiceClient create(Throwable throwable) {

        return new OrderIncomeServiceClient() {
            @Override
            public ResponsePacket insertIncomes(List<OrderIncomeParams> orderIncomeParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket getIncomeByOrderId(List<OrderQueryParam> queryParamList) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket getIncomeOfWorkRoom(List<WorkRoomQueryParams> workRoomQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket getIncomeRatio() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket getCalcIncomeRatio() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateIncomeRatio(RatioPrams ratioPrams) {
                return ResponsePacket.onHystrix();
            }
            
            @Override
            public ResponsePacket insertIncomesJa(List<OrderIncomeParams> orderIncomeParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket getCalcAfterValue(List<QueryDiscountPriceParams> params) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}
