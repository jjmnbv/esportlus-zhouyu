package com.kaihei.esportingplus.user.api.vo;

import java.io.Serializable;

public class SecondRaidVo implements Serializable {

    private static final long serialVersionUID = 8543889765785930143L;
    private Integer raidCode;
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
