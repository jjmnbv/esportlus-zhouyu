package com.kaihei.esportingplus.core.domain.service.impl;

import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.core.api.params.MessageCustomParam;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.core.api.params.PushParam;
import com.kaihei.esportingplus.core.config.MessageConfig;
import com.kaihei.esportingplus.core.data.repository.MessageTemplateRepository;
import com.kaihei.esportingplus.core.domain.entity.MessageTemplate;
import com.kaihei.esportingplus.core.domain.service.MessageSendService;
import com.kaihei.esportingplus.core.message.MessagePublishFactory;
import com.kaihei.esportingplus.core.message.Receiver;
import com.kaihei.esportingplus.core.message.ReceiverBuilder;
import com.kaihei.esportingplus.core.message.SourceMessage;
import com.kaihei.esportingplus.core.utils.RonYunUtils;
import io.rong.RongCloud;
import io.rong.methods.conversation.Conversation;
import io.rong.models.conversation.ConversationModel;
import io.rong.models.response.ResponseResult;
import io.rong.util.CodeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

import static com.kaihei.esportingplus.core.constant.MessageConstants.Constants.IM_USER_ADMIN;

/**
 * @Author liuyang
 * @Description 发送消息
 * @Date 2018/10/29 14:17
 **/
@Service
public class MessageSendServiceImpl implements MessageSendService {

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private MessageConfig messageConfig;

    @Override
    public boolean send(MessageSendParam param) {
        //獲取模板
        MessageTemplate template = messageTemplateRepository.selectByTemplateId(param.getTemplateId());
        HashMap templateMap = JacksonUtils.toBean(template.getTemplate(), HashMap.class);
        templateMap.putAll(param.getData());
        Receiver receiver = null;
        switch (template.getMsgClass()) {
            case 1:
                receiver = ReceiverBuilder.multiUser(param.getReciever());
                break;
            case 2:
                if (CollectionUtils.isEmpty(param.getToUsers())) {
                    receiver = ReceiverBuilder.group(param.getReciever());
                } else {
                    receiver = ReceiverBuilder.groupDirect(param.getReciever(), param.getToUsers());
                }
                break;
            case 3:
                receiver = ReceiverBuilder.multiUser(param.getReciever());
                break;
        }

        SourceMessage sm = new SourceMessage(param, template);
        MessagePublishFactory factory = new MessagePublishFactory();
        return factory.createMessagePublish(template.getMsgClass()).send(param.getSender(), receiver, sm);
    }

    @Override
    public boolean sendCustom(MessageCustomParam param) {
        Receiver receiver = null;
        switch (param.getType()) {
            case 1:
                receiver = ReceiverBuilder.multiUser(param.getReciever());
                break;
            case 2:
                if (CollectionUtils.isEmpty(param.getToUsers())) {
                    receiver = ReceiverBuilder.group(param.getReciever());
                } else {
                    receiver = ReceiverBuilder.groupDirect(param.getReciever(), param.getToUsers());
                }
                break;
            case 3:
                receiver = ReceiverBuilder.multiUser(param.getReciever());
                break;
        }

        SourceMessage sm = new SourceMessage();
        sm.setParam(param);
        MessagePublishFactory factory = new MessagePublishFactory();
        return factory.createMessagePublish(param.getType()).send(param.getSender(), receiver, sm);
    }

    @Override
    public String push(PushParam param) {
        List<String> userid = param.getAudience().getUserid();
        List<String> inWhiteList = RonYunUtils.inWhiteList(userid);
        if (!inWhiteList.isEmpty()) {
            List<String> imUser = RonYunUtils.encodeIMUser(userid);
            param.getAudience().setUserid(imUser);
        } else {
            return null;
        }

        if (StringUtils.isEmpty(param.getFromuserid())) {
            param.setFromuserid(IM_USER_ADMIN);
        }

        HttpHeaders header = RonYunUtils.getHeader();
        RestTemplate template = new RestTemplate();

        String data = JacksonUtils.toJson(param);
        HttpEntity entity = new HttpEntity(data, header);
        String url = messageConfig.getRonyun().getUrl() + "/push.json";
        ResponseEntity<HashMap> response = template.postForEntity(url, entity, HashMap.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().get("id").toString();
        }

        return null;
    }

}
