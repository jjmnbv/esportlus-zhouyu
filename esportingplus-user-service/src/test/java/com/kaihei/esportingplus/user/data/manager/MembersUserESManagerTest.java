package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.esportingplus.user.BaseTest;
import com.kaihei.esportingplus.user.domain.entity.MembersUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户信息ES保存查询测试类
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/25 15:01
 */
public class MembersUserESManagerTest extends BaseTest {

    @Autowired
    private MembersUserESManager membersUserESManager;

    @Test
    public void test() {
        MembersUser membersUser = new MembersUser();
        membersUser.setUid("8965644");
        membersUser.setPhone("412547851");
        membersUserESManager.saveMembersUserES(membersUser);
        membersUserESManager.getMembersUserES("8965644");
    }

}
