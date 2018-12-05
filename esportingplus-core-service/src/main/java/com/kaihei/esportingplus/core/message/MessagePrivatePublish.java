package com.kaihei.esportingplus.core.message;

import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.core.config.CoreContext;
import com.kaihei.esportingplus.core.constant.RongYunConstants;
import io.rong.RongCloud;
import io.rong.models.message.PrivateMessage;
import io.rong.models.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.concurrent.TimeUnit;

/**
 * @Author liuyang
 * @Description 发送单聊消息
 * @Date 2018/10/29 15:13
 **/
public class MessagePrivatePublish extends AbstractMessagePublish {

    private RongCloud rongCloud = CoreContext.context().getBean(RongCloud.class);

    private Converter<SourceMessage, Message> converter = RonyunMessageConverter.privateConverter;

    @Override
    public boolean send(String from, Receiver to, Object message) {
        logger.debug("cmd=MessagePrivatePublish.send| before converter message=" + JacksonUtils.toJson(message));
        Message convertMsg = converter.convert((SourceMessage) message);
        PrivateMessage msg = (PrivateMessage) ((ObjectMessage) convertMsg).getObject();
        UserReceiver ur = (UserReceiver) to;
        msg.setTargetId(ur.getUsers().toArray(new String[ur.getUsers().size()]));
        msg.setSenderId(from);
        return send(msg);
    }

    protected boolean send(PrivateMessage msg) {
        try {
            logger.debug("cmd=MessagePrivatePublish.send| PrivateMessage=" + JacksonUtils.toJson(msg));

            //融云每次接收消息的用户上限为 1000 人
            String[] targetIds = msg.getTargetId();
            String[][] split = split(targetIds, RongYunConstants.Limit.MAX_USERS_PER_PRIVATE);
            for (int i = 0; i < split.length; i++) {
                msg.setTargetId(split[i]);
                ResponseResult result = rongCloud.message.msgPrivate.send(msg);
                logger.debug("cmd=MessagePrivatePublish.send| rongyunResult| result=" + JacksonUtils.toJson(result));
                //判断是否超出频率
                if (result.getCode().intValue() == RongYunConstants.Result.OVERRATELIMIT) {
                    //超出频率任务延时发送
                    addTask(timeout -> {
                        //clone msg  防止targetid 出错
                        PrivateMessage taskMessage = new PrivateMessage();
                        BeanUtils.copyProperties(msg, taskMessage);
                        try {
                            ResponseResult taskResult = rongCloud.message.msgPrivate.send(taskMessage);
                        } catch (Exception e) {
                            logger.error("MessagePrivatePublish task Error:{}, send failed", e.getMessage());
                        }
                    }, 60, TimeUnit.SECONDS);

                    continue;
                }

                if (result.getCode().intValue() != RongYunConstants.Result.SUCCESSCODE) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("MessagePrivatePublish Error:{}, send failed", e.getMessage());
        }

        return false;
    }
}
