package com.kaihei.esportingplus.gamingteam.domain.service;


import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.EndTeamLocalTransaction;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.LeaveTeamLocalTransaction;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.StartTeamLocalTransaction;

/**
 * 车队本地事务业务类
 *
 * @author liangyi
 */
public interface TeamTransactionService {

    /**
     * 立即开车
     * 涉及事务: 修改 DB 车队状态, 发布融云和微信通知
     * @param startTeamLocalTransaction
     * @return
     */
    boolean startTeam(StartTeamLocalTransaction startTeamLocalTransaction);

    /**
     * 离开车队
     * 涉及事务: 发布融云和微信通知
     * @param leaveTeamLocalTransaction
     */
    void leaveTeam(LeaveTeamLocalTransaction leaveTeamLocalTransaction);

    /**
     * 结束车队
     * 涉及事务: 修改车队数据, 删除 redis 中的车队数据
     * @param endTeamLocalTransaction 车队序列号
     * @return
     */
    boolean endTeam(EndTeamLocalTransaction endTeamLocalTransaction);

    /**
     * 修改车队状态
     * @param teamSequence
     * @param teamStatus
     * @return
     */
    int updateTeamStatus(String teamSequence, Integer teamStatus);
}
