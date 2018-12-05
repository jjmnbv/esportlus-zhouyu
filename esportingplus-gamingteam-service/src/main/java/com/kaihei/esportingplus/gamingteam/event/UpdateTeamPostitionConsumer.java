package com.kaihei.esportingplus.gamingteam.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.kaihei.esportingplus.common.event.EventConsumer;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.gamingteam.api.params.TeamImCmdMsgBaseParams;
import com.kaihei.esportingplus.gamingteam.data.manager.ImService;
import com.kaihei.esportingplus.gamingteam.data.repository.TeamRepository;
import com.kaihei.esportingplus.gamingteam.domain.entity.Team;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 更新车队信息时事件消费
 *
 * @author zhangfang
 */
@Component
public class UpdateTeamPostitionConsumer extends EventConsumer{
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ImService imService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateTeamPostitionConsumer.class);

    /**
     * 将车队信息缓存到 redis 中
     */
    @Subscribe
    @AllowConcurrentEvents
    @Transactional(rollbackFor = Exception.class)
    public void updateTeamPostition(UpdateTeamPositionEvent event) {
        LOGGER.info("更新车队位置数，车队序列号:{}，实际位置数修改为:{}", event.getSequence(),
                event.getActuallyPositionCount());
        Team team = new Team();
        team.setId(event.getTeamId());
        team.setActuallyPositionCount(event.getActuallyPositionCount());
        //发送车队未知数修改通知
        TeamImCmdMsgBaseParams params = new TeamImCmdMsgBaseParams();
        params.setReciever(Arrays.asList(ObjectTools.covertToString(event.getSequence())));
        params.setTeamSequence(event.getSequence());
        params.setToUsers(event.getUids());
        imService.changeTeamCount(params);
        //位置数修改入库
        teamRepository.updateByPrimaryKeySelective(team);

    }
}
