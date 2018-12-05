package com.kaihei.esportingplus.payment;

import com.kaihei.esportingplus.payment.api.enums.TradeType;
import com.kaihei.esportingplus.payment.api.params.PageParams;
import com.kaihei.esportingplus.payment.util.PageUtils;
import com.kaihei.esportingplus.payment.util.SimpleDateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * @author: tangtao
 **/
public class SimpleTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void pageUtilsTest() {

        PageParams params = new PageParams();

        params.setPage(String.valueOf(0));
        params.setSize(String.valueOf(0));
        PageRequest pageRequest = PageUtils.getPageRequest(params);
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());

        params.setPage(String.valueOf(2));
        params.setSize(String.valueOf(1000));
        pageRequest = PageUtils.getPageRequest(params);
        assertEquals(1, pageRequest.getPageNumber());
        assertEquals(999, pageRequest.getPageSize());

        params.setPage(String.valueOf(-1));
        params.setSize(String.valueOf(-1));
        pageRequest = PageUtils.getPageRequest(params);
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());

        params.setPage(String.valueOf(2));
        params.setSize(String.valueOf(15));
        pageRequest = PageUtils.getPageRequest(params);
        assertEquals(1, pageRequest.getPageNumber());
        assertEquals(15, pageRequest.getPageSize());

        params.setPage(null);
        params.setSize("");
        pageRequest = PageUtils.getPageRequest(params);
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());

        params.setPage("");
        params.setSize("");
        pageRequest = PageUtils.getPageRequest(params);
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());

    }

    @Test
    public void LocalDateTimeTest() throws ParseException {

        System.out.println(TradeType.PAYMENT.name());
        System.out.println(TradeType.PAYMENT.getMsg());

    }

    @Test
    public void SimpleDateUtilTest() {
        System.out.println(SimpleDateUtils.getDayBegin() + "--" + SimpleDateUtils.getDayEnd());
        System.out.println(SimpleDateUtils.getBeginDayOfMonth() + "--" + SimpleDateUtils.getEndDayOfMonth());
    }

}
