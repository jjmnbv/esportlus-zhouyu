package com.kaihei.esportingplus.trade.domain.service.impl;

import static com.kaihei.esportingplus.common.enums.GameResultEnum.HOURS_NOT_PLAY;
import static com.kaihei.esportingplus.common.enums.GameResultEnum.HOURS_PLAYED;
import static com.kaihei.esportingplus.common.enums.GameResultEnum.ROUNDS_DEFEAT;
import static com.kaihei.esportingplus.common.enums.GameResultEnum.ROUNDS_NOT_PLAY;
import static com.kaihei.esportingplus.common.enums.GameResultEnum.ROUNDS_VICTORY;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BaojiLevelEnum;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.enums.PlayModeEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.sensors.enums.SensorsEventEnum;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.feign.PVPFreeTeamServiceClient;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamEndOrderMessage;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamStartOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPTeamLeaveMemberAfterStartedVO;
import com.kaihei.esportingplus.trade.api.enums.OrderStatusEnum;
import com.kaihei.esportingplus.trade.api.enums.OrderTeamStatus;
import com.kaihei.esportingplus.trade.api.params.ChickenPointIncomeParams;
import com.kaihei.esportingplus.trade.api.params.PVPFreeOrdersForBackGroundParams;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBaseVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBossOrderForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBossPointsVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderIncomeForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderListVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderVo;
import com.kaihei.esportingplus.trade.api.vo.PVPFreePreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeTeamMemberOrderVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeTeamOrderVo;
import com.kaihei.esportingplus.trade.api.vo.UserInfoVo;
import com.kaihei.esportingplus.trade.common.TradeConstants;
import com.kaihei.esportingplus.trade.common.UpdateOrderParams;
import com.kaihei.esportingplus.trade.data.repository.OrderItemTeamPVPFreeDetailRepository;
import com.kaihei.esportingplus.trade.data.repository.OrderItemTeamPVPFreeRepository;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamPVPFree;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamPVPFreeDetail;
import com.kaihei.esportingplus.trade.domain.service.PVPFreeInComeService;
import com.kaihei.esportingplus.trade.domain.service.PVPFreeOrderService;
import com.kaihei.esportingplus.trade.enums.BusinessTypeEnum;
import com.kaihei.esportingplus.trade.enums.PayMentTypeEnum;
import com.kaihei.esportingplus.user.api.enums.UserPointItemType;
import com.kaihei.esportingplus.user.api.feign.UserPointServiceClient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PVP免费车队service
 *
 * @author zhangfang
 */
@Service("pvpFreeOrderService")
public class PVPFreeOrderServiceImpl extends AbstractOrderService implements PVPFreeOrderService {

    @Autowired
    private OrderItemTeamPVPFreeRepository pvpFreeRepository;

    @Autowired
    private OrderItemTeamPVPFreeDetailRepository pvpFreeDetailRepository;

    @Autowired
    private UserPointServiceClient userPointServiceClient;

    @Autowired
    private PVPFreeInComeService freeInComeService;

    @Autowired
    private PVPFreeTeamServiceClient pvpFreeTeamServiceClient;

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTeamStartOrder(PVPFreeTeamStartOrderVO pvpFreeTeamStartOrderVO) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, pvpFreeTeamStartOrderVO,
                pvpFreeTeamStartOrderVO.getMemberInfos());
        //判断这个车队的订单是否已经被创建，如果被创建，则不执行
        String idempotentKey = String
                .format(RedisKey.CREATE_BAOJI_ORDER_LOCK_KEY, pvpFreeTeamStartOrderVO.getGameCode(),
                        pvpFreeTeamStartOrderVO.getSequence());
        try {
            String result = cacheManager
                    .set(idempotentKey, pvpFreeTeamStartOrderVO.getSequence(),
                            RedisKey.SET_IF_NOT_EXIST,
                            RedisKey.SET_WITH_EXPIRE_TIME,
                            CommonConstants.ONE_HOUR_SECONDS * 1000);
            ValidateAssert.isTrue(RedisKey.FIND_SUCCESS.equals(result),
                    BizExceptionEnum.TEAM_BAOJI_ORDER_ALREADY_CREATE);
            //创建暴鸡订单
            this.createPvpFreeOrders(pvpFreeTeamStartOrderVO);
        } catch (Exception e) {
            cacheManager.del(idempotentKey);
            throw e;
        }
    }

    @Override
    public PVPFreeTeamOrderVo getOrderDetailsByUidAndTeamSequence(String uid, String teamSequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid, teamSequence);
