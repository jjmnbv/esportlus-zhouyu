package com.kaihei.esportingplus.gamingteam.api.params.pvp;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 免费车队匹配参数
 *
 * @author liangyi
 */
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PVPFreeTeamMatchingParams implements Serializable {

    private static final long serialVersionUID = 44416614985883432L;

    @NotNull(message = "服务结算类型不能为空")
    @ApiModelProperty(value = "服务结算类型", required = true, example = "0")
    private Integer settlementType;

    @NotBlank(message = "服务结算显示的中文名为空")
    @ApiModelProperty(value = "服务结算显示的中文名", required = true, example = "1局")
    private String settlementName;

    @NotNull(message = "用户身份不能为空")
    @ApiModelProperty(value = "用户身份", required = true, position = 1, example = "0")
    private Integer userIdentity;

    /**
     * 免费车队类型 id
     */
    @NotNull(message = "免费车队类型Id不能为空")
    @Min(value = 1, message = "免费车队类型Id不能小于1")
    @ApiModelProperty(value = "免费车队类型Id", required = true, position = 2, example = "1")
    private Integer teamTypeId;

    /**
     * 免费车队类型名称
     */
    @NotBlank(message = "免费车队类型名称不能为空")
    @ApiModelProperty(value = "免费车队类型名称", required = true, position = 3, example = "14")
    private String teamTypeDisplayName;

    @NotNull(message = "游戏Id不能为空")
    @Min(value = 1, message = "游戏Id不能小于1")
    @ApiModelProperty(value = "游戏Id", required = true, position = 2, example = "1")
    private Integer gameId;

    /**
     * 游戏大区 id
     */
    @NotNull(message = "游戏区Id不能为空")
    @ApiModelProperty(value = "游戏大区ID", required = true, position = 4, example = "14")
    private Integer gameZoneId;

    /**
     * 游戏大区名称
     */
    @NotBlank(message = "游戏大区名称不能为空")
    @ApiModelProperty(value = "游戏大区名称", required = true, position = 5, example = "14")
    private String gameZoneName;

    /**
     * 游戏段位 id, 陪玩车队暂时不传
     */
    @NotNull(message = "游戏段位ID不能为空")
    @ApiModelProperty(value = "游戏段位ID", required = true, position = 6, example = "10")
    private Integer gameDanId;
    /**
     * 游戏段位名称, 陪玩车队暂时不传
     */
    @NotBlank(message = "游戏段位名称不能为空")
    @ApiModelProperty(value = "游戏段位名称", required = true, position = 7, example = "10")
    private String gameDanName;

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    public Integer getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(Integer settlementType) {
        this.settlementType = settlementType;
    }

    public Integer getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(Integer teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    public Integer getGameZoneId() {
        return gameZoneId;
    }

    public void setGameZoneId(Integer gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    public Integer getGameDanId() {
        return gameDanId;
    }

    public void setGameDanId(Integer gameDanId) {
        this.gameDanId = gameDanId;
    }

    public String getGameZoneName() {
        return gameZoneName;
    }

    public void setGameZoneName(String gameZoneName) {
        this.gameZoneName = gameZoneName;
    }

    public String getGameDanName() {
        return gameDanName;
    }

    public void setGameDanName(String gameDanName) {
        this.gameDanName = gameDanName;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getTeamTypeDisplayName() {
        return teamTypeDisplayName;
    }

    public void setTeamTypeDisplayName(String teamTypeDisplayName) {
        this.teamTypeDisplayName = teamTypeDisplayName;
    }

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
