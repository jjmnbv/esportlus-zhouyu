package com.kaihei.esportingplus.api.vo;

import java.io.Serializable;

/**
 * @author zhangfang
 */
public class RedisGameAcrossZone implements Serializable {

    private static final long serialVersionUID = -5097156955016583138L;
    /** 跨区code **/
    private Integer zoneAcrossCode;
    /** 跨区名称 **/
    private String zoneAcrossName;

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
