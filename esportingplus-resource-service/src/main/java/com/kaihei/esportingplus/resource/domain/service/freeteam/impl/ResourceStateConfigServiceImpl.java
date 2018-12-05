package com.kaihei.esportingplus.resource.domain.service.freeteam.impl;

import com.kaihei.esportingplus.api.enums.ResourceStatePositionEnum;
import com.kaihei.esportingplus.api.enums.ResourceStatePositionEnum;
import com.kaihei.esportingplus.api.enums.ResourceStatePositionEnum;
import com.kaihei.esportingplus.api.enums.ResourceUserTypeEnum;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ResourceStateConfigVo;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.common.tools.HanyuPinyinUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryCategoryDictManager;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ResourceStateConfigService;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资源位管理
 * @author zhangfang
 */
@Service
public class ResourceStateConfigServiceImpl implements ResourceStateConfigService {
    @Autowired
    private DictionaryDictManager dictionaryDictManager;
    @Autowired
    private DictionaryCategoryDictManager dictionaryCategoryDictManager;
    /**
     * 获取资源位
     * @param pagingRequest
     * @return
     */
    @Override
    public List<ResourceStateConfigVo> findResourceStateConfig(Integer userType, String position){
        //查询出所有符合条件的banner
        List<ResourceStateConfigVo> configVos = this.findAllConfigVOsByCondition(userType, position);
        this.sortResourceStateVos(configVos);
        return configVos;
    }

    private List<ResourceStateConfigVo> findAllConfigVOsByCondition(Integer userType, String position) {
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                ResourceStatePositionEnum.getByCode(position));
        //1：先从位置分类查询数据字典
        List<Dictionary> dictionaryList = dictionaryDictManager.findByCategoryCode(position);
        //2:再转回成对应的bannerVO对象
        List<ResourceStateConfigVo> resourceStateConfigVos = dictionaryList.stream()
                .map(it -> convertToBannerConfigVo(it)).collect(Collectors.toList());
        //再过滤对应的用户类型，注意的时候全量用户对所有用户都生效
        if(userType!=null){
            resourceStateConfigVos = resourceStateConfigVos.stream()
                    .filter(it -> ResourceUserTypeEnum.ALL.getCode().equals(it.getUserType())
                            || userType.equals(it.getUserType())).collect(Collectors.toList());
        }
        return resourceStateConfigVos;
    }

    private void sortResourceStateVos(List<ResourceStateConfigVo> configVos) {
        Collections.sort(configVos, new Comparator<ResourceStateConfigVo>() {
            @Override
            public int compare(ResourceStateConfigVo o1, ResourceStateConfigVo o2) {
                return o2.getOrderIndex()-o1.getOrderIndex();
            }
        });
    }

    private ResourceStateConfigVo convertToBannerConfigVo(Dictionary dictionary) {
        ResourceStateConfigVo configVo = JacksonUtils
                .toBeanWithSnake(dictionary.getValue(), ResourceStateConfigVo.class);
        configVo.setUserTypeDesc(ResourceUserTypeEnum.getByCode(configVo.getUserType()).getMsg());
        configVo.setResourceId(dictionary.getId());
        configVo.setPositionZh(ResourceStatePositionEnum.getByCode(configVo.getPosition()).getDesc());
        return configVo;
    }


    /**
     * 获取一个资源位
     * @return
     */
    @Override
    public ResourceStateConfigVo findResourceStateConfigById(Integer resourceId){
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, resourceId);
        Dictionary dictionary = dictionaryDictManager.findById(resourceId);
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_NOT_FOUND, dictionary);

        return convertToBannerConfigVo(dictionary);
    }

    /**
     * 添加资源位
     * @param resourceStateSaveParams
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveResourceStateConfig(ResourceStateSaveParams resourceStateSaveParams){
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, resourceStateSaveParams);
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                ResourceStatePositionEnum.getByCode(resourceStateSaveParams.getPosition()));
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                ResourceUserTypeEnum.getByCode(resourceStateSaveParams.getUserType()));
        DictionaryCategory category = dictionaryCategoryDictManager
                .findByCode(resourceStateSaveParams.getPosition());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, category);
        //构造数据字典，添加
        Dictionary dictionary = new Dictionary();
        dictionary.setParentId(0);
        dictionary.setOrderIndex(resourceStateSaveParams.getOrderIndex());
        dictionary.setCategoryId(category.getId());
        dictionary.setName(resourceStateSaveParams.getName());
        dictionary.setCode(HanyuPinyinUtils.getFirstLettersLo(resourceStateSaveParams.getName()));
        dictionary.setStatus((byte) StatusEnum.ENABLE.getCode());
        dictionary.setGmtCreate(new Date());
        dictionary.setGmtModified(new Date());
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(resourceStateSaveParams));
        dictionaryDictManager.insert(dictionary);
    }
    /**
     * 修改资源位
     * @param resourceStateUpdateParams
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResourceStateConfig(ResourceStateUpdateParams resourceStateUpdateParams){
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, resourceStateUpdateParams);
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                ResourceStatePositionEnum.getByCode(resourceStateUpdateParams.getPosition()));
        Dictionary dictionary = dictionaryDictManager.findById(resourceStateUpdateParams.getResourceId());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_NOT_FOUND, dictionary);
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                ResourceStatePositionEnum.getByCode(dictionary.getDictionaryCategory().getCode()));
        DictionaryCategory category = dictionaryCategoryDictManager
                .findByCode(resourceStateUpdateParams.getPosition());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, category);
        dictionary.setOrderIndex(resourceStateUpdateParams.getOrderIndex());
        dictionary.setGmtModified(new Date());
        dictionary.setCategoryId(category.getId());
        dictionary.setOrderIndex(resourceStateUpdateParams.getOrderIndex());
        dictionary.setName(resourceStateUpdateParams.getName());
        dictionary.setCode(HanyuPinyinUtils.getFirstLettersLo(resourceStateUpdateParams.getName()));
        dictionary.setStatus((byte) StatusEnum.ENABLE.getCode());
        resourceStateUpdateParams.setResourceId(null);
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(resourceStateUpdateParams));
        dictionaryDictManager.updateById(dictionary);
    }

    /**
     * 删除资源位
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteResourceStateConfig(Integer id){
        dictionaryDictManager.deleteById(id);
    }
}
