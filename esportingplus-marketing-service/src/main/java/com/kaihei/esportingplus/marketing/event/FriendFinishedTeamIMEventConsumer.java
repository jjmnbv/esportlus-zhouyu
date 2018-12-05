package com.kaihei.esportingplus.marketing.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.marketing.api.vo.UserFreeCouponsInfoVo;
import com.kaihei.esportingplus.marketing.config.ContentConfig;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserFreeCouponsService;
import com.kaihei.esportingplus.user.api.enums.MessageType;
import com.kaihei.esportingplus.user.api.feign.UserInfoServiceClient;
import com.kaihei.esportingplus.user.api.feign.UserMessageServiceClient;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import com.kaihei.esportingplus.user.api.vo.SendFreeTeamMessageVo;
import com.kaihei.esportingplus.user.api.vo.SendFreeTeamMsgDtlVo;
import com.kaihei.esportingplus.user.api.vo.SendMessageDataVo;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-17 16:51
 * @Description:
 */
@Component
public class FriendFinishedTeamIMEventConsumer extends EventConsumer {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserInfoServiceClient userInfoServiceClient;

    @Autowired
    private UserMessageServiceClient userMessageServiceClient;

    @Autowired
    private MarketUserFreeCouponsService marketUserFreeCouponsService;

    @Autowired
    private ContentConfig contentConfig;

    @Subscribe
    @AllowConcurrentEvents
    public void friendFinishedTeamIM(FriendFinishedTeamIMEvent event) {
        String json = JacksonUtils.toJson(event);
        logger.info("cmd=FriendFinishedTeamIMEventConsumer.friendFinishedTeamIM | msg={} | req={}",
                "开发发送IM", json);
        if (event == null || event.getUserId() == null || event.getFreeAmount() == null
                || event.getImUserId() == null) {
            logger.error(
                    "cmd=FriendFinishedTeamIMEventConsumer.friendFinishedTeamIM | msg={} | req={}",
                    "参数错误", json);
            return;
        }
        ResponsePacket<MembersUserVo> userResp = userInfoServiceClient
                .getMembersUserById(event.getUserId());
        if (!userResp.responseSuccess()) {
            logger.error("cmd=FriendFinishedTeamIMEventConsumer.friendFinishedTeamIM | msg={}",
                    "用户服务熔断");
            return;
        }
        if (userResp.getData() == null) {
            logger.error("cmd=FriendFinishedTeamIMEventConsumer.friendFinishedTeamIM | msg={}",
                    "该用户不存在");
            return;
        }
        MembersUserVo user = userResp.getData();
        if (user == null) {
            logger.error(
                    "cmd=FriendFinishedTeamIMEventConsumer.friendFinishedTeamIM | msg={} | userId={}",
                    "该用户不存在", event.getUserId());
            return;
        }
        ResponsePacket<MembersUserVo> imUserResp = userInfoServiceClient
                .getMembersUserById(event.getImUserId());
        if (!imUserResp.responseSuccess()) {
            logger.error("cmd=FriendFinishedTeamIMEventConsumer.friendFinishedTeamIM | msg={}",
                    "用户服务熔断");
            return;
        }
        if (imUserResp.getData() == null) {
            logger.error("cmd=FriendFinishedTeamIMEventConsumer.friendFinishedTeamIM | msg={}",
                    "该用户不存在");
            return;
        }
        MembersUserVo imUser = imUserResp.getData();
        UserFreeCouponsInfoVo info = marketUserFreeCouponsService
                .getUserFreeCouponsInfo(imUser.getUid());
        String s = contentConfig.getFriendFinishOrderIm();
        String content = String.format(s, event.getFreeAmount());

        SendFreeTeamMessageVo vo = new SendFreeTeamMessageVo();
        vo.setReciever(Arrays.asList(imUser.getUid()));

        //好友完成免费车队
        List<SendMessageDataVo> dataArr = new LinkedList<>();
        SendFreeTeamMsgDtlVo msg = new SendFreeTeamMsgDtlVo("好友完成免费车队", content, dataArr,
                MessageType.FREE_TEAM.getCode(), null);
        vo.setData(msg);

        SendMessageDataVo nickName = new SendMessageDataVo("好友昵称", user.getUsername());
        SendMessageDataVo awardFrequency = new SendMessageDataVo("奖励次数",
                event.getFreeAmount().toString());
        SendMessageDataVo remainFrequency = new SendMessageDataVo("剩余次数",
                info.getAvailableCount() == null ? null : info.getAvailableCount().toString());
        dataArr.add(nickName);
        dataArr.add(awardFrequency);
        dataArr.add(remainFrequency);

        userMessageServiceClient.SendFreeTeamMessage(vo);
        logger.info("cmd=FriendFinishedTeamIMEventConsumer.friendFinishedTeamIM | msg={} | req={}",
                "结束发送IM", json);
    }
}
