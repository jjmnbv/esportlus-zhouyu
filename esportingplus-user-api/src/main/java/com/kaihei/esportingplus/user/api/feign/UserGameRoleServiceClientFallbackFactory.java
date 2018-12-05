package com.kaihei.esportingplus.user.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.params.UserGameQueryBaseParams;
import com.kaihei.esportingplus.user.api.params.UserGameRaidCodeQueryParams;
import com.kaihei.esportingplus.user.api.params.UserGameRoleAcrossQueryParams;
import com.kaihei.esportingplus.user.api.params.UserSingeRoleQueryParams;
import com.kaihei.esportingplus.user.api.vo.*;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserGameRoleServiceClientFallbackFactory implements
        FallbackFactory<UserGameRoleServiceClient> {

    @Override
    public UserGameRoleServiceClient create(Throwable throwable) {
        return new UserGameRoleServiceClient() {
            @Override
            public ResponsePacket<List<UserGameBaseRoleInfoVo>> getUserAllBaseRoles(String userId,
                    Integer gameCode) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<List<UserGameDetailRoleInfoVo>> getUserAllDetailRoles(
                    String userId,
                    Integer gameCode, UserGameQueryBaseParams params) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<List<UserGameDetailRoleInfoVo>> getCertificalUserGameRoles(
                    String userId,
                    Integer gameCode, UserGameRaidCodeQueryParams params) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<Boolean> isCertifica(String userId, Integer gameCode) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<List<UserGameAboardVo>> getAboardGameRole(
                    UserGameRoleAcrossQueryParams params) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<Void> addUserGameRole(String token, UserGameRoleSimpleVo vo) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<Void> deleteUserGameRole(String token, Long roleId) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<Boolean> isCertificaApp(String token, Integer gameCode) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<List<UserGameDetailRoleInfoVo>> getCertificalUserGameRolesApp(
                    String token, Integer gameCode, UserGameRaidCodeQueryParams params) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<List<UserGameDetailRoleInfoVo>> getUserAllDetailRolesApp(
                    String token,
                    Integer gameCode, UserGameQueryBaseParams params) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<List<UserGameBaseRoleInfoVo>> getUserAllBaseRolesApp(String token,
                    Integer gameCode) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<UserSingleRoleDetailInfoVo> getUserIdentityRoleInfo(
                    UserSingeRoleQueryParams params) {
                return ResponsePacket.onError();
            }

            @Override
            public ResponsePacket<List<CertRoleWithJoinRaidVo>> getUserCredentialsAndRaidRoles(
                    String token,
                    Integer gameCode) {
                return ResponsePacket.onError();
            }
        };
    }
}
