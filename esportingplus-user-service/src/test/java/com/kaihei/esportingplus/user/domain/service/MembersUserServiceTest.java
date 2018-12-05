package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.commons.cache.utils.JsonsUtils;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.user.BaseTest;
import com.kaihei.esportingplus.user.UserServiceApplication;
import com.kaihei.esportingplus.user.api.params.UserPhoneLoginParams;
import com.kaihei.esportingplus.user.api.vo.PhoneLoginContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xiekeqing
 * @Title: MembersUserServiceTest
 * @Description: 终端用户服务测试类
 * @date 2018/9/1215:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class MembersUserServiceTest extends BaseTest {

    @Autowired
    private RegistLoginService registLoginService;

    @Autowired
    private MembersUserService membersUserService;

    @Test
    public void testLoginPhone(){
        PhoneLoginContext phoneLoginContext = new PhoneLoginContext();
        phoneLoginContext.setVersion("1.0");
        UserPhoneLoginParams params = new UserPhoneLoginParams();
        params.setPhone("13855138555");
        params.setChannel("wandoujia");
        params.setDeviceId("001001001");
        params.setCode("145232");
        phoneLoginContext.setParams(params);

        ResponsePacket responsePacket= registLoginService.loginPhone(phoneLoginContext);
        logger.debug(FastJsonUtils.toJson(responsePacket));
        Assert.assertNotNull(responsePacket);

    }

    @Test
    public void testGetMembersUserById() {
        logger.debug(JsonsUtils.toJson(membersUserService.getMembersUserById(1813)));
        Assert.assertNotNull(membersUserService.getMembersUserById(1813));
        logger.debug(JsonsUtils.toJson(membersUserService.getMembersUserById(95688)));
        Assert.assertNotNull(membersUserService.getMembersUserById(95688));
        logger.debug(JsonsUtils.toJson(membersUserService.getMembersUserById(999999999)));
        Assert.assertNull(membersUserService.getMembersUserById(999999999));
    }

    @Test
    public void testRandomUid() {
        String uid = membersUserService.getRandomUid();
        logger.debug("cmd=testRandomUid >> uid >> {}", uid);
        Assert.assertNotNull(uid);
    }

    @Test
    public void testGetUserIdByUid() {
        Integer userId = membersUserService.getUserIdByUid("75a8c2da");
        logger.debug(
                "cmd=testGetUserIdByUid >> userId >> {}", "0a37654d", userId);
        Assert.assertNotNull(userId);
    }
}
