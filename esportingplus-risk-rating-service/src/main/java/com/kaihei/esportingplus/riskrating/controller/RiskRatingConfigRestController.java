package com.kaihei.esportingplus.riskrating.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.RiskDictValueUpdateParams;
import com.kaihei.esportingplus.riskrating.domain.entity.AbstractEntity;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import com.kaihei.esportingplus.riskrating.repository.RiskDictRepository;
import com.kaihei.esportingplus.riskrating.service.RiskDictService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("config")
public class RiskRatingConfigRestController {

    @Autowired
    private RiskDictService riskDictService;

    @Autowired
    private RiskDictRepository riskDictRepository;

    @GetMapping
    public ResponsePacket<List<RiskDict>> findAll() {
        return ResponsePacket.onSuccess(riskDictRepository.findAll());
    }

    @GetMapping("group/{groupCode}/item/{itemCode}")
    public ResponsePacket<?> findByGroupCodeAndItemCode(@PathVariable String groupCode,
            @PathVariable String itemCode) {
        return ResponsePacket
                .onSuccess(riskDictService.findByGroupCodeAndItemCode(groupCode, itemCode));
    }

    @PutMapping
    public ResponsePacket<Integer> updateConfigValues(@RequestBody
            List<RiskDictValueUpdateParams> riskDictValueUpdateParams) {
        List<RiskDict> all = riskDictRepository
                .findByIdIn(riskDictValueUpdateParams.stream().map(RiskDictValueUpdateParams::getId)
                        .collect(
                                Collectors.toList()));
        Map<Long, RiskDict> riskDictMap = all.stream()
                .collect(Collectors.toMap(AbstractEntity::getId, e -> e));

        riskDictValueUpdateParams.parallelStream()
                .forEach(it -> it.cast(riskDictMap.get(it.getId())));
        riskDictRepository.save(all);
        return ResponsePacket.onSuccess(1);
    }

    @PutMapping("{id}")
    public ResponsePacket<Integer> updateConfigValue(@PathVariable Long id, @RequestBody
            RiskDictValueUpdateParams riskDictValueUpdateParams) {
        RiskDict cast = riskDictValueUpdateParams.cast(RiskDict.class);
        cast.setId(id);
        Integer update = riskDictService.updateById(cast);
        return ResponsePacket.onSuccess(update);
    }
}
