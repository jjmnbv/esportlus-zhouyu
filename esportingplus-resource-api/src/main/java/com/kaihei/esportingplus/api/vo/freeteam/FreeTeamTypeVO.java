package com.kaihei.esportingplus.api.vo.freeteam;

import com.kaihei.esportingplus.api.vo.SettlementModeVO;
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
 * 免费车队类型 VO
 * @author liangyi
 */
@Validated
@ApiModel(description = "免费车队类型")
public class FreeTeamTypeVO implements Serializable {

    private static final long serialVersionUID = 4787710490154789459L;

    /**
     * 免费车队类型主键id
     */
    @ApiModelProperty(value = "免费车队类型id",
            required = false, position = 1, example = "1")
    private Integer freeTeamTypeId;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题",
            required = false, position = 2, example = "陪玩0.5小时")
    private String subtitle;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "图片",
            required = true, position = 3,
            example = "http://img.nga.178.com/attachments/mon_201812/02/-d1rcQ5-idsaZ1fT3cS1k0-160.jpg")
    @NotEmpty(message = "免费车队类型图片不能为空")
    private String imgUrl;

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
     * 玩法模式 {@link com.kaihei.esportingplus.common.enums.PlayModeEnum}
     */
    @NotNull(message = "玩法模式不能为空")
    @ApiModelProperty(value = "玩法模式",
            required = true, position = 7, example = "1")
    private Integer playMode;

    @NotEmpty(message = "结算方式不能为空")
    @ApiModelProperty(value = "结算方式",
            required = false, position = 8, example = "[]")
    private List<SettlementModeVO> settlementModeList;

    /**
     * 可组建的身份 {@link com.kaihei.esportingplus.common.enums.UserIdentityEnum}
     */
    @NotNull(message = "可组建的身份不能为空")
    @ApiModelProperty(value = "可组建的身份",
            required = true, position = 9, example = "1")
    private Integer baojiIdentity;

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
    @ApiModelProperty(value = "排序权重,值越小越靠前",
            required = false, position = 11, example = "1")
    private Integer orderIndex;

    /**
     * 状态 1: 上架, 0: 下架
     */
    @ApiModelProperty(value = "状态",
            required = true, position = 12, example = "1")
    @NotNull(message = "上架/下架状态不能为空")
    private Integer status;

    public FreeTeamTypeVO() {
    }

    public Integer getFreeTeamTypeId() {
        return freeTeamTypeId;
    }

    public void setFreeTeamTypeId(Integer freeTeamTypeId) {
        this.freeTeamTypeId = freeTeamTypeId;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public Integer getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Integer playMode) {
        this.playMode = playMode;
    }

    public List<SettlementModeVO> getSettlementModeList() {
        return settlementModeList;
    }

    public void setSettlementModeList(
            List<SettlementModeVO> settlementModeList) {
        this.settlementModeList = settlementModeList;
    }

    public Integer getBaojiIdentity() {
        return baojiIdentity;
    }

    public void setBaojiIdentity(Integer baojiIdentity) {
        this.baojiIdentity = baojiIdentity;
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