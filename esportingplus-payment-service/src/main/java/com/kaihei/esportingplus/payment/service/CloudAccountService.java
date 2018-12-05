package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.dto.CloudWithdrawOrderDto;
import com.kaihei.esportingplus.payment.api.vo.CloudAccountDealerInfoVo;
import com.kaihei.esportingplus.payment.domain.entity.CapaySetting;

import java.util.List;
import java.util.Map;

/**
 * 云账户Service
 * @author chenzhenjun
 */
public interface CloudAccountService {

    /**
     * 查询商户余额
     * @param appId
     * @param tag
     * @return
     */
    public List<CloudAccountDealerInfoVo> queryAccount(String appId, String tag) throws BusinessException;

    /**
     * 处理云账户回调通知
     * @param requestMap
     * @return
     */
    public String receiveCloudNotify(Map<String, String> requestMap);

    /**
     * 主动查询云账户提现订单
     * @param outTradeNo
     * @param appId
     * @param tag
     * @return
     */
    public CloudWithdrawOrderDto searchWithdrawOrderInfo(String outTradeNo, String appId, String tag,
                                                         CapaySetting capaySetting) throws BusinessException;
}
