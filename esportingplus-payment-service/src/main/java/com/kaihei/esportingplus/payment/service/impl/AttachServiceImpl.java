package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.enums.AccountStateType;
import com.kaihei.esportingplus.payment.api.enums.AccountType;
import com.kaihei.esportingplus.payment.api.vo.AccountInfoVo;
import com.kaihei.esportingplus.payment.data.jpa.repository.GCoinBalanceRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.StarlightBillRepository;
import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import com.kaihei.esportingplus.payment.domain.entity.StarlightBalance;
import com.kaihei.esportingplus.payment.service.AttachService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 附加服务相关service
 *
 * @author xusisi
 * @create 2018-09-30 16:47
 **/
@Service
public class AttachServiceImpl implements AttachService {

    private static Logger logger = LoggerFactory.getLogger(AttachServiceImpl.class);

    @Autowired
    private GCoinBalanceRepository gCoinBalanceRepository;

    @Autowired
    private StarlightBillRepository starlightBillRepository;

    @Override
    public AccountInfoVo checkAccountInfo(String accountType, String userId, Integer amount) throws Exception {
        AccountInfoVo infoVo = new AccountInfoVo();
        logger.debug("checkAccountInfo >> start >> accountType : {} ,userId : {} ,amount : {}", accountType, userId, amount);


        //验证数据类型（001暴鸡币，002暴击值）
        if (!AccountType.GCOIN_ACCOUNT.getCode().equals(accountType) && !AccountType.STARLIGHT_ACCOUNT.getCode().equals(accountType)) {
            logger.error("checkAccountInfo >> exception : {} ", BizExceptionEnum.ACCOUNT_CHECK_PARAM_ERROR.getErrMsg());
            throw new BusinessException(BizExceptionEnum.ACCOUNT_CHECK_PARAM_ERROR);
        }

        //操作的金额(单位：分--->元)
        BigDecimal operateAmount = new BigDecimal(amount).divide(new BigDecimal(100));

        if (AccountType.GCOIN_ACCOUNT.getCode().equals(accountType)) {
            GCoinBalance gCoinBalance = gCoinBalanceRepository.findOneByUserId(userId);
            //暴鸡币账户不存在，则肯定暴鸡币金额不足
            if (gCoinBalance == null) {
                logger.error("checkAccountInfo >> exception : {} ", BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH);
            }

            String stateCode = gCoinBalance.getState();
            //  验证账户状态是否冻结
            if (AccountStateType.FROZEN.getCode().equals(stateCode)) {
                logger.error("checkAccountInfo >> exception : {} ", BizExceptionEnum.GCOIN_ACCOUNT_FROZEN.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_FROZEN);
            }

            //账户是否可用
            if (AccountStateType.UNAVAILABLE.getCode().equals(stateCode)) {
                logger.error("checkAccountInfo >> exception : {} ", BizExceptionEnum.GCOIN_ACCOUNT_UNAVAILABLE);
                throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_UNAVAILABLE);
            }


            //判断用户可用余额能够进行该项操作
            if (gCoinBalance.getUsableAmount().compareTo(operateAmount) < 0) {
                logger.error("checkAccountInfo >> exception : {} ", BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
                throw new BusinessException(BizExceptionEnum.GCOIN_ACCOUNT_BALANCE_NOT_ENOUGH);
            }

            infoVo.setAccountType(AccountType.GCOIN_ACCOUNT.getCode());
            infoVo.setState(gCoinBalance.getState());
            infoVo.setTotalAmount(gCoinBalance.getGcoinBalance().multiply(new BigDecimal(100)).intValue());
            infoVo.setFrozenAmount(gCoinBalance.getFrozenAmount().multiply(new BigDecimal(100)).intValue());
            infoVo.setUsableAmount(gCoinBalance.getUsableAmount().multiply(new BigDecimal(100)).intValue());
            infoVo.setUserId(userId);

        } else if (AccountType.STARLIGHT_ACCOUNT.getCode().equals(accountType)) {

            StarlightBalance starlightBalance = starlightBillRepository.findOneByUserId(userId);

            //星光账户不存在，则余额肯定不够提现等操作
            if (starlightBalance == null) {
                logger.error("checkAccountInfo >> exception : {} ", BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
                throw new BusinessException(BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH);
            }

            String stateCode = starlightBalance.getState();
            if (AccountStateType.UNAVAILABLE.getCode().equals(stateCode)) {
                logger.error("checkAccountInfo >> exception : {} ", BizExceptionEnum.STAR_ACCOUNT_UNAVAILABLE.getErrMsg());
                throw new BusinessException(BizExceptionEnum.STAR_ACCOUNT_UNAVAILABLE);
            }

            if (AccountStateType.FROZEN.getCode().equals(stateCode)) {
                logger.error("checkAccountInfo >> exception : {} ", BizExceptionEnum.STAR_ACCOUNT_FROZEN.getErrMsg());
                throw new BusinessException(BizExceptionEnum.STAR_ACCOUNT_FROZEN);
            }

            if (starlightBalance.getUsableAmount().compareTo(operateAmount) < 0) {
                logger.error("checkAccountInfo >> exception : {} ", BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH.getErrMsg());
                throw new BusinessException(BizExceptionEnum.STAR_ACCOUNT_BALANCE_NOT_ENOUGH);
            }

            infoVo.setState(starlightBalance.getState());
            infoVo.setUserId(userId);
            infoVo.setTotalAmount(starlightBalance.getBalance().multiply(new BigDecimal(100)).intValue());
            infoVo.setUsableAmount(starlightBalance.getUsableAmount().multiply(new BigDecimal(100)).intValue());
            infoVo.setFrozenAmount(starlightBalance.getFrozenAmount().multiply(new BigDecimal(100)).intValue());
            infoVo.setAccountType(AccountType.STARLIGHT_ACCOUNT.getCode());


        }

        return infoVo;
    }
}
