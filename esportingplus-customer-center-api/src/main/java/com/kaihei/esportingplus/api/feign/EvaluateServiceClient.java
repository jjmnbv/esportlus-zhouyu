package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.EvaluateCreateParam;
import com.kaihei.esportingplus.api.params.EvaluateQueryParam;
import com.kaihei.esportingplus.api.vo.EvaluateListVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 基于feign实现远程评价接口调用
 * 1.提交评论
 * 2.获取评论信息列表
 * fallbackFactory指定断路器实现类
 *
 * @author yangshidong
 * @date 2018/11/15
 */
@FeignClient(name = "esportingplus-customer-center-service", path = "/evaluate", fallbackFactory = EvaluateServiceClientFallbackFactory.class)
public interface EvaluateServiceClient {
    /**
     * 提交评论
     *
     * @param evaluateCreateParam
     * @return
     */
    @PostMapping(value = "/submit")
    ResponsePacket submitEvaluate(@RequestBody EvaluateCreateParam evaluateCreateParam);

    /**
     * 获取评论列表
     *
     * @param evaluateQueryParam
     * @return evaluateListVo
     */
    @PostMapping(value = "/list")
    ResponsePacket<EvaluateListVo> queryEvaluateList(@RequestBody EvaluateQueryParam evaluateQueryParam);
}
