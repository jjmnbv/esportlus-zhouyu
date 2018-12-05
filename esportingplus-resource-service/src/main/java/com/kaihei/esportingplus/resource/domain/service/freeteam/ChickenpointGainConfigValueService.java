package com.kaihei.esportingplus.resource.domain.service.freeteam;

import com.kaihei.esportingplus.api.params.freeteam.ChickenpointGainConfigValueUpdateParam;

public interface ChickenpointGainConfigValueService {

    /**
     * 根据车队类型Id更新鸡分获取配置
     */
    Integer updateChickenpointGainConfigValues(Integer freeTeamTypeId,
            ChickenpointGainConfigValueUpdateParam configData);
}
