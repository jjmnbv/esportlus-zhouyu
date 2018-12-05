package com.kaihei.esportingplus.gamingteam.domain.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.config.RonyunUserIdGenerator;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.data.JsonSerializable;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.common.enums.TeamCategoryEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.lock.PreventRepeatOperation;
import com.kaihei.esportingplus.common.lock.RedisLock;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.sensors.enums.SensorsEventEnum;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberDisplayStatus.PVPFreeMemberBaoji;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberDisplayStatus.PVPFreeMemberBoss;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberDisplayStatus.PVPFreeMemberLeader;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.params.PVPFreeTeamsForBackupParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamQueryParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.vo.BossInfoForOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.GameTeamTotal;
import com.kaihei.esportingplus.gamingteam.api.vo.PVPFreeTeamBackupVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberCompaintAdminVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceUidVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamCurrentMatchingInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamGameVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamMemberInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPTeamStartOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamListVO;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamMemberCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.MQTransaction;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.MockUser;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamBossMatchingVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.EndTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.LeaveTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.matching.PVPFreeTeamMatchingPool;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamGameResultRepository;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVPFree;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVPFree;
import com.kaihei.esportingplus.marketing.api.feign.UserFreeCouponsServiceClient;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsInfoVo;
import com.kaihei.esportingplus.trade.api.feign.PVPFreeOrdersServiceClient;
import com.kaihei.esportingplus.trade.api.params.ChickenPointIncomeParams;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBaseVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreePreIncomeVo;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PVPTeamFreeServiceImpl extends
        AbstractTeamService<TeamGamePVPFree, TeamMemberPVPFree> implements PVPTeamFreeService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected final Logger log = LOGGER;

    private static CopyOnWriteArrayList<DictBaseVO> gameList = new CopyOnWriteArrayList();

    @Autowired
    private PVPContextHolder<TeamGamePVPFree, TeamMemberPVPFree> pvpContextHolder;

    @Autowired
    private UserFreeCouponsServiceClient userFreeCouponsServiceClient;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PVPFreeTeamMatchingPool pvpFreeTeamMatchingPoool;

    @Autowired
    private TeamGameResultRepository teamGameResultRepository;

    @Autowired
    private PVPFreeOrdersServiceClient pvpFreeOrdersServiceClient;

    @Autowired
    private DictionaryClient dictionaryClient;

    @Autowired
    private PVPTeamCacheManager pvpTeamCacheManager;

    @Autowired
    private PVPTeamMemberCacheManager pvpTeamMemberCacheManager;

    @Autowired
    private RonyunUserIdGenerator ronyunUserIdGenerator;

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    /**
     * 立即开车
     */
    @Override
    @TeamOperation(scene = StartTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "$.sequence")
    @MQTransaction(topic = RocketMQConstant.TOPIC_PVP_FREE, tag = RocketMQConstant.CREATE_BAOJI_ORDER_TAG)
    public void startTeam(String sequence) {
        super.startTeam(sequence);
    }


    /**
     * 退出车队
     */
    @Override
    @TeamOperation(scene = LeaveTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "$.sequence")
    @MQTransaction(topic = RocketMQConstant.TOPIC_PVP_FREE, tag = RocketMQConstant.QUIT_TEAM_AFTER_RUNNING)
    public void quitTeam(String sequence) {
        super.quitTeam(sequence);
    }

    /**
     * 结束车队
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TeamOperation(end = true, sequencePath = "pvpTeamEndParams.teamSequence", scene = EndTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "$.pvpTeamEndParams.teamSequence")
    @MQTransaction(topic = RocketMQConstant.TOPIC_PVP_FREE, tag = RocketMQConstant.UPDATE_ORDER_STATUS_TAG)
    public void endTeam(PVPTeamEndParams pvpTeamEndParams) {
        super.endTeam(pvpTeamEndParams);
    }


    /**
     * 加入车队
     */
    @Override
    @RedisLock(keyFormate = TEAM_LOCK_PRE
            + "$.jsonSerializable.teamSequence", uid = "jsonSerializable.uid")
    @TeamOperation(sequencePath = "jsonSerializable.teamSequence", scene = JoinTeamScene.class)
    @MockUser(username = "jsonSerializable.username", avatar = "jsonSerializable.avatar", uid = "jsonSerializable.uid", chickenId = "jsonSerializable.chickenId")
    public void joinTeam(JsonSerializable jsonSerializable) {
        super.joinTeam(jsonSerializable);
    }

    @Override
    @PreventRepeatOperation(expireTime = 5)
    public String matchingTeam(PVPFreeTeamBossMatchingVO matchingVO) {
        // 当前用户
        String uid = matchingVO.getUid();
        LOGGER.debug("用户[{}]点击了匹配，全员第一战斗准备。入参:{}",uid,matchingVO.toString());

        //校验用户可用的免单次数
        ResponsePacket<UserFreeCouponsInfoVo> userFreeCouponsInfo = userFreeCouponsServiceClient
                .getUserFreeCouponsInfo(uid);
        if(userFreeCouponsInfo.getCode() != BizExceptionEnum.SUCCESS.getErrCode()
                || userFreeCouponsInfo.getData() == null
                || userFreeCouponsInfo.getData().getAvailableCount() <= 0){
            LOGGER.error("用户[{}]免单次数已经用光了，老鬼你还上什么车。",uid);

            throw new BusinessException(BizExceptionEnum.MATCHING_FREECOUPONS_NONE);
        }else{
            LOGGER.error("用户[{}]剩余免单次数:{}",uid,userFreeCouponsInfo.getData().getAvailableCount());
        }

        //校验用户点击匹配的时候，是否加入了其他车队,返回空说明可以加入
        //TODO 此接口需要全平台校验，而不是只校验某个游戏
        String teamSequence = pvpTeamMemberCacheManager
                .findUserTeamSequence(uid, matchingVO.getGameId());
        if (StringUtils.isNoneBlank(teamSequence)) {
            LOGGER.error(BizExceptionEnum.USER_IN_OTHER_TEAM.getErrMsg() + ",车队序列号:{}",
                    teamSequence);
            return teamSequence;
        }

        //校验是否已经有匹配记录
        Boolean existsMatch = cacheManager
                .exists(RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_HISTORY + uid);
        if (existsMatch) {
            LOGGER.error(BizExceptionEnum.MATCHING_EXISTS.getErrMsg() + ",uid:{}", uid);
            return StringUtils.EMPTY;
        }

        //由于使用队列，无法获取指定用户的已过去的匹配时间和选择匹配的入参等，单独缓存
        //先放记录到redis
        cacheManager.set(RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_HISTORY + uid,
                FastJsonUtils.toJson(matchingVO));

        //再放到本地内存，防止本地内存比redis放入的快，导致消息消费的时候找不到记录，把消息丢弃
        pvpFreeTeamMatchingPoool.pushMatchingPool(matchingVO);

        //上报神策 TODO 公共化
        HashMap<String,Object> data = new HashMap<>(5);
        data.put("game_type",matchingVO.getGameId());//游戏类型
        data.put("unit",matchingVO.getSettlementName());//单位
        data.put("game_zone",matchingVO.getGameZoneId());//游戏大区
        data.put("game_dan",matchingVO.getGameDanId());//游戏段位
        data.put("identity",matchingVO.getUserIdentity());//用户身份
        sensorsAnalyticsService.track(uid, SensorsEventEnum.START_MATCHING_FREETEAM.getCode(),data);
        LOGGER.debug("上报神策成功.入参:{}",data);
        return StringUtils.EMPTY;
    }

    /**
     *@Description: 用户取消匹配操作，删除【匹配记录】
     * 由于【匹配池】里还有数据在轮转，
     * 消费匹配池消息时，校验如果【匹配记录】无数据，证明用户已经取消过匹配，
     * 直接丢掉，不放回匹配池
     *@param: [vo]
     *@return: void
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/16 11:01
     */
    @Override
    public void cancelMatching() {
        String uid = UserSessionContext.getUser().getUid();
        // 查找匹配记录
        PVPFreeTeamBossMatchingVO bossMatchingHisroty = cacheManager.get(
                RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_HISTORY + uid,
                PVPFreeTeamBossMatchingVO.class);

//        //上报神策
//        HashMap<String,Object> data = new HashMap<>(7);
//        long seconds = DateUtil
//                .secondsBetween(bossMatchingHisroty.getStartMatchingTime(), LocalDateTime.now());
//
//        data.put("reason","用户匹配中取消");//结束原因
//        data.put("seconds",seconds);//匹配用时
//        data.put("game_type",bossMatchingHisroty.getGameId());//游戏类型
//        data.put("unit",bossMatchingHisroty.getSettlementName());//单位
//        data.put("game_zone",bossMatchingHisroty.getGameZoneId());//游戏大区
//        data.put("game_dan",bossMatchingHisroty.getGameDanId());//游戏段位
//        data.put("identity",bossMatchingHisroty.getUserIdentity());//用户身份
//        sensorsAnalyticsService.track(uid, SensorsEventEnum.END_MATCHING_FREETEAM.getCode(),data);

        //删除记录
        cacheManager.del(RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_HISTORY + uid);
        logger.info(">> 用户[{}]取消了免费车队的匹配", uid);
    }

    /**
     * 查询车队列表
     */
    @Override
    public PagingResponse<RPGTeamListVO> findTeamList(Integer gameCode,
            TeamQueryParams teamQueryParams) {
        return null;
    }

    /**
     * 查询符合条件的车队数
     */
    @Override
    public GameTeamTotal findTeamTotal(Integer gameCode) {
        return null;
    }

    /**
     * 获取车队基本信息
     */
    @Override
    @TeamOperation
    public PVPFreeTeamBaseVO getTeamBaseInfo(String sequence) {
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> pvpContext = pvpContextHolder.getContext();
        Team team = pvpContext.getTeam();

        /*Team team = Team.builder()
                .actuallyPositionCount(5)
                .gmtCreate(new Date())
                .gmtModified(new Date())
                .sequence("GT1234567890111111")
                .roomNum(123321)
                .settlementNumber(new BigDecimal(1))
                .playMode((byte) PlayModeEnum.ACCOMPANY_PLAY.getCode())
                .settlementType((byte) SettlementTypeEnum.ROUND.getCode())
                .originalPositionCount(10)
                .status((byte) TeamStatusEnum.PREPARING.getCode())
                .teamType((byte) ProductBizTypeEnum.GAMING_TEAM.getCode())
                .build();
        TeamGamePVPFree gamePVPFree = TeamGamePVPFree.builder()
                .freeTeamTypeId(1)
                .freeTeamTypeName("王者荣耀上分一局")
                .gameId(1)
                .gameZoneId(11)
                .gameZoneName("微信区")
                .lowerDanId(30)
                .lowerDanName("倔强青铜")
                .upperDanId(45)
                .upperDanName("最强王者")
                .build();*/

        // 车队基本信息
        PVPFreeTeamBaseVO pvpFreeTeamBaseVO =
                BeanMapper.map(team, PVPFreeTeamBaseVO.class);
        PVPFreeTeamGameVO pvpFreeTeamGameVO = getPVPFreeTeamGame();


        /*if (SettlementTypeEnum.ROUND.getCode() == team.getSettlementType()) {
            // 局
            pvpFreeTeamBaseVO.setRoundNum(team.getSettlementNumber().intValue());
        } else if (SettlementTypeEnum.HOUR.getCode() == team.getSettlementType()){
            // 小时
            pvpFreeTeamBaseVO.setHourNum(team.getSettlementNumber().doubleValue());
        }*/

        pvpFreeTeamBaseVO.setGameInfo(pvpFreeTeamGameVO);
        return pvpFreeTeamBaseVO;
    }

    private PVPFreeTeamGameVO getPVPFreeTeamGame() {
        TeamGamePVPFree gamePVPFree = pvpContextHolder.getContext().getTeamGame();
        // 游戏基本信息
        PVPFreeTeamGameVO pvpFreeTeamGameVO
                = BeanMapper.map(gamePVPFree, PVPFreeTeamGameVO.class);
        pvpFreeTeamGameVO.setFreeTeamTypeName(gamePVPFree.getFreeTeamTypeName());
        return pvpFreeTeamGameVO;
    }

    /**
     * 获取车队详细信息(包含车队队员信息)
     */
    @Override
    @TeamOperation
    public PVPFreeTeamCurrentInfoVO getTeamCurrentInfo(String sequence) {
        // 当前用户
        String uid = pvpContextHolder.getUser().getUid();
        PVPContext<TeamGamePVPFree, TeamMemberPVPFree> pvpContext = pvpContextHolder.getContext();

        Team team = pvpContext.getTeam();
        TeamGamePVPFree gamePVPFree = pvpContext.getTeamGame();

        // 车队游戏信息
        String teamGameInfo = getTeamGameInfoStr(team, gamePVPFree);

        // 车队队员
        List<TeamMemberPVPFree> teamMemberList = pvpContext.getTeamMemberList();

        // 当前队员
        TeamMemberPVPFree currentMember = teamMemberList.stream()
                .filter(m -> m.getUid().equals(uid))
                .findAny()
                .orElseThrow(
                        () -> new BusinessException(BizExceptionEnum.USER_NOT_IN_CURRENT_TEAM));

        // 过滤暴鸡 uid
        // TODO 调用用户服务查询暴鸡开车数量 这个版本先不做 2018/11/17

        List<PVPFreeBaseVO> pvpFreeBossVOS = teamMemberList.stream()
                .filter(f->f.getUserIdentity() == UserIdentityEnum.BOSS.getCode())
                .map(m->{
                        PVPFreeBaseVO baseVO = new PVPFreeBaseVO();
                        baseVO.setUid(m.getUid());
                        baseVO.setGameDanId(m.getGameDanId());
                        return baseVO;
                })
                .collect(Collectors.toList());

        int profit = 0;
        Byte userIdentity = currentMember.getUserIdentity();
        if ((UserIdentityEnum.LEADER.getCode() == userIdentity
                || UserIdentityEnum.BAOJI.getCode() == userIdentity
                || UserIdentityEnum.BN.getCode() == userIdentity)
                && CollectionUtils.isNotEmpty(pvpFreeBossVOS)) {
            // 暴鸡队员计算预计收益
            ChickenPointIncomeParams incomeParams = ChickenPointIncomeParams.builder()
                    .freeTeamTypeId(gamePVPFree.getFreeTeamTypeId())
                    .baojiLevel(currentMember.getBaojiLevel())
                    .gameResultCode(GameResultEnum.findPositiveGameResult(
                            SettlementTypeEnum.getByCode(team.getSettlementType()
                                    .intValue())).getCode())
                    .pvpFreeBossVOS(pvpFreeBossVOS)
                    .build();

            ResponsePacket<PVPFreePreIncomeVo> chickenPointIncomeResp = pvpFreeOrdersServiceClient
                    .getChickenPointIncome(incomeParams);
            if (!chickenPointIncomeResp.responseSuccess()) {
                // 计算错误, 返回-1, 前端显示收益计算中
                logger.error(">> 用户[{}]获取免费车队[{}]实时数据,计算暴鸡预期鸡分收益错误!返回结果: {}",
                        uid, sequence, chickenPointIncomeResp);
                profit = -1;
            } else {
                profit = chickenPointIncomeResp.getData().getTotalIncome();
            }
        }

        List<PVPFreeTeamMemberVO> teamMemberBaseVOList = getPVPFreeTeamMemberList();
        // 外显状态计算
        calculateMemberDisplayStatus(team.getStatus().intValue(),
                team.getActuallyPositionCount(), teamMemberBaseVOList);

        PVPFreeTeamCurrentInfoVO currentInfoVO = PVPFreeTeamCurrentInfoVO.builder()
                .teamCategoryName(TeamCategoryEnum.FREE.getDesc())
                .sequence(sequence)
                .teamGameInfo(teamGameInfo)
                .teamTitle(team.getTitle())
                .roomNum(team.getRoomNum())
                .playMode(team.getPlayMode().intValue())
                .teamGameInfo(teamGameInfo)
                .memberSize(teamMemberList.size())
                .originalPositionCount(team.getOriginalPositionCount())
                .teamStatus(team.getStatus().intValue())
                .userIdentity(currentMember.getUserIdentity().intValue())
                .profit(profit)
                .uid(uid)
                .teamMemberList(teamMemberBaseVOList)
                .build();
        logger.info(">> 用户[{}]获取免费车队[{}]的实时数据.", uid, sequence);
        return currentInfoVO;
    }

    private void calculateMemberDisplayStatus(Integer teamStatus, Integer teamActuallyPositionCount,
            List<PVPFreeTeamMemberVO> teamMemberBaseVOList) {

        // 全员是否已准备
        boolean allIsReady = true;
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.of(teamStatus);
        for (PVPFreeTeamMemberVO memberBaseVO : teamMemberBaseVOList) {

            UserIdentityEnum identityEnum = UserIdentityEnum.of(memberBaseVO.getUserIdentity());

            PVPTeamMemberStatusEnum memberStatusEnum =
                    PVPTeamMemberStatusEnum.of(memberBaseVO.getStatus());

            if (TeamStatusEnum.PREPARING.equals(teamStatusEnum)) {
                // 车队准备中
                if (!PVPTeamMemberStatusEnum.PREPARE_READY.equals(memberStatusEnum)) {
                    // 有队员状态不是已准备, 就不能开车
                    allIsReady = false;
                }
                setMemberDisplayStatusWhenTeamIsPreparing(memberBaseVO, identityEnum,
                        memberStatusEnum);
            } else if (TeamStatusEnum.RUNNING.equals(teamStatusEnum)) {
                // 车队进行中
                setMemberDisplayStatusWhenTeamIsRunning(memberBaseVO, identityEnum,
                        memberStatusEnum);
            }
        }

        // 车队准备中, 全员已准备, 已满员
        if (TeamStatusEnum.PREPARING.equals(teamStatusEnum)
                && teamMemberBaseVOList.size() == teamActuallyPositionCount
                && allIsReady) {
            // 设置队长的状态为立即开车
            teamMemberBaseVOList.stream()
                    .filter(m -> m.getUserIdentity() == UserIdentityEnum.LEADER.getCode())
                    .findAny()
                    .map(m -> {
                        m.setStatus(PVPFreeMemberLeader.START_TEAM_NOW);
                        return m;
                    });
        }
    }

    private void setMemberDisplayStatusWhenTeamIsRunning(PVPFreeTeamMemberVO memberBaseVO,
            UserIdentityEnum identityEnum, PVPTeamMemberStatusEnum memberStatusEnum) {
        switch (identityEnum) {
            case LEADER:
                memberBaseVO.setStatus(PVPFreeMemberLeader.SERVICE_IN_PROGRESS);
                break;
            case BOSS:
                if (PVPTeamMemberStatusEnum.PREPARE_READY.equals(memberStatusEnum)) {
                    memberBaseVO.setStatus(PVPFreeMemberBoss.SERVICE_IN_PROGRESS);
                } else if (PVPTeamMemberStatusEnum.TEAM_START_QUIT.equals(memberStatusEnum)) {
                    memberBaseVO.setStatus(PVPFreeMemberBoss.TEAM_STARTED_QUIT);
                }
                break;
            case BAOJI:
            case BN:
                if (PVPTeamMemberStatusEnum.PREPARE_READY.equals(memberStatusEnum)) {
                    memberBaseVO.setStatus(PVPFreeMemberBaoji.SERVICE_IN_PROGRESS);
                } else if (PVPTeamMemberStatusEnum.TEAM_START_QUIT.equals(memberStatusEnum)) {
                    memberBaseVO.setStatus(PVPFreeMemberBaoji.TEAM_STARTED_QUIT);
                }
                break;
            default:
                break;
        }
    }

    private void setMemberDisplayStatusWhenTeamIsPreparing(PVPFreeTeamMemberVO memberBaseVO,
            UserIdentityEnum identityEnum, PVPTeamMemberStatusEnum memberStatusEnum) {
        switch (identityEnum) {
            case LEADER:
                memberBaseVO.setStatus(PVPFreeMemberLeader.PREPARED);
                break;
            case BOSS:
                if (PVPTeamMemberStatusEnum.WAIT_READY.equals(memberStatusEnum)) {
                    memberBaseVO.setStatus(PVPFreeMemberBoss.WAITING_PREPARE);
                } else if (PVPTeamMemberStatusEnum.PREPARE_READY.equals(memberStatusEnum)) {
                    memberBaseVO.setStatus(PVPFreeMemberBoss.PREPARED);
                }
                break;
            case BAOJI:
            case BN:
                if (PVPTeamMemberStatusEnum.WAIT_READY.equals(memberStatusEnum)) {
                    memberBaseVO.setStatus(PVPFreeMemberBaoji.WAITING_PREPARE);
                } else if (PVPTeamMemberStatusEnum.PREPARE_READY.equals(memberStatusEnum)) {
                    memberBaseVO.setStatus(PVPFreeMemberBaoji.PREPARED);
                }
                break;
            default:
                break;
        }
    }

    private String getTeamGameInfoStr(Team team, TeamGamePVPFree gamePVPFree) {
        String freeTeamTypeName = gamePVPFree.getFreeTeamTypeName();
        String gameZoneName = gamePVPFree.getGameZoneName();
        String lowerDanName = gamePVPFree.getLowerDanName();
        String upperDanName = gamePVPFree.getUpperDanName();
        BigDecimal settlementNumber = team.getSettlementNumber();
        Byte settlementType = team.getSettlementType();
        // 客户端需要后台拼这一堆数据...
        return new StringBuilder()
                .append(freeTeamTypeName).append("·")
                .append(gameZoneName).append("·")
                .append(lowerDanName).append("-")
                .append(upperDanName).append("·")
                .append(settlementNumber)
                .append(SettlementTypeEnum.getByCode(settlementType).getDesc())
                .toString();
    }

    private List<PVPFreeTeamMemberVO> getPVPFreeTeamMemberList() {
        List<TeamMemberPVPFree> teamMemberList = pvpContextHolder.getContext().getTeamMemberList();
        if (ObjectTools.isNotEmpty(teamMemberList)) {
            return teamMemberList.stream()
                    .map(m -> {
                        PVPFreeTeamMemberVO memberVO = BeanMapper
                                .map(m, PVPFreeTeamMemberVO.class);
                        // 计算出融云 uid
                        memberVO.setRcUid(ronyunUserIdGenerator.encodeIMUser(m.getUid()));
                        memberVO.setChickenId(Integer.valueOf(m.getChickenId()));
                        return memberVO;
                    })
                    .sorted(Comparator
                            .comparingInt(PVPFreeTeamMemberVO::getUserIdentity).reversed()
                            .thenComparing(
                                    Comparator.comparingLong(x -> x.getJoinTime().getTime())))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * 提供给老板创建订单时使用 包含了车队的基本信息和老板的基本信息
     *
     * @param sequence 车队序列号
     * @param uid 老板的 uid
     */
    @Override
    public BossInfoForOrderVO getBossInfoForOrder(String sequence, String uid) {
        return null;
    }

    /**
     * 提供给立即开车后批量创建暴鸡订单 包含了车队的基本信息和暴鸡的基本信息
     *
     * @param sequence 车队序列号
     */
    @Override
    public PVPTeamStartOrderVO getBaojiInfoForOrder(String sequence) {
        return null;
    }

    @Override
    public PVPFreeMemberInTeamVO getMemberInTeam() {
        // 这里没办法获取到游戏 id, 只能查询所有游戏挨个去找了...
        String uid = UserSessionContext.getUser().getUid();

        // 查游戏--显然不能每次都去调, 查到了就先丢 JVM里面
        // TODO 很显然这个后期需要优化, 基础配置直接去 redis 中获取
        if (ObjectTools.isEmpty(gameList)) {
            ResponsePacket<List<DictBaseVO<Object>>> gameResp = dictionaryClient
                    .findByCategoryCode(DictionaryCategoryCodeEnum.GAME.getCode(),
                            (byte) StatusEnum.ENABLE.getCode());
            if (!gameResp.responseSuccess()) {
                logger.error(">> 用户[{}]获取是否在车队中的信息,调用资源服务查询游戏数据响应异常:{}",
                        uid, gameResp);
                throw new BusinessException(BizExceptionEnum.GAME_NOT_EXISTS);
            }
            List<DictBaseVO<Object>> gameDictList = gameResp.getData();
            if (ObjectTools.isEmpty(gameDictList)) {
                logger.error(">> 用户[{}]获取是否在车队中的信息,调用资源服务查询游戏数据为空",
                        uid, gameResp);
                throw new BusinessException(BizExceptionEnum.GAME_NOT_EXISTS);
            }
            gameList.addAll(gameDictList);
            logger.debug("gameList -> {}", gameList);
        }

        // 从 redis查车队--TODO 这个加上全局流程判断直接就优化掉了
        String teamSequence = null;
        for (DictBaseVO<Object> dictBaseVO : gameList) {
            String userTeamSequence = pvpTeamMemberCacheManager
                    .findUserTeamSequence(uid, dictBaseVO.getDictId());
            if (ObjectTools.isNotEmpty(userTeamSequence)) {
                teamSequence = userTeamSequence;
                break;
            }
        }
        // 有则返回车队基本数据
        if (ObjectTools.isNotEmpty(teamSequence)) {
            Team team = pvpTeamCacheManager.getTeam(teamSequence);
            List<TeamMemberPVPFree> teamMemberList = pvpTeamMemberCacheManager
                    .getTeamMemberList(teamSequence, TeamMemberPVPFree.class);
            return PVPFreeMemberInTeamVO.builder()
                    .memberInTeam(true)
                    .teamSequence(teamSequence)
                    .teamStatus(team.getStatus().intValue())
                    .actuallyPositionCount(team.getActuallyPositionCount())
                    .teamMemberSize(teamMemberList.size())
                    .build();
        }
        // 没有则返回 false
        return PVPFreeMemberInTeamVO.builder()
                .memberInTeam(false)
                .build();
    }

    /**
     * 获取车队比赛结果
     */
    @Override
    public List<TeamGameResultVO> getTeamGameResult(String sequence) {

        List<TeamGameResultVO> gameResultVOS = teamGameResultRepository
                .selectByTeamSequence(sequence);
        List<TeamGameResultVO> resultList = gameResultVOS.stream().map(r -> {
            r.setGameResultDesc(GameResultEnum.fromCode(r.getGameResultCode()).getDesc());
            return r;
        }).collect(Collectors.toList());
        return resultList;
    }

    /**
     * 获取车队队员的简略信息, 提供给管理后台投诉单查询车队详情时使用
     */
    @Override
    public List<TeamMemberCompaintAdminVO> getTeamMemberBriefInfo(String sequence) {
        return null;
    }

    /**
     * 根据车队序列号批量查询车队信息
     */
    @Override
    public List<TeamInfoVO> getBatchTeamInfo(List<String> sequenceList) {
        return null;
    }

    /**
     * 根据uid查找用户（暴鸡）所有已完成的车队唯一Id
     */
    @Override
    public List<TeamSequenceUidVO> getBaojiTeamSequencesByUids(List<String> uids) {
        return null;
    }


    @Override
    public PVPFreeTeamMemberInfoVO getTeamMemberInfo(String sequence) {

        PVPFreeTeamGameVO pvpFreeTeamGame = getPVPFreeTeamGame();

        List<PVPFreeTeamMemberVO> pvpFreeTeamMemberList = getPVPFreeTeamMemberList();

        return PVPFreeTeamMemberInfoVO.builder()
                .teamInfo(pvpFreeTeamGame)
                .memberInfoList(pvpFreeTeamMemberList)
                .build();
    }

    /**
     * 获取老板支付的倒计时
     */
    @Override
    public Integer getBossPayCountdown(String sequence) {
        return null;
    }

    @Override
    public PVPFreeTeamCurrentMatchingInfoVO getCurrentMatchingInfo() {
        UserSessionContext currentUser = UserSessionContext.getUser();
        String uid = currentUser.getUid();

        PVPFreeTeamBossMatchingVO matchingHistory = cacheManager
                .get(RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_HISTORY + uid,
                        PVPFreeTeamBossMatchingVO.class);

        if (matchingHistory == null) {
            logger.error(">> 未获取到用户[{}]的匹配信息!", uid);
            throw new BusinessException(BizExceptionEnum.TEAM_FREE_MATCHING_HISTORY_NOT_FOUND);
        }

        List<PVPFreeTeamMemberVO> teamMemberList = new ArrayList<>();

        String teamSequence = pvpTeamMemberCacheManager
                .findUserTeamSequence(uid, matchingHistory.getGameId());

        if (ObjectTools.isNotEmpty(teamSequence)) {
            List<TeamMemberPVPFree> teamMemberPVPFrees = pvpTeamMemberCacheManager
                    .getTeamMemberList(teamSequence, TeamMemberPVPFree.class);

            if (ObjectTools.isNotEmpty(teamMemberPVPFrees)) {
                teamMemberList = teamMemberPVPFrees.stream()
                        .map(tf -> BeanMapper.map(tf, PVPFreeTeamMemberVO.class))
                        .sorted(Comparator
                                .comparingInt(PVPFreeTeamMemberVO::getUserIdentity).reversed()
                                .thenComparingLong(tb -> tb.getJoinTime().getTime()))
                        .collect(Collectors.toList());
            }
        }

        LocalDateTime startMatchingTime = matchingHistory.getStartMatchingTime();
        long pastTime = DateUtil.secondsBetween(startMatchingTime, LocalDateTime.now());
        logger.info(">> 用户[{}]点击开始匹配[{}]秒后获取匹配信息.", uid, pastTime);
        String userMatchingGameInfo = String.join("·",
                matchingHistory.getTeamTypeDisplayName(), matchingHistory.getGameZoneName(),
                matchingHistory.getGameDanName());
        return PVPFreeTeamCurrentMatchingInfoVO.builder()
                .userMatchingGameInfo(userMatchingGameInfo)
                // 免费车队, 写死先
                .teamCategoryName(TeamCategoryEnum.FREE.getDesc())
                .userMatchingGameInfo(userMatchingGameInfo)
                .userAvatar(currentUser.getAvatar())
                .matchedTeam(ObjectTools.isNotEmpty(teamMemberList))
                .matchingPastTime(Long.valueOf(pastTime).intValue())
                .memberInfoList(teamMemberList)
                .build();
    }

    @Override
    public PagingResponse<PVPFreeTeamBackupVO> getTeamInfosForBackup(
            PVPFreeTeamsForBackupParams params) {
        //1：先分页查出车队
        Page<PVPFreeTeamBackupVO> page = PageHelper.startPage(params.getOffset(), params.getLimit())
                .doSelectPage(() -> teamRepository
                        .selectPVPFreeTeamsForBackup(UserIdentityEnum.LEADER.getCode(), params));
        List<PVPFreeTeamBackupVO> result = page.getResult();
        result.forEach(it -> {
            it.setSettlementName(it.getSettlementNumber().stripTrailingZeros().toPlainString()
                    + SettlementTypeEnum.getByCode(it.getSettlementType().intValue()).getDesc());
            it.setStatusZh(TeamStatusEnum.convert(it.getStatus()).getMsg());
            List<TeamGameResultVO> teamGameResultVOS = this.getTeamGameResult(it.getSequence());
            it.setGameResults(teamGameResultVOS);
        });
        return new PagingResponse<PVPFreeTeamBackupVO>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), result);
    }
}
