package com.kaihei.esportingplus.payment.util;

import com.kaihei.esportingplus.payment.api.enums.WalletStateEnum;
import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import com.kaihei.esportingplus.payment.domain.entity.StarlightBalance;

import java.math.BigDecimal;

/**
 * @program: esportingplus
 * @description: 账号相关工具类
 * @author: xusisi
 * @create: 2018-11-29 11:28
 **/
public class AccountUtil {

    private static final BigDecimal NUM_ZERO = new BigDecimal("0.00");

    /**
     * @Description: 初始化暴鸡币账号
     * @Param: [userId]
     * @Return com.kaihei.esportingplus.payment.domain.entity.GCoinBalance
     * @Author: xusisi
     */
    public static GCoinBalance generateGcoinBalance(String userId) {
        GCoinBalance gCoinBalance = new GCoinBalance();
        gCoinBalance.setGcoinBalance(NUM_ZERO);
        gCoinBalance.setFrozenAmount(NUM_ZERO);
        gCoinBalance.setUsableAmount(NUM_ZERO);
        gCoinBalance.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
        gCoinBalance.setUserId(userId);
        return gCoinBalance;
    }

    /**
     * @Description: 初始化暴击值账号
     * @Param: [userId]
     * @Return com.kaihei.esportingplus.payment.domain.entity.StarlightBalance
     * @Author: xusisi
     */
    public static StarlightBalance generateStarlightBalance(String userId) {
        StarlightBalance starlightBalance = new StarlightBalance();
        starlightBalance.setBalance(NUM_ZERO);
        starlightBalance.setFrozenAmount(NUM_ZERO);
        starlightBalance.setUsableAmount(NUM_ZERO);
        starlightBalance.setState(WalletStateEnum.WALLET_STATE_AVAILABLE.getCode());
        starlightBalance.setUserId(userId);
        return starlightBalance;
    }

}
