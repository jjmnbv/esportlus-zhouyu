package com.kaihei.esportingplus.user.api.params;

import java.util.List;

public class UserGameRoleAcrossDataQueryParams extends UserGameRoleAcrossQueryParams {
    private List<Integer> zoneSmallList;

    public List<Integer> getZoneSmallList() {
        return zoneSmallList;
    }

    public void setZoneSmallList(List<Integer> zoneSmallList) {
        this.zoneSmallList = zoneSmallList;
    }
}
