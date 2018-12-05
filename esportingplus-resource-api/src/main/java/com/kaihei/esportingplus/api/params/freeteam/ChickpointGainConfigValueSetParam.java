package com.kaihei.esportingplus.api.params.freeteam;

import com.kaihei.esportingplus.common.data.Castable;
import lombok.Data;

@Data
public class ChickpointGainConfigValueSetParam implements Castable {

    /**
     * 免费车队类型Id
     */
    private Integer freeTeamTypeId;
    /**
     * 游戏段位Id
     */
    private Integer gameDanId;
    /**
     * 游戏结果Id
     */
    private Integer gameResultId;


    /**
     * 暴鸡身份Id
     */
    private Integer baojiIdentityId;

    /**
     * 配置值
     */
    private Integer value;
}
