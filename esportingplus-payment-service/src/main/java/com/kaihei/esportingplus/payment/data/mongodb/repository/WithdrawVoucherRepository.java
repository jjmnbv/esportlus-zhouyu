package com.kaihei.esportingplus.payment.data.mongodb.repository;

import com.kaihei.esportingplus.payment.domain.document.WithdrawVoucher;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 云账户提现凭证-记录请求数据，返回数据
 * @author chenzhenjun
 */
public interface WithdrawVoucherRepository extends MongoRepository<WithdrawVoucher, String> {

    WithdrawVoucher findOneByOrderId(String outTradeNo);
}
