package com.kaihei.esportingplus.gamingteam.domain.service;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.api.feign.DictionaryClient;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.enums.PVPTeamMemberStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.enums.TeamStatusEnum;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPUpdateOrderStatusMessage;
import com.kaihei.esportingplus.gamingteam.api.params.TeamQueryParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.BossConfirmPaidSuccessParams;
import com.kaihei.esportingplus.gamingteam.api.vo.BossInfoForOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.GameTeamTotal;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPRedisTeamMemberVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPRedisTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPTeamStartOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamListVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberCompaintAdminVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceUidVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.data.manager.cache.PVPTeamCacheManager;
import com.kaihei.esportingplus.gamingteam.data.manager.core.context.PVPContextHolder;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamGamePVP;
import com.kaihei.esportingplus.gamingteam.domain.entity.TeamMemberPVP;
import com.kaihei.esportingplus.gamingteam.event.BossConfirmPaidSuccessEvent;
import com.kaihei.esportingplus.gamingteam.rocketmq.producer.GamingTeamCommonProducer;
import com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum;
import com.kaihei.esportingplus.trade.api.params.OrderTeamPVPMember;
import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusPVPParams;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Pipeline;

/**
 * 操作 {@link UserSessionContext} 获取用户信息
 *
 * 操作{@link PVPContextHolder} 获取车队信息
 *
 * 操作{@link PVPContextHolder} 内的对象实现组建车队、上下车等操作(注：该类仅需操作对象)，缓存、推送及存Mysql通过AOP完成
 *
 * @Author LiuQing.Qin
 * @author 谢思勇
 * @Date 2018/11/6 11:00:52
 */
