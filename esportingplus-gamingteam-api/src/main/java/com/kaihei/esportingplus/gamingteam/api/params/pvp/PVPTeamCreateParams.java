package com.kaihei.esportingplus.gamingteam.api.params.pvp;

import com.kaihei.esportingplus.common.data.JsonSerializable;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PVPTeamCreateParams implements JsonSerializable {

    /**
     * 用户身份 0: 老板, 1: 暴鸡, 2: 暴娘
     */
    @NotNull(message = "用户身份不能为空")
    @Range(min = 0, max = 2, message = "用户身份范围为0~2")
    @ApiModelProperty(value = "用户身份", required = true, position = 1, example = "0")
    private Integer userIdentity;

    /**
     * 游戏code
     */
    @NotNull(message = "游戏code不能为空")
    @Min(value = 1, message = "游戏code不能小于1")
    @ApiModelProperty(value = "游戏Id", required = true, position = 1, example = "88")
    private Integer gameId;

    /**
     * 车队标题
     */
    @NotBlank(message = "车队标题不能为空")
    @Size(max = 20, message = "车队标题不能超过20个字符")
    @ApiModelProperty(value = "车队标题", required = true, position = 1, example = "开车喽,开车喽...")
    private String title;

    @NotNull(message = "游戏区Id不能为空")
    @ApiModelProperty(value = "游戏大区ID", required = true, position = 1, example = "14")
    private Integer gameZoneId;

    @NotNull(message = "玩法Id不能为空")
    @Range(min = 0, max = 1, message = "玩法模式取值范围是0-1")
    @ApiModelProperty(value = "玩法模式, 0:上分; 1:陪玩;", required = true, position = 1, example = "14")
    private Byte playMode;

    /**
     * 车队类型, -1: 未知; 0:免费; 1:付费;
     *
     * 写死 1
     */
    public Integer getTeamType() {
        return 1;
    }

    /**
     * 结算类型, 由子类复写
     */
//    @NotNull(message = "段位下限ID不能为空")
    @ApiModelProperty(value = "段位下限ID", required = true, position = 1, example = "10")
    private Integer lowerDanId;

    //    @NotNull(message = "段位上限ID不能为空")
    @ApiModelProperty(value = "段位上限ID", required = true, position = 1, example = "11")
    private Integer upperDanId;

    @NotNull(message = "结算数量不能为空")
    @ApiModelProperty(value = "结算数量", required = true, position = 1, example = "3")
    private BigDecimal settlementNumber;

    @NotNull(message = "结算类型不能为空")
    @ApiModelProperty(value = "结算类型，1局 2小时", required = true, position = 1, example = "1")
    private Integer settlementType;
}
