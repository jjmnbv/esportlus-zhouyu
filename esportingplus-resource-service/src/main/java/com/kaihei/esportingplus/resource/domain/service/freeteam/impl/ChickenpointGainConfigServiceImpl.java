package com.kaihei.esportingplus.resource.domain.service.freeteam.impl;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.api.vo.SettlementModeVO;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointGainConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.common.enums.BaojiLevelEnum;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.common.enums.SettlementTypeEnum;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.tools.HanyuPinyinUtils;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.data.repository.teamtype.ChickenpointGainConfigRepository;
import com.kaihei.esportingplus.resource.data.repository.teamtype.ChickenpointGainConfigValueRepository;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.ChickenpointGainConfig;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.ChickenpointGainConfigValue;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ChickenpointGainConfigService;
import com.kaihei.esportingplus.resource.domain.service.freeteam.FreeTeamTypeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Slf4j
@Service
public class ChickenpointGainConfigServiceImpl implements
        ChickenpointGainConfigService {

    @Autowired
    private ChickenpointGainConfigRepository chickenpointGainConfigRepository;

    @Autowired
    private ChickenpointGainConfigValueRepository chickenpointGainConfigValueRepository;
    /**
     * 添加鸡分获取配置
     */
    private ChickenpointGainConfig initChickenpointGainConfig(Integer freeTeamTypeId) {
        ChickenpointGainConfig chickenpointGainConfig = ChickenpointGainConfig.builder()
                .freeTeamTypeId(freeTeamTypeId)
                .suportGainChickenpoint(1)
                .build();
        chickenpointGainConfigRepository.insertSelective(chickenpointGainConfig);
        return chickenpointGainConfig;
    }

    @Autowired
    private FreeTeamTypeService freeTeamTypeService;

    @Autowired
    private DictionaryDictManager dictionaryDictManager;

    /**
     * 初始化配置值
     *
     * 动态根据车队类型对缺少的配置补足及对多余的配置删除
     */
    private List<ChickenpointGainConfigValue> initChickenpointGainConfigValue(
            FreeTeamTypeDetailVO teamTypeDetailVO, SettlementTypeEnum settlementTypeEnum) {
        //生成一份新配置
        List<ChickenpointGainConfigValue> chickenpointGainConfigValues = createChickenpointGainConfigValues(
                teamTypeDetailVO, settlementTypeEnum.getCode());

        List<Integer> gameResultIds = GameResultEnum.findBySettlementType(settlementTypeEnum)
                .stream()
                .map(GameResultEnum::getCode).collect(
                        Collectors.toList());

        //数据库已有的配置
        List<ChickenpointGainConfigValue> databaseConfigValues = chickenpointGainConfigValueRepository
                .selectByExample(
                        Example.builder(ChickenpointGainConfigValue.class).where(
                                Sqls.custom().andEqualTo("freeTeamTypeId",
                                        teamTypeDetailVO.getFreeTeamTypeId())
                                        .andIn("gameResultId", gameResultIds)).build());

        //需要新增的配置
        List<ChickenpointGainConfigValue> toInsert = chickenpointGainConfigValues.stream()
                .filter(it -> !databaseConfigValues.contains(it))
                .peek(chickenpointGainConfigValueRepository::insertSelective)
                .collect(Collectors.toList());
        //需要被删除的
        List<ChickenpointGainConfigValue> toDelete = databaseConfigValues.stream()
                .filter(it -> !chickenpointGainConfigValues.contains(it))
                .peek(it -> chickenpointGainConfigValueRepository.deleteByPrimaryKey(it.getId()))
                .collect(Collectors.toList());

        databaseConfigValues.removeAll(toDelete);
        databaseConfigValues.addAll(toInsert);

        return databaseConfigValues;
    }

    /**
     * 迭代叶字典
     */
    private Stream<Dictionary> leafStream(Dictionary dictionary) {
        List<Dictionary> dictionaryList = Optional.ofNullable(dictionary)
                .map(Dictionary::getChildDictionary)
                .map(List::stream)
                .orElse(Stream.of(dictionary)).collect(Collectors.toList());

        if (dictionaryList.stream().allMatch(d -> Objects.isNull(d.getChildDictionary()))) {
            return dictionaryList.stream();
        }

        Builder<Dictionary> builder = Stream.builder();

        dictionaryList.forEach(d -> leafStream(d).forEach(builder::add));
        return builder.build();
    }

    /**
     * 通过游戏字典Id获取游戏全部段位
     */
    private List<Dictionary> getGameDansDict(Integer gameId) {
        //通过游戏Id 获取游戏所有段位
        Dictionary gameDict = dictionaryDictManager.findById(gameId);
        Stream<Dictionary> danDictStream = leafStream(gameDict);
        //段位列表
        return danDictStream
                .filter(it -> Objects.nonNull(it.getDictionaryCategory()))
                .filter(it -> "game_dan".equals(it.getDictionaryCategory().getCode()))
                .collect(Collectors.toList());
    }

    /**
     * 根据游戏类型 初始化一个配置列表
     */
    private List<ChickenpointGainConfigValue> createChickenpointGainConfigValues(
            FreeTeamTypeDetailVO teamTypeDetailVO, Integer settlementTypeCode) {
        //游戏Id
        Integer gameId = teamTypeDetailVO.getGame().getDictId();

        List<Integer> settlementTypeCodes = teamTypeDetailVO.getSettlementModeList().stream()
                .map(SettlementModeVO::getSettlementType)
                .collect(Collectors.toList());

        //不包含的类型直接返回
        if (!settlementTypeCodes.contains(settlementTypeCode)) {
            return new ArrayList<>();
        }

        //暴鸡身份Id
        Integer baojiIdentityCode = teamTypeDetailVO.getBaojiIdentity();
        UserIdentityEnum userIdentityEnum = UserIdentityEnum.of(baojiIdentityCode);
        List<Integer> baojiLevels = userIdentityEnum.getBaojiLevelEnumList().stream()
                .map(BaojiLevelEnum::getCode).collect(
                        Collectors.toList());

        //通过游戏Id 获取游戏所有段位
        List<Dictionary> gameDans = getGameDansDict(gameId);

        //通过游戏类型 获取游戏全部结果
        SettlementTypeEnum settlementTypeEnum = SettlementTypeEnum.getByCode(settlementTypeCode);

        List<Integer> gameResultCodelist = GameResultEnum
                .findBySettlementType(settlementTypeEnum).stream().map(GameResultEnum::getCode)
                .collect(Collectors.toList());

        //初始化配置项
        ArrayList<ChickenpointGainConfigValue> chickenpointGainConfigValues = new ArrayList<>();
        gameDans.forEach(
                gameDan -> gameResultCodelist.forEach(
                        gameResult -> baojiLevels.forEach(
                                baojiLevel -> {
                                    //配置项
                                    ChickenpointGainConfigValue chickenpointGainConfigValue = ChickenpointGainConfigValue
                                            .builder()
                                            .freeTeamTypeId(
                                                    teamTypeDetailVO.getFreeTeamTypeId())
                                            .gameDanId(gameDan.getId())
                                            .gameResultId(gameResult)
                                            .baojiIdentityId(baojiLevel)
                                            .value(0)
                                            .build();
                                    chickenpointGainConfigValues
                                            .add(chickenpointGainConfigValue);
                                })));
        return chickenpointGainConfigValues;
    }


    /**
     * 根据游戏类型Id 获取游戏鸡分获取配置
     */
    @Override
    public ChickenpointGainConfigVo getChickenpointGainConfig(Integer freeTeamTypeId,
            SettlementTypeEnum settlementTypeEnum) {
        //游戏类型 验证合法
        FreeTeamTypeDetailVO teamTypeDetailVO = freeTeamTypeService
                .getByFreeTeamTypeId(freeTeamTypeId);
        if (teamTypeDetailVO == null) {
            return null;
        }

        //配置项
        ChickenpointGainConfig query = ChickenpointGainConfig.builder()
                .freeTeamTypeId(freeTeamTypeId).build();
        //没有就初始化、再查一次
        ChickenpointGainConfig chickenpointGainConfig = Optional
                .ofNullable(chickenpointGainConfigRepository
                        .selectOne(query))
                .orElseGet(() -> initChickenpointGainConfig(freeTeamTypeId));
        //转成VO
        ChickenpointGainConfigVo chickenpointGainConfigVo = chickenpointGainConfig
                .cast(ChickenpointGainConfigVo.class);

        //初始化配置项值
        List<ChickenpointGainConfigValue> configValues = initChickenpointGainConfigValue(
                teamTypeDetailVO, settlementTypeEnum);

        //配置
        Map<Integer, List<ChickenpointGainConfigValue>> gameDanConfigGroup = configValues.stream()
                .collect(Collectors.groupingBy(
                        ChickenpointGainConfigValue::getGameDanId));

        List<Map<String, Object>> data = gameDanConfigGroup.entrySet().stream()
                .map(entry -> {
                    Integer k = entry.getKey();
                    List<ChickenpointGainConfigValue> v = entry.getValue();

                    //游戏段位
                    Dictionary gameDan = dictionaryDictManager.findById(k);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("dans", gameDan.getName());

                    v.parallelStream().forEachOrdered(it -> {
                        //游戏结果
                        Integer gameResultId = it.getGameResultId();
                        GameResultEnum gameResultEnum = GameResultEnum.fromCode(gameResultId);


                        //游戏段位
                        Integer baojiIdentityId = it.getBaojiIdentityId();
                        String desc = BaojiLevelEnum.getByCode(baojiIdentityId).getDesc();
                        desc = desc.substring(desc.length() - 2);

                        //拼接前端要的Key
                        String key = String.format("%s_%s_%s",
                                gameResultEnum.getDictCode(),
                                HanyuPinyinUtils
                                        .toHanyuPinyin(desc), baojiIdentityId
                        );
                        //设值
                        jsonObject.put(key, it.getValue());
                        jsonObject.put(key + "_id", it.getId());
                    });
            return jsonObject;
        }).collect(Collectors.toList());

        chickenpointGainConfigVo.setData(data);
        return chickenpointGainConfigVo;
    }

    /**
     * 获取按局结算车队类型
     */
    @Override
    public List<ChickenpointVO> getRoundTeamTypes() {
        return getTeamTypesbySettlementEnum(SettlementTypeEnum.ROUND);
    }

    /**
     * 获取按小时结算车队类型
     */
    @Override
    public List<ChickenpointVO> getHourTeamTypes() {
        return getTeamTypesbySettlementEnum(SettlementTypeEnum.HOUR);
    }

    private List<ChickenpointVO> getTeamTypesbySettlementEnum(
            SettlementTypeEnum settlementTypeEnum) {
        List<FreeTeamTypeDetailVO> allEnableFreeTeamType = freeTeamTypeService
                .getAllEnableFreeTeamType();

        return allEnableFreeTeamType.stream().filter(it -> {
            List<SettlementModeVO> settlementModeList = it.getSettlementModeList();
            List<Integer> settleTypeCodes = settlementModeList.stream()
                    .map(SettlementModeVO::getSettlementType)
                    .collect(Collectors.toList());
            return settleTypeCodes.contains(settlementTypeEnum.getCode());
        }).map(it ->
                ChickenpointVO.builder().freeTeamTypeId(it.getFreeTeamTypeId())
                        .name(it.getName())
                        .build()
        ).collect(Collectors.toList());
    }
    /**
     * 获取按小时结算车队类型 的配置
     */
    @Override
    public ChickenpointGainConfigVo getHourChickenpointGainConfigs(Integer teamTypeId) {
        return getChickenpointGainConfig(teamTypeId, SettlementTypeEnum.HOUR);
    }

    /**
     * 获取按局结算车队类型 的配置
     */
    @Override
    public ChickenpointGainConfigVo getRoundChickenpointGainConfigs(Integer teamTypeId) {
        return getChickenpointGainConfig(teamTypeId, SettlementTypeEnum.ROUND);
    }
}
