package com.kaihei.esportingplus.payment.data.mongodb.repository;

import com.kaihei.esportingplus.payment.domain.document.RefundVoucher;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefundVoucherRepository extends MongoRepository<RefundVoucher, String> {

    RefundVoucher findOneByOrderId(String orderId);
}
