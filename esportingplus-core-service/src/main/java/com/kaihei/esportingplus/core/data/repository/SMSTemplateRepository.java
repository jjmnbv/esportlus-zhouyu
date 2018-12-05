package com.kaihei.esportingplus.core.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.core.domain.entity.SmsTemplate;

import java.util.List;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/24 16:55
 **/
public interface SMSTemplateRepository extends CommonRepository<SmsTemplate> {

    List<SmsTemplate> selectByTemplateId(int templateId);
}
