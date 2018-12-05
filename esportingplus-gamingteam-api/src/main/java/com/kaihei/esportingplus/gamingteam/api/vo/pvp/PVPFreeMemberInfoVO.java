package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import com.kaihei.esportingplus.gamingteam.api.vo.BaojiInfoBaseVO;

public class PVPFreeMemberInfoVO extends BaojiInfoBaseVO {

    private static final long serialVersionUID = 2883911182604327531L;

    /**
     * 段位id
     */
    private Integer gameDanId;
    /**
     * 段位名称
     */

    private String gameDanName;
    /**
     * 鸡排号
     */
    private String userChickenId;
    /**
     * 用户昵称
     */
    private String userNickname;

    private String avatar;
    public Integer getGameDanId() {
        return gameDanId;
    }

    public void setGameDanId(Integer gameDanId) {
        this.gameDanId = gameDanId;
    }

    public String getGameDanName() {
        return gameDanName;
    }

    public void setGameDanName(String gameDanName) {
        this.gameDanName = gameDanName;
    }

    public String getUserChickenId() {
        return userChickenId;
    }

    public void setUserChickenId(String userChickenId) {
        this.userChickenId = userChickenId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
