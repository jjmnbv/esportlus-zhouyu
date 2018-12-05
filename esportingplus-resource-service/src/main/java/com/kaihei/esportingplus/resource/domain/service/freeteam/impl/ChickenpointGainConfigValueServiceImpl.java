package com.kaihei.esportingplus.resource.domain.service.freeteam.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.kaihei.esportingplus.api.params.freeteam.ChickenpointGainConfigValueUpdateParam;
import com.kaihei.esportingplus.resource.data.repository.teamtype.ChickenpointGainConfigRepository;
import com.kaihei.esportingplus.resource.data.repository.teamtype.ChickenpointGainConfigValueRepository;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.ChickenpointGainConfig;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.ChickenpointGainConfigValue;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ChickenpointGainConfigValueService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ChickenpointGainConfigValueServiceImpl implements ChickenpointGainConfigValueService {

    @Autowired
    private ChickenpointGainConfigValueRepository chickenpointGainConfigValueRepository;

    @Autowired
    private ChickenpointGainConfigRepository chickenpointGainConfigRepository;

    /**
     * 根据车队类型Id更新鸡分获取配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateChickenpointGainConfigValues(Integer freeTeamTypeId,
            ChickenpointGainConfigValueUpdateParam data) {
        
        //检查是否修改开启状态
        ChickenpointGainConfig chickenpointGainConfig = chickenpointGainConfigRepository
                .selectOne(ChickenpointGainConfig.builder().freeTeamTypeId(freeTeamTypeId).build());

        //需要修改开启状态再修改
        int update = 0;
        if (!data.getSuportGainChickenpoint()
                .equals(chickenpointGainConfig.getSuportGainChickenpoint())) {

            ChickenpointGainConfig config = ChickenpointGainConfig.builder()
                    .id(chickenpointGainConfig.getId())
                    .suportGainChickenpoint(data.getSuportGainChickenpoint()).build();

            update = chickenpointGainConfigRepository.updateByPrimaryKeySelective(config);
        }

        //全部配置
        List<ChickenpointGainConfigValue> chickenpointGainConfigValues = chickenpointGainConfigValueRepository
                .select(ChickenpointGainConfigValue.builder().freeTeamTypeId(freeTeamTypeId)
                        .build());

        //车队类型下的全部配置项
        Set<Integer> configValueIds = chickenpointGainConfigValues.parallelStream()
                .map(ChickenpointGainConfigValue::getId)
                .collect(Collectors.toSet());

        List<JSONObject> configData = data.getData();
        if (configData == null) {
            return update;
        }
        //映射成ConfigValueId -> ConfigValue
        Map<Integer, Integer> map = new HashMap<>();
        configData.parallelStream()
                .forEach(it -> it.keySet().stream().filter(k -> k.endsWith("_id"))
                        .forEach(k -> map.put(it.getInteger(k),
                                it.getInteger(k.substring(0, k.length() - 3)))));

        //过滤出该车队类型下存在的配置项
        Map<Integer, Integer> exitsConfigValues = Maps
                .filterEntries(map, it -> configValueIds.contains(it.getKey()));

        //更新配置
        List<ChickenpointGainConfigValue> configValues = exitsConfigValues.entrySet().stream()
                .map(e -> ChickenpointGainConfigValue.builder().id(e.getKey())
                        .value(e.getValue()).build()).collect(Collectors.toList());
        if (!configValues.isEmpty()) {
            update += chickenpointGainConfigValueRepository.batchUpdateValues(configValues);
        }

        return update;
    }
}
