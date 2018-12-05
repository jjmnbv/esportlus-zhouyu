package com.kaihei.esportingplus.security.aspect;

import com.kaihei.esportingplus.security.annotation.AliTextScan;
import com.kaihei.esportingplus.trade.api.params.OrderQueryParams;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestAli {

    @AliTextScan
    public void test(OrderQueryParams name) {
    }

}
