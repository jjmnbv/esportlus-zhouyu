package com.kaihei.esportingplus.gamingteam.data.manager.core.model;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 简易Model
 */
@Data
public class BaseTeamMember implements TeamMember {

    private Long id;

    private String uid;

    private Long teamId;

    private Date gmtModified;

    private String username;

    private Integer gameDanId;

    private String gameDanName;

    private Byte userIdentity;

    private Integer baojiLevel;

    private Byte status;

    private String chickenId;

    private Date joinTime;

    private Date gmtCreate;

    private List<Long> couponsIds;
}
