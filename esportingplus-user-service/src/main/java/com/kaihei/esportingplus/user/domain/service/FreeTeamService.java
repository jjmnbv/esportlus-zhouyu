package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.user.api.vo.PythonFreeteamChancesInfoVo;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-09 18:13
 * @Description:
 */
public interface FreeTeamService {

    /**
     * 调用python接口为用户添加免费车队次数
     * @param uid
     * @param freeCount
     */
    boolean callPythonToIncreFreeTimes(String uid, int freeCount);

    /**
     * 调用python接口查询免费车队免单统计信息
     * @param userId
     * @return
     */
    PythonFreeteamChancesInfoVo callPythonToGetFreeChancesInfo(Integer userId);
}
