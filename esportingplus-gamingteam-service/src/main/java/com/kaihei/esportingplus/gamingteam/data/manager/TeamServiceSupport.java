package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.freeteam.DanDictVo;
import com.kaihei.esportingplus.api.vo.freeteam.GameDanRangeVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.task.JobScheduleService;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.tools.RedisUtils;
import com.kaihei.esportingplus.gamingteam.api.enums.ImMemberOutReasonEnum;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcBossPaidSuccessEvent;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcConfirmJoinTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcEndTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcJoinTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcLeaveTeamEvent;
import com.kaihei.esportingplus.gamingteam.event.rongcloud.RcStartTeamEvent;
import com.kaihei.esportingplus.gamingteam.schedule.BossPaymentTimeOutJob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 抽取与车队业务耦合性较低的一些方法
 * 主要包括处理老板定时任务和推送事件通知等
 * @author liangyi
 */
@Component("teamServiceSupport")
public class TeamServiceSupport {

    private static final Logger logger = LoggerFactory.getLogger(TeamServiceSupport.class);
    private static CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    DictionaryClient dictionaryClient;

    /**
     * 车队老板支付定时任务 jobName
     */
    private static final String BOSS_PAYMENT_TIMEOUT_JOB_PREFIX = "boss-payment-job_%s_%s_%s";

    @Autowired
    JobScheduleService jobScheduleService;
    @Autowired
    BossPaymentTimeOutJob bossPaymentTimeOutJob;

    /**
     * 创建老板加入车队后超时未支付任务
     *
     * @param teamSequence 车队序列号
     * @param bossUid 老板 uid
     * @param bossJoinTime 老板加入车队时间
     * @param paymentTimeOut 老板超时未支付时间
     */
    public void createBossPaymentTimeoutJob(String teamSequence, String bossUid,
            Date bossJoinTime, Integer paymentTimeOut) {
        String now = DateUtil.dateTime2Str(LocalDateTime.now(), DateUtil.SIMPLE_FORMATTER);
        String jobName = String.format(BOSS_PAYMENT_TIMEOUT_JOB_PREFIX, teamSequence, bossUid, now);
        Date timeout = DateUtils.addSeconds(bossJoinTime, paymentTimeOut);
        // 将 jobName 存入 redis
        String jobShardingKey = getBossPaymentTimeoutJobShadingKey(bossUid);
        cacheManager.hset(jobShardingKey, bossUid, jobName);
        jobScheduleService.addJob(bossPaymentTimeOutJob, jobName, timeout);
        logger.info(">> 老板[{}]加入车队[{}], 创建超时未支付定时任务[{}]",
                bossUid, teamSequence, jobName);
    }

    /**
     * 删除老板到期未支付的定时任务
     *
     * @param teamSequence 车队序列号
     * @param bossUid 老板 uid
     */
    public void removeBossPaymentTimeoutJob(String teamSequence, String bossUid) {
        // 从 redis 中将 jobName 取出
        String jobShardingKey = getBossPaymentTimeoutJobShadingKey(bossUid);
        String jobName = cacheManager.hget(jobShardingKey, bossUid, String.class);
        if (ObjectTools.isNotEmpty(jobName)) {
            jobScheduleService.removeJob(jobName);
            // 同时删除该 value
            cacheManager.hdel(jobShardingKey, bossUid);
            logger.info(">> 删除老板[{}]在车队[{}]中的定时任务[{}]", bossUid, teamSequence, jobName);
        }
    }

    /**
     * 获取一个老板超时未支付定时任务的分片 key
     * @param bossUid
     * @return
     */
    private String getBossPaymentTimeoutJobShadingKey(String bossUid) {
        return RedisUtils.buildHashSegmentKey(bossUid, RedisKey.TEAM_BOSS_JOB_PREFIX);
    }


    /**
     * 创建或加入车队事件, 用于加入融云 im 群组
     *
     * @param teamSequence 车队序列号
     * @param teamTitle 车队标题
     * @param uid 车队队员 uid
     * @param username 车队队员昵称
     * @param otherUidList 车队其他要接收消息的队员 uid
     * @param createTeam 是否是创建车队 true: 创建车队 false: 加入车队
     */
    public void postJoinTeamEvent(String teamSequence, String teamTitle, String uid,
            String username, List<String> otherUidList, boolean createTeam) {
        RcJoinTeamEvent rcJoinTeamEvent = new RcJoinTeamEvent();
        rcJoinTeamEvent.setTeamSequence(teamSequence);
        rcJoinTeamEvent.setTeamTitle(teamTitle);
        rcJoinTeamEvent.setUid(uid);
        rcJoinTeamEvent.setUsername(username);
        rcJoinTeamEvent.setOtherUidList(otherUidList);
        rcJoinTeamEvent.setCreateTeam(createTeam);
        EventBus.post(rcJoinTeamEvent);
    }

