package com.kaihei.esportingplus.customer.center.domain.service;

import com.kaihei.esportingplus.api.params.FeedbackSubmitParams;
import com.kaihei.esportingplus.api.params.FeedbackListParams;
import com.kaihei.esportingplus.api.vo.PageInfoVo;

/**
 * 反馈与建议服务类
 *
 * @author yangshidong
 * @date 2018/12/3
 */
public interface FeedBackService {
    /**
     * 反馈与建议提交
     *
     * @param feedbackSubmitParams
     */
    void submitFeedback(FeedbackSubmitParams feedbackSubmitParams);

    /**
     * 查询反馈与建议列表
     *
     * @param feedbackListParams
     * @return {@link PageInfoVo}
     */
    PageInfoVo queryFeedbackList(FeedbackListParams feedbackListParams);
}
