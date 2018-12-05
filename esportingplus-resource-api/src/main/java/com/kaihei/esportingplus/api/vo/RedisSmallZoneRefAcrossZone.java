package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

public class RedisSmallZoneRefAcrossZone implements Serializable {

    private static final long serialVersionUID = -3644940804538033745L;
    private Integer zoneBigCode;
    private String zoneBigName;
    private Integer zoneAcrossCode;
    private String zoneAcrossName;

    public Integer getZoneBigCode() {
        return zoneBigCode;
    }

    public void setZoneBigCode(Integer zoneBigCode) {
        this.zoneBigCode = zoneBigCode;
    }

    public String getZoneBigName() {
        return zoneBigName;
    }

    public void setZoneBigName(String zoneBigName) {
        this.zoneBigName = zoneBigName;
    }

    public Integer getZoneAcrossCode() {
        return zoneAcrossCode;
    }

    public void setZoneAcrossCode(Integer zoneAcrossCode) {
        this.zoneAcrossCode = zoneAcrossCode;
    }

    public String getZoneAcrossName() {
        return zoneAcrossName;
    }

    public void setZoneAcrossName(String zoneAcrossName) {
        this.zoneAcrossName = zoneAcrossName;
    }
}
