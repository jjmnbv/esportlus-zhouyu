package com.kaihei.esportingplus.gamingteam.schedule;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.RedisUtils;
import com.kaihei.esportingplus.gamingteam.annotation.RedisDistlock;
import com.kaihei.esportingplus.gamingteam.api.enums.ImMemberOutReasonEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamVO;
import com.kaihei.esportingplus.gamingteam.data.manager.rpg.RPGTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcLeaveTeamEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Pipeline;

/**
 * 老板到期未支付, 直接被系统踢出车队
 * @author liangyi
 */
@Component
public class BossPaymentTimeOutJob implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(BossPaymentTimeOutJob.class);

    private static CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    RPGTeamCacheManager rpgTeamCacheManager;

    @Override
    public void execute(ShardingContext shardingContext) {
        // 分片数, 目前只有一个分片, 后续增加后需要对应修改
        int shardingItem = shardingContext.getShardingItem();
        String jobName = shardingContext.getJobName();
        switch (shardingItem) {
            case 0:
                if (ObjectTools.isNotEmpty(jobName)) {
                    String[] split = jobName.split("_");
                    String teamSequence = split[1];
                    String bossUid = split[2];
                    kickOutBossBySystem(teamSequence, bossUid);
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }

    /**
     * 系统踢出加入车队后超时未支付的老板
     * @param teamSequence 车队序列号
     * @param bossUid 老板 uid
     */
    @RedisDistlock(expireTime = 300)
    private void kickOutBossBySystem(String teamSequence, String bossUid) {
        // 1. 获取当前的车队信息
        RPGRedisTeamVO RPGRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(teamSequence);
        // 2. 校验车队异常状态--只有车队在准备中时才执行定时踢出任务
        Integer teamStatus = RPGRedisTeamVO.getStatus();
        if (TeamStatusEnum.PREPARING.getCode() != teamStatus) {
            logger.error(">> 系统踢出车队[{}]中超时未支付的老板[{}], 车队状态[{}]不是准备中",
                    teamSequence, bossUid, teamStatus);
        }
        // 3. 校验老板是否在车队中
        String uidShardingKey = RedisUtils.buildHashSegmentKey(bossUid,
                RedisKey.TEAM_USER_GAME_PREFIX,
                ObjectTools.covertToString(RPGRedisTeamVO.getGameCode()));
        boolean b = cacheManager.hexists(uidShardingKey, bossUid);
        if (!b) {
            // 老板已退出
            return ;
        }
        // 4. 获取老板信息
        String teamMemberKey = String.format(RedisKey.TEAM_MEMBER_PREFIX, RPGRedisTeamVO.getId());
        RPGRedisTeamMemberVO bossMember = cacheManager
                .hget(teamMemberKey, bossUid, RPGRedisTeamMemberVO.class);
        if (ObjectTools.isNull(bossMember)) {
            logger.error(">> 系统踢出车队[{}]中超时未支付的老板[{}], 未在 redis 中查询到该老板的数据",
                    teamSequence, bossUid);
        }
        Integer userIdentity = bossMember.getUserIdentity();
        if (UserIdentityEnum.BOSS.getCode() != userIdentity) {
            // 校验是否是老板, 不是抛出异常
            logger.error(">> 系统踢出车队[{}]中超时未支付的老板[{}], 该队员不是老板身份",
                    teamSequence, bossUid);
        }
        Integer status = bossMember.getStatus();
        if (TeamMemberStatusEnum.WAIT_FOR_PAY.getCode() != status) {
            // 如果不是待支付状态则不执行定时任务
            return ;
        }
        // 5. 获取车队中的其他队员, 准备推送退出车队消息
        List<String> otherUidList = new ArrayList<>();
        Map<String, RPGRedisTeamMemberVO> memberVOMap = cacheManager
                .hgetAll(teamMemberKey, RPGRedisTeamMemberVO.class);
        if (ObjectTools.isEmpty(memberVOMap)
                || ObjectTools.isEmpty(memberVOMap.values())) {
            logger.error(">> 系统踢出车队[{}]中超时未支付的老板[{}], 车队中无队员",
                    teamSequence, bossUid);
        }
        List<RPGRedisTeamMemberVO> memberVOS = new ArrayList<>(memberVOMap.values());
        for (RPGRedisTeamMemberVO teamMemberVO : memberVOS) {
            otherUidList.add(teamMemberVO.getUid());
        }
        otherUidList.remove(bossUid);
        // 6. 移除 redis 中的老板
        String jobShardingKey = RedisUtils
                .buildHashSegmentKey(bossUid, RedisKey.TEAM_BOSS_JOB_PREFIX);
        Pipeline pipelined = cacheManager.pipelined();
        // 从 redis 中移除该队员
        pipelined.hdel(teamMemberKey, bossUid);
        // 将 uid 从车队队员分片 key 中移除
        pipelined.hdel(uidShardingKey, bossUid);
        // 删除老板定时任务 key
        pipelined.hdel(jobShardingKey, bossUid);
        pipelined.sync();
        logger.info(">> 系统踢出车队[{}]中超时未支付的老板[{}],删除 redis中的老板数据",
                teamSequence, bossMember);
        // 7. 发布离开车队时的融云推送事件
        RcLeaveTeamEvent rcLeaveTeamEvent = new RcLeaveTeamEvent();
        rcLeaveTeamEvent.setTeamSequence(RPGRedisTeamVO.getSequence());
        rcLeaveTeamEvent.setTeamTitle(RPGRedisTeamVO.getTitle());
        rcLeaveTeamEvent.setUid(bossMember.getUid());
        rcLeaveTeamEvent.setUsername(bossMember.getUsername());
        rcLeaveTeamEvent.setOtherUidList(otherUidList);
        rcLeaveTeamEvent.setOutReason(ImMemberOutReasonEnum.PAID_OUT_TIME);
        rcLeaveTeamEvent.setKickOut(true);
        EventBus.post(rcLeaveTeamEvent);
    }
}
