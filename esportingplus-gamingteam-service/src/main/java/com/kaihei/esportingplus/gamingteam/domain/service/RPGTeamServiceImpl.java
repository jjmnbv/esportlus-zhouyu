package com.kaihei.esportingplus.gamingteam.domain.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.api.feign.ResourceServiceClient;
import com.kaihei.esportingplus.api.vo.RedisGame;
import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.api.vo.RedisSmallZoneRefAcrossZone;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.HttpConstant;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.constant.UploadFolderConstants;
import com.kaihei.esportingplus.common.enums.BaojiLevelEnum;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.file.FileUploadService;
import com.kaihei.esportingplus.common.lock.PreventRepeatOperation;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.QrCodeUtils;
import com.kaihei.esportingplus.common.tools.RedisUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.annotation.RedisDistlock;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamMemberActionEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.WxPushReasonEnum;
import com.kaihei.esportingplus.gamingteam.api.mq.RPGUpdateOrderStatusMessage;
import com.kaihei.esportingplus.gamingteam.api.mq.TeamStartOrderMessage;
import com.kaihei.esportingplus.gamingteam.api.params.TeamDataBaseQueryParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamQueryParams;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.BossConfirmPaidSuccessParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamCreateParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamRolesParams;
import com.kaihei.esportingplus.gamingteam.api.vo.BossInfoForOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.GameTeamTotal;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGBaojiInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamListVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberCompaintAdminVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceUidVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamMemberBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.config.GamingTeamConfig;
import com.kaihei.esportingplus.gamingteam.data.manager.GameConfigService;
import com.kaihei.esportingplus.gamingteam.data.manager.TeamServiceSupport;
import com.kaihei.esportingplus.gamingteam.data.manager.rpg.RPGTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.rpg.RPGTeamMemberCacheManager;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamGameRPGRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamGameResultRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamMemberRPGRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameRPG;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGameResult;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberRPG;
import com.kaihei.esportingplus.gamingteam.event.BossConfirmPaidSuccessEvent;
import com.kaihei.esportingplus.gamingteam.event.UpdateTeamPositionEvent;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcEndTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcStartTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinOrderCancelEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinOrderEndEvent;
import com.kaihei.esportingplus.gamingteam.event.weixin.WeXinStartTeamEvent;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.EndTeamLocalTransaction;
import com.kaihei.esportingplus.gamingteam.rocketmq.localtransaction.StartTeamLocalTransaction;
import com.kaihei.esportingplus.gamingteam.rocketmq.producer.EndTeamTransactionProducer;
import com.kaihei.esportingplus.gamingteam.rocketmq.producer.GamingTeamCommonProducer;
import com.kaihei.esportingplus.gamingteam.rocketmq.producer.StartTeamTransactionProducer;
import com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum;
import com.kaihei.esportingplus.trade.api.feign.RPGOrdersServiceClient;
import com.kaihei.esportingplus.trade.api.params.CheckTeamMemberPayedParams;
import com.kaihei.esportingplus.trade.api.params.OrderTeamRPGMember;
import com.kaihei.esportingplus.trade.api.params.RPGInComeParams;
import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusRPGParams;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPTeamMembersIncome;
import com.kaihei.esportingplus.user.api.feign.UserGameRoleServiceClient;
import com.kaihei.esportingplus.user.api.params.UserGameRoleAcrossQueryParams;
import com.kaihei.esportingplus.user.api.vo.UserGameAboardVo;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * RPG 车队业务
 *
 * @author liangyi
 */
@Service("teamService")
public class RPGTeamServiceImpl extends AbstractTeamService implements RPGTeamService {

    /**
     * 从开车到解散/结束的时间间隔(秒)
     */
    private static final int START_END_TIME = 30;

    /**
     * 暴鸡预期收益计算失败的收益默认值
     */
    private static final int BAOJI_PRE_PROFIT_FAIL = -1;

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamGameRPGRepository teamGameRPGRepository;
    @Autowired
    TeamGameResultRepository teamGameResultRepository;
    @Autowired
    TeamMemberRPGRepository teamMemberRPGRepository;
    @Autowired
    RPGTeamCacheManager rpgTeamCacheManager;
    @Autowired
    RPGTeamMemberCacheManager rpgTeamMemberCacheManager;
    @Autowired
    TeamServiceSupport teamServiceSupport;
    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Autowired
    UserGameRoleServiceClient userGameRoleServiceClient;
    @Autowired
    ResourceServiceClient resourceServiceClient;
    @Autowired
    RPGOrdersServiceClient rpgOrdersServiceClient;
    @Autowired
    GamingTeamConfig gamingTeamConfig;

    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    GameConfigService gameConfigService;
    @Autowired
    private Environment env;
    @Autowired
    TeamTransactionService teamTransactionService;
    @Autowired
    StartTeamTransactionProducer startTeamTransactionProducer;
    @Autowired
    EndTeamTransactionProducer endTeamTransactionProducer;
    @Autowired
    GamingTeamCommonProducer gamingTeamCommonProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @PreventRepeatOperation
    public String createTeam(RPGTeamCreateParams rpgTeamCreateParams) {

        UserSessionContext userSessionContext = UserSessionContext.getUser();
        String uid = userSessionContext.getUid();

        // 校验用户身份、状态
        Integer gameCode = rpgTeamCreateParams.getGameCode();
        rpgTeamMemberCacheManager.checkUserInOtherTeam(uid, gameCode);

        Integer userIdentity = rpgTeamCreateParams.getUserIdentity();
        if (UserIdentityEnum.BAOJI.getCode() != userIdentity) {
            // 不是暴鸡无权创建车队
            logger.error(">> 创建车队,当前用户不是暴鸡身份! uid: {},角色 id: {}",
                    uid, rpgTeamCreateParams.getGameRoleId());
            throw new BusinessException(BizExceptionEnum.TEAM_CREATE_NOT_BAOJI);
        }

        // 构建车队信息
        RedisGameRaid gameRaid = getRedisSingleGameRaid(rpgTeamCreateParams.getGameCode(),
                rpgTeamCreateParams.getRaidCode());
        Team team = buildTeamRPG(rpgTeamCreateParams, gameRaid);

        // 构建车队队员信息
        RPGRedisTeamMemberVO rpgRedisTeamMemberVO = rpgTeamMemberCacheManager
                .buildRPGRedisTeamMember(userSessionContext, gameCode,
                        rpgTeamCreateParams.getRaidCode(),
                        rpgTeamCreateParams.getGameRoleId(), userIdentity);

        // 构建车队游戏信息
        TeamGameRPG teamGameRPG = buildTeamGameRPG(rpgTeamCreateParams,
                rpgRedisTeamMemberVO.getZoneSmallCode(), gameRaid);

        // 车队及车队游戏数据入库
        teamRepository.insertSelective(team);
        teamGameRPG.setTeamId(team.getId());
        teamGameRPGRepository.insertSelective(teamGameRPG);

        // 车队缓存到 redis 中
        String teamSequence = team.getSequence();
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.buildRPGRedisTeam(team, teamGameRPG);
        rpgTeamCacheManager.saveRPGRedisTeam(rpgRedisTeamVO);

        // 设置车队队员身份: 队长, 状态: 已入团
        rpgRedisTeamMemberVO.setTeamId(team.getId());
        rpgRedisTeamMemberVO.setTeamSequence(teamSequence);
        rpgRedisTeamMemberVO.setUserIdentity(UserIdentityEnum.LEADER.getCode());
        rpgRedisTeamMemberVO.setStatus(TeamMemberStatusEnum.JOINED_TEAM.getCode());
        rpgRedisTeamMemberVO.setGmtModified(new Date());
        BeanMapper.map(userSessionContext, rpgRedisTeamMemberVO);

        // 车队队长存入 redis
        rpgTeamMemberCacheManager.saveRPGTeamMember(gameCode, teamSequence,rpgRedisTeamMemberVO);
        // 发布创建车队事件
        teamServiceSupport.postJoinTeamEvent(rpgRedisTeamVO.getSequence(),
                rpgRedisTeamVO.getTitle(), uid,
                userSessionContext.getUsername(), null, true);
        logger.info(">> 用户[{}]创建车队[{}]成功", uid, teamSequence);
        return teamSequence;
    }

    private TeamGameRPG buildTeamGameRPG(RPGTeamCreateParams rpgTeamCreateParams,
            Integer zoneSmallCode, RedisGameRaid gameRaid) {
        Integer gameCode = rpgTeamCreateParams.getGameCode();
        TeamGameRPG teamGame = BeanMapper.map(rpgTeamCreateParams, TeamGameRPG.class);
        RedisGame redisGame = cacheManager.hget(RedisKey.TEAM_GAME_LIST_KEY,
                ObjectTools.covertToString(gameCode), RedisGame.class);
        // 调用资源服务获取跨区信息
        logger.info(">> 创建车队, 开始调用资源服务查询跨区信息, 游戏 code: {}, 小区 code: {}",
                gameCode, zoneSmallCode);
        long startTime = System.currentTimeMillis();
        ResponsePacket<RedisSmallZoneRefAcrossZone> acrossZoneResponse = resourceServiceClient
                .getAcrossZoneFromSmallZoneCode(gameCode, zoneSmallCode);
        logger.info(">> 创建车队, 调用资源服务查询跨区耗时: {} ms",
                (System.currentTimeMillis()) - startTime);
        if (acrossZoneResponse.responseSuccess()) {
            RedisSmallZoneRefAcrossZone acrossZoneData = acrossZoneResponse.getData();
            ValidateAssert.allNotNull(BizExceptionEnum.ZONE_SERVER_NOT_MATCH, acrossZoneData);
            teamGame.setZoneAcrossCode(acrossZoneData.getZoneAcrossCode());
            teamGame.setZoneAcrossName(acrossZoneData.getZoneAcrossName());
        } else {
            logger.error(">> 创建车队, 调用资源服务查询跨区信息错误: {}", acrossZoneResponse);
            throw new BusinessException(BizExceptionEnum.TEAM_CREATE_FAILED);
        }
        teamGame.setGameName(redisGame.getName());
        teamGame.setOriginalFee(gameRaid.getFee());
        teamGame.setDiscountFee(gameRaid.getDiscountFee());
        teamGame.setRaidName(gameRaid.getRaidName());
        return teamGame;
    }


    private Team buildTeamRPG(RPGTeamCreateParams rpgTeamCreateParams, RedisGameRaid gameRaid) {
        Team team = BeanMapper.map(rpgTeamCreateParams, Team.class);
        // 生成 room no 和 sequence
        team.setRoomNum(generateRoomNum());
        team.setSequence(generateTeamSequence());
        team.setStatus((byte)TeamStatusEnum.PREPARING.getCode());
        team.setOriginalPositionCount(gameRaid.getMaxPositionCount());
        team.setActuallyPositionCount(gameRaid.getMaxPositionCount());
        return team;
    }

    @Override
    public List<UserGameAboardVo> getTeamRoles(RPGTeamRolesParams rpgTeamRolesParams) {

        String uid = UserSessionContext.getUser().getUid();

        // 校验车队是否能加入(车队是否为准备中、是否满员)
        RPGRedisTeamVO rpgRedisTeamVO = checkTeamCanJoin(rpgTeamRolesParams.getSequence());

        // 校验用户是否已在其他车队
        rpgTeamMemberCacheManager.checkUserInOtherTeam(uid, rpgRedisTeamVO.getGameCode());

        // 调用用户服务获取角色列表
        List<UserGameAboardVo> roleList = getUserGameRoleList(rpgTeamRolesParams,
                uid, rpgRedisTeamVO);

        return roleList;
    }

    private List<UserGameAboardVo> getUserGameRoleList(RPGTeamRolesParams rpgTeamRolesParams,
            String uid, RPGRedisTeamVO rpgRedisTeamVO) {
        UserGameRoleAcrossQueryParams queryParams = BeanMapper
                .map(rpgRedisTeamVO, UserGameRoleAcrossQueryParams.class);
        queryParams.setUid(uid);
        queryParams.setUserIdentity(rpgTeamRolesParams.getUserIdentity());
        queryParams.setRaidCode(rpgRedisTeamVO.getRaidCode());
        queryParams.setZoneAcrossCode(rpgRedisTeamVO.getZoneAcrossCode());

        logger.info(">> (上车)准备加入车队, 开始调用用户服务查询角色列表, 参数: {}", queryParams);
        long start = System.currentTimeMillis();
        ResponsePacket<List<UserGameAboardVo>> userGameResponse = userGameRoleServiceClient
                .getAboardGameRole(queryParams);
        logger.info(">> (上车)准备加入车队, 调用用户服务查询角色列表耗时: {} ms",
                (System.currentTimeMillis() - start));
        if (!userGameResponse.responseSuccess()) {
            logger.error(">> (上车)准备加入车队, 调用用户服务查询角色列表错误: {}", userGameResponse);
            throw new BusinessException(BizExceptionEnum.USER_GAME_ROLE_NOT_EXIST);
        }
        List<UserGameAboardVo> roleList = userGameResponse.getData();
        if (ObjectTools.isEmpty(roleList)) {
            logger.warn(">> (上车)准备加入车队, 用户[{}]未查询到符合车队[{}]的角色",
                    uid, rpgRedisTeamVO.getSequence());
        }
        return roleList;
    }

