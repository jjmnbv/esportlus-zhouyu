package com.kaihei.esportingplus.core.message;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.core.config.CoreContext;
import com.kaihei.esportingplus.core.constant.RongYunConstants;
import io.rong.RongCloud;
import io.rong.models.message.SystemMessage;
import io.rong.models.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.concurrent.TimeUnit;

/**
 * @Author liuyang
 * @Description 发送系统消息
 * @Date 2018/10/29 16:24
 **/
public class MessageSystemPublish extends AbstractMessagePublish {

    private RongCloud rongCloud = CoreContext.context().getBean(RongCloud.class);

    Converter<SourceMessage, ObjectMessage<SystemMessage>> converter = RonyunMessageConverter.systemConverter;

    @Override
    public boolean send(String from, Receiver to, Object message) {
        try {
            logger.debug("cmd=MessageSystemPublish.send| message="+ JsonsUtils.toJson(message));
            SystemMessage systemMessage = (SystemMessage) converter.convert((SourceMessage) message).getObject();
            UserReceiver ur = (UserReceiver) to;
            systemMessage.setSenderId(from);
            String[] targetIds = ur.getUsers().toArray(new String[ur.getUsers().size()]);
            String[][] split = split(targetIds, RongYunConstants.Limit.MAX_USERS_PER_SYSTEM);
            for (int i = 0; i < split.length; i++){
                systemMessage.setTargetId(split[i]);
                logger.debug("cmd=MessageSystemPublish.send| systemMessage=" + JacksonUtils.toJson(systemMessage));
                ResponseResult result = rongCloud.message.system.send(systemMessage);
                logger.debug("cmd=MessageSystemPublish.send| rongyunResult| result="+ JsonsUtils.toJson(result));

                //判断是否超出频率
                if (result.getCode().intValue() == RongYunConstants.Result.OVERRATELIMIT){
                    //超出频率任务延时发送
                    addTask(timeout -> {
                        //clone msg  防止targetid 出错
                        SystemMessage taskMessage = new SystemMessage();
                        BeanUtils.copyProperties(systemMessage, taskMessage);
                        try {
                            ResponseResult taskResult = rongCloud.message.system.send(taskMessage);
                        } catch (Exception e) {
                            logger.error("MessageSystemPublish task Error:{}, send failed", e.getMessage());
                        }
                    }, 1, TimeUnit.SECONDS);

                    continue;
                }

                if (result.getCode().intValue() != RongYunConstants.Result.SUCCESSCODE){
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("MessageSystemPublish Error:{}, send failed", e.getMessage());
        }

        return false;
    }
}
