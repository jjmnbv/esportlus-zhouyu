package com.kaihei.esportingplus.gamingteam.domain.entity;

import com.kaihei.esportingplus.common.data.Castable;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Builder
@Table(name = "team_member_pvp")
@Data
@AllArgsConstructor
public class TeamMemberPVP implements Castable, TeamMember {

    /**
     * PVP车队队员表主键ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 所属车队ID
     */
    @Column(name = "team_id")
    private Long teamId;

    /**
     * 车队队员UID
     */
    private String uid;

    /**
     * 车队队员昵称
     */
    private String username;

    /**
     * 车队队员头像
     */
    private String avatar;

    /**
     * 车队队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    @Column(name = "user_identity")
    private Byte userIdentity;

    /**
     * 暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     */
    @Column(name = "baoji_level")
    private Integer baojiLevel;

    /**
     * 车队队员游戏段位ID
     */
    @Column(name = "game_dan_id")
    private Integer gameDanId;

    /**
     * 车队队员游戏段位名称
     */
    @Column(name = "game_dan_name")
    private String gameDanName;

    /**
     * 车队队员状态, 0:待准备, 1:已准备(针对暴鸡,针对老板是已准备待支付), 2:已支付(针对老板), 3:开车后退出
     */
    private Byte status;

    @Transient
    private String chickenId;

    /**
     * 上车时间
     */
    @Column(name = "join_time")
    private Date joinTime;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    @Transient
    private List<Long> couponsIds;

    public TeamMemberPVP(Long id, Long teamId, String uid, String username, String avatar, Byte userIdentity, Integer baojiLevel, Integer gameDanId, String gameDanName, Byte status, Date joinTime, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.teamId = teamId;
        this.uid = uid;
        this.username = username;
        this.avatar = avatar;
        this.userIdentity = userIdentity;
        this.baojiLevel = baojiLevel;
        this.gameDanId = gameDanId;
        this.gameDanName = gameDanName;
        this.status = status;
        this.joinTime = joinTime;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public TeamMemberPVP() {
        super();
    }

    /**
     * 获取PVP车队队员表主键ID
     *
     * @return id - PVP车队队员表主键ID
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * 设置PVP车队队员表主键ID
     *
     * @param id PVP车队队员表主键ID
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取所属车队ID
     *
     * @return team_id - 所属车队ID
     */
    @Override
    public Long getTeamId() {
        return teamId;
    }

    /**
     * 设置所属车队ID
     *
     * @param teamId 所属车队ID
     */
    @Override
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    /**
     * 获取车队队员UID
     *
     * @return uid - 车队队员UID
     */
    @Override
    public String getUid() {
        return uid;
    }

    /**
     * 设置车队队员UID
     *
     * @param uid 车队队员UID
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    /**
     * 获取车队队员昵称
     *
     * @return username - 车队队员昵称
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 设置车队队员昵称
     *
     * @param username 车队队员昵称
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取车队队员头像
     *
     * @return avatar - 车队队员头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置车队队员头像
     *
     * @param avatar 车队队员头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    /**
     * 获取车队队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     *
     * @return user_identity - 车队队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    @Override
    public Byte getUserIdentity() {
        return userIdentity;
    }

    /**
     * 设置车队队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     *
     * @param userIdentity 车队队员身份，0：老板，1：暴鸡，2：暴娘，3：队长
     */
    @Override
    public void setUserIdentity(Byte userIdentity) {
        this.userIdentity = userIdentity;
    }

    /**
     * 获取暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     *
     * @return baoji_level - 暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     */
    @Override
    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    /**
     * 设置暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     *
     * @param baojiLevel 暴鸡等级, 100: 普通暴鸡, 101: 优选暴鸡, 102: 超级暴鸡, 暴娘: 300
     */
    @Override
    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    /**
     * 获取车队队员游戏段位ID
     *
     * @return game_dan_id - 车队队员游戏段位ID
     */
    @Override
    public Integer getGameDanId() {
        return gameDanId;
    }

    /**
     * 设置车队队员游戏段位ID
     *
     * @param gameDanId 车队队员游戏段位ID
     */
    @Override
    public void setGameDanId(Integer gameDanId) {
        this.gameDanId = gameDanId;
    }

    /**
     * 获取车队队员游戏段位名称
     *
     * @return game_dan_name - 车队队员游戏段位名称
     */
    @Override
    public String getGameDanName() {
        return gameDanName;
    }

    /**
     * 设置车队队员游戏段位名称
     *
     * @param gameDanName 车队队员游戏段位名称
     */
    @Override
    public void setGameDanName(String gameDanName) {
        this.gameDanName = gameDanName == null ? null : gameDanName.trim();
    }

    /**
     * 获取车队队员状态, 0:待准备, 1:已准备(针对暴鸡,针对老板是已准备待支付), 2:已支付(针对老板), 3:开车后退出
     *
     * @return status - 车队队员状态, 0:待准备, 1:已准备(针对暴鸡,针对老板是已准备待支付), 2:已支付(针对老板), 3:开车后退出
     */
    @Override
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置车队队员状态, 0:待准备, 1:已准备(针对暴鸡,针对老板是已准备待支付), 2:已支付(针对老板), 3:开车后退出
     *
     * @param status 车队队员状态, 0:待准备, 1:已准备(针对暴鸡,针对老板是已准备待支付), 2:已支付(针对老板), 3:开车后退出
     */
    @Override
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取上车时间
     *
     * @return join_time - 上车时间
     */
    public Date getJoinTime() {
        return joinTime;
    }

    /**
     * 设置上车时间
     *
     * @param joinTime 上车时间
     */
    @Override
    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    @Override
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    @Override
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    @Override
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}