package com.kaihei.esportingplus.trade.domain.service.impl;

import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.PAID_DISMISSD;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.PAID_KICK;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.PAID_QUIT;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.PREPARED_DISMISSD;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.PREPARED_KICK;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.PREPARED_QUIT;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.PREPARE_DISMISSD;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.PREPARE_KICK;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.PREPARE_QUIT;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.TEAM_STARTED_DISMISSD;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.TEAM_STARTED_FINISH;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum.TEAM_STARTED_QUIT;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.enums.PlayModeEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.lock.RedisLock;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPBossInfoForOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPTeamStartOrderVO;
import com.kaihei.esportingplus.payment.api.enums.PayChannelEnum;
import com.kaihei.esportingplus.payment.api.feign.TradeServiceClient;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;
import com.kaihei.esportingplus.payment.api.params.PayOrderParams;
import com.kaihei.esportingplus.trade.api.enums.OrderStatusEnum;
import com.kaihei.esportingplus.trade.api.enums.OrderTeamStatus;
import com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum;
import com.kaihei.esportingplus.trade.api.enums.WxPayStatusEnum;
import com.kaihei.esportingplus.trade.api.params.GameResultPVPParams;
import com.kaihei.esportingplus.trade.api.params.OrderTeamPVPMember;
import com.kaihei.esportingplus.trade.api.params.PVPBossCurrentPaidAmountParams;
import com.kaihei.esportingplus.trade.api.params.PVPBossOrderCreateParams;
import com.kaihei.esportingplus.trade.api.params.PVPInComeParams;
import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusPVPParams;
import com.kaihei.esportingplus.trade.api.vo.InComeVo;
import com.kaihei.esportingplus.trade.api.vo.PVPBaojiOrderListVO;
import com.kaihei.esportingplus.trade.api.vo.PVPBaojiOrderVO;
import com.kaihei.esportingplus.trade.api.vo.PVPBossOrderListVO;
import com.kaihei.esportingplus.trade.api.vo.PVPBossOrderVO;
import com.kaihei.esportingplus.trade.api.vo.PVPGameResult;
import com.kaihei.esportingplus.trade.api.vo.PVPTeamMemberOrderVO;
import com.kaihei.esportingplus.trade.api.vo.PVPTeamOrderVo;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;
import com.kaihei.esportingplus.trade.api.vo.UserInfoVo;
import com.kaihei.esportingplus.trade.common.IncomeCaculateParams;
import com.kaihei.esportingplus.trade.common.ProfitCheckParams;
import com.kaihei.esportingplus.trade.common.UpdateOrderParams;
import com.kaihei.esportingplus.trade.common.UpdateOrderParams.UpdateOrderParamsBuilder;
import com.kaihei.esportingplus.trade.data.repository.OrderItemTeamPVPRepository;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamPVP;
import com.kaihei.esportingplus.trade.domain.service.PVPInComeService;
import com.kaihei.esportingplus.trade.domain.service.PVPOrderService;
import com.kaihei.esportingplus.trade.enums.GameOrderType;
import com.kaihei.esportingplus.trade.event.SendProfitEvent;
import com.kaihei.esportingplus.trade.handler.ActionHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pvpOrderService")
public class PVPOrderServiceImpl extends AbstractOrderService implements PVPOrderService {

    @Autowired
    private OrderItemTeamPVPRepository orderItemTeamPVPRepository;

    @Value("${python2java.payNofity}")
    private String payNotify;

    @Autowired
    private TradeServiceClient tradeServiceClient;

    @Autowired
    private PVPInComeService pvpInComeService;

    @Value("${python.order.retryInterval}")
    private String orderRetryInterval;

