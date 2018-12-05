package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.OfficialAccountConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.common.tools.HanyuPinyinUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.resource.data.manager.dao.DictionaryCategoryDAO;
import com.kaihei.esportingplus.resource.data.manager.dao.DictionaryDAO;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 官方账号配置
 * @author liangyi
 */
@RestController
@RequestMapping("/officialAccount")
@Api(tags = {"官方账号配置接口"})
public class OfficialAccountConfigController {

    @Autowired
    private DictionaryCategoryDAO dictionaryCategoryDAO;
    @Autowired
    private DictionaryDAO dictionaryDAO;

    private static String officialAccountCategoryCode =
            DictionaryCategoryCodeEnum.OFFICIAL_ACCOUNT_CONFIG.getCode();

    @ApiOperation(value = "根椐id查询官方账号配置")
    @GetMapping("/backend/{officialAccountConfigId}")
    public ResponsePacket<OfficialAccountConfigVO> getOfficialAccountConfigById(
           @PathVariable("officialAccountConfigId") Integer officialAccountConfigId) {
        ValidateAssert.isTrue(officialAccountConfigId > 0,
                BizExceptionEnum.PARAM_VALID_FAIL);
        OfficialAccountConfigVO configVO =
                buildOfficialAccountConfigVO(officialAccountConfigId);
        return ResponsePacket.onSuccess(configVO);
    }

    private OfficialAccountConfigVO buildOfficialAccountConfigVO(
            Integer officialAccountConfigId) {
        DictBaseVO<OfficialAccountConfigVO> dictBaseVO = dictionaryDAO
                .buildDictBase(officialAccountConfigId, OfficialAccountConfigVO.class);
        OfficialAccountConfigVO configVO = dictBaseVO.getValue();
        configVO.setCategoryName(dictBaseVO.getName());
        configVO.setOfficialAccountConfigId(dictBaseVO.getDictId());
        configVO.setOrderIndex(dictBaseVO.getOrderIndex());
        configVO.setStatus(dictBaseVO.getStatus());
        return configVO;
    }


    @ApiOperation(value = "查询所有官方账号配置")
    @GetMapping("/backend/all")
    public ResponsePacket<List<OfficialAccountConfigVO>> getAllOfficialAccountConfig() {

        List<Dictionary> dictList = dictionaryDAO
                .findByCategoryCode(officialAccountCategoryCode);

        List<OfficialAccountConfigVO> configVOS = dictList.stream()
                .map(d -> buildOfficialAccountConfigVO(d.getId()))
                .sorted(Comparator.comparingInt(OfficialAccountConfigVO::getOrderIndex))
                .collect(Collectors.toList());

        return ResponsePacket.onSuccess(configVOS);
    }


    @ApiOperation(value = "新增官方账号配置")
    @PostMapping("/backend/add")
    public ResponsePacket<Void> addOfficialAccountConfig(
            @ApiParam(value = "官方账号配置", required = true)
            @RequestBody OfficialAccountConfigVO officialAccountConfigVO) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, officialAccountConfigVO);
        Dictionary dictionary = buildOfficialAccountConfigDictionary(officialAccountConfigVO);
        dictionaryDAO.insert(dictionary);
        return ResponsePacket.onSuccess();
    }

    private Dictionary buildOfficialAccountConfigDictionary(
            OfficialAccountConfigVO officialAccountConfigVO) {
        Dictionary dictionary = new Dictionary();
        dictionary.setName(officialAccountConfigVO.getCategoryName());
        dictionary.setCode(officialAccountCategoryCode + "_"
                + HanyuPinyinUtils.getFirstLetter(officialAccountConfigVO.getCategoryName()));

        Integer orderIndex = officialAccountConfigVO.getOrderIndex();
        if (orderIndex == null) {
            orderIndex = 1;
        }
        dictionary.setOrderIndex(orderIndex);

        Integer status = officialAccountConfigVO.getStatus();
        if (status == null) {
            status = StatusEnum.ENABLE.getCode();
        }
        dictionary.setStatus(status.byteValue());

        dictionary.setCategoryId(dictionaryCategoryDAO
                .findByCode(officialAccountCategoryCode).getId());
        // 已有的数据字典属性置空
        officialAccountConfigVO.setCategoryName(null);
        officialAccountConfigVO.setOrderIndex(null);
        officialAccountConfigVO.setStatus(null);
        dictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(officialAccountConfigVO));
        return dictionary;
    }

    @ApiOperation(value = "修改官方账号配置")
    @PostMapping("/backend/update")
    public ResponsePacket<Void> updateOfficialAccountConfig(
            @ApiParam(value = "官方账号配置", required = true)
            @RequestBody OfficialAccountConfigVO officialAccountConfigVO) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, officialAccountConfigVO);
        Integer officialAccountConfigId = officialAccountConfigVO.getOfficialAccountConfigId();
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, officialAccountConfigId);

        Dictionary dictionary = buildOfficialAccountConfigDictionary(officialAccountConfigVO);
        dictionary.setId(officialAccountConfigId);
        dictionaryDAO.updateById(dictionary);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "删除官方账号配置")
    @GetMapping("/backend/delete/{officialAccountConfigId}")
    public ResponsePacket<Void> deleteOfficialAccountConfig(
            @PathVariable("officialAccountConfigId") Integer officialAccountConfigId) {
        ValidateAssert.isTrue(officialAccountConfigId > 0,
                BizExceptionEnum.PARAM_VALID_FAIL);

        Dictionary dictionary = new Dictionary();
        dictionary.setId(officialAccountConfigId);
        dictionary.setStatus((byte)StatusEnum.DISABLE.getCode());
        dictionaryDAO.updateById(dictionary);
        return ResponsePacket.onSuccess();
    }

}
