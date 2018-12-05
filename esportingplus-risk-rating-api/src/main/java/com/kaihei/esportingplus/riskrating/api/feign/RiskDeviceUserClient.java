package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceUserLogQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskDeviceWhiteVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(name = "esportingplus-risk-rating-service",
        path = "/risk/device/user", fallbackFactory = RiskDeviceUserFallbackFactory.class)
public interface RiskDeviceUserClient {
    /**
     * 分页查询设备充值记录
     * @param params
     * @return
     */
    @PostMapping("/recharge/log")
    public ResponsePacket<Object> findRiskDeviceRechargeLogByPage(@RequestBody RiskDeviceUserLogQueryParams params);

    /**
     * 分页查询设备白名单
     * @param params
     * @return
     */
    @PostMapping("/device/white")
    public ResponsePacket<Object> findRiskDeviceBindByPage(@RequestBody RiskDeviceWhiteQueryParams params);

    /**
     * 通过id查询设备白名单
     * @param id
     * @return
     */
    @GetMapping("/device/white/{id}")
    public ResponsePacket<RiskDeviceWhiteVo> findRiskDeviceWhiteVoById(@PathVariable("id")Long id);

    /**
     * 增加一个设备白名单
     * @param params
     */
    @PostMapping ("/device/white/save")
    public ResponsePacket<Void> saveRiskDeviceWhite(@RequestBody RiskDeviceWhiteUpdateParams params);

    /**
     * 修改设备白名单
     * @param params
     */
    @PutMapping ("/device/white/update")
    public ResponsePacket<Void> updateRiskDeviceWhite(@RequestBody RiskDeviceWhiteUpdateParams params);
}
