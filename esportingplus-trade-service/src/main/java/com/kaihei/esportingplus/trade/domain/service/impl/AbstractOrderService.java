package com.kaihei.esportingplus.trade.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BaojiLevelEnum;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.gamingteam.api.vo.BaojiInfoBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamStartOrderBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPBaojiInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPTeamStartOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGBaojiInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import com.kaihei.esportingplus.payment.api.enums.PayChannelEnum;
import com.kaihei.esportingplus.payment.api.params.OrderIncomeParams;
import com.kaihei.esportingplus.trade.api.enums.ChannelEnum;
import com.kaihei.esportingplus.trade.api.enums.OrderStatusEnum;
import com.kaihei.esportingplus.trade.api.enums.OrderTeamStatus;
import com.kaihei.esportingplus.trade.api.enums.ServiceTypeEnum;
import com.kaihei.esportingplus.trade.api.enums.TeamOrderPVPActionEnum;
import com.kaihei.esportingplus.trade.api.enums.TeamOrderRPGActionEnum;
import com.kaihei.esportingplus.trade.api.params.CheckCouponParams;
import com.kaihei.esportingplus.trade.api.params.CheckTeamMemberPayedParams;
import com.kaihei.esportingplus.trade.api.vo.PVPGameResult;
import com.kaihei.esportingplus.trade.api.vo.PVPTeamMemberBaseOrderVO;
import com.kaihei.esportingplus.trade.api.vo.TeamMemberOrderVo;
import com.kaihei.esportingplus.trade.common.ProfitCheckParams;
import com.kaihei.esportingplus.trade.common.TradeConstants;
import com.kaihei.esportingplus.trade.common.UpdateOrderParams;
import com.kaihei.esportingplus.trade.data.manager.PythonRestClient;
import com.kaihei.esportingplus.trade.data.repository.OrderItemTeamPVPFreeRepository;
import com.kaihei.esportingplus.trade.data.repository.OrderItemTeamPVPRepository;
import com.kaihei.esportingplus.trade.data.repository.OrderItemTeamRPGRepository;
import com.kaihei.esportingplus.trade.data.repository.OrderRefundRecordRepositry;
import com.kaihei.esportingplus.trade.data.repository.OrderRepository;
import com.kaihei.esportingplus.trade.domain.entity.Order;
import com.kaihei.esportingplus.trade.domain.entity.OrderCoupon;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamPVP;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamPVPFree;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamRPG;
import com.kaihei.esportingplus.trade.domain.entity.OrderRefundRecord;
import com.kaihei.esportingplus.trade.domain.service.CouponService;
import com.kaihei.esportingplus.trade.domain.service.OrderService;
import com.kaihei.esportingplus.trade.enums.BusinessTypeEnum;
import com.kaihei.esportingplus.trade.enums.GameOrderType;
import com.kaihei.esportingplus.trade.enums.PayMentTypeEnum;
import com.kaihei.esportingplus.trade.event.SendProfitEvent;
import com.kaihei.esportingplus.trade.mq.message.RefundMessage;
import com.kaihei.esportingplus.trade.mq.producer.RefundOrderProducer;
import com.kaihei.esportingplus.user.api.feign.UserServiceClient;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

/**
 *@Description: 订单服务公共抽象接口
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/11/6 11:44
*/
public abstract class AbstractOrderService implements OrderService {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderRefundRecordRepositry orderRefundRecordRepositry;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrderItemTeamPVPRepository orderItemTeamPVPRepository;

    @Autowired
    private OrderItemTeamPVPFreeRepository orderItemTeamPVPFreeRepository;

    @Autowired
    private OrderItemTeamRPGRepository orderItemTeamRPGRepository;

    @Autowired
    private RefundOrderProducer refundOrderProducer;

    @Autowired
    protected PythonRestClient pythonRestClient;

    @Autowired
    private UserServiceClient userServiceClient;

    protected static final CacheManager cacheManager = CacheManagerFactory.create();

    @Value("${python2java.updateOrder.retryInterval}")
    protected String updateOrderretryInterval;

