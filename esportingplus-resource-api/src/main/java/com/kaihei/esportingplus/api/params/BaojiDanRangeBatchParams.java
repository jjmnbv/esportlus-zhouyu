package com.kaihei.esportingplus.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 暴鸡接单范围
 * @author liangyi
 */
@Validated
@ApiModel(description = "暴鸡接单范围")
public class BaojiDanRangeBatchParams implements Serializable {

    private static final long serialVersionUID = 268455326222795211L;

    /**
     * 所属游戏id
     */
    @NotNull(message = "所属游戏id不能为空")
    @ApiModelProperty(value = "所属游戏id",
            required = true, position = 1, example = "1")
    private Integer gameId;

    /**
     * 暴鸡接单范围集合
     */
    @NotEmpty(message = "暴鸡接单范围不能为空")
    @ApiModelProperty(value = "暴鸡接单范围不能为空",
            required = true, position = 2, example = "[]")
    private List<BaojiDanRangeParams> baojiDanRangeList;

    public BaojiDanRangeBatchParams() {
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public List<BaojiDanRangeParams> getBaojiDanRangeList() {
        return baojiDanRangeList;
    }

    public void setBaojiDanRangeList(
            List<BaojiDanRangeParams> baojiDanRangeList) {
        this.baojiDanRangeList = baojiDanRangeList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}