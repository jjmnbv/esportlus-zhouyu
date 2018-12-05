package com.kaihei.esportingplus.core.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.params.PageParams;
import com.kaihei.esportingplus.core.api.vo.AppVersionChangelogVo;
import com.kaihei.esportingplus.core.api.vo.AppVersionNotifyVo;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 版本更新管理服务类
 *
 * @author yangshidong
 * @date 2018/12/1
 **/
@FeignClient(name = "esportingplus-core-service", path = "/version", fallbackFactory = SMSServiceClientFallback.class)
public interface AppVersionServiceClient {
    /**
     * 保存版本日志信息
     *
     * @param appVersionChangelogVo
     * @return
     */
    @PostMapping("/save")
    ResponsePacket saveVersionLog(@RequestBody AppVersionChangelogVo appVersionChangelogVo);

    /**
     * 更新版本日志信息
     *
     * @param appVersionChangelogVo
     * @return
     */
    @PostMapping("/update")
    ResponsePacket updateVersionLog(@RequestBody AppVersionChangelogVo appVersionChangelogVo);

    /**
     * 启用版本日志
     *
     * @param versionLogId
     * @return
     */
    @PostMapping("/enable/{versionLogId}")
    ResponsePacket enableVersionLog(@PathVariable(value = "versionLogId") int versionLogId);

    /**
     * 停用版本日志
     *
     * @param versionLogId
     * @return
     */
    @PostMapping("/disable/{versionLogId}")
    ResponsePacket disableVersionLog(@PathVariable(value = "versionLogId") int versionLogId);

    /**
     * 删除版本日志
     *
     * @param versionLogId
     * @return
     */
    @DeleteMapping("/delete/{versionLogId}")
    ResponsePacket deleteVersionLog(@PathVariable(value = "versionLogId") int versionLogId);

    /**
     * 获取版本日志列表
     *
     * @param pageParams
     * @return
     */
    @PostMapping("/list")
    ResponsePacket<PageInfo> getVersionLogList(@RequestBody PageParams pageParams);

    /**
     * 获取版本更新弹窗通知
     *
     * @param clientType 客户端类型(1:安卓 2:ios)
     * @param version 当前版本
     */
    @GetMapping("/notify")
    ResponsePacket<AppVersionNotifyVo> notifyAppUpdate(@RequestParam(value = "client_type", required = true) short clientType,
                                                       @RequestParam(value = "version", required = true) String version);

}
