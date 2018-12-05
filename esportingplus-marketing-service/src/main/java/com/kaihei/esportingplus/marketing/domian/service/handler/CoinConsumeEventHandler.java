package com.kaihei.esportingplus.marketing.domian.service.handler;

import com.kaihei.esportingplus.marketing.api.event.CoinConsumeEvent;
import org.springframework.stereotype.Component;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-10 19:15
 * @Description:
 */
@Component("coinConsumeEventHandler")
public class CoinConsumeEventHandler extends AbstractUserEventHandler<CoinConsumeEvent> {

    /*protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int STATUS_OFF = 0, BAOJI_COIN_AWARD_TYPE = 2;

    private static final String GCOIN_ORDER_TYPE = "004";

    @Autowired
    private MembersUserTaskService membersUserTaskService;

    @Autowired
    private MarketUserTaskAwardOrderRepository membersUserTaskAwardOrderRepository;

    @Autowired
    private BackStageServiceClient backStageServiceClient;

    @Autowired
    private TaskConfigServiceClient taskConfigServiceClient;

    @Autowired
    private MembersUserService membersUserService;

    private CacheManager cacheManager = CacheManagerFactory.create();*/

    @Override
    public boolean process(CoinConsumeEvent coinConsumeEvent) {
        /*String json = FastJsonUtils.toJson(coinConsumeEvent);
        avoidDupplicatePayOrder(coinConsumeEvent);
        // 从别的系统找出邀请配置
        List<InvitionShareConfigVo> configList = getConfigs();
        InvitionShareConfigVo config = getTypeConfig(configList,
                ShareCategoryCodeEnum.FRIENDS_CONSUME.getCode());
        if (config == null) {
            logger.info("cmd=CoinConsumeEventHandler.process | msg=没有找到对应配置任务 | req={}", json);
            return false;
        }
        if (STATUS_OFF == config.getStatus()) {//.检查奖励任务是不是已经下线了
            logger.info("cmd=CoinConsumeEventHandler.process | msg=任务下线了 | req={}", json);
            return false;
        }

        for (ConsumeUser consumeUser : coinConsumeEvent.getUsers()) {
            if (consumeUser == null) {
                logger.error("cmd=UserInvitShareController.shareTask | msg={} | order_no={}",
                        "user为空", coinConsumeEvent.getPayOrderNo());
                continue;
            }
            if (StringUtils.isEmpty(consumeUser.getUid())) {
                logger.error("cmd=UserInvitShareController.shareTask | msg={} | order_no={}",
                        "uid非法", coinConsumeEvent.getPayOrderNo());
                continue;
            }
            if (consumeUser.getCoin() == null) {
                logger.error("cmd=UserInvitShareController.shareTask | msg={} | order_no={}",
                        "coin非法", coinConsumeEvent.getPayOrderNo());
                continue;
            }

            Integer consumeUserId = membersUserService
                    .getUserIdByUid(consumeUser.getUid());
            if (consumeUserId == null) {
                logger.error("cmd=CoinConsumeEventHandler.process | msg=uid有误 | req={}",
                        json);
                continue;
            }
            //找出邀请关系
            MarketUserInvitingRelation relation = membersUserTaskService
                    .findRelationByUserId(consumeUserId);
            if (relation == null) {
                logger.error("cmd=CoinConsumeEventHandler.process | msg=该用户没有邀请人，不进行奖励操作 | req={}",
                        json);
                continue;
            }
            MembersUser invitedUser = membersUserService
                    .getMembersUserById(relation.getInvitingUserid());
            if (invitedUser == null) {
                logger.info("cmd=CoinConsumeEventHandler.process | msg=该用户没有邀请人 | req={}",
                        json);
                continue;
            }
            MarketUserTaskAccumualte accumulate = membersUserTaskService
                    .getOrCreateAccumulate(consumeUserId, MarketUserTaskAccumualte.COIN_CONSUME,
                            config.getOnlineTime());
            int accumulateCount = 0, awardSnapshot = 0;
            if (accumulate.getBatchDate().compareTo(config.getOnlineTime()) == 0) {
                accumulateCount = accumulate.getAccumulate() + consumeUser.getCoin();
                awardSnapshot = accumulate.getAwardSnapshot();
            } else {
                membersUserTaskService.createAccumulateHistroyByAcc(accumulate);//把记录变为历史记录
                accumulate.setBatchDate(config.getOnlineTime());
                accumulateCount = consumeUser.getCoin();
            }

            /**
             * 0.加金币数量+累计数量=总的累计数量
             * 1.总的累计数量-最近一次奖励时快照数量=需要计算的数量
             * 2.需要计算的数量/配置数量=需要增加多少倍的奖励
             * 3.需要计算的数量%配置数量=模的数量
             * 4.倍数*配置奖励数量=最终奖励数量
             * 如果有奖励时：
             *      5总累计数量-模的数量=最近一次奖励时快照数量
             * 例如：coin=10（加金币数量），accumulateCount=3（累计数量），
             *      awardSnapshot=2（最近一次奖励时快照数量）， configCount=2（配置数量），
             *      awardCount=3（配置奖励数量）
             * 则： 0.10+3=13，总的累计数量为13
             *      1.13-2=11，需要计算数量11个
             *      2.11/2=5， 倍数为5
             *      3.11%2=1， 模为1
             * 最后：4.5*3=15,最终奖励数量为15个
             *       5.13-1=12，最近一次奖励时快照数量为12
             *//*
            int calculateCount = accumulateCount - awardSnapshot;
            if (calculateCount < 0) {
                calculateCount = 0;
            }
            *//**
             * 配置项配单位是个，所以乘以100
             *//*
            int mod = 0, times = 0, configCount = 0, awardCount = 0;
            if (config.getConsumeAmount() != null) {
                configCount = ((new BigDecimal(config.getConsumeAmount())).multiply(new BigDecimal("100"))).intValue();
            }
            if (config.getRewardAmount() != null) {
                awardCount = ((new BigDecimal(config.getRewardAmount())).multiply(new BigDecimal("100"))).intValue();
            }
            if (configCount != 0) {
                mod = calculateCount % configCount;
                times = calculateCount / configCount;
            }
            accumulate.setAccumulate(accumulateCount);
            int totalAward = awardCount * times;//单位分
            if (totalAward != 0) {
                String awardString = String.valueOf(totalAward);
                boolean flag = awardBaojiCoin(invitedUser.getId(), invitedUser.getUid(),
                        awardString);
                if (flag) {
                    accumulate.setAwardSnapshot(accumulateCount - mod);//有兑奖情况成功时才更新兑奖快照
                    //异步IM通知
                    EventBus.post(
                            new CoinConsumeIMEvent(consumeUserId, invitedUser.getUid(),
                                    totalAward));
                } else {
                    //失败时，累计字段不累加当次数量
                    accumulate.setAccumulate(accumulateCount - consumeUser.getCoin());
                }
            }
            membersUserTaskService.updateAccumulate(accumulate);
        }*/
        return true;
    }

