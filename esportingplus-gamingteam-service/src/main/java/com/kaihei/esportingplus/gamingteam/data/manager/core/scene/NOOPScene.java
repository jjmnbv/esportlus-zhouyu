package com.kaihei.esportingplus.gamingteam.data.manager.core.scene;

import org.springframework.stereotype.Component;

@Component
public class NOOPScene implements Scene {

    @Override
    public String getName() {
        return "无操作、默认场景";
    }
}
