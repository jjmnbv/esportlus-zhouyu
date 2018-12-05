package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.user.api.vo.UserBalanceResutVo;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/10/22 17:40
 */
public interface UserBalanceService {
    /**
     * 获取用户暴击值兑换权限
     * @param uid
     * @return
     */
    public UserBalanceResutVo getExchangeAuthority(String uid);
}
