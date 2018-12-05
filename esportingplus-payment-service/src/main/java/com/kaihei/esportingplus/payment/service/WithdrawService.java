package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.params.StarlightExchangeParams;
import com.kaihei.esportingplus.payment.api.params.WithdrawAuditParams;
import com.kaihei.esportingplus.payment.api.params.WithdrawCreateParams;
import com.kaihei.esportingplus.payment.api.params.WithdrawUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.PageInfo;
import com.kaihei.esportingplus.payment.api.vo.UserBalanceListVO;
import com.kaihei.esportingplus.payment.api.vo.WithdrawAuditListVo;
import com.kaihei.esportingplus.payment.api.vo.WithdrawConfigVo;
import com.kaihei.esportingplus.payment.domain.entity.WithdrawConfig;
import com.kaihei.esportingplus.payment.domain.entity.WithdrawOrder;

import java.util.List;
import java.util.Map;

public interface WithdrawService {

    public WithdrawOrder getOrderInfoFromRedis(String orderId, String userId);

    /*     新的提现及兑换接口代码      -------------start             */

    /**
     * 校验 暴击值状态状态及余额
     *
     * @param withdrawCreateParams
     * @return
     * @throws BusinessException
     */
    public Map<String, String> checkAndCreateWithdrawOrder(
            WithdrawCreateParams withdrawCreateParams) throws BusinessException;

    /**
     * 修改 订单状态
     *
     * @param withdrawUpdateParams
     * @throws BusinessException
     */
    public Map<String, String> updateWithdrawStatus(WithdrawUpdateParams withdrawUpdateParams) throws BusinessException;

    /**
     * 获取工作室暴鸡的暴击值
     *
     * @param userIds
     * @return
     */
    public List<UserBalanceListVO> getStarLightValues(String userIds);

    /**
     * 查询
     *
     * @param orderId
     * @param userId
     * @return
     */
    public WithdrawOrder getWithdrawStateInfo(String orderId, String userId);

    /**
     * 暴击值兑换成暴鸡币
     *
     * @param starlightExchangeParams
     * @return
     * @throws BusinessException
     */
    public Map<String, String> convertStarlightToGCoin(
            StarlightExchangeParams starlightExchangeParams) throws BusinessException;


    /**
     * 查询订单状态
     *
     * @param outTradeNo
     * @param orderId
     * @param userId
     * @return
     */
    public WithdrawOrder getWithdrawStateInfoWithAllParams(String outTradeNo, String orderId, String userId);

    /**
     * 积分兑换暴击值
     * @param userId
     * @param amount
     */
    public void convertScoreToStarlight(String userId, int amount, String outTradeNo);

    /**
     * 查询 兑换订单状态
     *
     * @param orderId
     * @param userId
     * @return
     */
    public WithdrawOrder getExchangeOrderInfo(String orderId, String userId);

    /**
     * 获取提现配置信息
     * @return
     */
    public WithdrawConfigVo getWithdrawConfigVo();

    /**
     * 更新提现配置信息
     * @param configVo
     */
    public void updateWithdrawConfig(WithdrawConfigVo configVo);

    /**
     * 创建提现申请订单
     * @param auditParams
     * @return
     */
    public Map<String, String> createWithdrawAuditOrder(WithdrawAuditParams auditParams);

    /**
     * APP端查询提现审核记录
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public PageInfo<WithdrawAuditListVo> getAuditListByApp(String uid, String page, String size);

    /**
     * 后台查询提现审核记录
     * @param queryVo
     * @param page
     * @param size
     * @return
     */
    public PageInfo<WithdrawAuditListVo> getAuditListByBackend(WithdrawAuditListVo queryVo, String page, String size);

    /**
     * 审核提现申请记录
     * @param queryVo
     */
    public void updateAuditState(WithdrawAuditListVo queryVo);

    /**
     * 截停提现
     * @param queryVo
     */
    public void updateBlockState(WithdrawAuditListVo queryVo);

}
