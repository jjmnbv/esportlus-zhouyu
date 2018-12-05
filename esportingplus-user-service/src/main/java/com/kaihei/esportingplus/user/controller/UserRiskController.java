package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.feign.UserRiskServiceClient;
import com.kaihei.esportingplus.user.api.vo.MembersUserVo;
import com.kaihei.esportingplus.user.api.vo.UserRiskVo;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.service.MembersUserService;
import com.kaihei.esportingplus.user.domain.service.UserRiskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/risk")
public class UserRiskController implements UserRiskServiceClient {

    @Autowired
    private UserRiskService userRiskService;

    @Autowired
    private MembersUserService membersUserService;


    @Override
    public ResponsePacket<Integer> getRiskNextDataCount(@RequestParam(value = "user_id",required = true) String userId,
                                          @RequestParam(value = "device_id", required = true) String deviceId) {
        return  ResponsePacket.onSuccess(userRiskService.countDeviceId(userId, deviceId));
    }

    @Override
    public ResponsePacket<UserRiskVo> getRiskNextDataInfo(@RequestParam(value = "user_id",required = true) String userId,
                                                          @RequestParam(value = "device_id", required = true) String deviceId) {
        return  ResponsePacket.onSuccess(userRiskService.getEntityByUidAndDeviceId(userId, deviceId));
    }

    @Override
    public ResponsePacket<List<UserRiskVo>> getDeviceIds(@RequestParam(value = "uids",required = true) String uids) {
        return  ResponsePacket.onSuccess(userRiskService.getDeviceIdsByUids(uids));
    }

    @Override
    public ResponsePacket<MembersUserVo> getUserInfo(@RequestParam(value = "uid",required = true) String uid) {
        MembersUserVo vo = new MembersUserVo();
        MembersUser membersUser = membersUserService.getMembersUserByUid(uid);
        BeanUtils.copyProperties(membersUser, vo);
        return ResponsePacket.onSuccess(vo);
    }
}
