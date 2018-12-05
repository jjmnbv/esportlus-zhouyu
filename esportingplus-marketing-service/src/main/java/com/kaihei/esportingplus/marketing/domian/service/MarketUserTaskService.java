package com.kaihei.esportingplus.marketing.domian.service;

import com.kaihei.esportingplus.marketing.domian.entity.MarketUserInvitStatistics;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserInvitingRelation;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserTaskAccumualte;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserTaskAccumualteHistory;
import java.util.Date;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-12 14:38
 * @Description:
 */
public interface MarketUserTaskService {

    boolean updateAccumulate(MarketUserTaskAccumualte accumualte);

    MarketUserInvitingRelation findRelationByUserId(Integer userId);

    MarketUserTaskAccumualte getOrCreateAccumulate(Integer userId, Integer type, Date onlinTime);

    MarketUserTaskAccumualte getAccumulate(Integer userId, Integer type);

    MarketUserTaskAccumualte createAccumulate(MarketUserTaskAccumualte accumualte);

    MarketUserTaskAccumualteHistory createAccumulateHistroyByAcc(MarketUserTaskAccumualte acc);

    MarketUserInvitStatistics getOrCreateInvitStatistics(Integer userId);

    MarketUserInvitStatistics getInvitStatistics(Integer userId);

    boolean incrStatistics(Integer userId, Long invitCount, Long awardCount);
}
