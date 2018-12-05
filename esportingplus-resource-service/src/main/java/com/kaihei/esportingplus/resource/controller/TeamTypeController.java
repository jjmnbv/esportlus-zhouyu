package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.feign.TeamTypeServiceClient;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeAppQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeBackendQueryParams;
import com.kaihei.esportingplus.api.params.freeteam.TeamTypeParams;
import com.kaihei.esportingplus.api.vo.freeteam.CreateTeamGameTeamTypeVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeDetailVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamTypeSimpleVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.data.repository.teamtype.TeamTypeRepository;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.TeamType;
import com.kaihei.esportingplus.resource.domain.service.freeteam.TeamTypeService;
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
 * 车队类型配置
 * @author liangyi
 */
@RestController
@RequestMapping("/teamtype")
@Api(tags = {"车队类型配置接口"})
public class TeamTypeController implements TeamTypeServiceClient {

    @Autowired
    TeamTypeService teamTypeService;

    @Autowired
    private TeamTypeRepository teamTypeRepository;

    @Autowired
    private DictionaryDictManager dictionaryDictManager;

    @Override
    @GetMapping("/teamgame/{teamTypeId}")
    public ResponsePacket<CreateTeamGameTeamTypeVO> getCreateTeamGameTeamTypeVOById(
            @PathVariable("teamTypeId") Integer teamTypeId) {
        TeamType teamType = teamTypeRepository.selectByPrimaryKey(teamTypeId);
        String gameName = dictionaryDictManager.findById(teamType.getGameId()).getName();
        Integer baojiIdentityId = teamType.getBaojiIdentityId();

        String baojiIdentityCode = dictionaryDictManager.findById(baojiIdentityId).getCode();

        CreateTeamGameTeamTypeVO vo = CreateTeamGameTeamTypeVO.builder()
                .maxPositionCount(teamType.getMaxPositionCount())
                .gameId(teamType.getGameId())
                .userIdentityCode(baojiIdentityCode)
                .gameName(gameName)
                .displayName(String.format("%s-%s", gameName, teamType.getName())).build();

        return ResponsePacket.onSuccess(vo);
    }


    @ApiOperation(value = "根椐id查询车队类型")
    @Override
    public ResponsePacket<TeamTypeDetailVO> getTeamTypeById(
            @ApiParam(value = "车队类型id", required = true)
            @PathVariable("teamTypeId") Integer teamTypeId) {
        ValidateAssert.isTrue(teamTypeId > 0, BizExceptionEnum.PARAM_VALID_FAIL);
        TeamTypeDetailVO teamTypeDetailVO = teamTypeService.getTypeById(teamTypeId);
        return ResponsePacket.onSuccess(teamTypeDetailVO);
    }

    @ApiOperation(value = "根椐id和暴鸡身份查询免费车队类型-APP调用")
    @Override
    public ResponsePacket<TeamTypeDetailVO> getTeamTypeDetail(
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "游戏代码", required = true)
            @RequestBody TeamTypeAppQueryParams teamTypeAppQueryParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, teamTypeAppQueryParams);
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                teamTypeAppQueryParams.getTeamTypeId());
        TeamTypeDetailVO teamTypeDetailVO =
                teamTypeService.getTypeDetail(teamTypeAppQueryParams);
        return ResponsePacket.onSuccess(teamTypeDetailVO);
    }

    @ApiOperation(value = "分页所有车队类型")
    @Override
    public ResponsePacket<PagingResponse<TeamTypeSimpleVO>> getTeamTypeByPage(
            @ApiParam(value = "查询参数", required = false)
            @RequestBody TeamTypeBackendQueryParams teamTypeBackendQueryParams) {
        PagingResponse<TeamTypeSimpleVO> pagingResponse = teamTypeService
                .getAllByPage(teamTypeBackendQueryParams);
        return ResponsePacket.onSuccess(pagingResponse);
    }

    @Override
    public ResponsePacket<List<TeamTypeSimpleVO>> getAllTeamTypeByBaojiIdentity(
            @RequestHeader(value = "Authorization") String token,
            @ApiParam(value = "车队类型", required = true)
            @RequestBody TeamTypeAppQueryParams teamTypeAppQueryParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, teamTypeAppQueryParams);
        List<TeamTypeSimpleVO> teamTypeSimpleVOS =
                teamTypeService.getSimpleByBaojiIdentity(teamTypeAppQueryParams);
        return ResponsePacket.onSuccess(teamTypeSimpleVOS);
    }

    @ApiOperation(value = "新增车队类型")
    @Override
    public ResponsePacket<Void> addTeamType(
            @ApiParam(value = "车队类型", required = true)
            @RequestBody TeamTypeParams teamTypeParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, teamTypeParams);
        teamTypeService.addTeamType(teamTypeParams);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "修改车队类型")
    @Override
    public ResponsePacket<Void> updateTeamType(
            @ApiParam(value = "车队类型", required = true)
            @RequestBody TeamTypeParams teamTypeParams) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, teamTypeParams);
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,
                teamTypeParams.getTeamTypeId());
        teamTypeService.updateTeamType(teamTypeParams);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "删除车队类型")
    @Override
    public ResponsePacket<Void> deleteTeamType(
            @ApiParam(value = "车队类型", required = true)
            @PathVariable("teamTypeId") Integer teamTypeId) {
        ValidateAssert.isTrue(teamTypeId > 0, BizExceptionEnum.PARAM_VALID_FAIL);
        teamTypeService.deleteTeamType(teamTypeId);
        return ResponsePacket.onSuccess();
    }

}
