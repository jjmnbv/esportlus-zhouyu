package com.kaihei.esportingplus.payment.service;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.api.enums.WithdrawVerifyEnum;
import com.kaihei.esportingplus.payment.api.params.WithdrawAuditParams;
import com.kaihei.esportingplus.payment.api.vo.PageInfo;
import com.kaihei.esportingplus.payment.api.vo.WithdrawAuditListVo;
import java.util.Map;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 提现审核记录Service层-测试类
 *
 * @author chenzhenjun
 */
public class WithdrawServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawServiceTest.class);

    @Autowired
    private WithdrawService withdrawService;

    /*@MockBean
    private SensorsAutoConfiguration mockSensorsAutoConfiguration;*/

    @Test
    public void updateStateTest() {
        WithdrawAuditListVo queryVo = new WithdrawAuditListVo();
        queryVo.setId(1L);
        queryVo.setVerifyState(WithdrawVerifyEnum.VERIFY_FAIL.getValue());

        withdrawService.updateAuditState(queryVo);

    }

    @Test
    public void getAuditListByAppTest() {
        String uid = "218204c4";
        String page = "0";
        String size = "20";

        PageInfo<WithdrawAuditListVo> list = withdrawService.getAuditListByApp(uid, page, size);

        logger.info(">>APP 提现记录列表  >> [{}]" , list.getList().toString());

        assertNotEquals(list.getTotal().longValue(), 1);

    }

    /**
     * 发起提现-service接口测试用例
     */
    @Test
    public void createWithdrawAuditOrderTest() {
        WithdrawAuditParams auditParams = new WithdrawAuditParams();
        auditParams.setUserId(95696);
        auditParams.setUid("218204c4");
        auditParams.setAmount(200);
        auditParams.setAppId("WECHAT_PA_BJDJ");
        auditParams.setTransferType(2);
        auditParams.setClientIp("127.0.0.1");

        Map<String, String> map = withdrawService.createWithdrawAuditOrder(auditParams);

        assertNotNull("正常执行并成功创建提现审批记录", map);

        logger.info(">>APP 发起提现，返回数据  >> [{}]" , map);
    }

}
