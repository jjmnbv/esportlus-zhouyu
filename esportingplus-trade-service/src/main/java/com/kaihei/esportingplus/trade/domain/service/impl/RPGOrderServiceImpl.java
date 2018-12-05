package com.kaihei.esportingplus.trade.domain.service.impl;

import static com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum.JOINED_TEAM_DISMISSD;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum.JOINED_TEAM_KICK;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum.JOINED_TEAM_QUIT;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum.PREPARE_JOIN_TEAM_DISMISSD;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum.PREPARE_JOIN_TEAM_KICK;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum.PREPARE_JOIN_TEAM_QUIT;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum.TEAM_STARTED_DISMISSD;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum.TEAM_STARTED_FINISH;
import static com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum.TEAM_STARTED_QUIT;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.kaihei.esportingplus.api.feign.CompaintServiceClient;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.lock.RedisLock;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.feign.RPGTeamServiceClient;
import com.kaihei.esportingplus.gamingteam.api.params.TeamInfoBatchParams;
import com.kaihei.esportingplus.gamingteam.api.vo.BossInfoForOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceUidVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGBaojiInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;
import com.kaihei.esportingplus.trade.api.enums.ChannelEnum;
import com.kaihei.esportingplus.trade.api.enums.OrderStatusEnum;
import com.kaihei.esportingplus.trade.api.enums.OrderTeamStatus;
import com.kaihei.esportingplus.trade.api.enums.ServiceTypeEnum;
import com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum;
import com.kaihei.esportingplus.trade.api.enums.WxPayStatusEnum;
import com.kaihei.esportingplus.trade.api.params.CheckCouponParams;
import com.kaihei.esportingplus.trade.api.params.OrderParams;
import com.kaihei.esportingplus.trade.api.params.OrderQueryParams;
import com.kaihei.esportingplus.trade.api.params.OrderTeamRPGMember;
import com.kaihei.esportingplus.trade.api.params.StudioOrderQueryParams;
import com.kaihei.esportingplus.trade.api.params.StudioUserComplitedBossOrderStatisticGetParams;
import com.kaihei.esportingplus.trade.api.params.StudioUserOrderStatisticsQueryParams;
import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusRPGParams;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinPayConfirmPacket;
import com.kaihei.esportingplus.trade.api.params.pay.WeiXinRefundPacket;
import com.kaihei.esportingplus.trade.api.vo.BaojiOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.BaojiOrderVO;
import com.kaihei.esportingplus.trade.api.vo.BossOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.BossOrderVO;
import com.kaihei.esportingplus.trade.api.vo.CreatedBossOrderVO;
import com.kaihei.esportingplus.trade.api.vo.InComeVo;
import com.kaihei.esportingplus.trade.api.vo.OrderItemTeamVo;
import com.kaihei.esportingplus.trade.api.vo.StudioOrderListVo;
import com.kaihei.esportingplus.trade.api.vo.StudioOrderVo;
import com.kaihei.esportingplus.trade.api.vo.StudioUserOrderStatisticVO;
import com.kaihei.esportingplus.trade.api.vo.TeamBossOrderVO;
import com.kaihei.esportingplus.trade.api.vo.TeamMemberOrderVo;
import com.kaihei.esportingplus.trade.api.vo.TeamOrderVo;
import com.kaihei.esportingplus.trade.api.vo.UserInfoVo;
import com.kaihei.esportingplus.trade.common.IncomeCaculateParams;
import com.kaihei.esportingplus.trade.common.ProfitCheckParams;
import com.kaihei.esportingplus.trade.common.TradeConstants;
import com.kaihei.esportingplus.trade.common.UpdateOrderParams;
import com.kaihei.esportingplus.trade.common.UpdateOrderParams.UpdateOrderParamsBuilder;
import com.kaihei.esportingplus.trade.data.repository.OrderItemTeamRPGRepository;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.entity.OrderCoupon;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamRPG;
import com.kaihei.esportingplus.trade.domain.entity.TeamOrderCount;
import com.kaihei.esportingplus.trade.domain.service.CouponService;
import com.kaihei.esportingplus.trade.domain.service.RPGInComeService;
import com.kaihei.esportingplus.trade.domain.service.RPGOrderService;
import com.kaihei.esportingplus.trade.enums.BusinessTypeEnum;
import com.kaihei.esportingplus.trade.enums.GameOrderType;
import com.kaihei.esportingplus.trade.enums.PayMentTypeEnum;
import com.kaihei.esportingplus.trade.event.CreateBaojiOrderEvent;
import com.kaihei.esportingplus.trade.event.CreateBaojiOrderEventConsumer;
import com.kaihei.esportingplus.trade.event.CreateOrderEvent;
import com.kaihei.esportingplus.trade.event.SendProfitEvent;
import com.kaihei.esportingplus.trade.handler.ActionHandler;
import com.kaihei.esportingplus.user.api.feign.UserServiceClient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("rpgOrderService")
public class RPGOrderServiceImpl extends AbstractOrderService implements RPGOrderService {

    @Autowired
    private RPGTeamServiceClient rpgTeamServiceClient;

    @Autowired
    private OrderItemTeamRPGRepository orderItemTeamRPGRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private CompaintServiceClient compaintServiceClient;

    @Autowired
    private CreateBaojiOrderEventConsumer createBaojiOrderEventConsumer;

    @Autowired
    private CouponService couponService;

    @Autowired
    private RPGInComeService rpgInComeService;

    @Autowired
    private SnowFlake snowFlake;

    @Value("${python.order.retryInterval}")
    private String orderRetryInterval;

