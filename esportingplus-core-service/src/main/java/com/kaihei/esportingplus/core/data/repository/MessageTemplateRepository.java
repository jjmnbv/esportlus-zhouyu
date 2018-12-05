package com.kaihei.esportingplus.core.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.core.domain.entity.MessageTemplate;

public interface MessageTemplateRepository extends CommonRepository<MessageTemplate> {

    MessageTemplate selectByTemplateId(int templateId);
}
