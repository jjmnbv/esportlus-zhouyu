package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.user.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 鉴权token服务测试类
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/29 17:43
 */
public class AuthTokenServiceTest extends BaseTest {

    @Autowired
    private AuthTokenService authTokenService;

    @Test
    public void testGetAccessToken() {

        String token = authTokenService.getAccessToken("1a70cfac", "i_1.0.1");
        logger.debug("cmd=testGetAccessToken >> token >> {}", token);
        Assert.assertNotNull(token);
    }

}