    /**
     * 离开车队(退出车队、踢出车队)事件, 用于退出融云 im 群组
     *
     * @param teamSequence 车队序列号
     * @param teamTitle 车队名称
     * @param memberUid 离开车队的队员 uid
     * @param memberUsername 离开车队的队员 username
     * @param otherUidList 其他队员 uid
     * @param kickOut true: 被踢出, false: 主动退出
     */
    public void postLeaveTeamEvent(String teamSequence,
            String teamTitle, String memberUid, String memberUsername,
            List<String> otherUidList, boolean kickOut) {
        // 融云
        RcLeaveTeamEvent rcLeaveTeamEvent = RcLeaveTeamEvent.builder()
                .uid(memberUid)
                .username(memberUsername)
                .teamSequence(teamSequence)
                .teamTitle(teamTitle)
                .otherUidList(otherUidList)
                .kickOut(kickOut)
                .build();
        if (kickOut) {
            rcLeaveTeamEvent.setOutReason(ImMemberOutReasonEnum.LEADER_OUT);
        }
        EventBus.post(rcLeaveTeamEvent);
    }

    /**
     * 老板支付成功事件, 用于修改老板在车队中的状态
     * @param teamSequence 车队序列号
     */
    public void postBossPaidSuccessEvent(String teamSequence) {
        RcBossPaidSuccessEvent rcBossPaidSuccessEvent = new RcBossPaidSuccessEvent();
        rcBossPaidSuccessEvent.setTeamSequence(teamSequence);
        EventBus.post(rcBossPaidSuccessEvent);
    }


    /**
     * 立即开车事件
     *
     * @param teamSequence 车队序列号
     */
    public void postStartTeamEvent(String teamSequence) {
        RcStartTeamEvent rcStartTeamEvent = new RcStartTeamEvent();
        rcStartTeamEvent.setTeamSequence(teamSequence);
        EventBus.post(rcStartTeamEvent);
    }


    /**
     * 结束车队(解散车队、结束车队)事件, 用于销毁融云 im 群组
     * @param teamSequence 车队序列号
     * @param teamTitle 车队标题
     * @param leaderUid 队长 uid
     * @param allMemberUidList 所有队员 uid
     * @param dismissTeam 是否是解散车队 true: 是; false: 否
     */
    public void postEndTeamEvent(String teamSequence, String teamTitle,
            String leaderUid, List<String> allMemberUidList, boolean dismissTeam) {
        if (ObjectTools.isNotEmpty(allMemberUidList)) {
            // 不向队长自己推送融云解散消息
            allMemberUidList.remove(leaderUid);
        }
        RcEndTeamEvent rcEndTeamEvent = RcEndTeamEvent.builder()
                .dismissTeam(dismissTeam)
                .teamSequence(teamSequence)
                .teamTitle(teamTitle)
                .leaderUid(leaderUid)
                .otherUidList(allMemberUidList)
                .build();
        EventBus.post(rcEndTeamEvent);
    }

    /**
     * 确认入团事件, RPG 车队使用
     *
     * @param teamSequence 车队序列号
     */
    public void postConfirmJoinTeamEvent(String teamSequence) {
        RcConfirmJoinTeamEvent rcConfirmJoinTeamEvent = new RcConfirmJoinTeamEvent();
        rcConfirmJoinTeamEvent.setTeamSequence(teamSequence);
        EventBus.post(rcConfirmJoinTeamEvent);
    }

