package com.kaihei.esportingplus.trade.domain.service;


import com.kaihei.esportingplus.trade.api.vo.InComeVo;
import com.kaihei.esportingplus.trade.common.IncomeCaculateParams;

public interface IncomeService {

    /**
     *@Description: 结算收益
     *@param: [caculateParams]
     *@return: com.kaihei.esportingplus.trade.api.vo.InComeVo
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/22 19:48
    */
    InComeVo getIncome(IncomeCaculateParams caculateParams);
}
