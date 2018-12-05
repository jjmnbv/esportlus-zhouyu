package com.kaihei.esportingplus.gamingteam.listener;

import com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.matching.PVPFreeTeamMatchingHandler;
import com.kaihei.esportingplus.gamingteam.data.manager.pvpfree.matching.PVPFreeTeamMatchingPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *@Description: spring 4.2+,事件驱动监听器,监听容器启动完成后，开启匹配任务
 * 在早期，组件要从Spring事件获知自定义域事件中获取通知，必须实现ApplicationListener接口
 * 并覆写onApplicationEvent方法，这样会对每一个事件都创建一个新类，
 * 想重复造轮子被屌没带脑子就随意发挥
 *@author  Orochi-Yzh
 *@dateTime  2018/11/22 12:08
*/
@Component
public class SystemListener {

    @Autowired
    private PVPFreeTeamMatchingHandler pvpFreeTeamMatchingHandler;

    @Autowired
    private PVPFreeTeamMatchingPool pvpFreeTeamMatchingPool;

    /**
     * ContextStartedEvent ConfigurableApplicationContext.start() 触发
     * ContextRefreshedEvent ApplicationContext初始化结束或者刷新的时候触发.
     * ContextStoppedEvent ConfigurableApplicationContext.stop()触发
     * ContextClosedEvent ApplicationContext close时触发
     * @param event
     */
    @EventListener
    public void startMatchingTask(ContextRefreshedEvent event){
        //防止重复执行
        //applicationontext和使用MVC之后的webApplicationontext会两次调用onApplicationEvent方法
        if(event.getApplicationContext().getParent().getParent() == null){
            //先把已经匹配的用户加载到内存
            pvpFreeTeamMatchingPool.pickMatchingsIntoPool();
            //再开始匹配任务
            pvpFreeTeamMatchingHandler.startMatching();
        }
    }
}
