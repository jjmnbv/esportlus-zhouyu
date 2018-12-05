package com.kaihei.esportingplus.customer.center.data.repository;

import com.kaihei.esportingplus.api.params.EvaluateQueryParam;
import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.customer.center.domain.entity.CustomerOrderEvaluate;

import java.util.List;

/**
 * @author yangshidong
 * @date 2018/11/17
 */
public interface CustomerOrderEvaluateRepository extends CommonRepository<CustomerOrderEvaluate> {

    /**
     * 多纬度查询评价信息
     *
     * @param evaluateQueryParam 查询参数对象
     * @return
     */
    List<CustomerOrderEvaluate> selectEvaluateList(EvaluateQueryParam evaluateQueryParam);
}
