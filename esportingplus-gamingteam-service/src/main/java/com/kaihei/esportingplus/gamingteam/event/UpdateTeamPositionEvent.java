package com.kaihei.esportingplus.gamingteam.event;

import com.kaihei.esportingplus.common.event.Event;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 更新车队信息时的事件通知参数
 *
 * @author zhangfang
 */
public class UpdateTeamPositionEvent implements Event {
    private Long teamId;
    private String sequence;
    private Integer actuallyPositionCount;
    private List<String> uids;
    public UpdateTeamPositionEvent() {
    }

    public UpdateTeamPositionEvent(Long teamId, String sequence,
            Integer actuallyPositionCount,List<String> uids) {
        this.teamId = teamId;
        this.sequence = sequence;
        this.actuallyPositionCount = actuallyPositionCount;
        this.uids=uids;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Integer getActuallyPositionCount() {
        return actuallyPositionCount;
    }

    public void setActuallyPositionCount(Integer actuallyPositionCount) {
        this.actuallyPositionCount = actuallyPositionCount;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<String> getUids() {
        return uids;
    }

    public void setUids(List<String> uids) {
        this.uids = uids;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
