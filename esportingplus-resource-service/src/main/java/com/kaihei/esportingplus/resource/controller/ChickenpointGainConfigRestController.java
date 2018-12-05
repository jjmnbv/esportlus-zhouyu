package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.feign.ChickenpointGainConfigClient;
import com.kaihei.esportingplus.api.params.freeteam.ChickenpointGainConfigValueUpdateParam;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenPointGainConfigValueVO;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenPointGainConfigValueWithResultVO;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointGainConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.resource.data.repository.teamtype.ChickenpointGainConfigRepository;
import com.kaihei.esportingplus.resource.data.repository.teamtype.ChickenpointGainConfigValueRepository;
import com.kaihei.esportingplus.resource.data.repository.teamtype.TeamTypeRepository;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.ChickenpointGainConfig;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.ChickenpointGainConfigValue;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.TeamType;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ChickenpointGainConfigService;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ChickenpointGainConfigValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chickenpointgainconfig")
@Api("鸡分获取配置接口")
@Slf4j
public class ChickenpointGainConfigRestController implements ChickenpointGainConfigClient {

    @Autowired
    private ChickenpointGainConfigService chickenpointGainConfigService;

    @Autowired
    private ChickenpointGainConfigValueRepository chickenpointGainConfigValueRepository;

    @Autowired
    private ChickenpointGainConfigRepository chickenpointGainConfigRepository;

    @Autowired
    private TeamTypeRepository teamTypeRepository;
    @Autowired
    private ChickenpointGainConfigValueService chickenpointGainConfigValueService;


    @ApiOperation("下拉列表 -> 上分车队类型")
    @GetMapping("round/teamTypes")
    public ResponsePacket<?> getRoundTeamTypes() {
        return ResponsePacket.onSuccess(
                chickenpointGainConfigService.getRoundTeamTypes());
    }

    @ApiOperation("下拉列表 -> 上分车队类型")
    @GetMapping("hour/teamTypes")
    public ResponsePacket<?> getHourTeamTypes() {
        return ResponsePacket.onSuccess(
                chickenpointGainConfigService.getHourTeamTypes());
    }

    @ApiOperation("根据车队类型Id 获取鸡分获取配置")
    @GetMapping("teamType/hour/{teamTypeId}")
    public ResponsePacket<ChickenpointGainConfigVo> getHourChickenpointGainConfigs(
            @PathVariable("teamTypeId") Integer teamTypeId) {
        return ResponsePacket
                .onSuccess(
                        chickenpointGainConfigService.getHourChickenpointGainConfigs(teamTypeId));
    }

    @ApiOperation("根据车队类型Id 获取鸡分获取配置")
    @GetMapping("teamType/round/{teamTypeId}")
    public ResponsePacket<ChickenpointGainConfigVo> getRoundChickenpointGainConfigs(
            @PathVariable("teamTypeId") Integer teamTypeId) {
        return ResponsePacket
                .onSuccess(
                        chickenpointGainConfigService.getRoundChickenpointGainConfigs(teamTypeId));
    }

    @ApiOperation("配置获取鸡分值")
    @PutMapping("chickenpointGainConfigValue/teamType/{teamTypeId}")
    public ResponsePacket<Integer> updateChickenpointGainConfigValues(
            @PathVariable("teamTypeId") Integer teamTypeId,
            @RequestBody ChickenpointGainConfigValueUpdateParam configData) {

        return ResponsePacket.onSuccess(chickenpointGainConfigValueService
                .updateChickenpointGainConfigValues(teamTypeId, configData));
    }

    @Override
    public ResponsePacket<Integer> findChickenpointGainConfigValue(
            @RequestParam("freeTeamTypeId") Integer freeTeamTypeId,
            @RequestParam("gameDanId") Integer gameDanId,
            @RequestParam("gameResultCode") Integer gameResultCode,
            @RequestParam("baojiLevel") Integer baojiLevel,
            @RequestParam("settlementTypeId") Integer settlementTypeId) {
        //暴鸡Code 转 Id

        //Python结果转为字典的结果Id
        SettlementTypeEnum settlementTypeEnum = SettlementTypeEnum.getByCode(settlementTypeId);
        List<Integer> gameResults = GameResultEnum
                .findBySettlementType(settlementTypeEnum).stream().map(GameResultEnum::getCode)
                .collect(Collectors.toList());
        if (gameResults.contains(gameResultCode)) {
            return ResponsePacket
                    .onSuccess(0);
        }

        log.info("freeTeamTypeId: {} gameDanId: {} gameResultId:{} baojiIdentityId:{}",
                freeTeamTypeId, gameDanId, gameResultCode, baojiLevel);

        Integer value = Optional.ofNullable(
                chickenpointGainConfigValueRepository
                        .select(ChickenpointGainConfigValue.builder()
                                .freeTeamTypeId(freeTeamTypeId)
                                .gameDanId(gameDanId)
                                .gameResultId(gameResultCode)
                                .baojiIdentityId(baojiLevel)
                                .build())
        )
                .filter(it -> !it.isEmpty())
                .map(it -> it.get(0))
                .map(ChickenpointGainConfigValue::getValue)
                .orElse(0);

        return ResponsePacket
                .onSuccess(value);
    }


