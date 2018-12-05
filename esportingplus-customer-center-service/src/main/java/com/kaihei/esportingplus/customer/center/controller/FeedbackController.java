package com.kaihei.esportingplus.customer.center.controller;

import com.kaihei.esportingplus.api.feign.FeedbackServiceClient;
import com.kaihei.esportingplus.api.params.FeedbackListParams;
import com.kaihei.esportingplus.api.params.FeedbackSubmitParams;
import com.kaihei.esportingplus.api.vo.PageInfoVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.customer.center.domain.service.FeedBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 反馈与建议controller
 *
 * @author yangshidong
 * @date 2018/12/3
 */
@RestController
@RequestMapping("/feedback")
@Api(tags = {"反馈与建议接口"})
public class FeedbackController implements FeedbackServiceClient {
    @Autowired
    private FeedBackService feedBackService;

    @ApiOperation(value = "提交反馈与建议")
    @Override
    public ResponsePacket submitFeedback(@RequestBody FeedbackSubmitParams feedbackSubmitParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, feedbackSubmitParams);
        feedBackService.submitFeedback(feedbackSubmitParams);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "查询反馈与建议列表")
    @Override
    public ResponsePacket<PageInfoVo> queryFeedbackList(@RequestBody FeedbackListParams feedbackListParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, feedbackListParams);
        return ResponsePacket.onSuccess(feedBackService.queryFeedbackList(feedbackListParams));
    }
}
