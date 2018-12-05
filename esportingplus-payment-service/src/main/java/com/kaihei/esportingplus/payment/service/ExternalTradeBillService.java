package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.api.params.ExternalTradeBillQueryParams;
import com.kaihei.esportingplus.payment.api.vo.ExternalTradeBillVo;
import java.util.List;

/**
 * @author user
 */
public interface ExternalTradeBillService {

    /**
     * 保存第三方支付流水
     */
    <T> boolean saveTradeBill(T order);

    /**
     * 查询第三方支付流水
     */
    List<ExternalTradeBillVo> query(ExternalTradeBillQueryParams params);
}
