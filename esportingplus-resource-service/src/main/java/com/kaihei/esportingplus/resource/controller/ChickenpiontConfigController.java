package com.kaihei.esportingplus.resource.controller;

import com.kaihei.esportingplus.api.feign.ChickenpointConfigServiceClient;
import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointTaskConfigVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ChickenpointTaskConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 免费车队配置
 *
 * @author
 */
@RestController
@RequestMapping("/chickenpoint")
@Api(tags = {"鸡分配置接口"})
public class ChickenpiontConfigController implements ChickenpointConfigServiceClient {

    @Autowired
    ChickenpointTaskConfigService chickenpointTaskConfigService;

    /**
     * 新增鸡分任务
     */
    @ApiOperation(value = "新增鸡分任务")
    @Override
    public ResponsePacket<Void> saveChickpointTaskConfig(@RequestBody ChickenpointTaskConfigVo vo) {
        chickenpointTaskConfigService.save(vo);
        return ResponsePacket.onSuccess();
    }

    /**
     * 修改鸡分任务
     */
    @ApiOperation(value = "修改鸡分任务")
    @Override
    public ResponsePacket<Void> updateChickpointTaskConfig(
            @RequestBody ChickenpointTaskConfigVo vo) {
        chickenpointTaskConfigService.update(vo);
        return ResponsePacket.onSuccess();
    }

    /**
     * 获取鸡分任务列表
     */
    @ApiOperation(value = "获取鸡分任务列表")
    @Override
    public ResponsePacket<List<ChickenpointTaskConfigVo>> findChickpointTaskConfig() {
        return ResponsePacket.onSuccess(chickenpointTaskConfigService.findChickpointTaskConfig());
    }

    /**
     * APP-获取鸡分任务列表
     */
    @ApiOperation(value = "APP调用-获取鸡分任务列表")
    @Override
    public ResponsePacket<List<ChickenpointTaskConfigVo>> findChickpointTaskConfigForApp(
            @RequestHeader("Authorization") String token) {
        UserSessionContext.getUser();
        return ResponsePacket
                .onSuccess(chickenpointTaskConfigService.findEfficientChickpointTaskConfig());
    }

    /**
     * 删除鸡分任务
     */
    @ApiOperation(value = "删除鸡分任务")
    @Override
    public ResponsePacket<Void> deleteChickpointTaskConfig(@PathVariable("id") Long id) {
        chickenpointTaskConfigService.delete(id);
        return ResponsePacket.onSuccess();
    }
}
