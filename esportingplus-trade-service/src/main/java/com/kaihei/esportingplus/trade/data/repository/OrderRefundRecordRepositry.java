package com.kaihei.esportingplus.trade.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import com.kaihei.esportingplus.trade.domain.entity.OrderRefundRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderRefundRecordRepositry extends CommonRepository<OrderRefundRecord> {

    OrderRefundRecord getByOrderId(String orderId);

}