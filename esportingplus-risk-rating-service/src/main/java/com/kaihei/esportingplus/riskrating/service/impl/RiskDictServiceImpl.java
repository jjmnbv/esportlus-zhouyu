package com.kaihei.esportingplus.riskrating.service.impl;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.riskrating.api.enums.EnableEnum;
import com.kaihei.esportingplus.riskrating.api.enums.RiskDictEnum;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import com.kaihei.esportingplus.riskrating.repository.RiskDictRepository;
import com.kaihei.esportingplus.riskrating.service.RiskDictService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *黑产防刷风控相关服务实现类
 *
 * @author 张方、谢思勇
 */
@Service
public class RiskDictServiceImpl implements RiskDictService {
    protected static final CacheManager cacheManager = CacheManagerFactory.create();

    private static final Logger logger = LoggerFactory.getLogger(RiskDictServiceImpl.class);
    @Autowired
    private RiskDictRepository riskDictRepository;

    //TODO 测试暂时直接查库
    @Override
    public void initDataToRedis() {
//        List<RiskDict> riskDictList = riskDictRepository.findAll();
//        if(ObjectTools.isEmpty(riskDictList)){
//            return;
//        }
//        Pipeline pipelined = cacheManager.pipelined();
//        for(RiskDict riskDict:riskDictList){
//            pipelined.hset(RedisKey.RISK_DICT_GROUP_KEY+riskDict.getGroupCode(),riskDict.getItemCode(),
//                    JacksonUtils.toJson(riskDict));
//        }
//        pipelined.sync();
    }

    //TODO 测试：暂时直接查库
    @Override
    public RiskDict findByGroupCodeAndItemCode(String groupCode,String itemCode){
        return riskDictRepository.findByGroupCodeAndItemCode(groupCode, itemCode);
//        RiskDict riskDict = cacheManager
//                .hget(RedisKey.RISK_DICT_GROUP_KEY + groupCode, itemCode, RiskDict.class);
//        if(riskDict==null){
//            riskDict =riskDictRepository.findByGroupCodeAndItemCode(groupCode,itemCode);
//            if(riskDict!=null){
//                cacheManager.hset(RedisKey.RISK_DICT_GROUP_KEY + groupCode, itemCode,JacksonUtils.toJson(riskDict));
//            }
//        }
//        return riskDict;
    }

    //TODO 测试：暂时直接查库
    @Override
    public List<RiskDict> findByGroupCode(String groupCode){
        return riskDictRepository.findByGroupCode(groupCode);
//        Map<String, String> map = cacheManager
//                .hgetAll(RedisKey.RISK_DICT_GROUP_KEY + groupCode, String.class);
//        List<RiskDict> riskDicts = new ArrayList<>();
//        if(ObjectTools.isEmpty(map)){
//            riskDicts = riskDictRepository.findByGroupCode(groupCode);
//            if(ObjectTools.isNotEmpty(riskDicts)){
//                Pipeline pipelined = cacheManager.pipelined();
//                for(RiskDict riskDict:riskDicts){
//                    pipelined.hset(RedisKey.RISK_DICT_GROUP_KEY+riskDict.getGroupCode(),riskDict.getItemCode(),
//                            JacksonUtils.toJson(riskDict));
//                }
//                pipelined.sync();
//            }
//            return riskDicts;
//        }
//        return map.values().stream().map(it->JacksonUtils.toBean(it,RiskDict.class)).collect(Collectors.toList());
    }

    @Override
    public Integer updateById(RiskDict riskDict) {
        Optional.of(riskDict)
                .map(RiskDict::getItemValue)
                .ifPresent(rd -> {
                    RiskDict target = riskDictRepository.findOne(riskDict.getId());
                    if (!target.getItemValue().equals(rd)) {
                        return;
                    }
                    target.setItemValue(riskDict.getItemValue());
                    riskDictRepository.save(target);
                });
        return 1;
    }

    @Override
    public boolean checkRiskSwitchStatus() {
        RiskDict riskDict = this
                .findByGroupCodeAndItemCode(RiskDictEnum.RISK_SWITCH.getGroupCode(),
                        RiskDictEnum.RISK_SWITCH.getItemCode());
        ValidateAssert.hasNotNull(BizExceptionEnum.RISK_DICT_NOT_INIT,riskDict);

        return EnableEnum.ENABLE.getCode().toString().equals(riskDict.getItemValue());
    }
}
