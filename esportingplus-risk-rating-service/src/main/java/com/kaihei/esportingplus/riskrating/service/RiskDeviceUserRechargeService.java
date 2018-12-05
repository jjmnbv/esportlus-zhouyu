package com.kaihei.esportingplus.riskrating.service;

import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceUserLogQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskDeviceUserRechargeLogVo;
import com.kaihei.esportingplus.riskrating.api.vo.RiskDeviceWhiteVo;
import org.springframework.data.domain.Page;

public interface RiskDeviceUserRechargeService {

    /**
     * 分页查询设备充值记录
     * @param params
     * @return
     */
    public Page<RiskDeviceUserRechargeLogVo> findRiskDeviceRechargeLogByPage(RiskDeviceUserLogQueryParams params);

    /**
     * 分页查询设备白名单
     * @param params
     * @return
     */
    public Page<RiskDeviceWhiteVo> findRiskDeviceBindByPage(RiskDeviceWhiteQueryParams params);

    /**
     * 通过id查询设备白名单
     * @param id
     * @return
     */
    public RiskDeviceWhiteVo findRiskDeviceWhiteVoById(Long id);

    /**
     * 增加一个设备白名单
     * @param params
     */
    public void saveRiskDeviceWhite(RiskDeviceWhiteUpdateParams params);

    /**
     * 修改设备白名单
     * @param params
     */
    public void updateRiskDeviceWhite(RiskDeviceWhiteUpdateParams params);

}
