package com.kaihei.esportingplus.gamingteam.data.manager.core.scene;

import org.springframework.stereotype.Component;

@Component
public class DismissTeamScene implements Scene {

    @Override
    public String getName() {
        return "解散车队场景";
    }
}
