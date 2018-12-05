package com.kaihei.esportingplus.customer.center.controller;

import com.kaihei.esportingplus.api.feign.EvaluateServiceClient;
import com.kaihei.esportingplus.api.params.EvaluateCreateParam;
import com.kaihei.esportingplus.api.params.EvaluateQueryParam;
import com.kaihei.esportingplus.api.vo.EvaluateListVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.customer.center.domain.service.IEvaluateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评价服务接口
 *
 * @author yangshidong
 * @date 2018/11/16
 */
@RestController
@RequestMapping("/evaluate")
@Api(tags = {"评价服务接口"})
public class EvaluateController implements EvaluateServiceClient {
    @Autowired
    private IEvaluateService evaluateService;

    @PostMapping("/submit")
    @ApiOperation(value = "提交评价")
    @Override
    public ResponsePacket submitEvaluate(@RequestBody EvaluateCreateParam evaluateCreateParam) {
        evaluateService.submitEvaluate(evaluateCreateParam);
        return ResponsePacket.onSuccess();
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询评价列表")
    @Override
    public ResponsePacket<EvaluateListVo> queryEvaluateList(@RequestBody EvaluateQueryParam evaluateQueryParam) {
        EvaluateListVo evaluateListVo = evaluateService.getEvaluateList(evaluateQueryParam);
        return ResponsePacket.onSuccess(evaluateListVo);
    }
}
