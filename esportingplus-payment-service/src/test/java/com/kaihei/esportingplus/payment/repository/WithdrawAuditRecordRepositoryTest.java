package com.kaihei.esportingplus.payment.repository;

/*import com.kaihei.esportingplus.common.sensors.SensorsAutoConfiguration;*/
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.api.enums.WithdrawBlockEnum;
import com.kaihei.esportingplus.payment.api.enums.WithdrawChannelEnum;
import com.kaihei.esportingplus.payment.api.enums.WithdrawVerifyEnum;
import com.kaihei.esportingplus.payment.api.params.PageParams;
import com.kaihei.esportingplus.payment.data.jpa.repository.WithdrawAuditRecordRepository;
import com.kaihei.esportingplus.payment.domain.entity.WithdrawAuditRecord;
import com.kaihei.esportingplus.payment.util.PageUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/**
 * 提现审核记录DAO层-测试类
 *
 * @author chenzhenjun
 */
public class WithdrawAuditRecordRepositoryTest extends BaseTest {

    @Autowired
    private WithdrawAuditRecordRepository auditRecordRepository;

    /*@MockBean
    private SensorsAutoConfiguration mockSensorsAutoConfiguration;*/

    @Test
    public void queryTest() {

        String uid = "8a2acfcd";
        PageParams pageParams = new PageParams();
        pageParams.setPage("0");
        pageParams.setSize("20");
        PageRequest request = PageUtils.getPageRequest(pageParams);
        List<WithdrawAuditRecord> recordList = auditRecordRepository.findByUid(uid, request);

        assertNotEquals(recordList.size(), 0);
//        assertEquals(recordList.size(), 0);
    }

    @Test
    public void insertTest() {
        BDDMockito.when(mockedSnowFlake.nextId()).thenReturn(247371760596680704L);

        WithdrawAuditRecord record = new WithdrawAuditRecord();
        record.setUid("8a2acfcd");
        record.setChannel(WithdrawChannelEnum.WECHAT.getName());
        record.setOrderId("247371760596680704");
        record.setTotalFee(200);
        record.setVerifyState(WithdrawVerifyEnum.WAIT.getValue());
        record.setBlockState(WithdrawBlockEnum.NOT_YET.getValue());
        record.setRemark("申请提现");
        record.setSourceAppId("IOS_BJDJ");
        auditRecordRepository.save(record);

        assertNotNull(record.getId());

    }

    @Test
    public void countTest() {

        String uid = "8a2acfcd";
        List<String> verifyStateList = new ArrayList<>();
        verifyStateList.add(WithdrawVerifyEnum.WAIT.getValue());
        verifyStateList.add(WithdrawVerifyEnum.SUCCESS.getValue());
        verifyStateList.add(WithdrawVerifyEnum.VERIFY_SUCCESS.getValue());
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        Date queryStartDate = DateUtil.localDateTime2Date(startDate);
        Date queryEndDate = DateUtil.localDateTime2Date(LocalDateTime.now());

        Long usedTimes = auditRecordRepository.countByUidAndCreateDateBetweenAndVerifyStateIn(uid,
                queryStartDate, queryEndDate, verifyStateList);
    }
}
