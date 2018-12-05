package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.feign.BannerConfigServiceClient;
import com.kaihei.esportingplus.api.feign.ResourceStateConfigServiceClient;
import com.kaihei.esportingplus.api.params.freeteam.BannerSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.BannerUpdateParams;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateSaveParams;
import com.kaihei.esportingplus.api.params.freeteam.ResourceStateUpdateParams;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigAppVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.BannerDictConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ResourceStateConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.resource.domain.service.freeteam.BannerConfigService;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ResourceStateConfigService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 免费车队配置
 *
 * @author
 */
@RestController
@RequestMapping("/resource/state")
@Api(tags = {"资源位配置接口"})
public class ResourceStateConfigController implements ResourceStateConfigServiceClient {

    @Autowired
    private ResourceStateConfigService resourceStateConfigService;

    /**
     * 获取资源位
     */
    @ApiOperation("获取资源位列表")
    @Override
    public ResponsePacket<List<ResourceStateConfigVo>> findResourceStateConfig(
            @ApiParam(value = "用户类型 0:全量 1:老板 2:暴鸡暴娘", required = false) @RequestParam(value = "user_type", required = false) Integer userType,
            @ApiParam(value = "投放位置代码 resource_free_team_middle:免费车队首页中", required = true)@RequestParam(value = "position", required = true) String position) {
        return ResponsePacket
                .onSuccess(resourceStateConfigService.findResourceStateConfig(userType, position));
    }

    @ApiOperation("APP调用-获取资源位列表")
    @GetMapping("/app/list")
    public ResponsePacket<List<ResourceStateConfigVo>> findResourceStateConfigForApp(
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "用户类型 0:全量 1:老板 2:暴鸡暴娘", required = true)@RequestParam(value = "user_type", required = true) Integer userType,
            @ApiParam(value = "投放位置代码 resource_free_team_middle:免费车队首页中", required = true)@RequestParam(value = "position", required = true) String position) {
        UserSessionContext.getUser();
        return ResponsePacket
                .onSuccess(resourceStateConfigService.findResourceStateConfig(userType, position));
    }

    /**
     * 获取一个资源位
     */
    @ApiOperation("获取一个资源位")
    @Override
    public ResponsePacket<ResourceStateConfigVo> findResourceStateConfigById(
            @PathVariable("resource_id") Integer resourceId) {
        return ResponsePacket
                .onSuccess(resourceStateConfigService.findResourceStateConfigById(resourceId));
    }

    /**
     * 获取一个资源位
     */
    @ApiOperation("APP调用-获取一个资源位")
    @GetMapping("/app/id/{resource_id}")
    public ResponsePacket<ResourceStateConfigVo> findResourceStateConfigByIdForApp(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("resource_id") Integer resourceId) {
        UserSessionContext.getUser();
        return ResponsePacket
                .onSuccess(resourceStateConfigService.findResourceStateConfigById(resourceId));
    }

    /**
     * 添加资源位
     */
    @ApiOperation("增加一个资源位")
    @Override
    public ResponsePacket<Void> saveResourceStateConfig(
            @RequestBody ResourceStateSaveParams resourceStateSaveParams) {
        resourceStateConfigService.saveResourceStateConfig(resourceStateSaveParams);
        return ResponsePacket.onSuccess();
    }

    /**
     * 修改资源位
     */
    @ApiOperation("修改一个资源位")
    @Override
    public ResponsePacket<Void> updateResourceStateConfig(
            @RequestBody ResourceStateUpdateParams resourceStateUpdateParams) {
        resourceStateConfigService.updateResourceStateConfig(resourceStateUpdateParams);
        return ResponsePacket.onSuccess();
    }

    /**
     * 删除资源位
     */
    @ApiOperation("删除一个资源位")
    @Override
    public ResponsePacket<Void> deleteResourceStateConfig(
            @PathVariable("resource_id") Integer resourceId) {
        resourceStateConfigService.deleteResourceStateConfig(resourceId);
        return ResponsePacket.onSuccess();
    }
}