@Service
public class PVPTeamServiceImpl extends AbstractTeamService<TeamGamePVP, TeamMemberPVP> implements
        PVPTeamService {

    @Autowired
    private PVPTeamCacheManager pvpTeamCacheManager;

    @Autowired
    private DictionaryClient dictionaryClient;

    @Autowired
    private PVPContextHolder<TeamGamePVP, TeamMemberPVP> pvpContextHolder;
    /**
     * 字典Id转name
     */
    private Function<Integer, String> dictId2Name = dictId -> dictionaryClient.findById(dictId)
            .getData().getName();

    @Autowired
    GamingTeamCommonProducer gamingTeamCommonProducer;


    @Override
    public PagingResponse<RPGTeamListVO> findTeamList(Integer gameCode,
            TeamQueryParams teamQueryParams) {
        return null;
    }

    @Override
    public GameTeamTotal findTeamTotal(Integer gameCode, TeamQueryParams teamQueryParams) {
        return null;
    }

    @Override
    public RPGRedisTeamBaseVO getTeamBaseInfo(String sequence) {
        return null;
    }

    @Override
    public RPGTeamCurrentInfoVO getTeamCurrentInfo(String sequence) {
        return null;
    }

    @Override
    public BossInfoForOrderVO getBossInfoForOrder(String sequence, String uid) {
        return null;
    }

    @Override
    public PVPTeamStartOrderVO getBaojiInfoForOrder(String sequence) {
        return null;
    }

    @Override
    public RPGMemberInTeamVO getMemberInTeam(Integer gameCode) {
        return null;
    }

    @Override
    public Integer getBossPayCountdown(String sequence) {
        return null;
    }

    @Override
    public TeamGameResultVO getTeamGameResult(String sequence) {
        return null;
    }

    @Override
    public List<TeamMemberCompaintAdminVO> getTeamMemberBriefInfo(String sequence) {
        return null;
    }

    @Override
    public List<TeamInfoVO> getBatchTeamInfo(List<String> sequenceList) {
        return null;
    }

    @Override
    public List<TeamSequenceUidVO> getBaojiTeamSequencesByUids(List<String> uids) {
        return null;
    }

    @Override
    public void bossConfirmPaidSuccess(BossConfirmPaidSuccessParams bossConfirmPaidSuccessParams) {
        String bossUid = UserSessionContext.getUser().getUid();
        logger.info(">> PVP车队老板确认支付成功,参数:{}", bossConfirmPaidSuccessParams);
        BossConfirmPaidSuccessEvent bossConfirmPaidSuccessEvent = BossConfirmPaidSuccessEvent
                .builder()
                .bossUid(bossUid)
                .sequence(bossConfirmPaidSuccessParams.getTeamSequence())
                .orderSequence(bossConfirmPaidSuccessParams.getOrderSequence())
                .isRPG(false)
                .build();
        EventBus.post(bossConfirmPaidSuccessEvent);
    }

    @Override
    public void rollbackTeam(String sequence,String actionUid,TeamOrderPVPActionEnum pvpActionEnum) {
        // 1:查询出车队，并对状态判定能不能回退
        logger.info(">>执行回退车队队员状态，待回退车队序列号为:{}",sequence);
        PVPRedisTeamVO redisTeamVO = pvpTeamCacheManager
                .queryTeamInfoBySequence(sequence);
        ValidateAssert.hasNotNull(BizExceptionEnum.TEAM_HAS_DISMISSED,redisTeamVO);
        if(TeamStatusEnum.PREPARING.getCode()!=redisTeamVO.getStatus().intValue()){
            //如果不在准备中，则不做任何操作
            logger.info(">>车队:{} 队员状态不能回退，因为车队状态不在准备状态",sequence);
            return ;
        }
        // 2：获取缓存在redis中的车队队员
        String teamMemberKey = String.format(RedisKey.TEAM_MEMBER_PREFIX, redisTeamVO.getId());
        Map<String, PVPRedisTeamMemberVO> teamMemberMap = cacheManager
                .hgetAll(teamMemberKey, PVPRedisTeamMemberVO.class);
        ValidateAssert.allNotNull(
                BizExceptionEnum.TEAM_HAS_DISMISSED,teamMemberMap);
        // 3：遍历车队队员老板，组装已支付退款数据，
        List<OrderTeamPVPMember> orderTeamMembers = buildRollbackMemberOrders(teamMemberMap,actionUid,pvpActionEnum);
        //如果可回退的队员为空，则不回退
        if(ObjectTools.isEmpty(orderTeamMembers)){
            logger.info(">>车队:{} 没有已支付的队员，不回退状态",sequence);
            return ;
        }
        this.sendRollbackPvpMemberOrders(orderTeamMembers,actionUid,redisTeamVO.getSequence(),redisTeamVO.getSettlementNumber().intValue());
        //设置全员状态为已准备状态
        this.setMembersStatusForReady(teamMemberKey,teamMemberMap);
    }

    private void setMembersStatusForReady(String teamMemberKey,
            Map<String, PVPRedisTeamMemberVO> teamMemberMap) {
        Pipeline pipelined = cacheManager.pipelined();
        teamMemberMap.values().forEach(e->{
            pipelined.hset(teamMemberKey,e.getUid(), JsonsUtils.toJson(e));
        });
        pipelined.sync();
    }

    private void sendRollbackPvpMemberOrders(List<OrderTeamPVPMember> orderTeamMembers,String actionUid,String teamSequence,Integer settleCounts) {
        PVPUpdateOrderStatusMessage pvpMessage = new PVPUpdateOrderStatusMessage();
        UpdateOrderStatusPVPParams pvpParams = new UpdateOrderStatusPVPParams();
        pvpParams.setTeamSequence(teamSequence);
        pvpParams.setSettleCounts(settleCounts);
        pvpParams.setTeamStatus(TeamStatusEnum.PREPARING.getCode());
        pvpParams.setTeamMembers(orderTeamMembers);
        pvpMessage.setUpdateOrderStatusPVPParams(pvpParams);
        gamingTeamCommonProducer.sendRollbackTeamOrderStatus(actionUid,pvpMessage);
    }

    public List<OrderTeamPVPMember> buildRollbackMemberOrders(Map<String, PVPRedisTeamMemberVO> teamMemberMap,String actionUid,TeamOrderPVPActionEnum pvpActionEnum){
        return teamMemberMap.values().stream()
                .filter(it -> UserIdentityEnum.BOSS.getCode() == it.getUserIdentity()
                        && PVPTeamMemberStatusEnum.PAID.getCode() == it.getStatus()).map(it -> {
                    OrderTeamPVPMember pvpMember = new OrderTeamPVPMember();
                    pvpMember.setTeamMemberUID(it.getUid());
                    pvpMember.setTeamMemberName(it.getUsername());
                    pvpMember.setUserIdentity(it.getUserIdentity());
                    if(it.getUid().equals(actionUid)){
                        pvpMember.setTeamMemberStatus(pvpActionEnum.getCode());
                    }else{
                        pvpMember.setTeamMemberStatus(TeamOrderPVPActionEnum.PAID_REFUND_OTHER.getCode());
                    }
                    return pvpMember;
                }).collect(Collectors.toList());
    }
}