    /**
     * 计算车队可获取的鸡分数值
     */
    @Override
    @PostMapping("freeTeamType/{freeTeamTypeId}/baojiIdentity/{baojiLevel}/settlementType/{settlementTypeId}")
    public ResponsePacket<Integer> calculateChickenpointGainValue(
            @PathVariable("freeTeamTypeId") Integer freeTeamTypeId,
            @PathVariable("baojiLevel") Integer baojiLevel,
            @PathVariable("settlementTypeId") Integer settlementTypeId,
            @RequestBody List<Integer> gameDanIds) {

        SettlementTypeEnum settlementTypeEnum = SettlementTypeEnum.getByCode(settlementTypeId);

        //判断配置是否处于关闭状态
        ChickenpointGainConfig chickenpointGainConfig = chickenpointGainConfigRepository.selectOne(
                ChickenpointGainConfig.builder().freeTeamTypeId(freeTeamTypeId)
                        .suportGainChickenpoint(1).build());

        //关闭直接返回0
        if (chickenpointGainConfig == null) {
            return ResponsePacket.onSuccess(0);
        }

        TeamType teamType = teamTypeRepository.selectByPrimaryKey(freeTeamTypeId);
        //判断支持该结算类型
        CollectionUtils.find(teamType.getTeamSettlementModeList(),
                it -> it.getSettlementTypeId().equals(settlementTypeId))
                .orElseThrow(() -> new BusinessException(
                        BizExceptionEnum.INTERNAL_SERVER_ERROR));

        GameResultEnum positiveGameResult = GameResultEnum
                .findPositiveGameResult(settlementTypeEnum);

        log.info("positiveGameResult->{}", positiveGameResult);
        //免费车队类型 + 暴鸡身份 过滤配置
        List<ChickenpointGainConfigValue> configValues = chickenpointGainConfigValueRepository
                .selectPisitiveConfigValues(freeTeamTypeId, baojiLevel,
                        new HashSet<>(gameDanIds),
                        positiveGameResult.getCode());

        //过虑出 赢 或 打了 的配置 positiveResult
        //组成 Map < 段位Id -> 配置值 >
        Map<Integer, Integer> gameDanValueMap = configValues.stream()
                .collect(Collectors.toMap(ChickenpointGainConfigValue::getGameDanId,
                        ChickenpointGainConfigValue::getValue));
        //兼容、无需段位的车队类型
        gameDanValueMap
                .put(null,
                        configValues.stream().findFirst().map(ChickenpointGainConfigValue::getValue)
                                .orElse(0));

        int sum = gameDanIds.stream()
                .mapToInt(i -> gameDanValueMap.getOrDefault(i, 0))
                .sum();

        return ResponsePacket.onSuccess(sum);
    }

    /**
     * 根据freeTeamTypeId、gameResultCode、baojiIdentityLevel免费车队鸡分获取配置
     *
     * gameResultCode  是 {@link GameResultEnum} 的code
     */
    @Override
    @GetMapping("teamType/{teamTypeId}/gameResultCode/{gameResultCode}/baojiLevel/{baojiLevel}")
    public ResponsePacket<List<ChickenPointGainConfigValueVO>> findDanConfigValues(
            @PathVariable("teamTypeId") Integer freeTeamTypeId,
            @PathVariable("gameResultCode") Integer gameResultCode,
            @PathVariable("baojiLevel") Integer baojiLevel) {

        List<ChickenpointGainConfigValue> chickenpointGainConfigValues = chickenpointGainConfigValueRepository
                .select(ChickenpointGainConfigValue.builder()
                                .freeTeamTypeId(freeTeamTypeId)
                                .gameResultId(gameResultCode)
                        .baojiIdentityId(baojiLevel)
                                .build());

        return ResponsePacket.onSuccess(chickenpointGainConfigValues.stream()
                .map(it -> it.cast(ChickenPointGainConfigValueVO.class))
                .collect(Collectors.toList()));
    }

    /**
     * 游戏类型  + 暴鸡等级 -> 游戏鸡分获取配置
     */
    @Override
    @ApiOperation("游戏类型  + 暴鸡等级 -> 游戏鸡分获取配置")
    @GetMapping("teamType/{teamTypeId}/baojiLevel/{baojiLevel}")
    public ResponsePacket<List<ChickenPointGainConfigValueWithResultVO>> getChickenConfigValueByTeamTypeAndBaojiLevel(
            @PathVariable("teamTypeId") Integer teamTypeId,
            @PathVariable("baojiLevel") Integer baojiLevel) {
        List<ChickenpointGainConfigValue> chickenpointGainConfigValues = chickenpointGainConfigValueRepository
                .select(ChickenpointGainConfigValue.builder().freeTeamTypeId(teamTypeId)
                        .build());

        return ResponsePacket.onSuccess(chickenpointGainConfigValues.stream().map(it -> it.cast(
                ChickenPointGainConfigValueWithResultVO.class)).collect(Collectors.toList()));
    }
}
