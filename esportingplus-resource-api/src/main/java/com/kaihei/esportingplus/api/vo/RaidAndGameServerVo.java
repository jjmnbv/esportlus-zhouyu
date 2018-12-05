package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;
import java.util.List;

public class RaidAndGameServerVo implements Serializable {

    private static final long serialVersionUID = 4458666456975046382L;
    private List<SimpleGameRaid> raids;
    private List<RedisGameBigZone> servers;

    public List<SimpleGameRaid> getRaids() {
        return raids;
    }

    public void setRaids(List<SimpleGameRaid> raids) {
        this.raids = raids;
    }

    public List<RedisGameBigZone> getServers() {
        return servers;
    }

    public void setServers(List<RedisGameBigZone> servers) {
        this.servers = servers;
    }
}
