package com.kaihei.esportingplus.payment.migrate;

import com.google.common.collect.Lists;
import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.api.enums.ExternalRefundStateEnum;
import com.kaihei.esportingplus.payment.api.enums.ExternalWithdrawStateEnum;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalTradeBillRepository;
import com.kaihei.esportingplus.payment.domain.entity.ExternalPaymentOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalRefundOrder;
import com.kaihei.esportingplus.payment.domain.entity.ExternalTradeBill;
import com.kaihei.esportingplus.payment.domain.entity.ExternalWithdrawOrder;
import com.kaihei.esportingplus.payment.migrate.constant.TypeMapping;
import com.kaihei.esportingplus.payment.migrate.repository.QQPayOrderRepository;
import com.kaihei.esportingplus.payment.migrate.service.CommonService;
import com.kaihei.esportingplus.payment.service.ExternalTradeBillService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: tangtao
 **/
public class BatchInsertTest extends BaseTest {

    @Autowired
    protected SnowFlake snowFlake;
    @Autowired
    ExternalTradeBillRepository tradeBillRepository;
    @Autowired
    CommonService commonService;
    @Autowired
    private QQPayOrderRepository qqPayOrderRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ExternalTradeBillService externalTradeBillService;

    @Test
    public void tradeBillBatchInsertTest() {

        int count = 10;

        List<ExternalTradeBill> tradeBillList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ExternalTradeBill tradeBill = new ExternalTradeBill();
            tradeBill.setId(Long.valueOf(i));
            tradeBill.setChannel("WechatPay");
            tradeBill.setOrderId("000");
            tradeBill.setOrderType("000");
            tradeBill.setOutTradeNo("000000");
            tradeBill.setTransactionId("000000");
            tradeBill.setFlowNo(String.valueOf(System.currentTimeMillis() + i));
            tradeBill.setTotalFee(0);
            tradeBill.setTradeType("000");

            tradeBill.setLastModifiedDate(DateUtil.str2Date("2017-11-11 11:11:11", DateUtil.FORMATTER));
            tradeBill.setCreateDate(DateUtil.str2Date("2017-11-11 11:11:11", DateUtil.FORMATTER));

            tradeBillList.add(tradeBill);
        }

