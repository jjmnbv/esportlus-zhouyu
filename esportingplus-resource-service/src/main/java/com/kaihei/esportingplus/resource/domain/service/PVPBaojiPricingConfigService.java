package com.kaihei.esportingplus.resource.domain.service;

import com.kaihei.esportingplus.api.params.BaojiPricingConfigParams;
import com.kaihei.esportingplus.api.vo.PVPBaojiGameDanIncomeVO;
import java.util.List;

/**
 * @author liangyi
 */
public interface PVPBaojiPricingConfigService {

    /**
     * 查询暴鸡段位收益配置
     * @param baojiPricingConfigParams
     * @return
     */
    List<PVPBaojiGameDanIncomeVO> getBaojiGameDanIncome(
            BaojiPricingConfigParams baojiPricingConfigParams);

}
