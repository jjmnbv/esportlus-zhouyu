package com.kaihei.esportingplus.core.message;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.core.api.enums.FollowActionEnum;
import com.kaihei.esportingplus.core.api.enums.MessageTypeEnum;
import com.kaihei.esportingplus.core.api.enums.PlatformEnum;
import com.kaihei.esportingplus.core.api.enums.PushFormEnum;
import com.kaihei.esportingplus.core.api.params.MessageCustomParam;
import com.kaihei.esportingplus.core.api.params.PushMessageParam;
import com.kaihei.esportingplus.core.api.params.PushParam;
import com.kaihei.esportingplus.core.data.dto.PushMessageDto;
import com.kaihei.esportingplus.core.data.repository.UserTagInfoRepository;
import com.kaihei.esportingplus.core.domain.entity.UserTagInfo;
import com.kaihei.esportingplus.core.domain.service.MessageSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: esportingplus
 * @description: 融云推送消息转换器
 * @author: xusisi
 * @create: 2018-12-01 18:43
 **/
public class RonYunPushMessagePublish implements PushMessagePublish {
    private static Logger logger = LoggerFactory.getLogger(RonYunPushMessagePublish.class);

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private UserTagInfoRepository userTagInfoRepository;

    /***
     * 发送站外push
     * @param pushMessageParam
     * @return
     */
    @Override
    public Boolean sendMessage(PushMessageParam pushMessageParam) {
        try {
            logger.debug("message : {} ", JsonsUtils.toJson(pushMessageParam));

            PushParam pushParam = new PushParam();

            String title = pushMessageParam.getTitle();
            String content = pushMessageParam.getContent();
            String imageUri = pushMessageParam.getImageUri();
            List<String> tags = pushMessageParam.getTagNames();

            PushParam.Audience audience = new PushParam.Audience();
            audience.setTag(tags);
            pushParam.setAudience(audience);

            PushParam.Notification notification = new PushParam.Notification();
            notification.setAlert(title);
            List<String> pfList = new ArrayList<>();
            for (Integer pf : pushMessageParam.getPlatforms()) {
                if (PlatformEnum.ANDROID.getCode() == pf) {
                    pfList.add(PlatformEnum.ANDROID.getMsg());
                    PushParam.Android android = new PushParam.Android();
                    android.setAlert(content);
                    notification.setAndroid(android);
                }
                if (PlatformEnum.IOS.getCode() == pf) {
                    pfList.add(PlatformEnum.IOS.getMsg());
                    PushParam.Ios ios = new PushParam.Ios();
                    ios.setAlert(content);
                    ios.setTitle(title);
                    notification.setIos(ios);
                }
            }
            pushParam.setNotification(notification);
            pushParam.setPlatform(pfList);

            PushParam.Message message = new PushParam.Message();
            PushMessageDto dto = new PushMessageDto();
            dto.setId(pushMessageParam.getId());
            /***
             * 发送渠道：1、站内消息，2、站外push
             */
            dto.setPushChannel(2);
            Integer followAction = pushMessageParam.getFollowAction();
            dto.setFollowAction(followAction);
            String url = null;
            if (followAction != FollowActionEnum.APP_INDEX.getCode()) {
                url = pushMessageParam.getUrl();
                dto.setUrl(url);
            }
            Integer pushForm = pushMessageParam.getPushForm();

            if (pushForm == PushFormEnum.RICH_CONTENT_MESSAGE.getCode()) {
                dto.setPushForm(PushFormEnum.RICH_CONTENT_MESSAGE.getType());
                message.setObjectName(PushFormEnum.RICH_CONTENT_MESSAGE.getType());
            } else {
                dto.setPushForm(PushFormEnum.TEXT_MESSAGE.getType());
                message.setObjectName(PushFormEnum.TEXT_MESSAGE.getType());
            }
            dto.setTitle(title);
            dto.setImageUri(imageUri);
            dto.setContent(content);
            message.setContent(FastJsonUtils.toJson(dto));

            String response = messageSendService.push(pushParam);
            if (StringUtils.isNoneEmpty(response)) {
                return true;
            }

        } catch (Exception e) {
            logger.error("RonYunPushMessagePublish Error:{}, send failed", e.getMessage());
        }

        return false;
    }

    /***
     * 发送站内消息
     * @param pushMessageParam
     * @return
     */
    @Override
    public Boolean sendSystemMessage(PushMessageParam pushMessageParam) {
        MessageCustomParam messageCustomParam = new MessageCustomParam();
        String content = pushMessageParam.getContent();
        String title = pushMessageParam.getTitle();
        String imageUri = pushMessageParam.getImageUri();

        Set<String> userIdsSet = new HashSet<>();
        for (String tag : pushMessageParam.getTagNames()) {
            UserTagInfo userTagInfo = userTagInfoRepository.getTagInfoByTagName(tag);
            if (userTagInfo == null || userTagInfo.getUids() == null) {
                continue;
            }
            String uidStr = userTagInfo.getUids();
            List<String> userIds = FastJsonUtils.fromJsonToList(uidStr, String.class);
            userIdsSet.addAll(userIds);
        }
        messageCustomParam.setType(3);
        messageCustomParam.setReciever(new ArrayList(userIdsSet));
        messageCustomParam.setMessageType(MessageTypeEnum.SYSTEM);
        PushMessageDto dto = new PushMessageDto();
        dto.setId(pushMessageParam.getId());
        /***
         * 发送渠道：1、站内消息，2、站外push
         */
        dto.setPushChannel(1);
        dto.setContent(content);
        dto.setTitle(title);
        dto.setImageUri(imageUri);

        Integer followAction = pushMessageParam.getFollowAction();
        dto.setFollowAction(followAction);
        String url = null;
        if (followAction != FollowActionEnum.APP_INDEX.getCode()) {
            url = pushMessageParam.getUrl();
            dto.setUrl(url);
        }
        Integer pushForm = pushMessageParam.getPushForm();

        if (pushForm == PushFormEnum.RICH_CONTENT_MESSAGE.getCode()) {
            dto.setPushForm(PushFormEnum.RICH_CONTENT_MESSAGE.getType());
            messageCustomParam.setCode(PushFormEnum.RICH_CONTENT_MESSAGE.getType());
        } else {
            dto.setPushForm(PushFormEnum.TEXT_MESSAGE.getType());
            messageCustomParam.setCode(PushFormEnum.TEXT_MESSAGE.getType());
        }
        dto.setTitle(title);
        dto.setImageUri(imageUri);
        dto.setContent(content);
        messageCustomParam.setContent(FastJsonUtils.toJson(dto));

        Boolean flag = messageSendService.sendCustom(messageCustomParam);

        return flag;
    }

}
