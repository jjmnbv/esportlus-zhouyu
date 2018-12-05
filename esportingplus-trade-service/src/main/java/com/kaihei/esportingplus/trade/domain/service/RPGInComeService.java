package com.kaihei.esportingplus.trade.domain.service;

import com.kaihei.esportingplus.trade.api.params.RPGInComeParams;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;

/**
 *@Description: RPG收益Service
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/11/6 17:15
*/
public interface RPGInComeService extends IncomeService {


   /**
    *@Description: 获取RMB预计收益
    *@param: [profitQueryParams]
    *@return: com.kaihei.esportingplus.trade.api.vo.PreProfitVo
    *@throws:
    *
    *@author  Orochi-Yzh
    *@dateTime  2018/11/14 20:16
   */
   PVPPreIncomeVo getPreInCome(RPGInComeParams inComeParams);

}
