package com.kaihei.esportingplus.core.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.core.api.params.MessageCustomParam;
import com.kaihei.esportingplus.core.api.params.MessageParam;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.core.api.params.MessageSubType;
import com.kaihei.esportingplus.core.utils.RonYunUtils;
import io.rong.messages.BaseMessage;
import io.rong.models.message.GroupMessage;
import io.rong.models.message.PrivateMessage;
import io.rong.models.message.SystemMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author liuyang
 * @Description 融云消息转换器
 * @Date 2018/11/2 11:14
 **/
public interface RonyunMessageConverter {

    Logger LOGGER = LoggerFactory.getLogger(RonyunMessageConverter.class);

    static String getContent(SourceMessage message) {
        String content = null;
        MessageParam param = message.getParam();
        //消息类型整合
        MessageSubType resultMesType = new MessageSubType();
        resultMesType.setName(param.getMessageType().name().toLowerCase());
        MessageSubType msgSubType = param.getMsgSubType();
        if (msgSubType != null) {
            resultMesType.setSubType(msgSubType);
        }

        if (param instanceof MessageSendParam) {
            MessageSendParam messageSendParam = (MessageSendParam) param;
            HashMap templateMap = JacksonUtils.toBean(message.getTemplate().getTemplate(), HashMap.class);
            if (messageSendParam.getData().containsKey("extra")){
                try {
                    HashMap extra = JacksonUtils.toBean(messageSendParam.getData().get("extra").toString(), HashMap.class);
                    extra.put("msgType", resultMesType);
                } catch (Exception e) {
                    LOGGER.error("cmd=RonyunMessageConverter.convert | msg = extra 字段不是json格式");
                    throw e;
                }
            }else if (messageSendParam.getData().containsKey("data")){
                try {
                    HashMap data = JacksonUtils.toBean(messageSendParam.getData().get("data").toString(), HashMap.class);
                    data.put("msgType", resultMesType);
                } catch (Exception e) {
                    LOGGER.error("cmd=RonyunMessageConverter.convert | msg = data 字段不是json格式");
                    throw e;
                }
            }else{
                Map<String, Object> messageTypeMap = new HashMap<>();
                messageTypeMap.put("msgType", resultMesType);
                templateMap.put("extra", messageTypeMap);
            }

            content = JacksonUtils.toJson(templateMap);
        } else if (param instanceof MessageCustomParam) {
            content = ((MessageCustomParam) param).getContent();
        }

        return content;
    }

    static String getObjectName(SourceMessage message) {
        MessageParam param = message.getParam();
        if (param instanceof MessageSendParam) {
            return message.getTemplate().getMsgType();
        } else if (param instanceof MessageCustomParam) {
            return ((MessageCustomParam) param).getCode();
        }

        return null;
    }

    Converter<SourceMessage, Message> privateConverter = message -> {
        MessageParam param = message.getParam();
        final String msgContent = getContent(message);
        String from = RonYunUtils.encodeFrom(param.getSender());
        List<String> to = RonYunUtils.encodeIMUser(param.getReciever());
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSenderId(from)
                .setObjectName(getObjectName(message))
                .setContent(new BaseMessage() {
                    @Override
                    public String getType() {
                        return getObjectName(message);
                    }

                    @Override
                    public String toString() {
                        return msgContent;
                    }
                })
                .setPushContent(param.getPushContent())
                .setPushData(param.getPushData())
                .setVerifyBlacklist(1);
        return (ObjectMessage<PrivateMessage>) () -> privateMessage;
    };

    Converter<SourceMessage, ObjectMessage<SystemMessage>> systemConverter = message -> {
        MessageParam param = message.getParam();
        final String msgContent = getContent(message);
        SystemMessage systemMessage = new SystemMessage()
                .setObjectName(getObjectName(message))
                .setPushContent(param.getPushContent())
                .setPushData(param.getPushData())
                .setIsPersisted(1)
                .setIsCounted(0)
                .setContent(new BaseMessage() {
                    @Override
                    public String getType() {
                        return getObjectName(message);
                    }

                    @Override
                    public String toString() {
                        return msgContent;
                    }
                });

        return () -> systemMessage;
    };

    Converter<SourceMessage, ObjectMessage<GroupMessage>> groupConverter = message -> {
        MessageParam param = message.getParam();
        final String msgContent = getContent(message);
        GroupMessage groupMessage = new GroupMessage()
                .setObjectName(getObjectName(message))
                .setPushContent(param.getPushContent())
                .setPushData(param.getPushData())
                .setIsPersisted(1)
                .setIsCounted(0)
                .setIsIncludeSender(0)
                .setContent(new BaseMessage() {
                    @Override
                    public String getType() {
                        return getObjectName(message);
                    }

                    @Override
                    public String toString() {
                        return msgContent;
                    }
                });

        return () -> groupMessage;
    };

}
