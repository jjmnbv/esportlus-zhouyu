package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.ImMachineUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 风控需求-IM防骚扰及虚拟机注册判断
 * @author chenzhenjun
 */
@FeignClient(name = "esportingplus-risk-rating-service",
        path = "/im_machine", fallbackFactory = ImMachineBackFallbackFactory.class)
public interface ImMachineBackendClient {

    /**
     * 获取IM防骚扰及虚拟机开关配置
     * @return
     */
    @GetMapping("/setting")
    public ResponsePacket<ImMachineConfigVo> getImMachineConfig();

    /**
     * 修改防骚扰及虚拟机判断开关配置
     * @param configVo
     * @return
     */
    @PostMapping("/setting")
    public ResponsePacket updateImMachineConfig(
            @RequestBody ImMachineConfigVo configVo);

    /**
     * 查询数美ID黑名单列表
     * @param deviceId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/device/blacklists")
    public ResponsePacket<PageInfo<ImMachineListVo>> getDeviceBlackList(@RequestParam(value = "device_id", required = false) String deviceId,
                                                                       @RequestParam(value = "page", defaultValue = "1") String page,
                                                                       @RequestParam(value = "size", defaultValue = "20") String size);

    /**
     * 添加数美ID至黑名单
     * @param listVo
     * @return
     */
    @PostMapping("/device/blacklist")
    public ResponsePacket insertDeviceBlack(@RequestBody ImMachineListVo listVo);

    /**
     * 数美ID移出黑名单
     * @param id
     * @return
     */
    @DeleteMapping("/device/blacklist/{id}")
    public ResponsePacket deleteDeviceBlack(@PathVariable("id") long id);

    /**
     * 查询用户数美ID绑定关系
     * @param type
     * @param column
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/relations")
    public ResponsePacket<PageInfo<ImMachineListVo>> getUserDeviceBindList(@RequestParam(value = "type", required = true) String type,
                                                                     @RequestParam(value = "column", required = false) String column,
                                                                  @RequestParam(value = "page", defaultValue = "1") String page,
                                                                  @RequestParam(value = "size", defaultValue = "20") String size);

    /**
     * 用户数美ID关系解绑
     * @param listVo
     * @return
     */
    @PostMapping("/relation")
    public ResponsePacket unbindUserDeviceRelation(@RequestBody ImMachineListVo listVo);


    /**
     * 查询IM消息骚扰黑名单列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/user/blacklists")
    public ResponsePacket<PageInfo<ImMachineListVo>> getUserImBlackList(@RequestParam(value = "user_id", required = false) String userId,
                                                                  @RequestParam(value = "page", defaultValue = "1") String page,
                                                                  @RequestParam(value = "size", defaultValue = "20") String size);

    /**
     * 将用户从IM消息骚扰黑名单移出
     * @param id
     * @return
     */
    @DeleteMapping("/user/blacklist/{id}")
    public ResponsePacket deleteUserImBlack(@PathVariable("id") long id);

    /**
     * 用户注册登陆风控校验
     * @param imMachineUpdateParams
     * @return
     */
    @PostMapping("/login_register")
    public ResponsePacket<FreeTeamResponse> loginRegisterCheck(
            @RequestBody ImMachineUpdateParams imMachineUpdateParams);

    /**
     * JAVA调用-生成uid前校验注册
     * @pa
     * @return
     */
    @GetMapping("/register_check")
    public ResponsePacket<FreeTeamResponse> registerCheckBeforeGenerateUserId(
            @RequestParam(value = "device_id", required = true) String deviceId,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "version", required = false) String version);

    @GetMapping("/device/behaviour")
    public ResponsePacket<UserDeviceBehaviourRecordVo> getBehaviourRecordInfo(@RequestParam(value = "user_id", required = true) String userId);


    @PostMapping("/device/behaviour")
    public ResponsePacket updateBehaviourRecord(@RequestBody UserDeviceBehaviourRecordVo vo);


}