    @Override
    @Transactional
    @RedisLock(keyFormate = "BossOrder:teamSequence:$.orderParams.teamSequence:uid:$.uid", expireTime = 2000)
    public CreatedBossOrderVO createTeamBossOrder(OrderQueryParams orderParams) {
        //车队sequenceId
        String teamSequence = orderParams.getTeamSequence();
        //当前登陆用户ID为空，则从后台中取
        String userId = null;
        if (ObjectTools.isEmpty(userId)) {
            UserSessionContext userDetail = UserSessionContext.getUser();
            userId = userDetail.getUid();
        }
        //创建成功后返回实体
        CreatedBossOrderVO createdBossOrderVO = new CreatedBossOrderVO();
        createdBossOrderVO.setOrderType(17);
        //查询订单是否已经存在
        TeamBossOrderVO teamBossOrderVO = orderRepository
                .selectRPGBossOrderByTeamSequenceAndUid(teamSequence, userId);
        //订单已经存在的情况
        if (teamBossOrderVO != null) {
            ValidateAssert.allNotNull(BizExceptionEnum.INTERNAL_SERVER_ERROR, teamBossOrderVO);

            createdBossOrderVO.setOrderId(teamBossOrderVO.getSequeue());
            createdBossOrderVO.setFee(teamBossOrderVO.getPrepaidAmount() - teamBossOrderVO
                    .getDiscountAmount());
            createdBossOrderVO.setStatus(teamBossOrderVO.getStatus());
            return createdBossOrderVO;
        }

        LOGGER.info("开始创建订单，参数：{}", JSON.toJSONString(orderParams));
        //生成时间戳
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        String timeStamp = df.format(calendar.getTime());
        //生成雪花ID
        long snowId = snowFlake.nextId();
        //合成订单号
        String orderId = TradeConstants.ORDER_ID_PREFIEX + timeStamp + snowId;

        createdBossOrderVO.setOrderId(orderId);
        //DNF小程序
        //获取车队信息
        ResponsePacket<BossInfoForOrderVO> bossInfoForOrderVOResp = rpgTeamServiceClient
                .getBossInfoForOrder(teamSequence, userId);
        if (!bossInfoForOrderVOResp.responseSuccess()) {
            throw new BusinessException(bossInfoForOrderVOResp.getCode(),
                    bossInfoForOrderVOResp.getMsg());
        }
        BossInfoForOrderVO bossInfoForOrderVO = bossInfoForOrderVOResp.getData();
        createdBossOrderVO.setFee(bossInfoForOrderVO.getPrice());

        Order order = new Order();
        //订单用户uid
        order.setUid(userId);
        //订单序列号
        order.setSequeue(orderId);
        //订单业务类型
        order.setBusinessType((byte)BusinessTypeEnum.TEAM_ORDER.getCode());
        //订单状态
        order.setStatus((byte)OrderStatusEnum.READY_PAY.getCode());
        createdBossOrderVO.setStatus(OrderStatusEnum.READY_PAY.getCode());
        //实付金额
        order.setActualPaidAmount(0);
        //计算预付金额
        order.setPrepaidAmount(bossInfoForOrderVO.getPrice());
        //支付方式
        order.setPaymentType((byte)PayMentTypeEnum.WEIXIN_PAY.getCode());

        OrderItemTeamRPG orderItemTeamRPG = BeanMapper.map(bossInfoForOrderVO, OrderItemTeamRPG.class);
        orderItemTeamRPG.setTeamSequeue(bossInfoForOrderVO.getSequence());
        orderItemTeamRPG.setUserIdentity((byte) 0);
        orderItemTeamRPG.setUserBaojiLevel(0);
        orderItemTeamRPG.setPrice(0);

        List<Long> couponIds = orderParams.getCouponId();
        if (couponIds != null && !couponIds.isEmpty()) {
            Integer discountAmountSum = 0;
            Integer discountAmount = 0;
            CheckCouponParams checkCouponParams = null;
//            ConsumeCouponParams consumeCouponParams = null;
            for (Long couponId : couponIds) {
                //调用python接口，校验并获取优惠券对应的金额
                if (couponId == 0) {
                    continue;
                }
                order.setCouponId(couponId);

                checkCouponParams = new CheckCouponParams();
                checkCouponParams.setUid(userId);
                checkCouponParams.setCouponId(couponId);
                checkCouponParams.setChannel(ChannelEnum.MINI_PROGRAME.getCode());
                checkCouponParams.setGameType(88);
                checkCouponParams.setPrepay(bossInfoForOrderVO.getPrice());
                checkCouponParams.setServiceType(ServiceTypeEnum.DNF.getCode());
                ResponsePacket checkCouponResponsePacket = pythonRestClient
                        .checkCoupon(checkCouponParams);
                if (!checkCouponResponsePacket.responseSuccess()) {
                    LOGGER.info("确认优惠卷入参：{}", JSON.toJSONString(checkCouponParams));
                    LOGGER.info("确认优惠卷是否存在：{}", JSON.toJSONString(checkCouponResponsePacket));
                    throw new BusinessException(checkCouponResponsePacket.getCode(),
                            checkCouponResponsePacket.getMsg());
                }
                Map checkCouponResponseData = (HashMap) checkCouponResponsePacket.getData();
                boolean isValid = (boolean) checkCouponResponseData.get("is_valid");
                if (!isValid) {
                    throw new BusinessException(BizExceptionEnum.COUPON_CANNOT_USED.getErrCode(),
                            BizExceptionEnum.COUPON_CANNOT_USED.getErrMsg());
                }
                discountAmount = (int) checkCouponResponseData.get("discount");
                discountAmountSum += discountAmount;
            }
            //计算累计优惠金额discount_amount
            order.setDiscountAmount(discountAmountSum);
            //计算预付金额：prepaidAmount = bossInfoForOrderVO.getPrice() - discount_amount
            order.setPrepaidAmount(bossInfoForOrderVO.getPrice());
            createdBossOrderVO.setFee(bossInfoForOrderVO.getPrice() - discountAmountSum);

            LOGGER.info("拼接前端返回值:{}", JSON.toJSONString(checkCouponParams));
        }

        CreateOrderEvent event = new CreateOrderEvent(order, orderItemTeamRPG, couponIds);
        this.createOrder(event);
        //事务不能回滚，暂时不使用异步入库
        //EventBus.post(event);
        return createdBossOrderVO;
    }

    private void createOrder(CreateOrderEvent event) {
        //获取实体
        Order order = event.getOrder();
        if (Objects.isNull(order.getDiscountAmount())) {
            order.setDiscountAmount(0);
        }
        OrderItemTeamRPG orderItemTeamRPG = event.getOrderItemTeamRPG();
        List<Long> couponIds = event.getCouponIds();

        //Order实体入库
        LOGGER.info("Order入库：{}", JSON.toJSONString(order));
        orderRepository.insertOrder(order);
        orderItemTeamRPG.setOrderId(order.getId());
        orderItemTeamRPG.setUid(order.getUid());

        //OrderItemTeam实体入库
        LOGGER.info("OrderItemTeam入库：{}", JSON.toJSONString(orderItemTeamRPG));
        orderItemTeamRPGRepository.insertOrderItemTeam(orderItemTeamRPG);

        //OrderCoupon批量入库
        if (couponIds != null && !couponIds.isEmpty()) {
            List<OrderCoupon> orderConponList = new LinkedList<OrderCoupon>();
            OrderCoupon orderCoupon = null;
            for (Long couponId : couponIds) {
                orderCoupon = new OrderCoupon();
                orderCoupon.setOrderId(order.getId());
                orderCoupon.setCouponId(couponId);
                orderConponList.add(orderCoupon);
            }
            LOGGER.info("OrderConponList入库：{}", JSON.toJSONString(orderConponList));
            couponService.insertOrderCoupon(orderConponList);
        }

    }