    @Override
    @RedisDistlock(expireTime = 2000)
    public void joinTeam(RPGTeamJoinParams teamJoinParams) {
        UserSessionContext userSessionContext = UserSessionContext.getUser();
        String uid = userSessionContext.getUid();
        String teamSequence = teamJoinParams.getSequence();
        RPGRedisTeamVO rpgRedisTeamVO = checkTeamCanJoin(teamJoinParams.getSequence());
        // 校验是否已加入了该车队, 防止重复提交
        RPGRedisTeamMemberVO existMember = rpgTeamMemberCacheManager.
                getRedisTeamMember(teamSequence, uid);
        if (existMember == null) {
            // 未获取到, 说明是第一次加入该车队, 此时需要校验用户是否已在其他车队
            rpgTeamMemberCacheManager.checkUserInOtherTeam(uid, rpgRedisTeamVO.getGameCode());
        } else {
            // 获取到了车队队员
            logger.warn(">> 用户[{}]加入车队[{}]发送了多次请求!", uid, teamSequence);
            // 判断角色 id 是否相同
            if (existMember.getGameRoleId().equals(teamJoinParams.getGameRoleId())) {
                // 角色相同, 直接返回, 以第一次请求的数据为准
                return;
            }
            // 角色不相同, 需要覆盖掉之前加入的角色
            // 继续正常的加入车队流程
            if (UserIdentityEnum.BOSS.getCode() == existMember.getUserIdentity()) {
                // 如果是老板需要删除之前的定时任务
                teamServiceSupport.removeBossPaymentTimeoutJob(teamSequence, uid);
            }
        }

        // 创建车队队员
        Integer userIdentity = teamJoinParams.getUserIdentity();
        RPGRedisTeamMemberVO rpgRedisTeamMemberVO = rpgTeamMemberCacheManager
                .buildRPGRedisTeamMember(userSessionContext, rpgRedisTeamVO.getGameCode(),
                        rpgRedisTeamVO.getRaidCode(), teamJoinParams.getGameRoleId(), userIdentity);
        rpgRedisTeamMemberVO.setTeamId(rpgRedisTeamVO.getId());

        // 暴鸡和老板加入车队身份和状态不一样
        if (UserIdentityEnum.BAOJI.getCode() == userIdentity) {
            rpgRedisTeamMemberVO.setUserIdentity(UserIdentityEnum.BAOJI.getCode());
            rpgRedisTeamMemberVO.setStatus(TeamMemberStatusEnum.PREPARE_JOIN_TEAM.getCode());
        } else if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
            rpgRedisTeamMemberVO.setUserIdentity(UserIdentityEnum.BOSS.getCode());
            rpgRedisTeamMemberVO.setStatus(TeamMemberStatusEnum.WAIT_FOR_PAY.getCode());
            // 老板加入车队添加定时任务--到期未支付直接踢出车队 //todo 异步处理
            teamServiceSupport.createBossPaymentTimeoutJob(teamSequence, uid,
                    rpgRedisTeamMemberVO.getJoinTime(), rpgRedisTeamVO.getPaymentTimeout());
        }

