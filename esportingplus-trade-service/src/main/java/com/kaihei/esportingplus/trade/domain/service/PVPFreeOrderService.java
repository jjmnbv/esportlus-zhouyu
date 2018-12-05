package com.kaihei.esportingplus.trade.domain.service;

import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.gamingteam.api.mq.PVPFreeTeamEndOrderMessage;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamStartOrderVO;
import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPTeamLeaveMemberAfterStartedVO;
import com.kaihei.esportingplus.trade.api.params.PVPFreeOrdersForBackGroundParams;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeBossOrderForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderListVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeOrderIncomeForBackGroundVO;
import com.kaihei.esportingplus.trade.api.vo.PVPFreeTeamOrderVo;
import java.util.List;

/**
 * PVP免费车队service接口
 *
 * @author zhangfang
 */
public interface PVPFreeOrderService extends OrderService {

    /**
     * 创建开车订单，包含创建暴鸡订单及老板订单
     */
    public void createTeamStartOrder(PVPFreeTeamStartOrderVO pvpFreeTeamStartOrderVO);

    /**
     * 根据车队序列号和uid查询车队订单详情
     */
    public PVPFreeTeamOrderVo getOrderDetailsByUidAndTeamSequence(String uid, String teamSequence);

    /**
     * 根据订单序列号和uid查询订单详情
     */
    public PVPFreeTeamOrderVo getOrderDetailsBySequence(String uid, String sequence);

    /**
     *@Description: 更新订单和结算收益
     *@param: [teamSequence, teamStatus, gameResulCode]
     *@return: void
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/15 17:05
     */
    void updateOrderAndSendInCome(PVPFreeTeamEndOrderMessage message);

    /**
     * 查询免费车队老板订单列表
     * @param offset
     * @param limit
     * @return
     */
    PagingResponse<PVPFreeOrderListVO> selectUserBossOrdersByPage(int offset, int limit);

    /**
     * 查询免费车队老板列表
     * @param offset
     * @param limit
     * @return
     */
    PagingResponse<PVPFreeOrderListVO> selectUserBaojiOrdersByPage(int offset, int limit);

    /**
     * 后管系统该老板订单给与暴鸡收益明细
     * @param params
     * @return
     */
    List<PVPFreeOrderIncomeForBackGroundVO> selectPVPFreeBossGiveBaojiIncome(String sequence);

    /**
     * 后管系统该暴鸡订单给从老板处获得的收益明细
     * @param params
     * @return
     */
    List<PVPFreeOrderIncomeForBackGroundVO> selectPVPFreeBaojiFromBossIncome(String sequence);

    /**
     * 后管系统查询老板订单列表
     * @param params
     * @return
     */
    PagingResponse<PVPFreeBossOrderForBackGroundVO> selectPvpFreeBossOrderForBackGroundVO(PVPFreeOrdersForBackGroundParams params);

    /**
     * 后管系统查询暴鸡订单列表
     * @param params
     * @return
     */
    PagingResponse<PVPFreeBossOrderForBackGroundVO> selectPvpFreeBaojiOrderForBackGroundVO(
            PVPFreeOrdersForBackGroundParams params);

    /**
     *@Description: 中途退出更新订单状态
     *@return: boolean
     *@throws:
     *
     *@author  Orochi-Yzh
     *@dateTime  2018/11/12 17:14
     */
    boolean updateOrderForLeaveTeam(PVPTeamLeaveMemberAfterStartedVO message);
}
