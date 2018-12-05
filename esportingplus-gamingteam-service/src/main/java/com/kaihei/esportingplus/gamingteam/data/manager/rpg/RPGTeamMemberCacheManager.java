package com.kaihei.esportingplus.gamingteam.data.manager.rpg;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.data.manager.AbstractTeamMemberCacheManager;
import com.kaihei.esportingplus.user.api.feign.UserGameRoleServiceClient;
import com.kaihei.esportingplus.user.api.params.UserSingeRoleQueryParams;
import com.kaihei.esportingplus.user.api.vo.UserGameUserCredentialVo;
import com.kaihei.esportingplus.user.api.vo.UserSingleRoleDetailInfoVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Pipeline;

/**
 * RPG 车队队员 redis 操作
 *
 * @author liangyi
 */
@Service("rpgTeamMemberCacheManager")
public class RPGTeamMemberCacheManager extends AbstractTeamMemberCacheManager{

    @Autowired
    UserGameRoleServiceClient userGameRoleServiceClient;

    /**
     * 创建 RPG车队队员--创建车队、加入车队
     *
     * @param userSessionContext 用户信息
     * @param gameCode 游戏 code
     * @param raidCode 副本 code
     * @param gameRoleId 角色 id
     * @param userIdentity 用户身份
     * @return 车队队员信息
     */
    public RPGRedisTeamMemberVO buildRPGRedisTeamMember(UserSessionContext userSessionContext,
            Integer gameCode, Integer raidCode, long gameRoleId, Integer userIdentity) {

        UserSingeRoleQueryParams userSingeRoleQueryParams = new UserSingeRoleQueryParams();
        userSingeRoleQueryParams.setRaidCode(raidCode);
        userSingeRoleQueryParams.setRoleId(gameRoleId);
        userSingeRoleQueryParams.setUid(userSessionContext.getUid());
        userSingeRoleQueryParams.setGameCode(gameCode);
        userSingeRoleQueryParams.setUserIdentity(userIdentity);

        logger.info(">> 创建(或加入)车队, 开始调用用户服务查询用户角色信息, 参数: {}",
                userSingeRoleQueryParams);
        long start = System.currentTimeMillis();
        ResponsePacket<UserSingleRoleDetailInfoVo> userRoleInfoResp = userGameRoleServiceClient
                .getUserIdentityRoleInfo(userSingeRoleQueryParams);
        logger.info(">> 创建(或加入)车队, 调用用户服务查询用户角色信息耗时: {} ms",
                (System.currentTimeMillis() - start));
        if (!userRoleInfoResp.responseSuccess()) {
            logger.error(">> 创建(或加入)车队, 调用用户服务查询角色信息错误: {}", userRoleInfoResp);
            throw new BusinessException(BizExceptionEnum.USER_GAME_ROLE_NOT_EXIST);
        }
        UserSingleRoleDetailInfoVo singleRoleDetail = userRoleInfoResp.getData();
        ValidateAssert.allNotNull(BizExceptionEnum.USER_GAME_ROLE_NOT_EXIST, singleRoleDetail);
        // 基本信息
        Integer raidLocationCode = null;
        String raidLocationName = null;
        Integer baojiLevel = null;
        if (UserIdentityEnum.BAOJI.getCode() == userIdentity) {
            // 暴鸡--含有认证信息
            UserGameUserCredentialVo credential = singleRoleDetail.getCredential();
            ValidateAssert.allNotNull(BizExceptionEnum.USER_GAME_ROLE_NOT_EXIST, credential);
            raidLocationCode = credential.getRaidLocationCode();
            raidLocationName = credential.getRaidLocationName();
            baojiLevel = credential.getBaojiLevel();
        } else if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
            // 老板按默认值设置
            raidLocationCode = 0;
            raidLocationName = "";
            baojiLevel = 0;
        }
        RPGRedisTeamMemberVO rpgRedisTeamMemberVO = new RPGRedisTeamMemberVO();
        rpgRedisTeamMemberVO.setUid(userSessionContext.getUid());
        rpgRedisTeamMemberVO.setAvatar(userSessionContext.getAvatar());
        rpgRedisTeamMemberVO.setUsername(userSessionContext.getUsername());
        rpgRedisTeamMemberVO.setCareerCode(singleRoleDetail.getCareerCode());
        rpgRedisTeamMemberVO.setCareerName(singleRoleDetail.getCareerName());
        rpgRedisTeamMemberVO.setGameRoleId(singleRoleDetail.getId());
        rpgRedisTeamMemberVO.setGameRoleName(singleRoleDetail.getName());
        rpgRedisTeamMemberVO.setRaidLocationCode(raidLocationCode);
        rpgRedisTeamMemberVO.setRaidLocationName(raidLocationName);
        rpgRedisTeamMemberVO.setZoneSmallCode(singleRoleDetail.getZoneSmallCode());
        rpgRedisTeamMemberVO.setZoneSmallName(singleRoleDetail.getZoneSmallName());
        rpgRedisTeamMemberVO.setBaojiLevel(baojiLevel);
        rpgRedisTeamMemberVO.setJoinTime(new Date());
        rpgRedisTeamMemberVO.setGmtCreate(new Date());

