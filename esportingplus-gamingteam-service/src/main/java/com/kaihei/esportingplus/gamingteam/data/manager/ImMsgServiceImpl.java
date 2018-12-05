package com.kaihei.esportingplus.gamingteam.data.manager;

import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.ImKey;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.ForkJionUtils;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Im推送消息
 *
 * @author zhangfang
 */
@Service
public class ImMsgServiceImpl implements ImMsgService {

    @Autowired
    private ImMsgWithRetryService imMsgWithRetryService;

    @Override
    public void createGroup(ImGroupCommonParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        CompletableFuture.runAsync(() -> {
            imMsgWithRetryService.createGroup(params);
        }, ForkJionUtils.getCommonPool());
    }

    @Override
    public void joinGroup(ImGroupJoinParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        CompletableFuture.runAsync(() -> {
            imMsgWithRetryService.joinGroup(params);
        }, ForkJionUtils.getCommonPool());
    }

    @Override
    public void leaveGroup(ImGroupLeavelParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        CompletableFuture.runAsync(() -> {
            imMsgWithRetryService.leaveGroup(params);
        }, ForkJionUtils.getCommonPool());

    }

    @Override
    public void sendFullMembersMsg(ImFullMsgParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        CompletableFuture.runAsync(() -> {
            imMsgWithRetryService.sendFullMembersMsg(params);
        }, ForkJionUtils.getCommonPool());

    }

    @Override
    public void endTeam(ImEndTeamMsgParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        CompletableFuture.runAsync(() -> {
            imMsgWithRetryService.endTeam(params);
        }, ForkJionUtils.getCommonPool());

    }

    @Override
    public void dismissGroup(ImDismissGroupParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        CompletableFuture.runAsync(() -> {
            imMsgWithRetryService.dismissGroup(params);
        }, ForkJionUtils.getCommonPool());

    }

    @Override
    public void sendMatchMsg(ImMatchParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        CompletableFuture.runAsync(() -> {
            imMsgWithRetryService.sendMatchMsg(params);
        }, ForkJionUtils.getCommonPool());
    }

    @Override
    public void sendTeamStatusChange(ImTeamStatusChangeParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        CompletableFuture.runAsync(() -> {
            imMsgWithRetryService.sendTeamStatusChange(params);
        }, ForkJionUtils.getCommonPool());

    }

    @Override
    public void sendTeamStart(ImTeamStartParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        CompletableFuture.runAsync(() -> {
            imMsgWithRetryService.sendTeamStart(params);
        }, ForkJionUtils.getCommonPool());
    }


}
