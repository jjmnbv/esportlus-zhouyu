package com.kaihei.esportingplus.gamingteam.api.params.pvp;

import com.kaihei.esportingplus.common.enums.GameResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

@Data
@ApiModel
public class PVPTeamEndParams {

    @ApiModelProperty("车队序列号")
    private String teamSequence;

    /**
     * 游戏结果
     *
     * {@link GameResultEnum#getCode()}
     */

    @ApiModelProperty("游戏结果集合")
    private List<Integer> gameResult;

    /**
     * 截图
     */
    @ApiModelProperty("截图")
    private List<String> screenshots;
}