    @Override
    public List<String> createBaojiOrder(String sequeue) {
        //获取暴鸡信息
        RPGTeamStartOrderVO RPGTeamStartOrderVO = rpgTeamServiceClient.getBaojiInfoForOrder(sequeue)
                .getData();
        List<RPGBaojiInfoVO> baojiInfoList = RPGTeamStartOrderVO.getBaojiInfoList();
        //订单号列表
        List<String> orderIdList = new ArrayList<String>();
        //生成时间戳
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        String timeStamp = null;
        Long snowId = null;
        //生成雪花ID
        for (int i = 0; !baojiInfoList.isEmpty() && i < baojiInfoList.size(); i++) {
            timeStamp = df.format(calendar.getTime());
            snowId = snowFlake.nextId();
            orderIdList.add(timeStamp + String.valueOf(snowId));
        }
        //事务不能回滚，暂时不使用异步入库
        CreateBaojiOrderEvent event = new CreateBaojiOrderEvent(RPGTeamStartOrderVO, orderIdList);
        createBaojiOrderEventConsumer.createBaojiOrder(event);
        //EventBus.post(event);
        return orderIdList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBaojiOrderAndUpdateBossOrderStatus(RPGTeamStartOrderVO vo) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, vo, vo.getBaojiInfoList());
        //判断这个车队的暴鸡订单是否已经被创建，如果被创建，则不执行
        String idempotentKey = String
                .format(RedisKey.CREATE_BAOJI_ORDER_LOCK_KEY, vo.getGameCode(), vo.getSequence());
        String result = cacheManager
                .set(idempotentKey, vo.getSequence(), RedisKey.SET_IF_NOT_EXIST,
                        RedisKey.SET_WITH_EXPIRE_TIME,
                        CommonConstants.ONE_HOUR_SECONDS * 1000);
        ValidateAssert.isTrue(RedisKey.FIND_SUCCESS.equals(result),
                BizExceptionEnum.TEAM_BAOJI_ORDER_ALREADY_CREATE);
        try {
            List<RPGBaojiInfoVO> baojiInfoList = vo.getBaojiInfoList();
            //订单号列表
            List<String> orderIdList = new ArrayList<String>();
            //生成时间戳
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            Calendar calendar = Calendar.getInstance();
            String timeStamp = null;
            Long snowId = null;
            //生成雪花ID
            for (int i = 0; !baojiInfoList.isEmpty() && i < baojiInfoList.size(); i++) {
                timeStamp = df.format(calendar.getTime());
                snowId = snowFlake.nextId();
                orderIdList.add(TradeConstants.ORDER_ID_PREFIEX+timeStamp + String.valueOf(snowId));
            }
            //事务不能回滚，暂时不使用异步入库
            CreateBaojiOrderEvent event = new CreateBaojiOrderEvent(vo, orderIdList);
            createBaojiOrderEventConsumer.createBaojiOrder(event);

            //设置老板订单为开车
            this.setBossOrderTeamStatus(vo.getBossUidList(), vo.getSequence());
        } catch (Exception e) {
            cacheManager.del(idempotentKey);
            throw e;
        }

        //成功后放入redis中，防止重复消费

