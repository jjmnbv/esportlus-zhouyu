package com.kaihei.esportingplus.trade.domain.service;

import com.kaihei.esportingplus.trade.api.params.ChickenPointIncomeParams;
import com.kaihei.esportingplus.trade.api.vo.PVPFreePreIncomeVo;
import java.util.Map;
import java.util.function.Consumer;

/**
 *@Description: PVP免费车队收益Service
 *
 *@author  Orochi-Yzh
 *@dateTime  2018/11/6 17:15
*/
public interface PVPFreeInComeService {

  /**
   *@Description: 获取鸡分收益
   *@param: [incomeParams 收益入参
   *  updateOpt 需要更新数据的操作]
   *@return: java.lang.Integer
   *@throws:
   *
   *@author  Orochi-Yzh
   *@dateTime  2018/11/19 20:28
  */
  PVPFreePreIncomeVo getChickenPointIncome(ChickenPointIncomeParams incomeParams, Consumer<Map<Integer,Integer>> updateHook);


}
