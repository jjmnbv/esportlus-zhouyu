package com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.matching;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.enums.DictionaryCodeEnum;
import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.sensors.enums.SensorsEventEnum;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.ForkJionUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.gamingteam.api.enums.MatchResult;
import com.kaihei.esportingplus.gamingteam.api.params.ImMatchParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamFreeJoinParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImMsgService;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamBossMatchingVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamMatchingRouteVO;
import com.kaihei.esportingplus.gamingteam.data.manager.core.model.PVPFreeTeamMatchingVO;
import com.kaihei.esportingplus.gamingteam.domain.service.PVPTeamFreeService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *@Description: 匹配逻辑处理
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/11/16 12:00
*/
@Component
public class PVPFreeTeamMatchingHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PVPFreeTeamMatchingHandler.class);

    private CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private DictionaryClient dictionaryClient;

    @Autowired
    private PVPTeamFreeService pvpTeamFreeService;

    @Autowired
    private PVPFreeTeamMatchingPool pvpFreeTeamMatchingPool;

    @Autowired
    private ImMsgService imMsgService;

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    /**
     *@Description: 构造器初始化redis队列消费任务
     *@param: []
     *@return:
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/16 16:33
    */
    public void startMatching() {
        ResponsePacket<DictBaseVO<Object>> freeTeamConfig = dictionaryClient
                .findByCodeAndCategoryCode(DictionaryCategoryCodeEnum.FREE_TEAM_CONFIG.getCode(),
                        DictionaryCodeEnum.FREE_TEAM_CONFIG_CODE.getCode(), null);
        if(freeTeamConfig.getCode() != BizExceptionEnum.SUCCESS.getErrCode()){
            LOGGER.error("免费车队配置读取失败,重启吧：{}", freeTeamConfig.toString());
            throw new BusinessException(-1,"免费车队配置读取失败,重启吧："+freeTeamConfig.toString());
        }

        //获取免费车队配置
        FreeTeamConfigVO freeTeamConfigVO = JacksonUtils
                .toBeanWithSnake(JacksonUtils.toJson(freeTeamConfig.getData().getValue()),FreeTeamConfigVO.class);

        //获取匹配超时属性
        Integer timeout = freeTeamConfigVO.getMatchingWait().getWaitTimeout();
        //异步执行任务开始消费匹配数据
        LOGGER.info("启动匹配任务...");
        CompletableFuture.supplyAsync(()->matching(timeout), ForkJionUtils.getCommonPool());
    }

    private Void matching(int timeout){
        while (true){
            try {
                //从本地的全局变量里阻塞的取出已经在匹配池里的用户
                PVPFreeTeamMatchingRouteVO routeVO = PVPFreeTeamMatchingPool.matchingQueue.take();
                PVPFreeTeamBossMatchingVO redisMatchingVO = cacheManager.rpop(RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_QUEUE
                                + routeVO.getSettlementType() + ":" + routeVO.getTeamTypeId() + ":" + routeVO.getGameZoneId(),
                        PVPFreeTeamBossMatchingVO.class);

                if(redisMatchingVO != null){
                    String uid = redisMatchingVO.getUid();
                    Integer settlementType = redisMatchingVO.getSettlementType();
                    Integer teamTypeId = redisMatchingVO.getTeamTypeId();
                    Integer gameZoneId = redisMatchingVO.getGameZoneId();
                    Integer gameDanId = redisMatchingVO.getGameDanId();

                    //校验匹配是否超时，发起超时通知给前端
                    LocalDateTime startMatchingTime = redisMatchingVO.getStartMatchingTime();
                    //已经过去了这么久了
                    long pastTime = DateUtil.secondsBetween(startMatchingTime, LocalDateTime.now());
                    //如果大于配置的等待超时时间
                    if((pastTime >= timeout)){
                        //超时了直接 删除匹配记录
                        cacheManager.del(RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_HISTORY + uid);

                        LOGGER.error("老板[{}]匹配超时了,发送融云通知", uid);
                        ImMatchParams matchFailParams = ImMatchParams.builder()
                                .uids(Collections.singletonList(uid))
                                .result(MatchResult.FAIL.getCode())
                                .msgContent("您的匹配已经超时了")
                                .build();
                        imMsgService.sendMatchMsg(matchFailParams);

                        //上报神策 TODO
                        HashMap<String,Object> data = new HashMap<>(9);
                        data.put("reason","用户匹配中取消");//结束原因
                        data.put("seconds",pastTime);//匹配用时
                        data.put("game_type",redisMatchingVO.getGameId());//游戏类型
                        data.put("unit",redisMatchingVO.getSettlementName());//单位
                        data.put("game_zone",gameZoneId);//游戏大区
                        data.put("game_dan",gameDanId);//游戏段位
                        data.put("identity",redisMatchingVO.getUserIdentity());//用户身份
                        sensorsAnalyticsService.track(uid, SensorsEventEnum.END_MATCHING_FREETEAM.getCode(),data);
                        LOGGER.debug("上报神策成功入参:{}",data);
                    }else{
                        //没超时 先偷窥一下这个用户有还有没有匹配记录，因为取消的时候清掉匹配记录
                        PVPFreeTeamBossMatchingVO bossMatchingHisroty = cacheManager.get(
                                RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_HISTORY + uid,
                                PVPFreeTeamBossMatchingVO.class);

                        //如果没有匹配记录，丢弃此次消息(不放回匹配池)
                        if(bossMatchingHisroty == null){
                            LOGGER.error("用户[{}]取消过匹配或者所有车队都上车失败了，丢弃此次消息",uid);
                            continue;
                        }

                        //赶紧去找这个用户的游戏区有没有车队
                        Map<String, PVPFreeTeamMatchingVO> teamMatchings = cacheManager
                                .hgetAll(RedisKey.PVP_FREE_TEAM_MATCHING_TEAM_QUEUE
                                                + settlementType + ":" + teamTypeId + ":" + gameZoneId,
                                        PVPFreeTeamMatchingVO.class);

                        //对车队进行排序并且返回所有段位符合的车队
                        List<PVPFreeTeamMatchingVO> teams = teamMatchings.values().stream()
                                .filter(team->gameDanId >= team.getGameDanLowerLimit()
                                        && gameDanId <= team.getGameDanTopLimit())
                                .sorted(Comparator.comparing(PVPFreeTeamMatchingVO::getFreeMember)
                                        .thenComparing(PVPFreeTeamMatchingVO::getTeamCreateTime)).collect(
                        Collectors.toList());
                        //有车队
                        boolean matched = false;
                        if(CollectionUtils.isNotEmpty(teams)){
                            //偷窥一下有没有符合我段位的
                            for (PVPFreeTeamMatchingVO team: teams) {
                                //如果有车队的段位区间符合我的段位，啥也不说了 上车吧
                                //冲鸭 上赶紧挤上车，手快有 手慢无，简直无情
                                String teamSequence = team.getTeamSequence();
                                LOGGER.info("恭喜用户{}[{}-{}]匹配到了车队{}[{}-{}],赶紧去上车吧",
                                        uid,redisMatchingVO.getGameDanId(),redisMatchingVO.getGameDanName()
                                        ,teamSequence,team.getGameDanTopLimit(),team.getGameDanLowerLimit());
                                //如果不成功进下一个符合条件的车队，有可能第一个车队 满员或其他异常
                                try {
                                    //加入车队
                                    PVPTeamFreeJoinParams joinParams = PVPTeamFreeJoinParams.builder()
                                            .teamSequence(teamSequence)
                                            .gameDanId(gameDanId)
                                            .userIdentity(redisMatchingVO.getUserIdentity())
                                            .uid(uid)
                                            .chickenId(redisMatchingVO.getChickenId())
                                            .avatar(redisMatchingVO.getAvatar())
                                            .username(redisMatchingVO.getUsername())
                                            .build();
                                    pvpTeamFreeService.joinTeam(joinParams);

                                    //匹配成功 设置匹配记录 过期时间为10秒，前端只展示3秒
                                    cacheManager.expire(RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_HISTORY + uid,10);

                                    //推送成功消息
                                    ImMatchParams matchSuccessParams = ImMatchParams.builder()
                                            .uids(Collections.singletonList(uid))
                                            .result(MatchResult.SUCCESS.getCode())
                                            .msgContent("匹配成功")
                                            .teamSequence(teamSequence)
                                            .build();
                                    imMsgService.sendMatchMsg(matchSuccessParams);

                                    //上报神策 TODO
                                    HashMap<String,Object> data = new HashMap<>(9);
                                    data.put("reason","用户匹配中取消");//结束原因
                                    data.put("seconds",pastTime);//匹配用时
                                    data.put("team",teamSequence);//车队id
                                    data.put("room",pastTime);//房间id
                                    data.put("game_type",redisMatchingVO.getGameId());//游戏类型
                                    data.put("unit",redisMatchingVO.getSettlementName());//单位
                                    data.put("game_zone",gameZoneId);//游戏大区
                                    data.put("game_dan",gameDanId);//游戏段位
                                    data.put("identity",redisMatchingVO.getUserIdentity());//用户身份
                                    sensorsAnalyticsService.track(uid, SensorsEventEnum.END_MATCHING_FREETEAM.getCode(),data);
                                    LOGGER.debug("上报神策成功,入参:{}",data);
                                }catch (Exception e) {
                                    LOGGER.error("加入车队[{}]失败,继续加入下一个符合条件的车队",
                                            team.getTeamSequence(), e);
                                    continue;
                                }
                                //找到了 赶紧中断，防止又上了其他车
                                matched = true;
                                break;
                             }
                             //如果所有符合条件的车队都加入失败，就丢掉
                             if(!matched){
                                 LOGGER.error("所有能加入的车队都撸了一遍还失败，放弃吧少年，别挣扎了");
                                 //删除匹配记录
                                 cacheManager.del(RedisKey.PVP_FREE_TEAM_MATCHING_BOSS_HISTORY + uid);
                                continue;
                             }
                        }else{
                            //没有符合的车队 丢回老板匹配队列从新来过吧
                            pvpFreeTeamMatchingPool.pushMatchingPool(redisMatchingVO);
                        }
                    }
                }else{
                    LOGGER.error("用户匹配池无用户，或者被其他节点消费了");
                }
            } catch (Exception e) {
                LOGGER.error("匹配出问题了 接锅巴老鬼.",e);
            }
        }
    }
}