        return rpgRedisTeamMemberVO;
    }

    /**
     * 将 RPG车队队员存入 redis中--创建车队、加入车队
     *
     * @param gameCode 游戏 code
     * @param teamSequence 车队序列号
     * @param rpgRedisTeamMemberVO 车队队员信息
     */
    public void saveRPGTeamMember(Integer gameCode, String teamSequence,
            RPGRedisTeamMemberVO rpgRedisTeamMemberVO) {
        String teamMemberKey = generateTeamMemberKey(teamSequence);
        String field = rpgRedisTeamMemberVO.getUid();
        String uidShardingKey = generateTeamMemberShardingKey(field, gameCode);
        String teamMemberStr = JsonsUtils.toJson(rpgRedisTeamMemberVO);

        Pipeline pipelined = cacheManager.pipelined();
        // 将车队队员信息存入 redis
        pipelined.hset(teamMemberKey, field, teamMemberStr);
        // 将车队队员 uid 存入分片 key
        pipelined.hset(uidShardingKey, field, teamSequence);
        pipelined.sync();
        logger.info(">> 用户[{}]加入车队[{}], 保存用户信息到 redis", field, teamSequence);
    }

    /**
     * 修改 redis中的 RPG车队队员
     * @param rpgRedisTeamMemberVO
     */
    public void updateRPGTeamMember(RPGRedisTeamMemberVO rpgRedisTeamMemberVO) {
        String teamMemberKey = generateTeamMemberKey(rpgRedisTeamMemberVO.getTeamSequence());
        String field = rpgRedisTeamMemberVO.getUid();
        String teamMemberStr = JsonsUtils.toJson(rpgRedisTeamMemberVO);
        cacheManager.hset(teamMemberKey, field, teamMemberStr);
    }

    /**
     * 获取指定的 RPG车队队员, 不存在抛出异常
     *
     * @param teamSequence 车队序列号
     * @param uid 车队队员 uid
     * @return
     */
    public RPGRedisTeamMemberVO getSpecifiedTeamMember(String teamSequence, String uid) {
        RPGRedisTeamMemberVO rpgRedisTeamMemberVO = getRedisTeamMember(teamSequence, uid);
        if (ObjectTools.isEmpty(rpgRedisTeamMemberVO)) {
            logger.error(">> 未从redis中查询到指定用户[{}]在车队[{}]中的数据!", uid, teamSequence);
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_NOT_EXIST);
        }
        return rpgRedisTeamMemberVO;
    }


    /**
     * 获取当前的 RPG车队队员, 不存在抛出异常
     *
     * @param teamSequence 车队序列号
     * @param uid 车队队员 uid
     * @return
     */
    public RPGRedisTeamMemberVO getCurrentTeamMember(String teamSequence, String uid) {
        RPGRedisTeamMemberVO rpgRedisTeamMemberVO = getRedisTeamMember(teamSequence, uid);
        if (ObjectTools.isEmpty(rpgRedisTeamMemberVO)) {
            logger.error(">> 未从redis中查询到当前用户[{}]在车队[{}]中的数据!", uid, teamSequence);
            throw new BusinessException(BizExceptionEnum.TEAM_CURRENT_MEMBER_HAS_LEAVED);
        }
        return rpgRedisTeamMemberVO;
    }

    /**
     * 获取 RPG车队队员, 不存在不抛出异常
     *
     * @param teamSequence 车队序列号
     * @param uid 车队队员 uid
     * @return
     */
    public RPGRedisTeamMemberVO getRedisTeamMember(String teamSequence, String uid) {
        String teamMemberKey = generateTeamMemberKey(teamSequence);
        RPGRedisTeamMemberVO rpgRedisTeamMemberVO = cacheManager
                .hget(teamMemberKey, uid, RPGRedisTeamMemberVO.class);
        return rpgRedisTeamMemberVO;
    }

    /**
     * 获取 redis中的所有 RPG车队队员
     * @param teamSequence
     * @return
     */
    public List<RPGRedisTeamMemberVO> getAllRPGRedisTeamMemberVO(String teamSequence) {
        String teamMemberKey = generateTeamMemberKey(teamSequence);
        Map<String, RPGRedisTeamMemberVO> memberVOMap = cacheManager
                .hgetAll(teamMemberKey, RPGRedisTeamMemberVO.class);
        if (ObjectTools.isEmpty(memberVOMap) || ObjectTools.isEmpty(memberVOMap.values())) {
            logger.error(">> 车队[{}]无任何队员!", teamSequence);
            throw new BusinessException(BizExceptionEnum.TEAM_HAS_NO_MEMBER);
        }
        return new ArrayList<>(memberVOMap.values());
    }

}
