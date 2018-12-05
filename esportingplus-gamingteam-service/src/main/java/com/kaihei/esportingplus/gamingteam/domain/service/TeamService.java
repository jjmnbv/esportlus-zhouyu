package com.kaihei.esportingplus.gamingteam.domain.service;


import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;

/**
 * 车队业务类
 *
 * @author liangyi
 */
public interface TeamService {

    /**
     * 退出车队
     * @param sequence
     */
    void quitTeam(String sequence);

    /**
     * 踢出车队
     * @param sequence
     * @param uid
     */
    void kickOutTeamMember(String sequence, String uid);

    /**
     * 立即开车
     * @param sequence
     */
    void startTeam(String sequence);

    /**
     * 修改车队位置数量
     * @param uid
     * @param positionCountParams
     */
    void updateTeamPositioncount(String uid,UpdatePositionCountParams positionCountParams);


    /**
     * 获取老板支付的倒计时
     * @param sequence
     * @return
     */
    Integer getBossPayCountdown(String sequence);

}
