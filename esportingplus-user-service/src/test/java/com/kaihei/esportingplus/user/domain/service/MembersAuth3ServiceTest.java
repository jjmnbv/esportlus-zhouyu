package com.kaihei.esportingplus.user.domain.service;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.user.UserServiceApplication;
import com.kaihei.esportingplus.user.api.params.PhoneBindParams;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.data.manager.MembersAuth3Manager;
import com.netflix.discovery.converters.Auto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xiekeqing
 * @Title: MembersAuth3ServiceTest
 * @Description: TODO
 * @date 2018/9/1215:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class MembersAuth3ServiceTest {

    @Autowired
    private MembersAuth3Service membersAuth3Service;

    @Autowired
    private MembersAuth3Manager membersAuth3Manager;

    @Autowired
    private MembersUserService membersUserService;

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

    @Test
    public void testGetByIdentifierOrUnionId() {
        Integer userId = null;
        userId = membersAuth3Service.getByIdentifierOrUnionId("WX", null, "otuE0wOEovrj7JQ-8-Fmev6nN6CQ");
        System.out.println(userId);
        Assert.assertNotNull(userId);

        userId = membersAuth3Service.getByIdentifierOrUnionId("WX", null, "otuE0wOEovrj7JQ-8-Fmev6nN6CQFF");
        System.out.println(userId);
        Assert.assertNull(userId);

        userId = membersAuth3Service.getByIdentifierOrUnionId("QQ", "658E95B13D53D18B3DA0E52AE2E3E52B", null);
        System.out.println(userId);
        Assert.assertNotNull(userId);

    }

    @Test
    public void testBindList() {
        Map<String, String> result = membersAuth3Manager.getBindList(95730, "gbcwjkuo");
        Set<String> keys = result.keySet();
        for (String s : keys) {
            System.out.println(s + ":" + result.get(s));
        }
        Assert.assertNotNull(result);
    }

    @Test
    public void testcheckAndUnBindAuth3() {
        membersAuth3Manager.checkAndUnBindAuth3(95730, "WX", "gbcwjkuo");
    }

    @Test
    public void testUpdatePhone() {
        membersAuth3Manager.updatePhone(95730, "18945123245");
    }

    @Test
    public void testCreateAuth3AndUpdatePhone() {
        PhoneBindParams params = new PhoneBindParams();
        params.setPhone("13755558888");
        params.setSex(2);
        membersAuth3Manager.createAuth3AndUpdatePhone(params);
    }

    @Test
    public void testUpdatePhoneService(){
        PhoneBindParams params = new PhoneBindParams();
        params.setSex(1);
        params.setPhone("15999999999");
        params.setCode("4561");
        membersAuth3Service.updatePhone(params);
    }

    @Test
    public void testGetUnionIdByUids(){
        List<String> list = new ArrayList<>();
        list.add("92bef5e9");
        list.add("d0985d86");
        membersAuth3Service.getUnionIdByUids(list);
    }
}
