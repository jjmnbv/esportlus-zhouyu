package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenPointGainConfigValueVO;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenPointGainConfigValueWithResultVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import feign.hystrix.FallbackFactory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChickenpointGainConfigClientFallbackFactory implements
        FallbackFactory<ChickenpointGainConfigClient> {

    @Override
    public ChickenpointGainConfigClient create(Throwable throwable) {
        return new ChickenpointGainConfigClient() {

            /**
             * @param freeTeamTypeId 免费车队类型Id
             * @param gameDanId 游戏段位Id
             * @param gameResultCode 游戏结果Code
             * @param baojiIdentityCode 游戏暴鸡身份Id
             * @return 配置值
             */
            @Override
            public ResponsePacket<Integer> findChickenpointGainConfigValue(Integer freeTeamTypeId,
                    Integer gameDanId, Integer gameResultCode, Integer baojiIdentityCode,
                    Integer settlementTypeId) {
                return ResponsePacket.onHystrix();
            }

            /**
             * 计算车队可获取的鸡分数值
             */
            @Override
            public ResponsePacket<Integer> calculateChickenpointGainValue(Integer freeTeamTypeId,
                    Integer baojiIdentityCode, Integer settlementTypeId, List<Integer> gameDanIds) {
                return ResponsePacket.onHystrix(throwable);
            }

            @Override
            public ResponsePacket<List<ChickenPointGainConfigValueVO>> findDanConfigValues(
                    Integer freeTeamTypeId, Integer gameResultCode, Integer baojiIdentityLevel) {
                return ResponsePacket.onHystrix();
            }

            /**
             * 游戏类型  + 暴鸡等级 -> 游戏鸡分获取配置
             */
            @Override
            public ResponsePacket<List<ChickenPointGainConfigValueWithResultVO>> getChickenConfigValueByTeamTypeAndBaojiLevel(
                    Integer teamTypeId, Integer baojiIdentityLevel) {
                return ResponsePacket.onHystrix(throwable);
            }
        };
    }
}
