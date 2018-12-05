package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.feign.FreeTeamHomeServiceClient;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamAdvertiseHomeVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamHomeVo;
import com.kaihei.esportingplus.api.vo.freeteam.FreeTeamScrollTemplateHomeVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.resource.domain.entity.Dictionary;
import com.kaihei.esportingplus.resource.domain.service.freeteam.FreeTeamHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/freeteam/home")
@Api(tags = {"免费车队首页配置接口"})
public class FreeTeamHomeController extends AbstractDictBaseRestController<Dictionary> implements
        FreeTeamHomeServiceClient {

    @Autowired
    private FreeTeamHomeService freeTeamHomeService;
    @ApiOperation("获取所有机型首页宣传图")
    @Override
    public ResponsePacket<List<FreeTeamAdvertiseHomeVo>> findAllFreeTeamAdvertise() {
        return ResponsePacket.onSuccess(freeTeamHomeService.findHomeAdvertiseList());
    }

    @ApiOperation("新增免费车队宣传图")
    @Override
    public ResponsePacket<Void> saveFreeTeamAdvertiseHome(
            @RequestBody FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo) {
        freeTeamHomeService.saveFreeTeamAdvertiseHome(freeTeamAdvertiseHomeVo);
        return ResponsePacket.onSuccess();
    }
    @ApiOperation("编辑免费车队宣传图")
    @Override
    public ResponsePacket<Void> updateFreeTeamAdvertiseHome(
            @RequestBody FreeTeamAdvertiseHomeVo freeTeamAdvertiseHomeVo) {
        freeTeamHomeService.updateFreeTeamAdvertiseHome(freeTeamAdvertiseHomeVo);
        return ResponsePacket.onSuccess();
    }
    @ApiOperation("新增免费车队滚动配置")
    @Override
    public ResponsePacket<Void> saveFreeTeamScrollTemplate(
            @RequestBody FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo) {
        freeTeamHomeService.saveFreeTeamScrollTemplate(freeTeamScrollTemplateHomeVo);
        return ResponsePacket.onSuccess();
    }
    @ApiOperation("修改免费车队滚动配置")
    @Override
    public ResponsePacket<Void> updateFreeTeamScrollTemplate(
            @RequestBody FreeTeamScrollTemplateHomeVo freeTeamScrollTemplateHomeVo) {
        freeTeamHomeService.updateFreeTeamScrollTemplate(freeTeamScrollTemplateHomeVo);
        return ResponsePacket.onSuccess();
    }
    @ApiOperation("删除滚动模板")
    @Override
    public ResponsePacket<Void> deleteFreeTeamScrollTemplate(@PathVariable("id") Integer id) {
        freeTeamHomeService.deleteFreeTeamScrollTemplate(id);
        return ResponsePacket.onSuccess();
    }
    @ApiOperation("APP调用-根据机器类型获取宣传图")
    @Override
    public ResponsePacket<FreeTeamAdvertiseHomeVo> findFreeTeamAdvertiseForApp(@RequestHeader(value = "Authorization")String token,
            @ApiParam(value = "机器类型 0:默认配置,1:ios的x/xs版,2:ios的xsm/xr版,3:android的1080p版,4:android的18:9全面屏版", required = true) @PathVariable("machine_type")Integer machineType) {
        UserSessionContext.getUser();
        return ResponsePacket.onSuccess(freeTeamHomeService.findAdvertise(machineType));
    }
    @ApiOperation("内部调用-根据机器类型获取宣传图")
    @Override
    public ResponsePacket<FreeTeamAdvertiseHomeVo> findFreeTeamAdvertise(@ApiParam(value = "机器类型 0:默认配置,1:ios的x/xs版,2:ios的xsm/xr版,3:android的1080p版,4:android的18:9全面屏版", required = true)@PathVariable("machine_type")Integer machineType) {
        return ResponsePacket.onSuccess(freeTeamHomeService.findAdvertise(machineType));
    }
}
