package com.kaihei.esportingplus.gamingteam.domain.service;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.data.JsonSerializable;
import com.kaihei.esportingplus.common.enums.BaojiLevelEnum;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.lock.RedisLock;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamMemberActionEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.anno.TeamOperation;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContext;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamGame;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.TeamMember;
import com.kaihei.esportingplus.gamingteam.data.manager.core.populator.RoomNumberGenerator;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossCancelPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.BossConfirmPrepareScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.CreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.DismissTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.EndTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.JoinTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.KickOutTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.LeaveTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.RecreateTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.StartTeamScene;
import com.kaihei.esportingplus.gamingteam.data.manager.core.scene.UpdateTeamPositionCountScene;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import com.maihaoche.starter.mq.base.AbstractMQTransactionProducer;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 车队业务公共方法
 *
 * @Author LiuQing.Qin
 * @Date 2018/11/6 10:59:29
 */
public abstract class AbstractTeamService<TG extends TeamGame, TM extends TeamMember> implements
        TeamService {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractTeamService.class);
    protected static CacheManager cacheManager = CacheManagerFactory.create();
    protected static final String TEAM_LOCK_PRE = "team:lock:";

    private Class<TG> tgType;
    private Class<TM> tmType;

    @SuppressWarnings("unchecked")
    public AbstractTeamService() {
        try {
            ParameterizedType genericSuperclass = (ParameterizedType) this.getClass()
                    .getGenericSuperclass();
            tgType = (Class<TG>) genericSuperclass.getActualTypeArguments()[0];
            tmType = (Class<TM>) genericSuperclass.getActualTypeArguments()[1];
        } catch (Exception e) {
            //ignored
        }
    }

    /**
     * 车队序列号前缀
     */
    protected static final String TEAM_SEQUENCE_PREFIX = "GT";

    @Autowired
    private RoomNumberGenerator roomNumGenerator;

    @Autowired
    private PVPContextHolder<TG, TM> pvpContextHolder;

    @Autowired
    SnowFlake snowFlake;

    /**
     * 通过分布式雪花 id 生成车队序列号
     * @return
     */
    protected String generateTeamSequence() {
        return TEAM_SEQUENCE_PREFIX + snowFlake.nextId();
    }

    /**
     * 生成车队房间号
     */
    protected Integer generateRoomNum() {
        return roomNumGenerator.next();
    }

    /**
     * 组建车队
     */
    @Transactional
    @TeamOperation(init = true, scene = CreateTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "creator:$.uid", uid = "uid", expireTime = 5000)
    public TeamSequenceVO createTeam(JsonSerializable jsonSerializable) {
        Date now = new Date();
        PVPContext<TG, TM> context = pvpContextHolder.getContext();
        //同步Team
        Team team = context.getTeam();
        team = jsonSerializable.cast(team);
        team.setGmtModified(now);
        context.setTeam(team);

        //同步TeamGame
        TG tg = jsonSerializable.cast(tgType);
        tg.setGmtCreate(now);
        tg.setGmtModified(now);

        logger.info("生成TG -> {}", tg);
        context.setTeamGame(tg);

        //创建用户
        TM tm = createTM(jsonSerializable);

        context.getTeamMemberList().add(tm);

        return TeamSequenceVO.builder().sequence(team.getSequence()).group(team.getGroupId())
                .build();
    }

    /**
     * 组建车队
     */
    @Transactional
    @TeamOperation(init = true, scene = RecreateTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "creator:$.uid", uid = "uid", expireTime = 5000)
    public TeamSequenceVO recreateTeam() {
        Date now = new Date();
        PVPContext<TG, TM> context = pvpContextHolder.getContext();
        String teamSequence = TEAM_SEQUENCE_PREFIX + snowFlake.nextId();

        //重新初始化 、入库
        Team team = context.getTeam();
        team.setId(null);
        team.setGmtCreate(now);
        team.setGmtModified(now);
        team.setStatus(((byte) (TeamStatusEnum.PREPARING.getCode())));
        team.setSequence(teamSequence);

        TG teamGame = context.getTeamGame();
        teamGame.setId(null);
        teamGame.setTeamId(team.getId());
        teamGame.setGmtCreate(now);
        teamGame.setGmtModified(now);

        List<TM> teamMemberList = context.getTeamMemberList();
        teamMemberList.removeIf(tm -> PVPTeamMemberStatusEnum.TEAM_START_QUIT
                .equals(PVPTeamMemberStatusEnum.of(tm.getStatus())));

        //重新设置车队Id
        //去除存盘后生成的Id
        //去除存盘后生成的Id
        //重置创建、修改、加入时间
        //重置 老板状态
        //去除消费卷
        teamMemberList.forEach(tm -> {
            tm.setId(null);
            tm.setTeamId(team.getId());
            tm.setGmtCreate(now);
            tm.setGmtModified(now);
            tm.setJoinTime(now);
            tm.setCouponsIds(null);
            if (UserIdentityEnum.BOSS.equals(UserIdentityEnum.of(tm.getUserIdentity()))) {
                tm.setStatus(((byte) (PVPTeamMemberStatusEnum.WAIT_READY.getCode())));
            }
        });

        return null;
    }

    /**
     * 加入车队
     */
    @TeamOperation(sequencePath = "jsonSerializable.teamSequence", scene = JoinTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE
            + "$.jsonSerializable.teamSequence")
    public void joinTeam(JsonSerializable jsonSerializable) {
        PVPContext<TG, TM> context = pvpContextHolder.getContext();
        TM me = context.getMe();
        logger.info("用户加入车队入参 -> {}", jsonSerializable);

        if (me == null) {
            TM tm = createTM(jsonSerializable);
            List<TM> teamMemberList = context.getTeamMemberList();
            teamMemberList.add(tm);
        }
    }

    private TM createTM(JsonSerializable jsonSerializable) {
        Date now = new Date();
        //创建用户
        TM tm = jsonSerializable.cast(tmType);
        UserSessionContext user = pvpContextHolder.getUser();
        tm = user.cast(tm, "id");

        logger.info("user -> {}", user);

        //时间
        tm.setGmtCreate(now);
        tm.setGmtModified(now);
        tm.setJoinTime(now);

        //初始化暴鸡等级和待准备状态
        tm.setBaojiLevel(BaojiLevelEnum.BOSS.getCode());
        tm.setStatus(((byte) (PVPTeamMemberStatusEnum.WAIT_READY.getCode())));

        logger.info("生成TM -> {}", tm);

        return tm;
    }

    /**
     * 退出车队
     */
    @Override
    @TeamOperation(scene = LeaveTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "$.sequence")
    public void quitTeam(String sequence) {
        PVPContext<TG, TM> context = pvpContextHolder.getContext();
        Team team = context.getTeam();
        TM me = context.getMe();
        TeamStatusEnum preparing = TeamStatusEnum.PREPARING;
        //准备中、移除
        if (preparing.equals(TeamStatusEnum.of(team.getStatus()))) {
            List<TM> teamMemberList = context.getTeamMemberList();
            teamMemberList.remove(me);
            return;
        }
        PVPTeamMemberStatusEnum teamStartQuit = PVPTeamMemberStatusEnum.TEAM_START_QUIT;

        if (!teamStartQuit.equals(PVPTeamMemberStatusEnum.of(me.getStatus()))) {
            //开车后、设成离线
            me.setStatus(((byte) (teamStartQuit.getCode())));
            me.setGmtModified(new Date());
        }
    }

    /**
     * 踢出车队
     */
    @Override
    @TeamOperation(scene = KickOutTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "$.sequence")
    public void kickOutTeamMember(String sequence, String uid) {
        pvpContextHolder.getContext().getTeamMemberList().removeIf(it -> it.getUid().equals(uid));
    }

    /**
     * 解散车队
     */
    @Transactional(rollbackFor = Exception.class)
    @TeamOperation(end = true, scene = DismissTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "$.sequence", expireTime = 5000)
    public void dismissTeam(String sequence) {
        Team team = pvpContextHolder.getContext().getTeam();
        team.setGmtModified(new Date());
        team.setStatus(((byte) (TeamStatusEnum.DISMISSED.getCode())));
    }

    /**
     * 立即开车
     */
    @Override
    @TeamOperation(scene = StartTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "$.sequence")
    public void startTeam(String sequence) {
        Team team = pvpContextHolder.getContext().getTeam();
        team.setStatus(((byte) (TeamStatusEnum.RUNNING.getCode())));
        team.setGmtModified(new Date());
    }


    /**
     * 结束车队
     */
    @Transactional(rollbackFor = Exception.class)
    @TeamOperation(end = true, sequencePath = "pvpTeamEndParams.teamSequence", scene = EndTeamScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "$.pvpTeamEndParams.teamSequence")
    public void endTeam(PVPTeamEndParams pvpTeamEndParams) {
        PVPContext<TG, TM> context = pvpContextHolder.getContext();
        Team contextTeam = context.getTeam();
        Date now = new Date();

        contextTeam.setStatus(((byte) TeamStatusEnum.COMPLETED.getCode()));
        contextTeam.setGmtModified(now);

    }

    /**
     * 取消准备
     */
    @TeamOperation(scene = BossCancelPrepareScene.class)
    public void cancelReadyGamingTeam(String sequence) {
        TM me = pvpContextHolder.getContext().getMe();
        me.setStatus(((byte) (PVPTeamMemberStatusEnum.WAIT_READY.getCode())));
        me.setGmtModified(new Date());
    }


    /**
     * 修改车队位置数量
     */
    @Override
    @TeamOperation(sequencePath = "positionCountParams.sequence", scene = UpdateTeamPositionCountScene.class)
    @RedisLock(keyFormate = TEAM_LOCK_PRE + "$.positionCountParams.sequence")
    public void updateTeamPositioncount(String uid, UpdatePositionCountParams positionCountParams) {
        PVPContext<TG, TM> context = pvpContextHolder.getContext();
        //判断修改位置小于当前已有人数
        if (positionCountParams.getNumber() < context.getTeamMemberList().size()) {
            throw new BusinessException(BizExceptionEnum.TEAM_SEAT_DOWN_LIMIT);
        }
        Team team = pvpContextHolder.getContext().getTeam();
        team.setActuallyPositionCount(positionCountParams.getNumber());
        team.setGmtModified(new Date());
    }

    /**
     * 确认准备
     */
    @TeamOperation(scene = BossConfirmPrepareScene.class)
    public void confirmReadyGamingTeam(String sequence) {
        TM me = pvpContextHolder.getContext().getMe();
        me.setGmtModified(new Date());
        me.setStatus(((byte) (PVPTeamMemberStatusEnum.PREPARE_READY.getCode())));
    }


    /**
     * 根据车队序列号生成一个分布式事务 id
     */
    protected String genTransactionId(String teamSequence) {
        String now = DateUtil.dateTime2Str(LocalDateTime.now(), DateUtil.SIMPLE_FORMATTER);
        return String.format(RocketMQConstant.GAMINGTEAM_TRANSACTION_ID, teamSequence, now);
    }

    /**
     * 发送 mq 事务消息
     *
     * @param teamSequence 车队序列号
     * @param tags mq tags
     * @param msgBody 消息的 body
     * @param localTransaction 本地事务
     * @param mqTransactionProducer 消息生产者
     */
    protected void sendTransactionMessage(String teamSequence, String tags, Object msgBody,
            Object localTransaction, AbstractMQTransactionProducer mqTransactionProducer) {
        Message message = new Message();
        message.setTopic(RocketMQConstant.TOPIC_RPG);
        message.setTags(tags);
        message.setTransactionId(genTransactionId(teamSequence));
        message.setBody(FastJsonUtils.toJson(msgBody).getBytes());
        SendResult sendResult = mqTransactionProducer
                .sendMessageInTransaction(message, localTransaction);
        if (ObjectTools.isNotNull(sendResult)
                && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            logger.info(">> MQ事务消息发送成功,事务id:[{}]", message.getTransactionId());
            return;
        }
        // 直接抛出业务异常
        logger.error(" >> 发送MQ事务消息失败! teamSequence: {}, tags: {},"
                        + " msgBody: {}, localTransaction: {}",
                teamSequence, tags, msgBody, localTransaction);
        throw new BusinessException(BizExceptionEnum.TEAM_MQ_SEND_FAIL);
    }

    /**
     * 校验车队状态
     *
     * @param teamMemberActionEnum 车队队员当前的行为
     * @param teamStatus 车队当前状态
     */
    protected void checkTeamStatus(String teamSequence,
            TeamMemberActionEnum teamMemberActionEnum, Integer teamStatus) {

        TeamStatusEnum teamStatusEnum = TeamStatusEnum.convert(teamStatus);
        boolean passed = false;

        switch (teamMemberActionEnum) {
            // 加入车队、开车、踢人、确认入团、准备、取消准备时车队状态应该为: 准备中
            case START:
            case JOIN:
            case KICK_OUT:
            case RPG_CONFIRM_JOIN:
            case PVP_PREPARE:
            case PVP_CANCEL_PREPARE:
                if (TeamStatusEnum.PREPARING.getCode() == teamStatus) {
                    passed = true;
                }
                break;
            // 退出、解散时车队状态应该为: 准备中或进行中
            case QUIT:
            case DISMISS:
                if (TeamStatusEnum.PREPARING.getCode() == teamStatus
                        || TeamStatusEnum.RUNNING.getCode() == teamStatus) {
                    passed = true;
                }
                break;
            // 正常结束车队时车队状态应该为: 进行中
            case END:
                if (TeamStatusEnum.RUNNING.getCode() == teamStatus) {
                    passed = true;
                }
                break;
            default:
                break;
        }
        // 未通过抛出异常
        if (!passed) {
            logger.error("[{}]时,车队[{}]当前状态[{}]校验错误!", teamMemberActionEnum.getMsg(),
                    teamSequence, teamStatusEnum.getMsg());
            throw new BusinessException(BizExceptionEnum.TEAM_STATUS_ERROR,
                    teamStatusEnum.getMsg());
        }
    }

}
