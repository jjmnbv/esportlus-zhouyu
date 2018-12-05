package com.kaihei.esportingplus.resource.domain.service.freeteam.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.api.enums.BannerCodeEnum;
import com.kaihei.esportingplus.api.enums.BannerPositionEnum;
import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.enums.ResourceUserTypeEnum;
import com.kaihei.esportingplus.api.enums.UserTypeEnum;
import com.kaihei.esportingplus.api.params.freeteam.BannerSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.BannerUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerDictConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.HanyuPinyinUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryCategoryDictManager;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.data.repository.BannerConfigRepository;
import com.kaihei.esportingplus.resource.domain.entity.BannerConfig;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import com.kaihei.esportingplus.resource.domain.service.freeteam.BannerConfigService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BannerConfigServiceImpl implements BannerConfigService {

    @Autowired
    private DictionaryDictManager dictionaryDictManager;
    @Autowired
    private DictionaryCategoryDictManager dictionaryCategoryDictManager;

    @Override
    public PagingResponse<BannerConfigVo> findBannerConfig(Integer userType, String position,
            PagingRequest pagingRequest) {
        //查询出所有符合条件的banner
        List<BannerConfigVo> configVos = this.findAllConfigVOsByCondition(userType, position);
        //进行排序，
        this.sortBannerList(configVos);
        int start = (pagingRequest.getOffset() - 1) * pagingRequest.getLimit();
        int end = pagingRequest.getOffset() * pagingRequest.getLimit();
        if (configVos.size() < end) {
            end = configVos.size();
        }
        List<BannerConfigVo> pageConfigVos =
                configVos.size() <= start ? new ArrayList<BannerConfigVo>()
                        : configVos.subList(start, end);
        return new PagingResponse<>(pagingRequest.getOffset(), pagingRequest.getLimit(),
                configVos.size(), pageConfigVos);
    }

    private List<BannerConfigVo> findAllConfigVOsByCondition(Integer userType, String position) {
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_POSITION_ERROR,
                BannerPositionEnum.getByCode(position));
        //1：先从位置分类查询数据字典
        List<Dictionary> dictionaryList = dictionaryDictManager.findByCategoryCode(position);
        //2:再转回成对应的bannerVO对象
        List<BannerConfigVo> bannerConfigVos = dictionaryList.stream()
                .map(it -> convertToBannerConfigVo(it)).collect(Collectors.toList());
        //再过滤对应的用户类型，注意的时候全量用户对所有用户都生效
        if(userType!=null){
            bannerConfigVos = bannerConfigVos.stream()
                    .filter(it -> ResourceUserTypeEnum.ALL.getCode().equals(it.getUserType())
                            || userType.equals(it.getUserType())).collect(Collectors.toList());
        }

        return bannerConfigVos;
    }

    /**
     * 根据非冻结再前，其次权重高在前的原则进行排序
     */
    private void sortBannerList(List<BannerConfigVo> configVoList) {
        Date date = new Date();
        Collections.sort(configVoList, new Comparator<BannerConfigVo>() {
            @Override
            public int compare(BannerConfigVo o1, BannerConfigVo o2) {
                //先计算o1和o2是否冻结 ，冻结置位1，不冻结置位0
                int o1FreezedStarus = o1.getIsFreezed() == 0 ?
                        (date.compareTo(o1.getStartTime()) >= 0
                                && date.compareTo(o1.getEndTime()) < 0) ? 0 : 1 : 1;
                int o2FreezedStarus = o2.getIsFreezed() == 0 ?
                        (date.compareTo(o2.getStartTime()) >= 0
                                && date.compareTo(o2.getEndTime()) < 0) ? 0 : 1 : 1;
                int i = o2FreezedStarus - o1FreezedStarus;
                return i > 0 ? -1 : i < 0 ? 1 : o2.getOrderIndex() - o1.getOrderIndex();
            }
        });
    }

    @Override
    public List<BannerConfigVo> findCarouselBannerConfig(Integer userType, String position) {
        BannerPositionEnum bannerPositionEnum = BannerPositionEnum.getByCode(position);
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userType);
        //1: 查询出轮播配置数量
        Integer carouseCount = this.getBannerCarouseCount();
        //2:查询出所有符合条件的banner
        List<BannerConfigVo> configVos = this.findAllConfigVOsByCondition(userType, position);
        //3:只需要非冻结的元素
        Date currentDate = new Date();
        List<BannerConfigVo> notFreezedBanners = configVos.stream()
                .filter(it -> it.getIsFreezed() == 0 && (
                        currentDate.compareTo(it.getStartTime()) >= 0
                                && currentDate.compareTo(it.getEndTime()) < 0))
                .collect(Collectors.toList());
        if (ObjectTools.isEmpty(notFreezedBanners)) {
            return new ArrayList<>();
        }
        this.sortBannerList(notFreezedBanners);
        if (notFreezedBanners.size() < carouseCount) {
            carouseCount = notFreezedBanners.size();
        }
        return notFreezedBanners.subList(0, carouseCount);
    }

    @Override
    public BannerConfigVo findBannerConfigById(Integer bannerId) {
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL, bannerId);
        Dictionary dictionary = dictionaryDictManager.findById(bannerId);
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_NOT_FOUND, dictionary);

        return convertToBannerConfigVo(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBannerConfig(BannerSaveParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_POSITION_ERROR,
                BannerPositionEnum.getByCode(params.getPosition()));
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_USER_TYPE_ERROR,
                ResourceUserTypeEnum.getByCode(params.getUserType()));
        DictionaryCategory category = dictionaryCategoryDictManager
                .findByCode(params.getPosition());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, category);
        //构造数据字典，添加
        Dictionary dictionary = new Dictionary();
        dictionary.setParentId(0);
        dictionary.setOrderIndex(params.getOrderIndex());
        dictionary.setCategoryId(category.getId());
        dictionary.setOrderIndex(params.getOrderIndex());
        dictionary.setName(params.getName());
        dictionary.setCode(HanyuPinyinUtils.getFirstLettersLo(params.getName()));
        dictionary.setStatus((byte) StatusEnum.ENABLE.getCode());
        dictionary.setGmtCreate(new Date());
        dictionary.setGmtModified(new Date());
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(params));
        dictionaryDictManager.insert(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBannerConfig(BannerUpdateParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_POSITION_ERROR,
                BannerPositionEnum.getByCode(params.getPosition()));
        Dictionary dictionary = dictionaryDictManager.findById(params.getBannerId());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_NOT_FOUND, dictionary);
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                BannerPositionEnum.getByCode(dictionary.getDictionaryCategory().getCode()));
        DictionaryCategory category = dictionaryCategoryDictManager
                .findByCode(params.getPosition());
        ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, category);
        dictionary.setOrderIndex(params.getOrderIndex());
        dictionary.setGmtModified(new Date());
        dictionary.setCategoryId(category.getId());
        dictionary.setOrderIndex(params.getOrderIndex());
        dictionary.setName(params.getName());
        dictionary.setCode(HanyuPinyinUtils.getFirstLettersLo(params.getName()));
        dictionary.setStatus((byte) StatusEnum.ENABLE.getCode());
        params.setBannerId(null);
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(params));
        dictionaryDictManager.updateById(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBannerConfig(Integer id) {
        dictionaryDictManager.deleteById(id);
    }

    @Override
    public BannerDictConfigVo getBannerDictConfigVo() {
        Dictionary dictionary = dictionaryDictManager
                .findByCodeAndCategoryCode(BannerCodeEnum.BANNER_CAROUSE_CONFIG.getCode(),
                        DictionaryCategoryCodeEnum.BANNER_CAROUSEL_CONFIG.getCode());
        BannerDictConfigVo vo = new BannerDictConfigVo();
        if (dictionary != null) {
            vo = JacksonUtils.toBeanWithSnake(dictionary.getValue(), BannerDictConfigVo.class);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBannerDictConfigVo(BannerDictConfigVo vo) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, vo);
        Dictionary dictionary = dictionaryDictManager
                .findByCodeAndCategoryCode(BannerCodeEnum.BANNER_CAROUSE_CONFIG.getCode(),
                        DictionaryCategoryCodeEnum.BANNER_CAROUSEL_CONFIG.getCode());
        if (dictionary == null) {
            //如果为空，则需要新增
            DictionaryCategory dictionaryCategory = dictionaryCategoryDictManager
                    .findByCode(DictionaryCategoryCodeEnum.BANNER_CAROUSEL_CONFIG.getCode());
            ValidateAssert
                    .hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, dictionaryCategory);
            dictionary = new Dictionary();
            dictionary.setStatus((byte) 1);
            dictionary.setValue(JacksonUtils.toJsonWithSnake(vo));
            dictionary.setCode(BannerCodeEnum.BANNER_CAROUSE_CONFIG.getCode());
            dictionary.setName(BannerCodeEnum.BANNER_CAROUSE_CONFIG.getMsg());
            dictionary.setCategoryId(dictionaryCategory.getId());
            dictionary.setParentId(0);
            dictionary.setOrderIndex(0);
            dictionaryDictManager.insert(dictionary);
        } else {
            dictionary.setValue(JacksonUtils.toJsonWithSnake(vo));
            dictionaryDictManager.updateById(dictionary);
        }

    }

    private Integer getBannerCarouseCount() {
        BannerDictConfigVo bannerDictConfigVo = this.getBannerDictConfigVo();
        Integer count = bannerDictConfigVo.getCount();
        return count == null ? 3 : count;
    }

    private BannerConfigVo convertToBannerConfigVo(Dictionary dictionary) {
        BannerConfigVo configVo = JacksonUtils
                .toBeanWithSnake(dictionary.getValue(), BannerConfigVo.class);
        configVo.setUserTypeDesc(ResourceUserTypeEnum.getByCode(configVo.getUserType()).getMsg());
        configVo.setBannerId(dictionary.getId());
        configVo.setPositionZh(BannerPositionEnum.getByCode(configVo.getPosition()).getDesc());
        return configVo;
    }

}
