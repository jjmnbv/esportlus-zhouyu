package com.kaihei.esportingplus.core.message;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.core.config.CoreContext;
import com.kaihei.esportingplus.core.config.MessageConfig;
import com.kaihei.esportingplus.core.constant.RongYunConstants;
import io.rong.RongCloud;
import io.rong.models.message.GroupMessage;
import io.rong.models.response.ResponseResult;
import io.rong.util.CommonUtil;
import io.rong.util.GsonUtil;
import io.rong.util.HttpUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author liuyang
 * @Description 群组消息
 * @Date 2018/10/29 16:24
 **/
public class MessageGroupPublish extends AbstractMessagePublish {

    private RongCloud rongCloud = CoreContext.context().getBean(RongCloud.class);

    MessageConfig config = CoreContext.context().getBean(MessageConfig.class);

    private Converter<SourceMessage, ObjectMessage<GroupMessage>> converter = RonyunMessageConverter.groupConverter;

    @Override
    public boolean send(String from, Receiver to, Object message) {
        logger.debug("cmd=MessageGroupPublish.send| message=" + JacksonUtils.toJson(message));
        GroupMessage groupMessage = (GroupMessage) converter.convert((SourceMessage) message).getObject();
        groupMessage.setSenderId(from);
        if (to instanceof GroupDirectReceiver) {
            GroupDirectReceiver gr = (GroupDirectReceiver) to;
            groupMessage.setTargetId(gr.getGroups().toArray(new String[gr.getGroups().size()]));
            logger.debug("cmd=MessageGroupPublish.send| groupMessage=" + JacksonUtils.toJson(groupMessage));
            return sendGroupDirectional(gr.toUsers(), groupMessage);

        } else {
            GroupReceiver gr = (GroupReceiver) to;
            groupMessage.setTargetId(gr.getGroups().toArray(new String[gr.getGroups().size()]));
            logger.debug("cmd=MessageGroupPublish.send| groupMessage=" + JacksonUtils.toJson(groupMessage));
            return sendGroup(groupMessage);
        }
    }

    private Boolean sendGroupDirectional(List<String> toUsers, GroupMessage message) {
        String[][] split = split(message.getTargetId(), RongYunConstants.Limit.MAX_GROUPS_PER_SYSTEM);
        for (int i = 0; i < split.length; i++) {
            message.setTargetId(split[i]);
            ResponseResult result = null;
            try {
                result = sendDirect(toUsers, message);
            } catch (Exception e) {
                logger.error("cmd=MessageGroupPublish.send| rongyunResult| result=" + JsonsUtils.toJson(result));
            }
            logger.debug("cmd=MessageGroupPublish.send| rongyunResult| result=" + JsonsUtils.toJson(result));
            //判断是否超出频率
            if (result.getCode().intValue() == RongYunConstants.Result.OVERRATELIMIT) {
                //超出频率任务延时发送
                addTask(timeout -> {
                    try {
                        //clone msg  防止targetid 出错
                        GroupMessage taskMessage = new GroupMessage();
                        BeanUtils.copyProperties(message, taskMessage);
                        ResponseResult taskResult = sendDirect(toUsers, taskMessage);
                    } catch (Exception e) {
                        logger.error("MessageGroupPublish task Error:{}, send failed", e.getMessage());
                    }
                }, 1, TimeUnit.SECONDS);

                continue;
            }

            if (result.getCode().intValue() != RongYunConstants.Result.SUCCESSCODE) {
                return false;
            }
        }

        return true;
    }

    private ResponseResult sendDirect(List<String> toUsers, GroupMessage message) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("&fromUserId=").append(URLEncoder.encode(message.getSenderId().toString(), "UTF-8"));

        for (String tar : message.getTargetId()) {
            sb.append("&toGroupId=").append(URLEncoder.encode(tar, "UTF-8"));
        }

        for (String toUser : toUsers) {
            sb.append("&toUserId=").append(URLEncoder.encode(toUser, "UTF-8"));
        }

        sb.append("&objectName=").append(URLEncoder.encode(message.getContent().getType(), "UTF-8"));
        sb.append("&content=").append(URLEncoder.encode(message.getContent().toString(), "UTF-8"));
        if (message.getPushContent() != null) {
            sb.append("&pushContent=").append(URLEncoder.encode(message.getPushContent().toString(), "UTF-8"));
        }

        if (message.getPushData() != null) {
            sb.append("&pushData=").append(URLEncoder.encode(message.getPushData().toString(), "UTF-8"));
        }

        if (message.getIsPersisted() != null) {
            sb.append("&isPersisted=").append(URLEncoder.encode(message.getIsPersisted().toString(), "UTF-8"));
        }

        if (message.getIsCounted() != null) {
            sb.append("&isCounted=").append(URLEncoder.encode(message.getIsCounted().toString(), "UTF-8"));
        }

        if (message.getIsIncludeSender() != null) {
            sb.append("&isIncludeSender=").append(URLEncoder.encode(message.getIsIncludeSender().toString(), "UTF-8"));
        }

        if (message.getContentAvailable() != null) {
            sb.append("&contentAvailable=").append(URLEncoder.encode(message.getContentAvailable().toString(), "UTF-8"));
        }

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(this.rongCloud.getApiHostType(), config.getRonyun().getAppKey(), config.getRonyun().getAppSecret(), "/message/group/publish.json", "application/x-www-form-urlencoded");
        HttpUtil.setBodyParameter(body, conn);
        return (ResponseResult) GsonUtil.fromJson(CommonUtil.getResponseByCode("message/group", "send", HttpUtil.returnResult(conn)), ResponseResult.class);
    }


    private Boolean sendGroup(GroupMessage groupMessage) {
        try {
            String[][] split = split(groupMessage.getTargetId(), RongYunConstants.Limit.MAX_GROUPS_PER_SYSTEM);
            for (int i = 0; i < split.length; i++) {
                groupMessage.setTargetId(split[i]);
                ResponseResult result = rongCloud.message.group.send(groupMessage);
                logger.debug("cmd=MessageGroupPublish.send| rongyunResult| result=" + JsonsUtils.toJson(result));
                //判断是否超出频率
                if (result.getCode().intValue() == RongYunConstants.Result.OVERRATELIMIT) {
                    //超出频率任务延时发送
                    addTask(timeout -> {
                        //clone msg  防止targetid 出错
                        GroupMessage taskMessage = new GroupMessage();
                        BeanUtils.copyProperties(groupMessage, taskMessage);
                        try {
                            ResponseResult taskResult = rongCloud.message.group.send(taskMessage);
                        } catch (Exception e) {
                            logger.error("MessageGroupPublish task Error:{}, send failed", e.getMessage());
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
            logger.error("MessageGroupPublish Error:{}, send failed", e.getMessage());
        }
        return false;
    }

}
