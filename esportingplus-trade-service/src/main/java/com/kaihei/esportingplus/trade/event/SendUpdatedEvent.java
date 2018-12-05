package com.kaihei.esportingplus.trade.event;

import com.kaihei.esportingplus.common.event.Event;

public class SendUpdatedEvent implements Event {

    private String teamSequence;

    public SendUpdatedEvent() {
    }

    public SendUpdatedEvent(String teamSequence) {
        this.teamSequence = teamSequence;
    }

    public String getTeamSequence() {
        return teamSequence;
    }

    public void setTeamSequence(String teamSequence) {
        this.teamSequence = teamSequence;
    }
}