    @Value("${python.refund.delay}")
    private String refundDelay;

    @Autowired
    protected SnowFlake snowFlake;

    @Override
    public Order getBySequenceId(String id) {
        return orderRepository.getBySequenceId(id);
    }

    @Override
    public Order getPvpOrderBySequenceId(String sequence) {
        return orderRepository.getPvpOrderBySequenceId(sequence);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(Order orderParm) {
        try {
            Order order = getBySequenceId(orderParm.getSequeue());
            if (order == null) {
                LOGGER.error(BizExceptionEnum.ORDER_NOT_EXIST.getErrMsg() + ",微信单号={}，orderId={}",
                        orderParm.getOuterTradeNo(),orderParm.getSequeue());
                return;
            }

            //如果订单状态在车队服务确认支付的时候已更新，则直接返回
            if(order.getStatus().intValue() == OrderStatusEnum.PAYED.getCode()){
                LOGGER.info("订单状态在车队服务确认支付的时候已更新为已支付，忽略处理。");
                return;
            }

            String orderSequence = order.getSequeue();
            LOGGER.info("开始更新订单[{}]状态", orderSequence);
            //重试: 基于redis自增重试
            long incr = 0;
            String[] intervals = updateOrderretryInterval.split("/");
            do {
                try {
                    order.setStatus((byte)OrderStatusEnum.PAYED.getCode());
                    orderRepository.updateByPrimaryKeySelective(order);
                    LOGGER.info("订单[{}]状态更新成功",orderSequence);
                    //删除重试次数
                    if (cacheManager.exists(RedisKey.PAY4NOTIFY_RETRY + order.getSequeue())) {
                        cacheManager.del(RedisKey.PAY4NOTIFY_RETRY + order.getSequeue());
                    }

                    break;
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    incr = cacheManager.incr(RedisKey.PAY4NOTIFY_RETRY + order.getSequeue());
                    try {
                        LOGGER.error("订单[{}]状态更新失败，{}s后,重试：{}次",
                                orderSequence,intervals[(int) incr - 1], incr);
                        Thread.sleep(Long.valueOf(intervals[(int) incr]) * 1000);
                    } catch (InterruptedException e2) {
                        LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e2);
                        break;
                    }
                }
            } while (incr < intervals.length);

            if (cacheManager.exists(RedisKey.PAY4NOTIFY_RETRY + order.getSequeue())) {
                LOGGER.error("订单[{}]重试次数已达阀值：{}，不在重试",
                        orderSequence,intervals.length);
                cacheManager.del(RedisKey.PAY4NOTIFY_RETRY + order.getSequeue());
            }
            //消费订单优惠卷
            couponService.consumeCouoonParams(order.getUid(), order.getId(), order.getSequeue());
        } catch (BusinessException e) {
            LOGGER.error(e.getErrMsg(), e);
            throw new BusinessException(BizExceptionEnum.ORDER_PAID_NOTIFY_FAIL);
        }

    }

