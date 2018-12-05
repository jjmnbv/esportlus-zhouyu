package com.kaihei.esportingplus.marketing.domian.service.handler;

import com.kaihei.esportingplus.api.enums.DictionaryCodeEnum;
import com.kaihei.esportingplus.api.feign.TaskConfigServiceClient;
import com.kaihei.esportingplus.api.vo.freeteam.InvitionShareConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.marketing.api.event.UserExemptionEvent;
import com.kaihei.esportingplus.marketing.data.repository.MarketUserExemptionRecordRepository;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserExemptionRecord;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserFreeCouponsService;
import com.kaihei.esportingplus.user.api.feign.UserInfoServiceClient;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zl.zhao
 * @description:新版本用户启动免单操作
 * @date: 2018/10/25 17:13
 */
@Component("marketUserExemptionEventHandler")
public class MarketUserExemptionEventHandler extends AbstractUserEventHandler<UserExemptionEvent>{

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int STATUS_OFF = 0;

    @Autowired
    private UserInfoServiceClient userInfoServiceClient;

    @Autowired
    private TaskConfigServiceClient taskConfigServiceClient;

    @Autowired
    private MarketUserExemptionRecordRepository exemptionRecordRepository;

    @Autowired
    private MarketUserFreeCouponsService couponsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean process(UserExemptionEvent userExemptionEvent) {
        String json = FastJsonUtils.toJson(userExemptionEvent);
        if(userExemptionEvent ==null || userExemptionEvent.getUid() == null){
            logger.info("cmd=MarketUserExemptionEventHandler.process | msg=参数有误 | req={}",
                    json);
            return false;
        }
        ResponsePacket<MembersUserVo> userResp = userInfoServiceClient.getMembersUserByUid(userExemptionEvent.getUid());
        if (!userResp.responseSuccess()) {
            logger.error("cmd=MarketUserExemptionEventHandler.process | msg={}",
                    "用户服务熔断");
            return false;
        }
        MembersUserVo user = userResp.getData();
        if (user == null) {
            logger.info("cmd=MarketUserExemptionEventHandler.process | msg=uid有误 | req={}",
                    json);
            return false;
        }
        Integer userId = user.getId();

        //调用配置服务
        List<InvitionShareConfigVo> configList = getConfigs();
        ResponsePacket<InvitionShareConfigVo> config =taskConfigServiceClient
                .findShareTaskConfig(
                        DictionaryCodeEnum.USER_START_UP_REWARD.getCode());
        if (null == config || null == config.getData()) {
            logger.info("cmd=MarketUserExemptionEventHandler.process | msg=没有找到对应配置任务 | req={}", json);
            return false;
        }
        if (STATUS_OFF == config.getData().getStatus()) {//.检查奖励任务是不是已经下线了
            logger.info("cmd=MarketUserExemptionEventHandler.process | msg=任务下线了 | req={}", json);
            return false;
        }
        Integer rewardDays =  config.getData().getRewardDays();
        Integer rewardFreeCount = config.getData().getRewardFreeCount();
        Integer expiryDate = config.getData().getExpiryDate();

        //校验能否获取免费次数
        if (validate(json, userId, rewardDays)) return false;

        return setExemptionRecord(userExemptionEvent, userId, rewardFreeCount, expiryDate);
        //return setExemptionRecord(userExemptionEvent, userId, 3, expiryDate);

    }

    private boolean validate(String json, Integer userId, Integer rewardDays) {
        MarketUserExemptionRecord record = new MarketUserExemptionRecord();
        record.setUserId(userId);
        List<MarketUserExemptionRecord> userExemptionRecordsList = exemptionRecordRepository.select(record);
        Integer obtainDay = userExemptionRecordsList.size();
        //获得的奖励超过上限
        if(obtainDay>=rewardDays) {
            logger.info("cmd=MarketUserExemptionEventHandler.process | msg=获得的奖励超过上限 | req={}", json);
            return true;
        }
        //判断当天是否已获得奖励
        Date date = new Date();
        for(MarketUserExemptionRecord vo : userExemptionRecordsList){
           if(sameDate(date, vo.getCreateTime())) {
               logger.info("cmd=MarketUserExemptionEventHandler.process | msg=当天已获得奖励 | req={}", json);
               return true;
           }
        }
        return false;
    }

    /**
     * 调用免单接口，插入获取奖励记录
     *
     * @param userExemptionEvent
     * @param userId
     * @param rewardFreeCount
     * @param expiryDate
     * @return
     */
    private boolean setExemptionRecord(UserExemptionEvent userExemptionEvent, Integer userId,
                                            Integer rewardFreeCount, Integer expiryDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, expiryDate);
        Date invalidTime = c.getTime();
        //调用免单接口
        if(couponsService.addUserFreeCoupons(userExemptionEvent.getUid(), rewardFreeCount, invalidTime, 1, 1)){
            //插入获得奖励记录
            MarketUserExemptionRecord exemptionRecord = new MarketUserExemptionRecord();
            exemptionRecord.setUserId(userId);
            exemptionRecord.setCreateTime(new Date());
            exemptionRecord.setUpdateTime(new Date());
            exemptionRecordRepository.insertSelective(exemptionRecord);
            return true;
        }

        return false;
    }

    private List<InvitionShareConfigVo> getConfigs() {
        ResponsePacket<List<InvitionShareConfigVo>> resp = taskConfigServiceClient
                .findAllShareTaskConfig();
        if (resp == null || resp.getData() == null) {
            logger.error("cmd=MarketUserExemptionEventHandler.getConfigs | msg=获取新版本免单任务配置返回空 | resp={}",
                    JacksonUtils.toJson(resp));
            return null;
        }
        return resp.getData();
    }

    private InvitionShareConfigVo getTypeConfig(List<InvitionShareConfigVo> list, String code) {
        if (list == null) {
            return null;
        }
        InvitionShareConfigVo config = null;
        for (InvitionShareConfigVo vo : list) {
            if (code.equals(vo.getCode())) {
                config = vo;
                break;
            }
        }
        return config;
    }

    //时间比较
    public static boolean sameDate(Date d1, Date d2){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(d1).equals(fmt.format(d2));
    }
}
