package com.kaihei.esportingplus;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenPointGainConfigValueVO;
import com.kaihei.esportingplus.common.enums.UserIdentityEnum;
import com.kaihei.esportingplus.common.event.EventBus;
import com.kaihei.esportingplus.common.thread.pool.ThreadPoolManager;
import com.kaihei.esportingplus.trade.domain.entity.OrderItemTeamPVPFree;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Before;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes= TradeServiceApplication.class)
public class LogicTest
{

    @Before
    public void befor(){
        EventBus.create(ThreadPoolManager.INSTANCE.getEventBusExecutor());
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void queueTest(){
        ConcurrentLinkedQueue a = new ConcurrentLinkedQueue();
        ConcurrentLinkedDeque a1 = new ConcurrentLinkedDeque();
        LinkedBlockingQueue a2 = new LinkedBlockingQueue();
        AtomicReference<Object> b = new AtomicReference<>();
        CopyOnWriteArraySet c = new CopyOnWriteArraySet();
        a.add(1);
        a.add(2);
        a.add(2);
        a1.add(1);
        a1.add(2);
        a1.add(2);
        a2.add(1);
        a2.add(2);
        a2.add(2);
        b.lazySet("1");
        b.lazySet("2");
        b.lazySet("2");
        c.add(1);
        c.add(2);
        c.add(2);
        System.out.println(a);
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(b);
        System.out.println(c);
    }

    @Test
    public void chickenPoitTest(){
        ChickenPointGainConfigValueVO vo = new ChickenPointGainConfigValueVO();
        vo.setGameDanId(1);
        vo.setValue(5);
        ChickenPointGainConfigValueVO vo2 = new ChickenPointGainConfigValueVO();
        vo2.setGameDanId(2);
        vo2.setValue(6);
        ChickenPointGainConfigValueVO vo3 = new ChickenPointGainConfigValueVO();
        vo3.setGameDanId(3);
        vo3.setValue(7);
        List<ChickenPointGainConfigValueVO> danConfigValues = Arrays.asList(vo,vo2,vo3);

        Map<Integer, List<OrderItemTeamPVPFree>> leaderAndBoss = new HashMap<>();
        OrderItemTeamPVPFree free = new OrderItemTeamPVPFree();
        free.setGameDanId(1);
        OrderItemTeamPVPFree free2 = new OrderItemTeamPVPFree();
        free2.setGameDanId(2);
        OrderItemTeamPVPFree free3 = new OrderItemTeamPVPFree();
        free3.setGameDanId(3);
        leaderAndBoss.put(UserIdentityEnum.BOSS.getCode(),Arrays.asList(free,free2,free3));

        //计算老板鸡分总和
        int point = leaderAndBoss
                .get(UserIdentityEnum.BOSS.getCode()).stream()
                .mapToInt(m-> danConfigValues.parallelStream()
                        .filter(pointGain -> pointGain.getGameDanId().equals(m.getGameDanId()))
                        .mapToInt(ChickenPointGainConfigValueVO::getValue)
                        .findFirst().orElse(0))
                .sum();
        System.out.println(point);
    }
}
