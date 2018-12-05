package com.kaihei.esportingplus.riskrating.consumer;


import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.riskrating.api.enums.SourceTypeEnum;
import com.kaihei.esportingplus.riskrating.api.params.RiskBlackCheckConsumerParams;
import com.kaihei.esportingplus.riskrating.domain.entity.RechargeErrorLog;
import com.kaihei.esportingplus.riskrating.repository.RechargeErrorLogRepository;
import com.kaihei.esportingplus.riskrating.service.RiskBlackRechargeService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 谢思勇
 */
@MQConsumer(topic = "esportingplus_recharge", tag = "recharge_black", consumerGroup = "risk_black_check")
public class RiskBlackCheckConsumer extends AbstractMQPushConsumer<RiskBlackCheckConsumerParams> {

    private final Logger log = LoggerFactory.getLogger(RiskBlackCheckConsumer.class);
    /**
     * 预警内容
     *
     * TODO 改为从数据库获取
     */
    private final String WARNING_MSG = "{ \"msgtype\": \"text\", \"text\": { \"content\": \"风控预警提示： 今日平台充值金额已达风控阀值。\" } }";
    /**
     * 最大消息消费次数
     */
    private final int MAX_RECONSUME_TIMES = 5;


    @Autowired
    private RiskBlackRechargeService riskBlackRechargeService;
    @Resource
    private RechargeErrorLogRepository rechargeErrorLogRepository;

    /**
     * 消费充值MQ
     *
     * 累加用户当日充值GCoin 和 所有用户总充值GCoin
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean process(RiskBlackCheckConsumerParams recharge, Map<String, Object> extMap) {
        //消息Id 数据库唯一索引 防止重复消费
        String msgId = (String) extMap.get(MessageExtConst.PROPERTY_EXT_MSG_ID);
        recharge.setMsgId(msgId);
        //判断是否超过最大重复消费次数
        if (this.overReconsume(recharge, extMap)) {
            return true;
        }
        String dateKey = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        //需求 不是IOS记录直接返回
        if (!recharge.getSourceId().equals(SourceTypeEnum.IOS)) {
            return true;
        }
        riskBlackRechargeService.rechargeRiskAlerm(recharge);
        return true;
    }

    /**
     * 超过最大消费次数
     */
    private boolean overReconsume(RiskBlackCheckConsumerParams recharge,
            Map<String, Object> extMap) {
        //消费次数
        int reconsumeTimes = (int) extMap.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);
        log.info("风控黑产消费消费MQ：重试次数为{} \n 消息: {}", reconsumeTimes, recharge);
        if (reconsumeTimes < MAX_RECONSUME_TIMES) {
            return false;
        }
        handleOverReconsume(recharge, reconsumeTimes);
        return true;
    }

    /**
     * 超过最大消费重试补偿机制
     */
    private void handleOverReconsume(RiskBlackCheckConsumerParams recharge, int reconsumeTimes) {
        log.warn("消费超过重试次数：{}", recharge);
        RechargeErrorLog rechargeErrorLog = RechargeErrorLog.newBuilder()
                .group("risk_black_check")
                .topic("esportingplus_recharge")
                .tag("recharge_black")
                .reconsumeTimes(reconsumeTimes)
                .msgId(recharge.getMsgId())
                .msg(JSON.toJSONString(recharge)).build();

        rechargeErrorLogRepository.save(rechargeErrorLog);
    }
}
