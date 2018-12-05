package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.constant.ImKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.core.api.enums.MessageTypeEnum;
import com.kaihei.esportingplus.core.api.feign.MsgSendServiceClient;
import com.kaihei.esportingplus.core.api.feign.MsgUserGroupServiceClient;
import com.kaihei.esportingplus.core.api.params.GroupDismissParam;
import com.kaihei.esportingplus.core.api.params.GroupJoinParam;
import com.kaihei.esportingplus.core.api.params.GroupQuitParam;
import com.kaihei.esportingplus.core.api.params.MessageSendParam;
import com.kaihei.esportingplus.gamingteam.api.enums.ImMemberLeaveType;
import com.kaihei.esportingplus.gamingteam.api.enums.MatchResult;
import com.kaihei.esportingplus.gamingteam.api.params.ImDismissGroupParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImEndTeamMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImFullMsgParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupCommonParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImGroupLeavelParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImMatchParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImTeamStartParams;
import com.kaihei.esportingplus.gamingteam.api.params.ImTeamStatusChangeParams;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamImCmdMsgContent;
import com.kaihei.esportingplus.gamingteam.config.ImConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * Im推送消息
 *
 * @author zhangfang
 */
@Service
public class ImMsgWithRetryServiceImpl implements ImMsgWithRetryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImMsgWithRetryServiceImpl.class);
    @Autowired
    private MsgUserGroupServiceClient msgUserGroupServiceClient;
    @Autowired
    private MsgSendServiceClient msgSendServiceClient;
    @Autowired
    private ImConfig imConfig;

    @Override
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void createGroup(ImGroupCommonParams params) {
        joinOneGroup(params);
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void joinGroup(ImGroupJoinParams params) {
        joinOneGroup(params);
        //加入用户组成功
        LOGGER.info(">>发送用户加入车队命令，加入人:{},车队号:{}", params.getUid(),
                params.getTeamSequence());
        //发送消息
        ResponsePacket<Boolean> sendResponsePacket = sendCmdMsg(imConfig.getCmdTemplateId(),
                ImKey.JOIN_TEAM_KEY,
                Arrays.asList(params.getGroupId()), params.getMembers(),
                params.getMsgContent(),
                params.getTeamSequence());
        Boolean result =
                sendResponsePacket.getData() == null ? false : sendResponsePacket.getData();
        if (!result) {
            LOGGER.info(">>发送用户加入车队失败，加入人:{},车队号:{}", params.getUid(),
                    params.getTeamSequence());
        } else {
            LOGGER.info(">>发送用户加入车队成功，加入人:{},车队号:{}", params.getUid(),
                    params.getTeamSequence());
        }

    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void leaveGroup(ImGroupLeavelParams params) {
        //过滤离开或者被离开者本身
        List<String> otherMembers = params.getMembers().stream()
                .filter(it -> !it.equals(params.getUid())).collect(Collectors.toList());
        if (ImMemberLeaveType.active.getCode() != params.getLeaveType()) {
            //如果是被动离开,先给自己发被踢出的消息，再离开群组，然后向其他人推送离开消息
            LOGGER.info(">>发送用户被T命令，被T者:{},车队号:{}", params.getUid(), params.getTeamSequence());
            ResponsePacket<Boolean> sendResponsePacket = sendCmdMsg(imConfig.getCmdTemplateId(),
                    ImKey.TEAM_LEADER_OUT_MEMBER_KEY, Arrays.asList(params.getGroupId()),
                    Arrays.asList(new String[]{params.getUid()}), params.getToSelfMsgContent(),
                    params.getTeamSequence());
            LOGGER.info(">>发送用户被T命令结果:{}，被T者:{},车队号:{}", sendResponsePacket.getData(),
                    params.getUid(), params.getTeamSequence());

        }
        //离开群组
        leaveOneGroup(params.getGroupId(), params.getUid());
        //最后给其他人发送消息
        if (ObjectTools.isNotEmpty(otherMembers)) {
            ResponsePacket<Boolean> booleanResponsePacket = sendCmdMsg(
                    imConfig.getCmdTemplateId(), ImKey.EXIT_TEAM_KEY,
                    Arrays.asList(params.getGroupId()), otherMembers,
                    params.getToOtheMsgContent(), params.getTeamSequence());
            LOGGER.info(">>通知有人退出车队消息结果:{}，退出者:{},车队号:{}", booleanResponsePacket.getData(),
                    params.getUid(), params.getTeamSequence());
        }

    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void sendFullMembersMsg(ImFullMsgParams params) {
        ResponsePacket<Boolean> booleanResponsePacket = sendCmdMsg(imConfig.getCmdTemplateId(),
                ImKey.TEAM_MEMBER_FULL_KEY,
                Arrays.asList(params.getGroupId()),
                params.getMembers(),
                params.getMsgContent(), params.getTeamSequence());
        LOGGER.info(">>发送满员车队消息命令结果:{}，车队号:{}", booleanResponsePacket.getData(),
                params.getTeamSequence());

    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void endTeam(ImEndTeamMsgParams params) {
        //发送结束车队通知
        ResponsePacket<Boolean> booleanResponsePacket = sendCmdMsg(
                imConfig.getCmdTemplateId(),
                ImKey.END_TEAM_SERVER,
                Arrays.asList(params.getGroupId()), params.getMembers(),
                params.getMsgContent(),
                params.getTeamSequence());
        LOGGER.info(">>发送车队服务结束消息结果:{}，车队号:{}", booleanResponsePacket.getData(),
                params.getTeamSequence());
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void dismissGroup(ImDismissGroupParams params) {
        //解散群组，先给队员发送消息，再解散群组
        List<String> otherMembers = params.getMembers();
        if(!params.isSendLeader()){
            otherMembers = params.getMembers().stream()
                    .filter(it -> !it.equals(params.getUid())).collect(Collectors.toList());
        }
        if (ObjectTools.isNotEmpty(otherMembers)) {
            sendCmdMsg(imConfig.getCmdTemplateId(), ImKey.TEAM_DISMISS_KEY,
                    Arrays.asList(params.getGroupId()), otherMembers,
                    params.getMsgContent(), params.getTeamSequence());
        }
        //然后解散群组
        this.dismissOneGroup(params.getUid(), params.getGroupId());

    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void sendMatchMsg(ImMatchParams params) {
        String key = MatchResult.SUCCESS.getCode().equals(params.getResult())
                ? ImKey.MEMBER_MATCH_SUCCESS_KEY
                : ImKey.MEMBER_MATCH_FAIL_KEY;
        ResponsePacket<Boolean> booleanResponsePacket = sendCmdMsg(
                imConfig.getCmdSystemTemplateId(),
                key, params.getUids(), new ArrayList<>(),
                params.getMsgContent(), params.getTeamSequence());
        if (key.equals(ImKey.MEMBER_MATCH_SUCCESS_KEY)) {
            LOGGER.info(">>发送匹配成功消息结果:{}，匹配者:{},车队号:{}", booleanResponsePacket.getData(),
                    params.getUids(), params.getTeamSequence());
        } else {
            LOGGER.info(">>发送匹配失败消息结果:{}，匹配者:{}", booleanResponsePacket.getData(),
                    params.getUids());
        }

    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void sendTeamStatusChange(ImTeamStatusChangeParams params) {
        sendCmdMsg(imConfig.getCmdTemplateId(), params.getStatusEnum().getImKey(),
                Arrays.asList(params.getGroupId()), params.getMembers(),
                null, params.getTeamSequence());

    }

    @Override
    @Retryable(value = Exception.class, exclude = BusinessException.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void sendTeamStart(ImTeamStartParams params) {
        ResponsePacket<Boolean> booleanResponsePacket = sendCmdMsg(
                imConfig.getCmdTemplateId(), ImKey.TEAM_START_KEY,
                Arrays.asList(params.getGroupId()), params.getMembers(),
                params.getMsgContent(), params.getTeamSequence());
        LOGGER.info(">>发送车队开车消息结果:{}，车队号:{}", booleanResponsePacket.getData(),
                params.getTeamSequence());
    }

    private void dismissOneGroup(String uid, String groupId) {
        GroupDismissParam groupDismissParam = new GroupDismissParam();
        groupDismissParam.setUserId(uid);
        groupDismissParam.setGroupId(groupId);
        msgUserGroupServiceClient.dismissGroup(groupDismissParam);
    }

    private ResponsePacket<Boolean> leaveOneGroup(String groupId, String uid) {
        GroupQuitParam groupQuitParam = new GroupQuitParam();
        groupQuitParam.setGroupId(groupId);
        groupQuitParam.setMemebers(Arrays.asList(new String[]{uid}));
        return msgUserGroupServiceClient.leaveGroup(groupQuitParam);
    }

    private ResponsePacket<Boolean> sendCmdMsg(int templateId, String imKey, List<String> reciver,
            List<String> toUsers,
            String msgContent, String teamSequence) {
        MessageSendParam sendParam = new MessageSendParam();
        sendParam.setSender(imConfig.getSysUser());
        sendParam.setReciever(reciver);
        sendParam.setMessageType(MessageTypeEnum.SYSTEM);
        sendParam.setTemplateId(templateId);
        sendParam.setToSelf(false);
        //这里给toUsers置空，发送全员消息
        sendParam.setToUsers(toUsers);
        Map data = new HashMap();
        data.put("name", imKey);
        TeamImCmdMsgContent content = new TeamImCmdMsgContent();
        content.setContent(msgContent);
        content.setTeamSequence(teamSequence);
        data.put("data", JacksonUtils.toJsonWithSnakeAndNoNull(content));
        sendParam.setData(data);
        LOGGER.info("IM消息发送中，发送消息:{}", JacksonUtils.toJsonWithSnakeAndNoNull(sendParam));
        ResponsePacket<Boolean> booleanResponsePacket = msgSendServiceClient.send(sendParam);
        if (!(booleanResponsePacket.responseSuccess() && booleanResponsePacket.getData() != null
                && booleanResponsePacket.getData())) {
            LOGGER.info("IM消息发送失败，失败原因:{},发送消息:{}", JacksonUtils.toJson(booleanResponsePacket),
                    JacksonUtils.toJsonWithSnakeAndNoNull(sendParam));
            throw new BusinessException(BizExceptionEnum.IM_MSG_SEND_ERROR);
        }
        return booleanResponsePacket;
    }

    private ResponsePacket<Boolean> joinOneGroup(ImGroupCommonParams params) {
        List<String> members = new ArrayList<>();
        GroupJoinParam groupJoinParam = BeanMapper.map(params, GroupJoinParam.class);
        members.add(params.getUid());
        groupJoinParam.setMembers(members);
        ResponsePacket<Boolean> group = msgUserGroupServiceClient.joinGroup(groupJoinParam);
        if (!(group.responseSuccess() && group.getData() != null && group.getData())) {
            LOGGER.info(">>> 用户:[{}]加入群组[{}]失败", params.getUid(), params.getGroupId());
            throw new BusinessException(BizExceptionEnum.IM_MSG_SEND_ERROR);
        } else {
            LOGGER.info(">>> 用户:[{}]加入群组[{}]成功", params.getUid(), params.getGroupId());
        }
        return group;
    }
}
