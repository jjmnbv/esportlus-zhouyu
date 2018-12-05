package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import com.kaihei.esportingplus.common.data.Castable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class PVPRedisTeamMemberVO implements Castable {

    /**
     * 车队队员UID
     */
    private String uid;

    /**
     * 车队队员昵称
     */
    private String username;

    /**
     * 暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     */
    private Integer baojiLevel;

    /**
     * 车队队员身份，0：老板，1：暴鸡，2：暴娘，9：队长
     */
    private Integer userIdentity;

    /**
     * 车队队员头像
     */
    private String avatar;

    /**
     * 游戏区Id
     */
    private Integer gameZoneId;

    /**
     * 游戏区名
     */
    private String gameZoneName;

    /**
     * 上车时间
     */
    private Date joinTime;

    /**
     * 车队队员状态(0: 待支付(只针对老板), 1: 准备入团, 2: 已入团)
     */
    private Integer status;

    /**
     * 所属车队ID
     */
    private Long teamId;

    /**
     * 所属车队序列号
     */
    private String teamSquence;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;
}
