package com.kaihei.esportingplus.resource.data.manager.impl;

import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.vo.BaojiLevelRateVo;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.ConfigBaojiService;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigBaojiServiceImpl implements ConfigBaojiService {

    @Autowired
    private DictionaryDictManager dictionaryDictManager;
    @Override
    public BigDecimal getBaojiLevelRate(Integer baojiLevel) {
        Dictionary dictionary = dictionaryDictManager
                .findByCodeAndCategoryCode(baojiLevel.toString(),
                        DictionaryCategoryCodeEnum.BAOJI_LEVEL.getCode());
        if(dictionary!=null && ObjectTools.isNotEmpty(dictionary.getChildDictionary())) {
           return dictionary.getChildDictionary().stream().filter(it->it.getDictionaryCategory()!=null &&ObjectTools.isNotEmpty(it.getValue()) && it.getDictionaryCategory().getCode().equals(DictionaryCategoryCodeEnum.BAOJI_LEVEL_RATE.getCode())).map(it->new BigDecimal(it.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP)).findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public List<BaojiLevelRateVo> getBaojiLevelRateBatch(List<Integer> params) {
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        List<BaojiLevelRateVo> list = new ArrayList<>();
        for (Integer baojiLevel : params) {
            BigDecimal baojiLevelRate = this.getBaojiLevelRate(baojiLevel);
            if (baojiLevelRate != null) {
                BaojiLevelRateVo vo = new BaojiLevelRateVo();
                vo.setBaojiLevel(baojiLevel);
                vo.setBaojiLevelRate(baojiLevelRate);
                list.add(vo);
            }
        }
        return list;
    }
}
