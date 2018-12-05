package com.kaihei.esportingplus.payment.data.mongodb.repository;

import com.kaihei.esportingplus.payment.domain.document.PaymentVoucher;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 支付凭证Mongo对象文档数据源仓库
 *
 * @author haycco
 */
public interface PaymentVoucherRepository extends MongoRepository<PaymentVoucher, String> {

    PaymentVoucher findOneByOrderId(String orderId);

}
