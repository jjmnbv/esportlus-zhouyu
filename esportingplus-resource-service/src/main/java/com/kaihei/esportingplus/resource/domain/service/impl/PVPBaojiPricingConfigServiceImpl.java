package com.kaihei.esportingplus.resource.domain.service.impl;

import com.kaihei.esportingplus.api.params.BaojiPricingConfigParams;
import com.kaihei.esportingplus.api.vo.PVPBaojiGameDanIncomeVO;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.data.repository.PVPBaojiPricingConfigRepository;
import com.kaihei.esportingplus.resource.domain.entity.PVPBaojiPricingConfig;
import com.kaihei.esportingplus.resource.domain.service.PVPBaojiPricingConfigService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liangyi
 */
@Service("pvpBaojiPricingConfigService")
public class PVPBaojiPricingConfigServiceImpl implements PVPBaojiPricingConfigService {

    @Autowired
    DictionaryDictManager dictionaryDictManager;
    @Autowired
    PVPBaojiPricingConfigRepository pvpBaojiPricingConfigRepository;

    @Override
    public List<PVPBaojiGameDanIncomeVO> getBaojiGameDanIncome(
            BaojiPricingConfigParams baojiPricingConfigParams) {

        List<PVPBaojiPricingConfig> pvpBaojiPricingConfigList = pvpBaojiPricingConfigRepository
                .selectBaojiGameDanIncome(
                        baojiPricingConfigParams.getGameId(),
                        baojiPricingConfigParams.getBossGameDanIdList(),
                        baojiPricingConfigParams.getBaojiLevelCodeList(),
                        baojiPricingConfigParams.getPricingType(),
                        StatusEnum.ENABLE.getCode());

        return pvpBaojiPricingConfigList.stream()
                .map(bc -> BeanMapper.map(bc, PVPBaojiGameDanIncomeVO.class))
                .sorted(Comparator.comparingInt(PVPBaojiGameDanIncomeVO::getBaojiLevelCode))
                .collect(Collectors.toList());
    }
}
