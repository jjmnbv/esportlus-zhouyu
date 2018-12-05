package com.kaihei.esportingplus.core.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.enums.AppVersionSaveTypeEnum;
import com.kaihei.esportingplus.core.api.feign.AppVersionServiceClient;
import com.kaihei.esportingplus.core.api.params.PageParams;
import com.kaihei.esportingplus.core.api.vo.AppVersionChangelogVo;
import com.kaihei.esportingplus.core.api.vo.AppVersionNotifyVo;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import com.kaihei.esportingplus.core.domain.service.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 版本日志记录
 *
 * @author yangshidong
 * @date 2018/12/1
 */
@RestController
@RequestMapping("/version")
@Api(tags = {"版本日志记录"})
public class AppVersionController implements AppVersionServiceClient {
    @Autowired
    private AppVersionService appVersionService;

    @ApiOperation(value = "保存版本日志记录")
    @Override
    public ResponsePacket saveVersionLog(@RequestBody AppVersionChangelogVo appVersionChangelogVo) {
        appVersionService.saveAppVersion(appVersionChangelogVo, AppVersionSaveTypeEnum.SAVE);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "更新版本日志记录")
    @Override
    public ResponsePacket updateVersionLog(@RequestBody AppVersionChangelogVo appVersionChangelogVo) {
        appVersionService.saveAppVersion(appVersionChangelogVo, AppVersionSaveTypeEnum.UPDATE);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "启用版本日志记录")
    @Override
    public ResponsePacket enableVersionLog(@PathVariable(value = "versionLogId") int versionLogId) {
        AppVersionChangelogVo appVersionChangelogVo = new AppVersionChangelogVo();
        appVersionChangelogVo.setId(versionLogId);
        appVersionService.saveAppVersion(appVersionChangelogVo, AppVersionSaveTypeEnum.ENABLE);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "停用版本日志记录")
    @Override
    public ResponsePacket disableVersionLog(@PathVariable(value = "versionLogId") int versionLogId) {
        AppVersionChangelogVo appVersionChangelogVo = new AppVersionChangelogVo();
        appVersionChangelogVo.setId(versionLogId);
        appVersionService.saveAppVersion(appVersionChangelogVo, AppVersionSaveTypeEnum.DISABLE);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "删除版本日志记录")
    @Override
    public ResponsePacket deleteVersionLog(@PathVariable(value = "versionLogId") int versionLogId) {
        AppVersionChangelogVo appVersionChangelogVo = new AppVersionChangelogVo();
        appVersionChangelogVo.setId(versionLogId);
        appVersionService.saveAppVersion(appVersionChangelogVo, AppVersionSaveTypeEnum.DELETE);
        return ResponsePacket.onSuccess();
    }

    @ApiOperation(value = "获取版本日志记录列表")
    @Override
    public ResponsePacket<PageInfo> getVersionLogList(@RequestBody PageParams pageParams) {
        return ResponsePacket.onSuccess(appVersionService.getAppVersionList(pageParams));
    }

    @ApiOperation(value = "获取版本升级弹窗信息")
    @Override
    public ResponsePacket<AppVersionNotifyVo> notifyAppUpdate(@RequestParam(value = "client_type", required = true) short clientType,
                                                              @RequestParam(value = "version", required = true) String version) {
        return ResponsePacket.onSuccess(appVersionService.getVersionNotify(clientType, version));
    }
}
