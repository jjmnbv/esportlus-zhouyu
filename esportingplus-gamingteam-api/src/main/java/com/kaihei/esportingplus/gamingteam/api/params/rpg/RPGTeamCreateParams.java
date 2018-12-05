package com.kaihei.esportingplus.gamingteam.api.params.rpg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 创建 RPG 车队参数
 * @author liangyi
 */
@Validated
@ApiModel(value = "创建车队", description = "创建车队参数")
public class RPGTeamCreateParams extends RPGTeamMemberBaseParams {

    /** 游戏code */
    @NotNull(message = "游戏code不能为空")
    @Min(value = 1, message = "游戏code不能小于1")
    @ApiModelProperty(value = "游戏code", required = true, position = 1, example = "88")
    private Integer gameCode;

    /** 车队标题 */
    @NotBlank(message = "车队标题不能为空")
    @Size(max = 20, message = "车队标题不能超过20个字符")
    @ApiModelProperty(value = "车队标题", required = true, position = 1, example = "开车喽,开车喽...")
    private String title;

    /** 攻坚队名称，20 字符以内 */
    @NotBlank(message = "攻坚队名称不能为空")
    @Size(max = 20, message = "攻坚队名称不能超过20个字符")
    @ApiModelProperty(value = "攻坚队名称", required = true, position = 1, example = "暴鸡打团1001")
    private String assaultName;

    /** 副本code */
    @NotNull(message = "副本code不能为空")
    @Min(value = 1, message = "副本code不能小于1")
    @ApiModelProperty(value = "副本code", required = true, position = 1, example = "2")
    private Integer raidCode;

    /** 游戏频道，20 字符以内 */
    @NotBlank(message = "游戏频道不能为空")
    @Size(max = 20, message = "游戏频道不能超过20个字符")
    @ApiModelProperty(value = "游戏频道", required = true, position = 1, example = "CH123456")
    private String channel;


    public RPGTeamCreateParams() {
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssaultName() {
        return assaultName;
    }

    public void setAssaultName(String assaultName) {
        this.assaultName = assaultName;
    }

    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
