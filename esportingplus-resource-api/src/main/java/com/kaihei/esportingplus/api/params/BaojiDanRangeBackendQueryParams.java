package com.kaihei.esportingplus.api.params;

import com.kaihei.esportingplus.common.paging.PagingRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡接单范围后台查询参数
 * @author liangyi
 */
@ApiModel(description = "暴鸡接单范围")
public class BaojiDanRangeBackendQueryParams extends PagingRequest implements Serializable {

    private static final long serialVersionUID = -8269846043691176451L;

    /**
     * 所属游戏 id
     */
    @ApiModelProperty(value = "免费车队类型状态",
            required = false, position = 1, example = "1")
    private Integer gameId;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}