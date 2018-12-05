package com.kaihei.esportingplus.resource.domain.service.freeteam;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointGainConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointVO;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import java.util.List;

/**
 * @author 谢思勇
 * @date 2018年10月9日 14:30:24
 */
public interface ChickenpointGainConfigService {


    /**
     * 根据游戏类型Id 获取游戏鸡分获取配置
     */
    ChickenpointGainConfigVo getChickenpointGainConfig(Integer freeTeamTypeId,
            SettlementTypeEnum settlementTypeEnum);

    /**
     * 获取按局结算车队类型
     */
    List<ChickenpointVO> getRoundTeamTypes();

    /**
     * 获取按小时结算车队类型
     */
    List<ChickenpointVO> getHourTeamTypes();

    /**
     * 获取按小时结算车队类型 的配置
     */
    ChickenpointGainConfigVo getHourChickenpointGainConfigs(Integer teamTypeId);

    /**
     * 获取按局结算车队类型 的配置
     */
    ChickenpointGainConfigVo getRoundChickenpointGainConfigs(Integer teamTypeId);
}
