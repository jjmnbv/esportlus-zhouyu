package com.kaihei.esportingplus.trade.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel(value = "PVP游戏结果参数", description = "PVP游戏结果参数")
@Builder
public final class GameResultPVPParams implements Serializable {

    private static final long serialVersionUID = 2468338418214056200L;
    @NotNull(message = "游戏结果不能为空")
    @Range(min = -1,max = 2,message = "游戏结果，要求：-1 到 2")
    @ApiModelProperty(value = "游戏结果，0:胜利 1：失败 2:未打 -1：未知", required = true, position = 1, example = "2")
    private Integer gameResult;

    @NotNull(message = "局次不能为空")
    @ApiModelProperty(value = "第几局", required = true, position = 2, example = "1")
    private Integer rounds;

    public Integer getGameResult() {
        return gameResult;
    }

    public void setGameResult(Integer gameResult) {
        this.gameResult = gameResult;
    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameResultPVPParams that = (GameResultPVPParams) o;
        return Objects.equals(gameResult, that.gameResult) &&
                Objects.equals(rounds, that.rounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameResult, rounds);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