/*        if (!cacheManager.exists(RedisKey.UPDATED_ORDER + teamSequence)) {
            LOGGER.info(">>订单详情没有检测到redisKey:[" + RedisKey.UPDATED_ORDER + teamSequence + "]");
            throw new BusinessException(BizExceptionEnum.ORDER_NOT_SETTLE_COMPLETE);
        }*/
        Order order = orderRepository.selectPVPFreeUserOrder(uid, teamSequence);
        return doMadeTeamOrderDetails(uid, order);
    }

    @Override
    public PVPFreeTeamOrderVo getOrderDetailsBySequence(String uid, String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid, sequence);
        Order order = orderRepository.getPvpFreeOrderBySequenceId(sequence);
        return doMadeTeamOrderDetails(uid, order);
    }

    private PVPFreeTeamOrderVo doMadeTeamOrderDetails(String uid, Order order) {
        ValidateAssert.hasNotNull(BizExceptionEnum.ORDER_NOT_EXIST, order);
        ValidateAssert.isTrue(uid.equals(order.getUid()), BizExceptionEnum.OPERATE_PERSON_DIFFER);
        ValidateAssert
                .hasNotNull(BizExceptionEnum.ORDER_NOT_EXIST, order.getOrderItemTeamPVPFree());
        //2：第二步，拿到车队订单序列号,并通过订单序列号查询出其它人的信息
        String teamSequence = order.getOrderItemTeamPVPFree().getTeamSequeue();
        List<Order> otherOrders = orderRepository
                .selectPVPFreeTeamOtherOrders(teamSequence, order.getSequeue());
        return createOrderDetails(order, otherOrders);
    }

    private PVPFreeTeamOrderVo createOrderDetails(Order order, List<Order> otherOrders) {
        OrderItemTeamPVPFree owerOrderIteam = order.getOrderItemTeamPVPFree();
        PVPFreeTeamOrderVo teamOrderVo = new PVPFreeTeamOrderVo();
        // 查找比赛结果
        ResponsePacket<List<TeamGameResultVO>> resultResponse = pvpFreeTeamServiceClient
                .getTeamGameResult(null, owerOrderIteam.getTeamSequeue());
        ValidateAssert.isTrue(resultResponse.responseSuccess(), BizExceptionEnum.TEAM_NOT_EXIST);
        teamOrderVo.setStatusZh(
                OrderStatusEnum.fromCode(order.getStatus().intValue()).getFreeFrontZh());
        teamOrderVo.setSequence(order.getSequeue());
        teamOrderVo.setTeamSequence(owerOrderIteam.getTeamSequeue());
        teamOrderVo.setUserIdentity(owerOrderIteam.getUserIdentity());
        //拼接订单中文信息
        teamOrderVo.setOrderInfoZh(
                owerOrderIteam.getFreeTeamTypeName() + "|" + owerOrderIteam
                        .getGameZoneName() + "|" + owerOrderIteam.getSettlementNumber()
                        .stripTrailingZeros().toPlainString() + "|" + SettlementTypeEnum
                        .getByCode(owerOrderIteam.getSettlementType()
                        ).getDesc());
        teamOrderVo.setStatus(order.getStatus());
       teamOrderVo.setGameResultZh(resultResponse.getData().get(0).getGameResultDesc());
        List<PVPFreeTeamMemberOrderVO> memberOrders = buildTeamMemberOrderVO(order,otherOrders);
        teamOrderVo.setOrders(memberOrders);
        //根据身份和订单状态，如果是暴鸡，订单处于服务中时，计算预计收益，如果是已完成，计算实际收益，先过滤出所有老板,
        //注意的是，因为只统计查询中是暴鸡，所以老板都在otherOrders里面
        List<PVPFreeBossPointsVO> freeBossPointsVOS = buildBossPointVO(order, otherOrders);
        if(ObjectTools.isNotEmpty(freeBossPointsVOS)){
            teamOrderVo.setBossPoints(freeBossPointsVOS);
            teamOrderVo.setAmount(freeBossPointsVOS.stream().map(it->it.getAmount()).reduce(0,Integer::sum));
        }
        return teamOrderVo;
    }
    private List<PVPFreeBossPointsVO> buildBossPointVO(Order order,List<Order> otherOrders){
        OrderItemTeamPVPFree owerOrderIteam = order.getOrderItemTeamPVPFree();
        List<PVPFreeBossPointsVO> freeBossPointsVOS = null;
        List<Order> bossOrders = otherOrders.stream()
                .filter(it -> it.getOrderItemTeamPVPFree().getUserIdentity()
                        == UserIdentityEnum.BOSS.getCode()).collect(Collectors.toList());
        if (owerOrderIteam.getUserIdentity() != UserIdentityEnum.BOSS.getCode()
                && order.getStatus() != OrderStatusEnum.READY.getCode()) {
            //暴鸡处于已完成或者已取消状态
            List<String> bossUids = bossOrders.stream().map(it -> it.getUid())
                    .collect(Collectors.toList());
            //查询对应暴鸡的实际收入
            List<OrderItemTeamPVPFreeDetail> baojiIncomeDetails = pvpFreeDetailRepository
                    .findBaojiIncomeDetails(order.getUid(), owerOrderIteam.getTeamSequeue(),
                            bossUids);
            freeBossPointsVOS = bossOrders.stream().map(it -> {
                return PVPFreeBossPointsVO.builder().amount(baojiIncomeDetails.stream()
                        .filter(bd -> it.getUid().equals(bd.getBossUid())).findFirst()
                        .orElse(OrderItemTeamPVPFreeDetail.builder().income(0).build()).getIncome())
                        .avatar(it.getOrderItemTeamPVPFree().getAvatar())
                        .username(
                                it.getOrderItemTeamPVPFree().getUserNickname())
                        .build();
            }).collect(Collectors.toList());
        }else if(owerOrderIteam.getUserIdentity() != UserIdentityEnum.BOSS.getCode()
                && order.getStatus() == OrderStatusEnum.READY.getCode()){
            //调用收益计算接口
            List<PVPFreeBaseVO> freeBaseVOS = bossOrders.stream().map(it -> {
                return PVPFreeBaseVO.builder().uid(it.getUid())
                        .gameDanId(it.getOrderItemTeamPVPFree().getGameDanId()).build();
            }).collect(Collectors.toList());
            ChickenPointIncomeParams chickenPointIncomeParams = new ChickenPointIncomeParams();
            chickenPointIncomeParams.setFreeTeamTypeId(owerOrderIteam.getFreeTeamTypeId());
            chickenPointIncomeParams.setBaojiLevel(owerOrderIteam.getUserBaojiLevel());
            chickenPointIncomeParams.setGameResultCode(SettlementTypeEnum.ROUND.getCode()==owerOrderIteam.getSettlementType()?GameResultEnum.ROUNDS_VICTORY.getCode():GameResultEnum.HOURS_PLAYED.getCode());
            chickenPointIncomeParams.setTeamStatus(OrderTeamStatus.ALREADY_STATUS.getCode());
            chickenPointIncomeParams.setPvpFreeBossVOS(freeBaseVOS);
            PVPFreePreIncomeVo chickenPointIncome = freeInComeService
                    .getChickenPointIncome(chickenPointIncomeParams, null);
            freeBossPointsVOS =chickenPointIncome.getFreeTeamMembersIncomes().stream().map(it->{
                Order bossOrder = bossOrders.stream().filter(bo -> bo.getUid().equals(it.getUid()))
                        .findFirst().get();
                return PVPFreeBossPointsVO.builder().amount(it.getPrice()).avatar(bossOrder.getOrderItemTeamPVPFree().getAvatar())
                        .username(bossOrder.getOrderItemTeamPVPFree().getUserNickname()).build();
            }).collect(Collectors.toList());
        }
        return freeBossPointsVOS;
    }
    private List<PVPFreeTeamMemberOrderVO> buildTeamMemberOrderVO(Order order, List<Order> otherOrders){
        OrderItemTeamPVPFree owerOrderIteam = order.getOrderItemTeamPVPFree();
        List<PVPFreeTeamMemberOrderVO> memberOrders = new ArrayList<>();
        //组装所有用户列表
        // 下面组装其它队员信息列表
        for (Order otherOrder : otherOrders) {
            OrderItemTeamPVPFree otherOrderItemTeamPVP = otherOrder.getOrderItemTeamPVPFree();
            if (otherOrderItemTeamPVP == null) {
                continue;
            }
            memberOrders.add(buildMemberOrder(otherOrderItemTeamPVP.getUid(),
                    otherOrder.getOrderItemTeamPVPFree().getUserNickname(),otherOrder.getOrderItemTeamPVPFree().getAvatar(), otherOrderItemTeamPVP.getUserBaojiLevel(),
                    otherOrderItemTeamPVP.getUserIdentity(), otherOrder.getStatus()));

        }
        //因为排除了自己，所以再把自己加进去
        memberOrders.add(buildMemberOrder(order.getUid(), owerOrderIteam.getUserNickname(),owerOrderIteam.getAvatar(),
                owerOrderIteam.getUserBaojiLevel(), owerOrderIteam.getUserIdentity(),
                order.getStatus()));
        sortPvpOtherList(memberOrders);
        return memberOrders;
    }
    private PVPFreeTeamMemberOrderVO buildMemberOrder(String uid, String username,String avatar,
            Integer baojiLevel, Byte userIdentity, Byte orderStatus) {
        BaojiLevelEnum levelEnum = BaojiLevelEnum.getByCode(baojiLevel);
        return PVPFreeTeamMemberOrderVO.builder().uid(uid)
                .username(username)
                .avatar(avatar)
                .baojiLevelZh(levelEnum == null ? "" : levelEnum.getFrontZh())
                .userIdentity(userIdentity).baojiLevel(baojiLevel)
                .statusZh(OrderStatusEnum.fromCode(orderStatus.intValue()).getFreeFrontZh())
                .build();
    }

    private void sortPvpOtherList(List<PVPFreeTeamMemberOrderVO> otherVos) {
        Collections.sort(otherVos, new Comparator<PVPFreeTeamMemberOrderVO>() {
            @Override
            public int compare(PVPFreeTeamMemberOrderVO o1, PVPFreeTeamMemberOrderVO o2) {
                if (o1.getUserIdentity().compareTo(o2.getUserIdentity()) > 0) {
                    return -1;
                } else if (o1.getUserIdentity().equals(o2.getUserIdentity())) {
                    return 0;
                }
                return 1;
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderAndSendInCome(PVPFreeTeamEndOrderMessage message) {
        String teamSequence = message.getTeamSequence();
        Integer gameResultCode = message.getGameResultCode();
        Integer victoryRounds = GameResultEnum.fromCode(gameResultCode) == ROUNDS_VICTORY ? 1 : 0;
        Integer defeatRounds = GameResultEnum.fromCode(gameResultCode) == ROUNDS_DEFEAT ? 1 : 0;
        Integer playedRounds = GameResultEnum.fromCode(gameResultCode) == HOURS_PLAYED ? 1 : 0;
        Integer noPlayRounds = GameResultEnum.fromCode(gameResultCode) == HOURS_NOT_PLAY
                || GameResultEnum.fromCode(gameResultCode) == ROUNDS_NOT_PLAY ? 1 : 0;
        //更新订单状态为已完成
        byte orderStatus = (byte) OrderStatusEnum.FINISH.getCode();

        //获取免费车队队员订单列表
        List<Order> orders = orderRepository.getByTeamSequeue(teamSequence);
        if (CollectionUtils.isEmpty(orders)) {
            LOGGER.error("PVP免费车队队员订单列表为空，取消更新和发送鸡分收益，teamSequence:{}"
                    , teamSequence);
            return;
        }

        List<Order> baojis = orders.stream()
                .filter(f -> f.getOrderItemTeamPVPFree().getUserIdentity().intValue()
                        == UserIdentityEnum.LEADER.getCode())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(baojis)) {
            LOGGER.error("PVP免费车队[{}],连队长都没有你敢信？取消处理呗我还能咋样。"
                    , teamSequence);
            return;
        }
        List<Order> bosss = orders.stream()
                .filter(f -> f.getOrderItemTeamPVPFree().getUserIdentity().intValue()
                        == UserIdentityEnum.BOSS.getCode())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bosss)) {
            LOGGER.error("PVP免费车队[{}],没老板都没得，取消处理。"
                    , teamSequence);
            return;
        }

        //免费车队只有一个暴鸡:也就是队长
        for (Order baojiOrder : baojis) {
            OrderItemTeamPVPFree orderItemTeamPVPFree = baojiOrder.getOrderItemTeamPVPFree();
            Integer baojiLevel = orderItemTeamPVPFree.getUserBaojiLevel();
            Integer freeTeamTypeId = orderItemTeamPVPFree.getFreeTeamTypeId();
            String uid = orderItemTeamPVPFree.getUid();

            //获取鸡分收益
            List<PVPFreeBaseVO> pvpFreeBossVOS = bosss.stream()
                    .map(m->{
                        PVPFreeBaseVO baseVO = new PVPFreeBaseVO();
                        baseVO.setUid(m.getUid());
                        baseVO.setGameDanId(m.getOrderItemTeamPVPFree().getGameDanId());
                        return baseVO;
                    })
                    .collect(Collectors.toList());

            ChickenPointIncomeParams incomeParams = ChickenPointIncomeParams.builder()
                    .freeTeamTypeId(freeTeamTypeId)
                    .baojiLevel(baojiLevel)
                    .gameResultCode(gameResultCode)
                    .pvpFreeBossVOS(pvpFreeBossVOS)
                    .teamStatus(message.getTeamStatus())
                    .build();

            //老板的更新勾子
            Consumer<Map<Integer, Integer>> bossUpdateHook = (x) -> {
                //批量更新pvp_free表的老板price
                for (Order boss : bosss) {
                    OrderItemTeamPVPFree bossFree = boss.getOrderItemTeamPVPFree();
                    bossFree.setPrice(x.get(bossFree.getGameDanId()));

                    //更新为已完成
                    Order order = new Order();
                    order.setId(bossFree.getOrderId());
                    order.setOrderItemTeamPVPFree(bossFree);
                    order.setStatus(orderStatus);
                    UpdateOrderParams orderParams = UpdateOrderParams.builder()
                            .order(order)
                            .neddRefund(false)
                            .memberStatus(message.getTeamStatus())
                            .build();
                    //更新订单状态
                    updateOrder(orderParams);
                    //上报老板神策：订单状态更新了
                    HashMap<String, Object> statusData = new HashMap<>(9);
                    statusData.put("order", boss.getSequeue());//订单号
                    statusData.put("team", teamSequence);//车队id
                    statusData.put("room", message.getRoomNum());//房间id
                    statusData.put("state", boss.getStatus());//变更前状态
                    statusData.put("status", orderStatus);//变更后状态
                    statusData.put("game_type", message.getGameId());//游戏id
                    statusData.put("game_zone", bossFree.getGameZoneId());//游戏大区
                    statusData.put("game_dan", bossFree.getGameDanId());//游戏段位
                    statusData.put("quantity", victoryRounds);//胜利局数
                    statusData.put("counts", defeatRounds);//失败局数
                    statusData.put("amount", noPlayRounds);//没打局数
                    sensorsAnalyticsService.track(uid, SensorsEventEnum.
                            ORDER_STATUSCHANGE_FREETEAM.getCode(), statusData);
                    LOGGER.debug("上报神策成功,入参:{}",statusData);
                }
                //插入更新pvp_free_detail明细
                bosss.stream()
                        .map(m -> OrderItemTeamPVPFreeDetail.builder()
                                .bossUid(m.getUid())
                                .baojiUid(uid)
                                .teamSequence(m.getOrderItemTeamPVPFree().getTeamSequeue())
                                //找到段位一样设置price
                                .income(x.get(m.getOrderItemTeamPVPFree().getGameDanId()))
                                .build()).forEach(pvpFreeDetailRepository::insertSelective);
            };
            PVPFreePreIncomeVo inComdePoint = freeInComeService
                    .getChickenPointIncome(incomeParams, bossUpdateHook);

            //发送鸡分收益到用户服务
            if (inComdePoint != null) {
                //有收益了，更新暴鸡收益
                Integer totalIncome = inComdePoint.getTotalIncome();
                OrderItemTeamPVPFree baojiPvpFrees = OrderItemTeamPVPFree.builder()
                        .id(orderItemTeamPVPFree.getId())
                        .price(totalIncome)
                        .teamStatus(message.getTeamStatus().byteValue())
                        .build();
                pvpFreeRepository.updateByPrimaryKeySelective(baojiPvpFrees);
                //上报暴鸡神策：订单状态更新了
                HashMap<String,Object> statusData = new HashMap<>(9);
                statusData.put("order",baojiOrder.getSequeue());//订单号
                statusData.put("team",teamSequence);//车队id
                statusData.put("room",message.getRoomNum());//房间id
                statusData.put("state",baojiOrder.getStatus());//变更前状态
                statusData.put("status",orderStatus);//变更后状态
                statusData.put("game_type",message.getGameId());//游戏id
                statusData.put("game_zone",orderItemTeamPVPFree.getGameZoneId());//游戏大区
                statusData.put("game_dan",orderItemTeamPVPFree.getGameDanId());//游戏段位
                statusData.put("quantity",victoryRounds);//胜利局数
                statusData.put("counts",defeatRounds);//失败局数
                statusData.put("amount",noPlayRounds);//没打局数
                statusData.put("identity",orderItemTeamPVPFree.getUserIdentity());//服务者身份
                //暴鸡等级：100普通/101优选/102超级/300暴娘/-1无
                statusData.put("level",baojiLevel);
                sensorsAnalyticsService.track(uid, SensorsEventEnum.
                        ORDER_STATUSCHANGE_FREETEAM.getCode(),statusData);
                LOGGER.debug("上报神策成功,入参:{}",statusData);

                //发送收益
                LOGGER.info("暴鸡[{}]赚到了{}鸡分，发送收益到用户服务"
                        , uid, totalIncome);
                ResponsePacket<Void> incrPoint = userPointServiceClient.incrPoint(uid, totalIncome,
                        UserPointItemType.TEAM_DRIVE.getCode(), teamSequence);

                if (incrPoint.getCode() != BizExceptionEnum.SUCCESS.getErrCode()) {
                    LOGGER.error("发送收益[{}]到用户[{}]异常.{}",
                            inComdePoint, uid, incrPoint.toString());
                }else{
                    //上报暴鸡神策: 鸡分收益 TODO
                    HashMap<String,Object> incomeData = new HashMap<>(9);
                    statusData.put("type","1");//获得方式：1完成免费车队订单；2免费车队订单5星好评
                    statusData.put("team",teamSequence);//车队id
                    statusData.put("room",message.getRoomNum());//房间id
                    statusData.put("score",totalIncome);//鸡分数量
                    sensorsAnalyticsService.track(uid, SensorsEventEnum.GET_POINT_FREETEAM.getCode(),incomeData);
                }
            } else {
                LOGGER.error("系统中无对应的鸡分配置,暴鸡结算的收益为0，取消发送鸡分收益到用户服务,"
                        + "参数:{}", incomeParams.toString());
            }
        }
    }

    @Override
    public PagingResponse<PVPFreeOrderListVO> selectUserBossOrdersByPage(int offset,
            int limit) {
        UserSessionContext userDetail = UserSessionContext.getUser();
        Page<Order> page = PageHelper
                .startPage(offset, limit)
                .doSelectPage(() -> orderRepository.selectPVPFreeBossOrders(userDetail.getUid()));
        List<PVPFreeOrderListVO> bossOrderListVos = combineBossOrderListVos(page.getResult(),
                userDetail);
        PagingResponse<PVPFreeOrderListVO> pagingResponse = new PagingResponse<PVPFreeOrderListVO>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), bossOrderListVos);
        return pagingResponse;
    }

    @Override
    public PagingResponse<PVPFreeOrderListVO> selectUserBaojiOrdersByPage(int offset, int limit) {
        UserSessionContext userDetail = UserSessionContext.getUser();
        Page<Order> page = PageHelper
                .startPage(offset, limit)
                .doSelectPage(() -> orderRepository.selectPVPFreeBaojiOrders(userDetail.getUid()));
        List<PVPFreeOrderListVO> bossOrderListVos = combineBossOrderListVos(page.getResult(),
                userDetail);
        PagingResponse<PVPFreeOrderListVO> pagingResponse = new PagingResponse<PVPFreeOrderListVO>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), bossOrderListVos);
        return pagingResponse;
    }

    @Override
    public List<PVPFreeOrderIncomeForBackGroundVO> selectPVPFreeBossGiveBaojiIncome(
            String sequence) {
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        return orderRepository.selectPVPFreeBossGiveBaojiIncome(sequence);
    }

    @Override
    public List<PVPFreeOrderIncomeForBackGroundVO> selectPVPFreeBaojiFromBossIncome(
            String sequence) {
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, sequence);
        return orderRepository.selectPVPFreeBaojiFromBossIncome(sequence);
    }

    @Override
    public PagingResponse<PVPFreeBossOrderForBackGroundVO> selectPvpFreeBossOrderForBackGroundVO(
            PVPFreeOrdersForBackGroundParams params) {
        Page<PVPFreeBossOrderForBackGroundVO> page = PageHelper
                .startPage(params.getOffset(), params.getLimit())
                .doSelectPage(
                        () -> orderRepository.selectPVPFreeBossOrderListForBackGround(params));
        PagingResponse<PVPFreeBossOrderForBackGroundVO> pagingResponse = new PagingResponse<PVPFreeBossOrderForBackGroundVO>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), page.getResult());
        return pagingResponse;
    }

    @Override
    public PagingResponse<PVPFreeBossOrderForBackGroundVO> selectPvpFreeBaojiOrderForBackGroundVO(
            PVPFreeOrdersForBackGroundParams params) {
        Page<Order> page = PageHelper.startPage(params.getOffset(), params.getLimit())
                .doSelectPage(
                        () -> orderRepository.selectPVPFreeBaojiOrderListForBackGround(params));
        PagingResponse<PVPFreeBossOrderForBackGroundVO> pagingResponse = new PagingResponse<PVPFreeBossOrderForBackGroundVO>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), convertToBossOrderForBackGroundVOList(page.getResult()));
        return pagingResponse;
    }

    private List<PVPFreeBossOrderForBackGroundVO> convertToBossOrderForBackGroundVOList(
            List<Order> result) {
        return result.stream().map(it -> {
            OrderItemTeamPVPFree orderItemTeamPVPFree = it.getOrderItemTeamPVPFree();
            PVPFreeBossOrderForBackGroundVO vo = new PVPFreeBossOrderForBackGroundVO();
            vo.setSequence(it.getSequeue());
            vo.setTeamSequence(orderItemTeamPVPFree.getTeamSequeue());
            vo.setChickenId(orderItemTeamPVPFree.getUserChickenId());
            vo.setUid(orderItemTeamPVPFree.getUid());
            vo.setNickname(orderItemTeamPVPFree.getUserNickname());
            vo.setAmount(orderItemTeamPVPFree.getPrice());
            vo.setFreeTeamTypeTame(orderItemTeamPVPFree.getFreeTeamTypeName());
            vo.setGmtCreate(it.getGmtCreate());
            vo.setGmtCreate(it.getGmtModified());
            vo.setStatus(it.getStatus().intValue());
            return vo;
        }).collect(Collectors.toList());
    }

    private List<PVPFreeOrderListVO> combineBossOrderListVos(List<Order> result,
            UserSessionContext userDetail) {
        UserInfoVo userInfoVo = BeanMapper.map(userDetail, UserInfoVo.class);
        List<PVPFreeOrderListVO> list = new ArrayList<PVPFreeOrderListVO>();
        PVPFreeOrderListVO orderListVo = null;
        for (int i = 0; i < result.size(); i++) {
            orderListVo = new PVPFreeOrderListVO();
            Order order = result.get(i);
            OrderItemTeamPVPFree orderItemTeamPVPFree = order.getOrderItemTeamPVPFree();
            PVPFreeOrderVo pvpOrderVo = new PVPFreeOrderVo();
            pvpOrderVo.setOrderInfoZh(
                    orderItemTeamPVPFree.getFreeTeamTypeName() + "|" + orderItemTeamPVPFree
                            .getGameZoneName() + "|" + orderItemTeamPVPFree.getSettlementNumber()
                            .stripTrailingZeros().toPlainString() + "|" + SettlementTypeEnum
                            .getByCode(orderItemTeamPVPFree.getSettlementType()
                            ).getDesc());
            pvpOrderVo.setPlayModeName(
                    PlayModeEnum.getByCode(orderItemTeamPVPFree.getPlayMode()).getDesc());
            pvpOrderVo.setSequeue(order.getSequeue());
            pvpOrderVo.setGmtCreate(order.getGmtCreate());
            pvpOrderVo.setStatusZh(OrderStatusEnum.fromCode(order.getStatus()).getFreeFrontZh());
            orderListVo.setUserInfo(userInfoVo);
            orderListVo.setOrder(pvpOrderVo);
            //这里还需要查询车队人员头像
            List<OrderItemTeamPVPFree> orderItemTeamPVPFrees = pvpFreeRepository
                    .getByTeamSequence(orderItemTeamPVPFree.getTeamSequeue());
            List<String> memberAvatars = orderItemTeamPVPFrees.stream().map(it -> it.getAvatar())
                    .collect(Collectors.toList());
            pvpOrderVo.setAvatars(memberAvatars);
            list.add(orderListVo);
        }
        return list;
    }

    private void createPvpFreeOrders(PVPFreeTeamStartOrderVO pvpFreeTeamStartOrderVO) {
        //生成时间戳
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        String timeStamp = df.format(calendar.getTime());
        List<Order> list = new ArrayList<>();
        pvpFreeTeamStartOrderVO.getMemberInfos().forEach(it -> {
            Order order = new Order();
            order.setUid(it.getUid());
            order.setSequeue(TradeConstants.ORDER_ID_PREFIEX + timeStamp + String.valueOf(snowFlake.nextId()));
            order.setBusinessType((byte) BusinessTypeEnum.TEAM_ORDER.getCode());//订单业务类型
            order.setPrepaidAmount(0);//预付金额
            order.setActualPaidAmount(0);//实付金额
            order.setDiscountAmount(0);
            order.setPaymentType((byte) PayMentTypeEnum.WEIXIN_PAY.getCode());//支付方式
            if (UserIdentityEnum.BOSS.getCode() == it.getUserIdentity()) {
                order.setStatus((byte) OrderStatusEnum.PAYED.getCode());//订单状态
            } else {
                order.setStatus((byte) OrderStatusEnum.READY.getCode());//订单状态
            }
            orderRepository.insertOrder(order);

            OrderItemTeamPVPFree orderItemTeamPVPFree = BeanMapper
                    .map(it, OrderItemTeamPVPFree.class);
            orderItemTeamPVPFree.setTeamSequeue(pvpFreeTeamStartOrderVO.getSequence());
            orderItemTeamPVPFree.setFreeTeamTypeId(pvpFreeTeamStartOrderVO.getFreeTeamTypeId());
            orderItemTeamPVPFree.setFreeTeamTypeName(pvpFreeTeamStartOrderVO.getFreeTeamTypeName());
            orderItemTeamPVPFree.setGameZoneId(pvpFreeTeamStartOrderVO.getGameZoneId());
            orderItemTeamPVPFree.setGameZoneName(pvpFreeTeamStartOrderVO.getGameZoneName());
            orderItemTeamPVPFree.setUserBaojiLevel(it.getBaojiLevel());
            orderItemTeamPVPFree.setPrice(0);
            orderItemTeamPVPFree.setTeamStatus((byte) OrderTeamStatus.ALREADY_STATUS.getCode());
            orderItemTeamPVPFree.setUserChickenId(it.getUserChickenId());
            orderItemTeamPVPFree.setUserNickname(it.getUserNickname());
            orderItemTeamPVPFree.setAvatar(it.getAvatar());
            orderItemTeamPVPFree.setGameDanId(it.getGameDanId());
            orderItemTeamPVPFree.setGameDanName(it.getGameDanName());
            orderItemTeamPVPFree.setOrderId(order.getId());
            orderItemTeamPVPFree.setPlayMode(pvpFreeTeamStartOrderVO.getPlayMode());
            orderItemTeamPVPFree.setSettlementType(pvpFreeTeamStartOrderVO.getSettlementType());
            orderItemTeamPVPFree.setSettlementNumber(pvpFreeTeamStartOrderVO.getSettlementNumber());
            pvpFreeRepository.insertSelective(orderItemTeamPVPFree);
        });
    }

    @Override
    public boolean updateOrderForLeaveTeam(PVPTeamLeaveMemberAfterStartedVO message) {
        String teamSequence = message.getTeamSequence();
        String uid = message.getTeamMemberVO().getUid();

        List<Order> orders = getByTeamSequenceIdAndUids(teamSequence,
                Arrays.asList(uid), false);

        if(CollectionUtils.isEmpty(orders)){
            LOGGER.info(">>找不到订单信息,忽略处理。"
                    + "车队:{},uid:{}",teamSequence,uid);
            return true;
        }
        Order orderOld = orders.get(0);
        OrderItemTeamPVPFree orderItemTeamPVPFree = orderOld.getOrderItemTeamPVPFree();

        //更新订单中间态:取消(外显为服务中退出)
        byte tobeUpdateStatus = (byte) OrderStatusEnum.PAY_CANCEL.getCode();
        //只更新需要的字段
        Order tobeUpdateOrder = new Order();
        tobeUpdateOrder.setId(orderOld.getId());
        tobeUpdateOrder.setStatus(tobeUpdateStatus);
        UpdateOrderParams orderParams = UpdateOrderParams.builder()
                .order(tobeUpdateOrder)
                .neddRefund(false)
                .memberStatus(message.getUserStatus())
                .build();
        updateOrder(orderParams);

        //上报老板神策：订单状态更新了
        HashMap<String,Object> statusData = new HashMap<>(9);
        statusData.put("order",orderOld.getSequeue());//订单号
        statusData.put("team",teamSequence);//车队id
        statusData.put("room",message.getRoomNum());//房间id
        statusData.put("state", orderOld.getStatus());//变更前状态
        statusData.put("status",tobeUpdateStatus);//变更后状态
        statusData.put("game_type",message.getGameId());//游戏id
        statusData.put("game_zone",orderItemTeamPVPFree.getGameZoneId());//游戏大区
        statusData.put("game_dan",orderItemTeamPVPFree.getGameDanId());//游戏段位
        sensorsAnalyticsService.track(uid, SensorsEventEnum.
                ORDER_STATUSCHANGE_FREETEAM.getCode(),statusData);
        return true;
    }

    @Override
    public BizExceptionEnum checkTeamMemberPayed(String orderSequence) {
        return null;
    }
}
