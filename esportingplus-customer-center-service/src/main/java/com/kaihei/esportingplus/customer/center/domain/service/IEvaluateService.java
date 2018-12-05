package com.kaihei.esportingplus.customer.center.domain.service;

import com.kaihei.esportingplus.api.params.EvaluateCreateParam;
import com.kaihei.esportingplus.api.params.EvaluateQueryParam;
import com.kaihei.esportingplus.api.vo.EvaluateListVo;

/**
 * 评价服务接口类
 *
 * @author yangshidong
 * @date 2018/11/15
 * */
public interface IEvaluateService {
    /**
     * 提交评价信息
     * @param evaluateCreateParam
     * @return
     * */
    void submitEvaluate(EvaluateCreateParam evaluateCreateParam);

    /**
     * 查询评价列表
     * @param evaluateQueryParam
     * @return EvaluateList
     * */
    EvaluateListVo getEvaluateList(EvaluateQueryParam evaluateQueryParam);
}
