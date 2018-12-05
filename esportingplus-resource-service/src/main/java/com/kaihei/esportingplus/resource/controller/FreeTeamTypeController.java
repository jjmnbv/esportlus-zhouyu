package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.feign.FreeTeamTypeServiceClient;
import com.kaihei.esportingplus.api.params.freeteam.FreeTeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamTypeSimpleVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.resource.domain.service.freeteam.FreeTeamTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 免费车队类型
 * @author liangyi
 */
@RestController
@RequestMapping("/freeteam/type/pvp")
@Api(tags = {"免费车队类型接口"})
public class FreeTeamTypeController implements FreeTeamTypeServiceClient {

    @Autowired
    FreeTeamTypeService freeTeamTypeService;


    @ApiOperation(value = "根椐id查询免费车队类型")
    @Override
    public ResponsePacket<FreeTeamTypeDetailVO> getFreeTeamTypeById(
            @ApiParam(value = "免费车队类型id", required = true)
            @PathVariable("freeTeamTypeId") Integer freeTeamTypeId) {
        ValidateAssert.isTrue(freeTeamTypeId > 0, BizExceptionEnum.PARAM_VALID_FAIL);
        FreeTeamTypeDetailVO byFreeTeamTypeId = freeTeamTypeService.getByFreeTeamTypeId(freeTeamTypeId);
        return ResponsePacket.onSuccess(byFreeTeamTypeId);
    }

    @ApiOperation(value = "根椐id和暴鸡身份查询免费车队类型-APP调用")
    @Override
    public ResponsePacket<FreeTeamTypeDetailVO> getFreeTeamTypeDetail(
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "免费车队类型", required = true)
            @RequestBody FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, freeTeamTypeAppQueryParams);
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                freeTeamTypeAppQueryParams.getFreeTeamTypeId());
        FreeTeamTypeDetailVO detailVO = freeTeamTypeService
                .getFreeTeamTypeDetail(freeTeamTypeAppQueryParams);
        return ResponsePacket.onSuccess(detailVO);
    }

    @ApiOperation(value = "查询所有免费车队类型")
    @Override
    public ResponsePacket<List<FreeTeamTypeSimpleVO>> getAllFreeTeamType() {
        List<FreeTeamTypeSimpleVO> allFreeTeamType = freeTeamTypeService.getAll();
        return ResponsePacket.onSuccess(allFreeTeamType);
    }

    @Override
    public ResponsePacket<List<FreeTeamTypeSimpleVO>> getAllFreeTeamTypeByBaojiIdentity(
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "车队类型", required = true)
            @RequestBody FreeTeamTypeAppQueryParams freeTeamTypeAppQueryParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, freeTeamTypeAppQueryParams);
        List<FreeTeamTypeSimpleVO> simpleVOS = freeTeamTypeService
                .getSimpleByBaojiIdentity(freeTeamTypeAppQueryParams);
        return ResponsePacket.onSuccess(simpleVOS);
    }

    @ApiOperation(value = "新增车队类型")
    @Override
    public ResponsePacket<Void> addFreeTeamType(
            @ApiParam(value = "免费车队类型", required = true)
            @RequestBody FreeTeamTypeVO freeTeamTypeVO) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, freeTeamTypeVO);
        freeTeamTypeService.addFreeTeamType(freeTeamTypeVO);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "修改免费车队类型")
    @Override
    public ResponsePacket<Void> updateFreeTeamType(
            @ApiParam(value = "免费车队类型", required = true)
            @RequestBody FreeTeamTypeVO freeTeamTypeVO) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, freeTeamTypeVO);
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                freeTeamTypeVO.getFreeTeamTypeId());
        freeTeamTypeService.updateFreeTeamType(freeTeamTypeVO);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "删除免费车队类型")
    @Override
    public ResponsePacket<Void> deleteFreeTeamType(
            @ApiParam(value = "免费车队类型", required = true)
            @PathVariable("freeTeamTypeId") Integer freeTeamTypeId) {
        ValidateAssert.isTrue(freeTeamTypeId > 0, BizExceptionEnum.PARAM_VALID_FAIL);
        freeTeamTypeService.deleteFreeTeamType(freeTeamTypeId);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "查询所有上架的免费车队类型")
    @GetMapping("/allEnable")
    public ResponsePacket<List<FreeTeamTypeDetailVO>> getAllEnableFreeTeamType() {
        List<FreeTeamTypeDetailVO> detailVOList = freeTeamTypeService
                .getAllEnableFreeTeamType();
        return ResponsePacket.onSuccess(detailVOList);
    }

}
