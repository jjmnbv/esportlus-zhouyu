package com.kaihei.esportingplus.marketing.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.marketing.domian.entity.MarketUserInvitStatistics;

public interface MarketUserInvitStatisticsRepository extends CommonRepository<MarketUserInvitStatistics> {

    /**
     * 增加相应字段相应值，如model里数据为，invitAmount=2, awardAmount=3,则数据记录在对应字段上增加2和3
     * @param record
     * @return
     */
    int incrInvitCountOrAwardCount(MarketUserInvitStatistics record);
}