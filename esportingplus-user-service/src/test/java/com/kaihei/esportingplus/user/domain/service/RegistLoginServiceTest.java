package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.sensors.service.SensorsAnalyticsService;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.user.UserServiceApplication;
import com.kaihei.esportingplus.user.api.params.MiniprogramLoginParam;
import com.kaihei.esportingplus.user.api.params.MiniprogramUserInfo;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-25 11:18
 * @Description:
 */
@SpringBootTest(classes = UserServiceApplication.class)
@RunWith(SpringRunner.class)
public class RegistLoginServiceTest {

    @Autowired
    private RegistLoginService registLoginService;

    @Autowired
    private MembersUserService membersUserService;

    @Autowired
    private SensorsAnalyticsService sensorsAnalyticsService;

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

    @Test
    public void testMpAuth() {
        // 密文
        String encryptedData = "VFA5OHIkDwjETBrL0m0zkvTL7na2fw7W1TACxiknmGnebUrkCsiKlX88D27sdzeJnIYeoCpt3dDy+lqST6xzzbsvR2gRfjZ9ly50KiR3lWP2r02oPKzEFaR/uG/0EX5PxX0dQFVstXt8dCvHvHtqa9lr17QrMREjEGFUkKc8DrY5oxecvMRtdKM7+2oqJftoEg7YKNz6TX6L0f9lzL1L3SBGhb8MXbKua25nIBvAYP6khV3mSCnPnfiQb1tq+j45il/WR/Um18KM+zE9YYHszXyz94WRiTgY/p5hNRIBblWXwjoFMubfwEPhxbfTULbJK0G0zUdAAI+FOvFjOMi4CIQ7zcY6MEqWnoqYvPd4kPfL8rHj4oSzinodJohs3WCTM+PkHS71eSds8fcFFulrWuS8nIuC9CGJIecK+QIEgH/Cs6aRxF4XOUADwuQ93nD6ViNKEpINMBNY0R/GweIaHRkSPTOG5IaHgpgwdMmnfnL0yAMD3afgEXFB0hIWU7KsRy/Qngy8MbDaGmYiL6Yy/9Qtxke1G9ml0Y05EEEC3oU=";
        //向量
        String iv = "P2cTmODKZMTJ+YiNyF7Rxg==";
        MiniprogramLoginParam param = new MiniprogramLoginParam();
        param.setCode("021v0DvI16l8X60G8SvI1LVrvI1v0DvX");
        MiniprogramUserInfo info = new MiniprogramUserInfo();
        info.setEncryptedData(encryptedData);
        info.setIv(iv);
        param.setUserInfo(info);
        registLoginService.miniprogramLogin(param, null, null);
    }

    @Test
    public void testDevice() {
        MembersUser user = new MembersUser();
        user.setId(95730);
        membersUserService.getOrCreateDevice(user);
    }

    @Test
    public void testSA() {
        sensorsAnalyticsService
                .trackSignUp("oUg00000", "satest11223312321");

        System.out.println("finished------------------------------------------------------------------------------------------");
    }
}