    @Value("${pay.refund.param.order_type}")
    private int order_type;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(keyFormate = "BossOrder:teamSequence:$.orderParams.teamSequence:uid:$.uid", expireTime = 2000)
    public Map<String, String> createTeamBossOrder(PVPBossOrderCreateParams orderParams,
            String ip) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, orderParams);
        //获取当前请求用户
        UserSessionContext user = UserSessionContext.getUser();
        String uid = user.getUid();

        //TODO 从车队服务获取车队及老板和支付价格
        PVPBossInfoForOrderVO PVPBossInfoForOrderVO = new PVPBossInfoForOrderVO();
        Order order = this.checkAndSaveBossOrder(orderParams.getTeamSequence(), user,
                orderParams.getCouponId(),
                PVPBossInfoForOrderVO);
        //向支付系统发起支付请求,组装参数
        PayOrderParams payOrderParams = new PayOrderParams();
        payOrderParams.setUserId(uid);
        payOrderParams.setDescription("付费车队支付");
        payOrderParams.setOrderType("17");
        payOrderParams.setOutTradeNo(order.getSequeue());
        payOrderParams.setSubject("用户付费车队支付");
        payOrderParams.setTotalAmount(order.getPrepaidAmount() - order.getDiscountAmount());
        payOrderParams
                .setBody("用户付费车队支付" + (order.getPrepaidAmount() - order.getDiscountAmount()) + "分");
        payOrderParams.setNotifyUrl(payNotify);
        payOrderParams.setCurrencyType("CN");
        payOrderParams.setIp(ip);
        LOGGER.info("老板[{}]为了能拿到付费车队的船票，向支付系统发起支付，金额:[{}]分", user.getUsername(),
                payOrderParams.getTotalAmount());
        ResponsePacket<Map<String, String>> orderCreateResponsePacket = tradeServiceClient
                .create(payOrderParams, orderParams.getAppId(), orderParams.getChannerTag());
        Map<String, String> data = orderCreateResponsePacket.getData();
        if (!orderCreateResponsePacket.responseSuccess()) {
            LOGGER.warn("呀！老板[{}]向支付系统请求支付失败，友谊的小船已经翻了，失败原因:{}", user.getUsername(),
                    orderCreateResponsePacket.getMsg());
            throw new BusinessException(orderCreateResponsePacket.getCode(),
                    orderCreateResponsePacket.getMsg());
        }
        if(orderParams.getChannerTag().equals(PayChannelEnum.WALLET_PAY.getValue())){
            //如果是暴鸡币支付，直接置位已支付
            order.setStatus((byte)OrderStatusEnum.PAYED.getCode());
            orderRepository.updateSelectiveBySequenceId(order);
        }
        data.put("trade_order_id", order.getSequeue());
        return data;
    }

    private Order checkAndSaveBossOrder(String teamSequence, UserSessionContext userSessionContext,
            List<Long> couponIds,
            PVPBossInfoForOrderVO pvpBossInfoForOrderVO) {
        Order order = orderRepository
                .selectPVPBossOrderByTeamSequenceAndUid(teamSequence, userSessionContext.getUid());
        //如果订单存在，则对下面几种情况进行判定
        /***
         *  比较老板当前预支付金是否和订单状态为2，4的支付金一致， 如果一致，则抛出业务异常，提示用户已支付订单或订单已完成，否则重新生成订单；
         *  比较老板当前预支付金是否和订单状态为1的支付金一致， 如果一致，则直接使用订单状态为1的最新订单的订单号，否则重新生成订单；
         */
        Map<String, String> orderSequence = new HashMap<>();
        if (order != null) {
            orderSequence.put("trade_order_id", order.getSequeue());
            if (OrderStatusEnum.PAYED.getCode() == order.getStatus()
                    && pvpBossInfoForOrderVO.getPrice().intValue() == order.getPrepaidAmount()) {
                LOGGER.info("老板[{}]在车队[{}]已经支付，无需重复支付",userSessionContext.getUsername(),teamSequence);
                throw BusinessException
                        .newInstanceExceptionWithData(BizExceptionEnum.ORDER_ALREADY_PAID,
                                orderSequence);
            }
            if (OrderStatusEnum.FINISH.getCode() == order.getStatus()
                    && pvpBossInfoForOrderVO.getPrice().intValue() == order.getPrepaidAmount()) {
                LOGGER.info("老板[{}]在车队[{}]已经完成，无需重复支付",userSessionContext.getUsername(),teamSequence);
                throw BusinessException
                        .newInstanceExceptionWithData(BizExceptionEnum.ORDER_ALREADY_FINISH,
                                orderSequence);
            }
            if (OrderStatusEnum.READY_PAY.getCode() == order.getStatus()
                    && pvpBossInfoForOrderVO.getPrice().intValue() == order.getPrepaidAmount()) {
                return order;
            }
        }
        //其余情况，当新创建一个订单
        order = super
                .buildBossPrePayOrder(userSessionContext.getUid(), pvpBossInfoForOrderVO.getPrice(),
                        couponIds);
        OrderItemTeamPVP orderItemTeamPVP = BeanMapper
                .map(pvpBossInfoForOrderVO, OrderItemTeamPVP.class);
        orderItemTeamPVP.setTeamSequeue(teamSequence);
        orderItemTeamPVP.setUserIdentity((byte) 0);
        orderItemTeamPVP.setUserBaojiLevel(0);
        orderItemTeamPVP.setPrice(0);
        orderItemTeamPVP.setUserChickenId(userSessionContext.getChickenId());
        orderItemTeamPVP.setUserNickname(userSessionContext.getUsername());
        //创建订单
        super.insertOrderAndCoupon(order, couponIds);
        //这里的设置订单id必须等插入后才能设置，否则为空
        orderItemTeamPVP.setOrderId(order.getId());
        orderItemTeamPVP.setUid(order.getUid());
        orderItemTeamPVPRepository.insertSelective(orderItemTeamPVP);
        order.setOrderItemTeamPVP(orderItemTeamPVP);
        return order;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTeamStartOrder(PVPTeamStartOrderVO PVPTeamStartOrderVO) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, PVPTeamStartOrderVO,
                PVPTeamStartOrderVO.getBaojiInfoList());
        //判断这个车队的暴鸡订单是否已经被创建，如果被创建，则不执行
        String idempotentKey = String
                .format(RedisKey.CREATE_BAOJI_ORDER_LOCK_KEY, PVPTeamStartOrderVO.getGameCode(),
                        PVPTeamStartOrderVO.getSequence());
        try {
            String result = cacheManager
                    .set(idempotentKey, PVPTeamStartOrderVO.getSequence(),
                            RedisKey.SET_IF_NOT_EXIST,
                            RedisKey.SET_WITH_EXPIRE_TIME,
                            CommonConstants.ONE_HOUR_SECONDS * 1000);
            ValidateAssert.isTrue(RedisKey.FIND_SUCCESS.equals(result),
                    BizExceptionEnum.TEAM_BAOJI_ORDER_ALREADY_CREATE);
            //创建暴鸡订单
            List<Order> baojiOrders = super
                    .createBaojiOrders(PVPTeamStartOrderVO, PVPTeamStartOrderVO.getBaojiInfoList(),
                            GameOrderType.PVP);
            //将老板订单设置为已开车
            this.setBossOrderTeamStatus(PVPTeamStartOrderVO.getBossUidList(),
                    PVPTeamStartOrderVO.getSequence());
        } catch (Exception e) {
            cacheManager.del(idempotentKey);
            throw e;
        }


    }

    private void setBossOrderTeamStatus(List<String> bossUidList, String teamSequence) {
        if (ObjectTools.isNotEmpty(bossUidList)) {
            bossUidList.forEach(uid -> {
                Order order = orderRepository.selectLastPaidPVPUserOrder(uid, teamSequence);
                ValidateAssert.hasNotNull(BizExceptionEnum.ORDER_STATUS_NOT_EXIST, order);
                OrderItemTeamPVP orderItemTeamPVP = order.getOrderItemTeamPVP();
                ValidateAssert
                        .hasNotNull(BizExceptionEnum.ORDER_STATUS_NOT_EXIST, orderItemTeamPVP);
                //判断订单状态是否是已付款，如果不是已付款，则抛出异常
                //  ValidateAssert.isTrue(OrderStatusEnum.PAYED.getCode()==order.getStatus(),BizExceptionEnum.ORDER_UNKNOW_USER_STATUS);
                //设置车队状态为已开车
                //  orderItemTeam.setTeamStatus(OrderTeamStatus.ALREADY_STATUS.getCode());
                orderItemTeamPVPRepository
                        .updateTeamStatusById(OrderTeamStatus.ALREADY_STATUS.getCode(),
                                order.getId());
            });
        }
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatusPVPParams updateOrderStatusPVPParams) {
        LOGGER.info("收到更新PVP订单状态参数：{}", updateOrderStatusPVPParams);
        //声明逻辑分发处理
        BiFunction<UpdateOrderStatusPVPParams, OrderTeamPVPMember, ProfitCheckParams>
                function = this::dispatHandler;

        //处理相应逻辑并且返回结果
        List<ProfitCheckParams> collect = updateOrderStatusPVPParams.getTeamMembers().stream()
                .map(member ->
                        function.apply(updateOrderStatusPVPParams, member)
                ).collect(Collectors.toList());

        //多余的钱扔给队长
        int baojiProfit = collect.stream()
                .filter(member -> member.getUserIdentity() != UserIdentityEnum.LEADER.getCode())
                .mapToInt(ProfitCheckParams::getProfitAmout).sum();
        int bossPaySum = collect.get(0).getPaySum();
        int surplus = bossPaySum - baojiProfit;
        if (surplus > 0) {
            for (ProfitCheckParams membersProfit : collect) {
                if (membersProfit.getUserIdentity() == UserIdentityEnum.LEADER.getCode()) {
                    LOGGER.info("队长[{}]收益: {}",
                            membersProfit.getUid(),
                            surplus);

                    OrderItemTeamPVP orderItem = orderItemTeamPVPRepository
                            .selectByPrimaryKey(membersProfit.getOrderItermId());
                    orderItem.setPrice(surplus);
                    orderItemTeamPVPRepository.updateByPrimaryKey(orderItem);

                    //发送队长收益
                    OrderIncomeParams sendProfitParams = new OrderIncomeParams();
                    sendProfitParams.setUserId(orderItem.getUid());
                    sendProfitParams.setOrderId(membersProfit.getOrderSequence());
                    sendProfitParams.setAmount(surplus);
                    EventBus.post(new SendProfitEvent(sendProfitParams));
                }
            }
        }
        LOGGER.info("批量更新PVP订单状态完毕");
    }

    @Override
    public PagingResponse<PVPBossOrderListVO> selectUserBossOrdersByPage(int offset, int limit) {
        UserSessionContext userDetail = UserSessionContext.getUser();
        Page<PVPBossOrderVO> page = PageHelper
                .startPage(offset, limit)
                .doSelectPage(() -> orderRepository.selectPvpBossOrderVoList(userDetail.getUid()));
        List<PVPBossOrderListVO> bossOrderListVos = combineBossOrderListVos(page.getResult(),
                userDetail);

        PagingResponse<PVPBossOrderListVO> pagingResponse = new PagingResponse<PVPBossOrderListVO>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), bossOrderListVos);
        return pagingResponse;
    }

    private List<PVPBossOrderListVO> combineBossOrderListVos(List<PVPBossOrderVO> result,
            UserSessionContext userDetail) {
        UserInfoVo userInfoVo = BeanMapper.map(userDetail, UserInfoVo.class);

        List<PVPBossOrderListVO> list = new ArrayList<PVPBossOrderListVO>();
        PVPBossOrderListVO bossOrderListVo = null;
        PVPBossOrderVO pvpBossOrderVO = null;
        for (int i = 0; i < result.size(); i++) {
            pvpBossOrderVO = result.get(i);
            pvpBossOrderVO.setPlayModeName(
                    PlayModeEnum.getByCode(pvpBossOrderVO.getPlayMode()).getDesc());
            pvpBossOrderVO.setSettlementTypeName(
                    SettlementTypeEnum.getByCode(pvpBossOrderVO.getSettlementType()).getDesc());
            bossOrderListVo = new PVPBossOrderListVO();
            bossOrderListVo.setUserInfo(userInfoVo);
            bossOrderListVo.setOrder(pvpBossOrderVO);
            list.add(bossOrderListVo);
        }
        return list;
    }

    @Override
    public PagingResponse<PVPBaojiOrderListVO> selectUserBaojiOrdersByPage(int offset, int limit) {
        UserSessionContext userDetail = UserSessionContext.getUser();
        Page<PVPBaojiOrderVO> page = PageHelper
                .startPage(offset, limit)
                .doSelectPage(() -> orderRepository.selectPvpBaojiOrderVoList(userDetail.getUid()));
        List<PVPBaojiOrderListVO> bossOrderListVos = combineBaojiOrderListVos(page.getResult(),
                userDetail);

        PagingResponse<PVPBaojiOrderListVO> pagingResponse = new PagingResponse<PVPBaojiOrderListVO>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), bossOrderListVos);
        return pagingResponse;
    }

    @Override
    public PVPTeamOrderVo getOrderDetailsBySequence(String uid, String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid, sequence);
        //1: 第一步根据订单序列号查询出改订单
        Order order = getPvpOrderBySequenceId(sequence);
        return doMadeTeamOrderDetails(uid, order);
    }

    @Override
    public PVPPreIncomeVo preIncome(PVPInComeParams pvpInComeParams) {
        return pvpInComeService.preIncome(pvpInComeParams);
    }

    private PVPTeamOrderVo doMadeTeamOrderDetails(String uid, Order order) {
        ValidateAssert.hasNotNull(BizExceptionEnum.ORDER_NOT_EXIST, order);
        ValidateAssert.isTrue(uid.equals(order.getUid()), BizExceptionEnum.OPERATE_PERSON_DIFFER);
        ValidateAssert.hasNotNull(BizExceptionEnum.ORDER_NOT_EXIST, order.getOrderItemTeamPVP());
        //2：第二步，拿到车队订单序列号,并通过订单序列号查询出其它人的信息
        String teamSequence = order.getOrderItemTeamPVP().getTeamSequeue();
        //TODO 查询出其它人的订单信息
        List<Order> otherOrders = new ArrayList<>();
        //如果是老板订单且在开车之后的订单，才查出其他人的开车后订单，否则不查询
        if (UserIdentityEnum.BOSS.getCode() == order.getOrderItemTeamPVP().getUserIdentity()) {
            if (OrderTeamStatus.ALREADY_STATUS.getCode() == order.getOrderItemTeamPVP()
                    .getTeamStatus()) {
                otherOrders = orderRepository
                        .selectPVPTeamOtherOrders(teamSequence, order.getSequeue());
            }
        } else {
            //暴鸡订单一定是已开车后的订单
            otherOrders = orderRepository
                    .selectPVPTeamOtherOrders(teamSequence, order.getSequeue());
        }
        //:3：第三步 根据查询者的身份，组装不同的数据
        PVPTeamOrderVo vo = null;
        if (UserIdentityEnum.BOSS.getCode() == order.getOrderItemTeamPVP().getUserIdentity()) {
            vo = createBossOrderDetails(order, otherOrders);
        } else {
            vo = createBaoJiOrderDetails(order, otherOrders);
        }
        //
        return vo;
    }

    private PVPTeamOrderVo createBaoJiOrderDetails(Order order, List<Order> otherOrders) {
        OrderItemTeamPVP owerOrderIteam = order.getOrderItemTeamPVP();
        PVPTeamOrderVo teamOrderVo = new PVPTeamOrderVo();
        teamOrderVo.setGameName(owerOrderIteam.getGameName());
        //TODO  查找比赛结果
        ResponsePacket<PVPGameResult> resultResponse = ResponsePacket.onSuccess();
       /* ResponsePacket<TeamGameResultVO> resultResponse = pvpTeamServiceClient
                .getGamingTeamGameResult(owerOrderIteam.getTeamSequeue());*/
        ValidateAssert.isTrue(resultResponse.responseSuccess(), BizExceptionEnum.TEAM_NOT_EXIST);
        teamOrderVo.setGameResult(resultResponse.getData());
        teamOrderVo.setGameZoneName(owerOrderIteam.getGameZoneName());
        teamOrderVo.setPlayModeName(PlayModeEnum.getByCode(owerOrderIteam.getPlayMode()).getDesc());
        teamOrderVo.setSettlementTypeName(
                SettlementTypeEnum.getByCode(owerOrderIteam.getSettlementType()).getDesc());
        teamOrderVo.setSettlementNumber(
                owerOrderIteam.getSettlementNumber().stripTrailingZeros().toPlainString());
        //批量查找用户信息
        Map<String, UserSessionContext> userInfoMap = this.getUserInfos(order, otherOrders);
        PVPTeamMemberOrderVO owerOrder = new PVPTeamMemberOrderVO();
        owerOrder.setUid(order.getUid());
        owerOrder.setGameDanName(owerOrderIteam.getGameDanName());
        owerOrder.setUserIdentity(owerOrderIteam.getUserIdentity());
        owerOrder.setBaojiLevel(owerOrderIteam.getUserBaojiLevel());
        owerOrder.setIncomeAmount(owerOrderIteam.getPrice());
        owerOrder.setOrderStatus(order.getStatus());
        this.setOtherBaseInfos(owerOrder, userInfoMap.get(owerOrder.getUid()));
        List<PVPTeamMemberOrderVO> otherVos = new ArrayList<>();
        // 下面组装其它队员信息列表
        for (Order otherOrder : otherOrders) {
            OrderItemTeamPVP otherOrderItemTeamPVP = otherOrder.getOrderItemTeamPVP();
            if (otherOrderItemTeamPVP == null) {
                continue;
            }
            PVPTeamMemberOrderVO otherVo = new PVPTeamMemberOrderVO();
            //再组装一些基础信息
            otherVo.setUid(otherOrder.getUid());
            otherVo.setUserIdentity(otherOrderItemTeamPVP.getUserIdentity());
            otherVo.setGameDanName(otherOrderItemTeamPVP.getGameDanName());
            otherVo.setOrderStatus(otherOrder.getStatus());
            this.setOtherBaseInfos(otherVo, userInfoMap.get(otherVo.getUid()));
            //组装数据
            if (otherOrderItemTeamPVP.getUserIdentity().intValue() == UserIdentityEnum.BOSS
                    .getCode()) {
                //设置老板付款信息
                otherVo.setActualPaidAmount(
                        otherOrder.getActualRefundAmount() == 0 ? otherOrder.getActualPaidAmount()
                                - otherOrder
                                .getPreRefundAmount() : otherOrder.getActualPaidAmount());
            } else {
                otherVo.setBaojiLevel(otherOrderItemTeamPVP.getUserBaojiLevel());
            }
            otherVos.add(otherVo);
        }
        //按照队员身份和暴鸡等级排序
        sortBossQueryOtherList(otherVos);
        teamOrderVo.setOwnerOrder(owerOrder);
        teamOrderVo.setOtherOrders(otherVos);
        return teamOrderVo;
    }

    private PVPTeamOrderVo createBossOrderDetails(Order order, List<Order> otherOrders) {
        /**
         * 老板订单中，老板所有者会有订单信息，但他人订单中只显示基本信息以及暴击等级，不输出金额等参数
         */
        OrderItemTeamPVP owerOrderIteam = order.getOrderItemTeamPVP();
        PVPTeamOrderVo teamOrderVo = new PVPTeamOrderVo();
        teamOrderVo.setGameName(owerOrderIteam.getGameName());
        //TODO  查找比赛结果
        ResponsePacket<PVPGameResult> resultResponse = ResponsePacket.onSuccess();
       /* ResponsePacket<TeamGameResultVO> resultResponse = pvpTeamServiceClient
                .getGamingTeamGameResult(owerOrderIteam.getTeamSequeue());*/
        ValidateAssert.isTrue(resultResponse.responseSuccess(), BizExceptionEnum.TEAM_NOT_EXIST);
        teamOrderVo.setGameResult(resultResponse.getData());
        teamOrderVo.setGameZoneName(owerOrderIteam.getGameZoneName());
        teamOrderVo.setPlayModeName(PlayModeEnum.getByCode(owerOrderIteam.getPlayMode()).getDesc());
        teamOrderVo.setSettlementTypeName(
                SettlementTypeEnum.getByCode(owerOrderIteam.getSettlementType()).getDesc());
        teamOrderVo.setSettlementNumber(
                owerOrderIteam.getSettlementNumber().stripTrailingZeros().toPlainString());
        Map<String, UserSessionContext> userInfoMap = getUserInfos(order, otherOrders);
        PVPTeamMemberOrderVO owerOrder = new PVPTeamMemberOrderVO();
        owerOrder.setUid(order.getUid());
        owerOrder.setGameDanName(owerOrderIteam.getGameDanName());
        owerOrder.setActualPaidAmount(
                order.getActualRefundAmount() == 0 ? order.getActualPaidAmount() - order
                        .getPreRefundAmount() : order.getActualPaidAmount());
        owerOrder.setUserIdentity(owerOrderIteam.getUserIdentity());
        owerOrder.setOrderStatus(order.getStatus());
        owerOrder.setDiscountAmount(order.getDiscountAmount());
        this.setOtherBaseInfos(owerOrder, userInfoMap.get(owerOrder.getUid()));
        //其他用户
        List<PVPTeamMemberOrderVO> otherVos = new ArrayList<>();
        for (Order otherOrder : otherOrders) {
            OrderItemTeamPVP orderItemTeamPVP = otherOrder.getOrderItemTeamPVP();
            if (orderItemTeamPVP == null) {
                continue;
            }
            PVPTeamMemberOrderVO otherVo = new PVPTeamMemberOrderVO();
            //再组装一些基础信息
            otherVo.setUid(otherOrder.getUid());
            otherVo.setGameDanName(orderItemTeamPVP.getGameDanName());
            this.setOtherBaseInfos(otherVo, userInfoMap.get(otherVo.getUid()));
            this.setOtherMoreInfos(otherVo, otherOrder);
            otherVos.add(otherVo);
        }
        //按照队员身份和暴鸡等级排序
        sortBossQueryOtherList(otherVos);
        teamOrderVo.setOwnerOrder(owerOrder);
        teamOrderVo.setOtherOrders(otherVos);
        return teamOrderVo;
    }

    private void setOtherMoreInfos(PVPTeamMemberOrderVO otherVo, Order otherOrder) {
        OrderItemTeamPVP orderItemTeamRPG = otherOrder.getOrderItemTeamPVP();
        otherVo.setUserIdentity(orderItemTeamRPG.getUserIdentity());
        otherVo.setBaojiLevel(orderItemTeamRPG.getUserBaojiLevel());
    }


    private List<PVPBaojiOrderListVO> combineBaojiOrderListVos(List<PVPBaojiOrderVO> result,
            UserSessionContext userDetail) {

        UserInfoVo userInfoVo = BeanMapper.map(userDetail, UserInfoVo.class);

        List<PVPBaojiOrderListVO> list = new ArrayList<PVPBaojiOrderListVO>();
        PVPBaojiOrderListVO baojiOrderListVo = null;
        PVPBaojiOrderVO PVPBaojiOrderVO = null;
        for (int i = 0; i < result.size(); i++) {
            PVPBaojiOrderVO = result.get(i);
            PVPBaojiOrderVO.setPlayModeName(
                    PlayModeEnum.getByCode(PVPBaojiOrderVO.getPlayMode()).getDesc());
            PVPBaojiOrderVO.setSettlementTypeName(
                    SettlementTypeEnum.getByCode(PVPBaojiOrderVO.getSettlementType()).getDesc());
            baojiOrderListVo = new PVPBaojiOrderListVO();
            baojiOrderListVo.setUserInfo(userInfoVo);
            baojiOrderListVo.setOrder(PVPBaojiOrderVO);
            list.add(baojiOrderListVo);
        }
        return list;
    }

    /**
     * @Description: 不同场景处理
     * @param: [updateOrderStatusPVPParams, member]
     * @return: com.kaihei.esportingplus.trade.common.ProfitCheckParams
     * @throws:
     * @author Orochi-Yzh
     * @dateTime 2018/11/7 17:31
     */
    private ProfitCheckParams dispatHandler(UpdateOrderStatusPVPParams updateOrderStatusPVPParams,
            OrderTeamPVPMember member) {

        //车队序列号
        String teamSequence = updateOrderStatusPVPParams.getTeamSequence();
        //车队状态
        Integer teamStatus = updateOrderStatusPVPParams.getTeamStatus();
        //队员列表
        List<OrderTeamPVPMember> teamMembers = updateOrderStatusPVPParams.getTeamMembers();
        //游戏结果列表
        List<GameResultPVPParams> gameResults = updateOrderStatusPVPParams.getGameResults();
        //队员状态
        Integer teamMemberStatus = member.getTeamMemberStatus();
        //队员动作
        TeamOrderPVPActionEnum orderAction = TeamOrderPVPActionEnum.fromCode(teamMemberStatus);
        //队员身份
        Integer userIdentity = member.getUserIdentity();
        //uid
        String uid = member.getTeamMemberUID();
        //车队模式
        SettlementTypeEnum settlementTypeEnum = updateOrderStatusPVPParams.getSettlementTypeEnum();

        //处理结果：检查收益并把多余的钱转移给队长
        ProfitCheckParams resultParams = new ProfitCheckParams();
        UpdateOrderParamsBuilder paramsBuilder = UpdateOrderParams.builder().neddRefund(false);

        LOGGER.info(orderAction.getDesc());
        LOGGER.info("teamSequence：{},teamStatus：{},uid：{}", teamSequence, teamStatus,
                member.getTeamMemberUID());

        //查询已支付的订单记录
        Order payedOrder = getPayedOrder(uid, teamSequence);
        if (payedOrder == null) {
            LOGGER.info("用户[{}]无已支付的PVP订单，忽略处理", uid);
            return resultParams;
        }

        if (orderAction.equals(PREPARE_KICK) //待准备，被队长踢出车队
                || orderAction.equals(PREPARE_QUIT) //待准备，主动退出
                || orderAction.equals(PREPARE_DISMISSD) //待准备，队长解散车队
                || orderAction.equals(PREPARED_KICK) // 已准备，被队长踢出车队
                || orderAction.equals(PREPARED_DISMISSD)//已准备，队长解散车队
                || orderAction.equals(PREPARED_QUIT)    //已准备，主动退出
                || orderAction.equals(PAID_KICK)  //已支付&未开车，被队长踢出车队
                || orderAction.equals(PAID_QUIT)  //已支付&未开车，主动退出
                || orderAction.equals(PAID_DISMISSD)) { //已支付&未开车，队长解散车队

            //所有老板全额退款
            if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                //更新订单：取消 + 退款
                Order tobeUpdateOrder = new Order();
                tobeUpdateOrder.setId(payedOrder.getId());
                tobeUpdateOrder.setStatus((byte) OrderStatusEnum.PAY_CANCEL.getCode());
                //设置预退款金额
                tobeUpdateOrder.setPreRefundAmount(payedOrder.getActualPaidAmount());
                sendRefundMessageToMQ(teamMemberStatus, tobeUpdateOrder, GameOrderType.PVP);
            }

            return resultParams;
        } else if (orderAction.equals(TEAM_STARTED_QUIT) //已开车，主动退出
                || orderAction.equals(TEAM_STARTED_DISMISSD)//已开车，队长解散车队
                || orderAction.equals(TEAM_STARTED_FINISH)) { //已开车，队长正常结束车队

            LOGGER.info("游戏结果：{}", updateOrderStatusPVPParams.getGameResults());

            //总胜局和总败局分组(陪玩为总已打局和总未打局)
            Map<Integer, Long> gameTotalTimes = gameResults.stream()
                    .collect(Collectors.groupingBy(r -> {
                        //总的上分胜局数(或总的已陪玩局数）
                        if (settlementTypeEnum == SettlementTypeEnum.ROUND
                                && r.getGameResult() == GameResultEnum.ROUNDS_VICTORY.getCode()
                                || (settlementTypeEnum == SettlementTypeEnum.HOUR
                                && r.getGameResult() == GameResultEnum.HOURS_PLAYED.getCode())) {
                            //返回0：胜的或已打的
                            return 0;
                            //总的败局数(或陪玩未打局数)
                        } else if (settlementTypeEnum == SettlementTypeEnum.ROUND
                                && r.getGameResult() == GameResultEnum.ROUNDS_DEFEAT.getCode()
                                || (settlementTypeEnum == SettlementTypeEnum.HOUR
                                && r.getGameResult() == GameResultEnum.HOURS_NOT_PLAY.getCode())) {
                            //返回0：败的或未打的
                            return 1;
                        } else {
                            return -1;
                        }
                    }, Collectors.counting()));

            //总局数/总小时数
            int totalRounds = updateOrderStatusPVPParams.getSettleCounts();
            //上分净胜局或者陪玩已打局
            long victoryOnlys = gameTotalTimes.get(0) - gameTotalTimes.get(1);
            //退款局数
            long refunds = totalRounds - victoryOnlys;

            LOGGER.debug("模式：{},总局数:{}|总胜局(或已陪玩):{}|总败局(或未陪玩):{}|净胜局(净已陪玩局):{},退款局数:{}",
                    settlementTypeEnum.getDesc(), totalRounds, gameTotalTimes.get(0),
                    gameTotalTimes.get(1), victoryOnlys, refunds);

            //需要退款的，只管发起退款就完事了
            //净胜局小于等于 0，则发起全额退款 + 订单取消，输都输了暴鸡也别想着收益了
            if (victoryOnlys <= 0) {

                Order tobeUpdateOrder = new Order();
                tobeUpdateOrder.setId(payedOrder.getId());
                tobeUpdateOrder.setPreRefundAmount(payedOrder.getActualPaidAmount());
                tobeUpdateOrder.setStatus((byte) OrderStatusEnum.PAY_CANCEL.getCode());
                sendRefundMessageToMQ(teamMemberStatus, tobeUpdateOrder, GameOrderType.PVP);

                //净胜局大于0且小于总局数 则发起部分退款 + 订单完成，赢了一两局还是有钱分滴
            } else {

                payedOrder = getPayedOrder(uid, teamSequence);
                payedOrder.setStatus((byte) OrderStatusEnum.FINISH.getCode());

                //是否全赢了
                boolean allVictory = victoryOnlys >= totalRounds;
                LOGGER.info("是否全赢：{}", allVictory);
                //不全赢的话发起部分退款
                //部分退款的金额=实付金额/总局数*退款局数
                //小数点向下取整
                int refundFee = (int) (payedOrder.getActualPaidAmount() / totalRounds * refunds);
                if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                    if (allVictory) {
                        //老板更新订单：完成+不退款
                        paramsBuilder.order(payedOrder).memberStatus(teamMemberStatus);
                        ActionHandler.updateHandler = (params, result) -> updateOrder(params);
                    } else {
                        LOGGER.debug("老板[{}]在车队[{}]中没能全赢，部分退款[{}],",
                                member.getTeamMemberUID(), refundFee);
                        Order tobeUpdateOrder = new Order();
                        tobeUpdateOrder.setId(payedOrder.getId());
                        tobeUpdateOrder.setPreRefundAmount(refundFee);
                        tobeUpdateOrder.setStatus((byte) OrderStatusEnum.FINISH.getCode());

                        sendRefundMessageToMQ(teamMemberStatus, tobeUpdateOrder, GameOrderType.PVP);
                    }

                    //暴鸡/暴娘开始算收益
                } else {
                    //暴鸡更新订单：完成 + 结算收益
                    LOGGER.info("开始结算暴鸡/娘[{}]收益", uid);
                    List<String> bossUids = teamMembers.stream()
                            .map(OrderTeamPVPMember::getTeamMemberUID)
                            .collect(Collectors.toList());
                    List<Order> payedOrders = getPayedOrders(bossUids, teamSequence);

                    //计算出暴鸡收益
                    IncomeCaculateParams caculateParams = IncomeCaculateParams.builder()
                            .payedOrders(payedOrders)
                            .pvpTeamMembers(teamMembers)
                            .pvpMember(member)
                            .refundFee(refundFee)
                            .refundFee((int) victoryOnlys)
                            .build();
                    InComeVo inComeVo = pvpInComeService.getIncome(caculateParams);
                    Integer profitAmout = inComeVo.getInComeAmounts();
                    resultParams.setProfitAmout(profitAmout);
                    resultParams.setPaySum(inComeVo.getPaySum());
                    if (UserIdentityEnum.LEADER.getCode() == userIdentity) {
                        resultParams.setUserIdentity(userIdentity);
                        resultParams.setUid(uid);
                    }

                    Order tobeUpdateOrder = new Order();
                    tobeUpdateOrder.setId(payedOrder.getId());
                    tobeUpdateOrder.setStatus((byte) OrderStatusEnum.FINISH.getCode());

                    paramsBuilder.order(tobeUpdateOrder)
                            .memberStatus(teamMemberStatus)
                            .profitAmout(profitAmout);

                    //暴鸡更新订单：完成+无收益
                    ActionHandler.updateHandler = this::updateOrderAndProfit;
                }
            }
        } else {
            LOGGER.error(BizExceptionEnum.ORDER_UNKNOW_USER_STATUS.getErrMsg()
                            + ",teamSequence={}"
                            + ",teamStatus={}"
                            + ",member={}"
                            + ",payedOrder={}"
                    , teamSequence, teamStatus, member, payedOrder);
            throw new BusinessException(BizExceptionEnum.ORDER_UNKNOW_USER_STATUS);
        }

        //开始处理订单更新、退款
        ActionHandler.updateHandler.update(paramsBuilder.build(), resultParams);

        return resultParams;
    }

    @Override
    public boolean checkBossBetweenSamePaid(PVPBossCurrentPaidAmountParams amountParams){
        int bossPaySum = pvpInComeService.getBossPaySum(amountParams.getGameId(),
                Arrays.asList(amountParams.getBossGameDanId()),
                amountParams.getBaojiLevelCodeList());

        Order order = getBySequenceId(amountParams.getOrderSequence());

        //老板已付金额
        Integer actualPaidAmount = order.getActualPaidAmount();
        //结算数量
        BigDecimal settleMentNumber = amountParams.getSettlementNumber();
        //结算数量去尾
        int settleMentNumberCutTail = settleMentNumber.intValue();
        //老板当前应付金额
        int shouldPay = bossPaySum * settleMentNumberCutTail;

        LOGGER.info("已支付金额:{}，当前应付金额:{},结算数量:{}(去尾-{})",
                actualPaidAmount,shouldPay, settleMentNumberCutTail,
                settleMentNumber.doubleValue()-settleMentNumberCutTail);
        return actualPaidAmount == shouldPay;
    }

    @Override
    public BizExceptionEnum checkTeamMemberPayed(String orderSequence) {
        Order order = getBySequenceId(orderSequence);
        if(order == null){
            return BizExceptionEnum.ORDER_NOT_EXIST;
        }

        //如果本地库里不是已支付，则去py查询最新的订单数据
        if(order.getStatus().intValue() != OrderStatusEnum.PAYED.getCode()){

            String[] delay = orderRetryInterval.split("/");
            int retryLen = delay.length;
            int i;
            for ( i = 0; i < retryLen; i++) {
                try{
                    //查询支付系统的订单信息 TODO
                    ResponsePacket paymentOrder = tradeServiceClient.query(orderSequence,order_type+"");
                    if(paymentOrder != null &&  paymentOrder.getCode() != BizExceptionEnum.HYSTRIX_SERVER.getErrCode()){
                        if(paymentOrder.getData() == null || paymentOrder.getCode() != BizExceptionEnum.SUCCESS.getErrCode()){
                            LOGGER.error("查询支付系统的订单[{}]信息失败：{}",orderSequence,paymentOrder.toString());
                            return BizExceptionEnum.ORDER_FEOM_PAYMENT_FAIL;
                        }
                        Map payMap = (Map) paymentOrder.getData();
                        //是否已支付
                        boolean paySuccess = payMap.get("trade_state")
                                .equals(WxPayStatusEnum.SUCCESS.getCode());
                        if(!paySuccess){
                            LOGGER.warn(String.format(BizExceptionEnum.ORDER_STATUS_NOT_PAID.getErrMsg(),orderSequence)
                                    + ": " +
                                    WxPayStatusEnum.fromCode(payMap.get("trade_state").toString()));
                            return BizExceptionEnum.ORDER_STATUS_NOT_PAID;
                        }else{
                            //已支付更新状态
                            Date responseTime = DateUtil
                                    .str2Date(payMap.get("time_end").toString(), DateUtil.SIMPLE_FORMATTER);
                            order.setOuterTradeNo(payMap.get("transaction_id").toString());//第三方订单号
                            order.setActualPaidAmount(Integer.valueOf(payMap.get("total_fee").toString()));//实际支付金额
                            order.setGmtModified(new Date());//订单修改时间
                            order.setResponseTime(responseTime); //订单响应时间
                            order.setPaymentTime(new Date());//支付成功时间

                            //更新订单&消费优惠券
                            updateOrder(order);
                            break;
                        }
                    }else{
                        try {
                            LOGGER.error("获取微信订单[{}]信息失败，{}ms后,查询：{}次",
                                    orderSequence,
                                    delay[i],
                                    i+1);
                            Thread.sleep(Long.valueOf(delay[i]));
                        } catch (Exception e) {
                            LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e);
                            break;
                        }
                    }

                }catch (Exception e){
                    try {
                        LOGGER.error("获取微信订单[{}]信息异常，{}ms后,查询：{}次",
                                orderSequence,
                                delay[i],
                                i+1,e);
                        Thread.sleep(Long.valueOf(delay[i]));
                    } catch (Exception e2) {
                        LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e2);
                        break;
                    }
                }


            }
            //达到重试阈值，记录一下
            if(i == retryLen){
                LOGGER.warn("获取微信订单[{}]已支付状态,次数已达阀值：{}", orderSequence,retryLen);
            }
        }

        return BizExceptionEnum.SUCCESS;

    }
}
