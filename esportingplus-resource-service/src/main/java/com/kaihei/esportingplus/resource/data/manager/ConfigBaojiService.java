package com.kaihei.esportingplus.resource.data.manager;

import com.kaihei.esportingplus.api.vo.BaojiLevelRateVo;
import com.kaihei.esportingplus.api.vo.BatchBaojiRateQueryParams;
import java.math.BigDecimal;
import java.util.List;

public interface ConfigBaojiService {

    /**
     * 根据暴鸡等级查询暴鸡等级系数
     * @param baojiLevel
     * @return
     */
    public BigDecimal getBaojiLevelRate(Integer baojiLevel);

    /**
     * 根据多个暴鸡等级，查询暴鸡等级系数
     * @param params
     * @return
     */
    public List<BaojiLevelRateVo> getBaojiLevelRateBatch(List<Integer> params);
}
