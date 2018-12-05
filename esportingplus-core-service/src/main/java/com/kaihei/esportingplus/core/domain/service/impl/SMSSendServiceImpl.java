package com.kaihei.esportingplus.core.domain.service.impl;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.core.api.enums.SmsAuthenticationTypeEnum;
import com.kaihei.esportingplus.core.api.params.SmsCredentialParam;
import com.kaihei.esportingplus.core.config.SmsProperties;
import com.kaihei.esportingplus.core.data.dto.ChuanglanSmsSendDto;
import com.kaihei.esportingplus.core.data.dto.SmsSendDto;
import com.kaihei.esportingplus.core.data.manager.VerificationCodeCacheManager;
import com.kaihei.esportingplus.core.data.repository.SMSTemplateRepository;
import com.kaihei.esportingplus.core.domain.entity.SmsTemplate;
import com.kaihei.esportingplus.core.domain.service.SMSSendService;
import com.kaihei.esportingplus.core.utils.TemplateFormat;
import com.kaihei.esportingplus.core.utils.VerificationCodeGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @Author liuyang
 * @Description 创蓝发送短信
 * @Date 2018/10/24 16:44
 **/
@Service
public class SMSSendServiceImpl implements SMSSendService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SMSTemplateRepository smsTemplateRepository;

    @Autowired
    private VerificationCodeCacheManager verificationCodeCacheManager;

    @Autowired
    private SmsProperties smsProperties;

    private static int VERIFICATIONCODE_SIZE = 6;

    private enum SmsChannel {
        NONE(0, "无"),
        NOTIFICATION(1, "通知类"),
        PROMOTION(2, "推广类");
        int key;
        String desc;

        SmsChannel(int key, String value) {
            this.key = key;
            this.desc = value;
        }

        public static SmsChannel convert(Integer key) {
            for (SmsChannel channel : SmsChannel.values()) {
                if (channel.key == key.intValue())
                    return channel;
            }

            throw new IllegalArgumentException("SmsChannel.convert 参数错误");
        }
    }

    @Override
    public SmsTemplate getSmsTemplateByTemplateId(int templateId) {
        List<SmsTemplate> smsTemplates = smsTemplateRepository.selectByTemplateId(templateId);
        if (CollectionUtils.isEmpty(smsTemplates)) {
            return null;
        }

        return smsTemplates.get(0);
    }

    @Override
    public boolean send(SmsSendDto param) {
        SmsTemplate smsTemplate = this.getSmsTemplateByTemplateId(param.getTemplateId());
        if (Objects.isNull(smsTemplate)) {
            logger.error("cmd=SMSSendService.send | msg=没有对应模板");
            return false;
        }

        //如果是发验证码，生成验证码， 保存到redis
        if (param.getTemplateId() == 1) {
            String code = VerificationCodeGenerator.RANDOM_NUM.generate(VERIFICATIONCODE_SIZE);
            logger.debug("cmd=SMSSendService.send | msg= {}生成的验证码为 {}", param.getPhone(), code);
            verificationCodeCacheManager.setPhoneCode(createKey(param.getType(), param.getPhone()), code);
            param.setData(code);
        }

        Integer channel = smsTemplate.getChannel();
        SmsProperties.SMSInfo smsInfo = getSmsInfo(channel);
        String template = smsTemplate.getTemplate();
        String text = smsProperties.getPrefix() + TemplateFormat.Type.SMSTEMPLATE.getFormat().format(template, param.getData());
        ChuanglanSmsSendDto sendParam = new ChuanglanSmsSendDto(smsInfo.getAccount(), smsInfo.getPassword(), text, param.getPhone());
        smsSend(smsInfo, sendParam);
        return true;
    }

    @Override
    public boolean credential(SmsCredentialParam param) {
        String key = createKey(param.getType(), param.getPhone());
        String codeByPhone = verificationCodeCacheManager.getCodeByPhone(key);
        return param.getCode().equals(codeByPhone);
    }

    private String createKey(SmsAuthenticationTypeEnum type, String phone){
        if (Objects.isNull(type)){
            type = SmsAuthenticationTypeEnum.DEFAULT;
        }

        return type.name().toLowerCase() + ":" + phone;
    }

    /**
     * 发送短信到创蓝
     *
     * @param smsInfo
     * @param smsSendParam
     * @return
     */
    private String smsSend(SmsProperties.SMSInfo smsInfo, ChuanglanSmsSendDto smsSendParam) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        String requestJson = JsonsUtils.toJson(smsSendParam);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        return new RestTemplate().postForObject(smsInfo.getSend_url(), entity, String.class);
    }

    private SmsProperties.SMSInfo getSmsInfo(Integer channel) {
        SmsProperties.SMSInfo smsInfo = null;
        switch (SmsChannel.convert(channel)) {
            case NONE:
            case NOTIFICATION:
                smsInfo = smsProperties.getNotification();
                break;
            case PROMOTION:
                smsInfo = smsProperties.getPromotion();
                break;
        }

        return smsInfo;
    }
}
