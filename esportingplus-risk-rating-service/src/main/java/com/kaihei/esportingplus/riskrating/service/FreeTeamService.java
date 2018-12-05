package com.kaihei.esportingplus.riskrating.service;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamBasicParams;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamCheckParams;
import com.kaihei.esportingplus.riskrating.api.params.FreeTeamWhiteQueryParams;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamConfigVo;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import com.kaihei.esportingplus.riskrating.domain.entity.FreeTeamConfig;

import java.util.Map;

/**
 * 免费车队风控相关服务接口
 *
 * @author chenzhenjun
 */
public interface FreeTeamService {

    /**
     * 校验新用户注册是否享受奖励
     * @param freeTeamBasicParams
     * @return map
     * @throws BusinessException
     */
    public FreeTeamResponse checkRegisterReward(FreeTeamBasicParams freeTeamBasicParams) throws BusinessException;

    /**
     * 校验免费车队上车机会
     * @param freeTeamBasicParams
     * @return
     * @throws BusinessException
     */
    public FreeTeamResponse checkFreeTeamChance(FreeTeamBasicParams freeTeamBasicParams)  throws BusinessException;

    /**
     * 核减免费车队上车次数
     * @param uids
     * @throws BusinessException
     */
    public void updateTimes(String uids) throws BusinessException;

    /**
     * 恶意设备加白
     * @param deviceId
     * @throws BusinessException
     */
    public void insertWhiteList(String deviceId) throws BusinessException;

    /**
     * 查询白名单列表
     * @param queryParams
     * @return
     * @throws BusinessException
     */
    public Map<String, Object> getWhiteList(FreeTeamWhiteQueryParams queryParams) throws BusinessException;

    /**
     * 设备移出白名单
     * @param id
     * @return
     */
    public boolean deleteWhite(long id);

    /**
     * 查询免费车队风控配置
     * @return FreeTeamConfigVo
     */
    public FreeTeamConfigVo getConfig();

    /**
     * 修改免费车队风控配置
     * @param configVo
     */
    public void updateFreeTeamConfig(FreeTeamConfigVo configVo);
}
