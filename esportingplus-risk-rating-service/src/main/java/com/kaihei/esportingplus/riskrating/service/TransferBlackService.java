package com.kaihei.esportingplus.riskrating.service;

import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import com.kaihei.esportingplus.riskrating.api.vo.ImMachineListVo;
import com.kaihei.esportingplus.riskrating.api.vo.PageInfo;
import com.kaihei.esportingplus.riskrating.api.vo.TransferBlackListVo;

/**
 * 提现黑名单-Service
 */
public interface TransferBlackService {

    /**
     * 提现黑名单列表
     * @param deviceId
     * @param page
     * @param size
     * @return
     */
    PageInfo<TransferBlackListVo> getTransferBlackList(String deviceId, String page, String size);

    /**
     * 拉黑
     * @param userId
     * @param remark
     */
    void insertTransferBlack(String userId, String remark);

    /**
     * 移出
     * @param userId
     */
    void deleteTransferBlack(String userId);

    /**
     * 提现验证-用户是否在黑名单中
     * @param ids
     * @return
     */
    FreeTeamResponse checkTransfer(String ids);
}
