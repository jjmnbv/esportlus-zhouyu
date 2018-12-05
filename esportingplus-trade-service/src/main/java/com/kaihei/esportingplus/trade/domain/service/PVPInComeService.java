package com.kaihei.esportingplus.trade.domain.service;

import com.kaihei.esportingplus.trade.api.params.PVPInComeParams;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;
import java.util.List;

/**
 *@Description: PVP付费车队收益Service
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/11/6 17:15
*/
public interface PVPInComeService extends IncomeService {

    /**
     *@Description: 获取PVP老板支付总额
     *@param: [gameId, bossDans, baojiLevels]
     *@return: int
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/27 16:50
    */
    int getBossPaySum(Integer gameId, List<Integer> bossDans,List<Integer> baojiLevels);

    /**
     *@Description: 计算暴鸡预计收益
     *@param: [pvpInComeParams]
     *@return: com.kaihei.esportingplus.trade.api.vo.PreIncomeVo
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/27 16:26
    */
    PVPPreIncomeVo preIncome(PVPInComeParams pvpInComeParams);
}
