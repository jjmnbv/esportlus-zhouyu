package com.kaihei.esportingplus.core.domain.service;

import com.kaihei.esportingplus.core.api.params.SmsCredentialParam;
import com.kaihei.esportingplus.core.api.params.SmsSendParam;
import com.kaihei.esportingplus.core.data.dto.SmsSendDto;
import com.kaihei.esportingplus.core.domain.entity.SmsTemplate;

import java.util.List;

/**
 * @Author liuyang
 * @Description
 * @Date 2018/10/24 16:15
 **/
public interface SMSSendService {

    SmsTemplate getSmsTemplateByTemplateId(int templateId);

    boolean send(SmsSendDto param);

    boolean credential(SmsCredentialParam param);
}
