package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.BaojiLevelRateVo;
import com.kaihei.esportingplus.api.vo.BaseGameRaidVo;
import com.kaihei.esportingplus.api.vo.FrontTopCareer;
import com.kaihei.esportingplus.api.vo.RaidAndGameServerVo;
import com.kaihei.esportingplus.api.vo.RedisGame;
import com.kaihei.esportingplus.api.vo.RedisGameAcrossZone;
import com.kaihei.esportingplus.api.vo.RedisGameBigZone;
import com.kaihei.esportingplus.api.vo.RedisGameRaid;
import com.kaihei.esportingplus.api.vo.RedisGameSmallZone;
import com.kaihei.esportingplus.api.vo.RedisSmallZoneRefAcrossZone;
import com.kaihei.esportingplus.api.vo.RedisTopCareer;
import com.kaihei.esportingplus.api.vo.ResourceVO;
import com.kaihei.esportingplus.api.vo.SimpleGameRaid;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ResourceClientFallbackFactory implements FallbackFactory<ResourceServiceClient> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ResourceClientFallbackFactory.class);

    @Override
    public ResourceServiceClient create(Throwable throwable) {
        return new ResourceServiceClient() {


            @Override
            public ResponsePacket<ResourceVO> getResourceById(String id) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RedisSmallZoneRefAcrossZone> getAcrossZoneFromSmallZoneCode(
                    Integer gameCode,
                    Integer smallZoneCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGameBigZone>> getBigOrSmallZoneByAcrossCode(
                    String token,
                    Integer gameCode, Integer zoneAcrossCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGameSmallZone>> getSmallZoneByAcrossCode(
                    Integer gameCode,
                    Integer zoneAcrossCode) {
                return ResponsePacket.onHystrix();
            }


            @Override
            public ResponsePacket<List<RedisGameSmallZone>> getSmallZoneByBigCode(Integer gameCode,
                    Integer zoneBigCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RedisGameBigZone> getBigAndSmallZoneByBigCode(Integer gameCode,
                    Integer zoneBigCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisTopCareer>> getCareerByGameCode(
                    Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<FrontTopCareer>> getCareerByGameCodeForApp(String token,
                    Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RedisTopCareer> getCareerByGameCodeAndTopCareerCode(
                    Integer gameCode,
                    Integer careerCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGame>> getGameList() {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGame>> getGameListForApp(String token) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGameRaid>> getGameRaids(Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<SimpleGameRaid>> getGameRaidsApp(String token,
                    Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RedisGameRaid> getSingleGameRaid(Integer gameCode,
                    Integer raidCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<BaseGameRaidVo>> getCertGameRaids(Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<BaseGameRaidVo> getCertSingleGameRaid(Integer gameCode,
                    Integer certRaidCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGameRaid>> getGameRaidThroughCertCode(Integer gameCode,
                    Integer certRaidCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGameAcrossZone>> getGameAcrossZone(Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGameAcrossZone>> getGameAcrossZoneForApp(String token,
                    Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<BigDecimal> getBaojiLevelRate(Integer baojiLevel) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<BaojiLevelRateVo>> getBaojiLevelRateBatch(
                    List<Integer> params) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGameBigZone>> getBigZoneAndSmallZone(Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<List<RedisGameBigZone>> getBigZoneAndSmallZoneForApp(String token,
                    Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<RaidAndGameServerVo> getRaidAndServer(String token,
                    Integer gameCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<String> getSmallZoneName(Integer gameCode,
                    Integer smallZoneCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<String> getBigZoneName(Integer gameCode, Integer bigZoneCode) {
                return ResponsePacket.onHystrix();
            }

            @Override
            public ResponsePacket<String> getAcrossZoneName(Integer gameCode,
                    Integer acrossZoneCode) {
                return ResponsePacket.onHystrix();
            }


        };
    }
}
