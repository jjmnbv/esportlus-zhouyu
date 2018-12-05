package com.kaihei.esportingplus.api.feign;

import com.kaihei.esportingplus.api.params.freeteam.ShareCopywriterConfigParams;
import com.kaihei.esportingplus.api.vo.freeteam.InvitionShareConfigVo;
import com.kaihei.esportingplus.api.vo.freeteam.ShareCopywriterConfigVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "esportingplus-resource-service", path = "/task/config", fallbackFactory = TaskConfigServiceFallbackFactory.class)
public interface TaskConfigServiceClient {

    /**
     * 上下线分享邀请
     */
    @PostMapping("/share/invition/online/{id}/type/{type}")
    public ResponsePacket<Void> onlineShareInvite(
            @PathVariable("id") Integer id,
            @PathVariable("type") Integer type);

    /**
     * 修改分享任务
     */
    @PutMapping("/share/invition/update")
    public ResponsePacket<Void> updateShareInvite(
            @RequestBody InvitionShareConfigVo invitionShareConfigVo);

    /**
     * 通过分类code查找分享任务
     */
    @GetMapping("/share/invition/code/{code}")
    public ResponsePacket<InvitionShareConfigVo> findShareTaskConfig(
            @PathVariable("code") String code);

    /**
     * 通过id查询分享任务
     */
    @GetMapping("/share/invition/id/{id}")
    public ResponsePacket<InvitionShareConfigVo> findShareTaskConfigById(
            @PathVariable("id") Integer id);

    /**
     * 查询全部分享任务
     */
    @GetMapping("/share/invition/all")
    public ResponsePacket<List<InvitionShareConfigVo>> findAllShareTaskConfig();

    /**
     * App-查询全部分享任务
     */
    @GetMapping("/share/invition/app/all")
    public ResponsePacket<List<InvitionShareConfigVo>> findAllShareTaskConfigForApp(
            @RequestHeader("Authorization") String token);

    /**
     * 查询分享文案配置
     */
    @GetMapping("/share/copywriter/all")
    public ResponsePacket<List<ShareCopywriterConfigVO>> findShareCopywriterConfigForBack(
            @RequestParam(value = "scene", required = true) String scene);

    /**
     * 更新或者保存分享文案配置
     */
    @PostMapping("/share/copywriter/save")
    public ResponsePacket<Void> saveThirdShareConfig(@RequestBody ShareCopywriterConfigParams params);

    /**
     * App调用-所有分享文案配置，对APP来说，只有生效的才有意义
     */
    @GetMapping("/share/copywriter/app/all")
    public ResponsePacket<List<ShareCopywriterConfigVO>> findShareCopywriterConfigForApp(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "scene", required = true)String scene);

    /**
     * 通过id查询分享文案配置
     */
    @GetMapping("/share/copywriter/id/{id}")
    ResponsePacket<ShareCopywriterConfigVO> findShareCopywriterConfigById(@PathVariable("id") Integer id);

}
