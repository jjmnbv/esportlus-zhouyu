package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.gamingteam.api.params.PVPFreeTeamsForBackupParams;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamFreeCreateParams;
import com.kaihei.esportingplus.gamingteam.api.params.pvp.PVPTeamFreeJoinParams;
import com.kaihei.esportingplus.gamingteam.api.vo.PVPFreeTeamBackupVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamCurrentMatchingInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamMemberInfoVO;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * pvp 免费车队服务熔断
 *
 * @author liangyi
 */
@Component
public class PVPFreeTeamClientFallbackFactory implements FallbackFactory<PVPFreeTeamServiceClient> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PVPFreeTeamClientFallbackFactory.class);

    @Override
    public PVPFreeTeamServiceClient create(Throwable throwable) {
        return new PVPFreeTeamServiceClient() {

            @Override
            public ResponsePacket<TeamSequenceVO> createTeam(String token,
                    PVPTeamFreeCreateParams pvpTeamFreeCreateParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PVPFreeTeamBaseVO> getTeamBaseInfo(String token,
                    String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PVPFreeTeamCurrentInfoVO> getTeamCurrentInfo(String token,
                    String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<?> joinGamingTeam(String token,
                    PVPTeamFreeJoinParams teamJoinParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<?> quitGamingTeam(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> kickOutMember(String token, String sequence, String uid) {
                return ResponsePacket.onHystrix();
            }

            /**
             * 立即开车
             *
             * @param token auth 认证的token
             * @param sequence 车队序列号
             */
            @Override
            public ResponsePacket<Void> startGamingTeam(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateTeamPositioncount(String token,
                    UpdatePositionCountParams positionCountParams) {
                return ResponsePacket.onHystrix();
            }

            /**
             * 确认准备
             *
             * @param token auth 认证的token
             * @param sequence 车队序列号
             */
            @Override
            public ResponsePacket<Void> confirmReadyGamingTeam(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<?> endGamingTeam(String token,
                    PVPTeamEndParams pvpTeamEndParams) {
                return ResponsePacket.onHystrix();
            }

            /**
             * 解散车队
             *
             * @param token auth 认证的token
             */
            @Override
            public ResponsePacket<Void> dismissGamingTeam(String token, String teamSequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<TeamGameResultVO>> getTeamGameResult(String token,
                    String teamSequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PVPFreeTeamMemberInfoVO> getTeamMemberInfo(String token,
                    String teamSequence) {
                return ResponsePacket.onHystrix();
            }

            /**
             * 取消准备
             */
            @Override
            public ResponsePacket<Void> cancelReadyGamingTeam(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PVPFreeTeamCurrentMatchingInfoVO> getCurrentMatchingInfo(String token) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<PVPFreeTeamBackupVO>> getPvpFreeTeamBackup(
                    PVPFreeTeamsForBackupParams params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PVPFreeMemberInTeamVO> getMemberInTeam(String token) {
                return ResponsePacket.onHystrix();
            }
        };
    }
}
