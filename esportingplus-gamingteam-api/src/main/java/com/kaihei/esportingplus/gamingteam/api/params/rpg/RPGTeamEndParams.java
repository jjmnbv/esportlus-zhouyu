package com.kaihei.esportingplus.gamingteam.api.params.rpg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

/**
 * 结束车队参数
 * 场景: 解散车队、正常结束车队
 * @author liangyi
 */
@Validated
@ApiModel(value = "结束车队", description = "结束车队参数")
public class RPGTeamEndParams {

    /** 车队序列号 */
    @NotEmpty(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1, example = "12345678945")
    private String sequence;

    /**
     * 是否有比赛结果, 只有车队在进行中解散时才有比赛结果
     * 参考 com.kaihei.esportingplus.common.enums.GameResultEnum
     */
    @Range(min = -1, max = 2, message = "车队比赛结果范围为-1~2")
    @ApiModelProperty(value = "比赛结果, 只有进行中解散车队才有比赛结果", required = false, position = 1, example = "0")
    private Integer gameResult;

    public RPGTeamEndParams() {
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getGameResult() {
        return gameResult;
    }

    public void setGameResult(Integer gameResult) {
        this.gameResult = gameResult;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
