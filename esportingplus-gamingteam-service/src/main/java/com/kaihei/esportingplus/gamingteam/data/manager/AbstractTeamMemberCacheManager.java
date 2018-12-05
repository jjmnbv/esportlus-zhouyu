package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.RedisUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 车队队员 redis 操作
 *
 * @author liangyi
 */
public abstract class AbstractTeamMemberCacheManager {

    protected static final Logger logger = LoggerFactory
            .getLogger(AbstractTeamMemberCacheManager.class);
    protected static CacheManager cacheManager = CacheManagerFactory.create();


    /**
     * 移除 redis中的车队队员--踢出队员、退出车队
     *
     * @param gameCode 游戏 code或 id
     * @param teamSequence 车队 id
     * @param uid 车队队员 uid
     */
    public void removeRedisTeamMember(Integer gameCode, String teamSequence, String uid) {
        String teamMemberKey = generateTeamMemberKey(teamSequence);
        String uidShardingKey = generateTeamMemberShardingKey(uid, gameCode);
        Pipeline pipelined = cacheManager.pipelined();
        // 从 redis 中移除该队员
        pipelined.hdel(teamMemberKey, uid);
        // 将 uid 从车队队员分片 key 中移除
        pipelined.hdel(uidShardingKey, uid);
        pipelined.sync();
        // 删除失败后异步重试 todo
        logger.info(">> 主动退出(或被踢出)车队, 从redis中删除车队[{}]中的用户[{}]", teamSequence, uid);
    }


    /**
     * 从 redis 中移除所有的车队数据 (包括车队信息和车队队员信息)--解散车队、结束车队
     *
     * @param gameCode 游戏 code 或 id
     * @param teamSequence 车队序列号
     * @param memberUidList 车队队员 uid 集合
     */
    public void removeAllRedisTeamData(Integer gameCode,
            String teamSequence, List<String> memberUidList) {
        String teamKey = String.format(RedisKey.TEAM_PREFIX, teamSequence);
        String teamMemberKey = generateTeamMemberKey(teamSequence);
        // 获取所有车队队员的分片 key
        // 批量操作
        Pipeline pipelined = cacheManager.pipelined();
        List<String> uidKeyList = new ArrayList<>();
        List<Response> uidShardingKeyResultList = new ArrayList<>();
        if (ObjectTools.isNotEmpty(memberUidList)) {
            for (String uid : memberUidList) {
                String uidShardingKey = generateTeamMemberShardingKey(uid, gameCode);
                uidKeyList.add(uidShardingKey);
                // redis 中移除该队员分片的 key
                Response<Long> uidShardingKeyHdel = pipelined.hdel(uidShardingKey, uid);
                uidShardingKeyResultList.add(uidShardingKeyHdel);
            }
        }
        // redis 中移除车队队员
        Response<Long> mdel = pipelined.del(teamMemberKey);
        // redis 中移除车队
        Response<Long> tdel = pipelined.del(teamKey);
        logger.info(">> 解散(或正常结束)车队[{}], 准备删除redis中该车队的所有数据", teamSequence);
        pipelined.sync();
        logger.info("===========================================");
        for (int i = 0; i < uidKeyList.size(); i++) {
            logger.info(">> pipelined.hdel 删除队员分片 key: {}, 结果: {}",
                    uidKeyList.get(i), uidShardingKeyResultList.get(i).get());
        }
        logger.info(">> pipelined.del 删除队员 key: {}, 结果: {}", teamMemberKey, mdel.get());
        logger.info(">> pipelined.del 删除车队 key: {}, 结果: {}", teamKey, tdel.get());
    }

    /**
     * 校验用户是否在当前车队中, 抛出异常
     *
     * @param teamSequence 车队序列号
     * @param uid 用户 uid
     * @param gameCode 游戏 code 或 id
     */
    public void checkUserInCurrentTeam(String teamSequence, String uid, Integer gameCode) {
        String redisTeamSequence = userInWhichTeam(uid, gameCode);
        logger.info(">> 校验用户[{}]在当前车队, redis 中获取到的车队序列号: {}, 当前车队序列号: {}",
                uid, redisTeamSequence, teamSequence);
        if (ObjectTools.isEmpty(redisTeamSequence)
                || !redisTeamSequence.equals(teamSequence)) {
            logger.error(">> 校验用户在当前车队, 用户[{}]不在当前车队[{}]中", uid, teamSequence);
            throw new BusinessException(BizExceptionEnum.USER_NOT_IN_CURRENT_TEAM);
        }
    }

    /**
     * 校验用户是否在其他车队中, 抛出异常
     * @param uid 用户 uid
     * @param gameCode 游戏 code 或 id
     */
    public void checkUserInOtherTeam(String uid, Integer gameCode) {
        String teamSequence = this.userInWhichTeam(uid, gameCode);
        if (ObjectTools.isNotEmpty(teamSequence)) {
            logger.error(">> 校验用户在其他车队, 用户[{}]已经在车队[{}]中", uid, teamSequence);
            throw new BusinessException(BizExceptionEnum.USER_IN_OTHER_TEAM);
        }
    }

    /**
     * 校验用户是否在其他车队中, 抛出异常
     *
     * @param gameCode 游戏 code 或 id
     */
    public void checkUserInOtherTeam(Integer gameCode) {
        UserSessionContext userSessionContext = UserSessionContext.getUser();
        checkUserInOtherTeam(userSessionContext.getUid(), gameCode);
    }

    /**
     * 校验用户是否已在车队中
     *
     * @param uid 用户 uid
     * @param gameCode 游戏 code 或 id
     * @return 车队序列号
     */
    public String userInWhichTeam(String uid, Integer gameCode) {
        String uidShardingKey = generateTeamMemberShardingKey(uid, gameCode);
        return cacheManager.hget(uidShardingKey, uid, String.class);
    }

    /**
     * 生成车队队员 key
     * @param teamSequence
     * @return
     */
    protected String generateTeamMemberKey (String teamSequence) {
        return String.format(RedisKey.TEAM_MEMBER_PREFIX, teamSequence);
    }

    /**
     * 生成车队队员所属游戏的分片 key
     * @param uid
     * @param gameCode
     * @return
     */
    protected String generateTeamMemberShardingKey (String uid, Integer gameCode) {
        return RedisUtils.buildHashSegmentKey(uid,
                RedisKey.TEAM_USER_GAME_PREFIX, ObjectTools.covertToString(gameCode));
    }
}
