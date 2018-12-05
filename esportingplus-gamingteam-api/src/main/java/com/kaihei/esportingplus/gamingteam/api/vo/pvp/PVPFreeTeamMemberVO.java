package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberVO;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队队员信息
 * @author liangyi
 */
@AllArgsConstructor
@NoArgsConstructor
public class PVPFreeTeamMemberVO extends TeamMemberVO {

    private static final long serialVersionUID = -8046001151829794647L;

    /**
     * 车队队员昵称
     */
    private String username;

    /**
     * 鸡牌号
     */
    private Integer chickenId;

    /**
     * 融云 uid
     */
    private String rcUid;

    /**
     * 车队队员头像
     */
    private String avatar;

    /**
     * 队员段位名称 (暴鸡队长显示空字符串)
     */
    private String gameDanName;

    /**
     * 暴鸡开车数量 todo 这个版本先不做这个字段
     */
    private Integer driveCount = 0;

    /**
     * 车队队员外显状态
     * {@link com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberDisplayStatus}
     */
    private Integer status;

    /**
     * 上车时间
     */
    @JsonIgnore
    private Date joinTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getChickenId() {
        return chickenId;
    }

    public void setChickenId(Integer chickenId) {
        this.chickenId = chickenId;
    }

    public String getRcUid() {
        return rcUid;
    }

    public void setRcUid(String rcUid) {
        this.rcUid = rcUid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGameDanName() {
        if (gameDanName == null) {
            gameDanName = "";
        }
        return gameDanName;
    }

    public void setGameDanName(String gameDanName) {
        this.gameDanName = gameDanName;
    }

    public Integer getDriveCount() {
        return driveCount;
    }

    public void setDriveCount(Integer driveCount) {
        this.driveCount = driveCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
