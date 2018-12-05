package com.kaihei.esportingplus.user.api.vo;

import java.util.List;

public class CertRoleWithJoinRaidVo extends UserGameBaseRoleInfoVo {

    private List<SecondRaidVo> raids;

    public List<SecondRaidVo> getRaids() {
        return raids;
    }

    public void setRaids(List<SecondRaidVo> raids) {
        this.raids = raids;
    }
}
