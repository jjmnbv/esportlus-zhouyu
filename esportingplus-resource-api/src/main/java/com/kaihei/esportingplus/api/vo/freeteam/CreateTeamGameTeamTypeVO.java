package com.kaihei.esportingplus.api.vo.freeteam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class CreateTeamGameTeamTypeVO {

    /**
     * 游戏名+类型名
     */
    private String displayName;

    /**
     * 游戏Id
     */
    private Integer gameId;

    /**
     * 游戏名
     */
    private String gameName;

    /**
     * 位置
     */
    private Integer maxPositionCount;

    /**
     * 身份
     */
    private String userIdentityCode;
}
