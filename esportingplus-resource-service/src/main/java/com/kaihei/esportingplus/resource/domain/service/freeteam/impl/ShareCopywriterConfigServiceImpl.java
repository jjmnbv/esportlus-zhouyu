package com.kaihei.esportingplus.resource.domain.service.freeteam.impl;

import com.kaihei.esportingplus.api.enums.ShareCopywriterScenEnum;
import com.kaihei.esportingplus.api.params.freeteam.ShareCopywriterConfigParams;
import com.kaihei.esportingplus.api.vo.freeteam.ShareCopywriterConfigVO;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.HanyuPinyinUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryCategoryDictManager;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ShareCopywriterConfigService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareCopywriterConfigServiceImpl implements ShareCopywriterConfigService {

    @Autowired
    private DictionaryCategoryDictManager dictionaryCategoryDictManager;
    @Autowired
    private DictionaryDictManager dictionaryDictManager;

    @Override
    public List<ShareCopywriterConfigVO> findShareCopywriterConfig(String scene) {
        ShareCopywriterScenEnum scenEnum = ShareCopywriterScenEnum.getByCode(scene);
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, scenEnum);
        List<Dictionary> dictionaryList = dictionaryDictManager
                .findByCategoryCode(scene);
        if (ObjectTools.isEmpty(dictionaryList)) {
            return new ArrayList<>();
        }
        return dictionaryList.stream().map(it -> convertToThirdShareConfigVOWithCount(it))
                .collect(Collectors.toList());
    }

    private ShareCopywriterConfigVO convertToThirdShareConfigVO(Dictionary it) {
        ShareCopywriterConfigVO shareCopywriterConfigVO = JacksonUtils
                .toBeanWithSnake(it.getValue(), ShareCopywriterConfigVO.class);
        shareCopywriterConfigVO.setShareId(it.getId());
        shareCopywriterConfigVO.setScene(it.getDictionaryCategory().getCode());
        shareCopywriterConfigVO.setStatus(it.getStatus().intValue());
        shareCopywriterConfigVO.setGmtCreate(it.getGmtCreate());
        return shareCopywriterConfigVO;
    }

    private ShareCopywriterConfigVO convertToThirdShareConfigVOWithCount(Dictionary it) {
        ShareCopywriterConfigVO thirdShareConfigVO = this.convertToThirdShareConfigVO(it);
        //TODO  分享次数和点击次数，需要调用第三方接口
        return thirdShareConfigVO;
    }

    @Override
    public void saveShareCopywriterConfig(ShareCopywriterConfigParams params) {
        ShareCopywriterScenEnum scenEnum = ShareCopywriterScenEnum.getByCode(params.getScene());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, scenEnum);
        DictionaryCategory dictionaryCategory = dictionaryCategoryDictManager
                .findByCode(params.getScene());
        ValidateAssert
                .hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, dictionaryCategory);
        Dictionary dictionary = new Dictionary();
        dictionary.setParentId(0);
        dictionary.setName(params.getShareTitle());
        dictionary.setCode(HanyuPinyinUtils.getFirstLettersLo(params.getShareTitle()));
        dictionary.setGmtCreate(new Date());
        dictionary.setGmtModified(new Date());
        dictionary.setOrderIndex(0);
        dictionary.setCategoryId(dictionaryCategory.getId());
        dictionary.setStatus(params.getStatus().byteValue());
        params.setShareId(null);
        params.setStatus(null);
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(params));
        dictionaryDictManager.insert(dictionary);
    }

    @Override
    public void updateShareCopywriterConfig(ShareCopywriterConfigParams params) {
        ShareCopywriterScenEnum scenEnum = ShareCopywriterScenEnum.getByCode(params.getScene());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, scenEnum);
        Dictionary dictionary = dictionaryDictManager.findById(params.getShareId());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_NOT_FOUND, dictionary);
        //不是分享任务不允许编辑
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT,
                ShareCopywriterScenEnum.getByCode(dictionary.getDictionaryCategory().getCode()));
        //重新设置对应的Category
        DictionaryCategory category = dictionaryCategoryDictManager
                .findByCode(params.getScene());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, category);
        params.setShareId(null);
        dictionary.setName(params.getShareTitle());
        dictionary.setCode(HanyuPinyinUtils.getFirstLettersLo(params.getShareTitle()));
        dictionary.setStatus(params.getStatus().byteValue());
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(params));
        dictionary.setCategoryId(category.getId());
        dictionaryDictManager.updateById(dictionary);
    }

    @Override
    public ShareCopywriterConfigVO findById(Integer shareId) {
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, shareId);
        Dictionary dictionary = dictionaryDictManager.findById(shareId);
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_NOT_FOUND, dictionary);
        return convertToThirdShareConfigVO(dictionary);
    }
}
