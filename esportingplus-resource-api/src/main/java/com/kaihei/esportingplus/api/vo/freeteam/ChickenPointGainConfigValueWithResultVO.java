package com.kaihei.esportingplus.api.vo.freeteam;

import com.kaihei.esportingplus.common.enums.GameResultEnum;
import lombok.Data;

@Data
public class ChickenPointGainConfigValueWithResultVO {

    /**
     * 免费车队类型Id
     */
    private Integer gameDanId;

    /**
     * 配置值
     */
    private Integer value;

    /**
     * 游戏结果Code{@link GameResultEnum#getCode()}
     */
    private Integer gameResultId;
}
