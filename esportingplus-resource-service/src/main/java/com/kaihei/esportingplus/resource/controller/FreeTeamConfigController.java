package com.kaihei.esportingplus.resource.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.enums.DictionaryCodeEnum;
import com.kaihei.esportingplus.api.feign.FreeTeamConfigServiceClient;
import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.freeteam.DictFreeTeamConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamConfigVO;
import com.kaihei.esportingplus.api.vo.freeteam.TeamUserWhiteListVO;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.data.repository.teamtype.TeamUserWhiteListRepository;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.entity.freeteam.TeamUserWhiteList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 免费车队配置
 * @author liangyi
 */
@RestController
@RequestMapping("/freeteam")
@Api(tags = {"免费车队配置接口"})
public class FreeTeamConfigController implements FreeTeamConfigServiceClient {

    @Autowired
    DictionaryDictManager dictionaryDictManager;
    @Autowired
    TeamUserWhiteListRepository teamUserWhiteListRepository;

    @Override
    @ApiOperation(value = "获取免费车队配置")
    public ResponsePacket<?> getFreeTeamConfig() {
        Dictionary dictionary = dictionaryDictManager.findByCodeAndCategoryCode(
                DictionaryCodeEnum.FREE_TEAM_CONFIG_CODE.getCode(),
                DictionaryCategoryCodeEnum.FREE_TEAM_CONFIG.getCode());
        DictBaseVO tc = BeanMapper.map(dictionary, DictBaseVO.class);
        String tcStr = tc.getValue().toString();
        FreeTeamConfigVO freeTeamConfigVO = JacksonUtils
                .toBeanWithSnake(tcStr, FreeTeamConfigVO.class);
        tc.setValue(freeTeamConfigVO);
        return ResponsePacket.onSuccess(tc);
    }

    @ApiOperation(value = "修改免费车队配置")
    @Override
    public ResponsePacket<Void> updateFreeTeamConfig(
            @ApiParam(value = "修改免费车队配置", required = true)
            @RequestBody DictFreeTeamConfigVO dictFreeTeamConfigVO) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, dictFreeTeamConfigVO);
        Dictionary dictionary = new Dictionary();
        dictionary.setId(dictFreeTeamConfigVO.getDictId());
        dictionary.setValue(JacksonUtils.toJsonWithSnake(dictFreeTeamConfigVO.getValue()));
        dictionaryDictManager.updateById(dictionary);
        return ResponsePacket.onSuccess();
    }


    @ApiOperation(value = "分页查询用户免费车队白名单")
    @Override
    public ResponsePacket<PagingResponse<TeamUserWhiteListVO>> getAllFreeTeamUserWhiteList(
            @ApiParam(value = "分页参数", required = false)
            @RequestBody PagingRequest pagingRequest) {
        Page<TeamUserWhiteListVO> page = PageHelper
                .startPage(pagingRequest.getOffset(), pagingRequest.getLimit())
                .doSelectPage(() -> teamUserWhiteListRepository.selectAllUserWhiteList());
        PagingResponse<TeamUserWhiteListVO> pagingResponse = new PagingResponse<>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), page.getResult());
        return ResponsePacket.onSuccess(pagingResponse);
    }

    @ApiOperation(value = "添加用户白名单")
    @Override
    public ResponsePacket<Integer> addBatchFreeTeamUserWhiteList(
            @ApiParam(value = "白名单uid集合", required = true)
            @RequestBody List<String> uidList) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, uidList);
        teamUserWhiteListRepository.addBatchUserWhiteList(uidList);
        return ResponsePacket.onSuccess(uidList.size());
    }

    @ApiOperation(value = "删除用户白名单")
    @Override
    public ResponsePacket<Void> deleteFreeTeamUserWhiteList(
            @ApiParam(value = "白名单记录id", required = true)
            @PathVariable("freeTeamUserWhiteListId") Integer freeTeamUserWhiteListId) {
        ValidateAssert.isTrue(freeTeamUserWhiteListId > 0,
                BizExceptionEnum.PARAM_VALID_FAIL);
        teamUserWhiteListRepository.deleteByPrimaryKey(freeTeamUserWhiteListId);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "校验用户是否在白名单中")
    @Override
    public ResponsePacket<Boolean> checkUserInFreeTeamUserWhiteList(
            @ApiParam(value = "要校验的 uid", required = true)
            @PathVariable("uid") String uid) {
        // 获取免费车队配置
        Dictionary dict = dictionaryDictManager
                .findByCodeAndCategoryCode(DictionaryCodeEnum.FREE_TEAM_CONFIG_CODE.getCode(),
                        DictionaryCategoryCodeEnum.FREE_TEAM_CONFIG.getCode());
        FreeTeamConfigVO freeTeamConfigVO = JacksonUtils
                .toBeanWithSnake(dict.getValue(), FreeTeamConfigVO.class);
        boolean isInWhiteList = true;
        if (freeTeamConfigVO.getWhiteListSwitch() == StatusEnum.ENABLE.getCode()) {
            // 免费车队白名单开关已开启, 查询用户是否在白名单中
            TeamUserWhiteList teamUserWhiteList = teamUserWhiteListRepository
                    .selectUserWhiteListByUid(uid);
            isInWhiteList = ObjectTools.isNotNull(teamUserWhiteList);
        }
        return ResponsePacket.onSuccess(isInWhiteList);
    }
}
