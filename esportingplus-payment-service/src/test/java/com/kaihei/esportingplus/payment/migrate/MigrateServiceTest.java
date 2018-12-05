package com.kaihei.esportingplus.payment.migrate;

import com.kaihei.esportingplus.payment.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: tangtao
 **/
public class MigrateServiceTest extends BaseTest {

    @Autowired
    private MigrateService migrateService;

    @Test
    public void test() {
        migrateService.migrate("recharge", null, null, null);
    }

}
