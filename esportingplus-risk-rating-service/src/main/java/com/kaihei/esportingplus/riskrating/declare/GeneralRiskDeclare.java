package com.kaihei.esportingplus.riskrating.declare;

import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import com.kaihei.esportingplus.riskrating.service.RiskDictService;
import java.util.Optional;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GeneralRiskDeclare {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 字典group code
     */
    private final String GROUP_CODE = "RISK_RATING";
    @Autowired
    private RiskDictService riskDictService;

    /**
     * 根据itemCode查字典Value
     */
    protected RiskDict findValueByItemCode(String itemCode) {
        return Optional.ofNullable(itemCode)
                .map(ic -> riskDictService.findByGroupCodeAndItemCode(GROUP_CODE, ic))
                .orElse(null);
    }

    /**
     * 根据itemCode查字典Value
     *
     * 并进行转换
     */
    protected <R> R findValueByItemCode(String itemCode, Function<RiskDict, R> function) {
        return Optional.ofNullable(findValueByItemCode(itemCode))
                .map(function)
                .orElse(null);
    }
}