        // 获取车队中的其他队员, 推送加入车队消息
        List<String> otherUidList = new ArrayList<>(20);
        List<RPGRedisTeamMemberVO> allMember =
                rpgTeamMemberCacheManager.getAllRPGRedisTeamMemberVO(teamSequence);
        for (RPGRedisTeamMemberVO teamMemberVO : allMember) {
            otherUidList.add(teamMemberVO.getUid());
        }
        // 如果有重复提交的情况, 这里的 uid 肯定存在, 所以需要删除掉
        otherUidList.remove(uid);
        // 存入 redis
        rpgTeamMemberCacheManager.saveRPGTeamMember(rpgRedisTeamVO.getGameCode(),
                teamSequence, rpgRedisTeamMemberVO);
        // 发布加入车队事件
        teamServiceSupport.postJoinTeamEvent(teamSequence, rpgRedisTeamVO.getTitle(),
                uid, rpgRedisTeamMemberVO.getUsername(), otherUidList, false);
    }


    @Override
    @RedisDistlock
    public void quitTeam(String sequence) {
        String uid = UserSessionContext.getUser().getUid();
        // 校验车队状态
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        rpgTeamMemberCacheManager.checkUserInCurrentTeam(sequence, uid, rpgRedisTeamVO.getGameCode());
        // 要退出的车队队员
        RPGRedisTeamMemberVO quitMember = rpgTeamMemberCacheManager
                .getCurrentTeamMember(sequence, uid);
        Integer userIdentity = quitMember.getUserIdentity();
        Integer memberStatus = quitMember.getStatus();
        Integer teamStatus = rpgRedisTeamVO.getStatus();
        // 订单行为状态
        TeamOrderRPGActionEnum leaveTeamAction = acquireLeaveTeamOrderAction(userIdentity, memberStatus,
                teamStatus, false);
        // 获取车队中的其他队员, 准备推送退出车队消息
        List<String> otherUidList = new ArrayList<>();
        List<RPGRedisTeamMemberVO> allRPGRedisTeamMemberVO =
                rpgTeamMemberCacheManager.getAllRPGRedisTeamMemberVO(sequence);
        for (RPGRedisTeamMemberVO rpgRedisTeamMemberVO : allRPGRedisTeamMemberVO) {
            otherUidList.add(rpgRedisTeamMemberVO.getUid());
        }
        otherUidList.remove(uid);
        // 主动退出车队, 判断车队状态和用户身份
        boolean shouldPushWeXinMsg = false;
        if (TeamStatusEnum.PREPARING.getCode() == teamStatus) {
            // 车队准备中, 只向已支付的老板推送微信通知
            if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                if (TeamMemberStatusEnum.WAIT_FOR_PAY.getCode() != memberStatus) {
                    shouldPushWeXinMsg = true;
                } else {
                    // 老板未支付主动退出, 删除定时任务
                    teamServiceSupport.removeBossPaymentTimeoutJob(sequence, uid);
                }
            }
        } else if (TeamStatusEnum.RUNNING.getCode() == teamStatus) {
            // 车队进行中退出, 不论暴鸡还是老板都推送订单取消微信通知
            shouldPushWeXinMsg = true;
        } else {
            logger.error(">> 用户[{}]退出车队[{}], 车队状态[{}]错误!", uid, sequence, teamStatus);
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_QUIT_STATUS_ERROR);
        }

        WeXinOrderCancelEvent weXinOrderCancelEvent = null;
        if (shouldPushWeXinMsg) {
            List<String> memberUidList = new ArrayList<>();
            memberUidList.add(uid);
            weXinOrderCancelEvent = buildLeaveTeamOrderCancel(memberUidList,
                    rpgRedisTeamVO, WxPushReasonEnum.BOSS_QUIT);
        }
        // 处理车队队员离开车队
        handleMemberLeaveTeam(rpgRedisTeamVO, quitMember, leaveTeamAction,
                otherUidList, weXinOrderCancelEvent, false);
        logger.info(">> 用户[{}]主动退出了车队[{}]", uid, sequence);

    }


    /**
     * 处理队员离开车队 场景: 退出车队、踢出队员
     *
     * @param redisTeam 车队信息
     * @param redisTeamMember 离开车队的队员信息
     * @param leaveTeamAction 离开的行为
     * @param otherUidList 车队中其他队员 uid
     * @param weXinOrderCancelEvent 微信订单取消服务通知
     * @param kickOut 是否被踢出
     */
    private void handleMemberLeaveTeam(RPGRedisTeamVO redisTeam, RPGRedisTeamMemberVO redisTeamMember,
            TeamOrderRPGActionEnum leaveTeamAction, List<String> otherUidList,
            WeXinOrderCancelEvent weXinOrderCancelEvent, boolean kickOut) {
        String sequence = redisTeam.getSequence();
        String uid = redisTeamMember.getUid();
        String username = redisTeamMember.getUsername();

        if (ObjectTools.isNotNull(leaveTeamAction)) {
            // -- 发送离开消息到 mq, 修改订单状态
            OrderTeamRPGMember orderTeamRPGMember = buildOrderTeamRPGMembers(uid, username,
                    redisTeamMember.getUserIdentity(),
                    leaveTeamAction, redisTeamMember.getBaojiLevel(),
                    redisTeamMember.getRaidLocationCode());
            pushLeaveTeamUpdateOrderMQ(sequence, redisTeam.getStatus(), orderTeamRPGMember);
        }

        // 移除 redis 中的车队队员
        rpgTeamMemberCacheManager.removeRedisTeamMember(redisTeam.getGameCode(), sequence, uid);

        // 发布离开车队融云事件
        teamServiceSupport.postLeaveTeamEvent(redisTeam.getSequence(),
                redisTeam.getTitle(), uid, username, otherUidList, kickOut);
        // 如果需要推送微信订单取消通知, 则发布该事件
        if (ObjectTools.isNotNull(weXinOrderCancelEvent)) {
            EventBus.post(weXinOrderCancelEvent);
        }
    }

    /**
     * 离开车队时 创建微信订单取消服务通知 场景: 解散车队、结束车队、退出车队、被踢出车队
     *
     * @param memberUidList 用户 uid
     * @param wxPushReasonEnum 订单取消原因
     */
    private WeXinOrderCancelEvent buildLeaveTeamOrderCancel(List<String> memberUidList,
            RPGRedisTeamVO rpgRedisTeamVO, WxPushReasonEnum wxPushReasonEnum) {
        WeXinOrderCancelEvent weXinOrderCancelEvent = new WeXinOrderCancelEvent();
        weXinOrderCancelEvent.setGameName(rpgRedisTeamVO.getGameName());
        weXinOrderCancelEvent.setRaidName(rpgRedisTeamVO.getRaidName());
        weXinOrderCancelEvent.setTeamSequence(rpgRedisTeamVO.getSequence());
        weXinOrderCancelEvent.setMemberUidList(memberUidList);
        weXinOrderCancelEvent.setReasonType(wxPushReasonEnum.getCode());
        return weXinOrderCancelEvent;
    }

    @Override
    @RedisDistlock(expireTime = 300)
    public void kickOutTeamMember(String sequence, String kickOutMemberUid) {
        String leaderUid = UserSessionContext.getUser().getUid();
        // 获取当前的车队信息
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        // 校验队长是否在当前车队
        rpgTeamMemberCacheManager.checkUserInCurrentTeam(sequence,
                leaderUid, rpgRedisTeamVO.getGameCode());
        // 校验被踢队员是否在当前车队
        rpgTeamMemberCacheManager.checkUserInCurrentTeam(sequence,
                kickOutMemberUid, rpgRedisTeamVO.getGameCode());
        Integer teamStatus = rpgRedisTeamVO.getStatus();
        // 车队准备中才能踢出队员
        if (TeamStatusEnum.PREPARING.getCode() != teamStatus) {
            logger.error(">> 踢出队员[{}]时, 车队[{}]的状态[{}]不是准备中!",
                    kickOutMemberUid, sequence, teamStatus);
            throw new BusinessException(BizExceptionEnum.TEAM_KICKOUT_MEMBER_STATUS_ERROR);
        }
        // 校验用户是不是队长
        RPGRedisTeamMemberVO leaderMember = rpgTeamMemberCacheManager
                .getCurrentTeamMember(sequence, leaderUid);
        if (UserIdentityEnum.LEADER.getCode() != leaderMember.getUserIdentity()) {
            logger.error(">> 踢出队员[{}]时, 车队[{}]的当前操作人[{}]不是队长身份!",
                    kickOutMemberUid, sequence, leaderUid);
            throw new BusinessException(BizExceptionEnum.TEAM_NOT_LEADER);
        }
        // 4. 获取要被踢的队员信息
        RPGRedisTeamMemberVO kickOutMember = rpgTeamMemberCacheManager
                .getSpecifiedTeamMember(sequence, kickOutMemberUid);
        Integer userIdentity = kickOutMember.getUserIdentity();
        Integer memberStatus = kickOutMember.getStatus();
        TeamOrderRPGActionEnum leaveTeamAction = acquireLeaveTeamOrderAction(userIdentity, memberStatus,
                teamStatus, true);
        // 获取车队中的其他队员, 准备推送退出车队消息
        List<RPGRedisTeamMemberVO> allRPGRedisTeamMemberVO =
                rpgTeamMemberCacheManager.getAllRPGRedisTeamMemberVO(sequence);
        List<String> otherUidList = allRPGRedisTeamMemberVO.stream()
                .filter(rm -> !rm.getUid().equals(kickOutMemberUid))
                .map(RPGRedisTeamMemberVO::getUid).collect(Collectors.toList());
        // 如果是老板被踢出车队, 判断是否需要推送微信订单取消通知
        WeXinOrderCancelEvent weXinOrderCancelEvent = null;
        if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
            if (TeamMemberStatusEnum.WAIT_FOR_PAY.getCode() == memberStatus) {
                // 老板处于未支付状态被踢出, 移除定时任务
                teamServiceSupport.removeBossPaymentTimeoutJob(sequence, kickOutMemberUid);
            } else {
                // 只向已支付的老板推送微信取消通知
                List<String> memberUidList = Arrays.asList(kickOutMemberUid);
                weXinOrderCancelEvent = buildLeaveTeamOrderCancel(
                        memberUidList, rpgRedisTeamVO, WxPushReasonEnum.BOSS_KICKOUT);
            }
        }
        // 处理车队队员离开车队
        handleMemberLeaveTeam(rpgRedisTeamVO, kickOutMember, leaveTeamAction,
                otherUidList, weXinOrderCancelEvent, true);
        logger.info(">> 用户[{}]被[{}]踢出了车队[{}]", kickOutMemberUid, leaderUid, sequence);
    }

    /**
     * 离开车队时, 发送修改订单状态消息到 mq 场景: 主动退出车队、被踢出车队
     *
     * @param teamSequence 车队序列号
     * @param teamStatus 车队状态
     * @param orderTeamRPGMember 修改订单基础参数
     */
    private void pushLeaveTeamUpdateOrderMQ(String teamSequence, Integer teamStatus,
            OrderTeamRPGMember orderTeamRPGMember) {
        // 构建 mq 消息
        UpdateOrderStatusRPGParams updateOrderStatusRPGParams = new UpdateOrderStatusRPGParams();
        updateOrderStatusRPGParams.setTeamSequence(teamSequence);
        updateOrderStatusRPGParams.setTeamStatus(teamStatus);
        updateOrderStatusRPGParams.setGameResult(GameResultEnum.UNKNOWN.getCode());
        updateOrderStatusRPGParams.setTeamMembers(Arrays.asList(orderTeamRPGMember));
        // 离开车队消息
        RPGUpdateOrderStatusMessage updateOrderStatusRPGMessage = new RPGUpdateOrderStatusMessage();
        updateOrderStatusRPGMessage.setUpdateOrderStatusRPGParams(updateOrderStatusRPGParams);
        // 发送非事务消息
        gamingTeamCommonProducer.sendUpdateOrderStatusMessage4LeaveTeam(
                orderTeamRPGMember.getTeamMemberUID(), updateOrderStatusRPGMessage);
    }

    @Override
    @RedisDistlock
    public void dismissTeam(RPGTeamEndParams rpgTeamEndParams) {
        String leaderUid = UserSessionContext.getUser().getUid();
        String teamSequence = rpgTeamEndParams.getSequence();
        // 获取车队信息
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(teamSequence);
        // 校验车队状态
        Integer teamStatus = rpgRedisTeamVO.getStatus();
        checkTeamStatus(teamSequence, TeamMemberActionEnum.DISMISS, teamStatus);
        // 获取所有队员, 准备解散车队
        List<RPGRedisTeamMemberVO> allRPGRedisTeamMemberVO =
                rpgTeamMemberCacheManager.getAllRPGRedisTeamMemberVO(teamSequence);
        // 校验当前队员, 只有队长才可以解散车队
        rpgTeamMemberCacheManager.checkUserInCurrentTeam(teamSequence, leaderUid,
                rpgRedisTeamVO.getGameCode());
        RPGRedisTeamMemberVO leaderMember = allRPGRedisTeamMemberVO.stream()
                .filter(r -> r.getUid().equals(leaderUid))
                .filter(r -> UserIdentityEnum.LEADER.getCode() == r.getUserIdentity())
                .findAny()
                .orElseThrow(() -> new BusinessException(BizExceptionEnum.TEAM_NOT_LEADER));
        Integer gameResult = rpgTeamEndParams.getGameResult();
        // 待持久化的车队队员
        List<RPGRedisTeamMemberVO> persistenceMemberList =
                assembleEndTeamPersistTeamMember(allRPGRedisTeamMemberVO, teamStatus);
        // 车队队员 uid 集合, 融云推送使用
        List<String> rcMemberUidList = allRPGRedisTeamMemberVO.stream()
                .map(RPGRedisTeamMemberVO::getUid)
                .collect(Collectors.toList());
        // 待推送微信服务通知的 uid 集合
        List<String> wxMemberUidList =
                assembleEndTeamWXPushTeamMember(allRPGRedisTeamMemberVO, teamStatus);
        // 需要发送 MQ的数据
        List<OrderTeamRPGMember> otmList =
                assembleEndTeamOrderActionParams(allRPGRedisTeamMemberVO, teamStatus, true);
        // 微信订单取消服务通知
        WeXinOrderCancelEvent weXinOrderCancelEvent = null;
        // 微信订单完成服务通知
        WeXinOrderEndEvent weXinOrderEndEvent = null;
        // 判断车队状态--组装解散车队时的其他业务逻辑参数
        if (TeamStatusEnum.PREPARING.getCode() == teamStatus) {
            // 车队准备中解散
            if (GameResultEnum.UNKNOWN.getCode() != gameResult) {
                // 车队准备中的游戏结果一定为默认值, 否则抛出业务异常
                logger.error(">> 车队[{}]准备中解散, 比赛结果[{}]错误!", teamSequence, gameResult);
                throw new BusinessException(BizExceptionEnum.TEAM_PREPARING_GAME_RESULT_ERROR);
            }
            // 车队准备中解散向已支付的老板推送订单取消通知
            weXinOrderCancelEvent = buildLeaveTeamOrderCancel(
                    wxMemberUidList, rpgRedisTeamVO, WxPushReasonEnum.LEADER_DISMISS);
        } else if (TeamStatusEnum.RUNNING.getCode() == teamStatus) {
            // 车队进行中解散
            checkTeamEndToFast(teamSequence, rpgRedisTeamVO.getGmtModified());
            if (GameResultEnum.UNKNOWN.getCode() == gameResult) {
                // 车队进行中的游戏结果不能为默认值, 否则抛出业务异常
                logger.error(">> 车队[{}]进行中解散, 比赛结果[{}]错误!", teamSequence, gameResult);
                throw new BusinessException(BizExceptionEnum.TEAM_RUNNING_GAME_RESULT_ERROR);
            }
            // 车队进行中解散向所有的车队队员推送订单完成通知
            weXinOrderEndEvent = buildEndTeamOrderEnd(rpgRedisTeamVO,
                    leaderMember.getUsername(), wxMemberUidList);
        }
        // 处理结束车队
        handleEndTeam(gameResult, rpgRedisTeamVO, leaderUid,
                otmList, persistenceMemberList, rcMemberUidList,
                weXinOrderCancelEvent, weXinOrderEndEvent, true);

    }


    /**
     * 解散或结束车队时, 根据车队当前状态组装需要入库的车队队员
     *
     * @param allRPGRedisTeamMemberVO 车队队员列表
     * @param teamStatus 车队状态
     * @return
     */
    private List<RPGRedisTeamMemberVO> assembleEndTeamPersistTeamMember(
            List<RPGRedisTeamMemberVO> allRPGRedisTeamMemberVO, Integer teamStatus) {

        List<RPGRedisTeamMemberVO> persistTeamMemberList = new ArrayList<>();
        if (TeamStatusEnum.RUNNING.getCode() == teamStatus) {
            // 车队此时是进行中--开车后解散或正常结束
            // 所有队员入库
            persistTeamMemberList = allRPGRedisTeamMemberVO;
        } else if (TeamStatusEnum.PREPARING.getCode() == teamStatus) {
            // 车队此时是准备中--开车前解散
            // todo 是否需要保存队长 -- 目前是支付成功的老板入库
            persistTeamMemberList = allRPGRedisTeamMemberVO.stream()
                    .filter(m -> UserIdentityEnum.BOSS.getCode() == m.getUserIdentity())
                    .filter(m -> TeamMemberStatusEnum.WAIT_FOR_PAY.getCode() != m.getStatus())
                    .collect(Collectors.toList());
        }

        return persistTeamMemberList;
    }

    /**
     * 解散或结束车队时, 根据车队当前状态组装需要推送微信通知的车队队员 uid
     *
     * @param allRPGRedisTeamMemberVO 车队队员列表
     * @param teamStatus 车队状态
     * @return
     */
    private List<String> assembleEndTeamWXPushTeamMember(
            List<RPGRedisTeamMemberVO> allRPGRedisTeamMemberVO, Integer teamStatus) {

        List<String> pushTeamMemberUidList = new ArrayList<>();

        if (TeamStatusEnum.RUNNING.getCode() == teamStatus) {
            // 车队此时是进行中--开车后解散或正常结束
            // 所有队员都需要推送微信通知
            pushTeamMemberUidList = allRPGRedisTeamMemberVO.stream()
                    .map(RPGRedisTeamMemberVO::getUid)
                    .collect(Collectors.toList());
        } else if (TeamStatusEnum.PREPARING.getCode() == teamStatus) {
            // 车队此时是准备中--开车前解散
            // 只向已支付的老板推送通知
            pushTeamMemberUidList = allRPGRedisTeamMemberVO.stream()
                    .filter(m-> UserIdentityEnum.BOSS.getCode() == m.getUserIdentity())
                    .filter(m-> TeamMemberStatusEnum.WAIT_FOR_PAY.getCode() != m.getStatus())
                    .map(RPGRedisTeamMemberVO::getUid)
                    .collect(Collectors.toList());
        }
        return pushTeamMemberUidList;
    }


    /**
     * 立即开车后, 不能在指定时间内马上结束车队(结束车队、解散车队)
     *
     * @param teamSequence 车队序列号
     * @param teamStartDate 车队开车时间
     */
    private void checkTeamEndToFast(String teamSequence, Date teamStartDate) {
        long seconds = 0;
        try {
            LocalDateTime startDateTime = LocalDateTime
                    .ofInstant(teamStartDate.toInstant(), ZoneId.systemDefault());
            Duration duration = Duration.between(startDateTime, LocalDateTime.now());
            seconds = duration.getSeconds();
        } catch (Exception e) {
            // 如果抛出异常不能阻碍正常的解散车队或结束车队的流程
            logger.error(">> 解散或结束车队[{}]时, 时间间隔计算错误!", teamSequence, e);
        }
        if (seconds < START_END_TIME) {
            // 从开车到解散小于30s, 提示操作太过频繁
            logger.error(">> 解散或结束车队[{}]时, 距离开车时间为[{}]s", teamSequence, seconds);
            throw new BusinessException(BizExceptionEnum.TEAM_OPERATE_TOO_FAST);
        }
    }

    /**
     * 组装车队准备中解散的业务逻辑参数
     *
     * @param teamSequence 车队序列号
     * @param rpgRedisTeamMemberVOList 所有车队队员
     * @param memberOrderParamList 修改订单状态参数
     * @param persistenceMemberList 待持久化的车队队员
     * @param allMemberUidList 所有车队队员 uid
     * @param memberWXUidList 待微信推送的车队队员 uid
     */
    /*private void assemblePreparingDismissTeam(String teamSequence,
            List<RPGRedisTeamMemberVO> rpgRedisTeamMemberVOList,
            List<MemberOrderParam> memberOrderParamList,
            List<RPGRedisTeamMemberVO> persistenceMemberList, List<String> allMemberUidList,
            List<String> memberWXUidList) {
        for (RPGRedisTeamMemberVO rpgRedisTeamMemberVO : rpgRedisTeamMemberVOList) {
            Integer userIdentity = rpgRedisTeamMemberVO.getUserIdentity();
            Integer memberStatus = rpgRedisTeamMemberVO.getStatus();
            String uid = rpgRedisTeamMemberVO.getUid();
            allMemberUidList.add(uid);
            if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                // 车队准备中解散暴鸡不用管, 老板需判断他是否已支付
                TeamOrderRPGActionEnum orderActionEnum;
                if (TeamMemberStatusEnum.PREPARE_JOIN_TEAM.getCode() == memberStatus) {
                    // 老板已支付, 待入团
                    orderActionEnum = TeamOrderRPGActionEnum.PREPARE_JOIN_TEAM_DISMISSD;
                    persistenceMemberList.add(rpgRedisTeamMemberVO);
                    memberWXUidList.add(rpgRedisTeamMemberVO.getUid());
                } else if (TeamMemberStatusEnum.JOINED_TEAM.getCode() == memberStatus) {
                    // 老板已支付, 已入团
                    orderActionEnum = TeamOrderRPGActionEnum.JOINED_TEAM_DISMISSD;
                    persistenceMemberList.add(rpgRedisTeamMemberVO);
                    memberWXUidList.add(rpgRedisTeamMemberVO.getUid());
                } else {
                    // 未支付的老板, 这里涉及到一个支付超时解散车队再去支付的问题
                    *//* 2018-10-25 退款流程优化, 这里返回 null即可 *//*
                    *//* orderActionEnum = TeamOrderActionEnum.PAY_TIME_OUT *//*
                    orderActionEnum = null;
                    // 删除定时任务
                    teamServiceSupport.removeBossPaymentTimeoutJob(teamSequence, uid);
                }
                // 车队准备中解散, 需要修改订单状态的车队老板
                if (ObjectTools.isNotNull(orderActionEnum)) {
                    MemberOrderParam memberOrderParam = createMemberOrderParam(
                            rpgRedisTeamMemberVO.getUid(), rpgRedisTeamMemberVO.getUsername(),
                            userIdentity, orderActionEnum,
                            rpgRedisTeamMemberVO.getBaojiLevel(),
                            rpgRedisTeamMemberVO.getRaidLocationCode());
                    memberOrderParamList.add(memberOrderParam);
                }
            }
        }
    }*/

    /**
     * 组装需要发送 MQ的数据
     * @param teamStatus
     * @param rpgRedisTeamMemberVOList
     * @param dismiss
     * @return
     */
    private List<OrderTeamRPGMember> assembleEndTeamOrderActionParams(
            List<RPGRedisTeamMemberVO> rpgRedisTeamMemberVOList,
            Integer teamStatus, boolean dismiss) {
        List<OrderTeamRPGMember> otmrpgList = new ArrayList<>();
        for (RPGRedisTeamMemberVO rpgRedisTeamMember : rpgRedisTeamMemberVOList) {
            Integer userIdentity = rpgRedisTeamMember.getUserIdentity();
            Integer memberStatus = rpgRedisTeamMember.getStatus();

            TeamOrderRPGActionEnum orderActionEnum = acquireEndTeamOrderAction(teamStatus,
                    userIdentity, memberStatus, dismiss);

            // 订单行为不为空需要发 MQ, 此时要组装订单参数
            if (ObjectTools.isNotNull(orderActionEnum)) {
                OrderTeamRPGMember orderTeamRPGMember = buildOrderTeamRPGMembers(
                        rpgRedisTeamMember.getUid(),
                        rpgRedisTeamMember.getUsername(),
                        userIdentity,
                        orderActionEnum,
                        rpgRedisTeamMember.getBaojiLevel(),
                        rpgRedisTeamMember.getRaidLocationCode());
                otmrpgList.add(orderTeamRPGMember);
            }
        }
        return otmrpgList;
    }

    private TeamOrderRPGActionEnum acquireEndTeamOrderAction(Integer teamStatus,
            Integer userIdentity, Integer memberStatus, boolean dismiss) {

        if (TeamStatusEnum.RUNNING.getCode() == teamStatus) {
            if (dismiss) {
                // 车队进行中解散
                return TeamOrderRPGActionEnum.TEAM_STARTED_DISMISSD;
            } else {
                // 车队正常正常结束, 所有队员订单行为为开车后结束
                return TeamOrderRPGActionEnum.TEAM_STARTED_FINISH;
            }
        } else if (TeamStatusEnum.PREPARING.getCode() == teamStatus) {
            // 车队准备中解散, 暴鸡不用管, 老板根据他当前在车队中的状态判断其订单行为
            if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                // 车队准备中解散暴鸡不用管, 老板需判断他是否已支付
                if (TeamMemberStatusEnum.PREPARE_JOIN_TEAM.getCode() == memberStatus) {
                    // 老板已支付, 待入团
                    return TeamOrderRPGActionEnum.PREPARE_JOIN_TEAM_DISMISSD;
                } else if (TeamMemberStatusEnum.JOINED_TEAM.getCode() == memberStatus) {
                    // 老板已支付, 已入团
                    return TeamOrderRPGActionEnum.JOINED_TEAM_DISMISSD;
                } else {
                    // 未支付的老板, 这里涉及到一个支付超时解散车队再去支付的问题
                    /* 2018-10-25 退款流程优化, 这里返回 null即可 */
                    /* orderActionEnum = TeamOrderActionEnum.PAY_TIME_OUT */
                    return null;
                }
            }
        }
        return null;
    }


    /**
     * 组装车队进行中解散的业务逻辑参数
     *
     * @param teamSequence 车队序列号
     * @param RPGRedisTeamMemberVOList 所有车队队员
     * @param memberOrderParamList 修改订单状态参数
     * @param persistenceMemberList 待持久化的车队队员
     * @param allMemberUidList 所有车队队员 uid
     * @param memberWXUidList 待微信推送的车队队员 uid
     */
    /*private void assembleRunningDismissTeam(String teamSequence,
            List<RPGRedisTeamMemberVO> RPGRedisTeamMemberVOList,
            List<MemberOrderParam> memberOrderParamList,
            List<RPGRedisTeamMemberVO> persistenceMemberList, List<String> allMemberUidList,
            List<String> memberWXUidList) {
        for (RPGRedisTeamMemberVO rpgRedisTeamMemberVO : RPGRedisTeamMemberVOList) {
            Integer userIdentity = rpgRedisTeamMemberVO.getUserIdentity();
            Integer memberStatus = rpgRedisTeamMemberVO.getStatus();
            String uid = rpgRedisTeamMemberVO.getUid();
            allMemberUidList.add(uid);
            if (TeamMemberStatusEnum.JOINED_TEAM.getCode() != memberStatus) {
                // 车队进行中, 所有队员状态一定都是已入团, 如果不是直接抛出业务异常
                logger.error(">> 车队[{}]进行中解散, 队员[{}]的状态不是已入团!", teamSequence, uid);
                throw new BusinessException(BizExceptionEnum.TEAM_RUNNING_MEMBER_STATUS_ERROR);
            }
            // 车队进行中解散需要获取所有的车队队员--这里暂时不考虑暴娘
            MemberOrderParam memberOrderParam = createMemberOrderParam(
                    rpgRedisTeamMemberVO.getUid(), rpgRedisTeamMemberVO.getUsername(),
                    userIdentity, TeamOrderRPGActionEnum.TEAM_STARTED_DISMISSD,
                    rpgRedisTeamMemberVO.getBaojiLevel(),
                    rpgRedisTeamMemberVO.getRaidLocationCode());
            memberOrderParamList.add(memberOrderParam);
            persistenceMemberList.add(rpgRedisTeamMemberVO);
            memberWXUidList.add(rpgRedisTeamMemberVO.getUid());
        }
    }*/

    /**
     * 处理车队结束 场景: 解散车队、结束车队
     *
     * @param gameResult 车队比赛结果
     * @param rpgRedisTeamVO 车队信息
     * @param leaderUid 队长 uid
     * @param orderTeamRPGMemberList 待调用的订单服务参数
     * @param persistenceMemberList 待入库的车队队员
     * @param rcMemberUidList 车队队员 uid, 融云推送使用
     * @param weXinOrderCancelEvent 微信订单服务取消通知
     * @param weXinOrderEndEvent 微信订单服务结束通知
     * @param dismissTeam 是否是解散车队
     */
    private void handleEndTeam(Integer gameResult, RPGRedisTeamVO rpgRedisTeamVO,
            String leaderUid, List<OrderTeamRPGMember> orderTeamRPGMemberList,
            List<RPGRedisTeamMemberVO> persistenceMemberList, List<String> rcMemberUidList,
            WeXinOrderCancelEvent weXinOrderCancelEvent,
            WeXinOrderEndEvent weXinOrderEndEvent, boolean dismissTeam) {
        String teamSequence = rpgRedisTeamVO.getSequence();
        String teamTitle = rpgRedisTeamVO.getTitle();
        // 更新车队状态
        if (dismissTeam) {
            rpgRedisTeamVO.setStatus(TeamStatusEnum.DISMISSED.getCode());
        } else {
            rpgRedisTeamVO.setStatus(TeamStatusEnum.COMPLETED.getCode());
        }
        Integer teamStatus = rpgRedisTeamVO.getStatus();
        // 创建结束车队本地事务--推送融云消息和微信服务通知
        EndTeamLocalTransaction endTeamLocalTransaction = createEndTeamLocalTransaction(
                rpgRedisTeamVO.getId(),
                teamSequence, teamStatus, gameResult, teamTitle,
                persistenceMemberList, leaderUid, rcMemberUidList,
                weXinOrderCancelEvent, weXinOrderEndEvent, dismissTeam);
        if (ObjectTools.isNotEmpty(orderTeamRPGMemberList)) {
            // 如果需要发送 mq 消息修改订单状态
            // 发送 mq 消息, 修改订单状态
            pushEndTeamUpdateOrderMQ(teamSequence, teamStatus, gameResult,
                    orderTeamRPGMemberList, endTeamLocalTransaction);
            // 如果需要执行本地事务, 本地事务已在 mq 的回调中处理了
        } else {
            // 如果不需要发送 MQ 消息修改订单状态, 直接本地处理
            /* 这里需要注意的是, 如果不发送 MQ 消息, 只会有一种情况, 那就是车队准备中解散,
               并且车队只有暴鸡(老板涉及到支付超时), 这时候是不需要保存车队队员的数据到 DB 中的,
               因此, 这里只需要修改车队状态和发布融云和微信通知就好了  */

            /*  2018-10-25 退款流程优化,
                如果此时车队中只有暴鸡和未支付的老板也不需要发 MQ,
                但同样不保存到 DB, 无影响
            */
            // 修改车队状态
            int result = teamRepository.updateTeamStatus(teamSequence, teamStatus);
            // 保存比赛结果
            teamGameResultRepository.insertSelective(TeamGameResult.builder()
                    .teamId(rpgRedisTeamVO.getId())
                    .gameResult(GameResultEnum.UNKNOWN.getCode())
                    // DNF 只有一局
                    .resultSequence(1).build());
            if (result > 0) {
                RcEndTeamEvent rcEndTeamEvent = endTeamLocalTransaction.getRcEndTeamEvent();
                if (ObjectTools.isNotNull(rcEndTeamEvent)) {
                    EventBus.post(rcEndTeamEvent);
                }
                if (ObjectTools.isNotNull(weXinOrderCancelEvent)) {
                    EventBus.post(weXinOrderCancelEvent);
                }
                if (ObjectTools.isNotNull(weXinOrderEndEvent)) {
                    EventBus.post(weXinOrderEndEvent);
                }
            } else {
                logger.error(">> 车队[{}]准备中解散, 此时车队中只有暴鸡队员, "
                        + "更新车队状态[{}]到 DB 中错误!", teamSequence, teamStatus);
                throw new BusinessException(BizExceptionEnum.TEAM_UPDATE_ERROR);
            }
        }
        // 存入车队结果
        saveTeamGameResultToRedis(teamSequence, teamTitle, gameResult);
        // 删除 redis 中的所有车队数据
        // 由于前面创建了融云的推送事件过滤了队长的 uid, 这里要把它加回来
        rcMemberUidList.add(leaderUid);
        rpgTeamMemberCacheManager.removeAllRedisTeamData(rpgRedisTeamVO.getGameCode(),
                teamSequence, rcMemberUidList);
        if (dismissTeam) {
            logger.info(">> 队长[{}]解散车队[{}]成功, 车队状态为: {}, 比赛结果为: {}",
                    leaderUid, teamSequence, teamStatus, gameResult);
        } else {
            logger.info(">> 队长[{}]结束车队[{}]成功, 车队状态为: {}, 比赛结果为: {}",
                    leaderUid, teamSequence, teamStatus, gameResult);
        }
    }

    @Override
    @RedisDistlock
    public void confirmJoinTeam(String sequence) {
        String uid = UserSessionContext.getUser().getUid();
        // 获取当前的车队信息
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        // 校验车队异常状态
        Integer teamStatus = rpgRedisTeamVO.getStatus();
        checkTeamStatus(rpgRedisTeamVO.getSequence(),
                TeamMemberActionEnum.RPG_CONFIRM_JOIN, teamStatus);

        // 获取当前队员
        RPGRedisTeamMemberVO rpgRedisTeamMemberVO = rpgTeamMemberCacheManager
                .getCurrentTeamMember(sequence, uid);
        Integer userIdentity = rpgRedisTeamMemberVO.getUserIdentity();
        // 老板确认入团--调用订单服务校验其是否已支付
        if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
            CheckTeamMemberPayedParams checkPayedParams = new CheckTeamMemberPayedParams();
            checkPayedParams.setTeamSequeue(sequence);
            checkPayedParams.setUid(uid);
            checkPayedParams.setUserIdentity(userIdentity);
            logger.info(">> 老板确认入团, 开始调用交易服务校验支付状态, 参数: {}",
                    checkPayedParams);
            long start = System.currentTimeMillis();
            ResponsePacket<Void> checkPayedResponse = rpgOrdersServiceClient
                    .checkTeamMemberPayed(checkPayedParams);
            logger.info(">> 老板确认入团, 调用交易服务校验支付状态耗时: {} ms",
                    (System.currentTimeMillis() - start));
            if (!checkPayedResponse.responseSuccess()) {
                logger.error(">> 老板[{}]在车队[{}]中确认入团, 调用交易服务校验支付状态错误: {}",
                        uid, sequence, checkPayedResponse);
                throw new BusinessException(BizExceptionEnum.TEAM_BOSS_PAYMENT_CALLBACK_ERROR);
            }
        }
        // 更新队员状态为已入团
        rpgRedisTeamMemberVO.setStatus(TeamMemberStatusEnum.JOINED_TEAM.getCode());
        rpgRedisTeamMemberVO.setGmtModified(new Date());
        rpgTeamMemberCacheManager.updateRPGTeamMember(rpgRedisTeamMemberVO);
        // 发布确认入团事件
        teamServiceSupport.postConfirmJoinTeamEvent(rpgRedisTeamVO.getSequence());
    }

    @Override
    @RedisDistlock
    public void startTeam(String sequence) {
        String leaderUid = UserSessionContext.getUser().getUid();
        // 获取当前的车队信息
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        rpgTeamMemberCacheManager.checkUserInCurrentTeam(sequence,
                leaderUid, rpgRedisTeamVO.getGameCode());
        // 校验车队异常状态
        Integer teamStatus = rpgRedisTeamVO.getStatus();
        checkTeamStatus(rpgRedisTeamVO.getSequence(), TeamMemberActionEnum.START, teamStatus);
        // 校验是否是队长
        RPGRedisTeamMemberVO leaderVO = rpgTeamMemberCacheManager
                .getCurrentTeamMember(sequence, leaderUid);
        Integer userIdentity = leaderVO.getUserIdentity();
        if (UserIdentityEnum.LEADER.getCode() != userIdentity) {
            logger.error(">> 立即开车时, 用户[{}]在车队[{}]中不是队长!", leaderUid, sequence);
            throw new BusinessException(BizExceptionEnum.TEAM_NOT_LEADER);
        }
        // 校验车队是否已满员
        List<RPGRedisTeamMemberVO> allMemberList =
                rpgTeamMemberCacheManager.getAllRPGRedisTeamMemberVO(sequence);
        int size = allMemberList.size();
        if (!rpgRedisTeamVO.getActuallyPositionCount().equals(size)) {
            logger.error(">> 立即开车时, 车队[{}]未满员!", sequence);
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_NOT_FULL);
        }
        // 校验车队队员(是否有老板, 是否都已入团)
        List<String> allUidList = new ArrayList<>();
        List<RPGBaojiInfoVO> baojiInfoList = new ArrayList<>(20);
        List<String> bossUidList = new ArrayList<>(20);
        // 组装立即开车的其他业务逻辑数据
        boolean hasBoss = assembleStartTeamData(sequence, allMemberList, allUidList,
                baojiInfoList, bossUidList);
        if (!hasBoss) {
            logger.error(">> 立即开车时, 车队[{}]中无老板队员", sequence);
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_HAS_NO_BOSS);
        }
        if (baojiInfoList.size() == 0) {
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_HAS_NO_BAOJI);
        }
        RPGTeamStartOrderVO teamStartOrder = BeanMapper.map(rpgRedisTeamVO, RPGTeamStartOrderVO.class);
        teamStartOrder.setBaojiInfoList(baojiInfoList);
        teamStartOrder.setBossUidList(bossUidList);
        // 修改车队状态为进行中
        rpgRedisTeamVO.setStatus(TeamStatusEnum.RUNNING.getCode());
        rpgRedisTeamVO.setGmtModified(new Date());
        // 创建立即开车本地事务
        StartTeamLocalTransaction startTeamLocalTransaction
                = createStartTeamLocalTransaction(rpgRedisTeamVO, leaderUid, allUidList);
        // -- 发送 mq 消息创建暴鸡订单
        pushStartTeamCreateBaojiOrderToMQ(teamStartOrder, startTeamLocalTransaction);
        // -- mq 消息处理结束
        // 更新 redis 车队状态为 进行中
        String teamKey = String.format(RedisKey.TEAM_PREFIX, rpgRedisTeamVO.getSequence());
        cacheManager.set(teamKey, JsonsUtils.toJson(rpgRedisTeamVO));
        logger.info(">> 立即开车成功, 车队: {}, 实际位置数: {}",
                sequence, rpgRedisTeamVO.getActuallyPositionCount());
    }

    /**
     * 组装立即开车参数
     *
     * @param sequence 车队序列号
     * @param allMemberList 所有车队队员
     * @param allUidList 所有车队队员 uid
     * @param baojiInfoList 所有暴鸡队员
     * @param bossUidList 所有老板队员的 uid
     */
    private boolean assembleStartTeamData(String sequence, List<RPGRedisTeamMemberVO> allMemberList,
            List<String> allUidList, List<RPGBaojiInfoVO> baojiInfoList,
            List<String> bossUidList) {
        boolean hasBoss = false;
        for (RPGRedisTeamMemberVO RPGRedisTeamMemberVO : allMemberList) {
            String memberUid = RPGRedisTeamMemberVO.getUid();
            Integer memberStatus = RPGRedisTeamMemberVO.getStatus();
            Integer memberIdentity = RPGRedisTeamMemberVO.getUserIdentity();
            allUidList.add(memberUid);
            if (TeamMemberStatusEnum.JOINED_TEAM.getCode() != memberStatus) {
                // 只要有一个队员的状态不是 已入团, 就抛出业务异常
                logger.error(">> 立即开车时, 车队队员[{}]在车队[{}]的状态不是已入团!",
                        memberUid, sequence);
                throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_NOT_ALL_JOINED_TEAM);
            }
            if (UserIdentityEnum.BOSS.getCode() == memberIdentity) {
                // 只要有一个老板
                bossUidList.add(memberUid);
                hasBoss = true;
            }
            if (UserIdentityEnum.LEADER.getCode() == memberIdentity
                    || UserIdentityEnum.BAOJI.getCode() == memberIdentity) {
                // 获取暴鸡信息
                RPGBaojiInfoVO baojiInfo = BeanMapper
                        .map(RPGRedisTeamMemberVO, RPGBaojiInfoVO.class);
                baojiInfoList.add(baojiInfo);
            }
        }
        return hasBoss;
    }

    /**
     * 立即开车时, 发送创建暴鸡订单消息到 mq
     *
     * @param baojiInfoForOrder 创建暴鸡订单所需消息
     * @param startTeamLocalTransaction 立即开车时的本地事务
     */
    private void pushStartTeamCreateBaojiOrderToMQ(RPGTeamStartOrderVO baojiInfoForOrder,
            StartTeamLocalTransaction startTeamLocalTransaction) {
        // 构建 mq 立即开车消息
        TeamStartOrderMessage teamStartOrderMessage = new TeamStartOrderMessage();
        teamStartOrderMessage.setRPGTeamStartOrderVO(baojiInfoForOrder);
        // 发送立即开车事务消息
        sendTransactionMessage(startTeamLocalTransaction.getTeamSequence(),
                RocketMQConstant.CREATE_BAOJI_ORDER_TAG, teamStartOrderMessage,
                startTeamLocalTransaction, startTeamTransactionProducer);
    }


    @Override
    @RedisDistlock
    public void endTeam(RPGTeamEndParams rpgTeamEndParams) {
        String leaderUid = UserSessionContext.getUser().getUid();
        // 获取车队信息
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager
                .queryTeamInfoBySequence(rpgTeamEndParams.getSequence());
        // 校验车队状态, 当前操作人是否是队长
        String teamSequence = rpgRedisTeamVO.getSequence();
        Integer teamStatus = rpgRedisTeamVO.getStatus();
        rpgTeamMemberCacheManager.checkUserInCurrentTeam(teamSequence,
                leaderUid, rpgRedisTeamVO.getGameCode());
        checkTeamStatus(teamSequence, TeamMemberActionEnum.END, teamStatus);

        RPGRedisTeamMemberVO leaderMember = rpgTeamMemberCacheManager
                .getCurrentTeamMember(teamSequence, leaderUid);
        if (UserIdentityEnum.LEADER.getCode() != leaderMember.getUserIdentity()) {
            logger.error(">> 结束车队[{}]时, 操作人[{}]不是队长!", teamSequence, leaderUid);
            throw new BusinessException(BizExceptionEnum.TEAM_NOT_LEADER);
        }
        // 开车到结束不能小于 30s
        checkTeamEndToFast(teamSequence, rpgRedisTeamVO.getGmtModified());
        // 获取所有队员, 准备结束车队...
        List<RPGRedisTeamMemberVO> allRPGRedisTeamMemberVO =
                rpgTeamMemberCacheManager.getAllRPGRedisTeamMemberVO(teamSequence);
        Integer gameResult = rpgTeamEndParams.getGameResult();
        // 待持久化的车队队员
        List<RPGRedisTeamMemberVO> persistenceMemberList =
                assembleEndTeamPersistTeamMember(allRPGRedisTeamMemberVO, teamStatus);
        // 待融云推送的车队队员 uid 集合
        List<String> rcMemberUidList = allRPGRedisTeamMemberVO.stream()
                .map(RPGRedisTeamMemberVO::getUid)
                .collect(Collectors.toList());
        // 待推送微信服务通知的 uid 集合, 结束车队所有人都要推, 用 rcMemberUidList即可
        /*List<String> memberWXUidList =
                assembleEndTeamWXPushTeamMember(allRPGRedisTeamMemberVO, teamStatus);*/
        // 调用交易服务所需参数
        List<OrderTeamRPGMember> otmList =
                assembleEndTeamOrderActionParams(allRPGRedisTeamMemberVO, teamStatus, false);
        // 判断车队状态--组装结束车队时的其他业务逻辑参数
        if (GameResultEnum.UNKNOWN.getCode() == gameResult) {
            // 车队完成时的游戏结果不能为默认值, 否则抛出业务异常
            logger.error(">> 结束车队[{}]时, 车队比赛结果[{}]错误!", teamSequence, gameResult);
            throw new BusinessException(BizExceptionEnum.TEAM_RUNNING_GAME_RESULT_ERROR);
        }
        // 车队完成时需要向所有车队队员推送订单完成服务通知
        WeXinOrderEndEvent weXinOrderEndEvent = buildEndTeamOrderEnd(rpgRedisTeamVO,
                leaderMember.getUsername(), rcMemberUidList);
        // 处理结束车队
        handleEndTeam(gameResult, rpgRedisTeamVO, leaderUid, otmList,
                persistenceMemberList, rcMemberUidList, null,
                weXinOrderEndEvent, false);
    }

    /**
     * 构建订单结束通知
     *
     * @param rpgRedisTeamVO 车队信息
     * @param leaderUsername 队长 username
     * @param memberUidList 车队队员 uid
     */
    private WeXinOrderEndEvent buildEndTeamOrderEnd(RPGRedisTeamVO rpgRedisTeamVO,
            String leaderUsername, List<String> memberUidList) {
        WeXinOrderEndEvent weXinOrderEndEvent = WeXinOrderEndEvent.builder()
                .teamSequence(rpgRedisTeamVO.getSequence())
                .gameName(rpgRedisTeamVO.getGameName())
                .raidName(rpgRedisTeamVO.getRaidName())
                .leaderName(leaderUsername)
                .memberUidList(memberUidList)
                .build();
        return weXinOrderEndEvent;
    }


    @Override
    public PagingResponse<RPGTeamListVO> findTeamList(Integer gameCode,
            TeamQueryParams teamQueryParams) {
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, gameCode);
        //1:判断服务器区服，如果有筛选条件，则拿到跨区信息
        Integer acrossZoneCode = Optional.ofNullable(teamQueryParams)
                .map(TeamQueryParams::getZoneSmallCode)
                .map(it -> gameConfigService.getBigAndAcrossZoneBySmallZoneCode(gameCode, it))
                .map(RedisSmallZoneRefAcrossZone::getZoneAcrossCode).orElse(null);
        //2：先通关数据库初始分页，并排序查找
        TeamDataBaseQueryParams params = new TeamDataBaseQueryParams(gameCode,
                teamQueryParams == null ? null : teamQueryParams.getRaidCode(), acrossZoneCode);
        Page<Team> page = PageHelper
                .startPage(teamQueryParams.getOffset(), teamQueryParams.getLimit())
                .doSelectPage(() -> teamRepository.selectHomePageTeamList(params));

        //3:查出队员信息并转换成车队列表
        List<RPGTeamListVO> RPGTeamListVOS = convertAndSortTeamList(page.getResult());
        PagingResponse<RPGTeamListVO> pagingResponse = new PagingResponse<>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), RPGTeamListVOS);
        return pagingResponse;
    }

    @Override
    public GameTeamTotal findTeamTotal(Integer gameCode, TeamQueryParams teamQueryParams) {
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, gameCode);
        //第一步判断服务器区服，如果有筛选条件，则拿到跨区信息
        Integer acrossZoneCode = null;
        if (teamQueryParams != null && teamQueryParams.getZoneSmallCode() != null) {
            RedisSmallZoneRefAcrossZone redisSmallZoneRefAcrossZone = gameConfigService
                    .getBigAndAcrossZoneBySmallZoneCode(gameCode,
                            teamQueryParams.getZoneSmallCode());
            acrossZoneCode = redisSmallZoneRefAcrossZone.getZoneAcrossCode();
        }
        TeamDataBaseQueryParams params = new TeamDataBaseQueryParams(gameCode,
                teamQueryParams == null ? null : teamQueryParams.getRaidCode(), acrossZoneCode);
        GameTeamTotal gameTeamTotal = new GameTeamTotal();
        gameTeamTotal.setGameType(gameCode);
        gameTeamTotal.setPremadeNum(teamRepository.selectHomePageTeamTotal(params));
        return gameTeamTotal;
    }

    @Override
    public String getSmallProgramTeamQrCode(Integer gameCode, String sequence,String uid) {
        String content = String.format(gamingTeamConfig.getSmallProgramInvitationUrl(), sequence,uid);
        InputStream in = QrCodeUtils.createCodeStream(content);
        String fileName = fileUploadService.upload(in, env.getProperty(CommonConstants.PROJECT_ENV),
                UploadFolderConstants.QR_CODE_FLODER, sequence, false);
        return fileUploadService.getUploadFileLink(fileName);
    }

    @Override
    @RedisDistlock(expireTime = 800)
    public void updateTeamPositioncount(String uid, UpdatePositionCountParams positionCountParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid, positionCountParams);
        //1：从redis中获取缓存信息
        RPGRedisTeamVO teamInfoVO = rpgTeamCacheManager
                .queryTeamInfoBySequence(positionCountParams.getSequence());
        //2:校验是否是这个队伍的队长操作,先从redis中查出操作人,再看是不是队长
        String teamMemberKey = String.format(RedisKey.TEAM_MEMBER_PREFIX, teamInfoVO.getId());
        RPGRedisTeamMemberVO memberVO = cacheManager.hget(teamMemberKey, uid, RPGRedisTeamMemberVO.class);
        ValidateAssert.allNotNull(BizExceptionEnum.OPERATE_PERSON_DIFFER, memberVO);
        ValidateAssert
                .isTrue(UserIdentityEnum.LEADER.getCode() == memberVO.getUserIdentity(),
                        BizExceptionEnum.OPERATE_PERSON_DIFFER);

        //3:校验是否超过原未知数
        ValidateAssert
                .isTrue(teamInfoVO.getOriginalPositionCount() >=
                                positionCountParams.getNumber(),
                        BizExceptionEnum.TEAM_SEAT_UPPER_LIMIT);

        //4:校验车队是否在准备状态，不在拒绝
        ValidateAssert.isTrue(TeamStatusEnum.PREPARING.getCode() ==
                        teamInfoVO.getStatus(), BizExceptionEnum.TEAM_IS_NOT_PREPARING);

        //5:校验当前队员数是否小于设定值，如果是，则拒绝
        // 5.1 从redis中获取当前队员
        Map<String, RPGRedisTeamMemberVO> teamMemberMap = cacheManager
                .hgetAll(teamMemberKey, RPGRedisTeamMemberVO.class);
        ValidateAssert.isFalse(teamMemberMap.size() > positionCountParams.getNumber(),
                BizExceptionEnum.TEAM_SEAT_DOWN_LIMIT);

        //6:更新缓存中车队位置信息
        teamInfoVO.setActuallyPositionCount(positionCountParams.getNumber());
        cacheManager.set(String.format(RedisKey.TEAM_PREFIX, positionCountParams.getSequence()),
                JsonsUtils.toJson(teamInfoVO));
        List<String> uids = teamMemberMap.values().stream().map(RPGRedisTeamMemberBaseVO::getUid)
                .collect(Collectors.toList());
        //7:数据入库和消息异步通知
        UpdateTeamPositionEvent event = new UpdateTeamPositionEvent(teamInfoVO.getId(),
                teamInfoVO.getSequence(), positionCountParams.getNumber(), uids);
        EventBus.post(event);

    }

    @Override
    public RPGRedisTeamBaseVO getTeamBaseInfo(String sequence) {
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        Integer teamStatus = rpgRedisTeamVO.getStatus();
        if (TeamStatusEnum.DISMISSED.getCode() == teamStatus) {
            throw new BusinessException(BizExceptionEnum.TEAM_HAS_DISMISSED);
        }
        if (TeamStatusEnum.COMPLETED.getCode() == teamStatus) {
            throw new BusinessException(BizExceptionEnum.TEAM_HAS_COMPLETED);
        }
        RPGRedisTeamBaseVO rpgRedisTeamBaseVO =
                BeanMapper.map(rpgRedisTeamVO, RPGRedisTeamBaseVO.class);
        return rpgRedisTeamBaseVO;
    }

    @Override
    public RPGTeamCurrentInfoVO getTeamCurrentInfo(String sequence) {
        long start = System.currentTimeMillis();
        String uid = UserSessionContext.getUser().getUid();
        // 获取车队信息
        RPGRedisTeamVO redisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        rpgTeamMemberCacheManager.checkUserInCurrentTeam(sequence, uid, redisTeamVO.getGameCode());
        // 校验车队状态
        Integer teamStatus = redisTeamVO.getStatus();
        checkTeamStatus(sequence, TeamMemberActionEnum.CURRENT_INFO, teamStatus);

        // 获取车队成员列表
        List<RPGRedisTeamMemberVO> teamMemberVOS = rpgTeamMemberCacheManager
                .getAllRPGRedisTeamMemberVO(sequence);
        // 当前队员
        RPGRedisTeamMemberVO currentTeamMember = teamMemberVOS.stream()
                .filter(r -> r.getUid().equals(uid)).findFirst().get();
        if (currentTeamMember == null) {
            logger.error("用户[{}]获取车队[{}]实时信息, 该用户已不再车队中!", uid, sequence);
            throw new BusinessException(BizExceptionEnum.USER_NOT_IN_CURRENT_TEAM);
        }
        Integer userIdentity = currentTeamMember.getUserIdentity();
        List<RPGRedisTeamMemberBaseVO> teamMemberBaseVOS = new ArrayList<>(teamMemberVOS.size());
        Collections.copy(teamMemberBaseVOS, teamMemberVOS);

        // 排序--身份降序, 加入时间升序
        teamMemberBaseVOS.sort(Comparator
                        .comparingInt(RPGRedisTeamMemberBaseVO::getUserIdentity).reversed()
                        .thenComparingLong(m -> m.getJoinTime().getTime()));

        RPGTeamCurrentInfoVO teamCurrentInfo = new RPGTeamCurrentInfoVO();
        teamCurrentInfo.setTeamStatus(teamStatus);
        // 实际位置数
        teamCurrentInfo.setActuallyPositionCount(redisTeamVO.getActuallyPositionCount());
        // 禁用的位置数
        int disablePositionCount = redisTeamVO.getOriginalPositionCount()
                - redisTeamVO.getActuallyPositionCount();
        teamCurrentInfo.setDisablePositionCount(disablePositionCount);
        teamCurrentInfo.setUid(uid);
        teamCurrentInfo.setUserIdentity(userIdentity);
        // 老板支付超时时间
        Integer bossPayCountDown = calculateBossPayCountDown(redisTeamVO.getPaymentTimeout(),
                currentTeamMember.getJoinTime(), userIdentity);
        teamCurrentInfo.setPayCountdown(bossPayCountDown);
        // 预期收益
        Integer profit = 0;
        if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
            // 老板直接返回车队价格
            profit = redisTeamVO.getDiscountFee();
        }
        List<PVPTeamMembersIncome> teamMemberProfitList = new ArrayList<>(20);
        // 是否需要计算预期收益
        boolean shouldCalculateProfit = shouldCalculateProfit(redisTeamVO, teamStatus,
                teamMemberVOS, userIdentity);
        if (shouldCalculateProfit) {
            // 计算预期收益
            PVPPreIncomeVo preInComeVo = calculateProfit(sequence, redisTeamVO.getGameCode(),
                    redisTeamVO.getRaidCode(), redisTeamVO.getDiscountFee(),
                    currentTeamMember, teamMemberBaseVOS);
            teamMemberProfitList = preInComeVo.getTeamMembersIncomes();
            // 按身份排序, 队长排第一位
            teamMemberProfitList.sort(Comparator
                    .comparingInt(PVPTeamMembersIncome::getUserIdentity).reversed());
            if (UserIdentityEnum.LEADER.getCode() == userIdentity
                    || UserIdentityEnum.BAOJI.getCode() == userIdentity) {
                // 如果当前用户身份是暴鸡或队长返回预计收益
                profit = preInComeVo.getPrice();
            }
        }
        // 返回结果
        teamCurrentInfo.setPrice(profit);
        teamCurrentInfo.setTeamMemberProfitList(teamMemberProfitList);
        teamCurrentInfo.setTeamMemberList(teamMemberBaseVOS);
        teamCurrentInfo.setMemberSize(teamMemberBaseVOS.size());
        logger.info(">> 用户[{}]获取车队[{}]实时数据, 耗时: {} ms",
                uid, sequence, (System.currentTimeMillis() - start));
        return teamCurrentInfo;
    }

    private boolean shouldCalculateProfit(RPGRedisTeamVO redisTeamVO, Integer teamStatus,
            List<RPGRedisTeamMemberVO> teamMemberVOS, Integer userIdentity) {
        // 是否计算预期收益
        boolean shouldCalculateProfit = false;
        // 如果车队已经满员, 车队中的暴鸡队员和队长需要调用交易接口计算收益
        if (UserIdentityEnum.BAOJI.getCode() == userIdentity
                || UserIdentityEnum.LEADER.getCode() == userIdentity) {
            if (redisTeamVO.getActuallyPositionCount().equals(teamMemberVOS.size())) {
                shouldCalculateProfit = true;
            }
        }
        // 如果车队进行中有暴鸡退出车队
        if (TeamStatusEnum.RUNNING.getCode() == teamStatus) {
            if (UserIdentityEnum.LEADER.getCode() == userIdentity
                    || UserIdentityEnum.BAOJI.getCode() == userIdentity) {
                // 此时车队中的其他暴鸡需要调用交易接口计算收益
                shouldCalculateProfit = true;
            }
        }
        return shouldCalculateProfit;
    }


    /**
     * 计算老板支付倒计时
     * @param paymentTimeout 支付超时时间
     * @param joinTime 加入车队时间
     * @param userIdentity 队员身份, 暴鸡直接返回 0
     * @return
     */
    private Integer calculateBossPayCountDown(Integer paymentTimeout,
            Date joinTime, Integer userIdentity) {
        Integer bossPayCountDown = 0;
        if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
            // 老板返回支付倒计时
            LocalDateTime joinDateTime = LocalDateTime
                    .ofInstant(joinTime.toInstant(), ZoneId.systemDefault());
            Duration duration = Duration.between(joinDateTime, LocalDateTime.now());
            bossPayCountDown = Long.valueOf(paymentTimeout - duration.getSeconds()).intValue();
            if (bossPayCountDown < 0) {
                // 倒计时小于 0 说明定时任务执行异常或者车队此时在进行中, 直接返回 0
                bossPayCountDown = 0;
            }
        }
        return bossPayCountDown;
    }


    @Override
    public BossInfoForOrderVO getBossInfoForOrder(String sequence, String uid) {
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        RPGRedisTeamMemberVO bossMember = rpgTeamMemberCacheManager
                .getCurrentTeamMember(sequence, uid);
        Integer userIdentity = bossMember.getUserIdentity();
        ValidateAssert.isTrue(UserIdentityEnum.BOSS.getCode() == userIdentity,
                BizExceptionEnum.TEAM_MEMBER_NOT_BOSS);
        if (TeamStatusEnum.PREPARING.getCode() != rpgRedisTeamVO.getStatus()) {
            // 车队在准备中才可以创建老板订单
            logger.error(">> 创建老板[{}]订单, 车队[{}]不在准备中!", uid, sequence);
            throw new BusinessException(BizExceptionEnum.TEAM_IS_NOT_PREPARING);
        }
        BossInfoForOrderVO bossInfoForOrderVO = BeanMapper
                .map(rpgRedisTeamVO, BossInfoForOrderVO.class);
        BeanMapper.map(bossMember, bossInfoForOrderVO);
        bossInfoForOrderVO
                .setPrice(rpgRedisTeamVO.getDiscountFee() == null ? 0 : rpgRedisTeamVO.getDiscountFee());
        return bossInfoForOrderVO;
    }


    @Override
    public RPGTeamStartOrderVO getBaojiInfoForOrder(String sequence) {
        // 1. 获取当前的车队信息
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        // 2. 校验车队异常状态
        Integer teamStatus = rpgRedisTeamVO.getStatus();
        // 这里先改为车队状态不是准备中都可以创建暴鸡订单...因为有延时
        if (TeamStatusEnum.PREPARING.getCode() == teamStatus) {
            // 如果此时车队状态是准备中, 不允许创建暴鸡订单
            logger.error(">> 创建暴鸡订单, 车队[{}]状态[{}]错误!", sequence, teamStatus);
            throw new BusinessException(BizExceptionEnum.TEAM_CREATE_BAOJI_ORDER_STATUS_ERROR);
        }
        List<RPGBaojiInfoVO> baojiInfoList = new ArrayList<>(20);
        List<String> bossUidList = new ArrayList<>(20);
        // 根据车队状态获取车队中的暴鸡队员信息
        if (TeamStatusEnum.RUNNING.getCode() == teamStatus) {
            // 车队进行中从 redis 中获取车队队员
            logger.info(">> 创建暴鸡订单, 车队[{}]正在进行中", sequence);
            // 获取车队队员
            List<RPGRedisTeamMemberVO> memberList =
                    rpgTeamMemberCacheManager.getAllRPGRedisTeamMemberVO(sequence);
            // 校验车队队员状态
            for (RPGRedisTeamMemberVO RPGRedisTeamMemberVO : memberList) {
                if (TeamMemberStatusEnum.JOINED_TEAM.getCode() != RPGRedisTeamMemberVO.getStatus()) {
                    // 只要有一个队员的状态不是 已入团, 就抛出业务异常
                    String memberUid = RPGRedisTeamMemberVO.getUid();
                    logger.error(">> 创建暴鸡订单, 车队[{}]正在进行中, 车队队员[{}]的状态不是已入团!",
                            sequence, memberUid);
                    throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_NOT_ALL_JOINED_TEAM);
                }
                // 封装暴鸡信息
                Integer userIdentity = RPGRedisTeamMemberVO.getUserIdentity();
                if (UserIdentityEnum.LEADER.getCode() == userIdentity
                        || UserIdentityEnum.BAOJI.getCode() == userIdentity) {
                    // 暴鸡和队长
                    RPGBaojiInfoVO baojiInfo = BeanMapper
                            .map(RPGRedisTeamMemberVO, RPGBaojiInfoVO.class);
                    baojiInfoList.add(baojiInfo);
                } else if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                    // 老板 uid
                    bossUidList.add(RPGRedisTeamMemberVO.getUid());
                }
            }
        } else {
            // 车队已解散或已完成的情况下直接去数据库查询队员
            logger.info(">> 创建暴鸡订单, 车队[{}]当前的状态为: {}", sequence, teamStatus);
            List<TeamMemberRPG> teamMemberList = teamMemberRPGRepository.getMemberListByTeam(sequence);
            if (ObjectTools.isNotEmpty(teamMemberList)) {
                for (TeamMemberRPG teamMember : teamMemberList) {
                    // 封装暴鸡信息
                    Byte userIdentity = teamMember.getUserIdentity();
                    if (UserIdentityEnum.LEADER.getCode() == userIdentity
                            || UserIdentityEnum.BAOJI.getCode() == userIdentity) {
                        // 暴鸡和队长
                        RPGBaojiInfoVO baojiInfo = BeanMapper
                                .map(teamMember, RPGBaojiInfoVO.class);
                        baojiInfoList.add(baojiInfo);
                    } else if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                        // 老板 uid
                        bossUidList.add(teamMember.getUid());
                    }
                }
            }
        }
        if (baojiInfoList.size() == 0) {
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_HAS_NO_BAOJI);
        }
        RPGTeamStartOrderVO baojiInfoForOrder = BeanMapper
                .map(rpgRedisTeamVO, RPGTeamStartOrderVO.class);
        baojiInfoForOrder.setBaojiInfoList(baojiInfoList);
        baojiInfoForOrder.setBossUidList(bossUidList);
        return baojiInfoForOrder;
    }

    @Override
    public RPGMemberInTeamVO getMemberInTeam(Integer gameCode) {
        String uid = UserSessionContext.getUser().getUid();
        String uidShardingKey = RedisUtils.buildHashSegmentKey(uid,
                RedisKey.TEAM_USER_GAME_PREFIX, ObjectTools.covertToString(gameCode));
        String teamSequence = cacheManager.hget(uidShardingKey, uid, String.class);
        RPGMemberInTeamVO memberInTeamVO = new RPGMemberInTeamVO();
        if (ObjectTools.isNotEmpty(teamSequence)) {
            RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(teamSequence);
            Integer teamStatus = rpgRedisTeamVO.getStatus();
            if (TeamStatusEnum.DISMISSED.getCode() == teamStatus) {
                throw new BusinessException(BizExceptionEnum.TEAM_HAS_DISMISSED);
            }
            if (TeamStatusEnum.COMPLETED.getCode() == teamStatus) {
                throw new BusinessException(BizExceptionEnum.TEAM_HAS_COMPLETED);
            }
            List<RPGRedisTeamMemberVO> allRPGRedisTeamMemberVO =
                    rpgTeamMemberCacheManager.getAllRPGRedisTeamMemberVO(teamSequence);
            memberInTeamVO.setMemberInTeam(true);
            memberInTeamVO.setActuallyPositionCount(rpgRedisTeamVO.getActuallyPositionCount());
            memberInTeamVO.setSequence(teamSequence);
            memberInTeamVO.setStatus(rpgRedisTeamVO.getStatus());
            memberInTeamVO.setMemberSize(allRPGRedisTeamMemberVO.size());
            logger.info(">> 获取用户[{}]在车队[{}]中的数据", uid, teamSequence);
        } else {
            memberInTeamVO.setMemberInTeam(false);
        }
        return memberInTeamVO;
    }

    @Override
    public Integer getBossPayCountdown(String sequence) {
        String uid = UserSessionContext.getUser().getUid();
        // 1. 获取车队信息
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        rpgTeamMemberCacheManager.checkUserInCurrentTeam(sequence, uid,
                rpgRedisTeamVO.getGameCode());
        // todo 这个接口是否还需要...需要的话这个行为要变更
        checkTeamStatus(rpgRedisTeamVO.getSequence(),
                TeamMemberActionEnum.START, rpgRedisTeamVO.getStatus());
        // 2. 获取老板支付倒计时, 如果是其他身份直接返回 0
        RPGRedisTeamMemberVO uidMember = rpgTeamMemberCacheManager
                .getCurrentTeamMember(sequence, uid);
        int payCountdown = 0;
        // 老板返回支付倒计时
        if (UserIdentityEnum.BOSS.getCode() == uidMember.getUserIdentity()) {
            try {
                Date joinTime = uidMember.getJoinTime();
                LocalDateTime joinDateTime = LocalDateTime
                        .ofInstant(joinTime.toInstant(), ZoneId.systemDefault());
                Duration duration = Duration.between(joinDateTime, LocalDateTime.now());
                Long pcd = rpgRedisTeamVO.getPaymentTimeout() - duration.getSeconds();
                payCountdown = pcd.intValue();
                if (payCountdown < 0) {
                    // 小于 0 时直接返回 0, 此时说明老板定时任务执行失败
                    logger.error(">> 获取老板[{}]在车队[{}]中的支付倒计时, "
                            + "老板超时未支付定时任务执行失败!", uid, sequence);
                    payCountdown = 0;
                }
            } catch (Exception e) {
                logger.error(">> 获取老板[{}]在车队[{}]中的支付倒计时, 倒计时计算失败!", uid, sequence);
                payCountdown = 0;
            }
            logger.info(">> 老板[{}]在车队[{}]中的支付倒计时剩余[{}]秒", uid, sequence, payCountdown);
        }
        // 暴鸡不用计算, 直接返回 0
        return payCountdown;
    }

    @Override
    public TeamGameResultVO getTeamGameResult(String sequence) {
        return rpgTeamCacheManager.queryTeamGameResultBySequence(sequence);
    }

    @Override
    public List<TeamMemberCompaintAdminVO> getTeamMemberBriefInfo(String sequence) {
        List<TeamMemberCompaintAdminVO> briefInfo = teamMemberRPGRepository.selectBriefInfo(sequence);
        if (ObjectTools.isEmpty(briefInfo)) {
            logger.error(">> Python 后台获取车队队员的简略信息, 车队[{}]中无队员", sequence);
            throw new BusinessException(BizExceptionEnum.TEAM_HAS_NO_MEMBER);
        }
        List<String> uidList = new ArrayList<>();
        for (TeamMemberCompaintAdminVO teamMemberCompaintAdminVO : briefInfo) {
            String uid = teamMemberCompaintAdminVO.getUid();
            Integer userIdentity = teamMemberCompaintAdminVO.getUserIdentityCode();
            if (null == userIdentity) {
                userIdentity = 0;
            }
            Integer baojiLevel = teamMemberCompaintAdminVO.getBaojiLevelCode();
            if (null == baojiLevel) {
                baojiLevel = 0;
            }
            teamMemberCompaintAdminVO.setUserIdentityDesc(
                    UserIdentityEnum.getByCode(userIdentity.byteValue()).getDesc());
            teamMemberCompaintAdminVO.setBaojiLevelDesc(
                    BaojiLevelEnum.getByCode(baojiLevel).getDesc());
            uidList.add(uid);
        }
        // 调用用户服务批量查询用户信息--todo userserviceclient
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(HttpConstant.CONTENT_TYPE_JSON);
        headers.setContentType(type);
        Map<String, Object> uidMap = new HashMap<>(20);
        uidMap.put("uid_list", uidList);
        HttpEntity httpEntity = new HttpEntity<>(JsonsUtils.toJson(uidMap), headers);
        String batchQueryUrl = gamingTeamConfig.getUserDetailBatchQueryUrl();
        ResponsePacket resp = restTemplateExtrnal.postForObject(batchQueryUrl, httpEntity,
                ResponsePacket.class);
        if (!resp.responseSuccess()) {
            throw new BusinessException(resp.getCode(), resp.getMsg());
        }
        String userListStr = JacksonUtils.toJsonWithSnake(resp.getData());
        Collection<UserSessionContext> userList = JacksonUtils
                .toBeanCollectionWithSnake(userListStr, List.class, UserSessionContext.class);
        if (ObjectTools.isEmpty(userList)) {
            logger.error(">> Python 后台获取车队队员的简略信息, 车队[{}]中无队员", sequence);
            throw new BusinessException(BizExceptionEnum.USER_NOT_EXIST);
        }
        for (UserSessionContext user : userList) {
            String userUid = user.getUid();
            for (TeamMemberCompaintAdminVO member : briefInfo) {
                String memberUid = member.getUid();
                if (memberUid.equals(userUid)) {
                    member.setChickenId(user.getChickenId());
                    member.setId(user.getId());
                    String label = new StringBuilder()
                            .append(user.getUsername())
                            .append("/")
                            .append(user.getUid())
                            .append("/")
                            .append(user.getChickenId()).toString();
                    member.setLabel(label);
                }
            }
        }
        return briefInfo;
    }

    @Override
    public List<TeamInfoVO> getBatchTeamInfo(List<String> sequenceList) {
        List<TeamInfoVO> teamInfoVOS = teamRepository.selectTeamInfoBySequence(sequenceList);
        return teamInfoVOS;
    }


    private List<RPGTeamListVO> convertAndSortTeamList(List<Team> results) {
        List<RPGTeamListVO> RPGTeamListVOS = new ArrayList<>();
        for (Team team : results) {
            TeamGameRPG teamGame = team.getTeamGameRPG();
            if (teamGame == null) {
                continue;
            }
            RPGTeamListVO listVo = BeanMapper.map(team, RPGTeamListVO.class);
            listVo.setAssaultName(teamGame.getAssaultName());
            listVo.setZoneAcrossCode(teamGame.getZoneAcrossCode());
            listVo.setZoneAcrossName(teamGame.getZoneAcrossName());
            listVo.setStatusDesc(TeamStatusEnum.convert(team.getStatus()).getMsg());
            listVo.setRaidName(teamGame.getRaidName());
            listVo.setPrice(teamGame.getDiscountFee());
            //从redis中获取一个车队的所有队员
            String teamMemberKey = String.format(RedisKey.TEAM_MEMBER_PREFIX, team.getId());
            Map<String, TeamMemberRPG> stringTeamMemberMap = cacheManager
                    .hgetAll(teamMemberKey, TeamMemberRPG.class);
            //如果车队队员为空则直接跳过，不加入车队
            if (ObjectTools.isEmpty(stringTeamMemberMap)) {
                continue;
            }
            List<TeamMemberRPG> members = new ArrayList<>(stringTeamMemberMap.values());
            //组装成员
            //对成员排序
            sortTeamMember(members);
            members.forEach(m -> {

                listVo.addMembers(BeanMapper.map(m, RPGTeamMemberVO.class));
            });
            //设置当前车队成员数
            listVo.setCurrentPositionCount(listVo.getMembers().size());
            //适配APP，最多显示4个人，降低流量
            if (listVo.getMembers().size() > 4) {
                listVo.setMembers(listVo.getMembers().subList(0, 4));
            }
            RPGTeamListVOS.add(listVo);
        }
        //对所有车队进行排序
        sortTeamList(RPGTeamListVOS);
        return RPGTeamListVOS;
    }

    private void sortTeamList(List<RPGTeamListVO> RPGTeamListVOS) {
        //将teamListVos，按照 座位数越多降序，空位数越少升序排序，但是满员车队往下排
        Collections.sort(RPGTeamListVOS, new Comparator<RPGTeamListVO>() {
            @Override
            public int compare(RPGTeamListVO o1, RPGTeamListVO o2) {
                int idleO2Count = o2.getActuallyPositionCount() - o2.getCurrentPositionCount();
                int idleO1Count = o1.getActuallyPositionCount() - o1.getCurrentPositionCount();
                int i = o2.getActuallyPositionCount() - o1.getActuallyPositionCount();
                //如果o1的位置已满，则比较o2的空闲数，如果o2的空闲数也满，则比较座位数
                if (idleO1Count <= 0) {
                    if (idleO2Count <= 0) {
                        return i > 0 ? i
                                : (i == 0 ? idleO1Count - idleO2Count : -1);
                    } else {
                        return 1;
                    }
                }
                //如果o2的位置已满，则经过上一段比较逻辑，o1一定不会满，可以直接返回负数向后排
                if (idleO2Count <= 0) {
                    return -1;
                }
                //两个对象没有任何一方满员，比较排序
                return i > 0 ? i
                        : (i == 0 ? idleO1Count - idleO2Count : -1);
            }
        });
    }

    private void sortTeamMember(List<TeamMemberRPG> members) {
        members.sort((o1, o2) -> {
            if (o1.getUserIdentity().compareTo(o2.getUserIdentity()) > 0) {
                return -1;
            } else if (o1.getUserIdentity().equals(o2.getUserIdentity())) {
                if (o1.getJoinTime().before(o2.getJoinTime())) {
                    return -1;
                } else {
                    return 1;
                }
            }
            return 1;
        });

    }


    /**
     * 校验车队是否可以加入(要求车队是准备中且车队未满员)
     *
     * @param sequence 车队序列号
     * @return 车队数据
     */
    private RPGRedisTeamVO checkTeamCanJoin(String sequence) {
        // 校验车队是否能加入(车队是否为准备中、是否满员)
        RPGRedisTeamVO rpgRedisTeamVO = rpgTeamCacheManager.queryTeamInfoBySequence(sequence);
        // 校验车队状态
        checkTeamStatus(sequence, TeamMemberActionEnum.JOIN, rpgRedisTeamVO.getStatus());
        // 校验车队是否已满员
        List<RPGRedisTeamMemberVO> memberVOS =
                rpgTeamMemberCacheManager.getAllRPGRedisTeamMemberVO(sequence);
        if (rpgRedisTeamVO.getActuallyPositionCount().equals(memberVOS.size())) {
            logger.error(">> (上车)加入车队, 车队[{}]已满员", sequence);
            throw new BusinessException(BizExceptionEnum.TEAM_MEMBER_IS_FULL);
        }
        return rpgRedisTeamVO;
    }


    /**
     * 创建立即开车本地事务
     *
     * @param RPGRedisTeamVO 车队信息
     * @param leaderUid 队长 uid
     * @param allUidList 所有队员 uid
     */
    private StartTeamLocalTransaction createStartTeamLocalTransaction(RPGRedisTeamVO RPGRedisTeamVO,
            String leaderUid, List<String> allUidList) {
        StartTeamLocalTransaction startTeamLocalTransaction = new StartTeamLocalTransaction();
        String teamSequence = RPGRedisTeamVO.getSequence();
        startTeamLocalTransaction.setTeamSequence(teamSequence);
        startTeamLocalTransaction.setTeamStatus(RPGRedisTeamVO.getStatus());
        // 融云 im
        RcStartTeamEvent rcStartTeamEvent = new RcStartTeamEvent();
        rcStartTeamEvent.setTeamSequence(teamSequence);
        startTeamLocalTransaction.setRcStartTeamEvent(rcStartTeamEvent);
        // 微信服务通知
        WeXinStartTeamEvent weXinStartTeamEvent = new WeXinStartTeamEvent();
        weXinStartTeamEvent.setLeaderUid(leaderUid);
        weXinStartTeamEvent.setGameName(RPGRedisTeamVO.getGameName());
        weXinStartTeamEvent.setRaidName(RPGRedisTeamVO.getRaidName());
        weXinStartTeamEvent.setTeamSequence(teamSequence);
        weXinStartTeamEvent.setZoneAcrossName(RPGRedisTeamVO.getZoneAcrossName());
        // 微信开车通知不需要向队长推送
        allUidList.remove(leaderUid);
        weXinStartTeamEvent.setOtherUidList(allUidList);
        startTeamLocalTransaction.setWeXinStartTeamEvent(weXinStartTeamEvent);
        return startTeamLocalTransaction;
    }


    /**
     * 获取车队队员行为与订单对应的状态
     * 返回为 null 时不需要发送 MQ 消息
     *
     * @param userIdentity 车队队员身份
     * @param memberStatus 车队队员状态
     * @param teamStatus 车队状态
     * @param isKickOut 是否是被踢出
     * @return 离开时的行为枚举
     */
    private TeamOrderRPGActionEnum acquireLeaveTeamOrderAction(Integer userIdentity,
            Integer memberStatus, Integer teamStatus, boolean isKickOut) {
        // 车队已解散和车队已完成时退出视为无效操作, 直接抛出业务异常
        // 具体逻辑在解散车队和车队完成中分别处理
        if (TeamStatusEnum.COMPLETED.getCode() == teamStatus) {
            throw new BusinessException(BizExceptionEnum.TEAM_HAS_COMPLETED);
        } else if (TeamStatusEnum.DISMISSED.getCode() == teamStatus) {
            throw new BusinessException(BizExceptionEnum.TEAM_HAS_DISMISSED);
        } else if (TeamStatusEnum.RUNNING.getCode() == teamStatus) {
            // 车队进行中, 车队进行中无法踢出队员! 此时的离开车队行为都是主动退出
            if (UserIdentityEnum.BOSS.getCode() == userIdentity
                    || UserIdentityEnum.BAOJI.getCode() == userIdentity) {
                if (TeamMemberStatusEnum.JOINED_TEAM.getCode() != memberStatus) {
                    // 车队进行中, 所有队员状态一定都是已入团, 如果不是直接抛出业务异常
                    logger.error(">> 车队进行中退出车队, 有车队队员的状态不是已入团!");
                    throw new BusinessException(BizExceptionEnum.TEAM_RUNNING_MEMBER_STATUS_ERROR);
                }
                // 老板--不退款, 通知订单已取消
                // 暴鸡--订单收益为 0
                return TeamOrderRPGActionEnum.TEAM_STARTED_QUIT;
            }
        } else if (TeamStatusEnum.PREPARING.getCode() == teamStatus) {
            // 车队准备中
            if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                // 老板
                if (TeamMemberStatusEnum.WAIT_FOR_PAY.getCode() == memberStatus) {
                    // 待支付--直接从 redis 中删除
                    // 由于这里存在超时未支付被踢出后再去支付的情况, 这里还是需要返回
                    /* 2018-10-25 退款流程优化, 这里返回 null即可 */
                    /* return TeamOrderActionEnum.PAY_TIME_OUT; */
                    return null;
                } else if (TeamMemberStatusEnum.PREPARE_JOIN_TEAM.getCode() == memberStatus) {
                    // 已支付, 待入团
                    if (isKickOut) {
                        // 被踢出
                        return TeamOrderRPGActionEnum.PREPARE_JOIN_TEAM_KICK;
                    } else {
                        // 主动退出
                        return TeamOrderRPGActionEnum.PREPARE_JOIN_TEAM_QUIT;
                    }
                } else if (TeamMemberStatusEnum.JOINED_TEAM.getCode() == memberStatus) {
                    // 已入团
                    if (isKickOut) {
                        // 被踢出
                        return TeamOrderRPGActionEnum.JOINED_TEAM_KICK;
                    } else {
                        // 主动退出
                        return TeamOrderRPGActionEnum.JOINED_TEAM_QUIT;
                    }
                }
            } else if (UserIdentityEnum.BAOJI.getCode() == userIdentity) {
                // 暴鸡--直接从 redis 中删除
                return null;
            }
        }
        return null;
    }

    /**
     * 结束车队时, 发送修改订单状态消息到 mq 场景: 解散车队、正常结束车队
     *
     * @param teamSequence 车队序列号
     * @param teamStatus 车队状态
     * @param gameResult 车队比赛结果
     * @param orderTeamRPGMemberList 修改订单状态所需参数
     * @param endTeamLocalTransaction 结束车队本地事务
     */
    private void pushEndTeamUpdateOrderMQ(String teamSequence, Integer teamStatus,
            Integer gameResult, List<OrderTeamRPGMember> orderTeamRPGMemberList,
            EndTeamLocalTransaction endTeamLocalTransaction) {
        // 构建 mq 消息
        // 封装订单服务所需参数
        UpdateOrderStatusRPGParams updateOrderStatusRPGParams = new UpdateOrderStatusRPGParams();
        updateOrderStatusRPGParams.setTeamSequence(teamSequence);
        updateOrderStatusRPGParams.setTeamStatus(teamStatus);
        updateOrderStatusRPGParams.setGameResult(gameResult);
        updateOrderStatusRPGParams.setTeamMembers(orderTeamRPGMemberList);
        // 结束车队消息
        RPGUpdateOrderStatusMessage updateOrderStatusRPGMessage = new RPGUpdateOrderStatusMessage();
        updateOrderStatusRPGMessage.setUpdateOrderStatusRPGParams(updateOrderStatusRPGParams);
        // 发送结束车队事务消息
        sendTransactionMessage(teamSequence,
                RocketMQConstant.UPDATE_ORDER_STATUS_TAG, updateOrderStatusRPGMessage,
                endTeamLocalTransaction, endTeamTransactionProducer);
    }

    /**
     * 创建结束车队(解散车队、正常结束车队)本地事务
     * 包括: 车队数据(车队队员数据)入库、异步推送融云和微信通知
     *
     * @return 结束车队本地事务
     */
    private EndTeamLocalTransaction createEndTeamLocalTransaction(Long teamId,
            String teamSequence, Integer teamStatus, Integer gameResult, String teamTitle,
            List<RPGRedisTeamMemberVO> persistenceMemberList, String leaderUid,
            List<String> rcMemberUidList, WeXinOrderCancelEvent weXinOrderCancelEvent,
            WeXinOrderEndEvent weXinOrderEndEvent, boolean dismissTeam) {
        EndTeamLocalTransaction endTeamLocalTransaction = new EndTeamLocalTransaction();
        endTeamLocalTransaction.setTeamId(teamId);
        endTeamLocalTransaction.setTeamSequence(teamSequence);
        endTeamLocalTransaction.setTeamStatus(teamStatus);
        endTeamLocalTransaction.setGameResult(gameResult);
        endTeamLocalTransaction.setPersistenceMemberList(persistenceMemberList);
        // 融云
        RcEndTeamEvent rcEndTeamEvent = new RcEndTeamEvent();
        rcEndTeamEvent.setDismissTeam(dismissTeam);
        rcEndTeamEvent.setTeamSequence(teamSequence);
        rcEndTeamEvent.setTeamTitle(teamTitle);
        rcEndTeamEvent.setLeaderUid(leaderUid);
        if (ObjectTools.isNotEmpty(rcMemberUidList)) {
            // 不向队长自己推送融云解散消息
            rcMemberUidList.remove(leaderUid);
        }
        rcEndTeamEvent.setOtherUidList(rcMemberUidList);
        endTeamLocalTransaction.setRcEndTeamEvent(rcEndTeamEvent);
        // 微信
        if (ObjectTools.isNotNull(weXinOrderCancelEvent)) {
            // 订单取消服务通知
            endTeamLocalTransaction.setWeXinOrderCancelEvent(weXinOrderCancelEvent);
        }
        if (ObjectTools.isNotNull(weXinOrderEndEvent)) {
            // 订单完成服务通知
            endTeamLocalTransaction.setWeXinOrderEndEvent(weXinOrderEndEvent);
        }
        return endTeamLocalTransaction;
    }


    /**
     * 将车队游戏结果存入 redis 中 场景: 解散车队、结束车队
     *
     * @param sequence 车队序列号
     * @param sequence 车队标题
     * @param gameResult 车队游戏结果
     */
    private void saveTeamGameResultToRedis(String sequence, String teamTitle, Integer gameResult) {
        String teamGameResultKey = String.format(RedisKey.TEAM_GAME_RESULT_PREFIX, sequence);
        TeamGameResultVO teamGameResultVO = new TeamGameResultVO();
        GameResultEnum gameResultEnum = GameResultEnum
                .fromCode(gameResult.intValue());
        if (ObjectTools.isNotEmpty(gameResultEnum)) {
            teamGameResultVO.setGameResultDesc(gameResultEnum.getDesc());
            teamGameResultVO.setTeamSequence(sequence);
            teamGameResultVO.setTeamTitle(teamTitle);
            teamGameResultVO.setGameResultCode(gameResult);
            // 保存两小时...
            cacheManager.set(teamGameResultKey,
                    JsonsUtils.toJson(teamGameResultVO), 2 * CommonConstants.ONE_HOUR_SECONDS);
            logger.info(">> 解散(或正常结束)车队[{}], 保存车队比赛结果[{}]到 redis",
                    sequence, gameResult);
        } else {
            logger.error(">> 解散(或正常结束)车队[{}], 保存车队比赛结果[{}]到 redis 错误",
                    sequence, gameResult);
            throw new BusinessException(BizExceptionEnum.TEAM_NOT_EXIST);
        }
    }

    /**
     * 调用交易服务计算暴鸡预期收益
     *
     * @param sequence 车队序列号
     * @param gameCode 游戏 code
     * @param raidCode 副本 code
     * @param price 车队价格
     * @param baojiMember 当前暴鸡队员
     * @param redisTeamMemberBaseVOS 车队成员列表
     */
    private PVPPreIncomeVo calculateProfit(String sequence, Integer gameCode,
            Integer raidCode, Integer price, RPGRedisTeamMemberBaseVO baojiMember,
            List<RPGRedisTeamMemberBaseVO> redisTeamMemberBaseVOS) {
        String uid = baojiMember.getUid();
        RPGInComeParams inComeParams = new RPGInComeParams();
        inComeParams.setGameCode(gameCode);
        inComeParams.setRaidCode(raidCode);
        inComeParams.setTeamPrice(price);
        inComeParams.setTeamSequence(sequence);
        inComeParams.setUid(uid);
        inComeParams.setUserIdentity(baojiMember.getUserIdentity());
        List<OrderTeamRPGMember> orderTeamRPGMemberList = new ArrayList<>();
        for (RPGRedisTeamMemberBaseVO redisTeamMemberBaseVO : redisTeamMemberBaseVOS) {
            OrderTeamRPGMember orderTeamRPGMember = new OrderTeamRPGMember();
            orderTeamRPGMember.setUserIdentity(redisTeamMemberBaseVO.getUserIdentity());
            orderTeamRPGMember.setTeamMemberStatus(redisTeamMemberBaseVO.getStatus());
            orderTeamRPGMember.setTeamMemberUID(redisTeamMemberBaseVO.getUid());
            orderTeamRPGMember.setTeamMemberName(redisTeamMemberBaseVO.getUsername());
            orderTeamRPGMember.setBaojiLevel(redisTeamMemberBaseVO.getBaojiLevel());
            orderTeamRPGMember.setRaidLocation(redisTeamMemberBaseVO.getRaidLocationCode());
            orderTeamRPGMemberList.add(orderTeamRPGMember);
        }
        // 按车队队员身份降序排, 这里队长排第一位
        orderTeamRPGMemberList.sort((o1, o2) -> o2.getUserIdentity() - o1.getUserIdentity());
        inComeParams.setTeamMembers(orderTeamRPGMemberList);
        logger.info(">> 用户[{}]获取车队[{}]实时数据, 开始调用交易服务计算暴鸡预期收益", uid, sequence);
        long start = System.currentTimeMillis();
        ResponsePacket<PVPPreIncomeVo> profitResp = rpgOrdersServiceClient
                .getPreProfit(inComeParams);
        logger.info(">> 用户[{}]获取车队[{}]实时数据, 调用交易服务计算暴鸡预期收益耗时: {} ms",
                uid, sequence, (System.currentTimeMillis() - start));
        if (profitResp.responseSuccess()) {
            return profitResp.getData();
        }
        logger.error(">> 用户[{}]获取车队[{}]实时数据, 调用交易服务计算暴鸡预期收益返回错误: {}",
                uid, sequence, profitResp);
        // 这里如果调用失败, 前端会捕捉到异常
        // 由于这个接口调用频率很高, 在调用失败时预期收益就不返回了
        PVPPreIncomeVo preInComeVo = new PVPPreIncomeVo();
        // 调用失败暴鸡预期收益返回-1, 前端显示收益计算中...
        preInComeVo.setPrice(BAOJI_PRE_PROFIT_FAIL);
        preInComeVo.setTeamMembersIncomes(new ArrayList<>());
        return preInComeVo;
    }

    /**
     * 创建修改队员订单状态所需参数
     *
     * @param uid 队员 uid
     * @param username 队员昵称
     * @param userIdentity 队员身份
     * @param teamOrderRPGActionEnum 离开行为枚举
     * @param baojiLevel 暴鸡等级
     * @param raidLocationCode 副本位置
     */
    private OrderTeamRPGMember buildOrderTeamRPGMembers(String uid, String username,
            Integer userIdentity, TeamOrderRPGActionEnum teamOrderRPGActionEnum,
            Integer baojiLevel, Integer raidLocationCode) {
        OrderTeamRPGMember orderTeamRPGMember = new OrderTeamRPGMember();
        orderTeamRPGMember.setTeamMemberUID(uid);
        orderTeamRPGMember.setTeamMemberName(username);
        orderTeamRPGMember.setTeamMemberStatus(teamOrderRPGActionEnum.getCode());
        orderTeamRPGMember.setUserIdentity(userIdentity);
        orderTeamRPGMember.setBaojiLevel(baojiLevel);
        orderTeamRPGMember.setRaidLocation(raidLocationCode);
        return orderTeamRPGMember;
    }

    /**
     * 构建修改订单状态消息
     * @param teamSequence 车队序列号
     * @param teamStatus 车队状态
     * @param gameResult 比赛结果
     * @param orderTeamRPGMemberList rpg队员订单参数
     * @return
     */
    private RPGUpdateOrderStatusMessage buildUpdateOrderStatusRPGMessage(
            String teamSequence, Integer teamStatus,
            Integer gameResult, List<OrderTeamRPGMember> orderTeamRPGMemberList) {
        // 封装订单服务所需参数
        UpdateOrderStatusRPGParams updateOrderStatusRPGParams = new UpdateOrderStatusRPGParams();
        updateOrderStatusRPGParams.setTeamSequence(teamSequence);
        updateOrderStatusRPGParams.setTeamStatus(teamStatus);
        updateOrderStatusRPGParams.setGameResult(gameResult);
        updateOrderStatusRPGParams.setTeamMembers(orderTeamRPGMemberList);
        // 修改订单状态消息
        RPGUpdateOrderStatusMessage updateOrderStatusRPGMessage = new RPGUpdateOrderStatusMessage();
        updateOrderStatusRPGMessage.setUpdateOrderStatusRPGParams(updateOrderStatusRPGParams);
        return updateOrderStatusRPGMessage;
    }

    /**
     * 获取 redis 中的副本信息, 不从本地预热的缓存中获取
     * @param gameCode 游戏 code
     * @param raidCode 副本 code
     * @return
     */
    private RedisGameRaid getRedisSingleGameRaid(Integer gameCode, Integer raidCode) {
        String result = cacheManager
                .hget(String.format(RedisKey.GAME_RAID_KEY, gameCode), raidCode.toString(),
                        String.class);
        RedisGameRaid redisGameRaid = null;
        if (ObjectTools.isNotEmpty(result)) {
            redisGameRaid = JacksonUtils
                    .toBeanWithSnake(result, RedisGameRaid.class);
            if (redisGameRaid.getRaidAssistRate() != null) {
                redisGameRaid
                        .setRaidAssistRate(redisGameRaid.getRaidAssistRate().setScale(2,
                                BigDecimal.ROUND_HALF_UP));
            }
            if (redisGameRaid.getRaidDpsRate() != null) {
                redisGameRaid.setRaidDpsRate(redisGameRaid.getRaidDpsRate().setScale(2,
                        BigDecimal.ROUND_HALF_UP));
            }
        }
        return redisGameRaid;
    }


    /**
     * 根据uid查找用户（暴鸡）所有已完成的车队唯一Id
     */
    @Override
    public List<TeamSequenceUidVO> getBaojiTeamSequencesByUids(List<String> uids) {
        return teamMemberRPGRepository.selectBaojiTeamSequencesByUids(uids);
    }

    @Override
    public void bossConfirmPaidSuccess(BossConfirmPaidSuccessParams bossConfirmPaidSuccessParams) {
        String bossUid = UserSessionContext.getUser().getUid();
        logger.info(">> RPG老板确认支付成功,参数:{}", bossConfirmPaidSuccessParams);
        BossConfirmPaidSuccessEvent bossConfirmPaidSuccessEvent = BossConfirmPaidSuccessEvent
                .builder()
                .bossUid(bossUid)
                .sequence(bossConfirmPaidSuccessParams.getTeamSequence())
                .orderSequence(bossConfirmPaidSuccessParams.getOrderSequence())
                .isRPG(true)
                .build();
        EventBus.post(bossConfirmPaidSuccessEvent);
    }

    public static void main(String[] args) {
        System.out.println(RedisUtils.buildHashSegmentKey("rfelje3g",
                RedisKey.TEAM_USER_GAME_PREFIX, ObjectTools.covertToString(88)));
    }
}
