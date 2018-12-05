package com.kaihei.esportingplus.gamingteam.api.vo.rpg;

import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberVO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * RPG 车队队员 VO
 * @author zhangfang
 */
public class RPGTeamMemberVO extends TeamMemberVO {

    private static final long serialVersionUID = -7090777136849141106L;
    
    /**
     * 车队队员昵称
     */
    private String username;

    /**
     * 车队队员头像
     */
    private String avatar;

    public RPGTeamMemberVO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
