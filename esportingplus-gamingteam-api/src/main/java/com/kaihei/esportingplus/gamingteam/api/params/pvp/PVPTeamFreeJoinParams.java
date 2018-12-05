package com.kaihei.esportingplus.gamingteam.api.params.pvp;

import com.kaihei.esportingplus.common.data.JsonSerializable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PVPTeamFreeJoinParams implements JsonSerializable {

    /**
     * 加入车队的序列号
     */
    @ApiModelProperty("加入车队的序列号")
    private String teamSequence;

    @ApiModelProperty("段位Id")
    private Integer gameDanId;

    @ApiModelProperty("加入身份")
    private Integer userIdentity;

    @ApiModelProperty("用户id")
    private String uid;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("鸡牌号")
    private String chickenId;
}
