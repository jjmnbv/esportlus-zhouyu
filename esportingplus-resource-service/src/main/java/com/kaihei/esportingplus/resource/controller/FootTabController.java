package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.feign.FootTabClient;
import com.kaihei.esportingplus.api.vo.freeteam.FootTabConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.FootTabItemConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.FootTabItemVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryCategoryDictManager;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.DictionaryCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foottab")
@Api(tags = {"底部tab配置"})
public class FootTabController extends AbstractDictBaseRestController<Dictionary> implements
        FootTabClient {

    @Autowired
    private DictionaryCategoryDictManager dictionaryCategoryDictManager;

    @ApiOperation("APP调用-通过分类Code查询底部所有tab,并且带tab下的所有服务配置")
    public ResponsePacket<List<?>> findTabsByCategoryCodeForAPP(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable String categoryCode) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        List<Dictionary> dictionaries = suportManager.findByCategoryCode(categoryCode);
        List<?> result = getTabsWithInsideList(dictionaries,true);
        return ResponsePacket.onSuccess(result);
    }

    @ApiOperation("后台管理调用-通过分类Code查询底部所有tab,并且带tab下的所有服务配置")
    public ResponsePacket<List<?>> findTabsByCategoryCodeWithInsideList(@PathVariable String categoryCode) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        List<Dictionary> dictionaries = suportManager.findByCategoryCode(categoryCode);
        List<?> result = getTabsWithInsideList(dictionaries,false);
        return ResponsePacket.onSuccess(result);
    }

    @ApiOperation("后台管理调用-通过分类Code单独查询底部所有tab,并且不带tab下的服务配置")
    public ResponsePacket<List<?>> findTabsByCategoryCode(@PathVariable String categoryCode) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        List<Dictionary> dictionaries = suportManager.findByCategoryCode(categoryCode);
        List<?> result = dictionaries.stream()
                    .map(m->{
                        //从dic value 里面拿出部分属性再加上下面属性返回给前端
                        FootTabConfigVO footTabConfigVO = FastJsonUtils.fromJson(m.getValue(),FootTabConfigVO.class);
                        footTabConfigVO.setId(m.getDictId());
                        footTabConfigVO.setCategoryCode(m.getDictionaryCategory().getCode());
                        footTabConfigVO.setName(m.getName());
                        footTabConfigVO.setOrderIndex(m.getOrderIndex());
                        footTabConfigVO.setStatus(m.getStatus());
                        return footTabConfigVO;
                    }).collect(Collectors.toList());
        return ResponsePacket.onSuccess(result);
    }

    @ApiOperation("批量插入底部Tab")
    public ResponsePacket<List<?>> batchInsert(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabConfigVO> footTabConfigs) {
        if(CollectionUtils.isEmpty(footTabConfigs)){
            return ResponsePacket.onError(BizExceptionEnum.FOOT_TAB_ARGS_EMPTY);
        }

        //校验重复的排序号
        List<Integer> duplicateOrderIndex = checkDuplicateOrderIndex(footTabConfigs);
        if(CollectionUtils.isNotEmpty(duplicateOrderIndex)){
            return ResponsePacket.onError(BizExceptionEnum.DUPLICATE_TAB_ORDER_INDEX.getErrCode(),
                    String.format(BizExceptionEnum.DUPLICATE_TAB_ORDER_INDEX.getErrMsg(),duplicateOrderIndex.get(0)));
        }

        //开始入库
        DictionaryDictManager dicManager = (DictionaryDictManager) getSuportManager();
        DictionaryCategory category = dictionaryCategoryDictManager.findByCode(footTabConfigs.get(0).getCategoryCode());
        List<Dictionary> dictionaries = footTabConfigs.stream().map(m->{
            Dictionary dic = new Dictionary();

            //原始名称
            dic.setName(m.getCurremtName());
            //排序号
            dic.setOrderIndex(m.getOrderIndex());
            //所属分类id
            dic.setCategoryId(category.getId());
            //状态
            dic.setStatus(m.getStatus());

            //把下面的属性组装成json放到dic表中的value里面
            FootTabConfigVO dicValue = new FootTabConfigVO();
            dicValue.setCurremtName(m.getCurremtName());
            dicValue.setIconSelected(m.getIconSelected());
            dicValue.setIconDiselect(m.getIconDiselect());
            dicValue.setActiveLandingPage(m.getActiveLandingPage());
            dicValue.setActiveTabInsideOrder(m.getActiveTabInsideOrder());
            dicValue.setActiveDefaultTab(m.getActiveDefaultTab());

            dic.setValue(FastJsonUtils.toJson(dicValue));
            return dic;
        }).collect(Collectors.toList());
        dicManager.batchInsertWithAutoCreateCode(dictionaries);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation("批量更新底部Tab")
    public ResponsePacket<List<?>> batchUpdate(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabConfigVO> footTabConfigs) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,footTabConfigs);
        DictionaryDictManager dicManager = (DictionaryDictManager) getSuportManager();

        //校验重复的排序号
        List<Integer> duplicateOrderIndex = checkDuplicateOrderIndex(footTabConfigs);
        if(CollectionUtils.isNotEmpty(duplicateOrderIndex)){
            return ResponsePacket.onError(BizExceptionEnum.DUPLICATE_TAB_ORDER_INDEX.getErrCode(),
                    String.format(BizExceptionEnum.DUPLICATE_TAB_ORDER_INDEX.getErrMsg(),duplicateOrderIndex.get(0)));
        }

        List<Dictionary> dictionaries = footTabConfigs.stream().map(m->{
            Dictionary dic = new Dictionary();

            //原始名称
            dic.setName(m.getCurremtName());
            //排序号
            dic.setOrderIndex(m.getOrderIndex());
            //状态
            dic.setStatus(m.getStatus());

            //如果id为空说明为需要新增数据，setCategoryId
            if(m.getId() == null){
                DictionaryCategory category = dictionaryCategoryDictManager.findByCode(m.getCategoryCode());
                //所属分类id
                dic.setCategoryId(category.getId());
            }else{
                //否则为需要修改数据
                dic.setDictId(m.getId());
                dic.setGmtModified(new Date());
            }

            //把下面的属性组装成json放到dic表中的value里面
            FootTabConfigVO dicValue = new FootTabConfigVO();
            dicValue.setCurremtName(m.getCurremtName());
            dicValue.setIconSelected(m.getIconSelected());
            dicValue.setIconDiselect(m.getIconDiselect());
            dicValue.setActiveLandingPage(m.getActiveLandingPage());
            dicValue.setActiveTabInsideOrder(m.getActiveTabInsideOrder());
            dicValue.setActiveDefaultTab(m.getActiveDefaultTab());

            dic.setValue(FastJsonUtils.toJson(dicValue));
            return dic;
        }).collect(Collectors.toList());

        dicManager.updateWithOutAutoCreateCode(dictionaries);
        return ResponsePacket.onSuccess();
    }

    private List<Integer> checkDuplicateOrderIndex(List<FootTabConfigVO> footTabConfigVOs) {
        return footTabConfigVOs.stream()
                //统计集合中排序号重复出现的次数
                .collect(Collectors.groupingBy(FootTabConfigVO::getOrderIndex, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                //返回重复出现的排序号
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<FootTabItemConfigVO> getTabsWithInsideList(List<Dictionary> dictionaries,boolean isApp) {
        return dictionaries.stream()
                .filter(f->{
                    //如果是给APP调用的过滤出状态为：启用(1)的
                    //否则不过滤状态
                    if(isApp){
                        return f.getStatus().intValue() == CommonConstants.ACTIVE;
                    }else{
                        return true;
                    }
                })
                .map(m -> {
                    FootTabItemConfigVO result = new FootTabItemConfigVO();
                    result.setTabId(m.getDictId());
                    result.setTabName(m.getName());
                    result.setTabCode(m.getCode());
                    result.setStatus(m.getStatus());
                    //从dic value 里面拿出图标属性返回给前端
                    FootTabConfigVO footTabConfigVO = FastJsonUtils
                            .fromJson(m.getValue(), FootTabConfigVO.class);
                    if(footTabConfigVO != null){
                        result.setActiveDefaultTab(footTabConfigVO.getActiveDefaultTab());
                        result.setActiveLandingPage(footTabConfigVO.getActiveLandingPage());
                        result.setActiveTabInsideOrder(footTabConfigVO.getActiveTabInsideOrder());
                        result.setTabIconSelected(footTabConfigVO.getIconSelected());
                        result.setTabIconDiselect(footTabConfigVO.getIconDiselect());
                    }


                    //把dic子节点转换为tab下的子对象
                    if(m.getChildDictionary() != null){
                        List<FootTabItemVO> footTabItems = m.getChildDictionary().stream()
                                .filter(f->{
                                    //如果是给APP调用的过滤出状态为：启用(1)的
                                    //否则不过滤状态
                                    if(isApp){
                                        return f.getStatus().intValue() == CommonConstants.ACTIVE;
                                    }else{
                                        return true;
                                    }
                                })
                                .map(item -> {

                                    //解析dic value json串为FootTabItemVO对象
                                    FootTabItemVO footTabItemVO = FastJsonUtils
                                            .fromJson(item.getValue(), FootTabItemVO.class);
                                    footTabItemVO.setId(item.getDictId());
                                    footTabItemVO.setTabId(item.getParentId());
                                    footTabItemVO.setName(item.getName());
                                    footTabItemVO.setCode(item.getCode());
                                    footTabItemVO.setOrderIndex(item.getOrderIndex());
                                    footTabItemVO.setStatus(item.getStatus());
                                    return footTabItemVO;
                                })
                                .collect(Collectors.toList());

                        result.setFootTabItems(footTabItems);
                    }

                    return result;
                }).collect(Collectors.toList());
    }

}