    public Order buildBossPrePayOrder(String uid,Integer price,List<Long> couponIds){
        LOGGER.info("开始创建订单，用户：{}，价格:{}，优惠券id:{}",uid,price,couponIds);
        //生成时间戳
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        String timeStamp = df.format(calendar.getTime());
        //生成雪花ID
        long snowId = snowFlake.nextId();
        //合成订单号
        String orderSequence = TradeConstants.ORDER_ID_PREFIEX + timeStamp + snowId;
        Order order = new Order();
        //订单用户uid
        order.setUid(uid);
        //订单序列号
        order.setSequeue(orderSequence);
        //订单业务类型
        order.setBusinessType((byte) BusinessTypeEnum.TEAM_ORDER.getCode());
        //订单状态
        order.setStatus((byte)OrderStatusEnum.READY_PAY.getCode());
       // createdBossOrderVO.setStatus(OrderStatusEnum.READY_PAY.getCode());
        //实付金额
        order.setActualPaidAmount(0);
        //计算预付金额
        order.setPrepaidAmount(price);
        //预设置优惠金额
        order.setDiscountAmount(0);
        //支付方式
        order.setPaymentType((byte) PayMentTypeEnum.WEIXIN_PAY.getCode());

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
                checkCouponParams.setUid(uid);
                checkCouponParams.setCouponId(couponId);
                checkCouponParams.setChannel(ChannelEnum.MINI_PROGRAME.getCode());
                checkCouponParams.setGameType(88);
                checkCouponParams.setPrepay(price);
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

            LOGGER.info("拼接前端返回值:{}", JSON.toJSONString(checkCouponParams));
        }
        return order;
    }

    @Override
    public void insertOrder(Order order) {
        orderRepository.insertOrder(order);
        //Order实体入库
        LOGGER.info("Order入库：{}", JSON.toJSONString(order));
    }

    @Override
    public void insertOrderAndCoupon(Order order, List<Long> couponIds) {
        this.insertOrder(order);
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

    public <T extends TeamStartOrderBaseVO,F extends  BaojiInfoBaseVO> List<Order> createBaojiOrders(T teamStartOrderBaseVo,List<F> baojiInfoBaseVos,GameOrderType gameOrderType){
        //生成时间戳
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        String timeStamp = df.format(calendar.getTime());
        List<Order> list = new ArrayList<>();
        for(BaojiInfoBaseVO baojiInfoBaseVO :baojiInfoBaseVos){
            Order order = new Order();
            order.setUid(baojiInfoBaseVO.getUid());
            order.setSequeue(TradeConstants.ORDER_ID_PREFIEX+timeStamp + String.valueOf(snowFlake.nextId()));
            order.setBusinessType((byte)BusinessTypeEnum.TEAM_ORDER.getCode());//订单业务类型
            order.setBusinessType((byte)BusinessTypeEnum.TEAM_ORDER.getCode());//订单业务类型
            order.setStatus((byte)OrderStatusEnum.READY.getCode());//订单状态
            order.setPrepaidAmount(0);//预付金额
            order.setActualPaidAmount(0);//实付金额
            order.setDiscountAmount(0);
            order.setPaymentType((byte)PayMentTypeEnum.WEIXIN_PAY.getCode());//支付方式
            //3.依次入库
            orderRepository.insertOrder(order);
            if(gameOrderType==GameOrderType.PVP){
                PVPTeamStartOrderVO pvpTeamStartOrderVO = (PVPTeamStartOrderVO) teamStartOrderBaseVo;
                PVPBaojiInfoVO pvpBaojiInfoVO = (PVPBaojiInfoVO) baojiInfoBaseVO;
                OrderItemTeamPVP orderItemTeamPVP = BeanMapper.map(pvpBaojiInfoVO,OrderItemTeamPVP.class);
                orderItemTeamPVP.setTeamSequeue(pvpTeamStartOrderVO.getSequence());
                orderItemTeamPVP.setGameId(pvpTeamStartOrderVO.getGameCode());
                orderItemTeamPVP.setGameName(pvpTeamStartOrderVO.getGameName());
                orderItemTeamPVP.setGameZoneId(pvpTeamStartOrderVO.getGameZoneId());
                orderItemTeamPVP.setGameZoneName(pvpTeamStartOrderVO.getGameZoneName());
                orderItemTeamPVP.setUserBaojiLevel(baojiInfoBaseVO.getBaojiLevel());
                orderItemTeamPVP.setPrice(0);
                orderItemTeamPVP.setTeamStatus((byte)OrderTeamStatus.ALREADY_STATUS.getCode());
                orderItemTeamPVP.setGameDanId(pvpBaojiInfoVO.getGameDanId());
                orderItemTeamPVP.setGameDanName(pvpBaojiInfoVO.getGameDanName());
                orderItemTeamPVP.setOrderId(order.getId());
                orderItemTeamPVP.setPlayMode(pvpTeamStartOrderVO.getPlayMode());
                orderItemTeamPVP.setSettlementType(pvpTeamStartOrderVO.getSettlementType());
                orderItemTeamPVP.setSettlementNumber(pvpTeamStartOrderVO.getSettlementNumber());
                orderItemTeamPVPRepository.insertSelective(orderItemTeamPVP);
            }else if(gameOrderType==GameOrderType.RPG){
                RPGTeamStartOrderVO RPGTeamStartOrderVo = (RPGTeamStartOrderVO) teamStartOrderBaseVo;
                RPGBaojiInfoVO rpgBaojiInfoVO = (RPGBaojiInfoVO) baojiInfoBaseVO;
                OrderItemTeamRPG orderItemTeamRPG = BeanMapper.map(rpgBaojiInfoVO, OrderItemTeamRPG.class);
                orderItemTeamRPG.setTeamSequeue(RPGTeamStartOrderVo.getSequence());
                orderItemTeamRPG.setGameCode(RPGTeamStartOrderVo.getGameCode());
                orderItemTeamRPG.setGameName(RPGTeamStartOrderVo.getGameName());
                orderItemTeamRPG.setRaidCode(RPGTeamStartOrderVo.getRaidCode());
                orderItemTeamRPG.setRaidName(RPGTeamStartOrderVo.getRaidName());
                orderItemTeamRPG.setUserBaojiLevel(rpgBaojiInfoVO.getBaojiLevel());
                orderItemTeamRPG.setPrice(0);
                orderItemTeamRPG.setTeamStatus((byte)OrderTeamStatus.ALREADY_STATUS.getCode());
                orderItemTeamRPG.setZoneAcrossCode(RPGTeamStartOrderVo.getZoneAcrossCode());
                orderItemTeamRPG.setZoneAcrossName(RPGTeamStartOrderVo.getZoneAcrossName());
                //暴鸡副本位信息
                orderItemTeamRPG.setRaidLocationCode(rpgBaojiInfoVO.getRaidLocationCode());
                orderItemTeamRPG.setRaidLocationName(rpgBaojiInfoVO.getRaidLocationName());
                //3.依次入库
                orderItemTeamRPG.setOrderId(order.getId());
                orderItemTeamRPGRepository.insertSelective(orderItemTeamRPG);
            }else {
                throw new BusinessException(BizExceptionEnum.ORDER_CATEGORY_ERROR);
            }
            list.add(order);

        }
        return list;
    }

    @Override
    public BizExceptionEnum checkTeamMemberPayed(
            CheckTeamMemberPayedParams checkTeamMemberPayedParams) {
        List<Order> orders = orderRepository.getByCondiction(checkTeamMemberPayedParams);
        //找出已支付的
        if (CollectionUtils.isEmpty(orders)) {
            return BizExceptionEnum.ORDER_NOT_PAY;
        }

        return BizExceptionEnum.SUCCESS;

    }

    @Override
    public void refundOrder(String sequence,GameOrderType gameOrderType) {
        Order order = getBySequenceId(sequence);
        LOGGER.info("订单[{}]支付后发现队员[{}]已不在车队中,发起退款({})。",
                sequence,order.getUid(),refundDelay);
        if(order.getStatus().intValue() == OrderStatusEnum.PAYED.getCode()){
            sendRefundMessageToMQ(TeamOrderRPGActionEnum.PAID_REFUND.getCode(),order,gameOrderType);
        }else{
            String[] delay = refundDelay.split("/");
            int delayLen = delay.length;
            int i;
            for ( i = 0; i < delayLen; i++) {
                try {
                    //延迟查询，等待py支付回调更新订单状态，实时性要求不高
                    LOGGER.info("延迟查询订单[{}]已支付状态，{}s后,查询：{}次",
                            sequence,
                            delay[i],
                            i+1);
                    Thread.sleep(Long.valueOf(delay[i]) * 1000);
                } catch (Exception e) {
                    LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e);
                    break;
                }
                order = orderRepository.getBySequenceId(sequence);
                if(order.getStatus().intValue() == OrderStatusEnum.PAYED.getCode()) {
                    sendRefundMessageToMQ(TeamOrderRPGActionEnum.PAID_REFUND.getCode(),order,gameOrderType);
                    break;
                }else {
                    LOGGER.error("订单[{}]不是已支付状态：{}", sequence,order.getStatus());
                }
            }
            //达到重试阈值，记录一下
            if(i == delayLen){
                LOGGER.warn("延迟查询订单[{}]已支付状态,次数已达阀值：{}", sequence,delayLen);
            }
        }
    }

    public static void main(String[] args) {
        GameOrderType gameOrderType = GameOrderType.RPG;
        System.out.println(gameOrderType.name().equals(GameOrderType.RPG.name())
                ? RocketMQConstant.TOPIC_RPG : RocketMQConstant.TOPIC_PVP);
    }
    /**
     * 发送 mq 消息
     */
    protected void sendRefundMessageToMQ(Integer memberStatus,Order order,GameOrderType gameOrderType) {
        //构建mq消息
        RefundMessage refundMessage = new RefundMessage();
        refundMessage.setMemberStatus(memberStatus);
        refundMessage.setOrder(order);
        String msgBody = FastJsonUtils.toJson(refundMessage);
        Message message = new Message();
        String topic = gameOrderType.name().equals(GameOrderType.RPG.name())
                ? RocketMQConstant.TOPIC_RPG : RocketMQConstant.TOPIC_PVP;
        message.setTopic(topic);
        message.setTags(RocketMQConstant.REFUND_ORDER_TAGS);
        message.setBody(msgBody.getBytes());
        SendResult sendResult = refundOrderProducer
                .sendMessageInTransaction(message, gameOrderType);
        if (ObjectTools.isNotNull(sendResult)
                && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            return;
        }
        // 直接抛出业务异常
        LOGGER.error(" >> 发送退款消息错误! tags: {}, teamSequence: {}, msgBody: {}",
                RocketMQConstant.REFUND_ORDER_TAGS, msgBody);
        throw new BusinessException(BizExceptionEnum.REFUND_MQ_SEND_FAIL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizExceptionEnum updateRefundOrder(Order refundOrder) {
        BizExceptionEnum result = BizExceptionEnum.SUCCESS;
        try {
            String orderSequence = refundOrder.getSequeue();
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
                    //实际支付金额：剔除退款
                    int actuaPaidAmout =
                            order.getActualPaidAmount() - refundOrder.getActualRefundAmount();
                    refundOrder.setActualPaidAmount(actuaPaidAmout);//实际支付金额

                    orderRepository.updateByPrimaryKeySelective(refundOrder);
                    LOGGER.info("订单更新成功(覆盖不为空的字段)：{}", refundOrder.toString());

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
    public List<Order> getByTeamSequenceIdAndUids(String id, List<String> uids,boolean needDelay) {
        List<Order> orders = null;
        try {
            if(needDelay){
                //TODO corePoolSize 配置不生效，一直为1个
                orders = ThreadPoolManager.INSTANCE
                        .getTaskTimer().schedule(() ->
                                orderRepository.getByTeamSequenceIdAndUids(id, uids),
                        //暴力的让它睡会儿，尽量让更新订单状态事务提交。
                        500,
                        TimeUnit.MILLISECONDS).get();
            }else{
                orders = orderRepository.getByTeamSequenceIdAndUids(id, uids);
            }

        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error(BizExceptionEnum.INTERNAL_SERVER_ERROR.getErrMsg(), e);
        }

        return orders;
    }

    protected Order getPayedOrder(String uid, String teamSequence) {
        List<Order> oleOrders = orderRepository
                .getByUidAndTeamSequeue(uid, teamSequence);

        if (CollectionUtils.isEmpty(oleOrders)) {
            throw new BusinessException(BizExceptionEnum.ORDER_TEAM_USER_EMPTY
                    , new String[]{teamSequence, uid});
        }

        //过虑已支付的
        Optional<Order> payed = oleOrders.stream()
                .filter(f -> {
                    if (f.getOrderItemTeamRPG().getUserIdentity()
                            .equals(UserIdentityEnum.BOSS.getCode())) {
                        return f.getStatus().intValue() == OrderStatusEnum.PAYED.getCode();
                    }
                    return true;
                })
                .findAny();
        return payed.orElse(null);
    }

    protected List<Order> getPayedOrders(List<String> uids, String teamSequence) {
        List<Order> paidOrders = orderRepository
                .getByTeamSequenceIdAndUids(teamSequence, uids);

        if (CollectionUtils.isEmpty(paidOrders)) {
            throw new BusinessException(BizExceptionEnum.ORDER_TEAM_USER_EMPTY
                    , new String[]{teamSequence, String.join(",", uids)});
        }

        //过虑已支付的
        return paidOrders.stream()
                .filter(f -> {
                    if (f.getOrderItemTeamPVP().getUserIdentity()
                            .equals(UserIdentityEnum.BOSS.getCode())) {
                        return f.getStatus().intValue() == OrderStatusEnum.PAYED.getCode();
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * @Description: 更新+收益
     * @param: [member, teamSequence, orderStatus, profitAmout]
     * @return: void
     * @throws:
     * @author Orochi-Yzh
     * @dateTime 2018/8/16 11:46
     */
    protected void updateOrderAndProfit(UpdateOrderParams params,
            ProfitCheckParams result) {

        //1.更新订单
        Order updateOrder = updateOrder(params);
        result.setOrderItermId(updateOrder.getOrderItemTeamRPG().getId());
        result.setOrderSequence(updateOrder.getSequeue());
        //发送收益暴鸡，除队长外：队长收益最后重新再计算
        if (result.getUserIdentity() != UserIdentityEnum.LEADER.getCode()) {
            OrderIncomeParams sendProfitParams = new OrderIncomeParams();
            sendProfitParams.setUserId(updateOrder.getUid());
            sendProfitParams.setOrderId(updateOrder.getSequeue());
            sendProfitParams.setAmount(params.getProfitAmout());
            EventBus.post(new SendProfitEvent(sendProfitParams));
        }
    }

    /**
     * @Description: 更新订单状态
     * @param: [member, teamSequence, orderStatus, isRefund, profitAmout]
     * @return: void
     * @throws:
     * @author Orochi-Yzh
     * @dateTime 2018/8/9 18:01
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrder(UpdateOrderParams params) {

        //更新订单主表
        Order order = params.getOrder();
        Integer profitAmout = params.getProfitAmout();
        Integer memberStatus = params.getMemberStatus();
        order.setGmtModified(new Date());

        //添加退款记录
        if(params.getNeddRefund()){
            OrderRefundRecord orderRefundRecord = new OrderRefundRecord();
            //TODO 我们需要APPID
            orderRefundRecord.setAppid("testAppid");
            orderRefundRecord.setChannelTag(
                    PayChannelEnum.lookup(order.getPaymentType().intValue()).getValue());
            orderRefundRecord.setOrderId(order.getId());
            orderRefundRecord.setOrderSequence(order.getSequeue());
            orderRefundRecord.setRefundFee(order.getPreRefundAmount());

            orderRefundRecord.setRefundSequence(params.getRefundSequence());
            orderRefundRecord.setReason(
                    TeamOrderPVPActionEnum.fromCode(params.getMemberStatus()).getDesc());
            orderRefundRecordRepositry.insertSelective(orderRefundRecord);
        }else{
            orderRepository.updateByPrimaryKeySelective(order);
        }

        //更新车队队员订单
        OrderItemTeamRPG orderItemTeamRPG = order.getOrderItemTeamRPG();
        if(orderItemTeamRPG != null){
            orderItemTeamRPG.setUserStatus(memberStatus.byteValue());
            orderItemTeamRPG.setGmtModified(new Date());

            if (profitAmout != null && profitAmout != 0) {
                LOGGER.info("暴鸡[{}]收益: {}", orderItemTeamRPG.getUid(), profitAmout);
                orderItemTeamRPG.setPrice(profitAmout);
            }

            orderItemTeamRPGRepository.updateByPrimaryKeySelective(orderItemTeamRPG);
        }

        OrderItemTeamPVP orderItemTeamPVP = order.getOrderItemTeamPVP();
        if(orderItemTeamPVP != null){
            orderItemTeamPVP.setUserStatus(memberStatus.byteValue());
            orderItemTeamPVP.setGmtModified(new Date());

            if (profitAmout != null && profitAmout != 0) {
                LOGGER.info("暴鸡[{}]收益: {}", orderItemTeamPVP.getUid(), profitAmout);
                orderItemTeamPVP.setPrice(profitAmout);
            }

            orderItemTeamPVPRepository.updateByPrimaryKeySelective(orderItemTeamPVP);
        }

        OrderItemTeamPVPFree orderItemTeamPVPFree = order.getOrderItemTeamPVPFree();
        if(orderItemTeamPVPFree != null){
            orderItemTeamPVPFree.setUserStatus(memberStatus.byteValue());
            orderItemTeamPVPFree.setGmtModified(new Date());

            if (profitAmout != null && profitAmout != 0) {
                LOGGER.info("暴鸡[{}]收益: {}", orderItemTeamPVPFree.getUid(), profitAmout);
                orderItemTeamPVPFree.setPrice(profitAmout);
            }

            orderItemTeamPVPFreeRepository.updateByPrimaryKeySelective(orderItemTeamPVPFree);
        }

        return order;
    }

    protected Map<String, UserSessionContext> getUserInfos(Order order, List<Order> otherOrders) {
        //过滤相同的uid用于批量查询用户信息
        List<String> uidList = new ArrayList<>();
        uidList.add(order.getUid());
        otherOrders.forEach(o -> {
            if (!uidList.contains(o.getUid())) {
                uidList.add(o.getUid());
            }
        });
        ResponsePacket<List<UserSessionContext>> response = userServiceClient
                .getUserInfosByUids(uidList);
        ValidateAssert.isTrue(response.responseSuccess(), BizExceptionEnum.HYSTRIX_SERVER);
        //为便于下面快速查找，转HashMap
        Map<String, UserSessionContext> userInfoMap = new HashMap<>();
        List<UserSessionContext> userInfos = response.getData();
        if (userInfos != null) {
            for (UserSessionContext info : userInfos) {
                userInfoMap.put(info.getUid(), info);
            }
        }
        return userInfoMap;
    }
    protected void setOtherBaseInfos(TeamMemberOrderVo owerOrder, UserSessionContext userInfo) {
        if (userInfo != null) {
            owerOrder.setAvatar(userInfo.getAvatar());
            owerOrder.setSex(userInfo.getSex());
            owerOrder.setUsername(userInfo.getUsername());
        }
    }
/*    protected PVPTeamMemberBaseOrderVO setPVPBaseInfos(String userName,Integer baojiLevel,Byte userIdentity, UserSessionContext userInfo){
        PVPTeamMemberBaseOrderVO owerOrder = new PVPTeamMemberBaseOrderVO();
        owerOrder.setUserName(userName);
        BaojiLevelEnum levelEnum = BaojiLevelEnum.getByCode(baojiLevel);
        owerOrder.setBaojiLevelZh(levelEnum==null?"":BaojiLevelEnum.getByCode(baojiLevel).getFrontZh());
        owerOrder.setBaojiLevel(baojiLevel);
        owerOrder.setUserIdentity(userIdentity);
        owerOrder.setIsLeader(UserIdentityEnum.LEADER.getCode()==userIdentity.intValue());
        owerOrder.setOrderCountZh("");
        if (userInfo != null) {
            owerOrder.setUid(userInfo.getUid());
            owerOrder.setAvatar(userInfo.getAvatar());
        }
        return owerOrder;
    }*/
    protected <T extends TeamMemberOrderVo>void sortBossQueryOtherList(List<T> orderVos) {
        Collections.sort(orderVos, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                if (o1.getUserIdentity().compareTo(o2.getUserIdentity()) > 0) {
                    return -1;
                } else if (o1.getUserIdentity().equals(o2.getUserIdentity())) {
                    return 0;
                }
                return 1;
            }
        });
    }

}
