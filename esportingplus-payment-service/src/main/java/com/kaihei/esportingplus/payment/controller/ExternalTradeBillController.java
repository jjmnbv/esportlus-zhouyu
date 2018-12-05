package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.payment.api.params.ExternalTradeBillQueryParams;
import com.kaihei.esportingplus.payment.api.vo.ExternalTradeBillVo;
import com.kaihei.esportingplus.payment.service.ExternalTradeBillService;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: tangtao
 **/
@RestController
@Api(tags = {"第三方支付流水相关API"})
@RequestMapping("/external/trade/bill")
public class ExternalTradeBillController {

    @Autowired
    private ExternalTradeBillService externalTradeBillService;

    @GetMapping
    public List<ExternalTradeBillVo> query(ExternalTradeBillQueryParams queryParams) {
        return externalTradeBillService.query(queryParams);
    }

}
