package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenPointGainConfigValueVO;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenPointGainConfigValueWithResultVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "esportingplus-resource-service", path = "chickenpointgainconfig", fallbackFactory = ChickenpointGainConfigClientFallbackFactory.class)
public interface ChickenpointGainConfigClient {

    /**
     * @param freeTeamTypeId 免费车队类型Id
     * @param gameDanId 游戏段位Id
     * @param gameResultCode 游戏结果Code
     * @param baojiLevel 游戏暴鸡身份Id
     * @return 配置值
     */
    @ApiOperation("通过免费车队类型Id查询")
    @GetMapping("findChickenpointGainConfig")
    ResponsePacket<Integer> findChickenpointGainConfigValue(
            @RequestParam("freeTeamTypeId") Integer freeTeamTypeId,
            @RequestParam("gameDanId") Integer gameDanId,
            @RequestParam("gameResultCode") Integer gameResultCode,
            @RequestParam("baojiLevel") Integer baojiLevel,
            @RequestParam("settlementTypeId") Integer settlementTypeId
    );


    /**
     * 计算车队可获取的鸡分数值
     */
    @ApiOperation("计算车队可获取的鸡分数值")
    @PostMapping("freeTeamType/{freeTeamTypeId}/baojiIdentity/{baojiLevel}/settlementType/{settlementTypeId}")
    ResponsePacket<Integer> calculateChickenpointGainValue(
            @PathVariable("freeTeamTypeId") Integer freeTeamTypeId,
            @PathVariable("baojiLevel") Integer baojiLevel,
            @PathVariable("settlementTypeId") Integer settlementTypeId,
            @RequestBody List<Integer> gameDanIds);

    @ApiOperation("游戏类型+结果+暴鸡等级 -> 游戏鸡分获取配置")
    @GetMapping("teamType/{teamTypeId}/gameResultCode/{gameResultCode}/baojiLevel/{baojiLevel}")
    ResponsePacket<List<ChickenPointGainConfigValueVO>> findDanConfigValues(
            @PathVariable("teamTypeId") Integer freeTeamTypeId,
            @PathVariable("gameResultCode") Integer gameResultCode,
            @PathVariable("baojiLevel") Integer baojiLevel
    );

    /**
     * 游戏类型  + 暴鸡等级 -> 游戏鸡分获取配置
     */
    @ApiOperation("游戏类型 + 暴鸡等级 -> 游戏鸡分获取配置")
    @GetMapping("teamType/{teamTypeId}/baojiLevel/{baojiLevel}")
    ResponsePacket<List<ChickenPointGainConfigValueWithResultVO>> getChickenConfigValueByTeamTypeAndBaojiLevel(
            @PathVariable("teamTypeId") Integer teamTypeId,
            @PathVariable("baojiLevel") Integer baojiLevel
    );
}
