package com.kaihei.esportingplus.gamingteam.data.manager.rpg;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamVO;
import com.kaihei.esportingplus.gamingteam.config.GamingTeamConfig;
import com.kaihei.esportingplus.gamingteam.data.manager.AbstractTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamGameResultRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameRPG;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RPG 车队 redis 操作
 * @author liangyi
 */
@Service("rpgTeamCacheManager")
public class RPGTeamCacheManager extends AbstractTeamCacheManager {

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamGameResultRepository teamGameResultRepository;
    @Autowired
    GamingTeamConfig gamingTeamConfig;

    /**
     * 构建 redis中的 RPG车队信息
     * @param team
     * @param teamGameRPG
     * @return
     */
    public RPGRedisTeamVO buildRPGRedisTeam(Team team, TeamGameRPG teamGameRPG) {
        RPGRedisTeamVO rpgRedisTeamVO = BeanMapper.map(team, RPGRedisTeamVO.class);
        BeanMapper.map(teamGameRPG, rpgRedisTeamVO);
        rpgRedisTeamVO.setId(team.getId());
        rpgRedisTeamVO.setPaymentTimeout(gamingTeamConfig.getPaymentTimeout());
        return rpgRedisTeamVO;
    }


    /**
     * 保存 RPG车队信息到 redis
     * @param rpgRedisTeamVO
     */
    public void saveRPGRedisTeam(RPGRedisTeamVO rpgRedisTeamVO) {
        String teamKey = generateTeamKey(rpgRedisTeamVO.getSequence());
        cacheManager.set(teamKey, JsonsUtils.toJson(rpgRedisTeamVO));
    }


    /**
     *
     * @param sequence
     * @return
     */
    public RPGRedisTeamVO queryTeamInfoBySequence(String sequence) {
        String teamKey = generateTeamKey(sequence);
        RPGRedisTeamVO rpgRedisTeamVO;
        rpgRedisTeamVO = cacheManager.get(teamKey, RPGRedisTeamVO.class);
        if (ObjectTools.isNull(rpgRedisTeamVO)) {
            logger.warn(">> 未从 redis 中查询到车队[{}]的数据, 继续从 DB 中查询", sequence);
            rpgRedisTeamVO = teamRepository.selectBySequence(sequence);
            if (ObjectTools.isNull(rpgRedisTeamVO)) {
                logger.error(">> 未从 DB 中查询到车队[{}]的数据", sequence);
                throw new BusinessException(BizExceptionEnum.TEAM_NOT_EXIST);
            }
            // 这里从数据库里查到的数据不要再加入到 redis 中了...
        }
        if (null == rpgRedisTeamVO.getPaymentTimeout()
                || 0 == rpgRedisTeamVO.getPaymentTimeout()) {
            // 由于数据库中没有存老板超时的字段
            // 所以这里需要从配置中读取
            rpgRedisTeamVO.setPaymentTimeout(gamingTeamConfig.getPaymentTimeout());
        }
        return rpgRedisTeamVO;
    }


    /**
     * 查询车队比赛结果
     * @param sequence
     * @return
     */
    public TeamGameResultVO queryTeamGameResultBySequence(String sequence) {
        String teamGameResultKey = String.format(RedisKey.TEAM_GAME_RESULT_PREFIX, sequence);
        TeamGameResultVO teamGameResultVO;
        teamGameResultVO = cacheManager.get(teamGameResultKey, TeamGameResultVO.class);
        if (ObjectTools.isEmpty(teamGameResultVO)) {
            List<TeamGameResultVO> gameResultList = teamGameResultRepository
                    .selectByTeamSequence(sequence);
            if (ObjectTools.isNotEmpty(gameResultList)) {
                // RPG 类(目前只有 DNF)只有一个比赛结果
                teamGameResultVO = gameResultList.get(0);
                GameResultEnum gameResultEnum = GameResultEnum
                        .fromCode(teamGameResultVO.getGameResultCode().intValue());
                if (ObjectTools.isNotEmpty(gameResultEnum)) {
                    teamGameResultVO.setGameResultDesc(gameResultEnum.getDesc());
                    teamGameResultVO.setTeamSequence(sequence);
                    teamGameResultVO.setResultSequence(1);
                    // 设置过期时间两小时
                    cacheManager.set(teamGameResultKey, JsonsUtils.toJson(teamGameResultVO),
                            CommonConstants.ONE_HOUR_SECONDS * 2);
                }
            } else {
                logger.error(">> 未查询到车队[{}]的比赛结果数据", sequence);
                throw new BusinessException(BizExceptionEnum.TEAM_NOT_EXIST);
            }
        }
        return teamGameResultVO;
    }

}
