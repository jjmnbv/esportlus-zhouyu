package com.kaihei.esportingplus.api.params;

import com.kaihei.esportingplus.common.enums.ProductBizTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡计价配置查询参数
 * @author liangyi
 */
@Builder
@AllArgsConstructor
@ApiModel(description = "暴鸡计价配置")
public class BaojiPricingConfigParams implements Serializable {

    private static final long serialVersionUID = 326583044741717901L;

    /**
     * 所属游戏 id
     */
    @ApiModelProperty(value = "游戏id",
            required = true, position = 1, example = "1")
    @NotNull
    private Integer gameId;

    /**
     * 所属段位 id
     */
    @ApiModelProperty(value = "老板段位id",
            required = true, position = 2, example = "[50, 51, 52]")
    private List<Integer> bossGameDanIdList;

    /**
     * 暴鸡等级 code
     */
    @ApiModelProperty(value = "暴鸡等级code",
            required = true, position = 3, example = "[100, 101, 102]")
    @NotEmpty
    private List<Integer> baojiLevelCodeList;

    /**
     * 计价类型, 参考 {@link ProductBizTypeEnum}
     */
    @ApiModelProperty(value = "计价类型",
            required = true, position = 4, example = "2")
    @NotNull
    private Integer pricingType;

    public BaojiPricingConfigParams() {
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public List<Integer> getBossGameDanIdList() {
        return bossGameDanIdList;
    }

    public void setBossGameDanIdList(List<Integer> bossGameDanIdList) {
        this.bossGameDanIdList = bossGameDanIdList;
    }

    public List<Integer> getBaojiLevelCodeList() {
        return baojiLevelCodeList;
    }

    public void setBaojiLevelCodeList(List<Integer> baojiLevelCodeList) {
        this.baojiLevelCodeList = baojiLevelCodeList;
    }

    public Integer getPricingType() {
        return pricingType;
    }

    public void setPricingType(Integer pricingType) {
        this.pricingType = pricingType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}