        tradeBillRepository.save(tradeBillList);


    }

    @Test
    public void jdbcTmplateBatchInsertTest() {

        String Batch_Inser_Trade_Bill_Sql = "INSERT INTO external_trade_bill(id, create_date, last_modified_date, channel, flow_no, order_id, order_type, out_trade_no, total_fee, trade_type, transaction_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        ExternalTradeBill tradeBill = new ExternalTradeBill();
        tradeBill.setId(111L);
        tradeBill.setChannel("WechatPay");
        tradeBill.setOrderId("000");
        tradeBill.setOrderType("000");
        tradeBill.setOutTradeNo("000000");
        tradeBill.setTransactionId("000000");
        tradeBill.setFlowNo("1111");
        tradeBill.setTotalFee(0);
        tradeBill.setTradeType("000");
        tradeBill.setCreateDate(new Date());
        tradeBill.setLastModifiedDate(new Date());

        ExternalTradeBill tradeBill2 = new ExternalTradeBill();
        tradeBill2.setId(222L);
        tradeBill2.setChannel("WechatPay");
        tradeBill2.setOrderId("000");
        tradeBill2.setOrderType("000");
        tradeBill2.setOutTradeNo("000000");
        tradeBill2.setTransactionId("000000");
        tradeBill2.setFlowNo("2222");
        tradeBill2.setTotalFee(0);
        tradeBill2.setTradeType("000");
        tradeBill2.setCreateDate(new Date());
        tradeBill2.setLastModifiedDate(new Date());

        List<ExternalTradeBill> externalTradeBills = Lists.newArrayList(tradeBill, tradeBill2);

        jdbcTemplate.batchUpdate(Batch_Inser_Trade_Bill_Sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                ExternalTradeBill tradeBill = externalTradeBills.get(i);

                ps.setLong(1, tradeBill.getId());
                ps.setDate(2, new java.sql.Date(tradeBill.getCreateDate().getTime()));
                ps.setDate(3, new java.sql.Date(tradeBill.getLastModifiedDate().getTime()));
                ps.setString(4, tradeBill.getChannel());
                ps.setString(5, tradeBill.getFlowNo());
                ps.setString(6, tradeBill.getOrderId());
                ps.setString(7, tradeBill.getOrderType());
                ps.setString(8, tradeBill.getOutTradeNo());
                ps.setInt(9, tradeBill.getTotalFee());
                ps.setString(10, tradeBill.getTradeType());
                ps.setString(11, tradeBill.getTransactionId());

            }

            @Override
            public int getBatchSize() {
                return externalTradeBills.size();
            }
        });
    }


    @Test
    public void tradeBillBatchSave() {
        List<ExternalTradeBill> externalTradeBills = createTradeBill();
        commonService.batchSaveTradeBill(externalTradeBills);
    }

    @Test
    public void paymentOrderBatchSaveTest() {
        List<ExternalTradeBill> externalTradeBills = createTradeBill();
        List<ExternalPaymentOrder> externalPaymentOrders = createPaymentOrder();
        commonService.batchSavePaymentOrder(externalPaymentOrders, externalTradeBills);
    }

    @Test
    public void refundOrderBatchSaveTest() {
        List<ExternalTradeBill> externalTradeBills = createTradeBill();
        List<ExternalRefundOrder> orders = createRefundOrder();
        commonService.batchSaveRefundOrder(orders, externalTradeBills);
    }

    @Test
    public void withdrawOrderBatchSaveTest() {
        List<ExternalTradeBill> externalTradeBills = createTradeBill();
        List<ExternalWithdrawOrder> orders = createWithdrawOrder();
        commonService.batchSaveWithdrawOrder(orders, externalTradeBills);
    }

    private List<ExternalRefundOrder> createRefundOrder() {

        ExternalRefundOrder order = new ExternalRefundOrder();
        order.setTransactionId("");
        order.setNotifyUrl("");
        order.setCreateDate(new Date());
        order.setLastModifiedDate(new Date());
        order.setOutTradeNo("otn1");
        order.setOrderType("ot1");
        order.setOutRefundNo("orn1");
        order.setRefundTime(new Date());
        order.setChannelId(1l);
        order.setChannelName("COO1");
        order.setSourceAppId(TypeMapping.getSourceAppId("kaihei"));
        order.setState(ExternalRefundStateEnum.SUCCESS.getCode());
        order.setTotalFee(100);
        order.setOrderId("oid1");
        order.setUserId("udi");

        ExternalRefundOrder order1 = new ExternalRefundOrder();
        order1.setTransactionId("");
        order1.setNotifyUrl("");
        order1.setCreateDate(new Date());
        order1.setLastModifiedDate(new Date());
        order1.setOutTradeNo("otn1");
        order1.setOrderType("ot1");
        order1.setOutRefundNo("orn2");
        order1.setRefundTime(new Date());
        order1.setChannelId(1l);
        order1.setChannelName("COO1");
        order1.setSourceAppId(TypeMapping.getSourceAppId("kaihei"));
        order1.setState(ExternalRefundStateEnum.SUCCESS.getCode());
        order1.setTotalFee(100);
        order1.setOrderId("oid2");
        order1.setUserId("udi");

        return Lists.newArrayList(order, order1);
    }

    private List<ExternalWithdrawOrder> createWithdrawOrder() {
        ExternalWithdrawOrder order = new ExternalWithdrawOrder();
        order.setOrderId("orderid1");
        order.setCardNo("cardno1");
        order.setIdcardNumber("idcardno1");
        order.setOutTradeNo("outtradeno1");
        order.setCreateDate(new Date());
        order.setLastModifiedDate(new Date());
        order.setPaiedTime(new Date());
        order.setRealName("realname");
        order.setRefNo("refno");
        order.setSourceAppId("appid");
        order.setState(ExternalWithdrawStateEnum.SUCCESS.getValue());
        order.setTotalFee(100);
        order.setUserId("userid");
        order.setChannel("channel");

        ExternalWithdrawOrder order1 = new ExternalWithdrawOrder();
        order1.setOrderId("orderid1");
        order1.setCardNo("cardno1");
        order1.setIdcardNumber("idcardno1");
        order1.setOutTradeNo("outtradeno1");
        order1.setCreateDate(new Date());
        order1.setLastModifiedDate(new Date());
        order1.setPaiedTime(new Date());
        order1.setRealName("realname");
        order1.setRefNo("refno");
        order1.setSourceAppId("appid");
        order1.setState(ExternalWithdrawStateEnum.SUCCESS.getValue());
        order1.setTotalFee(100);
        order1.setUserId("userid");
        order1.setChannel("channel");

        return Lists.newArrayList(order, order1);
    }

    private List<ExternalPaymentOrder> createPaymentOrder() {

        ExternalPaymentOrder order = new ExternalPaymentOrder();
        order.setUserId("0");
        order.setOutTradeNo("out");
        order.setTotalFee(100);
        order.setPrePayId("1234");
        order.setTransactionId("4321");
        order.setPaiedTime(null);
        order.setCreateDate(new Date());
        order.setLastModifiedDate(new Date());
        order.setOrderType("1");
        order.setState(TypeMapping.getStatus(1));
        order.setOrderId("out11");
        order.setSourceAppId(TypeMapping.getSourceAppId("kaihei"));
        order.setChannelId(1L);
        order.setChannelName("C001");
        order.setNotifyUrl("");

        ExternalPaymentOrder order2 = new ExternalPaymentOrder();
        order2.setUserId("0");
        order2.setOutTradeNo("out2");
        order2.setTotalFee(100);
        order2.setPrePayId("1234");
        order2.setTransactionId("4321");
        order2.setPaiedTime(null);
        order2.setCreateDate(new Date());
        order2.setLastModifiedDate(new Date());
        order2.setOrderType("1");
        order2.setState(TypeMapping.getStatus(1));
        order2.setOrderId("out22");
        order2.setSourceAppId(TypeMapping.getSourceAppId("kaihei"));
        order2.setChannelId(1L);
        order2.setChannelName("C001");
        order2.setNotifyUrl("");

        return Lists.newArrayList(order, order2);
    }

    private List<ExternalTradeBill> createTradeBill() {
        ExternalTradeBill tradeBill = new ExternalTradeBill();
        tradeBill.setId(111L);
        tradeBill.setChannel("WechatPay");
        tradeBill.setOrderId("000");
        tradeBill.setOrderType("000");
        tradeBill.setOutTradeNo("000000");
        tradeBill.setTransactionId(null);
        tradeBill.setFlowNo("1111");
        tradeBill.setTotalFee(0);
        tradeBill.setTradeType("000");
        tradeBill.setCreateDate(new Date());
        tradeBill.setLastModifiedDate(new Date());

        ExternalTradeBill tradeBill2 = new ExternalTradeBill();
        tradeBill2.setId(222L);
        tradeBill2.setChannel("WechatPay");
        tradeBill2.setOrderId("000");
        tradeBill2.setOrderType("000");
        tradeBill2.setOutTradeNo("000000");
        tradeBill2.setTransactionId("000000");
        tradeBill2.setFlowNo("2222");
        tradeBill2.setTotalFee(0);
        tradeBill2.setTradeType("000");
        tradeBill2.setCreateDate(new Date());
        tradeBill2.setLastModifiedDate(new Date());

        ExternalTradeBill tradeBill3 = new ExternalTradeBill();
        tradeBill3.setId(222L);
        tradeBill3.setChannel("WechatPay");
        tradeBill3.setOrderId("000");
        tradeBill3.setOrderType("000");
        tradeBill3.setOutTradeNo("000000");
        tradeBill3.setTransactionId("000000");
        tradeBill3.setFlowNo("2222");
        tradeBill3.setTotalFee(0);
        tradeBill3.setTradeType("000");
        tradeBill3.setCreateDate(DateUtil.str2Date("2017-01-01 01:01:01", DateUtil.FORMATTER));
        tradeBill3.setLastModifiedDate(new Date());

        return Lists.newArrayList(tradeBill, tradeBill2, tradeBill3);
    }

}
