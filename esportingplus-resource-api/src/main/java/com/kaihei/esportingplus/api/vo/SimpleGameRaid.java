package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

public class SimpleGameRaid implements Serializable {

    private static final long serialVersionUID = -4703909835767901767L;
    /** 副本code **/
    private Integer raidCode;
    /** 副本名称 **/
    private String raidName;

    public Integer getRaidCode() {
        return raidCode;
    }

    public void setRaidCode(Integer raidCode) {
        this.raidCode = raidCode;
    }

    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }
}
