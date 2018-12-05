package com.kaihei.esportingplus.resource.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaihei.esportingplus.api.enums.DictionaryCategoryCodeEnum;
import com.kaihei.esportingplus.api.enums.DictionaryCodeEnum;
import com.kaihei.esportingplus.api.feign.TaskConfigServiceClient;
import com.kaihei.esportingplus.api.params.freeteam.ShareCopywriterConfigParams;
import com.kaihei.esportingplus.api.vo.freeteam.InvitionShareConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ShareCopywriterConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.enums.StatusEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.resource.data.manager.impl.DictionaryDictManager;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ShareCopywriterConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"任务配置接口"})
@RestController
@RequestMapping("/task/config")
@Slf4j
public class TaskConfigController extends AbstractDictBaseRestController<Dictionary> implements
        TaskConfigServiceClient {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    DictionaryDictManager dictionaryDictManager;
    @Autowired
    private ShareCopywriterConfigService shareCopywriterConfigService;

    /**
     * 上下线分享邀请
     */
    @Override
    @ApiOperation(value = "分享邀请任务上下架")
    public ResponsePacket<Void> onlineShareInvite(
            @ApiParam(value = "字典id", required = true) @PathVariable("id") Integer id,
            @ApiParam(value = "0：下架，1：上架", required = true) @PathVariable("type") Integer type) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        Dictionary dictionary = suportManager.findById(id);
        //如果不是分享任务直接拒绝
        ValidateAssert.isTrue(DictionaryCategoryCodeEnum.SHARE_INVITE.getCode()
                .equals(dictionary.getCode()), BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_MATCH);
        String value = dictionary.getValue();
        try {
            Map<String, Object> valueMap = objectMapper
                    .readValue(value,
                            new TypeReference<Map<String, Object>>() {
                            });
            //1为上架
            if (type == 1) {
                ValidateAssert
                        .isFalse("1".equals(valueMap.get("status").toString()),
                                BizExceptionEnum.SHARE_INVITE_ALREADY_ONLINE);
                valueMap.put("status", 1);
                valueMap.put("online_time",
                        DateUtil.dateTime2Str(LocalDateTime.now(), DateUtil.FORMATTER));

            } else {
                //下架
                valueMap.put("status", 0);
            }
            value = objectMapper.writeValueAsString(valueMap);
            dictionary.setValue(value);
            suportManager.updateById(dictionary);
            return ResponsePacket.onSuccess();
        } catch (IOException e) {
            throw new BusinessException(BizExceptionEnum.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 新用户注册奖励value值：{"task_name":"新用户注册奖励","reward_free_count":3,"gmt_modified":"2018-10-23
     * 16:36:53","status":1,"online_time":"2018-10-23 10:43:54"} 用户启动奖励：{"task_name":"用户启动奖励","reward_free_count":3,"reward_days":7,"gmt_modified":"2018-10-23
     * 16:36:53","status":1,"online_time":"2018-10-23 10:43:54"} 邀请好友奖励value值：{"task_name":"邀请好友注册","task_title":"邀请新人好友使用暴鸡电竞","invite_count":1,"reward_free_count":3,"icon":"www.baidu.com","gmt_modified":"2018-10-10
     * 10:00:00"} 好友消费value值：{"task_name":"好友消费奖励暴鸡币","task_title":"新人好友每次消费10暴鸡币","consume_amount":60,"reward_amount":10,"icon":"www.baidu.com","gmt_modified":"2018-10-10
     * 10:00:00"} 邀请好友完成车队value值：{"task_name":"邀请好友完成车队奖励","task_title":"新人好友完成免费车队","finish_count":1,"reward_free_count":3,"icon":"www.baidu.com","gmt_modified":"2018-10-10
     * 10:00:00"} 好友成功邀请新人value值：{"task_name":"好友成功邀请新人","task_title":"新人好友完成免费车队","invite_count":2,"reward_free_count":1,"icon":"www.baidu.com","gmt_modified":"2018-10-10
     * 10:00:00"}
     */
    @Override
    @ApiOperation(value = "分享邀请任务修改")
    public ResponsePacket<Void> updateShareInvite(
            @RequestBody InvitionShareConfigVo invitionShareConfigVo) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        Dictionary oldDictionary = suportManager.findById(invitionShareConfigVo.getId());
        ValidateAssert.isTrue(DictionaryCategoryCodeEnum.SHARE_INVITE.getCode()
                .equals(oldDictionary.getDictionaryCategory().getCode()), BizExceptionEnum.DICT_CATEGORY_CONFIG_NOT_MATCH);
        InvitionShareConfigVo oldInvitionShareConfigVo = JacksonUtils
                .toBeanWithSnake(oldDictionary.getValue(), InvitionShareConfigVo.class);
        invitionShareConfigVo.setStatus(oldInvitionShareConfigVo.getStatus());
        invitionShareConfigVo.setOnlineTime(oldInvitionShareConfigVo.getOnlineTime());
        invitionShareConfigVo.setId(null);
        invitionShareConfigVo.setCode(null);
        invitionShareConfigVo.setCategoryCode(null);
        invitionShareConfigVo.setGmtModified(new Date());
        oldDictionary.setValue(JacksonUtils.toJsonWithSnakeAndNoNull(invitionShareConfigVo));
        suportManager.updateById(oldDictionary);
        return ResponsePacket.onSuccess();
    }

    @Override
    @ApiOperation(value = "查询某类型分享邀请任务配置")
    public ResponsePacket<InvitionShareConfigVo> findShareTaskConfig(
            @PathVariable("code") String code) {
        return ResponsePacket
                .onSuccess(this.findShareTaskConfigByCode(code));
    }

    @Override
    @ApiOperation(value = "根据id查询某邀请任务")
    public ResponsePacket<InvitionShareConfigVo> findShareTaskConfigById(
            @PathVariable("id") Integer id) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        Dictionary dictionary = suportManager.findById(id);
        return ResponsePacket
                .onSuccess(this.convertInvitionShareConfigVo(dictionary));
    }

    @Override
    @ApiOperation(value = "查询全部分享邀请任务配置")
    public ResponsePacket<List<InvitionShareConfigVo>> findAllShareTaskConfig() {

        return ResponsePacket.onSuccess(this.findAllShareTaskConfigToVo());
    }

    @ApiOperation(value = "APP调用-查询全部分享邀请任务配置")
    @Override
    public ResponsePacket<List<InvitionShareConfigVo>> findAllShareTaskConfigForApp(
            @RequestHeader("Authorization") String token) {
        UserSessionContext.getUser();
        List<InvitionShareConfigVo> vos = this.findAllShareTaskConfigToVo();
        //过滤掉下架的任务
        return ResponsePacket.onSuccess(
                vos.stream()
                        .filter(it -> (it.getStatus() == 1) && (
                                !DictionaryCodeEnum.USER_REGISTER_REWARD
                                        .getCode().equals(it.getCode())
                                        && !DictionaryCodeEnum.USER_START_UP_REWARD
                                        .getCode().equals(it.getCode())))
                        .collect(Collectors.toList()));
    }

    @ApiOperation(value = "后台调用-获取分享文案配置")
    @Override
    public ResponsePacket<List<ShareCopywriterConfigVO>> findShareCopywriterConfigForBack(
            @RequestParam(value = "scene", required = true) String scene) {
        return ResponsePacket
                .onSuccess(shareCopywriterConfigService.findShareCopywriterConfig(scene));
    }

    @ApiOperation(value = "更新或者保存分享文案配置")
    @Override
    public ResponsePacket<Void> saveThirdShareConfig(
            @RequestBody ShareCopywriterConfigParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, params);
        //如果id不为空，视为修改
        if (params.getShareId() != null && params.getShareId() != 0) {
            shareCopywriterConfigService.updateShareCopywriterConfig(params);
        } else {
            shareCopywriterConfigService.saveShareCopywriterConfig(params);
        }
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "APP调用-获取分享文案配置")
    @Override
    public ResponsePacket<List<ShareCopywriterConfigVO>> findShareCopywriterConfigForApp(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "scene", required = true) String scene) {
        UserSessionContext.getUser();
        List<ShareCopywriterConfigVO> list = shareCopywriterConfigService
                .findShareCopywriterConfig(scene).stream()
                .filter(it -> it.getStatus().intValue() == StatusEnum.ENABLE.getCode())
                .collect(Collectors.toList());
        return ResponsePacket.onSuccess(list);
    }

    @ApiOperation(value = "通过id查找分享文案配置")
    @Override
    public ResponsePacket<ShareCopywriterConfigVO> findShareCopywriterConfigById(
            @PathVariable("id") Integer id) {
        return ResponsePacket.onSuccess(shareCopywriterConfigService.findById(id));
    }

    private List<InvitionShareConfigVo> findAllShareTaskConfigToVo() {
        List<InvitionShareConfigVo> list = new ArrayList<>();
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        List<Dictionary> dictionaryList = suportManager
                .findByCategoryCode(DictionaryCategoryCodeEnum.SHARE_INVITE.getCode());
        return dictionaryList.stream().map(it -> convertInvitionShareConfigVo(it))
                .collect(Collectors.toList());
    }

    private InvitionShareConfigVo findShareTaskConfigByCode(String code) {
        DictionaryDictManager suportManager = (DictionaryDictManager) getSuportManager();
        Dictionary dictionary = suportManager
                .findByCodeAndCategoryCode(code, DictionaryCategoryCodeEnum.SHARE_INVITE.getCode());
        return this.convertInvitionShareConfigVo(dictionary);
    }

    private InvitionShareConfigVo convertInvitionShareConfigVo(Dictionary dictionary) {
        if (dictionary != null) {
            InvitionShareConfigVo invitionShareConfigVo = JacksonUtils
                    .toBeanWithSnake(dictionary.getValue(), InvitionShareConfigVo.class);
            invitionShareConfigVo.setCategoryCode(dictionary.getDictionaryCategory().getCode());
            invitionShareConfigVo.setCode(dictionary.getCode());
            invitionShareConfigVo.setId(dictionary.getId());
            return invitionShareConfigVo;
        }
        return new InvitionShareConfigVo();
    }
}
