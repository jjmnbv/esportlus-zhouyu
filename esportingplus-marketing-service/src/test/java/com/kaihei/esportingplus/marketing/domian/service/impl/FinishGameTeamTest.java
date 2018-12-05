package com.kaihei.esportingplus.marketing.domian.service.impl;

import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberVO;
import com.kaihei.esportingplus.marketing.MarketingServiceApplication;
import com.kaihei.esportingplus.marketing.api.event.TeamFinishOrderEvent;
import com.kaihei.esportingplus.marketing.api.event.TeamMember;
import com.kaihei.esportingplus.marketing.api.event.UserEventHandler;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: chen.junyong
 * @Date: 2018-12-04 10:35
 * @Description:
 */
@SpringBootTest(classes = MarketingServiceApplication.class)
@RunWith(SpringRunner.class)
public class FinishGameTeamTest {

    {
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }

    @Resource(name = "teamFreeFinishEventHandler")
    private UserEventHandler teamFreeFinishEventHandler;

    @Test
    public void testFinishedGameTeam() {
        TeamFinishOrderEvent event = new TeamFinishOrderEvent();
        event.setTeamStatus(1);
        event.setGameResultCode(1);
        event.setTeamSequence("12332211");

        List<TeamMember> members = new ArrayList<>();
        TeamMember vo1 = new TeamMember();
        TeamMember vo2 = new TeamMember();
        vo1.setBaojiLevel(0);
        vo1.setUid("62ecdfc6");
        vo1.setUserIdentity(1);
        vo2.setBaojiLevel(0);
        vo2.setUid("2ceee331");
        vo2.setUserIdentity(0);
        members.add(vo1);
        members.add(vo2);
        event.setTeamMemberVOS(members);
        teamFreeFinishEventHandler.handle(event);
    }
}
