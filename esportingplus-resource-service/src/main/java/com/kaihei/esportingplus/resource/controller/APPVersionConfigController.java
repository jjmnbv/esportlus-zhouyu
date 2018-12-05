package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.feign.APPVersionConfigServiceClient;
import com.kaihei.esportingplus.api.vo.APPVersionConfigVO;
import com.kaihei.esportingplus.api.vo.APPVersionDetailConfigVO;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.PlatformEnum;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.HttpUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端版本信息配置
 * @author liangyi
 */
@RestController
@RequestMapping("/appversion")
@Api(tags = {"客户端版本信息配置接口"})
public class APPVersionConfigController implements APPVersionConfigServiceClient {

    @Autowired
    HttpServletRequest request;

    @Autowired
    DictionaryDictManager dictionaryDictManager;


    @ApiOperation(value = "查询最新的版本信息")
    @Override
    public ResponsePacket<APPVersionConfigVO> getLatestVersion(@PathVariable("x") String x) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, x);
        String platform;
        // 获取请求的客户端平台
        if (ObjectTools.isEmpty(x)) {
            platform = HttpUtils.getPlatform(request).getCode();
        } else {
            platform = PlatformEnum.getByParam(x.split("_")[0]).getCode();
        }

        List<Dictionary> dictionaryList = dictionaryDictManager.findByCategoryCode(
                DictionaryCategoryCodeEnum.APP_VERSION_CONFIG.getCode());

        APPVersionConfigVO appVersionConfigVO = dictionaryList.stream()
                // 过滤启用的
                .filter(d -> d.getStatus() == StatusEnum.ENABLE.getCode())
                .map(d -> JacksonUtils
                        .toBeanWithSnake(d.getValue(), APPVersionDetailConfigVO.class))
                // 取出当前请求的客户端平台
                .filter(a -> a.getPlatform().equals(platform))
                // 按更新时间倒序
                .sorted(Comparator.comparing(
                        APPVersionConfigVO::getUpdateTime, Comparator.reverseOrder()))
                .findFirst()
                .map(a -> BeanMapper.map(a, APPVersionConfigVO.class))
                .orElseThrow(() -> new BusinessException(
                        BizExceptionEnum.APP_VERSION_CONFIG_NOT_FOUND, platform));

        return ResponsePacket.onSuccess(appVersionConfigVO);
    }


    @ApiOperation(value = "查询所有的版本信息")
    @Override
    public ResponsePacket<List<APPVersionDetailConfigVO>> getVersionList() {
        List<APPVersionDetailConfigVO> detailConfigVOS = dictionaryDictManager.findByCategoryCode(
                DictionaryCategoryCodeEnum.APP_VERSION_CONFIG.getCode())
                .stream()
                .sorted(Comparator.comparingInt(Dictionary::getOrderIndex))
                .map(d -> JacksonUtils
                        .toBeanWithSnake(d.getValue(), APPVersionDetailConfigVO.class))
                .collect(Collectors.toList());
        return ResponsePacket.onSuccess(detailConfigVOS);
    }


    @ApiOperation(value = "新增或修改版本信息")
    @Override
    public ResponsePacket<Void> addOrUpdateVersion(
            @ApiParam(value = "新增或修改版本信息", required = true)
            @RequestBody APPVersionDetailConfigVO appVersionDetailConfigVO) {

        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, appVersionDetailConfigVO);

        Integer dictId = appVersionDetailConfigVO.getDictId();
        if (dictId != null && dictId > 0) {
            // 修改
            Dictionary dictionary = new Dictionary();
            dictionary.setId(dictId);
            dictionary.setValue(JacksonUtils.toJsonWithSnake(appVersionDetailConfigVO));
            dictionaryDictManager.updateById(dictionary);
        } else {
            // 新增 TODO 这个数据字典的新增待统一
            Dictionary dictionary = new Dictionary();
            // dictionary.setCategoryId(dictionaryDictManager);
            // dictionary.setCode(HanyuPinyinUtils);
            dictionary.setValue(JacksonUtils.toJsonWithSnake(appVersionDetailConfigVO));
            // dictionaryDictManager.add
        }

        return ResponsePacket.onSuccess();
    }


    @ApiOperation(value = "根据id查询版本信息")
    @Override
    public ResponsePacket<APPVersionConfigVO> getVersionById(
            @ApiParam(value = "根据id查询版本信息", required = true)
            @PathVariable("id") Integer id) {
        APPVersionDetailConfigVO detailConfigVO = dictionaryDictManager
                .buildDictBase(id, APPVersionDetailConfigVO.class).getValue();
        return ResponsePacket.onSuccess(detailConfigVO);
    }


    @ApiOperation(value = "根据id删除版本信息")
    @Override
    public ResponsePacket<Void> deleteVersionById(
            @ApiParam(value = "根据id删除版本信息", required = true)
            @PathVariable("id") Integer id) {
        // 修改为禁用状态
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        dictionary.setStatus((byte) StatusEnum.DISABLE.getCode());
        dictionaryDictManager.updateById(dictionary);
        return ResponsePacket.onSuccess();
    }


}
