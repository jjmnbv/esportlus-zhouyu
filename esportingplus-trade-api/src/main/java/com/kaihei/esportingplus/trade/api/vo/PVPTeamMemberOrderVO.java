package com.kaihei.esportingplus.trade.api.vo;

/**
 * @author zhangfang
 */
public class PVPTeamMemberOrderVO extends TeamMemberOrderVo {

    private static final long serialVersionUID = 5046202552059129377L;
    /**
     * 段位名称
     */
    private String gameDanName;

    public String getGameDanName() {
        return gameDanName;
    }

    public void setGameDanName(String gameDanName) {
        this.gameDanName = gameDanName;
    }
}
