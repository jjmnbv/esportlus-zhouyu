package com.kaihei.esportingplus.customer.center.data.repository;

import com.kaihei.esportingplus.api.vo.FeedbackVo;
import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.customer.center.domain.entity.FeedbackLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FeedbackLogRepository extends CommonRepository<FeedbackLog> {
    /**
     * 查询指定时间段内的投诉与反馈建议
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param handleStatus 0:未处理 1:已处理 2:全部
     * @return {@link FeedbackVo}
     */
    List<FeedbackVo> selectFeedBackVoList(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("handleStatus") short handleStatus);
}