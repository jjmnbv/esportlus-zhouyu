package com.kaihei.esportingplus.core.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.core.api.feign.MsgUserGroupServiceClient;
import com.kaihei.esportingplus.core.api.params.GroupDismissParam;
import com.kaihei.esportingplus.core.api.params.GroupJoinParam;
import com.kaihei.esportingplus.core.api.params.GroupQuitParam;
import com.kaihei.esportingplus.core.domain.service.RongYunService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author liuyang
 * @Description 群组controller
 * @Date 2018/10/26 17:45
 **/
@RestController
@RequestMapping("/message/group")
@Api(tags = {"群组管理"})
public class MsgGroupController implements MsgUserGroupServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MsgGroupController.class);

    @Autowired
    private RongYunService rongYunService;

    @Override
    public ResponsePacket<Boolean> createGroup(@RequestBody GroupJoinParam groupCreateParam){
        logger.debug("cmd=MsgGroupController.createGroup | param="+ FastJsonUtils.toJson(groupCreateParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, groupCreateParam);
        return ResponsePacket.onSuccess(rongYunService.joinGroup(groupCreateParam.getMembers(), groupCreateParam.getGroupId(), groupCreateParam.getGroupName()));
    }

    @Override
    public ResponsePacket<Boolean> joinGroup(@RequestBody GroupJoinParam groupCreateParam) {
        logger.debug("cmd=MsgGroupController.joinGroup | param="+ FastJsonUtils.toJson(groupCreateParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, groupCreateParam);
        return ResponsePacket.onSuccess(rongYunService.joinGroup(groupCreateParam.getMembers(), groupCreateParam.getGroupId(), groupCreateParam.getGroupName()));
    }

    @Override
    public ResponsePacket<Boolean> leaveGroup(@RequestBody GroupQuitParam groupQuitParam) {
        logger.debug("cmd=MsgGroupController.leaveGroup | param="+ FastJsonUtils.toJson(groupQuitParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, groupQuitParam);
        return ResponsePacket.onSuccess(rongYunService.leaveGroup(groupQuitParam.getMemebers(), groupQuitParam.getGroupId()));
    }

    @Override
    public ResponsePacket<Boolean> dismissGroup(@RequestBody GroupDismissParam groupDismissParam) {
        logger.debug("cmd=MsgGroupController.dismissGroup | param="+ FastJsonUtils.toJson(groupDismissParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, groupDismissParam);
        return ResponsePacket.onSuccess(this.rongYunService.dismissGroup(groupDismissParam.getUserId(), groupDismissParam.getGroupId()));
    }
}