        //EventBus.post(event);
    }

    private void setBossOrderTeamStatus(List<String> bossUidList, String teamSequence) {
        if (ObjectTools.isNotEmpty(bossUidList)) {
            bossUidList.forEach(uid -> {
                Order order = orderRepository.selectLastUserOrder(uid, teamSequence);
                ValidateAssert.hasNotNull(BizExceptionEnum.ORDER_STATUS_NOT_EXIST, order);
                OrderItemTeamRPG orderItemTeamRPG = order.getOrderItemTeamRPG();
                ValidateAssert.hasNotNull(BizExceptionEnum.ORDER_STATUS_NOT_EXIST, orderItemTeamRPG);
                //判断订单状态是否是已付款，如果不是已付款，则抛出异常
                //  ValidateAssert.isTrue(OrderStatusEnum.PAYED.getCode()==order.getStatus(),BizExceptionEnum.ORDER_UNKNOW_USER_STATUS);
                //设置车队状态为已开车
                //  orderItemTeam.setTeamStatus(OrderTeamStatus.ALREADY_STATUS.getCode());
                orderItemTeamRPGRepository
                        .updateTeamStatusById(OrderTeamStatus.ALREADY_STATUS.getCode(),
                                order.getId());
            });
        }
    }

    @Override
    public OrderItemTeamVo selectOrderItemTeamByOrderSequeue(String sequeue) {
        OrderItemTeamRPG orderItemTeamRPG = orderItemTeamRPGRepository
                .selectOrderItemTeamByOrderSequeue(sequeue);
        OrderItemTeamVo orderItemTeamVo = BeanMapper.map(orderItemTeamRPG, OrderItemTeamVo.class);
        return orderItemTeamVo;
    }

    @Override
    public PagingResponse<BossOrderListVo> selectUserBossOrdersByPage(OrderParams orderParams) {
        UserSessionContext userDetail = UserSessionContext.getUser();
        String userId = userDetail.getUid();

        Page<BossOrderVO> page = PageHelper
                .startPage(orderParams.getOffset(), orderParams.getLimit())
                .doSelectPage(() -> orderRepository.selectBossOrderVoList(userId));

        List<BossOrderListVo> bossOrderListVos = combineBossOrderListVos(page.getResult(),
                userDetail);
        PagingResponse<BossOrderListVo> pagingResponse = new PagingResponse<BossOrderListVo>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), bossOrderListVos);
        return pagingResponse;
    }

    private List<BossOrderListVo> combineBossOrderListVos(List<BossOrderVO> result,
            UserSessionContext userDetail) {
        UserInfoVo userInfoVo = BeanMapper.map(userDetail, UserInfoVo.class);

        List<BossOrderListVo> list = new ArrayList<BossOrderListVo>();
        BossOrderListVo bossOrderListVo = null;
        for (int i = 0; i < result.size(); i++) {
            bossOrderListVo = new BossOrderListVo();
            bossOrderListVo.setUserInfoVo(userInfoVo);
            bossOrderListVo.setBossOrderVO(result.get(i));
            list.add(bossOrderListVo);
        }
        return list;
    }

    @Override
    public PagingResponse<BaojiOrderListVo> selectUserBaojiOrdersByPage(OrderParams orderParams) {
        UserSessionContext userDetail = UserSessionContext.getUser();
        String userId = userDetail.getUid();

        Page<BaojiOrderVO> page = PageHelper
                .startPage(orderParams.getOffset(), orderParams.getLimit())
                .doSelectPage(() -> orderRepository.selectBaojiOrderVoList(userId));

        List<BaojiOrderListVo> baojiOrderListVos = combineBaojiOrderListVos(page.getResult(),
                userDetail);
        PagingResponse<BaojiOrderListVo> pagingResponse = new PagingResponse<BaojiOrderListVo>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), baojiOrderListVos);
        return pagingResponse;
    }

    private List<BaojiOrderListVo> combineBaojiOrderListVos(List<BaojiOrderVO> result,
            UserSessionContext userDetail) {
        UserInfoVo userInfoVo = BeanMapper.map(userDetail, UserInfoVo.class);
        List<BaojiOrderListVo> list = new ArrayList<BaojiOrderListVo>();
        BaojiOrderListVo baojiOrderListVo = null;
        for (int i = 0; i < result.size(); i++) {
            baojiOrderListVo = new BaojiOrderListVo();
            baojiOrderListVo.setUserInfoVo(userInfoVo);
            baojiOrderListVo.setBaojiOrderVO(result.get(i));
            list.add(baojiOrderListVo);
        }
        return list;
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatusRPGParams updateOrderStatusRPGParams) {
        LOGGER.info("收到更新RPG订单状态参数：{}", updateOrderStatusRPGParams);
        String teamSequence = updateOrderStatusRPGParams.getTeamSequence();
        Integer teamStatus = updateOrderStatusRPGParams.getTeamStatus();
        Integer gameResult = updateOrderStatusRPGParams.getGameResult();
        ActionHandler.dispatHandler = this::dispatHandler;

        //TODO 把老板排到最后更新,计算收益时要过虑已支付状态，如果老板前置，过虑已支付结果为空
        //TODO 这个接口会成为瓶颈与db交互频繁
        List<ProfitCheckParams> collect = updateOrderStatusRPGParams.getTeamMembers().stream()
                .sorted((x, y) -> y.getUserIdentity() - x.getUserIdentity())
                .map(member ->
                        ActionHandler.dispatHandler.handle(updateOrderStatusRPGParams.getTeamMembers()
                                , member, teamSequence, teamStatus, gameResult)

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

                    OrderItemTeamRPG orderItem = orderItemTeamRPGRepository
                            .selectByPrimaryKey(membersProfit.getOrderItermId());
                    orderItem.setPrice(surplus);
                    orderItemTeamRPGRepository.updateByPrimaryKey(orderItem);

                    //发送队长收益
                    OrderIncomeParams sendProfitParams = new OrderIncomeParams();
                    sendProfitParams.setUserId(orderItem.getUid());
                    sendProfitParams.setOrderId(membersProfit.getOrderSequence());
                    sendProfitParams.setAmount(surplus);
                    EventBus.post(new SendProfitEvent(sendProfitParams));
                }
            }
        }
        LOGGER.info("批量更新RPG订单状态完毕");

    }



    @Override
    public List<StudioUserOrderStatisticVO> getStudioUserOrderStatistics(
            StudioUserOrderStatisticsQueryParams studioUserOrderStatisticsQueryParams) {
        List<StudioUserOrderStatisticVO> result = new ArrayList<>();
        if (ObjectTools.isEmpty(studioUserOrderStatisticsQueryParams.getUids())) {
            // uids 是空直接返回空值
            return result;
        }
        List<StudioOrderVo> baojiOrderVOS = orderRepository
                .selectBaojiOrderVoListByUidsAndCreateTime(studioUserOrderStatisticsQueryParams);
        //全部订单sequeue
        List<String> sequeues = baojiOrderVOS.stream().map(StudioOrderVo::getSequeue)
                .collect(Collectors.toList());

        List<String> beCompaintedSequeue = Lists.newArrayList();
        if (!sequeues.isEmpty()) {
            beCompaintedSequeue.addAll(compaintServiceClient
                    .checkOrderBeComplainted(sequeues).getData());
        }

        //根据用户uid 分组
        Map<String, List<StudioOrderVo>> groups = baojiOrderVOS.stream()
                .collect(Collectors.groupingBy(StudioOrderVo::getUid));
        groups.forEach((uid, orderVOS) -> {
            StudioUserOrderStatisticVO studioUserOrderStatisticVO = new StudioUserOrderStatisticVO();
            result.add(studioUserOrderStatisticVO);

            //用户uid
            studioUserOrderStatisticVO.setUid(uid);
            //用户总单数
            studioUserOrderStatisticVO.setOrderNumberAll(((long) (orderVOS.size())));
            //用户已完成单数
            studioUserOrderStatisticVO.setOrderNumberSuccess(orderVOS.stream()
                    .filter(o -> o.getStatus().intValue() == OrderStatusEnum.FINISH.getCode())
                    .count());
            //统计总收入
            int sum = orderVOS.stream()
                    .filter(o -> o.getStatus().intValue() == OrderStatusEnum.FINISH.getCode())
                    .filter(o -> Objects.nonNull(o.getPrice()))
                    .mapToInt(StudioOrderVo::getPrice)
                    .sum();
            studioUserOrderStatisticVO.setOrderAmountIncome(sum);
            Optional.ofNullable(studioUserOrderStatisticsQueryParams.getWithCompaint())
                    .ifPresent(withCompaint -> {
                        if (withCompaint.equals(1)) {
                            //统计被投诉订单
                            List<StudioOrderVo> beComplaintOrders = orderVOS.stream()
                                    .filter(o -> beCompaintedSequeue.contains(o.getSequeue()))
                                    .collect(Collectors.toList());
                            studioUserOrderStatisticVO
                                    .setOrderNumberCompaint(((long) (beComplaintOrders.size())));

                            //涉及投诉订单金额
                            int beComplaintedPrice = beComplaintOrders.stream()
                                    .filter(o -> Objects.nonNull(o.getPrice()))
                                    .mapToInt(StudioOrderVo::getPrice).sum();
                            studioUserOrderStatisticVO.setOrderAmountCompaint(beComplaintedPrice);
                        }
                    });
        });
        return result;
    }

    @Override
    public PagingResponse<StudioOrderListVo> getStudioUserOrdersByPage(
            StudioOrderQueryParams params) {
        Page<StudioOrderListVo> page = PageHelper
                .startPage(params.getOffset(), params.getLimit())
                .doSelectPage(() -> orderItemTeamRPGRepository.selectStudioOrderVoList(params));

        List<StudioOrderListVo> studioOrderListVos = combineStudioOrderListVos(page.getResult(),
                params);
        PagingResponse<StudioOrderListVo> pagingResponse = new PagingResponse<StudioOrderListVo>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), studioOrderListVos);
        return pagingResponse;
    }

    private List<StudioOrderListVo> combineStudioOrderListVos(List<StudioOrderListVo> data,
            StudioOrderQueryParams params) {
        List<StudioOrderListVo> result = new LinkedList<StudioOrderListVo>();
        //根据车队序列号获取对应的原价,标题
        TeamInfoBatchParams teamInfoBatchParams = new TeamInfoBatchParams();
        List<String> teamSequeueList = new LinkedList<String>();
        if (data != null && !data.isEmpty()) {
            for (StudioOrderListVo studioOrderListVo : data) {
                teamSequeueList.add(studioOrderListVo.getTeamSequeue());
            }
        }
        teamInfoBatchParams.setTeamSequenceList(teamSequeueList);
        ResponsePacket<List<TeamInfoVO>> teamInfoListResp = rpgTeamServiceClient
                .getBatchGamingTeamInfo(teamInfoBatchParams);
        if (!teamInfoListResp.responseSuccess()) {
            throw new BusinessException(teamInfoListResp.getCode(), teamInfoListResp.getMsg());
        }
        List<TeamInfoVO> teamInfoList = teamInfoListResp.getData();
        //根据用户uid获取对应的昵称、鸡牌号
        ResponsePacket<List<UserSessionContext>> resp = userServiceClient
                .getUserInfosByUids(params.getUids());
        if (!resp.responseSuccess()) {
            throw new BusinessException(resp.getCode(), resp.getMsg());
        }
        List<UserSessionContext> list = resp.getData();
        //遍历拼接数据
        for (int i = 0; i < data.size(); i++) {
            StudioOrderListVo studioOrderListVo = data.get(i);
            list.stream()
                    .filter(item -> item.getUid().equalsIgnoreCase(studioOrderListVo.getUserUid()))
                    .forEach(item -> studioOrderListVo.setUserChickenId(item.getChickenId()));
            list.stream()
                    .filter(item -> item.getUid().equalsIgnoreCase(studioOrderListVo.getUserUid()))
                    .forEach(item -> studioOrderListVo.setUserNickname(item.getUsername()));
            teamInfoList.stream().filter(item -> item.getSequence()
                    .equalsIgnoreCase(studioOrderListVo.getTeamSequeue()))
                    .forEach(item -> studioOrderListVo.setTeamTitle(item.getTitle()));
            teamInfoList.stream().filter(item -> item.getSequence()
                    .equalsIgnoreCase(studioOrderListVo.getTeamSequeue()))
                    .forEach(item -> studioOrderListVo.setTeamOriginalPrice(item.getOriginalFee()));
            result.add(studioOrderListVo);
        }
        data.clear();
        return result;
    }

    private ProfitCheckParams dispatHandler(List<OrderTeamRPGMember> teamMembers,
            OrderTeamRPGMember member, String teamSequence,
            Integer teamStatus, Integer gameResult) {

        //队员状态
        Integer teamMemberStatus = member.getTeamMemberStatus();
        TeamOrderRPGActionEnum orderAction = TeamOrderRPGActionEnum.fromCode(teamMemberStatus);
        //队员身份
        Integer userIdentity = member.getUserIdentity();
        String uid = member.getTeamMemberUID();

        //处理结果：检查收益并把多余的钱转移给队长
        ProfitCheckParams resultParams = new ProfitCheckParams();

        //订单更新入参
        UpdateOrderParamsBuilder paramsBuilder = UpdateOrderParams.builder().neddRefund(false);

        LOGGER.info(orderAction.getDesc());
        LOGGER.info("teamSequence：{},teamStatus：{},uid：{}", teamSequence, teamStatus,
                member.getTeamMemberUID());

        //查询已支付的订单记录
        Order payedOrder = getPayedOrder(uid, teamSequence);
        if(payedOrder == null){
            LOGGER.info("用户[{}]无已支付的RPG订单，忽略处理",uid);
            return resultParams;
        }
        Order tobeUpdateOrder = new Order();

        if (orderAction.equals(PREPARE_JOIN_TEAM_KICK) //待入团，被队长踢出车队
                || orderAction.equals(PREPARE_JOIN_TEAM_QUIT) //待入团，主动退出
                || orderAction.equals(PREPARE_JOIN_TEAM_DISMISSD) //待入团，队长解散车队
                || orderAction.equals(JOINED_TEAM_KICK) // 已入团，被队长踢出车队
                || orderAction.equals(JOINED_TEAM_DISMISSD)//已入团，队长解散车队
                || orderAction.equals(JOINED_TEAM_QUIT)) { //已入团，主动退出
            if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                //更新订单：取消+退款
                tobeUpdateOrder.setId(payedOrder.getId());
                tobeUpdateOrder.setStatus((byte)OrderStatusEnum.PAY_CANCEL.getCode());
                //设置预退款金额
                tobeUpdateOrder.setPreRefundAmount(payedOrder.getActualPaidAmount());
                sendRefundMessageToMQ(teamMemberStatus,tobeUpdateOrder, GameOrderType.RPG);
            }
            return resultParams;
        }else if (orderAction.equals(TEAM_STARTED_QUIT)) { //已开车，主动退出

            tobeUpdateOrder.setId(payedOrder.getId());
            if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                //更新订单：完成+不退款
                tobeUpdateOrder.setStatus((byte)OrderStatusEnum.FINISH.getCode());
            }
            if (UserIdentityEnum.BAOJI.getCode() == userIdentity
                    || UserIdentityEnum.LEADER.getCode() == userIdentity) {
                //更新订单：取消+无收益
                tobeUpdateOrder.setStatus((byte)OrderStatusEnum.PAY_CANCEL.getCode());
            }

            tobeUpdateOrder.setId(payedOrder.getId());

            paramsBuilder.order(tobeUpdateOrder).memberStatus(teamMemberStatus);
            ActionHandler.updateHandler = (params,resilt)-> updateOrder(params);
        } else if (orderAction.equals(TEAM_STARTED_DISMISSD) //已开车，队长解散车队
                || orderAction.equals(TEAM_STARTED_FINISH)) { //已开车，队长结束车队

            //所有订单完成
            tobeUpdateOrder.setId(payedOrder.getId());
            tobeUpdateOrder.setStatus((byte)OrderStatusEnum.FINISH.getCode());

            LOGGER.info("游戏结果：{}", GameResultEnum.fromCode(gameResult).getDesc());
            //如果胜利：完成老板订单+计算暴鸡收益
            if (gameResult == GameResultEnum.ROUNDS_VICTORY.getCode()) {

                if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                    //老板更新订单：完成+不退款
                    LOGGER.info("老板[{}]正常结束游戏，更新订单为完成+不退款", uid);
                    paramsBuilder.order(tobeUpdateOrder).memberStatus(teamMemberStatus);
                    ActionHandler.updateHandler = (params,resilt)  -> updateOrder(params);
                }
                if (UserIdentityEnum.BAOJI.getCode() == userIdentity
                        || UserIdentityEnum.LEADER.getCode() == userIdentity) {
                    //暴鸡更新订单：完成+结算收益
                    //获取结算收益参数
                    LOGGER.info("开始结算暴鸡[{}]收益", uid);
                    List<String> teamUids = teamMembers.stream()
                            .map(OrderTeamRPGMember::getTeamMemberUID)
                            .collect(Collectors.toList());
                    //获取已支付订单和开车后中途退出的老板订单
                    List<Order> orders = getOrders(teamUids, teamSequence);
                    //计算出暴鸡收益
                    IncomeCaculateParams caculateParams = IncomeCaculateParams.builder()
                            .payedOrders(orders)
                            .rpgTeamMembers(teamMembers)
                            .rpgMember(member)
                            .build();
                    InComeVo inComeVo = rpgInComeService.getIncome(caculateParams);

                    Integer profitAmout = inComeVo.getInComeAmounts();
                    resultParams.setProfitAmout(profitAmout);
                    resultParams.setPaySum(inComeVo.getPaySum());
                    if (UserIdentityEnum.LEADER.getCode() == userIdentity) {
                        resultParams.setUserIdentity(userIdentity);
                        resultParams.setUid(uid);
                    }
                    paramsBuilder.order(tobeUpdateOrder)
                            .memberStatus(teamMemberStatus)
                            .profitAmout(profitAmout);
                    ActionHandler.updateHandler = this::updateOrderAndProfit;
                }
            } else {
                //败和未打，如果此时车队里有中途退出的老板，需要结算收益给暴鸡
                List<Order> leaveTeamOrders = orderRepository.getLeaveTeamOrders(teamSequence);
                if (CollectionUtils.isNotEmpty(leaveTeamOrders)
                        && (UserIdentityEnum.BAOJI.getCode() == userIdentity
                        || UserIdentityEnum.LEADER.getCode() == userIdentity)) {
                    LOGGER.info("车队[{}]有中途退出的老板订单，结算给暴鸡", teamSequence);
                    //计算出暴鸡收益
                    IncomeCaculateParams caculateParams = IncomeCaculateParams.builder()
                            .payedOrders(leaveTeamOrders)
                            .rpgTeamMembers(teamMembers)
                            .rpgMember(member)
                            .build();
                    InComeVo inComeVo = rpgInComeService.getIncome(caculateParams);

                    Integer profitAmout = inComeVo.getInComeAmounts();
                    resultParams.setProfitAmout(profitAmout);
                    resultParams.setPaySum(inComeVo.getPaySum());
                    if (UserIdentityEnum.LEADER.getCode() == userIdentity) {
                        resultParams.setUserIdentity(userIdentity);
                        resultParams.setUid(uid);
                    }
                    paramsBuilder.order(tobeUpdateOrder)
                            .memberStatus(teamMemberStatus)
                            .profitAmout(profitAmout);
                    ActionHandler.updateHandler = this::updateOrderAndProfit;
                } else {
                    LOGGER.debug("老板[{}]退款,暴鸡不结算此老板的金额", uid);
                    //老板更新订单：完成+退款
                    if (UserIdentityEnum.BOSS.getCode() == userIdentity) {
                        //设置预退款金额
                        tobeUpdateOrder.setPreRefundAmount(payedOrder.getActualPaidAmount());
                        sendRefundMessageToMQ(teamMemberStatus,tobeUpdateOrder,GameOrderType.RPG);
                        return resultParams;
                    }

                    //暴鸡更新订单：完成+无收益
                    if (UserIdentityEnum.BAOJI.getCode() == userIdentity
                            || UserIdentityEnum.LEADER.getCode() == userIdentity) {

                       paramsBuilder.order(tobeUpdateOrder).memberStatus(teamMemberStatus);
                        ActionHandler.updateHandler = (params,resilt)  -> updateOrder(params);
                    }
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

    //获取已支付订单和开车后中途退出的老板订单
    private List<Order> getOrders(List<String> teamUids, String teamSequeue) {
        List<Order> orders = orderRepository.getByTeamSequeue(teamSequeue);
        if (CollectionUtils.isEmpty(orders)) {
            throw new BusinessException(BizExceptionEnum.ORDER_TEAM_ITEM_EMPTY);
        } else {
            //结算收益的时候在车队里
            orders = orders.stream()
                    //过虑车队成员
                    .filter(f -> {
                        boolean contains = teamUids.contains(f.getUid());
                        boolean payed = true;
                        boolean out = true;
                        //可能一个老板有多个订单，过虑已支付的老板
                        if (f.getOrderItemTeamRPG().getUserIdentity()
                                .equals(UserIdentityEnum.BOSS.getCode())) {
                            payed = f.getStatus().intValue() == OrderStatusEnum.PAYED.getCode();
                            //加上中中途退出的老板
                            out = f.getStatus().intValue() == OrderStatusEnum.FINISH.getCode()
                                    && f.getOrderItemTeamRPG().getUserStatus().intValue()
                                       == TeamOrderRPGActionEnum.TEAM_STARTED_QUIT.getCode();
                            if (out) {
                                LOGGER.info("车队[{}]有中途退出的老板[{}]订单，结算给暴鸡", teamSequeue, f.getUid());
                            }
                        }
                        return (contains && payed) || out;
                    })
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(orders)) {
                throw new BusinessException(BizExceptionEnum.ORDER_PAYED_AND_LEAVETEAM_EMPTY);
            }
        }
        return orders;
    }

    @Override
    public TeamOrderVo getOrderDetailsBySequence(String uid, String sequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid, sequence);
        //1: 第一步根据订单序列号查询出改订单
        Order order = getBySequenceId(sequence);
        return doMadeTeamOrderDetails(uid, order);
    }

    @Override
    public TeamOrderVo getOrderDetailsByUidAndTeamSequence(String uid, String teamSequence) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid, teamSequence);
        if (!cacheManager.exists(RedisKey.UPDATED_ORDER + teamSequence)) {
            LOGGER.info(">>订单详情没有检测到redisKey:[" + RedisKey.UPDATED_ORDER + teamSequence + "]");
            return new TeamOrderVo();
        }
        //1: 第一步根据订单序列号查询出改订单
        Order order = orderRepository.selectLastUserOrder(uid, teamSequence);
        return doMadeTeamOrderDetails(uid, order);
    }

    private TeamOrderVo doMadeTeamOrderDetails(String uid, Order order) {
        ValidateAssert.hasNotNull(BizExceptionEnum.ORDER_NOT_EXIST, order);
        ValidateAssert.isTrue(uid.equals(order.getUid()), BizExceptionEnum.OPERATE_PERSON_DIFFER);
        ValidateAssert.hasNotNull(BizExceptionEnum.ORDER_NOT_EXIST, order.getOrderItemTeamRPG());
        //2：第二步，拿到车队订单序列号,并通过订单序列号查询出其它人的信息
        String teamSequence = order.getOrderItemTeamRPG().getTeamSequeue();
        //TODO 查询出其它人的订单信息
        List<Order> otherOrders = new ArrayList<>();
        //如果是老板订单且在开车之后的订单，才查出其他人的开车后订单，否则不查询
        if (UserIdentityEnum.BOSS.getCode() == order.getOrderItemTeamRPG().getUserIdentity()) {
            if (OrderTeamStatus.ALREADY_STATUS.getCode() == order.getOrderItemTeamRPG().getTeamStatus()) {
                otherOrders = orderRepository
                        .selectTeamOtherOrders(teamSequence, order.getSequeue());
            }
        } else {
            //暴鸡订单一定是已开车后的订单
            otherOrders = orderRepository.selectTeamOtherOrders(teamSequence, order.getSequeue());
        }
        //:3：第三步 根据查询者的身份，组装不同的数据
        TeamOrderVo vo = null;
        if (UserIdentityEnum.BOSS.getCode() == order.getOrderItemTeamRPG().getUserIdentity()) {
            vo = createBossOrderDetails(order, otherOrders);
        } else {
            vo = createBaoJiOrderDetails(order, otherOrders);
        }
        //
        return vo;
    }

    private TeamOrderVo createBaoJiOrderDetails(Order order, List<Order> otherOrders) {
        OrderItemTeamRPG owerOrderIteam = order.getOrderItemTeamRPG();
        TeamOrderVo teamOrderVo = new TeamOrderVo();
        teamOrderVo.setGameName(owerOrderIteam.getGameName());
        ResponsePacket<TeamGameResultVO> resultResponse = rpgTeamServiceClient
                .getGamingTeamGameResult(owerOrderIteam.getTeamSequeue());
        ValidateAssert.isTrue(resultResponse.responseSuccess(), BizExceptionEnum.TEAM_NOT_EXIST);
        teamOrderVo.setGameResult(resultResponse.getData().getGameResultCode());
        teamOrderVo.setRaidName(owerOrderIteam.getRaidName());
        teamOrderVo.setZoneAcrossName(owerOrderIteam.getZoneAcrossName());

        //批量查找用户信息
        Map<String, UserSessionContext> userInfoMap = this.getUserInfos(order, otherOrders);
        TeamMemberOrderVo owerOrder = new TeamMemberOrderVo();
        owerOrder.setUid(order.getUid());
        owerOrder.setUserIdentity(owerOrderIteam.getUserIdentity());
        owerOrder.setBaojiLevel(owerOrderIteam.getUserBaojiLevel());
        owerOrder.setIncomeAmount(owerOrderIteam.getPrice());
        owerOrder.setOrderStatus(order.getStatus());
        this.setOtherBaseInfos(owerOrder, userInfoMap.get(owerOrder.getUid()));
        List<TeamMemberOrderVo> otherVos = new ArrayList<>();
        // 下面组装其它队员信息列表
        for (Order otherOrder : otherOrders) {
            OrderItemTeamRPG otherOrderItemTeamRPG = otherOrder.getOrderItemTeamRPG();
            if (otherOrderItemTeamRPG == null) {
                continue;
            }
            TeamMemberOrderVo otherVo = new TeamMemberOrderVo();
            //再组装一些基础信息
            otherVo.setUid(otherOrder.getUid());
            otherVo.setUserIdentity(otherOrderItemTeamRPG.getUserIdentity());
            otherVo.setOrderStatus(otherOrder.getStatus());
            this.setOtherBaseInfos(otherVo, userInfoMap.get(otherVo.getUid()));
            //组装数据
            if (otherOrderItemTeamRPG.getUserIdentity().intValue() == UserIdentityEnum.BOSS
                    .getCode()) {
                //设置老板付款信息
                otherVo.setActualPaidAmount(
                        otherOrder.getActualRefundAmount() == 0 ? otherOrder.getActualPaidAmount()
                                - otherOrder
                                .getPreRefundAmount() : otherOrder.getActualPaidAmount());
            } else {
                otherVo.setBaojiLevel(otherOrderItemTeamRPG.getUserBaojiLevel());
            }
            otherVos.add(otherVo);
        }
        //按照队员身份和暴鸡等级排序
        sortBossQueryOtherList(otherVos);
        teamOrderVo.setOwnerOrder(owerOrder);
        teamOrderVo.setOtherOrders(otherVos);
        return teamOrderVo;
    }

    private TeamOrderVo createBossOrderDetails(Order order, List<Order> otherOrders) {
        /**
         * 老板订单中，老板所有者会有订单信息，但他人订单中只显示基本信息以及暴击等级，不输出金额等参数
         */
        OrderItemTeamRPG owerOrderIteam = order.getOrderItemTeamRPG();
        TeamOrderVo teamOrderVo = new TeamOrderVo();
        teamOrderVo.setGameName(owerOrderIteam.getGameName());
        //获取比赛结果
        ResponsePacket<TeamGameResultVO> resultResponse = rpgTeamServiceClient
                .getGamingTeamGameResult(owerOrderIteam.getTeamSequeue());
        ValidateAssert.isTrue(resultResponse.responseSuccess(), BizExceptionEnum.TEAM_NOT_EXIST);
        teamOrderVo.setGameResult(resultResponse.getData().getGameResultCode());
        teamOrderVo.setRaidName(owerOrderIteam.getRaidName());
        teamOrderVo.setZoneAcrossName(owerOrderIteam.getZoneAcrossName());
        Map<String, UserSessionContext> userInfoMap = getUserInfos(order, otherOrders);
        TeamMemberOrderVo owerOrder = new TeamMemberOrderVo();
        owerOrder.setUid(order.getUid());

        owerOrder.setActualPaidAmount(
                order.getActualRefundAmount() == 0 ? order.getActualPaidAmount() - order
                        .getPreRefundAmount() : order.getActualPaidAmount());
        owerOrder.setUserIdentity(owerOrderIteam.getUserIdentity());
        owerOrder.setOrderStatus(order.getStatus());
        owerOrder.setDiscountAmount(order.getDiscountAmount());
        this.setOtherBaseInfos(owerOrder, userInfoMap.get(owerOrder.getUid()));
        //其他用户
        List<TeamMemberOrderVo> otherVos = new ArrayList<>();
        for (Order otherOrder : otherOrders) {
            OrderItemTeamRPG orderItemTeamRPG = otherOrder.getOrderItemTeamRPG();
            if (orderItemTeamRPG == null) {
                continue;
            }
            TeamMemberOrderVo otherVo = new TeamMemberOrderVo();
            //再组装一些基础信息
            otherVo.setUid(otherOrder.getUid());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizExceptionEnum updateOrder(WeiXinRefundPacket weiXinRefundPacket) {
        BizExceptionEnum result = BizExceptionEnum.SUCCESS;
        try {
            String orderSequence = weiXinRefundPacket.getOut_trade_no();
            LOGGER.info("开始更新订单[{}]状态",orderSequence);
            Order order = getBySequenceId(orderSequence);
            if (order == null) {
                LOGGER.error(BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg() + ",orderId={}",
                        orderSequence);
                return BizExceptionEnum.ORDER_NOT_EXIST;
            }

            if (null == order.getActualPaidAmount() || order.getActualPaidAmount() <= 0) {
                LOGGER.error(String.format(BizExceptionEnum.INVALID_PAYED_AMOUT.getErrMsg(),
                        order.getActualPaidAmount()));
                throw new BusinessException(BizExceptionEnum.INVALID_PAYED_AMOUT,
                        order.getActualPaidAmount() + "");
            }

            long incr = 0;
            String[] intervals = updateOrderretryInterval.split("/");
            do {
                try {
                    Order orderParam = new Order();
                    //实际支付金额：剔除退款
                    int actuaPaidAmout =
                            order.getActualPaidAmount() - weiXinRefundPacket.getRefund_fee();
                    orderParam.setActualPaidAmount(actuaPaidAmout);//实际退款金额
                    orderParam.setActualRefundAmount(weiXinRefundPacket.getRefund_fee());//实际退款金额
                    orderParam.setGmtModified(new Date());//订单修改时间
                    orderParam.setStatus((byte)OrderStatusEnum.PAY_CANCEL.getCode());//订单状态
                    orderParam.setId(order.getId());

                    orderRepository.updateByPrimaryKeySelective(orderParam);
                    LOGGER.info("订单更新成功(覆盖不为空的字段)：{}", orderParam.toString());

                    //删除重试次数
                    if (cacheManager.exists(RedisKey.REFUND4NOTIFY_RETRY + order.getSequeue())) {
                        cacheManager.del(RedisKey.REFUND4NOTIFY_RETRY + order.getSequeue());
                    }
                    break;
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    incr = cacheManager.incr(RedisKey.REFUND4NOTIFY_RETRY + order.getSequeue());
                    try {
                        LOGGER.error("订单[{}]状态更新失败，{}s后,重试：{}次",
                                orderSequence,intervals[(int) incr - 1], incr);
                        Thread.sleep(Long.valueOf(intervals[(int) incr - 1]) * 1000);
                    } catch (InterruptedException e2) {
                        LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e2);
                        break;
                    }
                }
            } while (incr < intervals.length);

            if (cacheManager.exists(RedisKey.REFUND4NOTIFY_RETRY + order.getSequeue())) {
                LOGGER.error("订单[{}]重试次数已达阀值：{}，不在重试",
                        orderSequence,intervals.length);
                cacheManager.del(RedisKey.REFUND4NOTIFY_RETRY + order.getSequeue());
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new BusinessException(BizExceptionEnum.ORDER_FEFUND_NOTIFY_FAIL);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizExceptionEnum updateOrder(WeiXinPayConfirmPacket weiXinPayConfirmPacket) {
        try {
            String orderSequence = weiXinPayConfirmPacket.getOut_trade_no();
            Order order = orderRepository
                    .getBySequenceId(orderSequence);
            if (order == null) {
                LOGGER.error(BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg() + ",微信单号={}，orderId={}",
                        weiXinPayConfirmPacket.getTransaction_id(),
                        orderSequence);
                return BizExceptionEnum.ORDER_NOT_EXIST;
            }

            //如果已经支付，直接返回
            LOGGER.info("是否已支付：内部订单状态(2表示已支付)={} 并且 微信返回订单状态(SUCCESS表示已支付)={}",
                    order.getStatus(),
                    weiXinPayConfirmPacket.getTrade_state());
            if (order.getStatus().equals(OrderStatusEnum.PAYED.getCode())
                    && weiXinPayConfirmPacket.getTrade_state()
                    .equals(CommonConstants.WEIXIN_PAY_SUCCESS)) {
                LOGGER.info("订单[{}]已支付，直接返回成功。",orderSequence);
                return BizExceptionEnum.SUCCESS;
            }
            //目前对象工具类不支持内部类拷贝，手动设置需要的值(不使用Json转换)
            Date responseTime = DateUtil
                    .str2Date(weiXinPayConfirmPacket.getTime_end(), DateUtil.SIMPLE_FORMATTER);
            order.setResponseTime(responseTime);
            order.setOuterTradeNo(weiXinPayConfirmPacket.getTransaction_id());//第三方订单号
            order.setActualPaidAmount(weiXinPayConfirmPacket.getTotal_fee());//实际支付金额
            order.setGmtModified(new Date());//订单修改时间
            order.setResponseTime(responseTime); //订单响应时间
            order.setPaymentTime(new Date());//支付成功时间

            updateOrder(order);

        } catch (BusinessException e) {
            LOGGER.error(e.getErrMsg(), e);
            throw new BusinessException(BizExceptionEnum.ORDER_PAID_NOTIFY_FAIL);
        }

        return BizExceptionEnum.SUCCESS;
    }

    private void setOtherMoreInfos(TeamMemberOrderVo otherVo, Order other) {
        OrderItemTeamRPG orderItemTeamRPG = other.getOrderItemTeamRPG();
        otherVo.setUserIdentity(orderItemTeamRPG.getUserIdentity());
        otherVo.setBaojiLevel(orderItemTeamRPG.getUserBaojiLevel());
    }


    /**
     * 根据车队唯一标识
     *
     * 统计车队完成订单
     */
    @Override
    public Map<String, Long> getTeamComplitedBossOrderStatistics(List<String> teamSequences) {
        List<TeamOrderCount> teamOrderCounts = orderItemTeamRPGRepository
                .countTeamComplitedBossOrderStatisticsByTeamSequences(teamSequences);
        return teamOrderCounts.stream()
                .collect(
                        Collectors.toMap(TeamOrderCount::getTeamSequeue, TeamOrderCount::getCount));
    }

    /**
     * 统计工作室完成的老板订单的数量
     */
    @Override
    public Map<String, Long> getStudioUserComplitedBossOrderStatistics(
            List<StudioUserComplitedBossOrderStatisticGetParams> studioUserComplitedBossOrderStatisticGetParams) {
        //暴鸡 Uid -> 工作室Id
        Map<String, String> uidStudioIdMap = studioUserComplitedBossOrderStatisticGetParams
                .stream()
                .flatMap(it -> it.getUids().stream()
                        .collect(Collectors.toMap(e -> e, e -> it.getStudioId()
                        )).entrySet().stream())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        //全部暴鸡Id
        List<String> uids = studioUserComplitedBossOrderStatisticGetParams.parallelStream()
                .flatMap(it -> it.getUids().parallelStream()).collect(Collectors.toList());
        //去重的车队Id
        List<TeamSequenceUidVO> teamSequenceUidVOS = Optional
                .ofNullable(rpgTeamServiceClient.getBaojiTeamSequencesByUids(uids))
                .map(ResponsePacket::getData)
                .orElse(Collections.emptyList());

        //暴鸡Id -> 车队唯一标识
        Map<String, String> uidTeamSequenceMap = teamSequenceUidVOS.stream()
                .collect(Collectors.toMap(TeamSequenceUidVO::getUid,
                        TeamSequenceUidVO::getSequence));
        System.out.println(uidTeamSequenceMap);

        //车队唯一标识 -> 工作室Id
        Map<String, String> teamSequenceStudioIdMap = uidTeamSequenceMap.entrySet().stream()
                .collect(Collectors.toMap(Entry::getValue, e -> uidStudioIdMap.get(e.getKey())));

        //车队完成老板单数统计
        List<TeamOrderCount> teamOrderCounts = orderItemTeamRPGRepository
                .countTeamComplitedBossOrderStatisticsByTeamSequences(
                        new ArrayList<>(teamSequenceStudioIdMap.keySet()));
        return teamOrderCounts.stream()
                //按工作室分类
                .collect(Collectors
                        .groupingBy(toc -> teamSequenceStudioIdMap.get(toc.getTeamSequeue())))
                //统计工作室车队订单数
                .entrySet().parallelStream().collect(Collectors.toMap(Entry::getKey,
                        e -> e.getValue().parallelStream()
                                .filter(it -> Objects.nonNull(it.getCount()))
                                .mapToLong(TeamOrderCount::getCount)
                                .sum()));
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
                    //查询微信订单信息
                    ResponsePacket wxPayOrder = pythonRestClient.getWxPayOrder(orderSequence);
                    if(wxPayOrder != null &&
                            wxPayOrder.getCode() == CommonConstants.SUCCESS &&
                            wxPayOrder.getData() != null){
                        //TODO 灵异事件，各种尝试 无法转换，估计是下划线问题
//                        WeiXinPayConfirmPacket weiXinPayConfirmPacket = FastJsonUtils.fromMap(wxPayOrder.getData(),
//                                new TypeReference<WeiXinPayConfirmPacket>(){});

                        Map payMap = (Map) wxPayOrder.getData();

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