    /*private void avoidDupplicatePayOrder(CoinConsumeEvent coinConsumeEvent) {
        String payOrder = coinConsumeEvent.getPayOrderNo();
        String redisKey = String
                .format(UserRedisKey.COIN_CONSUME_PAY_ORDER_NO, payOrder);
        if (cacheManager.exists(redisKey)) {
            logger.error(
                    "cmd=CoinConsumeEventHandler.avoidDupplicatePayOrder | msg=重复爆鸡币消费订单 | payOrderNo={}",
                    payOrder);
            throw new BusinessException(BizExceptionEnum.COIN_CONSUME_AWARD_PAY_ORDERNO_DUPLICATE);
        }
        cacheManager.set(redisKey, "1", 180);//缓存秒数
    }

    private List<InvitionShareConfigVo> getConfigs() {
        ResponsePacket<List<InvitionShareConfigVo>> resp = taskConfigServiceClient
                .findAllShareTaskConfig();
        if (resp == null || resp.getData() == null) {
            logger.error("cmd=CoinConsumeEventHandler.getConfigs | msg=调用分享任务配置返回空 | resp={}",
                    JacksonUtils.toJson(resp));
            return null;
        }
        return resp.getData();
    }

    private boolean awardBaojiCoin(Integer userId, String uid, String totalAward) {
        boolean flag = false;
        String orderNo = System.currentTimeMillis() + BAOJI_COIN_AWARD_TYPE + Utils.random(10);
        GCoinBackStageRechargeParam param = new GCoinBackStageRechargeParam();
        param.setUserId(uid);
        param.setOutTradeNo(orderNo);
        param.setRechargeType(GCOIN_ORDER_TYPE);
        param.setOrderType("----");//默认值，必须有值
        param.setGcoinAmount(totalAward);

        BigDecimal bd = new BigDecimal(totalAward);
        //保留2位小数，后面的去掉，如2.356结果2.35
        BigDecimal result = bd.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN);

        param.setBody("好友消费暴击币奖励" + result.toString() + "个暴击币");
        param.setSubject("好友消费暴击币奖励");
        param.setMessage("好友消费暴击币奖励");
        ResponsePacket<GCoinRechargeVo> resp = null;
        int award = Integer.valueOf(totalAward);
        long start = 0;
        String req = JacksonUtils.toJson(param);
        try {
            logger.info("cmd=CoinConsumeEventHandler.awardBaojiCoin | msg=调用支付接口奖励暴击币开始 | req={}",
                    req);
            start = System.currentTimeMillis();
            resp = backStageServiceClient.createGcoinRecharge(param);
        } catch (Exception e) {
            logger.error(
                    "cmd=CoinConsumeEventHandler.awardBaojiCoin | msg=调用支付接口奖励暴击币异常 | req={} | cost time={}ms | exception={}",
                    req, System.currentTimeMillis() - start, e);
            recordOrder(award, orderNo, userId);
            return false;
        }
        if (resp == null || !resp.responseSuccess()) {
            logger.error(
                    "cmd=CoinConsumeEventHandler.awardBaojiCoin | msg=调用支付接口奖励暴击币失败 | req={} | resp={} | cost time={}ms",
                    req, JacksonUtils.toJson(resp), System.currentTimeMillis() - start);
            recordOrder(award, orderNo, userId);
            return false;
        }
        if (!"002".equals(resp.getData().getState())) {
            logger.error(
                    "cmd=CoinConsumeEventHandler.awardBaojiCoin | msg=调用支付接口奖励暴击币失败 | req={} | resp={} | cost time={}ms",
                    req, JacksonUtils.toJson(resp), System.currentTimeMillis() - start);
            recordOrder(award, orderNo, userId);
            return false;
        } else {
            flag = true;
            logger.info(
                    "cmd=CoinConsumeEventHandler.awardBaojiCoin | msg=调用支付接口奖励暴击币成功 | req={} | cost time={}ms",
                    req, System.currentTimeMillis() - start);
            //增加暴击币统计
            membersUserTaskService.incrStatistics(userId, null, Long.valueOf(award));
        }
        recordOrder(award, orderNo, userId);
        return flag;
    }

    private void recordOrder(Integer award, String orderNo, Integer userId) {
        //记录订单
        MarketUserTaskAwardOrder order = new MarketUserTaskAwardOrder();
        order.setAwardNum(award);
        order.setAwardType(BAOJI_COIN_AWARD_TYPE);
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        int i = membersUserTaskAwardOrderRepository.insertSelective(order);
        if (i < 0) {
            logger.error("cmd=CoinConsumeEventHandler.awardBaojiCoin | msg=插入订单失败 | data={}",
                    JacksonUtils.toJson(order));
        }
    }

    private InvitionShareConfigVo getTypeConfig(List<InvitionShareConfigVo> list, String code) {
        if (list == null) {
            return null;
        }
        InvitionShareConfigVo config = null;
        for (InvitionShareConfigVo vo : list) {
            if (code.equals(vo.getCategoryCode())) {
                config = vo;
                break;
            }
        }
        return config;
    }

    public static void main(String[] args) {
        System.out.println(0 / 1);
    }*/
}
