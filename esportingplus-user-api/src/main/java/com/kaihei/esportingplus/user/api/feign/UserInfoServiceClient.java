package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.params.UserPictureParams;
import com.kaihei.esportingplus.user.api.params.UserUpdateParams;
import com.kaihei.esportingplus.user.api.vo.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户基础服务Feign
 * @zhangfang
 */
@FeignClient(name = "esportingplus-user-service", path = "/userinfo", fallbackFactory = UserInfoServiceClientFallbackFactory.class)
public interface UserInfoServiceClient {
    /**
     * 修改照片
     * @param params
     * @return
     */
    @PostMapping("/update/album/pictures")
    public ResponsePacket<PicturesVo> updatePicture(@RequestBody UserPictureParams params);

    /**
     * 修改用户头像（用户头像审核）
     * @param params
     * @return
     */
    @PostMapping("/update/avatar")
    public ResponsePacket updateAvatar(@RequestBody UserUpdateParams params);

    /**
     * 修改用户昵称
     * @param params
     * @return
     */
    @PostMapping("/update/username")
    public ResponsePacket updateUsername(@RequestBody UserUpdateParams params);

    /**
     * 修改地区
     * @param params
     * @return
     */
    @PostMapping("/update/region")
    public ResponsePacket updateRegion(@RequestBody UserUpdateParams params);

    /**
     * 修改个人说明
     * @param params
     * @return
     */
    @PostMapping("/update/desc")
    public ResponsePacket updateDesc(@RequestBody UserUpdateParams params);

    /**
     * 修改生日
     * @param params
     * @return
     */
    @PostMapping("/update/birthday")
    public ResponsePacket updateBirthday(@RequestBody UserUpdateParams params);

    /**
     * 修改圈子的展示状态
     * @param params
     * @return
     */
    @PostMapping("/update/showgroup")
    public ResponsePacket updateShowGroup(@RequestBody UserUpdateParams params);

    /**
     * 获取用户IM基本信息
     * @param imUid
     * @return
     */
    @GetMapping("/immember/{im_uid}")
    public ResponsePacket<ImmemberVo> getImMenber(@PathVariable("im_uid")  String imUid);

    /**
     * uid查用户信息
     * @return
     */
    @GetMapping("/detail")
    public ResponsePacket<UserInfoVo> getUserInfo(String uid);

    @GetMapping("/user/get/id")
    ResponsePacket<MembersUserVo> getMembersUserById(@RequestParam("id") Integer id);

    @GetMapping("/user/get/uid")
    ResponsePacket<MembersUserVo> getMembersUserByUid(@RequestParam("uid") String uid);

    /**
     * 获取用户信息卡
     * @return
     */
    @GetMapping("/getusercard")
    public ResponsePacket<UserCardVo> getUserCard(@RequestParam(value = "uid",required = false) String uid,
                                                  @RequestParam(value = "game",required = false) Integer game);
}
