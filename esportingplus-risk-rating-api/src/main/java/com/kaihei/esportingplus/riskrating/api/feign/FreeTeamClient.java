package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamBasicParams;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamConfigVo;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "esportingplus-risk-rating-service",
        path = "/freeteam", fallbackFactory = FreeTeamFallbackFactory.class)
public interface FreeTeamClient {

    /**
     * 用户注册绑定数美ID
     * @param freeTeamBasicParams
     * @return
     */
    @PostMapping("/reward")
    public ResponsePacket<FreeTeamResponse> checkUserRegister(
            @RequestBody FreeTeamBasicParams freeTeamBasicParams);

    /**
     * 用户发起免费车队前校验
     * @param freeTeamBasicParams
     * @return
     */
    @PostMapping("/chance")
    public ResponsePacket<FreeTeamResponse> checkFreeTeamChance(
            @RequestBody FreeTeamBasicParams freeTeamBasicParams);

    /**
     * 更新免费车队上车次数
     * @param uids
     * @return
     */
    @PostMapping("/times")
    public ResponsePacket updateChanceTimes(@RequestParam(value = "uids", required = true) String uids);

    /**
     * 恶意设备加白名单
     * @param deviceId
     * @return
     */
    @PostMapping("/white")
    public ResponsePacket insertWhiteList(@RequestParam(value = "device_id", required = true) String deviceId);

    /**
     * 白名单列表
     * @param deviceId
     * @param beginDate
     * @param endDate
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/white/devices")
    public ResponsePacket<Map<String, Object>> getWhiteList(@RequestParam(value = "device_id", required = false) String deviceId,
                                                                        @RequestParam(value = "begin_date", required = false) String beginDate,
                                                                        @RequestParam(value = "end_date", required = false) String endDate,
                                                                        @RequestParam(value = "page", defaultValue = "1") String page,
                                                                        @RequestParam(value = "size", defaultValue = "20") String size);

    /**
     * 设备移出白名单
     * @param id
     * @return
     */
    @DeleteMapping("/white/{id}")
    public ResponsePacket<Boolean> deleteWhite(@PathVariable("id") long id);

    /**
     * 获取免费车队风控配置
     * @return
     */
    @GetMapping("/config")
    public ResponsePacket<FreeTeamConfigVo> getFreeTeamConfig();

    /**
     * 修改免费车队风控配置
     * @param configVo
     * @return
     */
    @PostMapping("/config")
    public ResponsePacket updateFreeTeamConfig(@RequestBody FreeTeamConfigVo configVo);

}
