package com.kaihei.esportingplus.gamingteam.domain.service;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamGameResultRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamMemberRPGRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameResult;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcEndTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcLeaveTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcStartTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinOrderCancelEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinOrderEndEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinStartTeamEvent;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.EndTeamLocalTransaction;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.LeaveTeamLocalTransaction;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.StartTeamLocalTransaction;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author liangyi
 */
@Service("teamTransactionService")
public class TeamTransactionServiceImpl implements TeamTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TeamTransactionServiceImpl.class);
    private CacheManager cacheManager = CacheManagerFactory.create();

    private static final String TEAM_START_ACTION = "start";
    private static final String TEAM_END_ACTION = "end";

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamGameResultRepository teamGameResultRepository;
    @Autowired
    TeamMemberRPGRepository teamMemberRPGRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTeam(StartTeamLocalTransaction startTeamLocalTransaction) {
        String teamSequence = startTeamLocalTransaction.getTeamSequence();
        boolean idempotenceFlag = checkIdempotenceFlag(teamSequence, TEAM_START_ACTION);
        if (!idempotenceFlag) {
            // 此时说明已经被执行过一次了...直接返回 false
            return false;
        }
        Integer teamStatus = startTeamLocalTransaction.getTeamStatus();
        if (TeamStatusEnum.RUNNING.getCode() != teamStatus) {
            // 车队不是进行中时, 修改数据库中的车队状态直接抛出异常
            logger.error(">> 立即开车本地事务, 更新 DB 中的车队[{}]状态[{}]错误!",
                    teamSequence, teamStatus);
            throw new BusinessException(BizExceptionEnum.TEAM_STATUS_ERROR);
        }
        int result = teamRepository.updateTeamStatus(teamSequence, teamStatus);
        logger.info(">> 立即开车本地事务, 成功更新 DB 中的车队[{}]状态[{}]", teamSequence, teamStatus);
        if (result > 0) {
            // 异步发布消息通知
            RcStartTeamEvent rcStartTeamEvent = startTeamLocalTransaction
                    .getRcStartTeamEvent();
            if (ObjectTools.isNotNull(rcStartTeamEvent)) {
                EventBus.post(rcStartTeamEvent);
            }
            WeXinStartTeamEvent weXinStartTeamEvent = startTeamLocalTransaction
                    .getWeXinStartTeamEvent();
            if (ObjectTools.isNotNull(weXinStartTeamEvent)) {
                EventBus.post(weXinStartTeamEvent);
            }
            // 本地事务成功执行, 添加 redis 标记实现幂等性
            setIdempotenceFlag(teamSequence, TEAM_START_ACTION);
            return true;
        }
        logger.error(">> 立即开车本地事务, 更新 DB 中的车队[{}]状态[{}]错误!",
                teamSequence, teamStatus);
        return false;
    }

    @Override
    public void leaveTeam(LeaveTeamLocalTransaction leaveTeamLocalTransaction) {
        RcLeaveTeamEvent rcLeaveTeamEvent = leaveTeamLocalTransaction
                .getRcLeaveTeamEvent();
        if (ObjectTools.isNotNull(rcLeaveTeamEvent)) {
            // 异步发布融云离开车队事件
            EventBus.post(rcLeaveTeamEvent);
        }
        WeXinOrderCancelEvent weXinOrderCancelEvent = leaveTeamLocalTransaction
                .getWeXinOrderCancelEvent();
        if (ObjectTools.isNotNull(weXinOrderCancelEvent)) {
            // 异步发布微信订单取消服务通知
            EventBus.post(weXinOrderCancelEvent);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean endTeam(EndTeamLocalTransaction endTeamLocalTransaction) {
        String teamSequence = endTeamLocalTransaction.getTeamSequence();
        boolean idempotenceFlag = checkIdempotenceFlag(teamSequence, TEAM_END_ACTION);
        if (!idempotenceFlag) {
            // 此时说明已经被执行过一次了...直接返回 false
            return false;
        }
        Integer teamStatus = endTeamLocalTransaction.getTeamStatus();
        Integer gameResult = endTeamLocalTransaction.getGameResult();
        boolean persistResult = persistenceTeamData(endTeamLocalTransaction.getTeamId(),
                teamSequence, teamStatus, gameResult,
                endTeamLocalTransaction.getPersistenceMemberList());
        if (persistResult) {
            logger.info("解散(或结束)车队[{}], 成功保存车队数据到 DB中", teamSequence);
            // 异步发布消息通知
            RcEndTeamEvent rcEndTeamEvent = endTeamLocalTransaction.getRcEndTeamEvent();
            if (ObjectTools.isNotNull(rcEndTeamEvent)) {
                EventBus.post(rcEndTeamEvent);
            }
            WeXinOrderCancelEvent weXinOrderCancelEvent = endTeamLocalTransaction
                    .getWeXinOrderCancelEvent();
            if (ObjectTools.isNotNull(weXinOrderCancelEvent)) {
                EventBus.post(weXinOrderCancelEvent);
            }
            WeXinOrderEndEvent weXinOrderEndEvent = endTeamLocalTransaction
                    .getWeXinOrderEndEvent();
            if (ObjectTools.isNotNull(weXinOrderEndEvent)) {
                EventBus.post(weXinOrderEndEvent);
            }
            // 本地事务成功执行, 添加 redis 标记实现幂等性
            setIdempotenceFlag(teamSequence, TEAM_END_ACTION);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTeamStatus(String teamSequence, Integer teamStatus) {
        logger.info(">> 解散(或正常结束)车队[{}], 准备更新 DB 中的车队数据, 车队状态: {}",
                teamSequence, teamStatus);
        return teamRepository.updateTeamStatus(teamSequence, teamStatus);
    }

    /**
     * 车队数据入库
     * @param teamId
     * @param teamSequence
     * @param teamStatus
     * @param gameResult
     * @param persistenceMemberList
     */
    private boolean persistenceTeamData(Long teamId, String teamSequence, Integer teamStatus,
            Integer gameResult, List<RPGRedisTeamMemberVO> persistenceMemberList) {
        // 车队状态更新, 此时的车队状态一定是已解散或已完成
        if (TeamStatusEnum.DISMISSED.getCode() != teamStatus
                && TeamStatusEnum.COMPLETED.getCode() != teamStatus) {
            logger.error(">> 解散(或正常结束)车队[{}], 准备更新 DB 中的车队, "
                            + "车队状态[{}]错误, 比赛结果: {}",
                    teamSequence, teamStatus, gameResult);
            return false;
        }
        // 修改车队状态
        teamRepository.updateTeamStatus(teamSequence, teamStatus);
        // 保存比赛结果
        teamGameResultRepository.insertSelective(TeamGameResult.builder()
                .teamId(teamId)
                .gameResult(gameResult)
                // DNF 只有一局
                .resultSequence(1).build());
        // 车队队员数据持久化
        if (ObjectTools.isEmpty(persistenceMemberList)) {
            // 车队队员列表为空, 无需保存到 DB
            return true;
        }
        int memberResult = teamMemberRPGRepository.insertMemberList(persistenceMemberList);
        logger.info(">> 解散(或正常结束)车队[{}], 成功执行保存车队队员数据到 DB, 队员数量: {}, "
                        + "受影响的行数: {}",
                teamSequence, persistenceMemberList.size(), memberResult);
        if (memberResult > 0) {
            return true;
        }
        return false;
    }


    public boolean checkIdempotenceFlag(String teamSequence, String teamAction) {
        String teamActionKey = getTeamActionKey(teamSequence, teamAction);
        String idempotenceFlag = cacheManager.get(teamActionKey, String.class);
        // 如果标识已存在则说明本地事务已经被执行过一次了, 本地事务此时直接返回 false, 否则继续流程
        logger.info(">> MQ本地事务幂等标记检查!车队:[{}]当前动作:[{}],幂等标记值:[{}]",
                teamSequence, teamAction, idempotenceFlag);
        return null == idempotenceFlag;
    }

    private void setIdempotenceFlag(String teamSequence, String teamAction) {
        String teamActionKey = getTeamActionKey(teamSequence, teamAction);
        // 放置一个标记, 值设置为车队序列号, 过期时间设置为 5分钟
        cacheManager.set(teamActionKey, teamSequence, CommonConstants.ONE_HOUR_SECONDS/12);
    }

    private String getTeamActionKey(String teamSequence, String teamAction) {
        return String.format(RedisKey.TEAM_LOCAL_TRANSACTION_IDEMPOTENCE,
                teamSequence, teamAction);
    }
}
