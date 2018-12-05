package com.kaihei.esportingplus.riskrating.service;

import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import java.util.List;

/**
 * 风控数据字典表
 *
 * @author 张方、谢思勇
 */
public interface RiskDictService {

    /**
     * 数据预热到redis,启动时初始化
     */
    public void initDataToRedis();

    /**
     * 通过groupCode和iteamCode查询唯一的风险字典对象
     * @param groupCode
     * @param itemCode
     * @return
     */
    public RiskDict findByGroupCodeAndItemCode(String groupCode,String itemCode);

    /**
     * 通过groupCode和iteamCode查询一组风险字典对象
     * @param groupCode
     * @return
     */
    public List<RiskDict> findByGroupCode(String groupCode);

    Integer updateById(RiskDict riskDict);

    public boolean checkRiskSwitchStatus();
}
