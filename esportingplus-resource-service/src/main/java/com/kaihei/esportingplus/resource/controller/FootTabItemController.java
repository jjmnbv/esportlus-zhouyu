package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.vo.freeteam.FootTabItemConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.FootTabItemVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foottabitem")
@Api(tags = {"底部tab内部服务配置"})
public class FootTabItemController extends AbstractDictBaseRestController<Dictionary> {


    @ApiOperation("通过分类Code查询底部所有tab的所有服务配置")
    @GetMapping("/category/{categoryCode}/insideList")
    public ResponsePacket<List<?>> findTabsInsideList(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable String categoryCode) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        List<Dictionary> dictionaries = suportManager.findByCategoryCode(categoryCode);
        List<FootTabItemVO> tabConfigs = dictionaries.stream()
                .filter(f->f.getChildDictionary() != null)
                .flatMap(fm-> fm.getChildDictionary().stream())
                .map(m->{
                    FootTabItemVO footTabItemVO = FastJsonUtils.fromJson(m.getValue(),FootTabItemVO.class);
                    footTabItemVO.setId(m.getId());
                    footTabItemVO.setTabId(m.getParentId());
                    footTabItemVO.setName(m.getName());
                    footTabItemVO.setOrderIndex(m.getOrderIndex());
                    footTabItemVO.setStatus(m.getStatus());
                    return footTabItemVO;
                })
                .collect(Collectors.toList());

        return ResponsePacket.onSuccess(tabConfigs);
    }

    @ApiOperation("批量插入底部Tab内部服务")
    @PostMapping("/inside/batchInsert")
    public ResponsePacket<List<?>> batchInsert(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabItemVO> footTabItems) {
        if(CollectionUtils.isEmpty(footTabItems)){
            return ResponsePacket.onError(BizExceptionEnum.FOOT_TAB_INSIDE_ARGS_EMPTY);
        }

        //组装数据
        DictionaryDictManager dicManager = (DictionaryDictManager) getSuportManager();
        List<Dictionary> dictionaries = footTabItems.stream().map(m->{
            Dictionary dic = new Dictionary();
            //状态
            dic.setStatus(m.getStatus());
            //所属tab的id(dic父id)
            dic.setParentId(m.getTabId());
            //当前名称
            dic.setName(m.getCurremtName());

            //把下面的属性组装成json放到dic表中的value里面
            FootTabItemVO dicValue = new FootTabItemVO();
            dicValue.setCurremtName(m.getCurremtName());
            dicValue.setIcon(m.getIcon());

            dicValue.setRefLink(m.getRefLink());

            dic.setValue(FastJsonUtils.toJson(dicValue));
            return dic;
        }).collect(Collectors.toList());
        dicManager.batchInsertWithAutoCreateCode(dictionaries);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation("批量更新底部Tab内部服务")
    @PostMapping("/inside/batchUpdate")
    public ResponsePacket<List<?>> batchUpdate(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabItemVO> footTabItems) {

        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,footTabItems);

        if(CollectionUtils.isEmpty(footTabItems)){
            return ResponsePacket.onError(BizExceptionEnum.FOOT_TAB_INSIDE_ARGS_EMPTY);
        }

        //组装数据
        DictionaryDictManager dicManager = (DictionaryDictManager) getSuportManager();
        List<Dictionary> dictionaries = footTabItems.stream().map(m->{
            Dictionary dic = new Dictionary();

            //状态
            dic.setStatus(m.getStatus());
            //所属tab的id(dic父id)
            dic.setParentId(m.getTabId());
            //更新当前名称
            dic.setName(m.getCurremtName());


            FootTabItemVO dicValue;
            //id为空说明是需要新增的数据
            if(m.getId() == null){
                //把下面的属性组装成json放到dic表中的value里面
                dicValue = new FootTabItemVO();
                dicValue.setCurremtName(m.getCurremtName());
                dicValue.setIcon(m.getIcon());
                dicValue.setRefLink(m.getRefLink());
            }else{
                //否则说明是需要修改的数据
                dic.setDictId(m.getId());
                dic.setGmtModified(new Date());
                //dic表中的value里面的值 拿出来添加排序和默认落地页属性后，又扔回去
                Dictionary dictionary = dicManager.findById(m.getId());
                dicValue = FastJsonUtils.fromJson(dictionary.getValue(),FootTabItemVO.class);
                dicValue.setCurremtName(m.getCurremtName());
                dicValue.setIcon(m.getIcon());
                dicValue.setRefLink(m.getRefLink());
            }

            dic.setValue(FastJsonUtils.toJson(dicValue));
            return dic;
        }).collect(Collectors.toList());

        dicManager.updateWithOutAutoCreateCode(dictionaries);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation("批量更新底部Tab内部服务内容配置")
    @PostMapping("/inside/config/batchUpdate")
    public ResponsePacket<List<?>> batchConfigUpdate(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<FootTabItemConfigVO> footTabItemConfigs) {

        if(CollectionUtils.isEmpty(footTabItemConfigs)){
            return ResponsePacket.onError(BizExceptionEnum.FOOT_TAB_INSIDE_ARGS_EMPTY);
        }

        //组装数据
        DictionaryDictManager dicManager = (DictionaryDictManager) getSuportManager();
        List<Dictionary> dictionaries = getDictionarys(footTabItemConfigs);

        dicManager.updateWithAutoCreateCodeById(dictionaries);
        return ResponsePacket.onSuccess();
    }

    private List<Dictionary> getDictionarys(List<FootTabItemConfigVO> footTabItemConfigs) {
        DictionaryDictManager dicManager = (DictionaryDictManager) getSuportManager();
        return footTabItemConfigs.stream().flatMap(m -> {
                    List<FootTabItemVO> footTabItems = m.getFootTabItems();
                    //校验重复的排序号
                    List<Integer> duplicateOrderIndex = checkDuplicateOrderIndex(footTabItems);
                    if (CollectionUtils.isNotEmpty(duplicateOrderIndex)) {
                        throw new BusinessException(BizExceptionEnum.DUPLICATE_TAB_ITEM_ORDER_INDEX,
                                new String[]{m.getTabName(), String.valueOf(duplicateOrderIndex.get(0))});
                    }
                //二维转数组转一维数组
                return footTabItems.stream();
            }).map(m->{
                Dictionary dic = new Dictionary();
                dic.setDictId(m.getId());
                dic.setGmtModified(new Date());
                //设置排序号
                dic.setOrderIndex(m.getOrderIndex());

                //dic表中的value里面的值 拿出来添加排序和默认落地页属性后，又扔回去
                Dictionary dictionary = dicManager.findById(m.getId());
                FootTabItemVO footTabItemVO = FastJsonUtils.fromJson(dictionary.getValue(),FootTabItemVO.class);

                footTabItemVO.setActiveLandingPage(m.getActiveLandingPage());

                dic.setValue(FastJsonUtils.toJson(footTabItemVO));
                return dic;
            }).collect(Collectors.toList());
    }

    private List<Integer> checkDuplicateOrderIndex(List<FootTabItemVO> footTabItems) {
        return footTabItems.stream()
                //统计集合中排序号重复出现的次数
                .collect(Collectors.groupingBy(FootTabItemVO::getOrderIndex, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                //返回重复出现的排序号
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

}
