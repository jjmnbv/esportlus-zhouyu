package com.kaihei.esportingplus.api.params.freeteam;

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
 * 车队类型参数
 * @author liangyi
 */
@Validated
@ApiModel(description = "车队类型")
public class TeamTypeParams implements Serializable {

    private static final long serialVersionUID = 4787710490154789459L;

    /**
     * 车队类型主键id
     */
    @ApiModelProperty(value = "车队类型id",
            required = false, position = 1, example = "1")
    private Integer teamTypeId;

    /**
     * 车队名称
     */
    @ApiModelProperty(value = "车队名称",
            required = false, position = 2, example = "陪玩0.5小时")
    private String name;

    /**
     * 分类 1: 付费, 0: 免费
     */
    @ApiModelProperty(value = "车队类型分类",
            required = true, position = 3, example = "1")
    private Integer category;
    /**
     * 所属游戏id,来自数据字典
     */
    @NotNull(message = "所属游戏id不能为空")
    @ApiModelProperty(value = "所属游戏id",
            required = true, position = 4, example = "1")
    private Integer gameId;

    /**
     * 所属游戏区
     */
    @NotEmpty(message = "所属游戏区不能为空")
    @ApiModelProperty(value = "所属游戏区",
            required = true, position = 5, example = "[]")
    private List<Integer> gameZoneIdList;

    /**
     * 所属段位
     */
    @ApiModelProperty(value = "所属游戏段位",
            required = false, position = 6, example = "[]")
    private List<Integer> gameDanIdList;

    /**
     * 玩法模式 id,来自数据字典(上分/陪玩)
     */
    @NotNull(message = "玩法模式id不能为空")
    @ApiModelProperty(value = "玩法模式id",
            required = true, position = 7, example = "1")
    private Integer playModeId;

    @NotEmpty(message = "结算数量不能为空")
    @ApiModelProperty(value = "结算数量",
            required = false, position = 8, example = "[]")
    private List<TeamSettlementModeParams> settlementModeList;

    /**
     * 可组建的身份,来自数据字典(暴鸡/暴娘)
     */
    @NotNull(message = "可组建的身份id不能为空")
    @ApiModelProperty(value = "可组建的身份id",
            required = true, position = 9, example = "1")
    private Integer baojiIdentityId;

    /**
     * 最大座位数
     */
    @NotNull(message = "最大座位数不能为空")
    @ApiModelProperty(value = "最大座位数",
            required = true, position = 10, example = "5")
    private Integer maxPositionCount;

    /**
     * 排序权重, 值越大越靠前
     */
    @ApiModelProperty(value = "排序权重,值越大越靠前",
            required = false, position = 11, example = "1")
    private Integer orderIndex;

    /**
     * 状态 1: 上架, 0: 下架
     */
    @ApiModelProperty(value = "状态",
            required = true, position = 12, example = "1")
    @NotNull(message = "上架/下架状态不能为空")
    private Integer status;

    public TeamTypeParams() {
    }

    public Integer getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(Integer teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public List<Integer> getGameZoneIdList() {
        return gameZoneIdList;
    }

    public void setGameZoneIdList(List<Integer> gameZoneIdList) {
        this.gameZoneIdList = gameZoneIdList;
    }

    public List<Integer> getGameDanIdList() {
        return gameDanIdList;
    }

    public void setGameDanIdList(List<Integer> gameDanIdList) {
        this.gameDanIdList = gameDanIdList;
    }

    public Integer getPlayModeId() {
        return playModeId;
    }

    public void setPlayModeId(Integer playModeId) {
        this.playModeId = playModeId;
    }

    public List<TeamSettlementModeParams> getSettlementModeList() {
        return settlementModeList;
    }

    public void setSettlementModeList(
            List<TeamSettlementModeParams> settlementModeList) {
        this.settlementModeList = settlementModeList;
    }

    public Integer getBaojiIdentityId() {
        return baojiIdentityId;
    }

    public void setBaojiIdentityId(Integer baojiIdentityId) {
        this.baojiIdentityId = baojiIdentityId;
    }

    public Integer getMaxPositionCount() {
        return maxPositionCount;
    }

    public void setMaxPositionCount(Integer maxPositionCount) {
        this.maxPositionCount = maxPositionCount;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}