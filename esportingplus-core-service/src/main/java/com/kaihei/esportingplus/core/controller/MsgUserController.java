package com.kaihei.esportingplus.core.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.core.api.feign.MsgUserServiceClient;
import com.kaihei.esportingplus.core.api.params.UserBlackParam;
import com.kaihei.esportingplus.core.api.params.UserBlockParam;
import com.kaihei.esportingplus.core.api.params.MsgUserParam;
import com.kaihei.esportingplus.core.api.params.UserTagParam;
import com.kaihei.esportingplus.core.domain.service.RongYunService;
import com.kaihei.esportingplus.core.utils.RonYunUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author liuyang
 * @Description 消息用户管理api
 * @Date 2018/10/26 11:57
 **/
@RestController
@RequestMapping("/message/user")
@Api(tags = {"用户管理"})
public class MsgUserController implements MsgUserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MsgUserController.class);

    @Autowired
    private RongYunService rongYunService;

    @Override
    public ResponsePacket<String> getToken(@RequestBody MsgUserParam param) {
        logger.debug("cmd=MsgUserController.getToken | param="+ FastJsonUtils.toJson(param));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, param);
        String token = rongYunService.getToken(param.getUserId(), param.getName(), param.getPortraitUri());
        if (StringUtils.isNotEmpty(token)){
            return ResponsePacket.onSuccess(token);
        }else {
            return ResponsePacket.onError();
        }
    }

    @Override
    public ResponsePacket<Boolean> updateUser(@RequestBody MsgUserParam param) {
        logger.debug("cmd=MsgUserController.updateUser | param="+ FastJsonUtils.toJson(param));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, param);
        return ResponsePacket.onSuccess(rongYunService.updateUser(param.getUserId(), param.getName(), param.getPortraitUri()));
    }

    @Override
    public ResponsePacket<Boolean> blockUser(@RequestBody UserBlockParam blockUserParam) {
        logger.debug("cmd=MsgUserController.blockUser | param="+ FastJsonUtils.toJson(blockUserParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, blockUserParam);
        return ResponsePacket.onSuccess(rongYunService.blockUser(blockUserParam.getUserIds(), blockUserParam.getMinute()));
    }

    @Override
    public ResponsePacket<Boolean> unBlockUser(@RequestBody(required = true) List<String> uids) {
        logger.debug("cmd=MsgUserController.unBlockUser | param="+ FastJsonUtils.toJson(uids));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uids);
        return ResponsePacket.onSuccess(rongYunService.unBlockUser(uids));
    }

    @Override
    public ResponsePacket<Boolean> addUserToBlacklist(@RequestBody UserBlackParam blackUserParam) {
        logger.debug("cmd=MsgUserController.addUserToBlacklist | param="+ FastJsonUtils.toJson(blackUserParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, blackUserParam);
        return ResponsePacket.onSuccess(rongYunService.addUserToBlacklist(blackUserParam.getUserId(), blackUserParam.getBlackUserIds()));
    }

    @Override
    public ResponsePacket<Boolean> removeUserFromBlacklist(@RequestBody UserBlackParam blackUserParam) {
        logger.debug("cmd=MsgUserController.removeUserFromBlacklist | param="+ FastJsonUtils.toJson(blackUserParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, blackUserParam);
        return ResponsePacket.onSuccess(rongYunService.removeUserFromBlacklist(blackUserParam.getUserId(), blackUserParam.getBlackUserIds()));
    }

    @Override
    public ResponsePacket<String[]> queryUsersBlacklist(@PathVariable(name = "uid", required = true) String uid) {
        logger.debug("cmd=MsgUserController.queryUsersBlacklist | param="+ uid);
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid);
        return ResponsePacket.onSuccess(rongYunService.queryUsersBlacklist(uid));
    }

    @Override
    public ResponsePacket<String> queryRonyunUid(@PathVariable(name = "uid", required = true)String uid) {
        logger.debug("cmd=MsgUserController.queryRonyunUid | param="+ uid);
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uid);
        return ResponsePacket.onSuccess(RonYunUtils.encodeIMUser(uid));
    }

    @Override
    public ResponsePacket<Boolean> setTag(@RequestBody UserTagParam userTagParam) {
        logger.debug("cmd=MsgUserController.setTag | param="+ FastJsonUtils.toJson(userTagParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userTagParam);
        rongYunService.setTag(userTagParam);

        return null;
    }
}
