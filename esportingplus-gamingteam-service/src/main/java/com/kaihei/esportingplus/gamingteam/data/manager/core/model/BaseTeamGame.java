package com.kaihei.esportingplus.gamingteam.data.manager.core.model;

import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.data.JsonSerializable;
import com.kaihei.esportingplus.gamingteam.data.manager.core.populator.TeamCreateLeaderParamPopulator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.populator.TeamGameDanNameParamPopulator;
import java.util.Date;
import lombok.Data;

/**
 * 简易Model
 */
@Data
public class BaseTeamGame implements TeamGame {

    private Long id;
    // --------------------------前端传入------------------------------ //

    /**
     * 由前端传或通过免费车队类型推算
     */
    private Integer gameId;

    /**
     * 游戏大区Id
     */
    private Integer gameZoneId;

    /**
     * 最低段位ID
     */
    private Integer lowerDanId;

    /**
     * 最高段位Id
     */
    private Integer upperDanId;

    // ----------------------------系统自己生成-------------------------------------- //

    /**
     * 雪花算法 {@link SnowFlake#nextId()}
     */
    private Long teamId;

    /**
     * {@link com.kaihei.esportingplus.gamingteam.domain.service.AbstractTeamService#createTeam(JsonSerializable)}
     *
     * 生成时写死
     */
    private Date gmtModified;

    /**
     * {@link com.kaihei.esportingplus.gamingteam.domain.service.AbstractTeamService#createTeam(JsonSerializable)}
     *
     * 生成时写死
     */
    private Date gmtCreate;

    // ---------------------------填充器填充----------------------------------//

    /**
     * {@link TeamCreateLeaderParamPopulator#doPopulate()}
     */

    private String gameZoneName;

    private String gameName;

    /**
     * {@link TeamGameDanNameParamPopulator#doPopulate()}
     */
    private String lowerDanName;

    private String upperDanName;
}
