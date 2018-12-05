package com.kaihei.esportingplus.trade.domain.service;


import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPTeamStartOrderVO;
import com.kaihei.esportingplus.trade.api.params.PVPBossCurrentPaidAmountParams;
import com.kaihei.esportingplus.trade.api.params.PVPBossOrderCreateParams;
import com.kaihei.esportingplus.trade.api.params.PVPInComeParams;
import com.kaihei.esportingplus.trade.api.params.UpdateOrderStatusPVPParams;
import com.kaihei.esportingplus.trade.api.vo.PVPBaojiOrderListVO;
import com.kaihei.esportingplus.trade.api.vo.PVPBossOrderListVO;
import com.kaihei.esportingplus.trade.api.vo.PVPTeamOrderVo;
import com.kaihei.esportingplus.trade.api.vo.PVPPreIncomeVo;
import java.util.Map;

public interface PVPOrderService extends OrderService {

    /**
     * 老板创建订单
     */
    public Map<String, String> createTeamBossOrder(PVPBossOrderCreateParams orderParams, String ip);

    /**
     * 创建开车订单，包含创建暴鸡订单，及将老板订单状态修改为已开车
     */
    public void createTeamStartOrder(PVPTeamStartOrderVO PVPTeamStartOrderVO);

    /**
     * @Description: 更新PVP订单状态
     * @param: [updateOrderStatusPVPParams] 参数
     * @return: boolean true:更新状态成功，false:更新状态失败
     * @throws:
     * @author Orochi-Yzh
     * @dateTime 2018/8/8 15:45
     */
    void updateOrderStatus(UpdateOrderStatusPVPParams updateOrderStatusPVPParams);

    /**
     * 分页查询老板订单列表
     */
    public PagingResponse<PVPBossOrderListVO> selectUserBossOrdersByPage(int offset, int limit);

    /**
     * 分页查询暴鸡订单列表
     */
    public PagingResponse<PVPBaojiOrderListVO> selectUserBaojiOrdersByPage(int offset, int limit);

    /**
     * 获取车队订单详情
     */
    PVPTeamOrderVo getOrderDetailsBySequence(String uid, String sequence);

    /**
     *@Description:  预计收益
     *@param: [caculateParams]
     *@return: PreInComeVo
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/26 11:02
     */
    PVPPreIncomeVo preIncome(PVPInComeParams pvpInComeParams);

    /*/**
     *@Description: 校验老板应付金额与当前支付金额是否一致
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/27 18:21
    */
    boolean checkBossBetweenSamePaid(PVPBossCurrentPaidAmountParams amountParams);
}
