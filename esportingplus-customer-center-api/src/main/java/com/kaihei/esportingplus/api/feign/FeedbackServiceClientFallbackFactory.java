package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.FeedbackListParams;
import com.kaihei.esportingplus.api.params.FeedbackSubmitParams;
import com.kaihei.esportingplus.api.vo.PageInfoVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;

/**
 * 投诉与反馈建议异常处理
 *
 * @author yangshidong
 * @date 2018/12/3
 */
public class FeedbackServiceClientFallbackFactory implements FallbackFactory<FeedbackServiceClient> {

    @Override
    public FeedbackServiceClient create(Throwable throwable) {
        return new FeedbackServiceClient() {

            @Override
            public ResponsePacket submitFeedback(FeedbackSubmitParams feedbackSubmitParams) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<PageInfoVo> queryFeedbackList(FeedbackListParams feedbackListParams) {
                return ResponsePacket.onHystrix(throwable);
            }
        };
    }
}
