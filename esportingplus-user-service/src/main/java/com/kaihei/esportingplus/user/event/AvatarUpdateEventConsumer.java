package com.kaihei.esportingplus.user.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.HttpUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.user.config.UserProperties;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Component
public class AvatarUpdateEventConsumer extends EventConsumer {
    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;
    @Autowired
    private UserProperties userProperties;
    private static final Logger LOGGER = LoggerFactory.getLogger(AvatarUpdateEventConsumer.class);
    @Subscribe
    @AllowConcurrentEvents
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(AvatarUpdateEvent event){
        HttpEntity httpEntity = HttpUtils.buildParam(event);
        LOGGER.info(">>send user avatar update request ,uid:{},update avatar:{}",event.getUid(),event.getAvatar());
        String result = restTemplateExtrnal.postForObject(
                userProperties.getUserInfoDomain() + userProperties.getUpdateAvatarUrl(),
                httpEntity, String.class);
        ResponsePacket responsePacket = JacksonUtils.toBean(result, ResponsePacket.class);
        if(!responsePacket.responseSuccess()){
            LOGGER.error(">> user avatar update failed,uid:{},update avatar:{}",event.getUid(),event.getAvatar());
        }
        ValidateAssert
                .isTrue(responsePacket.responseSuccess(), BizExceptionEnum.USER_UPDATE_AVATAR_FAIL);
    }
}
