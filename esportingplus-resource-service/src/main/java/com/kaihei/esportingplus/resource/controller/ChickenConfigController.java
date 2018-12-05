package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.enums.ChickenConfigTypeEnum;
import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.feign.ChickenConfigServiceClient;
import com.kaihei.esportingplus.api.vo.ChickenConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryCategoryDictManager;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chicken/config")
@Api(tags = {"小鸡配置"})
public class ChickenConfigController implements
        ChickenConfigServiceClient {

    @Autowired
    private DictionaryDictManager dictionaryDictManager;
    @Autowired
    private DictionaryCategoryDictManager dictionaryCategoryDictManager;

    @ApiOperation("内部调用-获取小鸡配置")
    @Override
    public ResponsePacket<List<ChickenConfigVo>> findChickConfig() {
        List<Dictionary> dictionaryList = dictionaryDictManager
                .findByCategoryCode(DictionaryCategoryCodeEnum.CHICKEN_CONFIG.getCode());
        if (ObjectTools.isEmpty(dictionaryList)) {
            return ResponsePacket.onSuccess(new ArrayList<>());
        }
        List<ChickenConfigVo> vos = dictionaryList.stream().map(it -> {
            ChickenConfigVo chickenConfigVo = JacksonUtils
                    .toBeanWithSnake(it.getValue(), ChickenConfigVo.class);
            chickenConfigVo.setStatus(it.getStatus().intValue());
            return chickenConfigVo;
        }).collect(Collectors.toList());
        return ResponsePacket.onSuccess(vos);
    }

    @ApiOperation("APP调用-获取小鸡配置")
    @Override
    public ResponsePacket<ChickenConfigVo> findChickConfigForApp(
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "小鸡配置类型 1:ios个人中心小鸡, 2：ios免费车队小鸡 3: Android个人中心小鸡 4：Android免费车队小鸡", required = true) @RequestParam(value = "type", required = true) Integer type) {
        UserSessionContext.getUser();
        ChickenConfigVo chickConfigByTypeInner = this.findChickConfigByTypeInner(type);
        if (!new Integer(1).equals(chickConfigByTypeInner.getStatus())) {
            chickConfigByTypeInner = new ChickenConfigVo();
        }
        return ResponsePacket.onSuccess(chickConfigByTypeInner);
    }

    @Override
    public ResponsePacket<ChickenConfigVo> findChickConfigByType(
            @ApiParam(value = "小鸡配置类型 1:ios个人中心小鸡, 2：ios免费车队小鸡 3: Android个人中心小鸡 4：Android免费车队小鸡", required = true) @PathVariable("type") Integer type) {
        return ResponsePacket.onSuccess(findChickConfigByTypeInner(type));
    }

    private ChickenConfigVo findChickConfigByTypeInner(Integer type) {
        Dictionary dictionary = dictionaryDictManager
                .findByCodeAndCategoryCode(type.toString(),
                        DictionaryCategoryCodeEnum.CHICKEN_CONFIG.getCode());
        if (dictionary == null) {
            return new ChickenConfigVo();
        }
        ChickenConfigVo chickenConfigVo = JacksonUtils
                .toBeanWithSnake(dictionary.getValue(), ChickenConfigVo.class);
        chickenConfigVo.setStatus(dictionary.getStatus().intValue());
        return chickenConfigVo;
    }

    @ApiOperation("保存小鸡配置")
    @Override
    public ResponsePacket<Void> saveChickConfig(@RequestBody ChickenConfigVo vo) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, vo);
        Dictionary dictionary = dictionaryDictManager
                .findByCodeAndCategoryCode(vo.getType().toString(),
                        DictionaryCategoryCodeEnum.CHICKEN_CONFIG.getCode());
        if (dictionary != null) {
            dictionary.setStatus((byte) vo.getStatus().intValue());
            vo.setStatus(null);
            dictionary.setName(ChickenConfigTypeEnum.fromCode(vo.getType()).getMsg());
            dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(vo));
            dictionaryDictManager.updateById(dictionary);
        } else {
            DictionaryCategory category = dictionaryCategoryDictManager
                    .findByCode(DictionaryCategoryCodeEnum.CHICKEN_CONFIG.getCode());
            ValidateAssert.hasNotNull(BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_INIT, category);
            dictionary = new Dictionary();
            dictionary.setStatus((byte) vo.getStatus().intValue());
            vo.setStatus(null);
            dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(vo));
            dictionary.setCode(vo.getType().toString());
            dictionary.setName(ChickenConfigTypeEnum.fromCode(vo.getType()).getMsg());
            dictionary.setCategoryId(category.getId());
            dictionary.setOrderIndex(0);
            dictionary.setGmtModified(new Date());
            dictionary.setParentId(0);
            dictionary.setGmtCreate(new Date());
            dictionaryDictManager.insert(dictionary);
        }
        return ResponsePacket.onSuccess();
    }
}
