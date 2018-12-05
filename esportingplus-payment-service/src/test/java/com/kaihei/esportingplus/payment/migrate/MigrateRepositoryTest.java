package com.kaihei.esportingplus.payment.migrate;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.migrate.entity.AliPayOrder;
import com.kaihei.esportingplus.payment.migrate.entity.OrderBill;
import com.kaihei.esportingplus.payment.migrate.entity.QQPayOrder;
import com.kaihei.esportingplus.payment.migrate.entity.RechargeOrder;
import com.kaihei.esportingplus.payment.migrate.entity.RefundOrder;
import com.kaihei.esportingplus.payment.migrate.entity.TransferOrder;
import com.kaihei.esportingplus.payment.migrate.entity.WechatPayOrder;
import com.kaihei.esportingplus.payment.migrate.entity.YunOrder;
import com.kaihei.esportingplus.payment.migrate.repository.AliPayOrderRepository;
import com.kaihei.esportingplus.payment.migrate.repository.OrderBillRepository;
import com.kaihei.esportingplus.payment.migrate.repository.QQPayOrderRepository;
import com.kaihei.esportingplus.payment.migrate.repository.RechargeOrderRepository;
import com.kaihei.esportingplus.payment.migrate.repository.RefundOrderRepository;
import com.kaihei.esportingplus.payment.migrate.repository.TransferOrderRepository;
import com.kaihei.esportingplus.payment.migrate.repository.WechatPayOrderRepository;
import com.kaihei.esportingplus.payment.migrate.repository.YunOrderRepository;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: tangtao
 **/
public class MigrateRepositoryTest extends BaseTest {

    @Autowired
    AliPayOrderRepository aliPayOrderRepository;
    @Autowired
    OrderBillRepository orderBillRepository;
    @Autowired
    QQPayOrderRepository qqPayOrderRepository;
    @Autowired
    RechargeOrderRepository rechargeOrderRepository;
    @Autowired
    RefundOrderRepository refundOrderRepository;
    @Autowired
    TransferOrderRepository transferOrderRepository;
    @Autowired
    WechatPayOrderRepository wechatPayOrderRepository;
    @Autowired
    YunOrderRepository yunOrderRepository;


    @Test
    public void test() {

        List<AliPayOrder> aliPayOrders = aliPayOrderRepository.findAll();
        List<OrderBill> orderBills = orderBillRepository.findAll();
        List<QQPayOrder> qqPayOrders = qqPayOrderRepository.findAll();
        List<RechargeOrder> rechargeOrders = rechargeOrderRepository.findAll();
        List<RefundOrder> refundOrders = refundOrderRepository.findAll();
        List<TransferOrder> transferOrders = transferOrderRepository.findAll();
        List<WechatPayOrder> wechatPayOrders = wechatPayOrderRepository.findAll();
        List<YunOrder> yunOrders = yunOrderRepository.findAll();

    }

}
