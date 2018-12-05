package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.api.feign.UserInfoServiceClient;
import com.kaihei.esportingplus.user.api.params.UserPictureParams;
import com.kaihei.esportingplus.user.api.params.UserUpdateParams;
import com.kaihei.esportingplus.user.api.vo.*;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import com.kaihei.esportingplus.user.domain.service.MembersUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userinfo")
@Api(tags = {"用户基础服务接口"})
public class UserInfoController implements UserInfoServiceClient {

    @Autowired
    private MembersUserService membersUserService;

    @ApiOperation(value = "修改照片")
    @Override
    public ResponsePacket<PicturesVo> updatePicture(@RequestBody UserPictureParams params) {
        int userId = UserSessionContext.getUser().getId();
        Integer id = params.getId();
        String action = params.getAction();
        String url = params.getUrl();
        return ResponsePacket.onSuccess(membersUserService.updatePicture(userId,url,action,id));
    }

    @ApiOperation(value = "修改头像")
    @Override
    public ResponsePacket updateAvatar(@RequestBody UserUpdateParams params) {
        int userId = UserSessionContext.getUser().getId();
        String avatar = params.getAvatar();
        int result = membersUserService.updateAvatar(userId,avatar);
        return ResponsePacket.onSuccess(result);
    }

    @ApiOperation(value = "修改用户昵称")
    @Override
    public ResponsePacket updateUsername(@RequestBody UserUpdateParams params) {
        String uid = UserSessionContext.getUser().getUid();
        String username = params.getUsername();
        return ResponsePacket.onSuccess(membersUserService.updateUsername(uid, username));
    }

    @ApiOperation(value = "修改地址信息")
    @Override
    public ResponsePacket updateRegion(@RequestBody UserUpdateParams params) {
        String uid = UserSessionContext.getUser().getUid();
        String region = params.getRegion();
        return ResponsePacket.onSuccess(membersUserService.updateRegion(uid,region));
    }

    @ApiOperation(value = "修改个人说明")
    @Override
    public ResponsePacket updateDesc( @RequestBody UserUpdateParams params) {

        String uid = UserSessionContext.getUser().getUid();
        String desc = params.getDesc();
        return ResponsePacket.onSuccess(membersUserService.updateDesc(uid,desc));
    }

    @ApiOperation(value = "修改生日")
    @Override
    public ResponsePacket updateBirthday(@RequestBody UserUpdateParams params) {
        String uid = UserSessionContext.getUser().getUid();
        String birthday = params.getBirthday();
        return ResponsePacket.onSuccess(membersUserService.updateBirthDay(uid,birthday));
    }

    @ApiOperation(value = "修改圈子的展示状态")
    @Override
    public ResponsePacket updateShowGroup(@RequestBody UserUpdateParams params) {
        String uid = UserSessionContext.getUser().getUid();
        Boolean showgroup = params.isShowgroup();
        return ResponsePacket.onSuccess(membersUserService.updateShowGroup(uid,showgroup));
    }

    @ApiOperation(value = "获取用户IM基本信息")
    @Override
    public ResponsePacket<ImmemberVo> getImMenber(@PathVariable("im_uid")  String imUid) {
        return ResponsePacket.onSuccess(membersUserService.getImMenber(imUid));
    }

    @Override
    public ResponsePacket<UserInfoVo> getUserInfo(String uid) {
        if(StringUtils.isEmpty(uid)){
            uid = UserSessionContext.getUser().getUid();
        }
        UserInfoVo userInfoVo = membersUserService.getUserInfo(uid);
        return ResponsePacket.onSuccess(userInfoVo);
    }

    @Override
    @GetMapping("/user/get/id")
    public ResponsePacket<MembersUserVo> getMembersUserById(@RequestParam("id") Integer id) {
        MembersUser u = membersUserService.getMembersUserById(id);
        if (u != null) {
            return ResponsePacket.onSuccess(BeanMapper.map(u, MembersUserVo.class));
        }
        return ResponsePacket.onSuccess();
    }

    @Override
    @GetMapping("/user/get/uid")
    public ResponsePacket<MembersUserVo> getMembersUserByUid(@RequestParam("uid") String uid) {
        MembersUser u = membersUserService.getMembersUserByUid(uid);
        if (u != null) {
            return ResponsePacket.onSuccess(BeanMapper.map(u, MembersUserVo.class));
        }
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket<UserCardVo> getUserCard(@RequestParam(value = "uid",required = false) String uid,
                                                  @RequestParam(value = "game",required = false) Integer game) {
        if(StringUtils.isEmpty(uid)){
            uid = UserSessionContext.getUser().getUid();
        }
        return ResponsePacket.onSuccess(membersUserService.getUserCard(uid,game));
    }
}
