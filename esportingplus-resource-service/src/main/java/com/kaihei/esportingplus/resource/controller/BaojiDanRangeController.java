package com.kaihei.esportingplus.resource.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaihei.esportingplus.api.feign.BaojiDanRangeServiceClient;
import com.kaihei.esportingplus.api.params.BaojiDanRangeBackendQueryParams;
import com.kaihei.esportingplus.api.params.BaojiDanRangeBatchParams;
import com.kaihei.esportingplus.api.params.BaojiDanRangeParams;
import com.kaihei.esportingplus.api.vo.BaojiDanRangeVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.service.BaojiDanRangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 暴鸡接单范围配置
 * @author liangyi
 */
@RestController
@RequestMapping("/baoji/dan/range/pvp")
@Api(tags = {"暴鸡接单范围配置"})
public class BaojiDanRangeController implements BaojiDanRangeServiceClient {

    @Autowired
    DictionaryDictManager dictionaryDictManager;
    @Autowired
    BaojiDanRangeService baojiDanRangeService;


    @ApiOperation(value = "分页所有暴鸡接单范围")
    @Override
    public ResponsePacket<PagingResponse<BaojiDanRangeVO>> getBaojiDanRangeByPage(
            @ApiParam(value = "查询参数", required = false)
            @RequestBody BaojiDanRangeBackendQueryParams baojiDanRangeBackendQueryParams) {
        List<BaojiDanRangeVO> rangeVOList;
        PagingResponse<BaojiDanRangeVO> pagingResponse;
        if (baojiDanRangeBackendQueryParams.getLimit() == -1) {
            // 如果传入 -1, 不做分页处理
            rangeVOList = baojiDanRangeService
                    .getAllBaojiDanRangeByGameId(baojiDanRangeBackendQueryParams.getGameId());
            pagingResponse = new PagingResponse<>(
                    1, rangeVOList.size(),
                    rangeVOList.size(), rangeVOList);
        } else {
            // 正常分页
            PageHelper
                    .startPage(baojiDanRangeBackendQueryParams.getOffset(),
                            baojiDanRangeBackendQueryParams.getLimit());
            rangeVOList = baojiDanRangeService
                    .getAllBaojiDanRangeByGameId(baojiDanRangeBackendQueryParams.getGameId());
            PageInfo<BaojiDanRangeVO> page = new PageInfo<>(rangeVOList);
            pagingResponse = new PagingResponse<>(
                    page.getPageNum(), page.getPageSize(),
                    page.getTotal(), page.getList());
        }
        return ResponsePacket.onSuccess(pagingResponse);
    }

    @ApiOperation(value = "新增暴鸡接单范围")
    @Override
    public ResponsePacket<Void> addBaojiDanRangeByGame(
            @ApiParam(value = "暴鸡接单范围", required = true)
            @RequestBody BaojiDanRangeBatchParams baojiDanRangeBatchParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, baojiDanRangeBatchParams);
        baojiDanRangeService.addBaojiDanRange(baojiDanRangeBatchParams);
        for (BaojiDanRangeParams params : baojiDanRangeBatchParams.getBaojiDanRangeList()) {
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        }
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "修改暴鸡接单范围")
    @Override
    public ResponsePacket<Void> updateBaojiDanRangeByGame(
            @ApiParam(value = "暴鸡接单范围", required = true)
            @RequestBody BaojiDanRangeBatchParams baojiDanRangeBatchParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, baojiDanRangeBatchParams);
        List<BaojiDanRangeParams> danRangeParamsList = baojiDanRangeBatchParams
                .getBaojiDanRangeList();
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, danRangeParamsList);
        for (BaojiDanRangeParams params : danRangeParamsList) {
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                    params);
            ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                    params.getBaojiDanRangeId());
        }
        baojiDanRangeService.updateBaojiDanRange(baojiDanRangeBatchParams);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "删除暴鸡接单范围")
    @Override
    public ResponsePacket<Void> deleteBaojiDanRange(
            @ApiParam(value = "暴鸡接单范围", required = true)
            @RequestBody List<Integer> baojiDanRangeIds) {
        ValidateAssert.isTrue(baojiDanRangeIds.size() > 0,
                BizExceptionEnum.PARAM_VALID_FAIL);
        baojiDanRangeService.deleteBaojiDanRangeBatch(baojiDanRangeIds);
        return ResponsePacket.onSuccess();
    }
}
