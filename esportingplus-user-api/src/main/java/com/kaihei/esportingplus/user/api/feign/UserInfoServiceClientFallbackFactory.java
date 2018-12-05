package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.params.UserPictureParams;
import com.kaihei.esportingplus.user.api.params.UserUpdateParams;
import com.kaihei.esportingplus.user.api.vo.*;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
public class UserInfoServiceClientFallbackFactory implements
        FallbackFactory<UserInfoServiceClient> {

    @Override
    public UserInfoServiceClient create(Throwable throwable) {
        return new UserInfoServiceClient() {
            @Override
            public ResponsePacket<PicturesVo> updatePicture(UserPictureParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateAvatar(UserUpdateParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateUsername(UserUpdateParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateRegion(UserUpdateParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateDesc(UserUpdateParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateBirthday(UserUpdateParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket updateShowGroup(UserUpdateParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<ImmemberVo> getImMenber(String imUid) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<UserInfoVo> getUserInfo(String uid) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<MembersUserVo> getMembersUserById(Integer id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<MembersUserVo> getMembersUserByUid(String uid) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<UserCardVo> getUserCard(String uid,Integer game) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}
