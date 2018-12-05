package com.kaihei.esportingplus.core.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.core.domain.entity.WxTemplate;

import java.util.Collection;
import java.util.List;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/24 16:55
 **/
public interface WxTemplateRepository extends CommonRepository<WxTemplate> {

    WxTemplate selectByUnionId(String unionId);

    int deleteByIdBatch(List<Integer> ids);

    List<WxTemplate> selectByUnioinIds(Collection<String> unionIds);
}