    /**
     * 计算车队段位范围
     * @param gameId 游戏 id
     * @param bossDanIdList 老板段位 id 列表
     * @param teamLowerDanId 车队段位下限 id
     * @param teamUpperDanId 车队段位上限 id
     * @return
     */
    public GameDanRangeVO calculateGamingTeamDanRange(Integer gameId,
            List<Integer> bossDanIdList, Integer teamLowerDanId, Integer teamUpperDanId) {

        List<DanDictVo> gameDanList = getGameDanList(gameId);
        if (gameDanList.isEmpty()) {
            // 为空返回原来的车队数据
            return GameDanRangeVO.builder()
                    .lowerDanId(teamLowerDanId)
                    .upperDanId(teamUpperDanId)
                    .build();
        }
        DanDictVo teamLowerDan = null;
        DanDictVo teamUpperDan = null;
        for (DanDictVo danDictVo : gameDanList) {
            if (danDictVo.getDictId().equals(teamLowerDanId)) {
                teamLowerDan = danDictVo;
            }
            if (danDictVo.getDictId().equals(teamUpperDanId)) {
                teamUpperDan = danDictVo;
            }
        }

        List<DanDictVo> dangRange = new ArrayList<>();

        for (int i = 0; i < bossDanIdList.size(); i++) {
            // 取每个老板的段位和车队初始的交集
            List<DanDictVo> danIntersection = calculateDanRange(gameDanList,
                    bossDanIdList.get(i), teamLowerDan, teamUpperDan);
            if (i == 0) {
                // 第一个取第一次的交集
                dangRange = danIntersection;
            } else {
                dangRange = (ArrayList) CollectionUtils.intersection(dangRange, danIntersection);
            }
        }
        // 升序
        dangRange.sort(Comparator.comparingInt(DanDictVo::getOrderIndex));
        if (!dangRange.isEmpty()) {
            return GameDanRangeVO.builder()
                    .lowerDanId(dangRange.get(0).getDictId())
                    .upperDanId(dangRange.get(dangRange.size()-1).getDictId())
                    .build();
        }
        return GameDanRangeVO.builder()
                .lowerDanId(teamLowerDanId)
                .upperDanId(teamUpperDanId)
                .build();
    }


    public static void main(String[] args) {

        List<DanDictVo> danDictVos = new ArrayList<>();
        for (int i = 1; i <= 27; i++) {
            DanDictVo danDictVo = new DanDictVo();
            danDictVo.setDictId(i);
            danDictVo.setOrderIndex(i);
            danDictVo.setSocial(i%5==0?i/5: i/5+1);
            danDictVos.add(danDictVo);
        }
        /*GameDanRangeVO gameDanRangeVO = calculateGamingTeamDanRange(1,
                Arrays.asList(8,16),
                6, 24, danDictVos);
        System.out.println(gameDanRangeVO);*/
    }


    private List<DanDictVo> calculateDanRange(List<DanDictVo> gameDanList, Integer bossDanId,
            final DanDictVo teamLowerDan, final DanDictVo teamUpperDan) {
        DanDictVo bossDan = gameDanList.stream()
                .filter(d -> d.getDictId().equals(bossDanId))
                .findAny().orElse(null);

        if (bossDan != null && teamLowerDan != null && teamUpperDan != null) {
            final Integer bossDanSocial = bossDan.getSocial();
            final Integer teamLowerDanOrderIndex = teamLowerDan.getOrderIndex();
            final Integer teamUpperDanOrderIndex = teamUpperDan.getOrderIndex();
            return gameDanList.stream()
                    // 过滤阶层 -+1的段位(自己段位的阶层差是 0,也要包含在里面)
                    .filter(dan -> Math.abs(dan.getSocial() - bossDanSocial) == 1
                            || (dan.getSocial() - bossDanSocial) == 0)
                    // 升序排序
                    .sorted(Comparator.comparingInt(DanDictVo::getOrderIndex))
                    // 和车队段位取交集
                    .filter(dan -> dan.getOrderIndex() >= teamLowerDanOrderIndex)
                    .filter(dan -> dan.getOrderIndex() <= teamUpperDanOrderIndex)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * 获取游戏下的所有段位
     * @param gameId 游戏 id
     * @return
     */
    private List<DanDictVo> getGameDanList(Integer gameId) {

        List<DanDictVo> gameDanList = new ArrayList<>();

        // 查询该游戏下面的所有段位
        // 这里考虑对缓存做进一步处理, 更好取基本数据 TODO
        ResponsePacket<List<DictBaseVO<Object>>> gameDanListResp = dictionaryClient
                .findByDictcionayPidAndCategoryCode(gameId,
                        DictionaryCategoryCodeEnum.GAME_DAN.getCode());

        if (!gameDanListResp.responseSuccess()) {
            logger.error(">> 计算免费车队动态接单范围,调用资源服务获取游戏段位错误:{}", gameDanListResp);
        } else {
            List<DictBaseVO<Object>> dictBaseVOS = gameDanListResp.getData();
            gameDanList = dictBaseVOS.stream()
                    .map(dict -> JacksonUtils.toBeanWithSnake(
                            JacksonUtils.toJson(dict.getValue()), DanDictVo.class))
                    .collect(Collectors.toList());
        }
        return gameDanList;
    }

}
