package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.FeedbackListParams;
import com.kaihei.esportingplus.api.params.FeedbackSubmitParams;
import com.kaihei.esportingplus.api.vo.PageInfoVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 反馈与建议服务
 *
 * @author yangshidong
 * @date 2018/12/3
 */
@FeignClient(name = "esportingplus-customer-center-service", path = "/feedback", fallbackFactory = FeedbackServiceClientFallbackFactory.class)
public interface FeedbackServiceClient {
    /**
     * 提交反馈
     *
     * @param feedbackSubmitParams
     * @return
     */
    @PostMapping(value = "/submit")
    ResponsePacket submitFeedback(@RequestBody FeedbackSubmitParams feedbackSubmitParams);

    /**
     * 获取反馈列表
     *
     * @param feedbackListParams
     * @return FeedbackVoList
     */
    @PostMapping(value = "/list")
    ResponsePacket<PageInfoVo> queryFeedbackList(@RequestBody FeedbackListParams feedbackListParams);
}
