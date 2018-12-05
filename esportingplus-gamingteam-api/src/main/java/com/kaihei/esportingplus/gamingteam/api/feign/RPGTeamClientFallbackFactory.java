package com.kaihei.esportingplus.gamingteam.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.BossConfirmPaidSuccessParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamCreateParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamJoinParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamEndParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamInfoBatchParams;
import com.kaihei.esportingplus.gamingteam.api.params.TeamQueryParams;
import com.kaihei.esportingplus.gamingteam.api.params.rpg.RPGTeamRolesParams;
import com.kaihei.esportingplus.gamingteam.api.params.UpdatePositionCountParams;
import com.kaihei.esportingplus.gamingteam.api.vo.BossInfoForOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.GameTeamTotal;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGMemberInTeamVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGRedisTeamBaseVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamCurrentInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamInfoVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamListVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberCompaintAdminVO;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamSequenceUidVO;
import com.kaihei.esportingplus.gamingteam.api.vo.rpg.RPGTeamStartOrderVO;
import com.kaihei.esportingplus.user.api.vo.QrCodeVo;
import com.kaihei.esportingplus.user.api.vo.UserGameAboardVo;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 车队服务熔断
 * @author liangyi
 */
@Component
public class RPGTeamClientFallbackFactory implements FallbackFactory<RPGTeamServiceClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RPGTeamClientFallbackFactory.class);

    @Override
    public RPGTeamServiceClient create(Throwable throwable) {
        return new RPGTeamServiceClient() {

            @Override
            public ResponsePacket<String> createGamingTeam(String token, RPGTeamCreateParams RPGTeamCreateParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<UserGameAboardVo>> getGamingTeamRoles(String token, RPGTeamRolesParams RPGTeamRolesParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> joinGamingTeam(String token, RPGTeamJoinParams teamJoinParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> quitGamingTeam(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> kickOutMember(String token, String sequence, String uid) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> dismissGamingTeam(String token, RPGTeamEndParams RPGTeamEndParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> confirmJoinGamingTeam(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> startGamingTeam(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> endGamingTeam(String token, RPGTeamEndParams RPGTeamEndParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<PagingResponse<RPGTeamListVO>> getTeamList(String token,Integer gameCode,
                TeamQueryParams teamQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<GameTeamTotal> getTeamTotal(String token, Integer gameCode,
                    TeamQueryParams teamQueryParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<QrCodeVo> getTeamQrCode(String token,Integer gameCode, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> updateTeamPositioncount(String token,UpdatePositionCountParams positionCountParams) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RPGRedisTeamBaseVO> getTeamBaseInfo(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RPGTeamCurrentInfoVO> getTeamCurrentInfo(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<BossInfoForOrderVO> getBossInfoForOrder(String sequence, String uid) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RPGTeamStartOrderVO> getBaojiInfoForOrder(String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RPGMemberInTeamVO> getMemberInTeam(String token, Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Integer> getBossPayCountdown(String token, String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<TeamGameResultVO> getGamingTeamGameResult(String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<TeamMemberCompaintAdminVO>> getGamingTeamMemberBriefInfo(String sequence) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<TeamInfoVO>> getBatchGamingTeamInfo(
                    TeamInfoBatchParams teamInfoBatchParams) {
                return ResponsePacket.onHystrix();
            }

            /**
             * 根据uid查找用户（暴鸡）所有已完成的车队唯一Id [去重]
             */
            @Override
            public ResponsePacket<List<TeamSequenceUidVO>> getBaojiTeamSequencesByUids(
                    List<String> uids) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<Void> bossConfirmPaidSuccess(String token,
                    BossConfirmPaidSuccessParams bossConfirmPaidSuccessParams) {
                return ResponsePacket.onHystrix();
            }

        };
    }
}
