package com.kaihei.esportingplus.resource.domain.service.freeteam.impl;

import com.kaihei.esportingplus.api.enums.FreeTeamHomeCategoryCodeEnum;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamAdvertiseHomeVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamScrollTemplateHomeVo;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.HanyuPinyinUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryCategoryDictManager;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import com.kaihei.esportingplus.resource.domain.service.freeteam.FreeTeamHomeService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangfang
 */
@Service
public class FreeTeamHomeServiceImpl implements FreeTeamHomeService {

    @Autowired
    private DictionaryCategoryDictManager dictionaryCategoryDictManager;
    @Autowired
    private DictionaryDictManager dictionaryDictManager;


    @Override
    public List<FreeTeamAdvertiseHomeVo> findHomeAdvertiseList() {
        List<Dictionary> dictionaryList = dictionaryDictManager.findByCategoryCode(
                FreeTeamHomeCategoryCodeEnum.FREE_TEAM_HOME_ADVERTISE_IMAGE.getCode());
        return dictionaryList.stream().map(it->{
            FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo = JacksonUtils
                    .toBeanWithSnake(it.getValue(), FreeTeamAdvertiseHomeVo.class);
            freeTeamAdvertiseHomeVo.setAdvertiseId(it.getId());
            freeTeamAdvertiseHomeVo.setMachineType(Integer.valueOf(it.getCode()));
            return freeTeamAdvertiseHomeVo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFreeTeamAdvertiseHome(FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, freeTeamAdvertiseHomeVo);
        DictionaryCategory category = dictionaryCategoryDictManager
                .findByCode(FreeTeamHomeCategoryCodeEnum.FREE_TEAM_HOME_ADVERTISE_IMAGE.getCode());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, category);
        Dictionary dictionary = dictionaryDictManager
                .findByCodeAndCategoryCode(freeTeamAdvertiseHomeVo
                                .getMachineType().toString(),
                        FreeTeamHomeCategoryCodeEnum.FREE_TEAM_HOME_ADVERTISE_IMAGE.getCode());

        freeTeamAdvertiseHomeVo.setAdvertiseId(null);
        if (dictionary != null) {
            dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(freeTeamAdvertiseHomeVo));
            freeTeamAdvertiseHomeVo.setMachineType(null);
            dictionaryDictManager.updateById(dictionary);
        } else {
            dictionary = new Dictionary();
            dictionary.setCategoryId(category.getId());
            dictionary.setName("免费车队首页宣传图");
            dictionary.setStatus((byte) 1);
            dictionary.setCode(freeTeamAdvertiseHomeVo
                    .getMachineType().toString());
            freeTeamAdvertiseHomeVo.setMachineType(null);
            dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(freeTeamAdvertiseHomeVo));
            dictionaryDictManager.insert(dictionary);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFreeTeamAdvertiseHome(FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo) {
        Dictionary dictionary = dictionaryDictManager
                .findById(freeTeamAdvertiseHomeVo.getAdvertiseId());
        ValidateAssert.hasNotNull(BizExceptionEnum.FREE_TEAM_HOME_ADVERTISE_NOT_EXIST, dictionary);
        freeTeamAdvertiseHomeVo.setMachineType(null);
        freeTeamAdvertiseHomeVo.setAdvertiseId(null);
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(freeTeamAdvertiseHomeVo));
        dictionaryDictManager.updateById(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFreeTeamScrollTemplate(
            FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo) {
        DictionaryCategory category = dictionaryCategoryDictManager
                .findByCode(FreeTeamHomeCategoryCodeEnum.FREE_TEAM_HOME_SCROLL_WORDS_TEMPLATE
                        .getCode());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, category);
        freeTeamScrollTemplateHomeVo
                .setScrollTemplate("用户{0}" + freeTeamScrollTemplateHomeVo.getScrollTemplate());
        Dictionary dictionary = new Dictionary();
        dictionary.setCategoryId(category.getId());
        dictionary.setName(freeTeamScrollTemplateHomeVo.getScrollTemplate());
        dictionary.setCode(
                HanyuPinyinUtils.getFirstLettersLo(freeTeamScrollTemplateHomeVo.getScrollTemplate()));
        dictionary.setStatus((byte) 1);
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(freeTeamScrollTemplateHomeVo));
        dictionaryDictManager.insert(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFreeTeamScrollTemplate(
            FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo) {
        Dictionary dictionary = dictionaryDictManager
                .findById(freeTeamScrollTemplateHomeVo.getTemplateId());
        ValidateAssert
                .hasNotNull(BizExceptionEnum.FREE_TEAM_HOME_SCROLL_TEMPLATE_NOT_EXIST, dictionary);
        freeTeamScrollTemplateHomeVo
                .setScrollTemplate("用户{0}" + freeTeamScrollTemplateHomeVo.getScrollTemplate());
        freeTeamScrollTemplateHomeVo.setTemplateId(null);
        dictionary.setName(freeTeamScrollTemplateHomeVo.getScrollTemplate());
        dictionary.setCode(
                HanyuPinyinUtils.getFirstLettersLo(freeTeamScrollTemplateHomeVo.getScrollTemplate()));
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(freeTeamScrollTemplateHomeVo));
        dictionaryDictManager.updateById(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFreeTeamScrollTemplate(Integer id) {
        Dictionary dictionary = dictionaryDictManager
                .findById(id);
        ValidateAssert
                .hasNotNull(BizExceptionEnum.FREE_TEAM_HOME_SCROLL_TEMPLATE_NOT_EXIST, dictionary);
        dictionaryDictManager.deleteById(id);
    }

    @Override
    public FreeTeamAdvertiseHomeVo findAdvertise(Integer machineType) {
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, machineType);
        Dictionary dictionary = dictionaryDictManager.findByCodeAndCategoryCode(
                 machineType.toString(),
                FreeTeamHomeCategoryCodeEnum.FREE_TEAM_HOME_ADVERTISE_IMAGE.getCode());
        FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo = new FreeTeamAdvertiseHomeVo();
        if (ObjectTools.isNotEmpty(dictionary)) {
            freeTeamAdvertiseHomeVo = JacksonUtils
                    .toBeanWithSnake(dictionary.getValue(), FreeTeamAdvertiseHomeVo.class);
            freeTeamAdvertiseHomeVo.setAdvertiseId(dictionary.getId());
            freeTeamAdvertiseHomeVo.setMachineType(Integer.valueOf(dictionary.getCode()));

        }
        return freeTeamAdvertiseHomeVo;
    }

    private List<FreeTeamScrollTemplateHomeVo> findScorllWordsTemplateList() {
        List<Dictionary> dictionaryList = dictionaryDictManager.findByCategoryCode(
                FreeTeamHomeCategoryCodeEnum.FREE_TEAM_HOME_SCROLL_WORDS_TEMPLATE.getCode());
        return dictionaryList.stream().map(it -> {
            FreeTeamScrollTemplateHomeVo freeTeamAdvertiseHomeVo = JacksonUtils
                    .toBeanWithSnake(it.getValue(), FreeTeamScrollTemplateHomeVo.class);
            freeTeamAdvertiseHomeVo.setTemplateId(it.getId());
            return freeTeamAdvertiseHomeVo;
        }).collect(Collectors.toList());
    }
}
