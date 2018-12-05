package com.kaihei.esportingplus.resource.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.kaihei.esportingplus.api.params.DictionaryUpdateParam;
import com.kaihei.esportingplus.api.vo.freeteam.DictionaryVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("字典")
@RestController
@RequestMapping("dictionary")
@Slf4j
public class DictionaryRestController extends AbstractDictBaseRestController<Dictionary> {

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation("通过分类Code和字典Code查询字典")
    @GetMapping("category/{categoryCode}/dictionary/{code}")
    public ResponsePacket<?> findByCodeAndCategoryCode(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable String categoryCode,
            @PathVariable String code, Byte status) {
        Dictionary dictionary = ((DictionaryDictManager) getSuportManager())
                .findByCodeAndCategoryCode(code, categoryCode);

        return ResponsePacket.onSuccess(jsonValueConverter(dictionary, status));
    }

    @ApiOperation("通过分类Code查询字典")
    @GetMapping("category/{categoryCode}")
    public ResponsePacket<List<?>> findByCategoryCode(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable String categoryCode, Byte status) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        List<Dictionary> dictionaries = suportManager.findByCategoryCode(categoryCode);

        return ResponsePacket
                .onSuccess(dictionaries.parallelStream().map(d -> jsonValueConverter(d, status))
                        .collect(Collectors.toList()));
    }

    @ApiOperation("通过字典父Id 字典分类Code查询字典")
    @GetMapping("dictionaryparent/{pid}/category/{categoryCode}")
    public ResponsePacket<List<Map<String, Object>>> findByDictcionayPidAndCategoryCode(
            @PathVariable Integer pid, @PathVariable String categoryCode, Byte status) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        //父字典
        Dictionary dictionary = suportManager.findById(pid);

        List<Dictionary> dictionaries = Optional.ofNullable(dictionary)
                .map(Dictionary::getChildDictionary)
                .orElse(Lists.newArrayList());

        //过滤出 categoryCode相同的字典
        dictionaries = dictionaries.stream()
                .filter(d -> Objects.nonNull(d.getDictionaryCategory()))
                .filter(d -> categoryCode.equals(d.getDictionaryCategory().getCode()))
                .collect(Collectors.toList());
        //Json格式修正 返回
        return ResponsePacket
                .onSuccess(dictionaries.stream().map(d -> jsonValueConverter(d, status))
                        .collect(Collectors.toList()));
    }

    @ApiOperation("批量插入字典")
    @PostMapping("batchInsert")
    public ResponsePacket<List<?>> batchInsert(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<Dictionary> dictionaries) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        suportManager.batchInsertWithAutoCreateCode(dictionaries);
        return ResponsePacket.onSuccess();
    }


    @ApiOperation("批量更新字典")
    @PostMapping("batchUpdate")
    public ResponsePacket<List<?>> batchUpdate(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<Dictionary> dictionaries) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();

        suportManager.updateById(dictionaries);

        return ResponsePacket.onSuccess();
    }

    @ApiOperation("批量更新字典")
    @PutMapping
    public ResponsePacket<Integer> updateMutilDictionary(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<DictionaryVO> dictionarieVos) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();

        List<Dictionary> dictionaries = dictionarieVos.stream()
                .map(dvo -> {
                    Dictionary dictionary = dvo.cast(Dictionary.class);
                    if (!BeanUtils.isSimpleValueType(dvo.getValue().getClass())) {
                        dictionary.setValue(JacksonUtils.toJsonWithSnake(dvo.getValue()));
                    }
                    return dictionary;
                })
                .collect(Collectors.toList());

        return ResponsePacket.onSuccess(suportManager.updateById(dictionaries));
    }

    /**
     * 对字典及子字典的Value进行处理
     *
     * 是Json的转成Map再给前端
     */
    private Map<String, Object> jsonValueConverter(Dictionary dictionary, Byte status) {
        try {
            if (status != null && !dictionary.getStatus().equals(status)) {
                return null;
            }

            Map<String, Object> dicMap = objectMapper
                    .readValue(objectMapper.writeValueAsString(dictionary),
                            new TypeReference<Map<String, Object>>() {
                            });
            jsonValueConverter(dicMap, status);
            return dicMap;
        } catch (IOException e) {
            log.error("反序列化Json失败", e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void jsonValueConverter(Map<String, Object> dicMap, Byte status) {
        //子节点对象
        List<Map<String, Object>> childDictionary = (List<Map<String, Object>>) dicMap
                .get("child_dictionary");
        if (status != null && childDictionary != null) {
            List<Map<String, Object>> cdMap = CollectionUtils
                    .finds(childDictionary, d -> d.get("status").equals(status));
            dicMap.put("child_dictionary", cdMap);
        }

        String value = (String) dicMap.get("value");
        try {
            dicMap.put("value", objectMapper.readValue(value, Object.class));
        } catch (IOException ignored) {
        }

        //存在子节点、迭代子节点

        Optional.ofNullable(childDictionary).filter(cd -> !cd.isEmpty()).ifPresent(
                cd -> cd.parallelStream().forEach(dm -> jsonValueConverter(dm, status)));

    }

    /**
     * 接口：根据Id更新元素
     */
    @Override
    @PutMapping("updateElementById")
    public ResponsePacket<Integer> updateElementById(@PathVariable Integer id,
            Dictionary t) {
        return super.updateElementById(id, t);
    }

    /**
     * 接口：根据Id更新元素
     */
    @PutMapping("{id}")
    public ResponsePacket<Integer> updateDictionaryById(@PathVariable Integer id,
            @RequestBody DictionaryUpdateParam dictionaryUpdateParam)
            throws JsonProcessingException {
        Object value = dictionaryUpdateParam.getValue();
        Dictionary dictionary = dictionaryUpdateParam.cast(Dictionary.class);

        if (value instanceof Map || value instanceof Collection) {
            dictionary.setValue(objectMapper.writeValueAsString(value));
        }

        return this.updateElementById(id, dictionary);
    }
}
